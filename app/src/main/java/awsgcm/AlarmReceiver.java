package awsgcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Curefull on 02-02-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("recevice", "recevice");
        Log.e("recevice", "recevice");
        String action = intent.getAction();
        if ("Water".equalsIgnoreCase(action)) {
            Log.e("shuffTest", "Pressed YES");
        } else if ("Not".equalsIgnoreCase(action)) {
            Log.e("shuffTest", "Pressed NO");
        } else if ("Skip".equalsIgnoreCase(action)) {
            Log.e("shuffTest", "Pressed MAYBE");
        }
    }
}
