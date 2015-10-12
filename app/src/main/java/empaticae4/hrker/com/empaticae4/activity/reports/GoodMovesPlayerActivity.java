package empaticae4.hrker.com.empaticae4.activity.reports;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import empaticae4.hrker.com.empaticae4.R;
import empaticae4.hrker.com.empaticae4.main.MainActivity;

public class GoodMovesPlayerActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private int mediakey;

    private Button bPlay;
    private TextView tvMarquee;

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

        tvMarquee = (TextView) findViewById(R.id.marqueeText);
        tvMarquee.setSelected(true);

        bPlay = (Button) findViewById(R.id.bMp3);
        mediaPlayer = new MediaPlayer();

        if (mediakey == 1) {
            // CONTACT
            bPlay.setVisibility(View.INVISIBLE);
            tvMarquee.setVisibility(View.INVISIBLE);

        } else if (mediakey == 2) {
            // MP3
            bPlay.setVisibility(View.VISIBLE);
            tvMarquee.setText("Personal soothing soundtrack is currently playing. Enjoy!");
            File root = Environment.getExternalStorageDirectory();

            if (root.canRead()) {

                File dir = new File(root.getAbsolutePath() + "/mtmFile");
                File file = new File(dir, "soothingMusic.mp3");

                try {
                    Uri mp3Uri = Uri.fromFile(file);  // initialize Uri here
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(getApplicationContext(), mp3Uri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (mediakey == 3) {
            // MEDITATION
            bPlay.setVisibility(View.VISIBLE);
            tvMarquee.setText("A calming meditation track is currently playing.");
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.meditation_audio);

        } else if (mediakey == 4) {
          // ACCESSED FROM MAIN MENU


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
        bPlay.setBackgroundResource(R.mipmap.ic_play);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mp3Play();
        bPlay.setBackgroundResource(R.mipmap.ic_pause);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp3Stop();
    }

    private void mp3Play(){
        mediaPlayer.start();
    }
    private void mp3Stop(){
        mediaPlayer.release();
    }
    private void mp3Pause(){
        mediaPlayer.pause();
    }


}
