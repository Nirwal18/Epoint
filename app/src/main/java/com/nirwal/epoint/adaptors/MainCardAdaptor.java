package com.nirwal.epoint.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nirwal.epoint.activities.ExamActivity;
import com.nirwal.epoint.activities.MainActivity;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.customViews.CustomCrd;
import com.nirwal.epoint.models.ParentChildListItem;

import java.util.ArrayList;

public class MainCardAdaptor extends BaseAdapter {


    ArrayList<ParentChildListItem> _mainCardList;
    ParentChildListItem _mainCard;
    ArrayList<ParentChildListItem> _quizLists = new ArrayList<>();
    MyApp _app;
    Context _context;

    public MainCardAdaptor(ArrayList<ParentChildListItem> mainCardList, MyApp app, Context context){
        this._mainCardList=mainCardList;
        this._app=app;
        this._context=context;
    }


    @Override
    public int getCount() {
        if(_mainCardList==null){
            return 0;
        }
        return _mainCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return _mainCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        _mainCard =_mainCardList.get(position);
        _quizLists = _app.getSqlDb().readAllQuizListfromParentID(_mainCard.ChildId);

        if(view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_2,parent,false);
        }

        CustomCrd crd = view.findViewById(R.id.main_custom_card);
        crd.setTitle(_mainCard.Title);
        crd.setList(_app.getSqlDb().readAllQuizListfromParentID(_mainCardList.get(position).ChildId));
        crd.setOnClickSectionItemListener(new CustomCrd.OnClickSectionItemListener() {
            @Override
            public void onClick(ParentChildListItem item) {
                if(item.ChildType.equals("Question1")){
                    Intent i = new Intent(_context, ExamActivity.class);
                    //i.putExtra("TITLE",item.Title);
                    i.putExtra("ID",item.ChildId);
                    _context.startActivity(i);
                }else if(item.ChildType.equals("ParentChildListItem")){
                    MainActivity activity = (MainActivity) _context;
                    activity.startQuizListFragment(item);
                }

            }
        });


        return view;
    }






    private int periviousNum = 0;
    int getColor(){
       double Num = Math.random() *10;
       int currentNum = (int)Num;

       while (currentNum==periviousNum){
           Num = Math.random() *10;
           currentNum = (int)Num;
       }
       periviousNum =currentNum;


       switch (currentNum){
           case 1:{ return Color.rgb(225,0,127);}
           case 2:{ return Color.rgb(153,51,225);}
           case 3:{ return Color.rgb(255,51,51);}
           case 4:{ return Color.rgb(0,204,204);}
           case 5:{ return Color.rgb(128,128,128);}
           case 6:{ return Color.rgb(204,0,204);}
           case 7:{ return Color.rgb(102,204,0);}
           case 8:{ return Color.rgb(255,128,0);}
           case 9:{ return Color.rgb(0,102,102);}
           default:{ return Color.rgb(51,153,255);}

       }

    }



}

