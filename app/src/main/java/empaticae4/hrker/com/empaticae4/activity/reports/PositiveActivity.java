package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
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
import empaticae4.hrker.com.empaticae4.main.MainActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

// This is the Positive Response Activity

public class PositiveActivity extends Activity {


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
        long tempTime = Calendar.getInstance().getTimeInMillis();
        long tempTime2 = mCachedReportData.getStartTime().getTimeInMillis();
        return (tempTime - tempTime2);

    }

    private void finalizeReport() {

        // Set Duration of Current Report
        mCachedReportData.setDuration(getReportDuration());
        int duration = (int) ((mCachedReportData.getDuration() / 1000) % 60);

        // Set Current Time String: timeStamp
        cal = new Time(Time.getCurrentTimezone());
        cal.setToNow();
        String currentTime = cal.month + "/" + cal.monthDay + "/" + cal.year + "/" + cal.format("%k:%M:%S");
        String timeStamp = "log," + currentTime + "," + "report_type," +
                mCachedReportData.getReportType()+ "," + "temp: 00," + duration + "\n";
        // Current timeStamp format:

        // Set Data String: rowData
        String rowData = ",answer1," + Integer.toString(mCachedReportData.getAnswer1()) + ",I: " +
                mCachedReportData.getIntensity() + "\n," +
                "Positive Event"+ ",,,," + etResponse.getText().toString() + ",\n";
        /* Current data format:
        log | 8/18/2015/23:56:19 | report_type | RT | temp:00 | 245424
            | answer1            | #           | I:#|
        *   | Positive Event     |             |    |         | TEXT
        * */

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
            Toast.makeText(PositiveActivity.this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }

        mPrefs.setReportResponseCache(mCachedReportData);
    }

    private void openContinueAlert() {

        if (etResponse.getText().toString() == "") {
            Toast.makeText(getApplicationContext(), "Please Enter a Response", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PositiveActivity.this);
            alertDialogBuilder.setTitle("");
            alertDialogBuilder.setMessage("Glad to hear it! \nKeep up the good work!");

            // set positive button: Finish
            alertDialogBuilder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    // save DATA before exiting
                    finalizeReport();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Your response has been recorded", Toast.LENGTH_SHORT).show();
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
                startActivity(i);
                Toast.makeText(getApplicationContext(), "You have quit your report", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_response2, menu);
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
