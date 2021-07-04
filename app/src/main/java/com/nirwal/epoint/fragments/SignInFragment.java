package com.nirwal.epoint.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.activities.AuthanticationActivity;

public class SignInFragment extends Fragment implements View.OnClickListener {

    MyApp _app;
    EditText _userOrEmailTxt, _passTxt;
    Button _signInBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in,container,false);
        _app = (MyApp) getActivity().getApplication();
        _userOrEmailTxt = v.findViewById(R.id.frag_signIn_userName_or_email);
        _passTxt = v.findViewById(R.id.frag_signIn_password);
        _signInBtn = v.findViewById(R.id.frag_signIn_signInBtn);

        //register on click listener
        v.findViewById(R.id.frag_signIn_createAccount).setOnClickListener(this);
        v.findViewById(R.id.frag_signIn_forgotPassword).setOnClickListener(this);
        _signInBtn.setOnClickListener(this);

        //keyBoard done button click listener
        _passTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    signIn();
                }
                return false;
            }
        });



        return v;
    }



    void signIn(){
        String email = _userOrEmailTxt.getText().toString();
        String pass = _passTxt.getText().toString();

        _passTxt.setEnabled(false);
        _userOrEmailTxt.setEnabled(false);
        _signInBtn.setEnabled(false);

        // TODO sign into account
        Task<AuthResult> signInTask = ((MyApp)getActivity().getApplication()).getFirebaseAuth().signInWithEmailAndPassword(email, pass);
        signInTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String result = authResult.getUser().getDisplayName()== null ? authResult.getUser().getEmail() : authResult.getUser().getDisplayName();
                Toast.makeText(getActivity(),"Sign in as '"+result+"'",Toast.LENGTH_LONG).show();
            }
        });

        signInTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Sign failure : " +e.getMessage(),Toast.LENGTH_LONG).show();
                _passTxt.setEnabled(true);
                _userOrEmailTxt.setEnabled(true);
                _signInBtn.setEnabled(true);
            }
        });
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.frag_signIn_createAccount:{
            // TODO show SignUp fragment
            if(getActivity().getClass() == AuthanticationActivity.class){
                ((AuthanticationActivity)getActivity()).showSignUpFragment();
            }

            break;
        }
        case R.id.frag_signIn_signInBtn:{
            signIn();
            break;
        }
        case R.id.frag_signIn_forgotPassword:{
            Toast.makeText(getActivity(),"Functionality not implemented yet...",Toast.LENGTH_SHORT).show();
        }

    }

    }
}
