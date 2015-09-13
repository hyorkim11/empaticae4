package empaticae4.hrker.com.empaticae4.activity.settings;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SettingsActivity extends Activity {

    public static final String DATAFILE = "userData";
    SharedPreferences sharedP = null;

    private Button resetButton, showCustomFeeling1;
    private TextView sp1, sp2, sp3, sp4, sp5, sp6, sp7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        init();
    }

    private void init() {

        resetButton = (Button) findViewById(R.id.resetPrefs);
        showCustomFeeling1 = (Button) findViewById(R.id.showCustomFeeling1);
        sp1 = (TextView)findViewById(R.id.sp1);
        sp1.setText("positive mood: " + sharedP.getString("Positive_Mood", "Other"));
        sp2 = (TextView)findViewById(R.id.sp2);
        sp2.setText("negative mood: " + sharedP.getString("Negative_Mood", "Other"));
        sp3 = (TextView)findViewById(R.id.sp3);
        sp3.setText("custom negative event: " + sharedP.getString("Custom_negative_event", "Other"));
        sp4 = (TextView)findViewById(R.id.sp4);
        sp4.setText("custom negative mood: " + sharedP.getString("Custom_negative_mood", "Other"));
        sp5 = (TextView)findViewById(R.id.sp5);
        sp5.setText("custom event: " + sharedP.getString("custom_event", "Other"));
        sp6 = (TextView)findViewById(R.id.sp6);
        sp6.setText("custom cool thought: " + sharedP.getString("custom_coolthought", "Other"));
        sp7 = (TextView)findViewById(R.id.sp7);
        sp7.setText("custom drinking strategy: " + sharedP.getString("custom_drinking_strategy", "Other"));


        /* TESTING BUTTON TO RESET SHARED PREFS*/
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences(DATAFILE, Context.MODE_MULTI_PROCESS);
                settings.edit().clear().commit();
                Toast.makeText(SettingsActivity.this, "SharedPref has been reset", Toast.LENGTH_SHORT).show();
            }
        });
        showCustomFeeling1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences fetchCustom = getSharedPreferences(DATAFILE, Context.MODE_MULTI_PROCESS);
                Toast.makeText(SettingsActivity.this, "Custom Feeling 1 is set as: "+ fetchCustom.getString("custom", "N/A"), Toast.LENGTH_SHORT).show();

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
