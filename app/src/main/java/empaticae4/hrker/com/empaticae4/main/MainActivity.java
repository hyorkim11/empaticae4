package empaticae4.hrker.com.empaticae4.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.activity.empatica.LiveStreamActivity;
import empaticae4.hrker.com.empaticae4.activity.reports.ReportActivity;
import empaticae4.hrker.com.empaticae4.activity.settings.SettingsActivity;


public class MainActivity extends AppCompatActivity {


    private BootstrapButton b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Mind the Moment");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        b1 = (BootstrapButton) findViewById(R.id.b1);
        b2 = (BootstrapButton) findViewById(R.id.b2);
        //b3 = (BootstrapButton) findViewById(R.id.b3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                i.putExtra("report_type", "SI"); // self initiated
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), LiveStreamActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Overflow options menu
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.action_contact:
                openContact();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void openContact() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Contact Study Administrator");
        alertDialogBuilder.setMessage("Dawa S.");

        alertDialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                String number = "2129927178";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
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

}