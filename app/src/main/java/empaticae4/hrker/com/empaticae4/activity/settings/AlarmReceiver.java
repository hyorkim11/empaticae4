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
import empaticae4.hrker.com.empaticae4.activity.reports.ReportActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Vibrator vNoti = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        vNoti.vibrate(500);

        Intent i = new Intent(context, ReportActivity.class);
        i.putExtra("report_type", "IN");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent p = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Inactivity Notice")
                .setContentText("Would you like to make a report?")
                .setTicker("Nudge from MtM")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(p)
                .setAutoCancel(true)
                .setPriority(2)
                .build();
        notificationManager.notify(0, notification);
    }
}