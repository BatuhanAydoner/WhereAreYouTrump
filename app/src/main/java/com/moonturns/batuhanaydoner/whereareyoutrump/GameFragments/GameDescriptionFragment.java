package com.moonturns.batuhanaydoner.whereareyoutrump.GameFragements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.moonturns.batuhanaydoner.whereareyoutrump.R;

public class GameDescriptionFragment extends DialogFragment implements View.OnClickListener {

    private Button btnClose;
    private Switch switchOnline;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        setCancelable(false);

        View view=inflater.inflate(R.layout.game_descriptions_fragment,null);

        switchOnline=(Switch) view.findViewById(R.id.switchOnline);

        btnClose=(Button) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        changeStateOnline();

    }

    private void changeStateOnline(){

        switchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){

                    switchOnline.setText(R.string.online);

                }else {

                    switchOnline.setText(R.string.offline);

                }

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view==btnClose){

            dismiss();

        }

    }
}
