package awsgcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import curefull.healthapp.MainActivity;
import curefull.healthapp.R;
import utils.NotificationUtils;

import static utils.CheckNetworkState.context;

/*
 * This service is designed to run in the background and receive messages from gcm. If the app is in the foreground
 * when a message is received, it will immediately be posted. If the app is not in the foreground, the message will be saved
 * and a notification is posted to the NotificationManager.
 */
public class MessageReceivingService extends Service {
    private GoogleCloudMessaging gcm;
    private SharedPreferences sharedPreferencesUserLogin;

    public void onCreate() {
        super.onCreate();
        final String preferences = getString(R.string.preferences);
        // In later versions multi_process is no longer the default
        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
        sharedPreferencesUserLogin = getSharedPreferences("Login", 0);
        if (sharedPreferencesUserLogin.getBoolean(getString(R.string.first_launch), true)) {
            register();
            sharedPreferencesUserLogin.edit().putBoolean(getString(R.string.first_launch), false).commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected static void saveToLog(Bundle extras, Context context) {
        String message = getMessage(extras);
//        for (String key : extras.keySet()) {
//
//            String line = String.format("%s=%s", key, extras.getString(key));
//            message += (line + "\n");
//            Log.e("key"," "+key);
//        }

        if (!message.equalsIgnoreCase("")) {
            try {
                JSONObject jsonObject = new JSONObject(message);
                NotificationUtils notificationUtils = new NotificationUtils(context);
                notificationUtils.allGetNotfication(jsonObject.getString("name"), jsonObject.getString("text"), jsonObject.getString("typeId"), jsonObject.getString("type"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//        postNotification(new Intent(context, MainActivity.class), context);
    }


//    protected static void postNotification(Intent intentAction, Context context) {
//        final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentAction, Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL);
//        final Notification notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_menu_share)
//                .setContentTitle("Message Received!")
//                .setContentText("")
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .getNotification();
//
//        mNotificationManager.notify(R.string.notification_number, notification);
//    }

    private void register() {
        new AsyncTask() {
            protected Object doInBackground(final Object... params) {
                String token;
                try {
                    token = InstanceID.getInstance(MessageReceivingService.this)
                            .getToken(getString(R.string.project_number), GoogleCloudMessaging.INSTANCE_ID_SCOPE);

//                    Log.e("regId"," "+regId);
//                    token = gcm.register(getString(R.string.project_number));
                    String android_id = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    sharedPreferencesUserLogin.edit()
                            .putString("tokenid", token).commit();
//                    Log.e("tokenid",""+token);
                    sharedPreferencesUserLogin.edit()
                            .putString("android_id", android_id).commit();
                } catch (IOException e) {
                }
                return true;
            }
        }.execute(null, null, null);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }


    public static String getMessage(Bundle data) {
        // If a push notification is sent as plain text, then the message appears in "default".
        // Otherwise it's in the "message" for JSON format.
        return data.containsKey("default") ? data.getString("default") : data.getString(
                "message", "");
    }


}