package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.main.MainActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class DrinkActivity extends Activity {


    private RadioGroup mForm;
    private RadioButton mInitialOther, mOther;
    private BootstrapButton bCancel, bContinue;
    private String tempString;
    private Time cal;

    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        init();
    }

    private void init() {

        mPrefs = new AppSharedPrefs(DrinkActivity.this);
        mCachedReportData = mPrefs.getReportResponseCache();
        tempString = mPrefs.getInitCustomDrinking();

        mForm = (RadioGroup) findViewById(R.id.form1);
        mForm.setOnCheckedChangeListener(listener1);

        String[] drinking = new String[]{"Drink slowly",
                "Put extra ice in your drink", "Decide when to leave ahead of time",
                "Eat something", "Decide how much to drink ahead of time",
                "Switch between alcoholic & nonalcoholic drinks",
                "Ask a friend to keep tabs on you",
                "Go home with a friend", "Call a cab or Uber",
                "Limit the amount of money you spend on alcohol",
                "Think of the consequences of drinking too much"};
        final RadioButton[] array = new RadioButton[11];
        for (int i = 0; i < 11; i++) {
            array[i] = new RadioButton(this);
            array[i].setText(drinking[i].toString());
            array[i].setId(i + 1);
            mForm.addView(array[i]);
        }

        bCancel = (BootstrapButton) findViewById(R.id.bCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCancelAlert();
            }
        });
        bContinue = (BootstrapButton) findViewById(R.id.bContinue);
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getAnswerChoice() != -1) {

                    mCachedReportData.setAnswer4(getAnswerChoice());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    openFinishAlert();
                } else {
                    Toast.makeText(getApplicationContext(), "Please make a selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mInitialOther = (RadioButton) findViewById(R.id.bInitialOther);
        mOther = (RadioButton) findViewById(R.id.bOther);
        mInitialOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String t = mPrefs.getInitCustomDrinking();

                if (t == "Other") {

                    openCustom();
                } else {

                    mForm.clearCheck();
                    mOther.setChecked(false);
                    mInitialOther.setChecked(true);
                }
            }
        });

        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustom2();
            }
        });

        if (Objects.equals(tempString, "Other")) {
            mInitialOther.setText("Other");
        } else {
            mInitialOther.setText(tempString);
        }

    }

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (checkedId != -1) {

                mInitialOther.setChecked(false);
                mOther.setChecked(false);
            }
        }
    };

    private int getAnswerChoice() {

        // Main form was not selected
        if (Integer.valueOf(mForm.getCheckedRadioButtonId()) == -1) {
            if ((mInitialOther.isChecked()) && (!mOther.isChecked())) {
                // mInitialOther is checked
                return 12;
            } else if ((!mInitialOther.isChecked()) && (mOther.isChecked())) {
                // mOther is checked
                return 13;
            } else {
                return -1;
            }
        } else {
            return mForm.getCheckedRadioButtonId();
        }
    }

    private void openCustom() {

        mForm.clearCheck();
        mOther.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrinkActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom strategy");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);

        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {

                    Toast.makeText(DrinkActivity.this, "Please enter a strategy", Toast.LENGTH_SHORT).show();
                    mForm.clearCheck();
                    mInitialOther.setChecked(false);
                } else {

                    String ts = editor.getText().toString();
                    mInitialOther.setText(ts);
                    mInitialOther.setChecked(true);
                    tempString = ts;
                    mPrefs.setInitCustomDrinking(ts);
                    mCachedReportData.setIcd(ts);
                    dialog.cancel();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
                mInitialOther.setChecked(false);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openCustom2() {

        mForm.clearCheck();
        mInitialOther.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrinkActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom strategy");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {

                    Toast.makeText(DrinkActivity.this, "Please enter a strategy", Toast.LENGTH_SHORT).show();
                    mForm.clearCheck();
                    mOther.setChecked(false);
                } else {

                    String ts = editor.getText().toString();
                    mOther.setText(ts);
                    mOther.setChecked(true);
                    mPrefs.setCustomDrinking(ts);
                    mCachedReportData.setCd(ts);
                    dialog.cancel();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
                mOther.setChecked(false);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void openCancelAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrinkActivity.this);
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
                mCachedReportData.getIntensity() + "\n" +
                ",answer2," + Integer.toString(mCachedReportData.getAnswer2()) + "\n" +
                ",answer3," + Integer.toString(mCachedReportData.getAnswer3()) + "\n" +
                ",answer4," + Integer.toString(mCachedReportData.getAnswer4()) + "\n" +
                ",answer5," + Integer.toString(mCachedReportData.getAnswer5()) + "\n";
        /* Current data format:
        log | 8/18/2015/23:56:19 | report_type | RT | temp:00 | 245424
            | answer1            | #           | I:#|
        *   | answer2            | #
        *   | answer3            | #
        *   | answer4            | #
        *   | answer5            | #
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
            Toast.makeText(DrinkActivity.this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }

        mPrefs.setReportResponseCache(mCachedReportData);
    }

    private void openFinishAlert() {

        // TODO: 9/16/15 WRAP UP AND LOG
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrinkActivity.this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View Viewlayout = inflater.inflate(R.layout.exit_dialog, (ViewGroup) findViewById(R.id.exit_dialog));

        alertDialogBuilder.setView(Viewlayout);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Great Job,\nKeep up the good work!");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drink, menu);
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