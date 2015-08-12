package empaticae4.hrker.com.empaticae4;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Calendar;

public class Logger {

    public void saveLog(Context context) {

        // file written and saved in: data/data/empaticae4.hrker.com.empaticae4/files/log.csv
        String FILENAME = "log.csv";
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


}
