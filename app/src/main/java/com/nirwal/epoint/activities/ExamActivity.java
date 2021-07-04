package com.nirwal.epoint.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.activities.ResultActivity;
import com.nirwal.epoint.adaptors.SwipeAdaptor;
import com.nirwal.epoint.fragments.PaperFragment;
import com.nirwal.epoint.fragments.ResultFragment;
import com.nirwal.epoint.models.Question;

import java.util.ArrayList;
import java.util.Locale;

public class ExamActivity extends AppCompatActivity {

    private MyApp _app;
    private ArrayList<Question> _questionList;
    private static FragmentManager fm;
    private static SwipeAdaptor _swipeAdaptor;

    private int _questionCount=0;

    //private ResultFragment resultFragment;
    private TextView _totalQuestionCountView, _answeredQuestionCountView,_skippedQuestionCountView;
    private Button forwardBtn, backwordBtn,finishBtn, _showAnswerBtn;
    private ViewPager viewPager;


    private static ProgressDialog progressDialog;
    private static String _id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_layout);
        viewPager = findViewById(R.id.exam_swiper_viewPager);


        _totalQuestionCountView = findViewById(R.id.exam_total_question_count);
        _answeredQuestionCountView = findViewById(R.id.exam_answered_question_count);
       // _skippedQuestionCountView = findViewById(R.id.exam_skipped_question_count);
        _showAnswerBtn = findViewById(R.id.exam_btn_showAnswer);


        forwardBtn = findViewById(R.id.forward_btn);
        backwordBtn = findViewById(R.id.backword_btn);
        finishBtn = findViewById(R.id.finish_exam_btn);


        // progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Loading from internet please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        // recover data from saved state
        if(savedInstanceState!=null){
            _questionList= (ArrayList<Question>) savedInstanceState.getSerializable("questionList");
        }

        init();

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

        _questionList =null;
        _app =null;
        _swipeAdaptor =null;
        fm=null;


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("questionList",_questionList);

    }




    void init(){
        _app = (MyApp) getApplication();

        fm = getSupportFragmentManager();
       // resultFragment = new ResultFragment();

        Bundle b = getIntent().getExtras();
        _id = b.getString("ID");

        loadSwiperFunction();
        forwardBtn.setOnClickListener(navigationClickListener);
        backwordBtn.setOnClickListener(navigationClickListener);
        finishBtn.setOnClickListener(navigationClickListener);
        _showAnswerBtn.setOnClickListener(navigationClickListener);
    }


    // bottom toolbar button click listener
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

                    finishBtn.setEnabled(false);
                    finishTest();
                    break;
                }
                case R.id.exam_btn_showAnswer:{
                    displayAnswer();
                    break;
                }
            }

        }
    };


    void finishTest(){
        //user interface setup
        //displayExamUI(false);

        Intent resultIntent = new Intent(this,ResultActivity.class);
        Log.d("ExamActivity :" ,  String.valueOf(_questionList.size()));
        resultIntent.putExtra("ResultArray",_questionList);
        startActivity(resultIntent);


    }


    void loadSwiperFunction(){
        // Read question list from firebase
        if(_questionList==null){
            _app.getFirebaseDatabase().getReference("OnlineQuestionList")
                    .child(_id).addListenerForSingleValueEvent(valueEventListener);
        }else {
            setupUi();
        }

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
            _questionList= new ArrayList<>();
            for (DataSnapshot item : dataSnapshot.getChildren()) {
                _questionList.add(item.getValue(Question.class));
            }



            setupUi();

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

            if(position!=0 && backwordBtn.getVisibility()==View.INVISIBLE){
                backwordBtn.setVisibility(View.VISIBLE);
            }else if(position==0)
            {
                backwordBtn.setVisibility(View.INVISIBLE);
            }

        }
    }

    //update answered question status on every question answered
    public void postCompletedStatus(int qNo, String  choose) {
        if(_questionList==null || _questionList.size()==0)return;

        _questionList.get(qNo).questionNo = qNo + 1;
        _questionList.get(qNo).chosenOption=choose;
        _questionList.get(qNo).isCorrect = _questionList.get(qNo).Answer.equals(choose);

        if (!_questionList.get(qNo).isAnswered) {
            _questionList.get(qNo).isAnswered = true;
            _questionCount++;
            _answeredQuestionCountView.setText(String.valueOf(_questionCount));
        }
    }


    void setupUi(){
        _totalQuestionCountView.setText(String.valueOf(_questionList.size()));
        _swipeAdaptor = new SwipeAdaptor(fm,_questionList==null?new ArrayList<Question>():_questionList);
        viewPager.setAdapter(_swipeAdaptor);

        _answeredQuestionCountView.setText(String.valueOf(0));
        progressDialog.dismiss();

    }

    void displayAnswer(){
        if(_swipeAdaptor != null && viewPager != null){
             PaperFragment fragment= _swipeAdaptor.getFragment(viewPager.getCurrentItem());
             if(fragment!=null){
                 fragment.showAnswer();
             }

        }

    }


}
