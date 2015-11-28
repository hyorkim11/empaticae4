package empaticae4.hrker.com.empaticae4.activity.empatica;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import empaticae4.hrker.com.empaticae4.R;


public class LiveStreamActivity extends Activity {

    public static final int REQUEST_ENABLE_BT = 1;
    public static final String EMPATICA_API_KEY = "6c8d1b1459ff473fbc6e71d6ae76aa19";
    private Intent intent;

    private TextView tvEDA, tvBatt;
    private GraphView graph;
    private double xCount = 0;
    private LineGraphSeries<DataPoint> tempSeries;
    private ToggleButton toggleGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);
        intent = new Intent(this, dataService.class);

        tvEDA = (TextView) findViewById(R.id.curEDA);
        tvBatt = (TextView) findViewById(R.id.tvBattery);

        toggleGraph = (ToggleButton) findViewById(R.id.toggle);
        toggleGraph.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    graph.setVisibility(View.VISIBLE);
                } else {
                    graph.setVisibility(View.INVISIBLE);
                }
            }
        });

        graph = (GraphView) findViewById(R.id.graph);
        tempSeries = new LineGraphSeries<>();
        graph.addSeries(tempSeries);
        graph.setTitle("Live EDA Level");

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(dataService.BROADCAST_ACTION));
        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
//        stopService(intent);
    }

    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        float curEDA = intent.getFloatExtra("curEDA", 0.0f);
        float curBatt = intent.getFloatExtra("battery", 0.0f);

        TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        txtDateTime.setText(time);
        txtCounter.setText(counter);
        tvEDA.setText("EDA: " + curEDA);
        tvBatt.setText("E4 Battery: " + String.format("%.0f %%", curBatt * 100));
        xCount++;
        tempSeries.appendData(new DataPoint(xCount, curEDA), true, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(LiveStreamActivity.this, "Bluetooth is required to connect to E4", Toast.LENGTH_LONG).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
