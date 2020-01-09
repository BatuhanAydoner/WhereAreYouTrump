package com.moonturns.batuhanaydoner.whereareyoutrump;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.Renovation;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.User;

public class OpenActivity extends AppCompatActivity {

    private final String gameTicket = "5"; //günlük reklamsız oynama hakkı
    private final String adv = "5"; //toplam skor

    private String currentDay = ""; //hak yenileme için gün sayısı
    private String userDay = ""; //hak yenileme için gün sayısı

    private String userGameTicket = "";
    private String userAdv = "";
    private String week_number ="";
    private String user_week_number="";
    private String updatingState="";

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        initAuthStateListener();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);

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

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    waitOpen();

                } else {

                    openMainActivity();

                }

            }
        };

    }

    //veritabanına erişmeden önce 1 saniye bekletilir
    private void waitOpen() {

        CountDownTimer timer = new CountDownTimer(1000, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                getUserDay();

            }
        }.start();

    }

    //kullanıcının hak yenilemesi için yeni gün bilgisi alınır
    private void getUserDay() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query sorgu = reference.child("user").orderByKey().equalTo(user.getUid());
        sorgu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot single : dataSnapshot.getChildren()) {

                    User user = single.getValue(User.class);
                    userDay = user.getDay();
                    userGameTicket = user.getGame_ticket();
                    userAdv = user.getAdv();
                    user_week_number =user.getWeek_number();
                    getDay();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Bilet ve reklam hakkı için gün sayısını veritabanından alır
    private void getDay() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query sorgu = reference.child("renovation");
        sorgu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentDay = dataSnapshot.getValue(Renovation.class).getDay();
                week_number=dataSnapshot.getValue(Renovation.class).getWeek_number();
                updatingState=dataSnapshot.getValue(Renovation.class).getUpdating();
                renovation(userDay, currentDay,week_number,user_week_number);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //kullanıcının gün sayısı mevcut gün sayısından az ise bilet ve reklam hakkı yenilemesi yapılır
    private void renovation(String dayUser, String dayCurrent, String wn,String uwn) {

        if (!wn.equals(uwn)){

            newTotalScore();
            updateUserPlaying();

        }else if (dayUser.equals(dayCurrent)) {

            openMainActivity();

        } else if (dayCurrent.equals("0")) {

            newTotalScore();

        } else {

            updateUserPlaying();

        }

    }

    //kullanıcının toplam skoru sıfırlanır
    private void newTotalScore() {

        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("total_score").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                updateUserPlaying();

            }
        });

    }

    //kullanıcının oynama haklarını ve gün sayısını yeniler
    private void updateUserPlaying() {

        String[] userInfos = {"adv", "game_ticket", "day"};
        String[] userValues = {adv, gameTicket, currentDay};

        for (int i = 0; i < userInfos.length; i++) {

            final int finalI = i;
            FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child(userInfos[i]).setValue(userValues[i]).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        if (finalI == 2) {

                            openMainActivity();

                        }

                    }

                }
            });

        }

    }

    //MainActivity açılır intent
    private void openMainActivity() {

        CountDownTimer timer = new CountDownTimer(1000, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(OpenActivity.this, MainActivity.class);
                intent.putExtra("updating",updatingState);
                startActivity(intent);
                finish();

            }
        }.start();

    }

}
