package com.nirwal.epoint.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nirwal.epoint.R;
import com.nirwal.epoint.fragments.SignInFragment;
import com.nirwal.epoint.fragments.SignUpFragment;

public class AuthanticationActivity extends Activity {

    Fragment _signInFrag;
    Fragment _signUpFrag;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authantication);

        if(_signInFrag==null) _signInFrag = new SignInFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.act_auth_vPager,_signInFrag)
                .commit();



    }


    public void showSignUpFragment(){

        if(_signUpFrag==null) _signUpFrag = new SignUpFragment();
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("signInFragment")
                .replace(R.id.act_auth_vPager,_signUpFrag)
                .commit();
    }


    public void showSignInFragment(){

        if(_signInFrag==null) _signInFrag = new SignInFragment();
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("signInFragment")
                .replace(R.id.act_auth_vPager,_signInFrag)
                .commit();

    }






}
