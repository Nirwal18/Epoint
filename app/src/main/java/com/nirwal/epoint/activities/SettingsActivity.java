package com.nirwal.epoint.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.services.ConnectionDetector;
import com.nirwal.epoint.services.DataSyncService;

import java.lang.ref.WeakReference;
import java.util.Calendar;


public class SettingsActivity extends AppCompatActivity {
public static final String TAG=SettingsActivity.class.getName();
private SharedPreferences _shredPref;
private Switch aSwitch;
private JobScheduler jobScheduler;
private AlarmManager alarmManager;
public static final String service_status_tag="service_status";
public static final int Job_Id = 369;
public static WeakReference<SettingsActivity> _context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);
        _context = new WeakReference<>(this);
        _shredPref = PreferenceManager.getDefaultSharedPreferences(_context.get());
        aSwitch = findViewById(R.id.service_switch);
        init();
    }


    public void init(){
        final boolean isRunning = _shredPref.getBoolean(service_status_tag,false);

        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP){
            jobScheduler = (JobScheduler) _context.get().getSystemService(JOB_SCHEDULER_SERVICE);
        }else {
            alarmManager = (AlarmManager) _context.get().getSystemService(ALARM_SERVICE);
        }

        aSwitch.setChecked(isRunning);
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!isRunning) {
                scheduleService();
            }else {
                cancelJob();
            }

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        _context.clear();
        _shredPref=null;
        jobScheduler=null;
        alarmManager=null;
        aSwitch=null;
    }

    public void scheduleService() {
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP){
            ComponentName serviceName = new ComponentName(_context.get(), DataSyncService.class);
            JobInfo jobInfo = new JobInfo.Builder(Job_Id,serviceName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresCharging(true)
                    .setPeriodic(AlarmManager.INTERVAL_DAY)
                    .setPersisted(true)
                    .build();

            if(jobScheduler != null){
                int result = jobScheduler.schedule(jobInfo);
                if(result==JobScheduler.RESULT_SUCCESS){
                    Log.v(TAG,"Service started successfully");
                    _shredPref.edit().putBoolean(service_status_tag,true).apply();
                }
            }

        }else{
            Intent intent = new Intent(_context.get(), DataSyncService.class);
            PendingIntent pendingIntent = PendingIntent.getService(_context.get(), Job_Id, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC, AlarmManager.INTERVAL_DAY, currentTime, pendingIntent);

        }


    }


    public void cancelJob(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            jobScheduler.cancel(Job_Id);
        }
        else{
            alarmManager.cancel(PendingIntent.getService(_context.get(),Job_Id,
                    new Intent(_context.get(),DataSyncService.class),PendingIntent.FLAG_CANCEL_CURRENT));
        }
    _shredPref.edit().putBoolean(service_status_tag,false).apply();
}


    public static class StartScreenActivity extends AppCompatActivity {

        private static final int  Screen_Delay =200; //in milli seconds
        private static ProgressDialog progressDialog;
        private static Handler handler;
        private ConnectionDetector connectionDetector;
        private static SharedPreferences.Editor sharedPreferenceEditor;
        public AlertDialog _updateDialog;

        public WeakReference<StartScreenActivity> _context;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start_screen);
            _context = new WeakReference<>(this);
            connectionDetector= new ConnectionDetector(_context.get());

            handler =new Handler();
            setupConnectionDialog();
            checkUpdate();
            if(!showUpdateAlert()){
                init();
            }



        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if(_updateDialog!=null){
                _updateDialog.dismiss();
                _updateDialog=null;
            }

            if(progressDialog!=null){
                progressDialog.dismiss();
                progressDialog=null;
            }

            sharedPreferenceEditor=null;
            handler=null;
            _context.clear();
        }

        void setupConnectionDialog(){
            progressDialog = new ProgressDialog(_context.get());
            progressDialog.setTitle("Connection error.");
            progressDialog.setMessage("Please check your internet connection.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setButton(Dialog.BUTTON1,"Offline",onClickListener);
            progressDialog.setButton(Dialog.BUTTON2,"Exit",onClickListener);
        }

        void init(){
            if(connectionDetector.isConnected()){
                progressDialog.hide();
                runHandler();
            }else{
                progressDialog.show();
            }


        }

       void runHandler(){
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
            startAnotherActivity();
               }
           }, Screen_Delay);

       }
       void startAnotherActivity(){
           Intent intent= new Intent(_context.get(),MainActivity.class);
           startActivity(intent);
       }

        Dialog.OnClickListener onClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case Dialog.BUTTON1:{
                        progressDialog.dismiss();
                        runHandler();
                        break;
                    }
                    case Dialog.BUTTON2:{
                        exit();
                        break;
                    }
                }
            }
        };



        void checkUpdate(){
            MyApp app = (MyApp) getApplication();
            app.getFirebaseDatabase().getReference().child("AppConfig/Android").child("LATEST_VERSION").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    float LATEST_VERSION = Float.valueOf(dataSnapshot.getValue().toString());
                    sharedPreferenceEditor = getSharedPreferences(MyApp.App_Name,MODE_PRIVATE).edit();
                    sharedPreferenceEditor.putFloat("LATEST_VERSION",LATEST_VERSION).apply();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        boolean showUpdateAlert(){
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(MyApp.App_Name,MODE_PRIVATE);
                float LATEST_VERSION = sharedPreferences.getFloat("LATEST_VERSION",0);
                float CURRENT_VERSION = Float.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                Log.v("App", "Current :" +CURRENT_VERSION + " Latest ver:"+LATEST_VERSION);

                if(LATEST_VERSION>CURRENT_VERSION){
                    createAlertDialog();
                    _updateDialog.show();
                    return true;
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return false;
        }

        void createAlertDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(_context.get());
            builder.setIcon(R.drawable.ic_appicon);
            builder.setTitle("New update available.");
            builder.setMessage("Please update your app to get new features.");
            builder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launching Play store
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.nirwal.epoint"));
                     startActivity(intent);
                }
            });

            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  // continue to Main Activity
                    init();
                }
            });

            _updateDialog = builder.create();

        }

        private void exit(){

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN ){
                this.finish();
                System.exit(0);
            }else {
                this.finishAffinity();
            }


        }

    }
}



