package com.amber.applivelib.live.assist.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.amber.applivelib.live.util.LiveLog;

/**
 * Created by zhanghan on 2017/9/15.
 * <p>
 * 先不要使用  可能出现问题
 * 1、广播场景多样化的问题
 */
@Deprecated
public class OnePiexlActivity extends Activity {

    private BroadcastReceiver endReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LiveLog.log("OnePiexlActivity", "onCreate()");
        //设置1像素
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        //结束该页面的广播
        endReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LiveLog.log("OnePiexlActivity", "onReceive()", "ACTION_SCREEN_ON");
                finish();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(endReceiver, new IntentFilter(getBaseContext().getPackageName() + ".finish"));
        //检查屏幕状态
        checkScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkScreen();
    }

    @Override
    public void finish() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(endReceiver);
        } catch (Exception e) {
        }
        super.finish();
        try {
            // 部分机型 部分情况会出现问题
            /*
                java.lang.RuntimeException:Error receiving broadcast
            	Intent { act=mobi.infolife.ezweather.locker.fingerprint.wheelgear.finish flg=0x10 }
            	 in com.amber.applivelib.live.assist.ui.OnePiexlActivity$1@d389146
             */
            moveTaskToBack(true);
        } catch (Exception e) {
        }
        LiveLog.log("OnePiexlActivity", "finish()");
    }

    /**
     * 检查屏幕状态  isScreenOn为true  屏幕“亮”结束该Activity
     */
    private void checkScreen() {
        PowerManager pm = (PowerManager) OnePiexlActivity.this.getSystemService(Context.POWER_SERVICE);
        if (pm == null || pm.isScreenOn()) {
            finish();
        }
    }
}