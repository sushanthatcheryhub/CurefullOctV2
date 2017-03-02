package stepcounter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import asyns.JsonUtilsObject;
import awsgcm.AlarmReceiver;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentLandingPage;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;

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
        updateTimeOnEachSecond();
        initSensor();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This always shows up in the notifications area when this Service is running.
        // TODO: String localization
//        Notification notification = getNotification();
//        startForeground(NOTIF_ID, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        sensorManager.unregisterListener(this);
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
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

    private void initSensor() {
        // Get an instance of the SensorManager
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        numSteps = preferences.getInt("stepsIn", 0);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new SimpleStepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void step(long timeNs) {
//        numSteps = preferences.getInt("stepsIn", 0);
        numSteps++;
//        Notification notification = getNotification();
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(NOTIF_ID, notification);


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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }

    }


    public void updateTimeOnEachSecond() {
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
                        Toast.makeText(MessengerService.this, "" + updateTime(hrs, min), Toast.LENGTH_SHORT).show();
                        // display toast
                        if (updateTime(hrs, min).equalsIgnoreCase("12:00 am")) {
                            double wirght = 0;
                            if (preferences.getString("kg", "").equalsIgnoreCase("0") || preferences.getString("kg", "").equalsIgnoreCase(null) || preferences.getString("kg", "").equalsIgnoreCase("")) {
                                wirght = 40;
                            } else {
                                wirght = Double.parseDouble(preferences.getString("kg", ""));
                            }
                            numSteps = 0;
                            double i2 = Utils.getCaloriesBurnt((wirght * 2.20462), numSteps);
                            preferences.edit().putInt("stepsIn", 0).commit();
                            Intent intent = new Intent();
                            intent.setAction("steps");
                            intent.putExtra("stepsCount", "" + 0);
                            intent.putExtra("caloriesBurnt", "" + new DecimalFormat("###.###").format(i2));
                            intent.putExtra("waterin", "" + preferences.getString("waterin", ""));
                            intent.putExtra("newTime", "11:00 pm");
                            sendBroadcast(intent);
                        }
//                        else if (updateTime(hrs, min).equalsIgnoreCase("8:00 am")) {
//                            double wirght = 0;
//                            if (preferences.getString("kg", "").equalsIgnoreCase("0") || preferences.getString("kg", "").equalsIgnoreCase(null) || preferences.getString("kg", "").equalsIgnoreCase("")) {
//                                wirght = 40;
//                            } else {
//                                wirght = Double.parseDouble(preferences.getString("kg", ""));
//                            }
//                            double i2 = Utils.getCaloriesBurnt((wirght * 2.20462), numSteps);
//                            Intent intent = new Intent();
//                            intent.setAction("steps");
//                            intent.putExtra("stepsCount", "" + numSteps);
//                            intent.putExtra("caloriesBurnt", "" + new DecimalFormat("###.###").format(i2));
//                            intent.putExtra("waterin", "" + preferences.getString("waterin", ""));
//                            intent.putExtra("newTime", "11:00 pm");
//                            sendBroadcast(intent);
//                        }
//                        else if (updateTime(hrs, min).equalsIgnoreCase("12:00 pm")) {
//                            double wirght = 0;
//                            if (preferences.getString("kg", "").equalsIgnoreCase("0") || preferences.getString("kg", "").equalsIgnoreCase(null) || preferences.getString("kg", "").equalsIgnoreCase("")) {
//                                wirght = 40;
//                            } else {
//                                wirght = Double.parseDouble(preferences.getString("kg", ""));
//                            }
//                            double i2 = Utils.getCaloriesBurnt((wirght * 2.20462), numSteps);
//                            Intent intent = new Intent();
//                            intent.setAction("steps");
//                            intent.putExtra("stepsCount", "" + numSteps);
//                            intent.putExtra("caloriesBurnt", "" + new DecimalFormat("###.###").format(i2));
//                            intent.putExtra("waterin", "" + preferences.getString("waterin", ""));
//                            intent.putExtra("newTime", "11:00 pm");
//                            sendBroadcast(intent);
//                        } else if (updateTime(hrs, min).equalsIgnoreCase("8:00 pm")) {
//                            double wirght = 0;
//                            if (preferences.getString("kg", "").equalsIgnoreCase("0") || preferences.getString("kg", "").equalsIgnoreCase(null) || preferences.getString("kg", "").equalsIgnoreCase("")) {
//                                wirght = 40;
//                            } else {
//                                wirght = Double.parseDouble(preferences.getString("kg", ""));
//                            }
//                            double i2 = Utils.getCaloriesBurnt((wirght * 2.20462), numSteps);
//                            Intent intent = new Intent();
//                            intent.setAction("steps");
//                            intent.putExtra("stepsCount", "" + numSteps);
//                            intent.putExtra("caloriesBurnt", "" + new DecimalFormat("###.###").format(i2));
//                            intent.putExtra("waterin", "" + preferences.getString("waterin", ""));
//                            intent.putExtra("newTime", "11:00 pm");
//                            sendBroadcast(intent);
//                        }
                        else if (updateTime(hrs, min).equalsIgnoreCase("11:59 +")) {
                            double wirght = 0;
                            if (preferences.getString("kg", "").equalsIgnoreCase("0") || preferences.getString("kg", "").equalsIgnoreCase(null) || preferences.getString("kg", "").equalsIgnoreCase("")) {
                                wirght = 40;
                            } else {
                                wirght = Double.parseDouble(preferences.getString("kg", ""));
                            }
                            double i2 = Utils.getCaloriesBurnt((wirght * 2.20462), numSteps);
                            Intent intent = new Intent();
                            intent.setAction("steps");
                            intent.putExtra("stepsCount", "" + numSteps);
                            intent.putExtra("caloriesBurnt", "" + new DecimalFormat("###.###").format(i2));
                            intent.putExtra("waterin", "" + preferences.getString("waterin", ""));
                            intent.putExtra("newTime", "11:59 pm");
                            sendBroadcast(intent);
                        }

                    }

                });

            }
        }, 0, 1000 * 60);
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