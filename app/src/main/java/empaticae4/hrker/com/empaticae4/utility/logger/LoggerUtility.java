package empaticae4.hrker.com.empaticae4.utility.logger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class LoggerUtility {

    private Context mContext;
    private AppSharedPrefs mPrefs;
    private File root;
    private File datafile;
    public static final String DATAFILE = "userData.csv";
    private FileWriter writer;

    private String columnString = "\" Date \", \" col1 \", \" col2 \", \" col3 \"";
    private String dataString = "\"" + "temp" + "\"";

    public LoggerUtility(Context context)  {

        mContext = context;
        mPrefs = new AppSharedPrefs(context);
        root = Environment.getExternalStorageDirectory();
        datafile = new File(root, DATAFILE);
    }

    public void appendReportData(ReportDataWrapper r)  {

        mPrefs.appendReportData(r);
    }

    public void writeLog(float d, float e, float f) throws IOException {

        String line = String.format("%f,%f,%f\n", d, e, f);
        writer.write(line);
    }

    public void saveLog(Context context) {

        // file written and saved in: data/data/empaticae4.hrker.com.empaticae4/files/log.csv
        Calendar c = Calendar.getInstance();
        FileOutputStream fos;

        String entry = c.get(Calendar.YEAR) + "-"
                + c.get(Calendar.MONTH)
                + "-" + c.get(Calendar.DAY_OF_MONTH)
                + " at " + c.get(Calendar.HOUR_OF_DAY)
                + ":" + c.get(Calendar.MINUTE)
                + "\n";

        try {

            fos = context.openFileOutput(DATAFILE, Context.MODE_APPEND);
            fos.write(entry.getBytes());
            fos.close();
            Toast.makeText(context, "Logging Success", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Logging Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void sendLog() {

        String combinedString = columnString + "\n" + dataString;

        File file = null;
        File root = Environment.getExternalStorageDirectory();
        if (root.canWrite()) {
            File dir = new File(root.getAbsolutePath() + "/userData"); // was /PersonData
            dir.mkdirs();
            file = new File(dir, DATAFILE); // was Data.csv
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri u1;
        u1 = Uri.fromFile(file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/html");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "hyorim@umich.edu" });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MtM - Update");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is a data update email");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        mContext.startActivity(Intent.createChooser(sendIntent, "Send An Email"));
    }
}