package com.amber.applivelib.live.service.abs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amber.applivelib.live.util.NotificationUtils;

/**
 * Created by zhanghan on 2017/9/23.\
 * <p>
 * 只有在API > 18 &  < 25 的情况下启动，然后移除通知栏
 */
public abstract class AbsInnerService extends Service {
    static final String KEY_ID = "key_id";
    private int mId = -1;

    public AbsInnerService() {
    }

    /**
     * 这个只是消除掉>18情况下显示的通知栏
     */
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * 取消上层显示的通知栏
         */
        if (intent != null && intent.hasExtra(KEY_ID) && intent.getIntExtra(KEY_ID, -1) != -1) {
            mId = intent.getIntExtra(KEY_ID, -1);
            this.addFront();
        }
        /**
         * 结束自己的任务
         */
        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        this.stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 添加到前台进程
     */
    private void addFront() {
        if (Build.VERSION.SDK_INT < 18) {
            this.startForeground(mId, NotificationUtils.emptyNotification());
        } else if (Build.VERSION.SDK_INT >= 18 && Build.VERSION.SDK_INT < 25) {
            this.startForeground(mId, NotificationUtils.smallNotification(this));
        }
    }
}
