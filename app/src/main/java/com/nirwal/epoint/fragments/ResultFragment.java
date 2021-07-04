package com.nirwal.epoint.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;


import com.nirwal.epoint.R;
import com.nirwal.epoint.adaptors.ExamResultAdaptor;
import com.nirwal.epoint.customViews.CustomListView;
import com.nirwal.epoint.models.Question;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nirwal on 3/27/2018.
 */

public class ResultFragment extends Fragment {

    private  ArrayList<Question> _questionList;

    private TextView scoreTxt,totalQuestionTxt,answereQuestionTxt;
    private CustomListView recyclerView;
   private ScrollView _resultScrollerView;

    int _totalQuestion;

    public ResultFragment(){
        // default Contructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.result_fragment, container,false);


        _resultScrollerView = v.findViewById(R.id.result_scroller_view);
        scoreTxt = v.findViewById(R.id.score_txt);
        totalQuestionTxt = v.findViewById(R.id.totalQuestion_txt);
        answereQuestionTxt = v.findViewById(R.id.answeredQuestion_txt);
        recyclerView = v.findViewById(R.id.resultListView_rcyle);
        Init();
        return v;
    }



    void  Init(){
        // gettin data from (activity intent) Bundle
        if (getArguments() != null) {
            _questionList = (ArrayList<Question>) getArguments().getSerializable("ResultArray");
        }

        if(_questionList==null) return;




        _totalQuestion = _questionList.size();
        answereQuestionTxt.setText(getAnsweredCount());
        totalQuestionTxt.setText(String.valueOf(_totalQuestion));
        scoreTxt.setText(getScore());
        recyclerView.setAdapter(new ExamResultAdaptor(_questionList));

        _resultScrollerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT > 15)
                    _resultScrollerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                _resultScrollerView.fullScroll(View.FOCUS_UP);
                _resultScrollerView.scrollTo(0,0);
            }
        });
    }


    private String getScore(){
        int score=0;
        for (int i = 0; i < _totalQuestion; i++) {
            if (_questionList.get(i).isCorrect) {
                score++;
            }
        }
        float scorePercentage =(float) (score*100)/_totalQuestion;
        return String.format(Locale.getDefault(),"%.2f", scorePercentage)+ "%";
    }

    private String getAnsweredCount(){
        int answeredCount=0;
        for (int i = 0; i < _totalQuestion; i++) {
            if (_questionList.get(i).isAnswered) {
                answeredCount++;
            }
        }
        return String.valueOf(answeredCount);
    }


}
