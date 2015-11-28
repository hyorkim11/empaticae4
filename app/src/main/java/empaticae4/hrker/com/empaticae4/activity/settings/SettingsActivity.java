package empaticae4.hrker.com.empaticae4.activity.settings;

import android.app.Activity;
import android.app.Dialog;
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
    private Button bUserID, resetButton, sendButton, mResetCSV,
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

        sendButton = (Button) findViewById(R.id.bSend);
        sendButton.setOnClickListener(this);
//        resetButton = (Button) findViewById(R.id.resetPrefs);
//        resetButton.setOnLongClickListener(this);
//        mResetCSV = (Button) findViewById(R.id.bResetCSV);
//        mResetCSV.setOnLongClickListener(this);
        mSetCallContact = (Button) findViewById(R.id.bSetCallContact);
        mSetCallContact.setOnClickListener(this);
        bUserID = (Button) findViewById(R.id.bUserID);
        bUserID.setOnClickListener(this);

        sp0 = (TextView) findViewById(R.id.sp0);
        if (mCachedReportData.getUserID() != "Other") {
            sp0.setText("ID: " + mCachedReportData.getUserID());
        }
        sp1 = (TextView) findViewById(R.id.sp1);
        if (mCachedReportData.getEDAThresh() != 0.0f) {
            sp1.setText("EDAT: " + mCachedReportData.getEDAThresh());
        }
        sp2 = (TextView) findViewById(R.id.sp2);
        if (mCachedReportData.getCallContact() != "Other") {
            sp2.setText("Contact: " + mCachedReportData.getCallContact());
        }
        sp3 = (TextView) findViewById(R.id.sp3);
        if (mCachedReportData.getIcct() != "Other") {
            sp3.setText("ICCT: " + mCachedReportData.getIcct());
        }
        sp4 = (TextView) findViewById(R.id.sp4);
        if (mCachedReportData.getIcd() != "Other") {
            sp4.setText("ICD: " + mCachedReportData.getIcd());
        }
        sp5 = (TextView) findViewById(R.id.sp5);
        if (mCachedReportData.getIce() != "Other") {
            sp5.setText("ICE: " + mCachedReportData.getIce());
        }
        sp6 = (TextView) findViewById(R.id.sp6);
        if (mCachedReportData.getIcgm() != "Other") {
            sp6.setText("ICGM: " + mCachedReportData.getIcgm());
        }
        sp7 = (TextView) findViewById(R.id.sp7);
        if (mCachedReportData.getIcnm() != "Other") {
            sp7.setText("ICNM: " + mCachedReportData.getIcnm());
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.resetPrefs) {

//            mPrefs.wrapUp();
//            Toast.makeText(SettingsActivity.this, "SharedPref has been reset", Toast.LENGTH_SHORT).show();
//            finish();
//            startActivity(getIntent());
        } else if (v.getId() == R.id.bSend) {

            sendCSV();
        } else if (v.getId() == R.id.bResetCSV) {
            //resetCSV();
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
                    mCachedReportData.setEDAThresh(Float.parseFloat(et.getText().toString()));
                    mPrefs.setEdaThreshold(Float.parseFloat(et.getText().toString()));
                    mPrefs.setReportResponseCache(mCachedReportData);
                    Toast.makeText(SettingsActivity.this, "EDAT set as: " + et.getText().toString(), Toast.LENGTH_SHORT).show();
                    bSetEDAThresh.setText("EDAT: " + mCachedReportData.getEDAThresh());
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
                    mCachedReportData.setUserID(et.getText().toString());
                    mPrefs.setUserID(et.getText().toString());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    Toast.makeText(SettingsActivity.this, "ID set as: " + et.getText().toString(), Toast.LENGTH_SHORT).show();
                    bUserID.setText("ID: " + mCachedReportData.getUserID());
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
                    mCachedReportData.setCallContact(et.getText().toString());
                    mPrefs.setCallcontact(et.getText().toString());
                    mPrefs.setReportResponseCache(mCachedReportData);
                    Toast.makeText(SettingsActivity.this, "Contact set as: " + et.getText().toString(), Toast.LENGTH_SHORT).show();
                    mSetCallContact.setText(mCachedReportData.getCallContact());
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
