package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.LayoutInflater;
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
import empaticae4.hrker.com.empaticae4.activity.settings.AlarmReceiver;
import empaticae4.hrker.com.empaticae4.main.MainActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class GoodMovesActivity extends Activity implements View.OnClickListener {


    private long tempTime;
    private RadioButton mInitialOther, mOther;
    private RadioGroup mForm;
    private BootstrapButton bCancel, bContinue;
    private String tempString, tempString2;
    private Time cal;
    private int mediaChoice;

    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_moves);
        init();
    }

    private void init() {

        // Capture time in millis as soon as activity begins
        tempTime = Calendar.getInstance().getTimeInMillis();

        mPrefs = new AppSharedPrefs(GoodMovesActivity.this);
        mCachedReportData = mPrefs.getReportResponseCache();

        tempString = mPrefs.getInitCustomGoodmove();
        tempString2 = mPrefs.getCustomGoodmove();

        mForm = (RadioGroup) findViewById(R.id.form1);
        mForm.setOnCheckedChangeListener(listener1);

        String[] goodMoves = new String[]{"Walk away from the situation",
                "Call / Text", "Listen to a relaxing song",
                "Take 4 slow & deep breaths", "Listen to a guided meditation",
                "Exercise for 15-20 minutes"};
        final RadioButton[] array = new RadioButton[6];
        for (int i = 0; i < 6; i++) {
            array[i] = new RadioButton(this);
            array[i].setText(goodMoves[i]);
            array[i].setId(i + 1);
            mForm.addView(array[i]);

            // set dynamic OnClickListeners for contact/mp3/meditation
            if (i == 1) {
                array[i].setOnClickListener(contactListener);
                array[i].setText(mPrefs.getCallcontact());
            } else if (i == 2) {
                array[i].setOnClickListener(mp3Listener);
            } else if (i == 4) {
                array[i].setOnClickListener(meditationListener);
            }
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

                    mCachedReportData.setAnswer5(getAnswerChoice());
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

                String t = mPrefs.getInitCustomGoodmove();

                if (t.equals("Other")) {

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
                String t = mPrefs.getCustomGoodmove();

                if (t.equals("Other")) {

                    openCustom2();
                } else {

                    mForm.clearCheck();
                    mOther.setChecked(true);
                    mInitialOther.setChecked(false);
                }
            }
        });


        if (Objects.equals(tempString, "Other")) {
            mInitialOther.setText("Other");
        } else {
            mInitialOther.setText(tempString);
        }

        if (Objects.equals(tempString2, "Other")) {
            mOther.setText("Other");
        } else {
            mOther.setText(tempString2);
        }

        mediaChoice = 0;

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
        if (mForm.getCheckedRadioButtonId() == -1) {
            if ((mInitialOther.isChecked()) && (!mOther.isChecked())) {
                // mInitialOther is checked
                return 7;
            } else if ((!mInitialOther.isChecked()) && (mOther.isChecked())) {
                // mOther is checked
                return 8;
            } else {
                return -1;
            }
        } else {
            return mForm.getCheckedRadioButtonId();
        }
    }

    public View.OnClickListener contactListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // OPEN CONTACT
            //Toast.makeText(GoodMovesActivity.this, "Contact selected", Toast.LENGTH_SHORT).show();
            mediaChoice = 1;

        }
    };
    public View.OnClickListener mp3Listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // OPEN MP3
            //Toast.makeText(GoodMovesActivity.this, "Mp3 selected", Toast.LENGTH_SHORT).show();
            mediaChoice = 2;

        }
    };
    public View.OnClickListener meditationListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // OPEN MEDITATION
            //Toast.makeText(GoodMovesActivity.this, "Meditation selected", Toast.LENGTH_SHORT).show();
            mediaChoice = 3;

        }
    };

    private void openCustom() {

        mForm.clearCheck();
        mOther.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoodMovesActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom strategy");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);

        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {

                    Toast.makeText(GoodMovesActivity.this, "Please enter a strategy", Toast.LENGTH_SHORT).show();
                    mForm.clearCheck();
                    mInitialOther.setChecked(false);
                } else {

                    String ts = editor.getText().toString();
                    mInitialOther.setText(ts);
                    mInitialOther.setChecked(true);
                    tempString = ts;
                    mPrefs.setInitCustomGoodmove(ts);
                    mCachedReportData.setIcgm(ts);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoodMovesActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom strategy");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {

                    Toast.makeText(GoodMovesActivity.this, "Please enter a strategy", Toast.LENGTH_SHORT).show();
                    mForm.clearCheck();
                    mOther.setChecked(false);
                } else {

                    String ts = editor.getText().toString();
                    mOther.setText(ts);
                    mOther.setChecked(true);
                    tempString2 = ts;
                    mPrefs.setCustomGoodmove(ts);
                    mCachedReportData.setCgm(ts);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoodMovesActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Are you sure you want to quit? \n You're so close!");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
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

    private void openFinishAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoodMovesActivity.this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View Viewlayout = inflater.inflate(R.layout.exit_dialog, (ViewGroup) findViewById(R.id.exit_dialog));

        alertDialogBuilder.setView(Viewlayout);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("You're doing great! \n Keep up the good work.");

        // set positive button: Finish
        alertDialogBuilder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                // save DATA before exiting
                finalizeReport();
                registerAlarm();
                Toast.makeText(getApplicationContext(), "Your response has been recorded", Toast.LENGTH_SHORT).show();

                if (mediaChoice == 0) {

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                } else if (mediaChoice == 1) {

                    String tempName = mPrefs.getCallcontact();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tempName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {

                    Bundle b = new Bundle();
                    b.putInt("media_key", mediaChoice);
                    Intent i = new Intent(getApplicationContext(), GoodMovesPlayerActivity.class);
                    i.putExtras(b);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


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
        long tempT = Calendar.getInstance().getTimeInMillis();
        long tempTime2 = mCachedReportData.getStartTime().getTimeInMillis();
        return (tempT - tempTime2);

    }

    private void finalizeReport() {

        mCachedReportData.setDuration_5(getPageDuration());

        // Set Duration of Current Report
        mCachedReportData.setDuration(getReportDuration());
        //int duration = (int) ((mCachedReportData.getDuration() / 1000) % 60);
        float duration = mCachedReportData.getDuration();

        // Set Current Time String: timeStamp
        cal = new Time(Time.getCurrentTimezone());
        cal.setToNow();
        String currentTime = (cal.month + 1) + "/" + cal.monthDay + "/" + cal.year + "/" + cal.format("%k:%M:%S");
        String timeStamp = mCachedReportData.getUserID() + "," + currentTime + "," +
                mCachedReportData.getReportType() + "," + duration + "," +
                "Triggered EDA: " + mCachedReportData.getEDA() + " / " + mCachedReportData.getEDAThresh() + "\n";

        // Current timeStamp format:

        // Set Data String: rowData
        String rowData = ",answer1," + Integer.toString(mCachedReportData.getAnswer1()) + "," + Long.toString(mCachedReportData.getDuration_1()) + ",I: " + mCachedReportData.getIntensity() + "\n" +
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
            Toast.makeText(GoodMovesActivity.this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }

        mPrefs.setReportResponseCache(mCachedReportData);
    }

    private long getPageDuration() {

        long tempTime2 = Calendar.getInstance().getTimeInMillis();
        return (tempTime2 - tempTime);

    }

    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoodMovesActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Are you sure you want to quit?");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                finish();
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
    public void onClick(View view) {
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

    private void registerAlarm() {

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm_type", 0);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // INACTIVITY ALARM CURRENTLY SET TO 24 hrs
        alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + 86400000, alarmIntent);

    }
}
