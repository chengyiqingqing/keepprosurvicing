package com.sww.processproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sww.processproject.account.SyncService;
import com.sww.processproject.doubleprocess.FirstService;
import com.sww.processproject.doubleprocess.MyJobDaemonService;
import com.sww.processproject.doubleprocess.SecondService;
import com.sww.processproject.onepix.OnePixLiveService;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        startServices();
//        startOnePixService();
//        startAsynAccount();
    }


    private void startServices(){
        //双进程防杀死。
        startService(new Intent(mContext,FirstService.class));
        startService(new Intent(mContext,SecondService.class));
        //JobService保活。  它执行了，但是start和stop未必会被调度。
        startService(new Intent(mContext,MyJobDaemonService.class));
    }

    private static final String TAG = "sww";
    private void startOnePixService(){
        Log.e(TAG, "startOnePixService: run" );
        OnePixLiveService.toLiveService(mContext);
    }

    private void startAsynAccount(){
        //利用帐号同步机制  拉活
        SyncService.startAccountSync(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
