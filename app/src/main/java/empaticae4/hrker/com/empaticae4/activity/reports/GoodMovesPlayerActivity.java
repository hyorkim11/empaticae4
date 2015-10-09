package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.main.MainActivity;

public class GoodMovesPlayerActivity extends Activity {

    private static MediaPlayer mediaPlayer;
    private int mediakey;

    private Button bPlay;

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

        bPlay = (Button) findViewById(R.id.bMp3);

        if (mediakey == 1) {
            // CONTACT
            bPlay.setVisibility(View.INVISIBLE);

        } else if (mediakey == 2) {
            // MP3
            bPlay.setVisibility(View.VISIBLE);
            //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.SOOTHINGMP3);

        } else if (mediakey == 3) {
            // MEDITATION
            bPlay.setVisibility(View.VISIBLE);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.meditation_audio);

        } else {
            // ERROR
            Toast.makeText(GoodMovesPlayerActivity.this, "There was an error retrieving mediakey", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    mp3Pause();
                    bPlay.setBackgroundResource(R.mipmap.ic_play);
                } else {
                    mp3Play();
                    bPlay.setBackgroundResource(R.mipmap.ic_pause);
                }

            }
        });

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
