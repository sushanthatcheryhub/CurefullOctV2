package utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import curefull.healthapp.MainActivity;
import curefull.healthapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Sushant Hatcheryhub on 13-10-2016.
 */

public class NotificationUtils {


    private Context _context;
    public static final String KEY_TEXT_REPLY = "key_text_reply";

    public NotificationUtils(Context context) {
        this._context = context;
    }


    public void allGetNotfication(String name, String text, String typeId, String type) {

        notificationMedicine(name, text, typeId, type);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notificationMedicine(String name, String text, String id, String type) {
        // Prepare intent which is triggered if the
        // notification is selected

//        if (type.equalsIgnoreCase("DOCTOR_FOLLOWUP_REMINDER")) {
//            name = "Dr." + name;
//        }
        Intent intent = new Intent();
        intent.setAction("complete");
        intent.putExtra("perDayDosageDetailsId", id);
        intent.putExtra("type", type);
//        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getBroadcast(_context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent1 = new Intent();
        intent1.setAction("skip");
        intent1.putExtra("perDayDosageDetailsId", id);
        intent1.putExtra("type", type);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent2 = PendingIntent.getBroadcast(_context, (int) System.currentTimeMillis(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intent11 = new Intent(_context, MainActivity.class);
        intent11.setAction("open");
        intent11.putExtra("type", type);
        intent11.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent11.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent1 = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent11, PendingIntent.FLAG_UPDATE_CURRENT);

//        Intent intent2 = new Intent();
//        intent2.setAction("snooze");
//        intent2.putExtra("perDayDosageDetailsId", id);
//        intent.putExtra("type", type);
////        intent2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
////        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
////                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pIntent3 = PendingIntent.getBroadcast(_context, (int) System.currentTimeMillis(), intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        // Actions are just fake
        int color = ContextCompat.getColor(_context, R.color.health_red_drawer);
        Bitmap icon2 = BitmapFactory.decodeResource(_context.getResources(),
                R.mipmap.app_icon);
        Notification noti = new Notification.Builder(_context)
                .setContentTitle(name)
                .setContentText(text)
                .setSmallIcon(R.drawable.notification_small)
                .setColor(color)
                .setLargeIcon(icon2)
//                .setStyle(new Notification.BigTextStyle().bigText("Water can help against backache! Your intervertebral discs depend on sufficient interstitial fluid for proper function. Drinking enough water helps this happen."))
                .setAutoCancel(true)
                .addAction(R.drawable.done, "Done", pIntent)
                .addAction(R.drawable.skip, "Skip", pIntent2)
                .setContentIntent(pIntent1)
                .build();
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(Integer.parseInt(id), noti);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notificationWithImage() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(_context, MainActivity.class);
        intent.setAction("Show");
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intent1 = new Intent(_context, MainActivity.class);
        intent1.setAction("Share");
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent2 = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        // Actions are just fake
        Bitmap icon2 = BitmapFactory.decodeResource(_context.getResources(),
                R.mipmap.app_icon);
        Notification noti = new Notification.Builder(_context)
                .setContentTitle("Big picture notification")
                .setContentText("This is test of big picture notification.")
                .setLargeIcon(icon2)
                .setSmallIcon(R.drawable.ic_menu_share)
                .setAutoCancel(true)
                .setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(_context.getResources(),
                        R.drawable.profile_new)))
                .addAction(R.drawable.water_bottle1_yellow, "Show", pIntent)
                .addAction(R.drawable.delete_icon, "Share", pIntent2).build();
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(2, noti);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notificationWhetherReport(String loc, String contentText, String bigText, String Temp) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);


        // Build notification
        // Actions are just fake

        Notification noti = new Notification.Builder(_context)
                .setContentTitle(Temp.substring(0, 4) + "\u2109" + " " + loc)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_whatshot_black_18dp)
                .setAutoCancel(true)
                .setContentIntent(pIntent).build();
//                .setStyle(new Notification.BigTextStyle().bigText(contentText + "\n" + bigText)).build();
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(0, noti);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void startNotif() {

        Intent intent = new Intent(_context, MainActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        final PendingIntent pIntent = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent, 0);

        String replyLabel = "Reply";
        RemoteInput remoteInput = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel(replyLabel)
                    .build();
        }

        // Create the reply action and add the remote input.
        Notification.Action action =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            action = new Notification.Action.Builder(R.drawable.ic_menu_camera,
                    "Action", pIntent)
                    .addRemoteInput(remoteInput)
                    .build();
        }

        // Build the notification and add the action.
        Notification newMessageNotification =
                new Notification.Builder(_context)
                        .setSmallIcon(R.drawable.ic_menu_manage)
                        .setContentTitle("Title")
                        .setAutoCancel(true)
                        .setContentText("Content")
                        .addAction(action).build();

// Issue the notification.
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        newMessageNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        newMessageNotification.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(0, newMessageNotification);
    }

}
