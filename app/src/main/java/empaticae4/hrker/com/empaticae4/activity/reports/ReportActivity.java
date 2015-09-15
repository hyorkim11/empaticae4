package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Calendar;
import java.util.Objects;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class ReportActivity extends Activity {


    private BootstrapButton bContinue, bCancel;
    private RadioGroup mForm1, mForm2;
    private RadioButton chk1, chk2, mInitialOther, mOther;
    private int mIntensity;
    private String mReport_Type, tempString;

    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        init();

    }

    private void init() {

        // Get report type based on "report_type" extra passed into this activity
        mReport_Type = getIntent().getExtras().getString("report_type", "N/A");

        mPrefs = new AppSharedPrefs(ReportActivity.this);

        // Reset SharedPrefs
        mPrefs.setReportResponseCache(new ReportDataWrapper());

        // Instantiate temp ReportDataWrapper class to hold values
        mCachedReportData = new ReportDataWrapper();
        mCachedReportData.setStartTime(Calendar.getInstance());
        mCachedReportData.setReportType(mReport_Type);

        tempString = mPrefs.getInitCustomNegativeMood();
        mIntensity = 0; // reset intensity

        bContinue = (BootstrapButton) findViewById(R.id.bContinue);
        bCancel = (BootstrapButton) findViewById(R.id.bCancel);
        mForm1 = (RadioGroup) findViewById(R.id.form1);
        mForm2 = (RadioGroup) findViewById(R.id.form2);

        String[] negativeChoices = new String[]{"Anxious|Nervous","Stressed","Sad","Frustrated","Embarrassed","Tired"};
        String[] positiveChoices = new String[]{"Happy","Excited|Energized","Relieved","Satisfied"};
        final RadioButton[] NC = new RadioButton[6];
        final RadioButton[] PC = new RadioButton[4];

        for (int i = 0; i < 6; i++) {
            NC[i] = new RadioButton(this);
            NC[i].setText(negativeChoices[i].toString());
            NC[i].setId(i+1);
            mForm1.addView(NC[i]);
        }
        for (int j = 0; j < 4; j++) {
            PC[j] = new RadioButton(this);
            PC[j].setText(positiveChoices[j].toString());
            PC[j].setId(j + 9); // take into consideration the negative choice ids (+8)
            mForm2.addView(PC[j]);
        }

        mForm1.clearCheck();
        mForm2.clearCheck();
        mForm1.setOnCheckedChangeListener(listener1);
        mForm2.setOnCheckedChangeListener(listener2);
        chk1 = (RadioButton) mForm1.findViewById(mForm1.getCheckedRadioButtonId());
        chk2 = (RadioButton) mForm2.findViewById(mForm2.getCheckedRadioButtonId());
        mInitialOther = (RadioButton) findViewById(R.id.bInitialOther);

        if (Objects.equals(tempString, "Other")) {

            mInitialOther.setText("Other");
        } else {

            mInitialOther.setText(tempString);
        }

        mOther = (RadioButton) findViewById(R.id.bOther);

        bContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (getAnswerChoice() != -1) {

                    openIntensity();
                } else {

                    Toast.makeText(getApplicationContext(), "Please make a selection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openAlert();
            }
        });

        mInitialOther.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String t = mPrefs.getInitCustomNegativeMood();

                if (t == "Other") {
                    openCustom();
                } else {

                    mForm1.clearCheck();
                    mForm2.clearCheck();
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

    }

    private OnCheckedChangeListener listener1 = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (checkedId != -1) {
                mForm2.setOnCheckedChangeListener(null);
                mForm2.clearCheck();
                mForm2.setOnCheckedChangeListener(listener2); //reset the listener
                mInitialOther.setChecked(false);
                mOther.setChecked(false);
            }
        }
    };

    private OnCheckedChangeListener listener2 = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (checkedId != -1) {
                mForm1.setOnCheckedChangeListener(null);
                mForm1.clearCheck();
                mForm1.setOnCheckedChangeListener(listener1);
                mInitialOther.setChecked(false);
                mOther.setChecked(false);
            }
        }
    };

    private int getAnswerChoice() {

        // Neither forms were selected, meaning its one of the 'Other' choices
        if ((Integer.valueOf(mForm1.getCheckedRadioButtonId()) == -1)
                && (Integer.valueOf(mForm2.getCheckedRadioButtonId()) == -1)) {

            // mInitialOther || mOther selected
            if ((mInitialOther.isChecked()) && (!mOther.isChecked())) {
                return 7;
            } else if ((mOther.isChecked()) && (!mInitialOther.isChecked())) {
                return 8;
            } else {
                return -1;
            }
        } else if ((mForm1.getCheckedRadioButtonId() != -1)
                && (mForm2.getCheckedRadioButtonId() == -1)) {
            // selection is made within mForm1
            return mForm1.getCheckedRadioButtonId();
        } else if ((mForm2.getCheckedRadioButtonId() != -1)
                && (mForm1.getCheckedRadioButtonId() == -1)) {
            // selection is made within mForm2
            return mForm2.getCheckedRadioButtonId();
        } else { // default
            return -1;
        }

    }


    private Boolean PoN() {
        // Checks to see if a selection made was either Positive or Negative
        // Positive = True
        // Negative = False (default)

        if ((mForm1.getCheckedRadioButtonId() == -1) && (mForm2.getCheckedRadioButtonId() == -1)) {
            // no selection is made
            return false;
        } else if ((mForm2.getCheckedRadioButtonId() == -1) && (mForm1.getCheckedRadioButtonId() != -1)) {
            // positive form is empty && negative emotion is selected
            return false;
        } else if ((mForm1.getCheckedRadioButtonId() == -1) && (mForm2.getCheckedRadioButtonId() != -1)) {
            // negative form is empty && positive emotion is selected
            return true;
        } else { //default false
            return false;
        }
    }

    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
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

    private void openCustom() {

        // make sure no other selections are made
        mForm1.clearCheck();
        mForm2.clearCheck();
        mOther.setChecked(false);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom feeling");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);

        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {

                    Toast.makeText(ReportActivity.this, "Please enter a mood", Toast.LENGTH_SHORT).show();
                    mInitialOther.setChecked(false);
                } else {

                    String ts = editor.getText().toString();
                    mInitialOther.setText(ts);
                    mInitialOther.setChecked(true);
                    tempString = ts;
                    mPrefs.setInitCustomNegativeMood(ts);
                    mCachedReportData.setIcnm(ts);
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

        // make sure no other selections are made
        mForm1.clearCheck();
        mForm2.clearCheck();
        mInitialOther.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom feeling");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (editor.getText().toString().trim().length() == 0) {
                    Toast.makeText(ReportActivity.this, "Please enter a mood", Toast.LENGTH_SHORT).show();
                    mOther.setChecked(false);
                } else {
                    String ts = editor.getText().toString();
                    mOther.setText(ts);
                    mOther.setChecked(true);
                    mPrefs.setCustomNegativeMood(ts);
                    mCachedReportData.setCnm(ts);
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

    private void openIntensity() {

        // Intensity Levels
        final CharSequence[] items = {" 0 - Not at all ", " 1 ", " 2 ", " 3 ",
                " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 ", " 10 - Very strong "};

        // Create and Build Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How Strong Is This Feeling?");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        mIntensity = 0;
                        break;

                    case 1:
                        mIntensity = 1;
                        break;

                    case 2:
                        mIntensity = 2;
                        break;

                    case 3:
                        mIntensity = 3;
                        break;

                    case 4:
                        mIntensity = 4;
                        break;

                    case 5:
                        mIntensity = 5;
                        break;

                    case 6:
                        mIntensity = 6;
                        break;

                    case 7:
                        mIntensity = 7;
                        break;

                    case 8:
                        mIntensity = 8;
                        break;

                    case 9:
                        mIntensity = 9;
                        break;

                    case 10:
                        mIntensity = 10;
                        break;

                    default:
                        break;
                }
            }
        });
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int pos = ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();
                if (pos == -1) {
                    Toast.makeText(ReportActivity.this, "Please select intensity", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(ReportActivity.this, "AnswerChoice: " + getAnswerChoice() + " Intensity: " + mIntensity, Toast.LENGTH_LONG).show();

                    mCachedReportData.setIntensity(mIntensity);
                    mCachedReportData.setAnswer1(getAnswerChoice());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    if (PoN()) {
                        // if positive emotion selected
                        Intent j = new Intent(getApplicationContext(), PositiveActivity.class);
                        startActivity(j);

                    } else {
                        // negative emotion selected or by default
                        Intent j = new Intent(getApplicationContext(), NegativeActivity.class);
                        startActivity(j);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog levelDialog;
        levelDialog = builder.create();
        levelDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
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

    @Override
    public void onBackPressed() {

        openAlert();
    }
}
