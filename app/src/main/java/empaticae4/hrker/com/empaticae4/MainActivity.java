package empaticae4.hrker.com.empaticae4;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import empaticae4.hrker.com.empaticae4.Reports.ReportActivity;


public class MainActivity extends ActionBarActivity implements OnMenuItemClickListener,
        OnMenuItemLongClickListener {

    public static final String DATAFILE = "userData";
    private FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;

    private BootstrapButton b1, b2, b3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();
        init();
        addFragment(new MainFragment(), true, R.id.container);
    }

    private void init() {

        b1 = (BootstrapButton)findViewById(R.id.b1);
        b2 = (BootstrapButton)findViewById(R.id.b2);
        b3 = (BootstrapButton)findViewById(R.id.b3);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), LiveStreamActivity.class);
                startActivity(i);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendNotification(view);
                //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(i);

            }
        });


    }
    private void initMenuFragment() {

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.ic_close);
        close.setScaleType(ImageView.ScaleType.FIT_XY);

        MenuObject contact = new MenuObject("Contact Us");
        contact.setResource(R.mipmap.ic_launcher);
        contact.setScaleType(ImageView.ScaleType.FIT_XY);

        MenuObject settings = new MenuObject("Settings");
        settings.setResource(R.mipmap.ic_launcher);
        settings.setScaleType(ImageView.ScaleType.FIT_XY);
        // TODO: 8/13/15 enlarge the size of the icons

        menuObjects.add(close);
        menuObjects.add(contact);
        menuObjects.add(settings);

        return menuObjects;
    }

    private void initToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(10);
        //mToolbar.setNavigationIcon(R.drawable.ic_drawer);
        // TODO: 8/13/15 set logo here on the navigation icon and change onclick response to change activity to home

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //MainActivity.this.getClass().getSimpleName()
                //onBackPressed();
                //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(i);
            }
        });
        mToolBarTextView.setText("Mind the Moment");
        mToolBarTextView.setTextColor(Color.GRAY);
    }

    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {

        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_NONE);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    // Notifications
    public void sendNotification(View v) {

        Vibrator vNoti = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        vNoti.vibrate(500);

        Intent notificationIntent = new Intent(this, ReportActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_drawer)
                .setAutoCancel(true)
                .setContentTitle("Nudge from Your Empatica")
                .setContentText("Would you like to make a report? \n (swipe to dismiss)")
                .addAction(R.drawable.ic_drawer, "Sure", contentIntent);

        builder.setContentIntent(contentIntent);

        // Create semi-unique notification ID
        long time = new Date().getTime();
        String tempStr = String.valueOf(time);
        String last4Str = tempStr.substring(tempStr.length() - 5);
        int notificationID = Integer.valueOf(last4Str);

        // Push notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // hide the notification after its selected
        //builder.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

        manager.notify(notificationID, builder.build());

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }

    private void openContact() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Contact Administrators");
        alertDialogBuilder.setMessage("Dr. Noelle Leonard");

        alertDialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                String number = "000000000";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

        switch (position)  {
            case 1:
                openContact();
                break;
            case 2:
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                break;
            default:

                break;

        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

}