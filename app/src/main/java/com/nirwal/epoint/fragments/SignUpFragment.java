package com.nirwal.epoint.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.activities.AuthanticationActivity;

public class SignUpFragment extends Fragment implements View.OnClickListener {


    private Button _signUpBtn, _cancelBtn;
    private EditText _userName, _email, _password, _repeatPassword, _mobile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up,container,false);
        _email = v.findViewById(R.id.frag_signUp_email);
        _password = v.findViewById(R.id.frag_signUp_password);
        _repeatPassword = v.findViewById(R.id.frag_signUp_repeatPassword);
        //_mobile = v.findViewById(R.id.frag_signUp_mobileNo);
        _signUpBtn = v.findViewById(R.id.frag_signUp_signUpBtn);
        _cancelBtn = v.findViewById(R.id.frag_signUp_cancelBtn);


        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.frag_signUp_signUpBtn:{
                signUp();
                break;
            }

            case R.id.frag_signUp_cancelBtn:{

                // TODO show SignIn fragment
                if(getActivity().getClass() == AuthanticationActivity.class){
                    ((AuthanticationActivity)getActivity()).showSignInFragment();
                }

                break;
            }

        }

    }


    void signUp(){


        String email = _email.getText().toString();
        String password = _password.getText().toString();
        String repeatPassword = _repeatPassword.getText().toString();
       // String mobile = _mobile.getText().toString();

        if (email.isEmpty()){
            _email.setError("Can't be empty!");
            return;
        }

        if(password.equals(repeatPassword)){
            Task<AuthResult> createUserTask = ((MyApp) getActivity().getApplication()).getFirebaseAuth()
                    .createUserWithEmailAndPassword(email, password);
            createUserTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    // do something
                }
            });


            createUserTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // do something
                }
            });












        }
        else {
            _repeatPassword.setError("Password not matching error!");
            return;
        }





    }



}
