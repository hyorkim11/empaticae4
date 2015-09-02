package empaticae4.hrker.com.empaticae4.Reports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Objects;

import empaticae4.hrker.com.empaticae4.R;

public class ReportActivity extends AppCompatActivity {

    public static final String DATAFILE = "userData";

    SharedPreferences sharedP = null;

    BootstrapButton bContinue, bCancel;
    RadioGroup form1, form2;
    RadioButton chk1, chk2, mOther, mOther2;
    TextView chkText;
    Boolean formChked;
    Boolean firstOrNot;
    int mIntensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        init();
    }

    private void init() {

        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        String tempString = sharedP.getString("custom", "Other");


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

        if ((form1.getCheckedRadioButtonId() == -1)&&(form2.getCheckedRadioButtonId() == -1)) {
            // no selection is made
            return false;
        } else if ((form2.getCheckedRadioButtonId() == -1)&&(form1.getCheckedRadioButtonId() != -1))  {
            // positive form is empty && negative emotion is selected
            return false;
        } else if ((form1.getCheckedRadioButtonId() == -1)&&(form2.getCheckedRadioButtonId() != -1))  {
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

                //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(i);
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

        final AlertDialog intensityDialog;
        final CharSequence[] intensity = {"0","1","2","3","4","5","6","7","8","9","10"};
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View Viewlayout = inflater.inflate(R.layout.intensity_dialog, (ViewGroup) findViewById(R.id.layout_dialog));
        final TextView tvIntensity = (TextView) Viewlayout.findViewById(R.id.tvIntensity);

        alertDialogBuilder.setTitle("Please rate how strong this feeling is");
        alertDialogBuilder.setMessage("scale from not at all to very strong");
        alertDialogBuilder.setView(Viewlayout);

        alertDialogBuilder.setSingleChoiceItems(intensity, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        tvIntensity.setText("0 - Not at all");
                        mIntensity = 0;
                        break;
                    case 1:
                        tvIntensity.setText("1");
                        mIntensity = 1;
                        break;
                    case 2:
                        tvIntensity.setText("2");
                        mIntensity = 2;
                        break;
                    case 3:
                        tvIntensity.setText("3");
                        mIntensity = 3;
                        break;
                    case 4:
                        tvIntensity.setText("4");
                        mIntensity = 4;
                        break;
                    case 5:
                        tvIntensity.setText("5");
                        mIntensity = 5;
                        break;
                    case 6:
                        tvIntensity.setText("6");
                        mIntensity = 6;
                        break;
                    case 7:
                        tvIntensity.setText("7");
                        mIntensity = 7;
                        break;
                    case 8:
                        tvIntensity.setText("8");
                        mIntensity = 8;
                        break;
                    case 9:
                        tvIntensity.setText("9");
                        mIntensity = 9;
                        break;
                    case 10:
                        tvIntensity.setText("10 - very strong");
                        mIntensity = 10;
                        break;
                    default:
                        break;
                }
            }
        });

        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (PoN()) {
                    // if positive emotion selected
                    Intent i = new Intent(getApplicationContext(), PositiveActivity.class);
                    startActivity(i);

                } else {
                    // negative emotion selected or default
                    Intent j = new Intent(getApplicationContext(), NegativeActivity.class);
                    startActivity(j);
                }


            }
        });

        intensityDialog = alertDialogBuilder.create();
        intensityDialog.show();
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
