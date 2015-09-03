package empaticae4.hrker.com.empaticae4.Reports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import empaticae4.hrker.com.empaticae4.MainActivity;
import empaticae4.hrker.com.empaticae4.R;

public class GoodMovesActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton rbContact, rbMp3, rbMeditation;
    BootstrapButton bCancel, bContinue;
    MediaPlayer mPlayer;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_moves);
        init();
    }

    private void init() {

        rbContact = (RadioButton) findViewById(R.id.rbContact);
        rbMeditation = (RadioButton) findViewById(R.id.rbMeditation);
        rbMp3 = (RadioButton) findViewById(R.id.rbMp3);
        rbContact.setOnClickListener(this);
        rbMeditation.setOnClickListener(this);
        rbMp3.setOnClickListener(this);
        bCancel = (BootstrapButton) findViewById(R.id.bCancel);
        bContinue = (BootstrapButton) findViewById(R.id.bContinue);
        bCancel.setOnClickListener(this);
        bContinue.setOnClickListener(this);

        mPlayer = MediaPlayer.create(GoodMovesActivity.this, R.raw.meditation_audio);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rbContact:

                break;
            case R.id.rbMeditation:

                break;
            case R.id.rbMp3:

                break;
            case R.id.bCancel:
                openCancelAlert();
                break;
            case R.id.bContinue:
                openFinishAlert();
                break;
            default:
                break;

        }
    }

    private void openFinishAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoodMovesActivity.this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View Viewlayout = inflater.inflate(R.layout.exit_dialog, (ViewGroup) findViewById(R.id.exit_dialog));

        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("You're doing great! \n Keep up the good work.");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                // save DATA before exiting


                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Your response has been recorded", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_good_moves, menu);
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
