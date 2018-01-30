package com.amber.applivelib.live.core;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.amber.applivelib.live.AppLiveManager;
import com.amber.applivelib.live.assist.LiveAlarmService;
import com.amber.applivelib.live.assist.LiveJobService;
import com.amber.applivelib.live.assist.ui.OnePixelReceiver;
import com.amber.applivelib.live.service.abs.AbsFrontService;
import com.amber.applivelib.live.util.LiveLog;
import com.amber.applivelib.live.util.ServiceRunning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhanghan on 2017/9/5.
 * <p>
 * 保护服务
 * 核心实现类
 */

public abstract class AbsLiveService extends AbsFrontService implements HandlerUtils.OnReceiveMessageListener {

    private static final int MSG_CHECK_MAIN_SERVICE = 1;

    private static HandlerUtils.HandlerReceiver mHandlerReceiver;
    private static NotifyRunable mNotifyRunable;
    private List<String> mWorks;

    /**
     * 一像素方案
     */
    private BroadcastReceiver mOnepxReceiver;


    public void onCreate() {
        super.onCreate();
        /**
         * 启动主线程检测本服务
         */
        if (mHandlerReceiver == null) {
            mHandlerReceiver = new HandlerUtils.HandlerReceiver(this);
        } else {
            mHandlerReceiver.setOnReceiveMessageListener(this);
        }
        if (mNotifyRunable == null) {
            mNotifyRunable = new NotifyRunable();
        }
        try {
            mHandlerReceiver.removeCallbacks(mNotifyRunable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNotifyRunable.run();
        mWorks = new ArrayList<>();
        /**
         * 启动两个辅助保活重启线程
         */
        startJobService();
        startAlarmService();
        startUseUI();
    }

    /**
     * 发开Jobservice
     */
    private void startJobService() {
        LiveLog.log("LiveServiceL startJobService");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LiveJobService.startLiveJobService(getBaseContext());
        }
    }

    /**
     * 发开Jobservice
     */
    private void startAlarmService() {
        LiveLog.log("LiveServiceL startAlarmService");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LiveAlarmService.startLiveAlarmService(getBaseContext());
        }
    }

    /**
     * 使用一像素保活
     */
    private void startUseUI() {
        /**
         * 目前监听锁屏要在其他地方进行监听
         */
        if (true)
            return;
        /**
         * 注册监听屏幕的广播
         * 锁屏本身有锁屏方案，就不需要了
         */
        if (AppLiveManager.isUseUi(this)) {
            mOnepxReceiver = new OnePixelReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            registerReceiver(mOnepxReceiver, intentFilter);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LiveLog.log("LiveServiceL onStartCommand()");
        addService(intent);
        checkService();
        return START_STICKY;
    }

    /**
     * 先添加服务
     *
     * @param intent
     */
    private void addService(Intent intent) {
        String[] services = AppLiveManager.getServices(getBaseContext(), intent);
        if (services != null && services.length > 0) {
            for (String service : services) {
                if (!mWorks.contains(service))
                    mWorks.add(service);
            }
        }
    }

    /**
     * 检查所有的服务是否正常
     */
    private void checkService() {
        for (String service : mWorks) {
            if (service == null)
                continue;
            String packageName = getPackageName();
            if (!ServiceRunning.isServiceRunning(getBaseContext(), service.getClass())) {
                LiveLog.log("LiveServiceL checkService " + service);
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, service));
                    getBaseContext().startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveLog.log("LiveServiceL onDestroy");
        if (mHandlerReceiver != null && mNotifyRunable != null) {
            mHandlerReceiver.removeCallbacks(mNotifyRunable);
            mHandlerReceiver.setOnReceiveMessageListener(null);
        }
        /**
         * 取消掉注册
         */
        if (mOnepxReceiver != null) {
            try {
                unregisterReceiver(mOnepxReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 重新启动
         */
        startService(new Intent(this, AppLiveManager.getLiveService()));
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void handlerMessage(Message message) {
        if (message == null)
            return;
        /**
         * 检查核心服务
         */
        if (message.what == MSG_CHECK_MAIN_SERVICE) {
            LiveLog.log("LiveServiceL handlerMessage checkService()");
            checkService();
        }
    }


    /**
     * 循环通知事件
     */
    private static class NotifyRunable implements Runnable {

        /**
         * 每十分钟检测一次
         */
        // private static final long POST_DELAYED = 10 * 60 * 1000L;
        // private static final long POST_MORE_DELAYED = 30 * 60 * 1000L;
        private static final long POST_DELAYED = 2 * 1000L;
        private static final long POST_MORE_DELAYED = 2 * 1000L;

        public NotifyRunable() {
        }

        @Override
        public void run() {
            LiveLog.log("LiveService run");
            long time = POST_DELAYED;
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 7 || Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 11) {
                time = POST_MORE_DELAYED;
            }
            try {
                mHandlerReceiver.removeCallbacks(mNotifyRunable);
                mHandlerReceiver.postDelayed(mNotifyRunable, time);
                mHandlerReceiver.sendEmptyMessage(MSG_CHECK_MAIN_SERVICE);
            } catch (Exception e) {
            }
        }

    }


}
