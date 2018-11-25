package com.nirwal.epoint.fragments;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.adaptors.QuizListAdaptor;
import com.nirwal.epoint.models.ParentChildListItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class QuizListFragment extends Fragment{

    MyApp app;
    RecyclerView quizList;
    String _title="";
    WeakReference<Activity> _context;

    public QuizListFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.quiz_list_fragment,container,false);
        _context= new WeakReference<>(this.getActivity());
        quizList = v.findViewById(R.id.quizList);
        init();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        _context.get().setTitle(_title);
    }

    void init(){
        app = (MyApp) getActivity().getApplication();

        // reading supplied data from bundle
        Bundle b = getArguments();
        String id = b.getString("ID");
        _title = b.getString("TITLE");

         // reading data from sql
        ArrayList<ParentChildListItem> list = app.getSqlDb().readAllQuizListfromParentID(id);
        //setting up recycle adaptor
        quizList.setLayoutManager(new LinearLayoutManager(_context.get()));
        quizList.setAdapter(new QuizListAdaptor(list,_context.get(),QuizListFragment.class.getName()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
