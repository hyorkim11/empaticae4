package empaticae4.hrker.com.empaticae4.activity.settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.activity.reports.ReportActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class SettingsActivity extends Activity implements View.OnClickListener {

    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;
    private Time cal;

    private Button bUserID, resetButton, sendButton, mResetCSV, mSetCallContact;
    private TextView sp0, sp1, sp2, sp3, sp4, sp5, sp6, sp7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init() {

        mPrefs = new AppSharedPrefs(SettingsActivity.this);
        mCachedReportData = mPrefs.getReportResponseCache();

        bUserID = (Button) findViewById(R.id.bUserID);

        if (mCachedReportData.getUserID() != "Uninitialized") {
            bUserID.setText("ID: " + mCachedReportData.getUserID());
        } else {
            bUserID.setText("Set ID");
        }

        bUserID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserID();
            }
        });
        sendButton = (Button) findViewById(R.id.bSend);
        sendButton.setOnClickListener(this);
        resetButton = (Button) findViewById(R.id.resetPrefs);
        resetButton.setOnClickListener(this);
        mResetCSV = (Button) findViewById(R.id.bResetCSV);
        mResetCSV.setOnClickListener(this);
        mSetCallContact = (Button) findViewById(R.id.bSetCallContact);
        mSetCallContact.setOnClickListener(this);

        sp0 = (TextView) findViewById(R.id.sp0);
        sp1 = (TextView) findViewById(R.id.sp1);
        sp2 = (TextView) findViewById(R.id.sp2);
        sp3 = (TextView) findViewById(R.id.sp3);
        sp4 = (TextView) findViewById(R.id.sp4);
        sp5 = (TextView) findViewById(R.id.sp5);
        sp6 = (TextView) findViewById(R.id.sp6);
        sp7 = (TextView) findViewById(R.id.sp7);

        sp0.setText("init_custom_negative_mood: " + mPrefs.getInitCustomNegativeMood());
        sp1.setText("init_custom_event: " + mPrefs.getInitCustomEvent());
        sp2.setText("init_custom_coolthought: " + mPrefs.getInitCustomCoolthought());
        sp3.setText("init_custom_drinking: " + mPrefs.getInitCustomDrinking());
        sp4.setText("init_custom_goodmove: " + mPrefs.getInitCustomGoodmove());

        sp6.setText("last report duration: " + mCachedReportData.getDuration() + " milliseconds");
        // currently fetches long in milliseconds
        sp7.setText("a1: " + Integer.toString(mCachedReportData.getAnswer1()) + "\n"
                + "a2: " + Integer.toString(mCachedReportData.getAnswer2()) + "\n"
                + "a3: " + Integer.toString(mCachedReportData.getAnswer3()) + "\n"
                + "a4: " + Integer.toString(mCachedReportData.getAnswer4()) + "\n"
                + "a5: " + Integer.toString(mCachedReportData.getAnswer5()) + "\n");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.resetPrefs) {

            mPrefs.wrapUp();
            Toast.makeText(SettingsActivity.this, "SharedPref has been reset", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        } else if (v.getId() == R.id.bSend) {

            sendCSV();
        } else if (v.getId() == R.id.bResetCSV) {
            resetCSV();
        } else if (v.getId() == R.id.bSetCallContact) {
            // user types in name of friend to call
            // set in mPrefs.setCallContact()
            // getNumber(name, context)
        }
    }

    private void sendCSV() {

        cal = new Time(Time.getCurrentTimezone());
        cal.setToNow();
        String currentTime = (cal.month + 1) + "/" + cal.monthDay + "/" + cal.year + "/" + cal.format("%k:%M:%S");

        String timeStamp = "\n" + ",,,,exported:," + currentTime + "," + "\n\n";

        File file = null;
        File root = Environment.getExternalStorageDirectory();

        if (root.canWrite()) {

            File dir = new File(root.getAbsolutePath() + "/mtmData");

            if (!dir.exists()) {

                dir.mkdir();
            }

            file = new File(dir, "userData.csv");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(timeStamp.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(SettingsActivity.this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }

        Uri u1;
        u1 = Uri.fromFile(file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/html");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hyorim@umich.edu"});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MtM - Update");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is a data update email");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        startActivity(Intent.createChooser(sendIntent, "Send An Email"));

    }

    private void resetCSV() {

        File file;
        File root = Environment.getExternalStorageDirectory();

        if (root.canWrite()) {

            File dir = new File(root.getAbsolutePath() + "/mtmData");
            file = new File(dir, "userData.csv");
            file.delete();
            Toast.makeText(SettingsActivity.this, "CSV File Has Been Reset", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SettingsActivity.this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }
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

    private void openUserID() {

        // Create and Build Dialog
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_set_user_id, null);
        builder.setContentView(view);

        final EditText et = (EditText) builder.findViewById(R.id.etUserID);

        Button bContinue = (Button) builder.findViewById(R.id.bContinue);
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().length() == 0) {
                    Toast.makeText(SettingsActivity.this, "Please enter valid ID", Toast.LENGTH_SHORT).show();
                } else {
                    mCachedReportData.setUserID(et.getText().toString());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    Toast.makeText(SettingsActivity.this, "ID set as: " + et.getText().toString(), Toast.LENGTH_SHORT).show();
                    bUserID.setText("ID: " + mCachedReportData.getUserID());
                    builder.dismiss();
                }
            }
        });

        builder.setCanceledOnTouchOutside(true);
        builder.show();

    }

    private String getNumber(String name, Context context) {
        String number = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'%" + name + "%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);

        if (c.moveToFirst()) {
            number = c.getString(0);
        }
        c.close();
        if(number == null)
            number = "Unsaved";
        return number;
    }

}
