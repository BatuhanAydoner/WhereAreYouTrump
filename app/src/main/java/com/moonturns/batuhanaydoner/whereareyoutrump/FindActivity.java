package com.moonturns.batuhanaydoner.whereareyoutrump;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FindActivity extends AppCompatActivity {

    private ImageView imageSound;

    private MediaPlayer mediaPlayer;

    private boolean sound = true; //sesin açılıp kapnanmasına göre değer alır

    private int currentSound = 0;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser kullanici;

    private long totalScore = 0; //kullanıcının veritabanından alınan toplam skoru

    private boolean onlineGame = false;

    private ImageView imageBack;

    private int scoreLeader=0;

    private void crt() {

        imageSound = (ImageView) this.findViewById(R.id.imageSound);
        imageBack=(ImageView) this.findViewById(R.id.imageBack);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        crt();

        onlineGame = getIntent().getBooleanExtra("onlineGame", onlineGame);

        scoreLeader=getIntent().getIntExtra("scoreLeader",scoreLeader);

        initAuthStateListener();

        playSoundEffect();
        closeOpenSound();

        backScoreActivity();

        addScore();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();

        if (currentSound > 0 && sound) {

            mediaPlayer.selectTrack(currentSound);
            mediaPlayer.start();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
        currentSound = mediaPlayer.getCurrentPosition();

    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);

    }

    //AuthStateListener
    private void initAuthStateListener() {

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                kullanici = firebaseAuth.getCurrentUser();

                if (kullanici != null && onlineGame) {

                    addScore();

                }else {

                    setContentView(R.layout.activity_offline_find);

                }

            }
        };

    }

    //sesi açıp kapatır
    private void closeOpenSound() {

        imageSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sound) {

                    sound = false;
                    imageSound.setImageResource(R.drawable.no_sound);
                    mediaPlayer.pause();
                    currentSound = mediaPlayer.getCurrentPosition();

                } else {

                    sound = true;
                    imageSound.setImageResource(R.drawable.sound);
                    mediaPlayer.selectTrack(currentSound);
                    mediaPlayer.start();

                }

            }
        });

    }

    //bu ekrana gelince ses efekt çalışır
    private void playSoundEffect() {

        mediaPlayer = MediaPlayer.create(this, R.raw.sound_finish);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

    }

    //kullanıcının toplam skoruna +200 ekler
    private void addScore() {

        totalScore=getIntent().getIntExtra("ots",0);
        totalScore=totalScore+200;

        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("total_score").setValue(String.valueOf(totalScore));

        changeLeader();

    }

    //verilen fazladan puandan sonra kullanıcıya liderlik geçtiyse liderlik alanını değiştirir
    private void changeLeader(){

        if (totalScore>scoreLeader){

            FirebaseDatabase.getInstance().getReference().child("leader").child("player").child("player_uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            FirebaseDatabase.getInstance().getReference().child("leader").child("player").child("leader_score").setValue(String.valueOf(totalScore));
            FirebaseDatabase.getInstance().getReference().child("leader").child("player").child("leader_username").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {



                    } else {

                        Toast.makeText(FindActivity.this, R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

    //ScoreActivity açılır
    private void backScoreActivity(){

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FindActivity.super.onBackPressed();

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
