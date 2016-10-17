package utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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


    public void notificationWaterInTake() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(_context, MainActivity.class);
        intent.setAction("Water");
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent, 0);

        Intent intent1 = new Intent(_context, MainActivity.class);
        intent1.setAction("Not");
        intent1.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent2 = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent1, 0);

        // Build notification
        // Actions are just fake
        Bitmap icon2 = BitmapFactory.decodeResource(_context.getResources(),
                R.mipmap.app_icon);
        Notification noti = new Notification.Builder(_context)
                .setContentTitle("Drink Notification")
                .setContentText("Oh, dear! What is wrong? Please drink! You lagging behind with 500ml")
                .setSmallIcon(R.drawable.ic_local_drink_black_18dp).setLargeIcon(icon2)
                .setStyle(new Notification.BigTextStyle().bigText("Water can help against backache! Your intervertebral discs depend on sufficient interstitial fluid for proper function. Drinking enough water helps this happen."))
                .setAutoCancel(true)
                .addAction(R.drawable.places_ic_clear, "Water in Take", pIntent)
                .addAction(R.drawable.places_ic_search, "Not yet", pIntent2).build();
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(1, noti);

    }


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
                .addAction(R.drawable.places_ic_clear, "Show", pIntent)
                .addAction(R.drawable.places_ic_search, "Share", pIntent2).build();
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(2, noti);

    }


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

    public void startNotif() {

        Intent intent = new Intent(_context, MainActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        final PendingIntent pIntent = PendingIntent.getActivity(_context, (int) System.currentTimeMillis(), intent, 0);

        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        // Create the reply action and add the remote input.
        Notification.Action action =
                new Notification.Action.Builder(R.drawable.ic_menu_camera,
                        "Action", pIntent)
                        .addRemoteInput(remoteInput)
                        .build();

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
