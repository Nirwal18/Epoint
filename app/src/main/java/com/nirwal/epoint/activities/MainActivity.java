package com.nirwal.epoint.activities;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.database.DatabaseHelper;
import com.nirwal.epoint.fragments.FavouritesFragment;
import com.nirwal.epoint.fragments.HomeFragment;
import com.nirwal.epoint.fragments.QuizListFragment;
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
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.drawerLayout);

        _navigationView.setNavigationItemSelectedListener(onNavigationClickListener);
        fm = getFragmentManager();

        initDrawer();
        initSwiper();
        initHomeFragment();
        initAppLinkFunction();

        logFirebaseEvent();

    }


    /***
     * to initialize webapp url linking functionality
     */
    private void initAppLinkFunction(){

        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if(appLinkData==null) return;
        switch (appLinkData.toString()){
            case "https://epoint.gq": break;
            case "https://epoint.gq/home":break;
            case "https://epoint.gq/home/main-list":break;
            default:{
                if(appLinkData.getLastPathSegment()!=null){
                    startQuizListFragment(appLinkData.getLastPathSegment());
                }}
        }


    }



    public void startQuizListFragment(String data){
        Bundle bundle= new Bundle();
        bundle.putString("TITLE","Epoint app");
        bundle.putString("ID",data);
        _quizListFragment = new QuizListFragment();
        _quizListFragment.setArguments(bundle);
        ft= fm.beginTransaction();
        ft.addToBackStack("QuizList"+quizListFragCount);
        ft.setCustomAnimations(R.animator.enter_from_right,R.animator.exit_to_right,
                R.animator.enter_from_left,R.animator.exit_to_left);
        quizListFragCount++;
        ft.replace(R.id.vPager,_quizListFragment,"QuizList").commit();
    }


    void initSwiper(){
        _swiper = findViewById(R.id.home_page_swipe_refresh);
        _swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Fragment fragment = fm.findFragmentByTag("Home");
                if (fragment != null && fragment.isVisible()) {
                    HomeFragment hm = (HomeFragment) fragment;

                    if (hm._dataDownloadTask.getStatus() != DataDownloadAndSaveTask.Status.Running) {
                        hm._dataDownloadTask.start();
                    }

                } else {
                    _swiper.setRefreshing(false);
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
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



    //Option menu click listner
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.main_act_menu_notification:{
                Intent i = new Intent(_context.get(),NotificationActivity.class);
                _context.get().startActivity(i);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    void initDrawer(){

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){ getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(_context.get(),
                drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }


    void initHomeFragment(){
        ft= fm.beginTransaction();
        if(_homeFragment==null){
            _homeFragment = new HomeFragment();
        }
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // clear all backstack
        ft.replace(R.id.vPager,_homeFragment,"Home").commit();
    }


    public void startQuizListFragment(ParentChildListItem quiz){
        Bundle bundle= new Bundle();
        bundle.putString("TITLE",quiz.Title);
        bundle.putString("ID",quiz.ChildId);
        _quizListFragment = new QuizListFragment();
        _quizListFragment.setArguments(bundle);
        ft= fm.beginTransaction();
        ft.addToBackStack("QuizList"+quizListFragCount);
        ft.setCustomAnimations(R.animator.enter_from_right,R.animator.exit_to_right,
                R.animator.enter_from_left,R.animator.exit_to_left);
        quizListFragCount++;
        ft.replace(R.id.vPager,_quizListFragment,"QuizList").commit();
    }


    private void displayFavourites(){
        if(_favouritesFragment==null){
            _favouritesFragment= new FavouritesFragment();
        }
        ft=fm.beginTransaction();
        ft.addToBackStack("Favourites_fragment");
        ft.setCustomAnimations(R.animator.enter_from_right,R.animator.exit_to_right,
                R.animator.enter_from_left,R.animator.exit_to_left);
        ft.replace(R.id.vPager,_favouritesFragment,"Favourites_fragment").commit();
    }


    public void setFavourites(ParentChildListItem card){
        app.getSqlDb().insertParaentListItemIntoDb(DatabaseHelper.TableType.Favourites,card);
        Toast.makeText(_context.get(),"Added to favourites",Toast.LENGTH_LONG).show();
    }

    private  void logFirebaseEvent(){
        Bundle bundle = new Bundle();
        bundle.putInt("Android_ver", Build.VERSION.SDK_INT);
        app.getFirebaseAnalytics().logEvent("App_launch", bundle);
    }

}
