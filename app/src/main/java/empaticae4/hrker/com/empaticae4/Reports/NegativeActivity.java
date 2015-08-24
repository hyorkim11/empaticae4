package empaticae4.hrker.com.empaticae4.Reports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import empaticae4.hrker.com.empaticae4.MainActivity;
import empaticae4.hrker.com.empaticae4.R;

// This is the Negative Response Activity

public class NegativeActivity extends AppCompatActivity {

    RadioGroup form1;
    RadioButton bOther;
    BootstrapButton bCancel, bContinue;

    public static final String DATAFILE = "userData";
    SharedPreferences sharedP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        init();
    }

    private void init() {

        form1 = (RadioGroup)findViewById(R.id.form1);
        bOther = (RadioButton)findViewById(R.id.bOther);
        bCancel = (BootstrapButton)findViewById(R.id.bCancel);
        bContinue = (BootstrapButton)findViewById(R.id.bContinue);

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

    private String getResponse() {
        // get response choice from radio group form

        String response;

        if(form1.getCheckedRadioButtonId()!=-1)  {

            RadioGroup rg = (RadioGroup)findViewById(R.id.form1);
            response = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();

        } else {
            response = "N/A";
        }

        return response;
    }

    private void openContinueAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Aw Yeah", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                // TODO: 8/24/15  record data from String temp

//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);
//                Toast.makeText(getApplicationContext(), "Your response has been recorded", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
