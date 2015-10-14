package empaticae4.hrker.com.empaticae4.activity.empatica;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

import java.util.Date;


public class dataService extends Service implements EmpaDataDelegate, EmpaStatusDelegate {

    private static final String TAG = "dataService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    private EmpaDeviceManager deviceManager;
    public BluetoothAdapter mBluetoothAdapter;


    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);

        Log.d(TAG, "entered onCreate");

        // Create a new EmpaDeviceManager. MainActivity is both its data and status delegate.
        deviceManager = new EmpaDeviceManager(getApplicationContext(), this, this);
        // Initialize the Device Manager using your API key. You need to have Internet access at this point.
        deviceManager.authenticateWithAPIKey(LiveStreamActivity.EMPATICA_API_KEY);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "entered onStartCommand");
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

        return Service.START_STICKY;
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 10000); // 10 seconds
        }
    };


    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        intent.putExtra("time", new Date().toLocaleString());
        intent.putExtra("counter", String.valueOf(++counter));
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying dataService");
        handler.removeCallbacks(sendUpdatesToUI);
        deviceManager.cleanUp();
        super.onDestroy();
    }

    @Override
    public void didRequestEnableBluetooth() {
        // Request the user to enable Bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(enableBtIntent);
    }

    @Override
    public void didDiscoverDevice(BluetoothDevice bluetoothDevice, String deviceName, int rssi, boolean allowed) {


        Log.d(TAG, "entered didDiscoverDevice");
        if (allowed) {
            // Stop scanning. The first allowed device will do.
            deviceManager.stopScanning();
            try {
                // Connect to the device
                deviceManager.connectDevice(bluetoothDevice);
                //updateLabel(deviceNameLabel, "To: " + deviceName);
            } catch (ConnectionNotAllowedException e) {
                // This should happen only if you try to connect when allowed == false.
                //Toast.makeText(this, "Sorry, you can't connect to this device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void didUpdateSensorStatus(EmpaSensorStatus status, EmpaSensorType type) {
        // No need to implement this right now
        Log.d(TAG, "entered didUpdateSensorStatus");
    }

    @Override
    public void didUpdateStatus(EmpaStatus status) {
        // Update the UI
        //updateLabel(statusLabel, status.name());

        Log.d(TAG, "entered didUpdateStatus");
        // The device manager is ready for use
        if (status == EmpaStatus.READY) {
            //updateLabel(statusLabel, status.name() + " - Turn on your device");
            // Start scanning
            deviceManager.startScanning();


            // The device manager has established a connection
        } else if (status == EmpaStatus.CONNECTED) {
            // Stop streaming after STREAMING_TIME



            // The device manager disconnected from a device
        } else if (status == EmpaStatus.DISCONNECTED) {
            //updateLabel(deviceNameLabel, "");
        }
    }

    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {

        Log.d(TAG, "received Acc from E4");
    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {
        //updateLabel(bvpLabel, "" + bvp);

    }

    @Override
    public void didReceiveBatteryLevel(float battery, double timestamp) {
        //updateLabel(batteryLabel, String.format("%.0f %%", battery * 100));

    }

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {
        //updateLabel(edaLabel, "" + gsr);

        Log.d(TAG, "received EDA of: " + gsr);

        if (gsr > LiveStreamActivity.EDAthreshold) {
            // throw notification if EDA breaks threshold defined in LivestreamActivity
            Log.d(TAG, "broke EDA threshold: " + gsr);
        }
    }

    @Override
    public void didReceiveIBI(float ibi, double timestamp) {
        //updateLabel(ibiLabel, "" + ibi);
    }

    @Override
    public void didReceiveTemperature(float temp, double timestamp) {
        //updateLabel(temperatureLabel, "" + temp);
    }
}
