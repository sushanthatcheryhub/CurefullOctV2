package curefull.healthapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import awsgcm.MessageReceivingService;
import stepcounter.MessengerService;

/**
 * Created by Sushant Hatcheryhub on 08-12-2016.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().putBoolean("resetStepFirstTime", true).commit();
            preferences.edit().putBoolean("resetStepReboot", false).commit();
            Intent service = new Intent(context, MessengerService.class);
            context.startService(service);

            Intent service1 = new Intent(context, MessageReceivingService.class);
            context.startService(service1);
        }


    }

}
