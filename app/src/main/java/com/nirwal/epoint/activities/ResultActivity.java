package com.nirwal.epoint.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nirwal.epoint.R;
import com.nirwal.epoint.fragments.ResultFragment;
import com.nirwal.epoint.models.Question;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ArrayList<Question> _questionList;
    private ResultFragment _resultFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Log.d("ResultActivity :" ,  "Created, On Create called");


        // getting data from intent
        if(getIntent().getExtras()!=null) {
            _questionList = (ArrayList<Question>) getIntent().getExtras().getSerializable("ResultArray");
        }
        // getting data from saved state
        if(savedInstanceState != null){
            _questionList = (ArrayList<Question>) savedInstanceState.getSerializable("questionList");
        }

        showResult();
    }


    void showResult(){



        if(_resultFragment==null){_resultFragment = new ResultFragment();}
        Bundle bundle = new Bundle();
        if(_questionList!=null){
            bundle.putSerializable("ResultArray",_questionList);
            Log.d("ResultActivity :" ,  String.valueOf(_questionList.size()));
        }
        _resultFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.result_viewPager,_resultFragment,"result_fragment")
                .commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("questionList",_questionList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _questionList=null;
    }
}
