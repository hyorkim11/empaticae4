package empaticae4.hrker.com.empaticae4;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Calendar;

public class Logger {

    static final String FILENAME = "log.csv";

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
            Toast.makeText(context, "Oops, Logging Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

        /*
            * String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
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
        String[] data = {"Ship Name","Scientist Name", "...",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").formatter.format(date)});

        writer.writeNext(data);

        writer.close();
        *
        *
        * */

}
