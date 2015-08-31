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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Objects;

import empaticae4.hrker.com.empaticae4.MainActivity;
import empaticae4.hrker.com.empaticae4.R;

// Cool thoughts selection upon what is going on choice
public class NegativeActivity2 extends AppCompatActivity {

    public static final String DATAFILE = "userData";
    SharedPreferences sharedP = null;

    RadioButton bOther;
    BootstrapButton bCancel, bContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative2);
        init();
    }

    void init() {

        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        final String tempString = sharedP.getString("custom_coolthought", "Other");

        bCancel = (BootstrapButton)findViewById(R.id.bCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCancelAlert();
            }
        });
        bContinue = (BootstrapButton)findViewById(R.id.bContinue);
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQuestionAlert();
            }
        });
        bOther = (RadioButton) findViewById(R.id.bOther);
        bOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Objects.equals(tempString, "Other")) {
                    openCustom();
                }
            }
        });

        if (Objects.equals(tempString, "Other")) {
            bOther.setText("Other");
        } else {
            bOther.setText(tempString);
        }

        openTip();

    }

    private void openTip() {

        LayoutInflater layout = getLayoutInflater();
        View dialog = layout.inflate(R.layout.tip_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity2.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("TIP");
        alertDialogBuilder.setView(dialog);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void openCustom() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity2.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your custom cool thought");

        final EditText editor = new EditText(this);
        final SharedPreferences.Editor spEditor = sharedP.edit();
        alertDialogBuilder.setView(editor);

        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                String tempString = editor.getText().toString();
                bOther.setText(tempString);
                spEditor.putString("custom_coolthought", tempString).apply();

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

    private void openCancelAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity2.this);
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

    private void openQuestionAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NegativeActivity2.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Planning on drinking?");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // set negative button: No
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_negative_activity2, menu);
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
