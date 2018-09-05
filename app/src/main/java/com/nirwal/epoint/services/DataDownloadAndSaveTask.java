package com.nirwal.epoint.services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.models.ParentChildListItem;

import java.util.ArrayList;

public class DataDownloadAndSaveTask {
    private static final String TAG= DataDownloadAndSaveTask.class.getName();
    private  IOnTaskMethods iOnTaskMethods;
    private MyApp app;
    private DatabaseReference _mainCardRef, _quizListRef;
    private ArrayList<ParentChildListItem> _mainCards, _subCards;
    private static final String main_list_table = "OnlineQuizMainList";
    private static final String sub_list_table = "OnlineQuizSubList";

    private int mainCardLoopSts =0;
    private int subCardLoopSts=0;

    public DataDownloadAndSaveTask(MyApp app){
        this.app= app;
    }


    public void addEventListener(IOnTaskMethods iOnTaskMethods){
        this.iOnTaskMethods=iOnTaskMethods;
    }



    public void start() {

        onPreExecute();

        _mainCardRef = app.getFirebaseDatabase().getReference(main_list_table);
        _quizListRef = app.getFirebaseDatabase().getReference(sub_list_table);

        _mainCardRef.addListenerForSingleValueEvent(mainCardsValueEventListener);
        _quizListRef.addListenerForSingleValueEvent(subCardsValueEventListener);
    }


    private ValueEventListener mainCardsValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            _mainCards= new ArrayList<>();
            app.getSqlDb().deleteAllMainCard(); //clear all main card table

            mainCardLoopSts= 1;

            for (DataSnapshot items : dataSnapshot.getChildren()) {
                ParentChildListItem card = items.getValue(ParentChildListItem.class);

                if (card.isVisible) {
                    _mainCards.add(card);
                    app.getSqlDb().insertMainCard(card); //inserting data into sql table

                }
            }

            mainCardLoopSts=2;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener subCardsValueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            subCardLoopSts=1;
            _subCards=new ArrayList<>();
            app.getSqlDb().deleteAllQuizlist();
            long totalItem = dataSnapshot.getChildrenCount();
            long count=totalItem;
            for (DataSnapshot items1 : dataSnapshot.getChildren()) {

                for (DataSnapshot items2 : items1.getChildren()) {

                    ParentChildListItem list = items2.getValue(ParentChildListItem.class);
                    if (list.isVisible) {
                        _subCards.add(list);
                        app.getSqlDb().insertQuizList(list);
                    }

                }
                count--;
                onProgressUpdate(100-((count*100)/totalItem));
            }
            subCardLoopSts=2;
            onPostExecute();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };


    void onPreExecute() {
        Log.v(TAG,"OnPreExecute Called");
        if(iOnTaskMethods!=null){
            iOnTaskMethods.onPreExecute();
        }

    }

    public Status getStatus(){
        int sum = mainCardLoopSts+subCardLoopSts;
        switch (sum){
            case 0:return Status.NotInitialize;
            case 1:return Status.Running;
            case 2: return Status.Running;
            case 3: return Status.Running;
            case 4: return Status.Finished;

        }
        return Status.NotInitialize;
    }



   private void onPostExecute() {
        Log.v(TAG,"OnPostExecute Called");
       if(iOnTaskMethods!=null){
           iOnTaskMethods.onPostExecute(null);
       }

    }

    private void onProgressUpdate(Long values) {

        if(iOnTaskMethods!=null){
            iOnTaskMethods.onProgressUpdate(values);
        }
        Log.v(TAG ,"Progress: "+values.toString() );
    }


    public enum Status{
        NotInitialize,
        Running,
        Finished
    }
}

