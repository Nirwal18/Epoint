package com.nirwal.epoint.adaptors;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nirwal.epoint.R;
import com.nirwal.epoint.models.Question;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ExamResultAdaptor extends BaseAdapter{

    TextView question,answer,answerChoice;
    ArrayList<Question> _list;
    Question _question;

    public ExamResultAdaptor(ArrayList<Question> list){
        this._list=list;
    }


    @Override
    public int getCount() {
        if(_list==null){
            return 0;
        }
        return _list.size();
    }

    @Override
    public Object getItem(int position) {
        return _list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
         v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list,parent,false);

         _question = _list.get(position);

        question = v.findViewById(R.id.resultQuestion_txt);
        answer = v.findViewById(R.id.resultAnswer);
        answerChoice =v.findViewById(R.id.resultAnswerChoice);//

        question.setText("Q"+(position+1)+" "+_question.Question);
        answer.setText(_question.getAnswerText());
        answerChoice.setText(_question.getChosenOption());
        answerChoice.setTextColor(_question.isCorrect ? Color.GREEN:Color.RED);


        return v;
    }
}

