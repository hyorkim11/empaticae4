package empaticae4.hrker.com.empaticae4;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    public static final String DATAFILE = "userData";
    SharedPreferences sharedP = null;

    private Button resetButton, showCustomFeeling1;
    private TextView sp1, sp2, sp3, sp4, sp5, sp6, sp7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedP = getSharedPreferences(DATAFILE, MODE_MULTI_PROCESS);
        init();
    }

    private void init() {

        resetButton = (Button) findViewById(R.id.resetPrefs);
        showCustomFeeling1 = (Button) findViewById(R.id.showCustomFeeling1);
        sp1 = (TextView)findViewById(R.id.sp1);
        sp1.setText("custom negative mood: " + sharedP.getString("Custom_negative_mood", "Other"));
        sp2 = (TextView)findViewById(R.id.sp2);
        sp2.setText("negative mood: " + sharedP.getString("Negative_Mood", "Other"));
        sp3 = (TextView)findViewById(R.id.sp3);
        sp3.setText("positive mood: " + sharedP.getString("Positive_Mood", "Other"));
        sp4 = (TextView)findViewById(R.id.sp4);
        sp4.setText("custom negative event: " + sharedP.getString("Custom_negative_event", "Other"));
        sp5 = (TextView)findViewById(R.id.sp5);
        sp5.setText("custom event: " + sharedP.getString("custom_event", "Other"));
        sp6 = (TextView)findViewById(R.id.sp6);
        sp6.setText("custom cool thought: " + sharedP.getString("custom_coolthought", "Other"));
        sp7 = (TextView)findViewById(R.id.sp7);
        sp7.setText("custom drinking strategy: " + sharedP.getString("custom_drinking_strategy", "Other"));


        /* TESTING BUTTON TO RESET SHARED PREFS*/
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences(DATAFILE, Context.MODE_MULTI_PROCESS);
                settings.edit().clear().commit();
                Toast.makeText(SettingsActivity.this, "SharedPref has been reset", Toast.LENGTH_SHORT).show();
            }
        });
        showCustomFeeling1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences fetchCustom = getSharedPreferences(DATAFILE, Context.MODE_MULTI_PROCESS);
                Toast.makeText(SettingsActivity.this, "Custom Feeling 1 is set as: "+ fetchCustom.getString("custom", "N/A"), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
