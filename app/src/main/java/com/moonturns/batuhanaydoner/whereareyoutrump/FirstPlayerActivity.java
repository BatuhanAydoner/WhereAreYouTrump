package com.moonturns.batuhanaydoner.whereareyoutrump;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.Leader;
import com.moonturns.batuhanaydoner.whereareyoutrump.GameFragements.TicketFragment;

public class FirstPlayerActivity extends AppCompatActivity {

    private TextView txtUserName,txtFirstPlayerScore,txtReward,txtWinner;
    private ImageView imageWinner;
    private Button btnJoinCompetition;

    private String leaderUsername="";
    private String reward ="-"; //haftanın ödülü
    private String totalScore="-";
    private String stateWeekGame=""; //haftanın bitip bitmediğine göre play stop değerlerini alır

    private void crt() {

        txtUserName=(TextView) this.findViewById(R.id.txtUserName);
        txtFirstPlayerScore = (TextView) this.findViewById(R.id.txtFirstPlayerScore);
        txtReward=(TextView) this.findViewById(R.id.txtReward);
        txtWinner=(TextView) this.findViewById(R.id.txtWinner);
        imageWinner=(ImageView) this.findViewById(R.id.imageWinner);
        btnJoinCompetition=(Button) this.findViewById(R.id.btnJoinCompetition);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_player);
        crt();

        getFirstPlayer();
        playOnline();

    }

    //btnJoinCompetitionlistener
    private void playOnline(){

        btnJoinCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openTicketFragment();

            }
        });

    }

    //TicketFragment açılır
    private void openTicketFragment(){

        FragmentManager fragmentManager=this.getSupportFragmentManager();
        TicketFragment ticketFragment=new TicketFragment();
        ticketFragment.show(fragmentManager,"dialog");

    }

    //lider oyuncunun bilgilerini alır
    private void getFirstPlayer() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

         Query sorgu=reference.child("leader").orderByKey();
         sorgu.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 for (DataSnapshot single : dataSnapshot.getChildren()) {

                     leaderUsername =single.getValue(Leader.class).getLeader_username();
                     totalScore=single.getValue(Leader.class).getLeader_score();
                     reward = single.getValue(Leader.class).getReward();
                     stateWeekGame=single.getValue(Leader.class).getWeek();

                     txtUserName.setText(leaderUsername);
                     txtFirstPlayerScore.setText(totalScore);
                     txtReward.setText(reward);

                 }

                 stateWeek();

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

    }

    //hafta bittiyse imageWinner değişir
    private void stateWeek(){

        if (!stateWeekGame.equals("play")){

            imageWinner.setImageResource(R.drawable.winner);
            txtWinner.setVisibility(View.VISIBLE);

        }else {

            imageWinner.setImageResource(R.drawable.first);
            txtWinner.setVisibility(View.INVISIBLE);

        }

    }

}
