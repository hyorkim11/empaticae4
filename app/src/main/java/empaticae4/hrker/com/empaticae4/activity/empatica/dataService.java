package empaticae4.hrker.com.empaticae4.activity.empatica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private int mx = 0, my = 0, mz = 0;
    private float tempEDA = 0, tempBattery;
    private float EDAT, mBVP;
    private boolean notificationTrigger = false;
    private Time cal;
    private Handler bufferHandler = new Handler();


    private EmpaDeviceManager deviceManager;
    private AppSharedPrefs mPrefs;
    private ReportDataWrapper mCachedReportData;


    public void onCreate() {
        super.onCreate();
        mPrefs = new AppSharedPrefs(this);
        mCachedReportData = mPrefs.getReportResponseCache();

        intent = new Intent(BROADCAST_ACTION);
        Log.d(TAG, "entered onCreate with EDAT: " + EDAT);
        // Create a new EmpaDeviceManager. MainActivity is both its data and status delegate.
        deviceManager = new EmpaDeviceManager(getApplicationContext(), this, this);
        // Initialize the Device Manager using your API key. You need to have Internet access at this point.
        deviceManager.authenticateWithAPIKey(LiveStreamActivity.EMPATICA_API_KEY);
        EDAT = mCachedReportData.getEDAT();

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
            handler.postDelayed(this, 1000); // Grab every 10 seconds
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

    private void StickyNotification(Context context) {

        Intent i = new Intent(context, LiveStreamActivity.class);
        PendingIntent p = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Empatica")
                        .setContentText("Connected to E4")
                        .setTicker("MTM")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(p)
                        .setOngoing(true)
                        .setPriority(2);

        Notification n;
        n = builder.build();
        n.flags |= Notification.FLAG_ONGOING_EVENT;
        //  + Notification.FLAG_NO_CLEAR
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(10, n);

    }

    private void DisplayLoggingInfo() {

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
        Log.d(TAG, "entered didUpdateStatus");
        // The device manager is ready for use
        if (status == EmpaStatus.READY) {
            // Start scanning
            deviceManager.startScanning();

            // The device manager has established a connection
        } else if (status == EmpaStatus.CONNECTED) {
            StickyNotification(this);
            // The device manager disconnected from a device
        } else if (status == EmpaStatus.DISCONNECTED) {
            //updateLabel(deviceNameLabel, "");
            DismissNotification();
            onUnbind(intent);
        }
    }

    public void DismissNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(10);
        notificationManager.cancel(1);
    }

    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {
        //Log.d(TAG, "received Acc from E4");
        mx = x;
        my = y;
        mz = z;

    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {
        // updateLabel(bvpLabel, "" + bvp);
        // blood volume pulse
        // Log.d(TAG, "received BVP of: " + bvp);
        mBVP = bvp;
    }

    @Override
    public void didReceiveBatteryLevel(float battery, double timestamp) {
        tempBattery = battery;
        //updateLabel(batteryLabel, String.format("%.0f %%", battery * 100));
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            notificationTrigger = false;
            bufferHandler.postDelayed(this, 600000);
        }
    };

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {
    // Log.d(TAG, "received EDA of: " + gsr);
        tempEDA = gsr;
        writeEDA(tempEDA, mBVP, mx, my, mz);

        if ((gsr >= EDAT) && (gsr != 0.00)) {
        // EDAT broken
            Log.d(TAG, "broke EDAT: " + gsr);
            if (notificationTrigger == false) {
                // Activate 10 min buffering time after notification is fired
                EDANotice(this, gsr);
                notificationTrigger = true;
                bufferHandler.postDelayed(runnable, 600000);
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

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void writeEDA(float curEDA, float curBVP, int x, int y, int z) {

        // Set Current Time String: timeStamp
        cal = new Time(Time.getCurrentTimezone());
        cal.setToNow();
        String currentTime = (cal.month + 1) + "/" + cal.monthDay + "/" +
                cal.year + "/" + cal.format("%k:%M:%S");
        String timeStamp = currentTime + "," + curEDA + "," + curBVP + "," + x + "," + y + "," + z + "\n";

        File file = null;
        File root = Environment.getExternalStorageDirectory();

        if (root.canWrite()) {

            File dir = new File(root.getAbsolutePath() + "/mtmData");
            dir.mkdirs();
            file = new File(dir, "userEDA.csv");
            FileOutputStream out = null;

            try {
                out = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(timeStamp.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "External Storage Can't Be Accessed", Toast.LENGTH_SHORT).show();
        }

        mPrefs.setReportResponseCache(mCachedReportData);
    }

}
