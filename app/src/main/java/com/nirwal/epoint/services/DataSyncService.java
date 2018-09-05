package com.nirwal.epoint.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.nirwal.epoint.MyApp;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DataSyncService extends JobService {
    public static final String TAG=DataSyncService.class.getName();
    private DataDownloadAndSaveTask task;

    @Override
    public void onCreate() {
        super.onCreate();
        task = new DataDownloadAndSaveTask((MyApp) this.getApplication());
    }



    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.v(TAG,"job started using AlarmManger.class");
        Toast.makeText(this,"Service Started 1",Toast.LENGTH_LONG).show();
        onStartJob(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task=null;
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.v(TAG,"job started using JobScheduler.class");
        Toast.makeText(this,"Service Started 2",Toast.LENGTH_LONG).show();
        task.start();
        task.addEventListener(new IOnTaskMethods() {
            @Override
            public void onPostExecute(Object o) {
                jobFinished(params,true);
            }

            @Override
            public void onProgressUpdate(Object o) {

            }

            @Override
            public void onPreExecute() {
            }

        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
