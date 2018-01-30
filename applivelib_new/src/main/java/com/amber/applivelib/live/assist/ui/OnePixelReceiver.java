package com.amber.applivelib.live.assist.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.amber.applivelib.live.AppLiveManager;
import com.amber.applivelib.live.util.LiveLog;

/**
 * Created by zhanghan on 2017/9/15.
 */
public class OnePixelReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!AppLiveManager.isUseUi(context))
            return;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭启动1像素Activity
            onScreenOff(context);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {   //屏幕打开 结束1像素
            onScreenOn(context);
        }
    }

    /**
     * 屏幕开启,开启一像素方案
     *
     * @param context
     */
    public static final void onScreenOn(Context context) {
        if (!AppLiveManager.isUseUi(context))
            return;
        LiveLog.log("OnePixelReceiver", "onReceive()", "ACTION_SCREEN_ON");
        String packageName = context.getPackageName();
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(packageName + ".finish"));
        LiveLog.log("OnePixelReceiver", "onReceive()", "ACTION_SCREEN_ON", "end");
    }

    /**
     * 屏幕关闭,关闭一像素Avtivity
     *
     * @param context
     */
    public static final void onScreenOff(Context context) {
        if (!AppLiveManager.isUseUi(context))
            return;
        LiveLog.log("OnePixelReceiver", "onReceive()", "ACTION_SCREEN_OFF");
        Intent it = new Intent(context, OnePiexlActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(it);
    }
}