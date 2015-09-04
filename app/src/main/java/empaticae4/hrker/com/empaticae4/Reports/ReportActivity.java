package empaticae4.hrker.com.empaticae4.Reports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Calendar;
import java.util.Objects;

import empaticae4.hrker.com.empaticae4.R;

public class ReportActivity extends AppCompatActivity {

    public static final String DATAFILE = "userData";
    SharedPreferences sharedP = null;

    public static long start;
    BootstrapButton bContinue, bCancel;
    RadioGroup form1, form2;
    RadioButton chk1, chk2, mOther, mOther2;
    TextView chkText;
    Boolean formChked;
    Boolean firstOrNot;
    int mIntensity;
    String intensityLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        init();
    }

    private void init() {

        String tempString = sharedP.getString("custom", "Other");

        start = Calendar.getInstance().getTimeInMillis();
        SharedPreferences.Editor spEditor = sharedP.edit();
        spEditor.putLong("start_time", start).commit();

        mIntensity = 0; // reset intensity
        bContinue = (BootstrapButton) findViewById(R.id.bContinue);
        bCancel = (BootstrapButton) findViewById(R.id.bCancel);
        form1 = (RadioGroup) findViewById(R.id.form1);
        form2 = (RadioGroup) findViewById(R.id.form2);
        form1.clearCheck();
        form2.clearCheck();
        form1.setOnCheckedChangeListener(listener1);
        form2.setOnCheckedChangeListener(listener2);
        chk1 = (RadioButton) form1.findViewById(form1.getCheckedRadioButtonId());
        chk2 = (RadioButton) form2.findViewById(form2.getCheckedRadioButtonId());
        mOther = (RadioButton) findViewById(R.id.bOther);

        if (Objects.equals(tempString, "Other")) {

            mOther.setText("Other");
        } else {

            mOther.setText(tempString);
        }

        mOther2 = (RadioButton) findViewById(R.id.bOther2);
        chkText = (TextView) findViewById(R.id.chkText);
        chkText.setText("");
        firstOrNot = sharedP.getBoolean("first", false);

        bContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (checkForm()) {

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
        mOther.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
                String temp = sharedP.getString("custom", "Other");

                if (temp == "Other") {
                    openCustom();
                }

            }
        });
        mOther2.setOnClickListener(new View.OnClickListener() {

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
                formChked = true;
                form2.setOnCheckedChangeListener(null);
                form2.clearCheck();
                form2.setOnCheckedChangeListener(listener2); //reset the listener
                chkText.setText("" + form2.getCheckedRadioButtonId());
            }
        }
    };

    private OnCheckedChangeListener listener2 = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (checkedId != -1) {
                formChked = true;
                form1.setOnCheckedChangeListener(null);
                form1.clearCheck();
                form1.setOnCheckedChangeListener(listener1);
                chkText.setText("" + form1.getCheckedRadioButtonId());
            }
        }
    };


    private Boolean checkForm() {
        // Checks to see if a selection was made within either forms

        if ((chkText.getText() != "") && (formChked)) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean PoN() {
        // Checks to see if a selection made was either Positive or Negative
        // Positive = True
        // Negative = False (default)

        if ((form1.getCheckedRadioButtonId() == -1) && (form2.getCheckedRadioButtonId() == -1)) {
            // no selection is made
            return false;
        } else if ((form2.getCheckedRadioButtonId() == -1) && (form1.getCheckedRadioButtonId() != -1)) {
            // positive form is empty && negative emotion is selected
            return false;
        } else if ((form1.getCheckedRadioButtonId() == -1) && (form2.getCheckedRadioButtonId() != -1)) {
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom feeling");

        final EditText editor = new EditText(this);
        final SharedPreferences.Editor spEditor = sharedP.edit();
        alertDialogBuilder.setView(editor);

        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                String tempString = editor.getText().toString();
                mOther.setText(tempString);
                spEditor.putString("custom", tempString).commit();

                dialog.cancel();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
                form1.clearCheck();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void openCustom2() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your feeling");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                mOther2.setText(editor.getText());
                dialog.cancel();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openIntensity() {

        // Intensity Levels
        final CharSequence[] items = {" 0 - Not at all ", " 1 ", " 2 ", " 3 ",
                " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 ", " 10 - Very strong "};

        // SharedP for intensity record
        final SharedPreferences.Editor spEditor = sharedP.edit();

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

                // Save recorded response
                // Records: Intensity, Negative_Mood #, Positive_Mood #
                intensityLevel = String.valueOf(mIntensity);
                spEditor.putString("Intensity", intensityLevel).commit();
                spEditor.putString("Negative_Mood", String.valueOf(form1.getCheckedRadioButtonId()));
                spEditor.putString("Positive_Mood", String.valueOf(form2.getCheckedRadioButtonId()));
                spEditor.commit();
                Toast.makeText(ReportActivity.this, "intensity set at: " + intensityLevel + "N: " + String.valueOf(form1.getCheckedRadioButtonId()) + "P: " + String.valueOf(form2.getCheckedRadioButtonId()), Toast.LENGTH_LONG).show();

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
