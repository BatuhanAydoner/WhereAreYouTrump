package com.moonturns.batuhanaydoner.whereareyoutrump.GameFragements;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.moonturns.batuhanaydoner.whereareyoutrump.FieldOfGameActivity;
import com.moonturns.batuhanaydoner.whereareyoutrump.MainActivity;
import com.moonturns.batuhanaydoner.whereareyoutrump.R;
import com.moonturns.batuhanaydoner.whereareyoutrump.ScoreActivity;

public class WeekFragment extends DialogFragment {

    private ImageView imageExit;
    private Button btnPlayOffline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setCancelable(false);

        View view=inflater.inflate(R.layout.week_fragment,null);

        imageExit=(ImageView) view.findViewById(R.id.imageExit);
        btnPlayOffline=(Button) view.findViewById(R.id.btnPlayOffline);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        closeFragment();

        setBtnPlayOffline();

    }

    private void closeFragment(){

        imageExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();

            }
        });

    }

    //btnPlayOffline listener
    private void setBtnPlayOffline(){

        btnPlayOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFieldOfGameActivity();

            }
        });

    }

    //FieldOfActivity intent
    private void openFieldOfGameActivity() {

        Intent intent = new Intent(getActivity(), FieldOfGameActivity.class);
        intent.putExtra("onlineGame",false);
        startActivity(intent);
        getActivity().finish();

    }

}

