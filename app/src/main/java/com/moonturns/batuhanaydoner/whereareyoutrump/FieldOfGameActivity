package com.moonturns.batuhanaydoner.whereareyoutrump;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.moonturns.batuhanaydoner.whereareyoutrump.database.ScoreDatabase;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class FieldOfGameActivity extends AppCompatActivity {

    private static final long countDownReady = 4000; //geri sayımın süresi
    private static long countDownGame = 30000; //oyunun geri sayim süresi
    private static final long timeOfStop = 250; // resmin değiştirilme süresi milisaniye

    private ImageView[] listImageTrump;
    private ImageView imageTrump1, imageTrump2, imageTrump3, imageTrump4, imageTrump5, imageTrump6, imageTrump7, imageTrump8, imageTrump9;
    private ImageView imageScore, imageTime, imageSound, imageTicket;
    private TextView txtGeriSayim, txtTouch, txtTime, txtMoreTime, txtMoreScore;

    private int touchCount = 0; //touch sayısı
    private int getHighScore = 10000;
    private int replayScore = 0; //oyunu replay yapınca artar
    private int touchTime = 0; //time görseline dokulduğunda artar

    private Handler handler;
    private Runnable run;

    private CountDownTimer geriSayim;
    private CountDownTimer gameTime;

    private long pauseTime = 0;

    private boolean startedGame = false; //geri sayım başladığında true olur

    private boolean sound;
    public static MediaPlayer mediaPlayer = null;
    int musicStopTime = 0; //müzik duldurulduğu zaman kaldığı süreyi alır

    private int[] listMusic; //oyun müziklerinin listesi
    private int music = 0; //çalan müzik

    private boolean finishGame = false; //oyun bittiğinde openActivity çalıştığında true olur ve müziğin devam etmesini sağlar
    private boolean onlineGame = false;

    private InterstitialAd mInterstitialAd; //reklam

    private void crt() {

        imageTrump1 = (ImageView) this.findViewById(R.id.imageTrump1);
        imageTrump2 = (ImageView) this.findViewById(R.id.imageTrump2);
        imageTrump3 = (ImageView) this.findViewById(R.id.imageTrump3);
        imageTrump4 = (ImageView) this.findViewById(R.id.imageTrump4);
        imageTrump5 = (ImageView) this.findViewById(R.id.imageTrump5);
        imageTrump6 = (ImageView) this.findViewById(R.id.imageTrump6);
        imageTrump7 = (ImageView) this.findViewById(R.id.imageTrump7);
        imageTrump8 = (ImageView) this.findViewById(R.id.imageTrump8);
        imageTrump9 = (ImageView) this.findViewById(R.id.imageTrump9);
        imageScore = (ImageView) this.findViewById(R.id.imageScore);
        imageTime = (ImageView) this.findViewById(R.id.imageTime);
        imageSound = (ImageView) this.findViewById(R.id.imageSound);
        imageTicket = (ImageView) this.findViewById(R.id.imageTicket);

        txtGeriSayim = (TextView) this.findViewById(R.id.txtGeriSayim);
        txtTouch = (TextView) this.findViewById(R.id.txtTouch);
        txtTime = (TextView) this.findViewById(R.id.txtTime);
        txtMoreTime = (TextView) this.findViewById(R.id.txtMoreTime);
        txtMoreScore = (TextView) this.findViewById(R.id.txtMoreScore);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_of_game);
        crt();

        initInterstitial();

        onlineGame = getIntent().getBooleanExtra("onlineGame", false);

        handler = new Handler();

        getHighScore();
        listOfImages();

        stateSound();
        setImageSound();

    }

    @Override
    protected void onResume() {
        super.onResume();

        resumeCountDown();

        resumeMusic();

    }

    @Override
    protected void onStart() {
        super.onStart();

        replayScoreTime();

        hintTime();
        totalPlayingTime();

    }

    @Override
    protected void onPause() {
        super.onPause();

        stopedGame();

        pauseMusic();

    }

    //oyun oynanırken durdurulursa geri açıldığında geri sayımı devam ettirir
    private void resumeCountDown() {

        if (pauseTime != 0 && startedGame) {

            setStartGame(pauseTime);

        } else {

            setGeriSayim(countDownReady);

        }

    }

    //activity geri plandayken geri geldiğinde müzik devam ettirilir
    private void resumeMusic() {

        if (musicStopTime > 0 && sound) {

            mediaPlayer.selectTrack(musicStopTime);
            mediaPlayer.start();

        }

    }

    //oyunu 20 defa üst üste oynayınca zamanı 60 yapar
    private void replayScoreTime() {

        replayScore = getIntent().getIntExtra("replayScore", 0);

        if (replayScore >= 20) {

            txtMoreTime.setVisibility(View.VISIBLE);
            countDownGame = 60000;
        }

    }

    //oyundan çıkılma durumlarına göre gerisayımları durdurur
    private void stopedGame() {

        if (startedGame) {

            gameTime.cancel();

        }

        if (!startedGame) {

            geriSayim.cancel();

        }

        pauseTime = Long.valueOf(txtTime.getText().toString());
        pauseTime = pauseTime * 1000;

    }

    //activity kapatıldığı zaman müzik durdurulur ama müzik devam ediyorsa ve süre bittiyse müzik devam eder
    private void pauseMusic() {

        if (mediaPlayer != null && mediaPlayer.isPlaying() && !finishGame) {

            mediaPlayer.pause();
            musicStopTime = mediaPlayer.getCurrentPosition();

        }

    }

    //images listener
    public void imageTouch(View view) {

        touchCount++;
        txtTouch.setText("" + touchCount);

        redTouch();

        if (touchCount >= getHighScore + 2) {


        } else if (touchCount > getHighScore) {

            imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_2));
            moreTime();
            moreScore();

        }

        scoreImage();

    }

    //oyuna hazırlanmak için geçen süre countdown
    private void setGeriSayim(final long time) {

        geriSayim = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {

                txtGeriSayim.setText("" + l / 1000);
                imageSound.setImageTintList(ColorStateList.valueOf(Color.WHITE));

            }

            @Override
            public void onFinish() {

                imageSound.setImageTintList(ColorStateList.valueOf(Color.BLACK));


                startedGame = true;
                txtGeriSayim.setVisibility(View.INVISIBLE);
                txtMoreTime.setVisibility(View.INVISIBLE);

                setStartGame(countDownGame);
                setGame();

                surpriseTime();
                ticketTime();

            }
        }.start();

    }

    //oyunu oynarken geçen süre countdown
    private void setStartGame(long timeOfGame) {

        gameTime = new CountDownTimer(timeOfGame, 1000) {
            @Override
            public void onTick(long l) {

                txtTime.setText("" + l / 1000);

                if (txtTime.getText().equals("10")) {

                    txtTime.setTextColor(Color.RED);

                }

            }

            @Override
            public void onFinish() {

                txtTime.setText("0");

                handler.removeCallbacks(run);
                for (int i = 0; i < listImageTrump.length; i++) {

                    listImageTrump[i].setVisibility(View.INVISIBLE);

                }

                openScoreActivity();
                showInterstitial();

            }
        }.start();

    }

    //resimleri listeler
    private void listOfImages() {

        listImageTrump = new ImageView[]{imageTrump1, imageTrump2, imageTrump3, imageTrump4, imageTrump5, imageTrump6, imageTrump7, imageTrump8, imageTrump9};

        for (int i = 0; i < listImageTrump.length; i++) {

            listImageTrump[i].setVisibility(View.INVISIBLE);

        }

    }

    //resim değiştirilir
    private void setGame() {

        run = new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < listImageTrump.length; i++) {

                    listImageTrump[i].setVisibility(View.INVISIBLE);

                }

                Random random = new Random();
                int imageNumber = random.nextInt(8 - 0);

                listImageTrump[imageNumber].setVisibility(View.VISIBLE);

                handler.postDelayed(run, timeOfStop);

            }
        };

        handler.post(run);

    }

    //high score alınır
    private void getHighScore() {

        if (onlineGame) {

            getHighScore = getIntent().getIntExtra("highScore", 0);

        } else {

            Realm realm = Realm.getDefaultInstance();

            RealmResults<ScoreDatabase> realmResults = realm.where(ScoreDatabase.class).findAll();

            if (realmResults.size() != 0) {

                getHighScore = realmResults.get(0).getScore();

            }

        }

    }

    //ScoreActivity açılır
    private void openScoreActivity() {

        finishGame = true;
        replayScore++;
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("score", touchCount);
        intent.putExtra("replayScore", replayScore);
        intent.putExtra("stateSound", sound);
        intent.putExtra("onlineGame", onlineGame);
        startActivity(intent);
        finish();

    }

    //skor 35 ve üstü olduğu zaman txtTouch kırmızı olur
    private void redTouch() {

        if (txtTouch.getText().equals("35")) {

            txtTouch.setTextColor(Color.RED);

        }

    }

    //35 ve üstü skorlarda imageScore için görsel gelir
    private void scoreImage() {

        switch (touchCount) {

            case 35:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_6));

                break;

            case 37:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_1));

                break;

            case 39:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_7));

                break;

            case 41:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_4));

                break;

            case 43:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_8));

                break;

            case 45:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_3));

                break;

            case 47:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_2));

                break;

            case 49:

                imageScore.setBackground(getDrawable(R.drawable.kim_yong_un_9));

                break;

        }

    }

    //imageTime listener
    private void surpriseTime() {

        imageTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                touchTime++;

                if (touchTime == 18) {

                    moreTime();

                }

            }
        });

    }

    //yeni high score yapılınca 10 saniye verir
    private void moreTime() {

        if (onlineGame){

            gameTime.cancel();
            long time = Long.parseLong(txtTime.getText().toString());
            time = time + 10;
            time = time * 1000;
            setStartGame(time);

            if (time > 10000) {

                txtTime.setTextColor(Color.WHITE);

            }

            highScoreTime();

        }

    }

    //high score yapılınca +10 zamanın verildiği 3 saniye gözükür
    private void highScoreTime() {

        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

                txtMoreTime.setVisibility(View.VISIBLE);
                txtMoreTime.setText("+10");

            }

            @Override
            public void onFinish() {

                txtMoreTime.setVisibility(View.INVISIBLE);

            }
        }.start();

    }

    //high score yapılınca +5 score verir
    private void moreScore() {

        if (touchCount >= 20) {

            int currentScore = Integer.parseInt(txtTouch.getText().toString()) + 5;

            touchCount = currentScore;

            txtTouch.setText("" + currentScore);

            increaseScore();

        }

    }

    //high score yapılınca +5 score verildiği 3 saniye gözükür
    private void increaseScore() {

        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

                txtMoreScore.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFinish() {

                txtMoreScore.setVisibility(View.INVISIBLE);

            }
        };

    }

    //ipucuna göre zaman ve skor verir
    private void hintTime() {

        String moreTime = getIntent().getStringExtra("hintScore");

        if (moreTime != null) {

            switch (moreTime) {

                case "+15 saniye kazandın":

                    countDownGame = countDownGame + 15000;
                    txtMoreTime.setText("+15");

                    break;

                case "Saniye bileti kazandın,oyna ve kullan":

                    imageTicket.setVisibility(View.VISIBLE);

                    break;

            }

        }

    }

    //imageTicket listener
    private void ticketTime() {

        imageTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                increaseTime();

            }
        });

    }

    //ticket kullanılınca +20 saniye verir
    private void increaseTime() {

        gameTime.cancel();
        long currentTime = Integer.valueOf(txtTime.getText().toString()) * 1000 + 20000;
        setStartGame(currentTime);
        imageTicket.setVisibility(View.INVISIBLE);

    }

    //toplam oynama sayısı 5 ve katları ise +15 süre verir
    private void totalPlayingTime() {

        Realm realm = Realm.getDefaultInstance();

        RealmResults<ScoreDatabase> realmResults = realm.where(ScoreDatabase.class).findAll();
        if (realmResults.size() > 0) {

            int totalPlaying = realmResults.get(0).getPlaying();
            if (totalPlaying % 5 == 0) {

                countDownGame = countDownGame + 15000;

                if (txtTime.getText().equals("+15")) {

                    txtTime.setText("+30");

                } else {

                    txtTime.setText("+15");

                }

            }

            totalPlayingTicket(realmResults);

        }

    }

    //toplam oynama sayısı 8 ve katları ise ticket verir
    private void totalPlayingTicket(RealmResults<ScoreDatabase> realmResults) {

        if (realmResults.get(0).getPlaying() % 8 == 0) {

            imageTicket.setVisibility(View.VISIBLE);

        }
    }

    //sesin önceden kapatılıp kapatılmadığına bakar
    private void stateSound() {

        SharedPreferences preferences = this.getSharedPreferences("soundPreferences", Context.MODE_PRIVATE);

        sound = preferences.getBoolean("soundOpenClose", true);

        mediaPlayer = null;

        if (sound) {

            mediaPlayer = MediaPlayer.create(this, randomMusic());
            mediaPlayer.start();

        } else {

            imageSound.setImageResource(R.drawable.no_sound);

        }

    }

    //sesi açıp kapatır
    private void setImageSound() {

        imageSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sound) {

                    stopMusic();

                } else {

                    playMusic();

                }

            }
        });

    }

    //müziği durdurur
    private void stopMusic() {

        mediaPlayer.pause();
        musicStopTime = mediaPlayer.getCurrentPosition();
        sound = false;
        imageSound.setImageResource(R.drawable.no_sound);

    }

    //müziği oynatır
    private void playMusic() {

        if (mediaPlayer == null) {

            mediaPlayer = MediaPlayer.create(this, randomMusic());
            mediaPlayer.start();

        } else {

            mediaPlayer.selectTrack(musicStopTime);
            mediaPlayer.start();

        }

        musicStopTime = 0;
        sound = true;
        imageSound.setImageResource(R.drawable.sound);

    }

    //random rakama göre müzik açılır
    private int randomMusic() {

        listMusic = new int[]{R.raw.game_time_music2};

        //Random random = new Random();
        //music = random.nextInt(2 - 0);

        return listMusic[0];

    }

    //geçiş reklamı
    private void initInterstitial() {

        MobileAds.initialize(this,
                "ca-app-pub-5539936226294378~5338627173");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5539936226294378/6221968007");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    //geçiş reklamı gösterilir
    private void showInterstitial() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}

