package empaticae4.hrker.com.empaticae4.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import empaticae4.hrker.com.empaticae4.R;

public class Splash extends Activity implements Animation.AnimationListener {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private ImageView logo;
    private Animation animFadein;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
}
