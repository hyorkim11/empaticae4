package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.activity.settings.AlarmReceiver;
import empaticae4.hrker.com.empaticae4.main.MainActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

// This is the Positive Response Activity

public class PositiveActivity extends Activity {


    private long tempTime;
    private EditText etResponse;
    private BootstrapButton bContinue, bCancel;
    private Time cal;

    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positive);
        init();
    }

    private void init() {

        // Capture time in millis as soon as activity begins
        tempTime = Calendar.getInstance().getTimeInMillis();

        mPrefs = new AppSharedPrefs(PositiveActivity.this);
        mCachedReportData = mPrefs.getReportResponseCache();

        etResponse = (EditText)findViewById(R.id.etResponse);
        bContinue = (BootstrapButton)findViewById(R.id.bContinue);
        bCancel = (BootstrapButton)findViewById(R.id.bCancel);

        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openContinueAlert();
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCancelAlert();
            }
        });

    }

    private long getReportDuration() {

        /* DATE FORMATTER
        duration = endTime - startTime;
        temp = (new SimpleDateFormat("mm:ss:SSS")).format(new Date(duration));
        spEditor.putString("Report_duration", temp).commit();
        */
        long tempT = Calendar.getInstance().getTimeInMillis();
        long tempTime2 = mCachedReportData.getStartTime().getTimeInMillis();
        return (tempT - tempTime2);

    }

    private void finalizeReport() {

        mCachedReportData.setDuration_6(getPageDuration());

        // Set Duration of Current Report
        mCachedReportData.setDuration(getReportDuration());
        //int duration = (int) ((mCachedReportData.getDuration() / 1000) % 60);
        float duration = mCachedReportData.getDuration();

        // Set Current Time String: timeStamp
        cal = new Time(Time.getCurrentTimezone());
        cal.setToNow();
        String currentTime = (cal.month+1) + "/" + cal.monthDay + "/" + cal.year + "/" + cal.format("%k:%M:%S");
        String timeStamp = mCachedReportData.getUserID() + "," + currentTime + "," +
                mCachedReportData.getReportType() + "," + duration + "," +
                mCachedReportData.getEDA() + "\n";

        // Set Data String: rowData
        String rowData = ",answer1," + Integer.toString(mCachedReportData.getAnswer1()) + "," + Long.toString(mCachedReportData.getDuration_1()) + "," + mCachedReportData.getIntensity() + "\n" +
                ",answer2," + Integer.toString(mCachedReportData.getAnswer2()) + "," + Long.toString(mCachedReportData.getDuration_2()) + "\n" +
                ",answer3," + Integer.toString(mCachedReportData.getAnswer3()) + "," + Long.toString(mCachedReportData.getDuration_3()) + "\n" +
                ",answer4," + Integer.toString(mCachedReportData.getAnswer4()) + "," + Long.toString(mCachedReportData.getDuration_4()) + "\n" +
                ",answer5," + Integer.toString(mCachedReportData.getAnswer5()) + "," + Long.toString(mCachedReportData.getDuration_5()) + "\n" +
                ",answer6,," + Long.toString(mCachedReportData.getDuration_6()) + "\n";

        String finalLog = timeStamp + rowData;

        File file = null;
        File root = Environment.getExternalStorageDirectory();

        if (root.canWrite()) {

            File dir = new File(root.getAbsolutePath() + "/mtmData");
            dir.mkdirs();
            file = new File(dir, "userData.csv");
            FileOutputStream out = null;

            try {
                out = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(finalLog.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(PositiveActivity.this,
                    "External Storage Can't Be Accessed",
                    Toast.LENGTH_SHORT).show();
        }

        mPrefs.setReportResponseCache(mCachedReportData);
    }

    private void openContinueAlert() {

        if (etResponse.getText().toString() == "") {
            Toast.makeText(getApplicationContext(),
                    "Please Enter a Response", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PositiveActivity.this);
            alertDialogBuilder.setTitle("");
            alertDialogBuilder.setMessage("Great Job,\nKeep up the good work!");

            // set positive button: Finish
            alertDialogBuilder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    // save DATA before exiting
                    finalizeReport();
                    registerAlarm();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Your response has been recorded",
                            Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void openCancelAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PositiveActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Are you sure you want to quit?");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(),
                        "You have quit your report",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // set negative button: No
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private long getPageDuration() {

        long tempTime2 = Calendar.getInstance().getTimeInMillis();
        return (tempTime2 - tempTime);

    }

    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PositiveActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Are you sure you want to quit?");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                finish();
                Toast.makeText(getApplicationContext(),
                        "You have quit your report", Toast.LENGTH_SHORT).show();
            }
        });

        // set negative button: No
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {

        openAlert();
    }

    @Override
    protected void onResume() {

        tempTime = Calendar.getInstance().getTimeInMillis();

        super.onResume();
    }

    @Override
    protected void onPause() {

        mPrefs.setReportResponseCache(mCachedReportData);
        super.onPause();

    }

    private void registerAlarm(){

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm_type", 0);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // INACTIVITY ALARM CURRENTLY SET TO 24 hrs
        alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + 86400000, alarmIntent);

    }


}
