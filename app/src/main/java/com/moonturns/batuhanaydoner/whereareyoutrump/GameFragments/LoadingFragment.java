package com.moonturns.batuhanaydoner.whereareyoutrump.GameFragements;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.moonturns.batuhanaydoner.whereareyoutrump.R;

public class LoadingFragment extends DialogFragment {

    private ProgressBar loadingProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setCancelable(false);

        View view=inflater.inflate(R.layout.loading_fragment,null);
        loadingProgressBar=(ProgressBar) view.findViewById(R.id.loadingProgressBar);

        return view;
    }
}
