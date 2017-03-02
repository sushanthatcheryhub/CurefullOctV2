package utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import interfaces.SmsListener;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private static SmsListener mListener;

    public void onReceive(Context context, Intent intent) {
        String senderNum = "";
        String message = "";
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage
                            .createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage
                            .getDisplayOriginatingAddress();
//                    Log.e("phoneNumber", "- " + phoneNumber);
                    senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();
//                    Log.e("message", "- " + message);
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("Phone", senderNum);
//                    editor.putString("Message", message);
//                    editor.commit();
                    if (message.contains("Dear User ,")) {
                        mListener.messageReceived(message);
                    }

                }
            }
//			checkToSend(context, senderNum, message);
        } catch (Exception e) {
//            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}