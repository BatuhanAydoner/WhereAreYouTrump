package com.moonturns.batuhanaydoner.whereareyoutrump.GameFragements;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.moonturns.batuhanaydoner.whereareyoutrump.R;

public class ForgotPasswordFragment extends DialogFragment {

    private EditText etEmailChange;
    private ImageView imageChange;
    private ProgressBar progressBarForgotPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setCancelable(true);

        View view = inflater.inflate(R.layout.forgot_password_fragment, null);

        etEmailChange = (EditText) view.findViewById(R.id.etEmailChange);
        imageChange = (ImageView) view.findViewById(R.id.imageChange);
        progressBarForgotPassword=(ProgressBar) view.findViewById(R.id.progressBarForgotPassword);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sendMail();

    }

    private void getMailValue() {

        if (!etEmailChange.getText().toString().isEmpty()) {

            showProgressBar();
            FirebaseAuth.getInstance().sendPasswordResetEmail(etEmailChange.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        closeProgressBar();
                        Toast.makeText(getActivity(), R.string.passwordMail, Toast.LENGTH_LONG).show();
                        getDialog().dismiss();

                    }else {

                        closeProgressBar();
                        Toast.makeText(getActivity(), R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();

                    }

                }
            });

        } else {

            Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_LONG).show();

        }

    }

    private void sendMail() {

        imageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMailValue();

            }
        });

    }

    //progressbar gösterir
    private void showProgressBar() {

        progressBarForgotPassword.setVisibility(View.VISIBLE);

    }

    //progressbar kapatır
    private void closeProgressBar() {

        progressBarForgotPassword.setVisibility(View.INVISIBLE);

    }

}
