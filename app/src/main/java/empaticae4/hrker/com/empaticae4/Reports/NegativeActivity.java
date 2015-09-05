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
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Objects;

import empaticae4.hrker.com.empaticae4.MainActivity;
import empaticae4.hrker.com.empaticae4.R;

// This is the Negative Response Activity

public class NegativeActivity extends AppCompatActivity {

    RadioGroup form1;
    RadioButton mInitialOther, mOther;
    BootstrapButton bCancel, bContinue;

    public static final String DATAFILE = "userData";
    SharedPreferences sharedP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative);
        init();
    }

    private void init() {

        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        String tempString = sharedP.getString("Custom_negative_event", "Other");

        mInitialOther = (RadioButton) findViewById(R.id.bInitialOther);
        mInitialOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCustom();
            }
        });
        form1 = (RadioGroup) findViewById(R.id.form1);
        mOther = (RadioButton) findViewById(R.id.bOther);
        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCustom2();
            }
        });

        if (Objects.equals(tempString, "Other")) {
            mOther.setText("Other");
        } else {
            mOther.setText(tempString);
        }

        bCancel = (BootstrapButton) findViewById(R.id.bCancel);
        bContinue = (BootstrapButton) findViewById(R.id.bContinue);

        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (form1.getCheckedRadioButtonId() != -1) {

                    Intent i = new Intent(getApplicationContext(), NegativeActivity2.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Please make a selection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCancelAlert();
            }
        });

    }

    private void openCustom() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("What is going on right now?");

        final EditText editor = new EditText(this);
        final SharedPreferences.Editor spEditor = sharedP.edit();
        alertDialogBuilder.setView(editor);


        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                String tempString = editor.getText().toString();
                mOther.setText(tempString);
                spEditor.putString("custom_event", tempString).commit();

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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("What is going on right now?");

        final EditText editor = new EditText(this);
        alertDialogBuilder.setView(editor);
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                mOther.setText(editor.getText());
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

    private String getResponse() {
        // get response choice from radio group form

        String response;

        if (form1.getCheckedRadioButtonId() != -1) {

            RadioGroup rg = (RadioGroup) findViewById(R.id.form1);
            response = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();

        } else {
            response = "N/A";
        }

        return response;
    }

    private void openCancelAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_response, menu);
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
