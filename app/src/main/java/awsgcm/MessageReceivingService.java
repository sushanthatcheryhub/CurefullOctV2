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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import curefull.healthapp.MainActivity;
import curefull.healthapp.R;
import utils.NotificationUtils;

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
        SharedPreferences savedValues = PreferenceManager.getDefaultSharedPreferences(this);
        if (savedValues.getBoolean(getString(R.string.first_launch), true)) {
            register();
            SharedPreferences.Editor editor = savedValues.edit();
            editor.putBoolean(getString(R.string.first_launch), false);
            editor.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected static void saveToLog(Bundle extras, Context context) {
        String message = "";
        for (String key : extras.keySet()) {
            String line = String.format("%s=%s", key, extras.getString(key));
            message += (line + "\n");
        }
        Log.e("message", " " + message);
        String[] newMeassage = message.split("\n");
        String wow = newMeassage[3];
        Log.e("wow", " " + wow);
        String newString = wow.replace("default=", "");
        Log.e("newString", " " + newString);
        try {
            JSONObject jsonObject = new JSONObject(newString);
            NotificationUtils notificationUtils = new NotificationUtils(context);
            notificationUtils.notificationWaterInTake(jsonObject.getString("name"), jsonObject.getString("text"));

        } catch (JSONException e) {
            e.printStackTrace();
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
                    token = gcm.register(getString(R.string.project_number));
                    String android_id = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    sharedPreferencesUserLogin.edit()
                            .putString("tokenid", token).commit();
                    sharedPreferencesUserLogin.edit()
                            .putString("android_id", android_id).commit();
                    Log.e("android_id ", android_id);
                    Log.e("registrationId ", token);
                } catch (IOException e) {
                    Log.e("Registration Error", e.getMessage());
                }
                return true;
            }
        }.execute(null, null, null);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }


}