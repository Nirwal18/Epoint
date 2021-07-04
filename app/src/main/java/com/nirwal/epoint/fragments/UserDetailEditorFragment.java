package com.nirwal.epoint.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;

public class UserDetailEditorFragment extends Fragment {

    private TextView _nameTxt, _emailTxt, _mobileTxt;
    private ImageView _userImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_detail_editor,container,false);

        _nameTxt = v.findViewById(R.id.frag_update_user_name);
        _emailTxt = v.findViewById(R.id.frag_update_user_email);
        _mobileTxt = v.findViewById(R.id.frag_update_user_mobile);
        _userImage = v.findViewById(R.id.frag_update_user_image);

        FirebaseUser user = ((MyApp) getActivity().getApplication()).getFirebaseAuth().getCurrentUser();
        if(user!=null)updateUserDetailsUI(user);


        return v;
    }

    void updateUserDetailsUI(FirebaseUser user){
        _userImage.setImageURI(user.getPhotoUrl());
        _nameTxt.setText(user.getDisplayName());
        _emailTxt.setText(user.getEmail());
        _mobileTxt.setText(user.getPhoneNumber());

    }
}
