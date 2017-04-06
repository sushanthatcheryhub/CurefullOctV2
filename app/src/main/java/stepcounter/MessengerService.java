package stepcounter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import awsgcm.AlarmReceiver;
import curefull.healthapp.R;
import fragment.healthapp.FragmentLandingPage;
import operations.DbOperations;

public class MessengerService extends Service implements StepListener, SensorEventListener {
    /**
     * For showing and hiding our notification.
     */
    /**
     * Keeps track of all current registered clients.
     */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    SharedPreferences preferences;
    /**
     * Holds last value set by a client.
     */
    // int mValue = 0;

    public static final int NOTIF_ID = 1001;

    /**
     * Command to the service to register a client, receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client where callbacks should be sent.
     */
    public static final int MSG_REGISTER_CLIENT = 1;

    /**
     * Command to the service to unregister a client, ot stop receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client as previously given with MSG_REGISTER_CLIENT.
     */
    public static final int MSG_UNREGISTER_CLIENT = 2;


    /**
     * Command to service to set a new value.  This can be sent to the
     * service to supply a new value, and will be sent by the service to
     * any registered clients with the new value.
     */
    public static final int MSG_SET_VALUE = 3;

    public static final int STOP_FOREGROUND = 4;

    boolean activityRunning;
    private Handler mHandler = new Handler();
    private Runnable mUpdate;

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (preferences.getBoolean("isDestroy", false)) {
            } else {
                switch (msg.what) {
                    case MSG_REGISTER_CLIENT:
                        msg.arg1 = numSteps;
                        mClients.add(msg.replyTo);
                        break;
                    case MSG_UNREGISTER_CLIENT:
                        msg.arg1 = numSteps;
                        mClients.remove(msg.replyTo);
                        break;
                    case STOP_FOREGROUND:
                        stopForeground(true);
                        stopSelf();
                        break;
                    case MSG_SET_VALUE:
                        // mValue = msg.arg1;

                        for (int i = mClients.size() - 1; i >= 0; i--) {
                            try {
                                mClients.get(i).send(Message.obtain(null,
                                        MSG_SET_VALUE, numSteps, 0));
                            } catch (RemoteException e) {
                                mClients.remove(i);
                            }
                        }
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }

        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        initSensor();
        updateTimeOnEachSecond();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This always shows up in the notifications area when this Service is running.
        // TODO: String localization
//        Notification notification = getNotification();
        if (Build.VERSION.SDK_INT == 19) {
            initSensor();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        sensorManager.unregisterListener(this);
//        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }

    private Notification getNotification() {
        CharSequence text = getText(R.string.remote_service_started);
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent;
        if (preferences.getBoolean("isDestroy", false)) {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, null), 0);
        } else {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, FragmentLandingPage.class), 0);
        }


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker(text)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Step Count:" + numSteps)
                        .setContentText(text)
                        .setContentIntent(contentIntent);

        return mBuilder.build();
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }


    //write code of pedometer
    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private int newSteps;
    private boolean issenor = false;
    private boolean stepsReal = false;

    private void initSensor() {
        // Get an instance of the SensorManager
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        numSteps = preferences.getInt("stepsIn", 0);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (accel != null) {
//            Toast.makeText(MessengerService.this, "" + "true ", Toast.LENGTH_SHORT).show();
            issenor = true;
            simpleStepDetector = new SimpleStepDetector();
            simpleStepDetector.registerListener(this, MessengerService.this);
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        } else {
//            Toast.makeText(MessengerService.this, "" + "false ", Toast.LENGTH_SHORT).show();
            issenor = false;
            accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            simpleStepDetector = new SimpleStepDetector();
            simpleStepDetector.registerListener(this, MessengerService.this);
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        }

    }

    @Override
    public void step(long timeNs) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Calendar c = Calendar.getInstance();
        final int hrs = c.get(Calendar.HOUR_OF_DAY);
        final int min = c.get(Calendar.MINUTE);
        final int sec = c.get(Calendar.SECOND);

//        Toast.makeText(MessengerService.this, "" + "num " .+ numSteps, Toast.LENGTH_SHORT).show();
        preferences.edit().putBoolean("saveSteps", false).commit();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mUpdate, 3000);
        mUpdate = new Runnable() {
            @Override
            public void run() {
                if (!preferences.getBoolean("saveSteps", false)) {
//                    Toast.makeText(MessengerService.this, "" + "num " + numSteps, Toast.LENGTH_SHORT).show();
                    newSteps = 0;
                    stepsReal = false;
                }

            }
        };
        newSteps++;
        if (newSteps > 5) {
            stepsReal = true;
            if (newSteps == 6) {
                preferences.edit().putInt("stepsIn", (preferences.getInt("stepsIn", 0) + 6)).commit();
                updateTimeOnEachSecond();
            }
        }

        if (updateTime(hrs, min).equalsIgnoreCase("12:05 am")) {
            numSteps = 0;
            preferences.edit().putInt("stepsIn", 0).commit();
            preferences.edit().putString("waterTake", "0").commit();
            preferences.edit().putString("CaloriesCount", "0").commit();
            preferences.edit().putInt("percentage", 0).commit();

        }
        if (preferences.getBoolean("logout", false)) {
            preferences.edit().putInt("stepsIn", 0).commit();
            preferences.edit().putString("waterTake", "0").commit();
            preferences.edit().putString("CaloriesCount", "0").commit();
            preferences.edit().putInt("percentage", 0).commit();
            preferences.edit().putBoolean("logout", false).commit();
        }

        if (stepsReal) {
            numSteps = preferences.getInt("stepsIn", 0);
            numSteps++;
            preferences.edit().putInt("stepsIn", numSteps).commit();

            try {
                if (preferences.getBoolean("isDestroy", false)) {
                } else {
                    Message message = new Message();
                    message.what = MSG_SET_VALUE;
                    message.arg1 = numSteps;
                    mMessenger.send(message);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//                            Toast.makeText(MessengerService.this, "" + "num " + numSteps, Toast.LENGTH_SHORT).show();
        Calendar c1 = Calendar.getInstance();
        final int hrs1 = c1.get(Calendar.HOUR_OF_DAY);
        final int min1 = c1.get(Calendar.MINUTE);

        if (updateTime(hrs1, min1).equalsIgnoreCase("12:05 am")) {
            numSteps = 0;
            preferences.edit().putInt("stepsIn", 0).commit();
            preferences.edit().putString("waterTake", "0").commit();
            preferences.edit().putString("CaloriesCount", "0").commit();
            preferences.edit().putInt("percentage", 0).commit();
        }

        if (preferences.getBoolean("logout", false)) {
            numSteps = preferences.getInt("stepsIn", 0);
            preferences.edit().putString("waterTake", "0").commit();
            preferences.edit().putInt("percentage", 0).commit();
            preferences.edit().putString("CaloriesCount", "0").commit();
            preferences.edit().putBoolean("logout", false).commit();

        }

        if (issenor) {
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                updateTimeOnEachSecond();
                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                Calendar c = Calendar.getInstance();
                final int hrs = c.get(Calendar.HOUR_OF_DAY);
                final int min = c.get(Calendar.MINUTE);

                if (updateTime(hrs, min).equalsIgnoreCase("12:05 am")) {
                    numSteps = 0;
                    preferences.edit().putInt("stepsIn", 0).commit();
                    preferences.edit().putString("waterTake", "0").commit();
                    preferences.edit().putString("CaloriesCount", "0").commit();
                    preferences.edit().putInt("percentage", 0).commit();
                }

                if (preferences.getBoolean("logout", false)) {
                    numSteps = 0;
                    preferences.edit().putInt("stepsIn", 0).commit();
                    preferences.edit().putString("waterTake", "0").commit();
                    preferences.edit().putString("CaloriesCount", "0").commit();
                    preferences.edit().putInt("percentage", 0).commit();
                    preferences.edit().putBoolean("logout", false).commit();
                }

                preferences.edit().putBoolean("saveSteps", false).commit();
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(mUpdate, 3000);
                mUpdate = new Runnable() {
                    @Override
                    public void run() {
                        if (!preferences.getBoolean("saveSteps", false)) {
//                    Toast.makeText(MessengerService.this, "" + "num " + numSteps, Toast.LENGTH_SHORT).show();
                            newSteps = 0;
                        }

                    }
                };
//                newSteps++;
//                if (newSteps > 5) {
//                    if (newSteps == 7) {
//                        preferences.edit().putInt("stepsIn", (preferences.getInt("stepsIn", 0) + 7)).commit();
//                    }
//                }
                float steps = event.values[0];

//                textViewStepCounter.setText((int) steps + "");

                if (preferences.getBoolean("resetStepReboot", true)) {
                    if (preferences.getBoolean("resetStepFirstTime", true)) {
                        preferences.edit().putBoolean("resetStepFirstTime", false).commit();
                        preferences.edit().putInt("resetStep", (int) steps).commit();
                        numSteps = preferences.getInt("resetStep", 0) - preferences.getInt("resetStep", 0);
                        preferences.edit().putInt("firstLogin", preferences.getInt("stepsIn", 0)).commit();
                        if (preferences.getInt("firstLogin", 0) != 0) {
                            numSteps = preferences.getInt("firstLogin", 0);
                        }
                        preferences.edit().putInt("stepsIn", numSteps).commit();
                        try {
                            if (preferences.getBoolean("isDestroy", false)) {
                            } else {
                                Message message = new Message();
                                message.what = MSG_SET_VALUE;
                                message.arg1 = numSteps;
                                mMessenger.send(message);
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {

                        if (preferences.getInt("firstLogin", 0) != 0) {
                            numSteps = (int) steps - preferences.getInt("resetStep", 0);
                            numSteps += preferences.getInt("firstLogin", 0);
//                numSteps = (int) steps;
                            preferences.edit().putInt("stepsIn", numSteps).commit();
                            try {
                                if (preferences.getBoolean("isDestroy", false)) {
                                } else {
                                    Message message = new Message();
                                    message.what = MSG_SET_VALUE;
                                    message.arg1 = numSteps;
                                    mMessenger.send(message);
                                }

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            numSteps = (int) steps - preferences.getInt("resetStep", 0);
//                numSteps = (int) steps;
                            preferences.edit().putInt("stepsIn", numSteps).commit();
                            try {
                                if (preferences.getBoolean("isDestroy", false)) {
                                } else {
                                    Message message = new Message();
                                    message.what = MSG_SET_VALUE;
                                    message.arg1 = numSteps;
                                    mMessenger.send(message);
                                }

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                } else {

                    if (preferences.getBoolean("firstReboot", true)) {
                        preferences.edit().putBoolean("firstReboot", false).commit();
                        preferences.edit().putInt("firstLoginReboot", preferences.getInt("stepsIn", 0)).commit();
                    }
                    numSteps = preferences.getInt("firstLoginReboot", 0) + (int) steps;
                    preferences.edit().putInt("stepsIn", numSteps).commit();
                    try {
                        if (preferences.getBoolean("isDestroy", false)) {
                        } else {
                            Message message = new Message();
                            message.what = MSG_SET_VALUE;
                            message.arg1 = numSteps;
                            mMessenger.send(message);
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }


            }
        } else {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                simpleStepDetector.updateAccel(
                        event.timestamp, event.values[0], event.values[1], event.values[2]);
            }
        }


    }


    public void updateTimeOnEachSecond() {


//        if (preferences.getBoolean("stepFirstTime", true)) {
//            preferences.edit().putBoolean("stepFirstTime", false).commit();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 05);
//            Toast.makeText(MessengerService.this, "set hua" + calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("steps");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
// Wakes up the device in Doze Mode
        } else if (Build.VERSION.SDK_INT >= 19) {
// Wakes up the device in Idle Mode
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
// Old APIs
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
//        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                final int hrs = c.get(Calendar.HOUR_OF_DAY);
                final int min = c.get(Calendar.MINUTE);
                final int sec = c.get(Calendar.SECOND);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
//                        Log.e("hi", "hi" + updateTime(hrs, min));
                        // display toast

                        if (updateTime(hrs, min).equalsIgnoreCase("12:05 am")) {
                            numSteps = 0;
                            preferences.edit().putInt("stepsIn", 0).commit();
                            preferences.edit().putString("waterTake", "0").commit();
                            preferences.edit().putString("CaloriesCount", "0").commit();
                            preferences.edit().putInt("percentage", 0).commit();

                        }


                    }

                });

            }
        }, 0, 1000 * 40);
    }


    private String updateTime(int hours, int mins) {


        int selctHour = hours;

        String timeSet = "";
        if (selctHour > 12) {
            selctHour -= 12;
            timeSet = "pm";
        } else if (selctHour == 0) {
            selctHour += 12;
            timeSet = "am";
        } else if (selctHour == 12) {
            timeSet = "pm";
        } else {
            timeSet = "am";
        }

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(selctHour).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    private String getDateTime() {
        // get date time in custom format
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
        return sdf.format(new Date());
    }
}