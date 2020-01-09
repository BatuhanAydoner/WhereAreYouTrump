package com.moonturns.batuhanaydoner.whereareyoutrump;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NewsActivity extends AppCompatActivity {

    private ImageView imageNews;

    private MediaPlayer mediaPlayer;

    private boolean stateSound=false;

    private boolean onlineGame=false;

    private int scoreLeader=0;

    private void crt(){

        imageNews=(ImageView) this.findViewById(R.id.imageNews);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        crt();

        stateSound=getIntent().getBooleanExtra("stateSound",false);

        onlineGame=getIntent().getBooleanExtra("onlineGame",onlineGame);

        scoreLeader=getIntent().getIntExtra("scoreLeader",scoreLeader);

        setImageNews();
        playMusic();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer!=null){

            mediaPlayer.stop();

        }

    }

    //görsele tıklanıldığında FindActivity açılır
    private void setImageNews(){

        final int ots=getIntent().getIntExtra("ots",0);

        imageNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(NewsActivity.this,FindActivity.class);
                intent.putExtra("onlineGame",onlineGame);
                intent.putExtra("ots",ots);
                intent.putExtra("scoreLeader",scoreLeader);
                startActivity(intent);
                finish();

            }
        });

    }

    //activity açılınca müzük başlar
    private void playMusic(){

        if (stateSound){

            mediaPlayer=MediaPlayer.create(this,R.raw.game_music_news);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
