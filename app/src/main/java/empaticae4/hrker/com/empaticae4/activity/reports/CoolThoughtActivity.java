package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Calendar;
import java.util.Objects;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.main.MainActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class CoolThoughtActivity extends Activity {


    private long tempTime;
    private RadioGroup mForm1;
    private RadioButton mInitialOther, mOther;
    private BootstrapButton bCancel, bContinue;
    private String tempString, tempString2;

    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coolthought);
        init();
    }

    private void init() {

        // Capture time in millis as soon as activity begins
        tempTime = Calendar.getInstance().getTimeInMillis();

        mPrefs = new AppSharedPrefs(CoolThoughtActivity.this);
        mCachedReportData = mPrefs.getReportResponseCache();

        tempString = mPrefs.getInitCustomCoolthought();
        tempString2 = mPrefs.getCustomCoolthought();

        mForm1 = (RadioGroup) findViewById(R.id.form1);
        mForm1.setOnCheckedChangeListener(listener1);

        String[] coolThoughts = new String[]{"This feeling is temporary",
                "I can choose how to respond","My thoughts are not me",
                "If I drink too much, how will I feel tomorrow?",
                "I can handle this"};

        final RadioButton[] CT = new RadioButton[5];
        for (int i = 0; i < 5; i++) {
            CT[i] = new RadioButton(this);
            CT[i].setText(coolThoughts[i].toString());
            CT[i].setId(i + 1);
            mForm1.addView(CT[i]);
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

                    mCachedReportData.setAnswer3(getAnswerChoice());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    openQuestionAlert();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please make a selection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mInitialOther = (RadioButton) findViewById(R.id.bInitialOther);
        mOther = (RadioButton) findViewById(R.id.bOther);
        mInitialOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String t = mPrefs.getInitCustomCoolthought();

                if (t == "Other") {

                    openCustom();
                } else {

                    mForm1.clearCheck();
                    mOther.setChecked(false);
                    mInitialOther.setChecked(true);
                }
            }
        });

        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = mPrefs.getCustomCoolthought();

                if (t == "Other") {
                    openCustom2();
                } else {

                    mForm1.clearCheck();
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
        if (Integer.valueOf(mForm1.getCheckedRadioButtonId()) == -1) {
            if ((mInitialOther.isChecked()) && (!mOther.isChecked())) {
                // mInitialOther is checked
                return 6;
            } else if ((!mInitialOther.isChecked()) && (mOther.isChecked())) {
                // mOther is checked
                return 7;
            } else {
                return -1;
            }
        } else {
            return mForm1.getCheckedRadioButtonId();
        }
    }

    private void openCustom() {

        mForm1.clearCheck();
        mOther.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoolThoughtActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom cool thought");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);

        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {

                    Toast.makeText(CoolThoughtActivity.this, "Please enter a cool thought", Toast.LENGTH_SHORT).show();
                    mForm1.clearCheck();
                    mInitialOther.setChecked(false);
                } else {

                    String ts = editor.getText().toString();
                    mInitialOther.setText(ts);
                    mInitialOther.setChecked(true);
                    tempString = ts;
                    mPrefs.setInitCustomCoolthought(ts);
                    mCachedReportData.setIcct(ts);
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

        mForm1.clearCheck();
        mInitialOther.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoolThoughtActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom cool thought");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {

                    Toast.makeText(CoolThoughtActivity.this, "Please enter an event", Toast.LENGTH_SHORT).show();
                    mForm1.clearCheck();
                    mOther.setChecked(false);
                } else {

                    String ts = editor.getText().toString();
                    mOther.setText(ts);
                    mOther.setChecked(true);
                    tempString2 = ts;
                    mPrefs.setCustomCoolthought(ts);
                    mCachedReportData.setCct(ts);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoolThoughtActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Are you sure you want to quit?");

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

    private void openQuestionAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoolThoughtActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Planning on drinking?");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                mCachedReportData.setDuration_3(getPageDuration());
                mCachedReportData.setAnswer3(getAnswerChoice());
                mPrefs.setReportResponseCache(mCachedReportData);
                Intent i = new Intent(getApplicationContext(), DrinkActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        // set negative button: No
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mCachedReportData.setDuration_3(getPageDuration());
                mCachedReportData.setAnswer3(getAnswerChoice());
                mPrefs.setReportResponseCache(mCachedReportData);
                Intent i = new Intent(getApplicationContext(), GoodMovesActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoolThoughtActivity.this);
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

    private long getPageDuration() {

        long tempTime2 = Calendar.getInstance().getTimeInMillis();
        return (tempTime2 - tempTime);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
