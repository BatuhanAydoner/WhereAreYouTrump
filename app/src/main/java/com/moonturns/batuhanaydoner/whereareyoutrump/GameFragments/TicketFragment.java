package com.moonturns.batuhanaydoner.whereareyoutrump.GameFragements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
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
import com.moonturns.batuhanaydoner.whereareyoutrump.FieldOfGameActivity;
import com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase.User;
import com.moonturns.batuhanaydoner.whereareyoutrump.MainActivity;
import com.moonturns.batuhanaydoner.whereareyoutrump.R;

public class TicketFragment extends DialogFragment implements RewardedVideoAdListener {

    private Button btnTicket,btnPlayOffline;
    private ImageView imageExit;

    private ProgressBar progressBarTicket;

    private String userTicket = "", userAdv = "";
    private int stateBtnTicket = 0;

    private boolean onlineGame = false;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private RewardedVideoAd mAd;

    private boolean adv = false; //reklam izlenileceği zaman true döner

    private Context context;

    private LoadingFragment loadingFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setCancelable(false);

        View view = inflater.inflate(R.layout.ticket_fragment, null);

        btnTicket = (Button) view.findViewById(R.id.btnTicket);
        btnPlayOffline=(Button) view.findViewById(R.id.btnPlayOffline);
        imageExit = (ImageView) view.findViewById(R.id.imageExit);
        progressBarTicket = (ProgressBar) view.findViewById(R.id.progressBarTicket);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MobileAds.initialize(getContext(), "ca-app-pub-5539936226294378~5338627173");

        mAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        getUserInfos();
        setImageExit();
        setBtnPlayOffline();

    }

    private void loadRewardedVideoAd() {

        if (!mAd.isLoaded()) {

            mAd.loadAd("ca-app-pub-5539936226294378/6819959722",
                    new AdRequest.Builder().build());

        }

    }

    private void startVideoAdd() {

        if (mAd.isLoaded() && adv == true) {

            mAd.show();

        }

    }

    @Override
    public void onResume() {
        mAd.resume(getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        mAd.pause(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAd.destroy(getActivity());
        super.onDestroy();
    }

    private void setImageExit() {

        imageExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();

            }
        });

    }

    //btnTicket listener
    private void watchAdv() {

        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stateBtnTicket();

            }
        });

    }

    private void stateBtnTicket() {

        switch (stateBtnTicket) {

            case 0:
                openFieldOfGameActivity();
                break;

            case 1:

                openLoadingFragment();
                showProgressBar();
                stateAdv();
                break;

            case 2:

                stateTicket();
                break;

        }

    }

    private void textBtnTicket() {

        if (userTicket.equals("0") && userAdv.equals("0")) {

            btnTicket.setText(R.string.playOffline);
            btnPlayOffline.setText(R.string.home);

        } else if (userTicket.equals("0") && !userAdv.equals("0")) {

            btnTicket.setText(context.getResources().getString(R.string.getTicket) + " ");
            btnTicket.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.watch_video, 0);
            stateBtnTicket = 1;

            btnPlayOffline.setText(R.string.playOffline);

        } else {

            btnTicket.setText(R.string.playOnline);
            stateBtnTicket = 2;

            btnPlayOffline.setText(R.string.playOffline);

        }

    }

    //btnPlayOffline listener
    private void setBtnPlayOffline(){

        btnPlayOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stateBtnPlayOffline();

            }
        });

    }

    private void stateBtnPlayOffline(){

        switch (stateBtnTicket) {

            case 0:
                openMainActivity();
                getDialog().dismiss();
                break;

            case 1:

                openFieldOfGameActivity();
                break;

            case 2:

                openFieldOfGameActivity();
                break;

        }

    }

    private void getUserInfos() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        showProgressBar();

        Query sorgu = reference.child("user").orderByKey().equalTo(user.getUid());
        sorgu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot single : dataSnapshot.getChildren()) {

                    User kullanici = single.getValue(User.class);
                    userTicket = kullanici.getGame_ticket();
                    userAdv = kullanici.getAdv();
                    closeProgressBar();

                }

                textBtnTicket();
                watchAdv();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //bilet durumuna göre işlem yapar
    private void stateTicket() {

        onlineGame = true;

        showProgressBar();

        int ut = Integer.valueOf(userTicket);
        ut = ut - 1;
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("game_ticket").setValue(String.valueOf(ut)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    closeProgressBar();
                    openFieldOfGameActivity();

                } else {

                    closeProgressBar();
                    Toast.makeText(getActivity(), R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    //FieldOfGameActivity açılır intent
    private void openFieldOfGameActivity() {

        Intent intent = new Intent(context.getApplicationContext(), FieldOfGameActivity.class);
        intent.putExtra("onlineGame", onlineGame);
        context.getApplicationContext().startActivity(intent);

    }

    //MainActivity açılır intent
    private void openMainActivity(){

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    //reklam durumuna göre işlem yapar
    private void stateAdv() {

        onlineGame = true;

        int ua = Integer.valueOf(userAdv);
        ua = ua - 1;
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("adv").setValue(String.valueOf(ua)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    adv = true;
                    startVideoAdd();

                } else {

                    closeProgressBar();
                    closeLoadingFragment();
                    Toast.makeText(getActivity(), R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    //reklam izlendikten sonra bir bilet verir
    private void advGameTicket(){

        showProgressBar();
        openLoadingFragment();
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("game_ticket").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    closeLoadingFragment();
                    closeProgressBar();

                }else {

                    closeLoadingFragment();
                    closeProgressBar();
                    Toast.makeText(getActivity(), R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    //progressBar gösterilir
    private void showProgressBar() {

        progressBarTicket.setVisibility(View.VISIBLE);

    }

    //progresBar gizlenir
    private void closeProgressBar() {

        progressBarTicket.setVisibility(View.INVISIBLE);

    }

    //LoadingFragment açılır
    private void openLoadingFragment(){

        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        loadingFragment=new LoadingFragment();
        loadingFragment.show(fragmentManager,"dialog");

    }

    //LoadingFragment kapatılır
    private void closeLoadingFragment(){

        if (loadingFragment!=null){

            loadingFragment.dismiss();

        }

    }

    @Override
    public void onRewardedVideoAdLoaded() {

        closeLoadingFragment();
        startVideoAdd();

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

        loadRewardedVideoAd();
        getDialog().dismiss();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        advGameTicket();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
