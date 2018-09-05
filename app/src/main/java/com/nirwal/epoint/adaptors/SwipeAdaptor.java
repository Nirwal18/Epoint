package com.nirwal.epoint.adaptors;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.nirwal.epoint.fragments.PaperFragment;
import com.nirwal.epoint.models.Question;

import java.util.List;

/**
 * Created by nirwal on 3/10/2018.
 */


public class SwipeAdaptor extends FragmentStatePagerAdapter {


private List<Question> questions;
private Fragment fragment;

public SwipeAdaptor(FragmentManager fm, List<Question> questions) {
super(fm);
this.questions=questions;
}

@Override
public Fragment getItem(int position) {
fragment = new PaperFragment();
Question question = questions.get(position);
Bundle bundle= new Bundle();
bundle.putSerializable("data",question);
bundle.putInt("QuestionNo",position+1);
fragment.setArguments(bundle);
return fragment;
}


@Override
public int getCount() {
return questions.size();
}

}
