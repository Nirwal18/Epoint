package com.nirwal.epoint.adaptors;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.nirwal.epoint.fragments.PaperFragment;
import com.nirwal.epoint.models.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nirwal on 3/10/2018.
 */


public class SwipeAdaptor extends FragmentStatePagerAdapter {


private List<Question> questions;
private PaperFragment fragment;
private ArrayList<Fragment> _fragments;
private Map<Integer,PaperFragment> _pageRefrenceMap;

public SwipeAdaptor(FragmentManager fm, List<Question> questions) {
super(fm);
this.questions=questions;
this._pageRefrenceMap = new HashMap<>();
}

@Override
public Fragment getItem(int position) {
fragment = new PaperFragment();
Question question = questions.get(position);
Bundle bundle= new Bundle();
bundle.putSerializable("data",question);
bundle.putInt("QuestionNo",position+1);
fragment.setArguments(bundle);
_pageRefrenceMap.put(position,fragment);
return fragment;
}


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    _pageRefrenceMap.remove(position);
    super.destroyItem(container, position, object);
    }


    public PaperFragment getFragment(int index) {
        return _pageRefrenceMap.get(index);
    }


@Override
public int getCount() {
    if(questions==null){
        return 0;
    }
    return questions.size();
}

}
