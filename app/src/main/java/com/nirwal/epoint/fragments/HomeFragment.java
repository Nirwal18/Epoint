package com.nirwal.epoint.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.nirwal.epoint.MainActivity;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.adaptors.MainCardAdaptor;
import com.nirwal.epoint.database.DatabaseHelper;
import com.nirwal.epoint.models.ParentChildListItem;
import com.nirwal.epoint.services.DataDownloadAndSaveTask;
import com.nirwal.epoint.services.IOnTaskMethods;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String main_list_table="OnlineQuizMainList";
    private static final String sub_list_table="OnlineQuizSubList";

    private MyApp app;
    private WeakReference<MainActivity> _context;
    private MainCardAdaptor adaptor;
    private ArrayList<ParentChildListItem> _items = new ArrayList<>();
    private ListView listView;
    public DataDownloadAndSaveTask _dataDownloadTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.home_fragment, container, false);
         listView = v.findViewById(R.id.main_card_listView);
         _context = new WeakReference<>((MainActivity)this.getActivity());
         init();
         loadListView();
         return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        _context.get().setTitle(R.string.app_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _dataDownloadTask=null;
        listView=null;
        _items.clear();

        //_mainActivity =null;
    }

    void init() {
        app = (MyApp) getActivity().getApplication();
        _dataDownloadTask = new DataDownloadAndSaveTask(app);
        _dataDownloadTask.addEventListener(new IOnTaskMethods() {
            @Override
            public void onPostExecute(Object o) {
                _context.get()._swiper.setRefreshing(false);
                loadListView();
            }

            @Override
            public void onProgressUpdate(Object o) {

            }

            @Override
            public void onPreExecute() {
                _context.get()._swiper.setRefreshing(true);
            }
        });
    }

    public void loadListView(){
        _items = app.getSqlDb().readAllMainCard(); // reading Data from SQL on init

        // if data not available then load from internet firebase
        if(_items==null || _items.size()==0){
           _dataDownloadTask.start();
        }

        adaptor = new MainCardAdaptor(_items,app,_context.get());
        listView.setAdapter(adaptor);
        listView.deferNotifyDataSetChanged();
    }





}
