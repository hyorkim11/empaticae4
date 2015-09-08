package empaticae4.hrker.com.empaticae4;

import android.app.Application;
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

public class Logger extends Application {

    public static final String DATAFILE = "userData.csv";
    FileWriter writer;

    File root = Environment.getExternalStorageDirectory();
    File datafile = new File(root, DATAFILE);

    private void writeHeader(String h1, String h2, String h3) throws IOException {

        String line = String.format("%s,%s,%s\n", h1, h2, h3);
        writer.write(line);
    }

    private void writeLog(float d, float e, float f) throws IOException {

        String line = String.format("%f,%f,%f\n", d, e, f);
        writer.write(line);
    }

    /* this would be the way to use
        try {
        writer = new FileWriter(datafile);
        writeHeader("FirstParam","SecondParam","ThirdParam");
        writeData(0.31f,5.2f,7.0f);
        writeData(0.31f,5.2f,7.1f);
        writeData(0.31f,5.2f,7.2f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // don't forget to close
        writer.flush();
        writer.close();
    */

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

        /*
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "AnalysisData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath );
        CSVWriter writer;
        // File exist
        if(f.exists() && !f.isDirectory()){
        mFileWriter = new FileWriter(filePath , true);
        writer = new CSVWriter(mFileWriter);
        }
        else {
        writer = new CSVWriter(new FileWriter(filePath));
        }
        String[] data = {"temp1","temp2", "temp3",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").formatter.format(date)});

        writer.writeNext(data);

        writer.close();
        */

    private void sendLog() {

        String columnString = "\"Participant Name\", \"temp1\", \"temp2\", \"temp3\"";
        String dataString = "\"" + "temp" + "\"";
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

        Uri u1 = null;
        u1 = Uri.fromFile(file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/html");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "hyorim@umich.edu" });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MtM - Update");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is a data update email");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        startActivity(Intent.createChooser(sendIntent, "Send An Email"));
    }

}
