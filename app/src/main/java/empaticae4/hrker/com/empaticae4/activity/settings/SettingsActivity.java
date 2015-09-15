package empaticae4.hrker.com.empaticae4.activity.settings;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.activity.reports.ReportActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;

public class SettingsActivity extends Activity {

    private AppSharedPrefs mPrefs;

    private Button resetButton;
    private TextView sp1, sp2, sp3, sp4, sp5, sp6, sp7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init() {

        mPrefs = new AppSharedPrefs(SettingsActivity.this);

        resetButton = (Button) findViewById(R.id.resetPrefs);
        sp1 = (TextView)findViewById(R.id.sp1);
        sp1.setText("init_custom_negative_mood: " + mPrefs.getInitCustomNegativeMood());
        sp2 = (TextView)findViewById(R.id.sp2);
        sp3 = (TextView)findViewById(R.id.sp3);
        sp4 = (TextView)findViewById(R.id.sp4);
        sp5 = (TextView)findViewById(R.id.sp5);
        sp6 = (TextView)findViewById(R.id.sp6);
        sp7 = (TextView)findViewById(R.id.sp7);


        /* TESTING BUTTON TO RESET SHARED PREFS*/
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrefs.wrapUp();
                Toast.makeText(SettingsActivity.this, "SharedPref has been reset", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });

    }

    // Notifications
    public void sendNotification(View v) {

        Vibrator vNoti = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        vNoti.vibrate(500);

        Intent notificationIntent = new Intent(this, ReportActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_drawer)
                .setAutoCancel(true)
                .setContentTitle("Nudge from Your Empatica")
                .setContentText("Would you like to make a report? \n (swipe to dismiss)")
                .addAction(R.drawable.ic_drawer, "Sure", contentIntent);

        builder.setContentIntent(contentIntent);

        // Create semi-unique notification ID
        long time = new Date().getTime();
        String tempStr = String.valueOf(time);
        String last4Str = tempStr.substring(tempStr.length() - 5);
        int notificationID = Integer.valueOf(last4Str);

        // Push notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // hide the notification after its selected
        //builder.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

        manager.notify(notificationID, builder.build());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
