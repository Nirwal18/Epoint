package com.nirwal.epoint;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.nirwal.epoint.database.DatabaseHelper;
import com.nirwal.epoint.fragments.FavouritesFragment;
import com.nirwal.epoint.fragments.HomeFragment;
import com.nirwal.epoint.fragments.QuizListFragment;
import com.nirwal.epoint.models.MainCard;
import com.nirwal.epoint.models.ParentChildListItem;
import com.nirwal.epoint.services.DataDownloadAndSaveTask;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private MyApp app;
    private Toolbar toolbar;
    private NavigationView _navigationView;
    private static FragmentManager fm;
    private static FragmentTransaction ft;

    private HomeFragment _homeFragment;
    private QuizListFragment _quizListFragment;
    private FavouritesFragment _favouritesFragment;

    public SwipeRefreshLayout _swiper;
    private DrawerLayout drawerLayout;
    private WeakReference<MainActivity> _context ;
    int quizListFragCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        _context = new WeakReference<>(this);
        app = (MyApp) _context.get().getApplication();
        _navigationView = findViewById(R.id.navigation_view);
        _swiper = findViewById(R.id.home_page_swipe_refresh);
        _navigationView.setNavigationItemSelectedListener(onNavigationClickListener);
        fm=getFragmentManager();

        initDrawer();
        initHomeFragment();



        _swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Fragment fragment = fm.findFragmentByTag("Home");
                if(fragment!=null && fragment.isVisible())  {
                    HomeFragment hm = (HomeFragment)fragment;

                    if(hm._dataDownloadTask.getStatus()!= DataDownloadAndSaveTask.Status.Running){
                        hm._dataDownloadTask.start();
                    }

                    }else{
                    _swiper.setRefreshing(false);
                }

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        _swiper=null;
        _navigationView=null;
        _quizListFragment=null;
        _homeFragment=null;
        toolbar=null;
        fm = null;
        ft = null;
        _context.clear();
    }

    NavigationView.OnNavigationItemSelectedListener onNavigationClickListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home:{
                    initHomeFragment();
                    break;
                }
                case R.id.catagory:{

                    Intent i = new Intent(_context.get(),LearningActivity.class);
                    _context.get().startActivity(i);
                    break;
                }

                case R.id.settings:{
                    Intent i = new Intent(_context.get(),SettingsActivity.class);
                    _context.get().startActivity(i);
                    break;
                }

                case R.id.favourites:{
                    displayFavourites();
                    break;
                }
            }
            drawerLayout.closeDrawers();
            return false;
        }
    };



    void initDrawer(){
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(_context.get(), drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }


    void initHomeFragment(){
        ft= fm.beginTransaction();
        if(_homeFragment==null){
            _homeFragment = new HomeFragment();
        }
        ft.replace(R.id.vPager,_homeFragment,"Home").commit();
    }



    @SuppressLint("ResourceType")
    public void startQuizListFragment(ParentChildListItem quiz){
        Bundle bundle= new Bundle();
        bundle.putString("TITLE",quiz.Title);
        bundle.putString("ID",quiz.ChildId);
        _quizListFragment = new QuizListFragment();
        _quizListFragment.setArguments(bundle);
        ft= fm.beginTransaction();
        ft.addToBackStack("QuizList"+quizListFragCount);
        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.entrer_from_left,R.anim.exit_to_left);
        quizListFragCount++;
        ft.replace(R.id.vPager,_quizListFragment,"QuizList").commit();
    }


    @SuppressLint("ResourceType")
    private void displayFavourites(){
        if(_favouritesFragment==null){
            _favouritesFragment= new FavouritesFragment();
        }
        ft=fm.beginTransaction();
        ft.addToBackStack("Favourites_fragment");
        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.entrer_from_left,R.anim.exit_to_left);
        ft.replace(R.id.vPager,_favouritesFragment,"Favourites_fragment").commit();
    }

    public void setFavourites(ParentChildListItem card){
        app.getSqlDb().insertParaentListItemIntoDb(DatabaseHelper.TableType.Favourites,card);
        Toast.makeText(_context.get(),"Added to favourites",Toast.LENGTH_LONG).show();
    }


}
