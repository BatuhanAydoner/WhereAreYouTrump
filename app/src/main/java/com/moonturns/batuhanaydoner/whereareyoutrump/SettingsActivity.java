package com.moonturns.batuhanaydoner.whereareyoutrump;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.User;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.ValueEventGameTicket;
import com.moonturns.batuhanaydoner.whereareyoutrump.GameFragements.TicketFragment;
import com.moonturns.batuhanaydoner.whereareyoutrump.database.ScoreDatabase;

import io.realm.Realm;
import io.realm.RealmResults;

public class SettingsActivity extends AppCompatActivity {

    private Realm realm;
    private RealmResults<ScoreDatabase> realmResults;

    private ValueEventGameTicket gameTicket;

    private EditText etUserName;
    private TextView txtEmail, txtCoins, txtHighScore, txtTotalScore, txtTicket;
    private ImageView imageUserName, imageTicket;
    private ProgressBar progressBarSettings;

    private FirebaseUser kullanici = FirebaseAuth.getInstance().getCurrentUser();

    private String userTicket = "";
    private String userAdv = "";

    private String user_week_number="";
    private String userUserName = "";
    private String currentDay = "";

    private boolean activeUser;

    private void crt() {

        etUserName = (EditText) this.findViewById(R.id.etUserName);
        txtEmail = (TextView) this.findViewById(R.id.txtEmail);
        txtCoins = (TextView) this.findViewById(R.id.txtCoins);
        imageUserName = (ImageView) this.findViewById(R.id.imageUserName);
        txtHighScore = (TextView) this.findViewById(R.id.txtHighScore);
        txtTotalScore = (TextView) this.findViewById(R.id.txtTotalScore);
        txtTicket = (TextView) this.findViewById(R.id.txtTicket);
        imageTicket = (ImageView) this.findViewById(R.id.imageTicket);

        progressBarSettings = (ProgressBar) this.findViewById(R.id.progressBarSettings);

    }

    private void crtOffline(){

        txtHighScore = (TextView) this.findViewById(R.id.txtHighScore);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activeUser = getIntent().getBooleanExtra("activeUser", false);

        stateUser();

    }

    //kullanıcının online durumuna bakar
    private void stateUser() {

        if (activeUser) {

            setContentView(R.layout.activity_settings);
            crt();

            setImageUserName();
            readUserInfos();
            setImageTicket();

        } else {

            setContentView(R.layout.activity_offline_settings);
            crtOffline();
            offlineScore();

        }

    }

    //internetsiz alandaki skor
    private void offlineScore(){

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(ScoreDatabase.class).findAll();
        if (realmResults.size()==0) {

            txtHighScore.setText("0");

        } else {

            int highScore=realmResults.get(0).getScore();
            txtHighScore.setText(String.valueOf(highScore));

        }

    }

    //kullanıcı bilgilerini veritabanından alır
    private void readUserInfos() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query sorgu = reference.child("user")
                .orderByKey()
                .equalTo(kullanici.getUid());
        sorgu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    User okunanKullanici = singleSnapshot.getValue(User.class);
                    etUserName.setText(okunanKullanici.getUser_name());
                    txtEmail.setText(okunanKullanici.getEmail().toString());
                    txtCoins.setText(okunanKullanici.getWon());
                    txtTotalScore.setText(okunanKullanici.getTotal_score());

                    userAdv = okunanKullanici.getAdv();
                    userUserName = okunanKullanici.getUser_name();
                    currentDay = okunanKullanici.getDay();
                    user_week_number=okunanKullanici.getWeek_number();

                }

                valueGameTicket();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    User okunanKullanici = singleSnapshot.getValue(User.class);

                    userTicket = okunanKullanici.getGame_ticket();

                    txtTicket.setText(userTicket);

                    currentDay = dataSnapshot.getValue(User.class).getDay();

                }

                getMoreTicket();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //imageUserName listener
    private void setImageUserName() {

        imageUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getValues();

            }
        });

    }

    //girilen değerleri alır
    private void getValues() {

        if (!etUserName.getText().toString().isEmpty()) {

            if (etUserName.getText().toString().equals(userUserName.toString())) {

                Toast.makeText(this, R.string.sameUserName, Toast.LENGTH_LONG).show();

            } else {


                String name = etUserName.getText().toString();

                saveUsername(name);

            }

        } else {

            Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_LONG).show();

        }

    }

    //kullanıcının bilgilerini günceller
    private void updateUserInfos(final String name) {

        showProgressBar();
        UserProfileChangeRequest.Builder bilgileriGuncelle = new UserProfileChangeRequest.Builder().setDisplayName(name);
        kullanici.updateProfile(bilgileriGuncelle.build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    closeProgressBar();

                    changeDatabaseInfos(name);

                } else {

                    closeProgressBar();
                    Toast.makeText(SettingsActivity.this, R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    //kullanıcının veritabanındaki bilgilerini günceller
    private void changeDatabaseInfos(String userName) {

        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("user_name").
                setValue(userName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    userUserName = etUserName.getText().toString();
                    Toast.makeText(SettingsActivity.this, R.string.changedUserName, Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(SettingsActivity.this, R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();

                }

                closeProgressBar();

            }
        });

    }

    //kullanıcı isminin varlığını veritabanında sorgular
    private void saveUsername(final String saveUsername) {

        showProgressBar();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query sorgu = reference.child("user").orderByChild("user_name").equalTo(saveUsername);
        sorgu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    closeProgressBar();
                    Toast.makeText(SettingsActivity.this, R.string.exists_username, Toast.LENGTH_LONG).show();

                } else {

                    updateUserInfos(saveUsername);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                closeProgressBar();

            }
        });

    }

    //imageTicket listener
    private void setImageTicket() {

        imageTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gameTicket.getWeek().equals("")) {

                    Toast.makeText(SettingsActivity.this, R.string.internet, Toast.LENGTH_LONG).show();

                } else if (gameTicket.getWeek().equals("play")) {

                    openTicketFragment();

                } else {

                    gameTicket.openWeekFragment();

                }

            }
        });

    }

    //TicketFragment açılır
    private void openTicketFragment() {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        TicketFragment ticketFragment = new TicketFragment();
        ticketFragment.show(fragmentManager, "dialog");

    }

    //kullanıcının plat ticket sayısı bittiyse bilet için reklam izlettirilir
    private void getMoreTicket() {

        if (userTicket.equals("0")) {

            imageTicket.setImageResource(R.drawable.more_ticket);

        } else {

            imageTicket.setImageResource(R.drawable.play_ticket);

        }

    }

    //veritabanında gün ve ayda değişiklik olursa işlem yapar
    private void valueGameTicket() {

        gameTicket = new ValueEventGameTicket(this, currentDay,user_week_number);
        gameTicket.newGame();

    }

    //progressbar gösterir
    private void showProgressBar() {

        progressBarSettings.setVisibility(View.VISIBLE);

    }

    //progressbar kapatır
    private void closeProgressBar() {

        progressBarSettings.setVisibility(View.INVISIBLE);

    }

}
