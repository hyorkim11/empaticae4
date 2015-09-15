package empaticae4.hrker.com.empaticae4.activity.reports;

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
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import empaticae4.hrker.com.empaticae4.main.MainActivity;
import empaticae4.hrker.com.empaticae4.R;

// This is the Positive Response Activity

public class PositiveActivity extends AppCompatActivity {

    public static final String DATAFILE = "userData";
    SharedPreferences sharedP = null;

    EditText etResponse;
    String temp;
    BootstrapButton bContinue, bCancel;
    //Logger log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positive);
        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        init();
    }

    private void init() {

        //log = new Logger(PositiveActivity.this);

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

    private void recordTime() {

        // Pass in current time in milli, record report duration in SharedPref
        long startTime, endTime, duration;
        String temp;
        SharedPreferences.Editor spEditor = sharedP.edit();

        startTime = sharedP.getLong("Start_time", 0);
        endTime = Calendar.getInstance().getTimeInMillis();

        duration = endTime - startTime;
        temp = (new SimpleDateFormat("mm:ss:SSS")).format(new Date(duration));
        spEditor.putString("Report_duration", temp).commit();

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

                    // Record Positive_Event data & Duration of Report
                    temp = etResponse.getText().toString();
                    SharedPreferences.Editor spEditor = sharedP.edit();
                    spEditor.putString("Positive_Event", temp).commit();
                    recordTime();

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
