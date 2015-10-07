package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import empaticae4.hrker.com.empaticae4.R;

public class GoodMovesPlayerActivity extends Activity {

    private static MediaPlayer mediaPlayer;
    private int mediakey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_moves_player);
        init();
    }

    private void init() {

        Intent in = getIntent();
        Bundle b = in.getExtras();
        mediakey = b.getInt("media_key");

        if (mediakey == 1) {
            // CONTACT

        } else if (mediakey == 2) {
            // MP3
            //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.SOOTHINGMP3);

        } else if (mediakey == 3) {

            // MEDITATION
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.meditation_audio);
            mp3Play();

        } else {
            // ERROR
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp3Pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mp3Resume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp3Stop();
    }

    public static void mp3Play(){
        mediaPlayer.start();
    }
    public static void mp3Stop(){
        mediaPlayer.release();
    }
    public static void mp3Pause(){
        mediaPlayer.pause();
    }
    public static void mp3Resume(){
        mediaPlayer.start();
    }
}
