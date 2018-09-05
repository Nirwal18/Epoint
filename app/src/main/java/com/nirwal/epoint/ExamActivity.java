package com.nirwal.epoint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nirwal.epoint.adaptors.SwipeAdaptor;
import com.nirwal.epoint.fragments.ResultFragment;
import com.nirwal.epoint.models.Question;

import java.util.ArrayList;

public class ExamActivity extends AppCompatActivity {

    private MyApp _app;
    private ArrayList<Question> _questionList;
    private static FragmentManager fm;
    private static FragmentTransaction ft;
    private static SwipeAdaptor _swipeAdaptor;

    private int _totalQuestion=0;
    private int _questionCount=0;

    private ResultFragment resultFragment;
    private TextView _questionStatusTxt;
    private Button forwardBtn, backwordBtn,finishBtn;
    private ViewPager viewPager;
    private FrameLayout _resultLayout;
    private RelativeLayout _examToolbar;

    private static ProgressDialog progressDialog;
    private static String _id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_layout);
        viewPager = findViewById(R.id.exam_swiper_viewPager);
        _questionStatusTxt = findViewById(R.id.q_sts_txt);
        forwardBtn = findViewById(R.id.forward_btn);
        backwordBtn = findViewById(R.id.backword_btn);
        finishBtn = findViewById(R.id.finish_exam_btn);
        _examToolbar = findViewById(R.id.exam_toolbar);
        _resultLayout = findViewById(R.id.exam_result_viewPager);

        // progress dialog

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Loading from internet please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        init();

       // ft.replace(R.id.exam_result_viewPager,new ResultFragment(),"exam").commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progressDialog!= null){
            progressDialog.dismiss();
            progressDialog.cancel();
            progressDialog= null;
        }

        viewPager.removeOnPageChangeListener(onPageChangeListener);
        onPageChangeListener =null;
        valueEventListener =null;
        navigationClickListener = null;
        forwardBtn =null;
        backwordBtn=null;
        finishBtn = null;
        _examToolbar=null;
        _questionList =null;
        _app =null;
        _swipeAdaptor =null;
        _resultLayout=null;
        _questionStatusTxt=null;
        resultFragment=null;
        fm=null;
        ft=null;


    }


    void init(){

        _questionList = new ArrayList<>();
        _app = (MyApp) getApplication();


        fm = getSupportFragmentManager();
        ft=fm.beginTransaction();

        resultFragment = new ResultFragment();

        Bundle b = getIntent().getExtras();
        _id = b.getString("ID");
        loadSwiperFunction();
        forwardBtn.setOnClickListener(navigationClickListener);
        backwordBtn.setOnClickListener(navigationClickListener);
        finishBtn.setOnClickListener(navigationClickListener);
    }

    View.OnClickListener navigationClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.forward_btn: {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    break;
                }
                case R.id.backword_btn: {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    break;
                }
                case R.id.finish_exam_btn: {
                    finishTest();
                    break;
                }
            }

        }
    };

    public ArrayList<Question> getQuestionList(){
        return _questionList;
    }


    void finishTest(){
        //user interface setup
        viewPager.setVisibility(View.GONE);
        _resultLayout.setVisibility(View.VISIBLE);
        _examToolbar.setVisibility(View.GONE);
        ft.replace(R.id.exam_result_viewPager, resultFragment, "ResultFragment").commit();
    }


    public String[] getResult() {
        String[] data = new String[3];
        int score = 0;
        int totalQuestion= _questionList.size();
        data[0] = String.valueOf(_questionCount); //Answered
        data[1] = String.valueOf(totalQuestion); // Total Question
        for (int i = 0; i < totalQuestion; i++) {
            if (_questionList.get(i).isCorrect) {
                score++;
            }
        }
        float scorePercentage =(float) (score*100)/totalQuestion;
        data[2] = String.valueOf(scorePercentage)+ "%";
        return data;
    }


    void loadSwiperFunction(){
        //_questionList = _app.getSqlDb().readQuestionListById(id);
        _app.getFirebaseDatabase().getReference("OnlineQuestionList")
                .child(_id).addListenerForSingleValueEvent(valueEventListener);


    // page scroll event listener
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    ViewPager.OnPageChangeListener onPageChangeListener =new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            finishAndForwardBtnUIsetup(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // data gathring from firebase
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            // toolbar.setTitle(dataSnapshot.getValue(Paper.class).getTitle()); for future

            for (DataSnapshot item : dataSnapshot.getChildren()) {
                _questionList.add(item.getValue(Question.class));
            }
            _totalQuestion = _questionList.size();

            _swipeAdaptor = new SwipeAdaptor(fm,_questionList==null?new ArrayList<Question>():_questionList);
            viewPager.setAdapter(_swipeAdaptor);
            updateExamStatusOnUi(0,_totalQuestion);
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


// Finish button hide/show based on question no as position
    void finishAndForwardBtnUIsetup(int position){
        if (position == _questionList.size() - 1) {

            finishBtn.setVisibility(View.VISIBLE);
            forwardBtn.setVisibility(View.GONE);
        } else {

            if(forwardBtn.getVisibility()==View.GONE)
            {
                forwardBtn.setVisibility(View.VISIBLE);
                finishBtn.setVisibility(View.GONE);
            }

        }
    }

    //update answered question status on every question answered
    public void postCompletedStatus(int qNo, String  choose) {

        _questionList.get(qNo).questionNo = qNo + 1;
        _questionList.get(qNo).chosenOption=choose;
        _questionList.get(qNo).isCorrect = _questionList.get(qNo).Answer.equals(choose);

        if (!_questionList.get(qNo).isAnswered) {
            _questionList.get(qNo).isAnswered = true;
            _questionCount++;
            updateExamStatusOnUi(_questionCount,_totalQuestion);
        }
    }


    // for displaying exam current answered question/total question
    void updateExamStatusOnUi(int count, int totalCount){
        _questionStatusTxt.setText(String.valueOf(count) + "/" + String.valueOf(totalCount));
    }

}
