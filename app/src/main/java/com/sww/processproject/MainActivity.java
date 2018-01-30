package com.sww.processproject;

//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import com.sww.processproject.account.SyncService;
//import com.sww.processproject.alarmliving.LiveAlarmService;
//import com.sww.processproject.doubleprocess.FirstService;
//import com.sww.processproject.doubleprocess.MyJobDaemonService;
//import com.sww.processproject.doubleprocess.SecondService;
//import com.sww.processproject.onepix.OnePixLiveService;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amber.applivelib.live.AppLiveManager;
import com.sww.processlib.account.SyncService;
import com.sww.processlib.alarmliving.LiveAlarmService;
import com.sww.processlib.doubleprocess.FirstService;
import com.sww.processlib.doubleprocess.MyJobDaemonService;
import com.sww.processlib.doubleprocess.SecondService;
import com.sww.processlib.onepix.OnePixLiveService;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    private static final String TAG = "sww";
    //双进程+onePix;
    public static boolean multi_way_to_living=false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;

        AppLiveManager.init(MainActivity.this);

//        startServices();
//        startAlarmLiving();
//        startOnePixService();
//        startAsynAccount();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startServices(){
        //双进程防杀死。
        FirstService.toStartService(mContext);
        SecondService.toStartService(mContext);
        startService(new Intent(mContext,MyJobDaemonService.class));
    }

    private  void startOnePixService(){
        Log.e(TAG, "startOnePixService: run" );
        OnePixLiveService.toLiveService(mContext);
    }

    private void startAsynAccount(){
        //利用帐号同步机制  拉活
        SyncService.startAccountSync(getApplicationContext());
    }

    private void startAlarmLiving(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.e(TAG, "startAlarmLiving: 5.0以下" );
            LiveAlarmService.startLiveAlarmService(mContext);
        }else {
            Log.e(TAG, "startAlarmLiving: 5.0以上" );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: mainactivity" );
    }



}
