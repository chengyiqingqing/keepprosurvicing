package com.sww.processlib.onepix;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/11.
 */

public class OnePixLiveService extends Service{

    private static final String TAG = "sww";
    public static final String CLASS_NAME = OnePixLiveService.class.getSimpleName();
    public static Context context;

    public static void toLiveService(Context pContext) {
        Intent intent = new Intent(pContext, OnePixLiveService.class);
        pContext.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "OnePixLiveService --- onStartCommand: 一个像素的服务走起来。" );
        //screenManager类就是一个Java类用来持有LiveActivity类对象，并控制开启和销毁。（传入这个对象，会走activity的生命周期吗）
        final OnePixActivityManager onePixActivityManager = OnePixActivityManager.getInstance(OnePixLiveService.this);

        //屏幕广播的监听。 //自己调用自己，在哪里调用，就在哪里去实现。
        ScreenBroadcastController listener = ScreenBroadcastController.getInstance(this);
        listener.registerListener(new ScreenBroadcastController.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.e(TAG, CLASS_NAME +" -- onScreenOn");
                onePixActivityManager.finishActivity();
            }

            @Override
            public void onScreenOff() {
                Log.e(TAG, CLASS_NAME +" -- onScreenOff");
                onePixActivityManager.startActivity();
            }
        });
        Log.e(TAG, "onStartCommand:  ---  这 " );
        //首次是这样，但是并不是这样的东西。
        PowerManager pm = (PowerManager) getApplication().getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()){
            Log.e(TAG, "onStartCommand:  ---  1 " );
            onePixActivityManager.finishActivity();
        }else{
            Log.e(TAG, "onStartCommand:  ---  2 " );
            onePixActivityManager.startActivity();
        }
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
