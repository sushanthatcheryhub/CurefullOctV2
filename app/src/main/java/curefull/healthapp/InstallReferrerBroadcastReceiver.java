package curefull.healthapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Sushant Hatcheryhub on 12-01-2017.
 */

public class InstallReferrerBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent != null)
        {

            Log.e("aaya "," aaaya");
//            String referrerString = intent.getStringExtra(AppConstant.KEY_REFERRER);
//            HashMap<String, String> resolvedReferrerData = AppUtil.resolveReferrerString(referrerString);
//
//            if(resolvedReferrerData != null)
//            {
//                String referrerToken = resolvedReferrerData.get(AppConstant.KEY_REFERRER_TOKEN);
//                String referrerType = resolvedReferrerData.get(AppConstant.KEY_REFERRER_TYPE);
//
//                if (!AppUtil.isStringEmpty(referrerToken) && !AppUtil.isStringEmpty(referrerType))
//                {
//                    PersistentManager.persistReferrerToken(referrerToken);
//                    PersistentManager.persistReferrerType(referrerType);
//                }
//            }
        }
    }
}