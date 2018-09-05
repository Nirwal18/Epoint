package com.nirwal.epoint.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.nirwal.epoint.ExamActivity;
import com.nirwal.epoint.R;
import com.nirwal.epoint.adaptors.ExamResultAdaptor;
import com.nirwal.epoint.models.Question;

import java.util.ArrayList;

/**
 * Created by Nirwal on 3/27/2018.
 */

public class ResultFragment extends Fragment {

    private TextView scoreTxt,totalQuestionTxt,answereQuestionTxt;
    private ListView recyclerView;
    private ExamActivity examActivity;

    public ResultFragment(){
        // default Contructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        examActivity =(ExamActivity)this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.result_fragment, container,false);
        scoreTxt = v.findViewById(R.id.score_txt);
        totalQuestionTxt = v.findViewById(R.id.totalQuestion_txt);
        answereQuestionTxt = v.findViewById(R.id.answeredQuestion_txt);
        recyclerView = v.findViewById(R.id.resultListView_rcyle);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Init();
        return v;
    }
    void  Init(){
        String[] data = examActivity.getResult();
        answereQuestionTxt.setText(data[0]);
        totalQuestionTxt.setText(data[1]);
        scoreTxt.setText("Score : "+data[2]);
        recyclerView.setAdapter(new ExamResultAdaptor(examActivity.getQuestionList()));
    }


}
