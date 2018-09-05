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
import android.widget.LinearLayout;

import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.adaptors.QuizListAdaptor;
import com.nirwal.epoint.database.DatabaseHelper;
import com.nirwal.epoint.models.ParentChildListItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    private MyApp app;
    private RecyclerView _favList;
    private WeakReference<Activity> _context;

    public FavouritesFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favourites_fragment,container,false);
        _context= new WeakReference<>(this.getActivity());
        app = (MyApp) _context.get().getApplication();
        _favList = v.findViewById(R.id.favourites_list_view);

        initList();
        _context.get().setTitle("Favourites");
        return v;
    }

    void initList(){
        _favList.setLayoutManager(new LinearLayoutManager(_context.get()));
        ArrayList<ParentChildListItem> list = app.getSqlDb().readAllDataFromTable(DatabaseHelper.TableType.Favourites);
        _favList.setAdapter(new QuizListAdaptor(list,_context.get()));
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        _favList=null;
    }
}
