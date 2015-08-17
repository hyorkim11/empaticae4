package empaticae4.hrker.com.empaticae4.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import empaticae4.hrker.com.empaticae4.MainActivity;
import empaticae4.hrker.com.empaticae4.R;

public class ReportActivity extends AppCompatActivity {

    BootstrapButton bContinue, bCancel;
    RadioGroup form1, form2;
    RadioButton chk1, chk2, mOther;
    TextView chkText;
    Boolean formChked;
    //final SeekBar sbIntensity;
    //final TextView tvIntensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
    }

    private void init() {

        bContinue = (BootstrapButton) findViewById(R.id.bContinue);
        bCancel = (BootstrapButton) findViewById(R.id.bCancel);
        form1 = (RadioGroup) findViewById(R.id.form1);
        form2 = (RadioGroup) findViewById(R.id.form2);
        form1.clearCheck();
        form2.clearCheck();
        form1.setOnCheckedChangeListener(listener1);
        form2.setOnCheckedChangeListener(listener2);
        chk1 = (RadioButton)form1.findViewById(form1.getCheckedRadioButtonId());
        chk2 = (RadioButton)form2.findViewById(form2.getCheckedRadioButtonId());
        mOther = (RadioButton) findViewById(R.id.bOther);
        chkText = (TextView) findViewById(R.id.chkText);
        chkText.setText("");

        //sbIntensity = (SeekBar) findViewById(R.id.sbIntensity);
        //tvIntensity = (TextView) findViewById(R.id.tvIntensity);

        //tvIntensity.setText("Intensity: " + sbIntensity.getProgress() + "/" + sbIntensity.getMax());

        bContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkForm()) {

                    openIntensity(view);
                } else {
                    Toast.makeText(getApplicationContext(), "Please make a selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openAlert(view);
            }
        });
        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustom(view);
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
                chkText.setText("" + form1.getCheckedRadioButtonId());
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

        if ((chkText.getText() == null)&&(!formChked)) {
            return false;
        }
        else if ((chkText.getText() != "")&&(formChked)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void openAlert(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Are you sure you want to quit?");

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "You have quit your report", Toast.LENGTH_LONG).show();
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

    private void openCustom(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage("Please enter your feeling");

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
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openIntensity(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);
        alertDialogBuilder.setTitle("Please rate how strong this feeling is");
        alertDialogBuilder.setMessage("on a scale from 0 to 10;not at all to very strong");
        final SeekBar sbIntensity = new SeekBar(this);
        sbIntensity.setMax(10);
        alertDialogBuilder.setView(sbIntensity);

        // set positive button: Yes
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // set negative button: No
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
}
