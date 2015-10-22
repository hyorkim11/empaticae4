package empaticae4.hrker.com.empaticae4.activity.settings;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.activity.empatica.LiveStreamActivity;
import empaticae4.hrker.com.empaticae4.activity.reports.ReportActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private int tempInt;
    private Intent i;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context).build();

        PendingIntent p = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        tempInt = intent.getExtras().getInt("alarm_type", 0);

        if (tempInt == 0) {
            // 24 HR alarm

            i = new Intent(context, ReportActivity.class);
            i.putExtra("report_type", "IN");

            notification = new NotificationCompat.Builder(context)
                    .setContentTitle("")
                    .setContentText("Would you like to make a report?")
                    .setTicker("Inactivity Notice")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(p)
                    .setAutoCancel(true)
                    .setPriority(2)
                    .build();

        } else if (tempInt == 1) {
            // Reminder to connect to E4
            // remind the user to connect to the E4 whenever

            i = new Intent(context, LiveStreamActivity.class);
            i.putExtra("report_type", "Dev");

            notification = new NotificationCompat.Builder(context)
                    .setContentTitle("E4 Device Reminder")
                    .setContentText("Remember to connect to your E4 device!")
                    .setTicker("Connection Reminder Notice")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(p)
                    .setAutoCancel(true)
                    .setPriority(2)
                    .build();
        } else if (tempInt == 2) {
            // EDAT broken

            i = new Intent(context, ReportActivity.class);
            i.putExtra("report_type", "EDA");

            notification = new NotificationCompat.Builder(context)
                            .setContentTitle("")
                            .setContentText("")
                            .setTicker("EDA Notice")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(p)
                            .setAutoCancel(true)
                            .setPriority(2)
                            .build();

        }

        Vibrator vNoti = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 250 milliseconds
        vNoti.vibrate(250);
        notificationManager.notify(0, notification);



    }
}