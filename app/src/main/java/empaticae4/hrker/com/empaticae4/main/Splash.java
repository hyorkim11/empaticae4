package empaticae4.hrker.com.empaticae4.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Calendar;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.activity.settings.AlarmReceiver;

public class Splash extends Activity implements Animation.AnimationListener {

    private final int SPLASH_DISPLAY_LENGTH = 2500;
    private ImageView logo;
    private Animation animFadein;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setAlarm();

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        logo = (ImageView)findViewById(R.id.pic);
        logo.startAnimation(animFadein);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                Splash.this.startActivity(i);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
    }

    private void setAlarm() {

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm_type", 1);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        firingCal.set(Calendar.HOUR_OF_DAY, 16); // At 4PM DAILY
        firingCal.set(Calendar.MINUTE, 0); // minute
        firingCal.set(Calendar.SECOND, 0); // second

        if(firingCal.compareTo(currentCal) < 0) {
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        Long intendedTime = firingCal.getTimeInMillis();
        alarmMgr.setRepeating(AlarmManager.RTC, intendedTime , AlarmManager.INTERVAL_DAY, alarmIntent);
    }

}
