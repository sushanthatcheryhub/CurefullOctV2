package stepcounter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import curefull.healthapp.R;
import fragment.healthapp.FragmentLandingPage;

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


    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("", "steps received in service:" + msg.what);
            Log.e("", "steps arg in service: " + msg.arg1);
            if (preferences.getBoolean("isDestroy", false)) {
                Log.e("check", "check:" + "isdes");
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
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This always shows up in the notifications area when this Service is running.
        // TODO: String localization
        Notification notification = getNotification();
        startForeground(NOTIF_ID, notification);
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
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        Log.e("", "steps in service step:" + numSteps);
        Notification notification = getNotification();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, notification);


        try {
            if (preferences.getBoolean("isDestroy", false)) {
                Log.e("check", "check:" + "isdes");
            } else {
                Message message = new Message();
                message.what = MSG_SET_VALUE;
                message.arg1 = numSteps;
                mMessenger.send(message);
            }

        } catch (RemoteException e) {
            Log.e("", "steps error:" + e.getMessage());
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
}