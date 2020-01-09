package com.moonturns.batuhanaydoner.whereareyoutrump.UserActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.TotalAccount;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.User;
import com.moonturns.batuhanaydoner.whereareyoutrump.MainActivity;
import com.moonturns.batuhanaydoner.whereareyoutrump.R;

public class RegisterActivity extends AppCompatActivity {

    private final String gameTicket = "5"; //günlük reklamsız oynama hakkı
    private final String totalScore = "0"; //toplam skor
    private final String adv = "5"; //oynamak için reklam hakkı
    private final String day="2";
    private final String user_total_playing="0";
    private final String high_score="0";
    private final String week_number="1";
    private final String won="0";
    private String user_uid=""; //kullanıcı uid

    private EditText etEmailRegister, etPasswordRegister, etPasswordAgainRegister;
    private ImageView imageRegister;
    private ProgressBar progressBarRegister;

    private void crt() {

        etEmailRegister = (EditText) this.findViewById(R.id.etEmailRegister);
        etPasswordRegister = (EditText) this.findViewById(R.id.etPasswordRegister);
        etPasswordAgainRegister = (EditText) this.findViewById(R.id.etPasswordAgainRegister);

        imageRegister = (ImageView) this.findViewById(R.id.imageRegister);

        progressBarRegister = (ProgressBar) this.findViewById(R.id.progressBarRegister);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        crt();

        register();

    }

    //kayıt olmak için imageRegister listener
    private void register() {

        imageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerValues();

            }
        });

    }

    //kayıt olmak için girilen verilere bakar
    private void registerValues() {

        if (!etEmailRegister.getText().toString().isEmpty() && !etPasswordRegister.getText().toString().isEmpty() && !etPasswordAgainRegister.getText().toString().isEmpty()) {

            if (etPasswordRegister.getText().toString().length() >= 6) {

                if (etPasswordRegister.getText().toString().equals(etPasswordAgainRegister.getText().toString())) {

                    String email = etEmailRegister.getText().toString();
                    String password = etPasswordRegister.getText().toString();

                    newRegister(email, password);

                } else {

                    showMessage(R.string.same_password);

                }

            } else {

                showMessage(R.string.length_password);

            }

        } else {

            showMessage(R.string.empty_fields);

        }

    }

    //kullanıcıyı kayıt eder
    private void newRegister(final String email, final String password) {

        showProgressBar();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    user_uid=task.getResult().getUser().getUid().toString();
                    writeUserInfos(email, password);
                    updateTotalPlayer();

                } else {

                    closeProgressBar();
                    showMessage(R.string.register_error);
                    FirebaseAuth.getInstance().signOut();

                }

            }
        });

    }

    //veritabanına kullanıcı hakkında olan bilgileri yazar
    private void writeUserInfos(String email, String password) {

        final String userName = email.substring(0, email.indexOf("@")).toString();

        writeUserNameUpdateProfile(userName);

        User user = new User();
        user.setUser_name(userName);
        user.setEmail(email);
        user.setUser_password(password);
        user.setTotal_score(totalScore);
        user.setUser_uid(user_uid);
        user.setGame_ticket(gameTicket);
        user.setAdv(adv);
        user.setDay(day);
        user.setTotal_playing(user_total_playing);
        user.setHigh_score(high_score);
        user.setWeek_number(week_number);
        user.setWon(won);

        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    sendVerifyEmail();

                } else {

                    Toast.makeText(RegisterActivity.this, R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    closeProgressBar();

                }

            }
        });
    }

    //kayıt olduktan sonra girilen email adresini kullanıcının kullanıcı ismi yapar
    private void writeUserNameUpdateProfile(String userName) {

        UserProfileChangeRequest.Builder kullanıcıBilgileri = new UserProfileChangeRequest.Builder().setDisplayName(userName);
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(kullanıcıBilgileri.build());

    }

    //onaylama maili gönderir
    private void sendVerifyEmail() {

        FirebaseUser kullanici = FirebaseAuth.getInstance().getCurrentUser();

            kullanici.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        closeProgressBar();
                        showMessage(R.string.sendVerifyEmail);
                        FirebaseAuth.getInstance().signOut();
                        openMainActivity();

                    } else {

                        closeProgressBar();
                        showMessage(R.string.sendVerifyEmailError);
                        FirebaseAuth.getInstance().signOut();

                    }

                }
            });

    }

    //MainActivity açılır intent
    private void openMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    //progressbar gösterir
    private void showProgressBar() {

        progressBarRegister.setVisibility(View.VISIBLE);

    }

    //progressbar kapatır
    private void closeProgressBar() {

        progressBarRegister.setVisibility(View.INVISIBLE);

    }

    //toast mesaj gösterir
    private void showMessage(int message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    //veritabanında her yeni kayıttan sonra toplam oyuncu sayısını günceller
    private void updateTotalPlayer() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query sorgu = reference.child("totalplayer");
        sorgu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String totalPlayer = dataSnapshot.getValue(TotalAccount.class).getTotalaccount();
                long totalAccount = Long.valueOf(totalPlayer);
                totalAccount++;

                FirebaseDatabase.getInstance().getReference().child("totalplayer").child("totalaccount").setValue(String.valueOf(totalAccount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
