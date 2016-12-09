package curefull.healthapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import stepcounter.MessengerService;

/**
 * Created by Sushant Hatcheryhub on 08-12-2016.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, MessengerService.class);
        context.startService(service);

    }

}
