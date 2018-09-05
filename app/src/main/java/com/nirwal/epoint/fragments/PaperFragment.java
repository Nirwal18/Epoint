package com.nirwal.epoint.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nirwal.epoint.ExamActivity;
import com.nirwal.epoint.R;
import com.nirwal.epoint.models.Question;


public class PaperFragment extends Fragment {

    private ExamActivity examActivity;
    private TextView questionTextView;
    private Question question;
    private RadioGroup radioGroup;
    private RadioButton a;
    private RelativeLayout relativeLayout;
    private int qNo;


    public PaperFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        examActivity =(ExamActivity)this.getActivity();
        Init();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.paper_fragment, container, false);
        Bundle bundle = getArguments();

        // Inflate the layout for this fragment
        radioGroup = v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                examActivity.postCompletedStatus(qNo-1,String.valueOf(checkedId));
            }
        });

        questionTextView=v.findViewById(R.id.exam_question_txt1);
        relativeLayout = v.findViewById(R.id.imageRelativelayout);

        setAndUpdateView();
        return v;
    }


    void Init()
    {
        Bundle bundle = getArguments();
        qNo = bundle.getInt("QuestionNo");
        question = (Question) bundle.getSerializable("data");
    }


    void setAndUpdateView()
    {
        int index=1;
        for (String option:question.Options) {
            a = new RadioButton(getActivity());
            a.setText(option);
            a.setId(index);
            radioGroup.addView(a);
            index++;
        }
        questionTextView.setText("Q"+qNo+": "+question.Question);

        if(question.isImageavailable())
        {
            relativeLayout.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        questionTextView = null;

        question=null;
        relativeLayout =null;
        examActivity = null;
    }
}
