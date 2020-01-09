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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moonturns.batuhanaydoner.whereareyoutrump.R;
import com.moonturns.batuhanaydoner.whereareyoutrump.UserActivities.RegisterActivity;

public class ConformationMailFragment extends DialogFragment {

    private EditText etEmailConformation, etPasswordConformation;
    private ImageView imageSend;
    private ProgressBar progressBarConformationMail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setCancelable(true);

        View view = inflater.inflate(R.layout.conformation_mail_fragment, null);

        etEmailConformation = (EditText) view.findViewById(R.id.etEmailConformation);
        etPasswordConformation = (EditText) view.findViewById(R.id.etPasswordConformation);
        imageSend = (ImageView) view.findViewById(R.id.imageSend);
        progressBarConformationMail = (ProgressBar) view.findViewById(R.id.progressBarConformationMail);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setImageSend();

    }

    private void setImageSend() {

        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getValues();

            }
        });

    }

    private void getValues() {

        if (!etEmailConformation.getText().toString().isEmpty() && !etPasswordConformation.getText().toString().isEmpty()) {

            String email = etEmailConformation.getText().toString();
            String password = etPasswordConformation.getText().toString();

            sendConformationMail(email, password);

        } else {

            Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_LONG).show();

        }

    }

    private void sendConformationMail(String email, String password) {

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        showProgressBar();
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    verifyMail();

                } else {

                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getActivity(), R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();
                    closeProgressBar();

                }

            }
        });

    }

    private void verifyMail() {

        FirebaseUser kullanici = FirebaseAuth.getInstance().getCurrentUser();

        kullanici.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(getActivity(), R.string.againConformationMail, Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    closeProgressBar();
                    getDialog().dismiss();

                } else {

                    Toast.makeText(getActivity(), R.string.sendVerifyEmailError, Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    closeProgressBar();
                    getDialog().dismiss();

                }

            }
        });

    }

    //progressbar gösterir
    private void showProgressBar() {

        progressBarConformationMail.setVisibility(View.VISIBLE);

    }

    //progressbar kapatır
    private void closeProgressBar() {

        progressBarConformationMail.setVisibility(View.INVISIBLE);

    }

}
