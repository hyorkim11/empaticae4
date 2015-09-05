package empaticae4.hrker.com.empaticae4;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Logger extends Application {

    static final String FILENAME = "log.csv";
    FileWriter writer;

    File root = Environment.getExternalStorageDirectory();
    File datafile = new File(root, FILENAME);

    private void writeHeader(String h1, String h2, String h3) throws IOException {

        String line = String.format("%s,%s,%s\n", h1, h2, h3);
        writer.write(line);
    }

    private void writeData(float d, float e, float f) throws IOException {

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

            fos = context.openFileOutput(FILENAME, Context.MODE_APPEND);
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

}
