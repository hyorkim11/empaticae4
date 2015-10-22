package empaticae4.hrker.com.empaticae4.activity.empatica;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

import java.util.Date;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.activity.reports.ReportActivity;
import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;


public class dataService extends Service implements EmpaDataDelegate, EmpaStatusDelegate {

    private static final String TAG = "dataService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    private Intent intent;
    private int counter = 0;
    private float tempEDA, tempBattery;
    private int vibrateCounter = 0;
    private float EDAT;
    private boolean notificationTrigger;

    private EmpaDeviceManager deviceManager;
    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;


    public void onCreate() {
        super.onCreate();
        mPrefs = new AppSharedPrefs(this);
        mCachedReportData = mPrefs.getReportResponseCache();
        EDAT = mCachedReportData.getEDAThresh();
        // or EDAT = mPrefs.getEdaThrehold();

        intent = new Intent(BROADCAST_ACTION);
        Log.d(TAG, "entered onCreate with EDAT: " + EDAT);

        // Create a new EmpaDeviceManager. MainActivity is both its data and status delegate.
        deviceManager = new EmpaDeviceManager(getApplicationContext(), this, this);
        // Initialize the Device Manager using your API key. You need to have Internet access at this point.
        deviceManager.authenticateWithAPIKey(LiveStreamActivity.EMPATICA_API_KEY);

        notificationTrigger = false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "entered onStartCommand");
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second buffer time
        return Service.START_STICKY;
    }

    private Runnable sendUpdatesToUI = new Runnable() {

        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 10000); // Grab every 10 seconds
        }
    };

    private void EDANotice(Context context, float EDA) {

        Vibrator vNoti = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vNoti.vibrate(500);

        Intent i = new Intent(context, ReportActivity.class);
        i.putExtra("report_type", "EDA");
        i.putExtra("EDA", EDA);
        PendingIntent p = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("EDA Notice")
                        .setContentText("Your EDA broke the threshold")
                        .setTicker("Nudge from MtM")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(p)
                        .setAutoCancel(true)
                        .setPriority(2);

        int NOTIFICATION_ID = 1;

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());

    }

    private void BluetoothNotice(Context context) {

        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent p = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Bluetooth Notice")
                        .setContentText("Connect with the Empatica has been lost")
                        .setTicker("Nudge from MtM")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(p)
                        .setAutoCancel(true)
                        .setPriority(1);
        int NOTIFICATION_ID = 2;

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());

    }

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        intent.putExtra("time", new Date().toLocaleString());
        intent.putExtra("counter", String.valueOf(++counter));
        intent.putExtra("curEDA", tempEDA);
        intent.putExtra("battery", tempBattery);
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
                Toast.makeText(this, "Sorry, you can't connect to this device", Toast.LENGTH_SHORT).show();
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
            // Start scanning
            deviceManager.startScanning();

            // The device manager has established a connection
        } else if (status == EmpaStatus.CONNECTED) {

            // The device manager disconnected from a device
        } else if (status == EmpaStatus.DISCONNECTED) {
            //updateLabel(deviceNameLabel, "");
        }
    }

    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {
        //Log.d(TAG, "received Acc from E4");
    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {

        //updateLabel(bvpLabel, "" + bvp);

    }

    @Override
    public void didReceiveBatteryLevel(float battery, double timestamp) {
        tempBattery = battery;
        //updateLabel(batteryLabel, String.format("%.0f %%", battery * 100));

    }

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {

        //updateLabel(edaLabel, "" + gsr);

        Log.d(TAG, "received EDA of: " + gsr);

        tempEDA = gsr;
        if (gsr > EDAT) {
            vibrateCounter++;
            Log.d(TAG, "broke EDAT: " + gsr + " vc: " + vibrateCounter);

            if ((vibrateCounter == 20) && (notificationTrigger == false)) {
                // after this first shot of notification, block service until dataService is restarted
                EDANotice(this, gsr);
                vibrateCounter = 0;
                notificationTrigger = true;
            }

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
