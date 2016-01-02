package empaticae4.hrker.com.empaticae4.activity.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class SettingsActivity extends Activity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;
    private Time cal;
    private Button bUserID, bResetPrefs, bSendCSV, bResetCSV,
            mSetCallContact, bSetEDAThresh;
    private TextView sp0, sp1, sp2, sp3, sp4, sp5, sp6, sp7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init() {

        mPrefs = new AppSharedPrefs(SettingsActivity.this);
        mCachedReportData = mPrefs.getReportResponseCache();

        bUserID = (Button) findViewById(R.id.bUserID);
        bUserID.setText("Set ID");

        bSetEDAThresh = (Button) findViewById(R.id.SetEDAThresh);
        bSetEDAThresh.setText("Set EDAT");
        bSetEDAThresh.setOnClickListener(this);

        bSendCSV = (Button) findViewById(R.id.bSend);
        bSendCSV.setOnClickListener(this);

        bResetPrefs = (Button) findViewById(R.id.resetPrefs);
        bResetPrefs.setOnClickListener(this);

        bResetCSV = (Button) findViewById(R.id.bResetCSV);
        bResetCSV.setOnClickListener(this);

        mSetCallContact = (Button) findViewById(R.id.bSetCallContact);
        mSetCallContact.setOnClickListener(this);

        bUserID = (Button) findViewById(R.id.bUserID);
        bUserID.setOnClickListener(this);

        sp0 = (TextView) findViewById(R.id.sp0);
        sp0.setText("User ID: " + mPrefs.getUserID());
        sp1 = (TextView) findViewById(R.id.sp1);
        sp1.setText("Friend Contact: " + mPrefs.getCallcontact());
        sp2 = (TextView) findViewById(R.id.sp2);
        sp3 = (TextView) findViewById(R.id.sp3);
        sp4 = (TextView) findViewById(R.id.sp4);
        sp5 = (TextView) findViewById(R.id.sp5);
        sp6 = (TextView) findViewById(R.id.sp6);
        sp7 = (TextView) findViewById(R.id.sp7);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.resetPrefs) {
            openPrefResetWarning();
        } else if (v.getId() == R.id.bSend) {
            sendCSV();
        } else if (v.getId() == R.id.bResetCSV) {
            openCSVResetWarning();
        } else if (v.getId() == R.id.bSetCallContact) {
            openContact();
        } else if (v.getId() == R.id.SetEDAThresh) {
            setEDAThreshold();
        } else if (v.getId() == R.id.bUserID) {
            openUserID();
        }
    }


    private void setEDAThreshold() {

        // Create and Build Dialog
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_set_user_eda, null);
        builder.setContentView(view);

        final EditText et = (EditText) builder.findViewById(R.id.etUserEDA);

        Button bContinue = (Button) builder.findViewById(R.id.bContinue);
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().length() == 0) {
                    Toast.makeText(SettingsActivity.this, "Please enter valid EDAT", Toast.LENGTH_SHORT).show();
                } else {
                    // check if entered EDA is text or numbers
//                    mCachedReportData.setEDAT(Float.parseFloat(et.getText().toString()));
                    mPrefs.setEDAT(Float.parseFloat(et.getText().toString()));
                    mPrefs.setReportResponseCache(mCachedReportData);
                    Toast.makeText(SettingsActivity.this, "EDAT set as: " + et.getText().toString(), Toast.LENGTH_SHORT).show();
                    bSetEDAThresh.setText("EDAT: " + et.getText().toString());
                    builder.dismiss();
                }
            }
        });

        builder.setCanceledOnTouchOutside(true);
        builder.show();
    }

    private void sendCSV() {

        cal = new Time(Time.getCurrentTimezone());
        cal.setToNow();
        String currentTime = (cal.month + 1) + "/" + cal.monthDay +
                "/" + cal.year + "/" + cal.format("%k:%M:%S");
        String timeStamp = "\n" + ",,,,exported:," + currentTime + "," + "\n\n";
        File file = null;
        File file2 = null;
        File root = Environment.getExternalStorageDirectory();

        if (root.canWrite()) {

            File dir = new File(root.getAbsolutePath() + "/mtmData");
            if (!dir.exists()) {
                dir.mkdir();
            }

            file = new File(dir, "userData.csv");
            file2 = new File(dir, "userEDA.csv");
            FileOutputStream out = null;
            FileOutputStream out2 = null;
            try {
                out = new FileOutputStream(file, true);
                out2 = new FileOutputStream(file2, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(timeStamp.getBytes());
                out2.write(timeStamp.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
                out2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(SettingsActivity.this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }

        Uri u1, u2;
        u1 = Uri.fromFile(file);
        u2 = Uri.fromFile(file2);

        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(u1);
        uris.add(u2);
        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setType("text/html");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ds4578@nyu.edu"});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MtM - Update");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is a data update email");
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(sendIntent, "Send An Email"));

    }

    private void resetCSV() {

        File file;
        File root = Environment.getExternalStorageDirectory();

        if (root.canWrite()) {

            File dir = new File(root.getAbsolutePath() + "/mtmData");
            file = new File(dir, "userData.csv");
            file.delete();
            Toast.makeText(SettingsActivity.this, "CSV File Has Been Reset", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SettingsActivity.this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCSVResetWarning() {

        // Create and Build Dialog
        AlertDialog.Builder mDialog = new AlertDialog.Builder(SettingsActivity.this);
        mDialog.setTitle("Are You Sure?");
        mDialog.setMessage("Do you wish to reset CSV?");
        mDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SettingsActivity.this, "CSV has been reset", Toast.LENGTH_SHORT).show();
                resetCSV();
            }
        });
        mDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = mDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private void openPrefResetWarning() {

        // Create and Build Dialog
        AlertDialog.Builder mDialog = new AlertDialog.Builder(SettingsActivity.this);
        mDialog.setTitle("Are You Sure?");
        mDialog.setMessage("Do you wish to reset User Prefs?");
        mDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPrefs.wrapUp();
                Toast.makeText(SettingsActivity.this, "SharedPref has been reset", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });
        mDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = mDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private void openUserID() {

        // Create and Build Dialog
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_set_user_id, null);
        builder.setContentView(view);

        final EditText et = (EditText) builder.findViewById(R.id.etUserID);

        Button bContinue = (Button) builder.findViewById(R.id.bContinue);
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().length() == 0) {
                    Toast.makeText(SettingsActivity.this, "Please enter valid ID", Toast.LENGTH_SHORT).show();
                } else {
                    mPrefs.setUserID(et.getText().toString());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    Toast.makeText(SettingsActivity.this, "ID set as: " + et.getText().toString(), Toast.LENGTH_SHORT).show();
                    bUserID.setText("ID: " + et.getText().toString());
                    builder.dismiss();
                }
            }
        });

        builder.setCanceledOnTouchOutside(true);
        builder.show();

    }

    private void openContact() {

        // Create and Build Dialog
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_set_user_call, null);
        builder.setContentView(view);

        final EditText et = (EditText) builder.findViewById(R.id.etSetContact);

        Button bContinue = (Button) builder.findViewById(R.id.bContinue);
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().length() == 0) {
                    Toast.makeText(SettingsActivity.this, "Please enter valid name", Toast.LENGTH_SHORT).show();
                } else {
                    mPrefs.setCallcontact(et.getText().toString());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    Toast.makeText(SettingsActivity.this, "Contact set as: " + et.getText().toString(), Toast.LENGTH_SHORT).show();
                    mSetCallContact.setText(et.getText().toString());
                    builder.dismiss();
                }
            }
        });

        builder.setCanceledOnTouchOutside(true);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPrefs.setReportResponseCache(mCachedReportData);
        finish();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
