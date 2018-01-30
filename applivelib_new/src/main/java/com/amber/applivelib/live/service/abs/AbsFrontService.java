package com.amber.applivelib.live.service.abs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.amber.applivelib.live.AppLiveManager;
import com.amber.applivelib.live.service.ServiceNotificationIdManager;
import com.amber.applivelib.live.util.LiveLog;
import com.amber.applivelib.live.util.NotificationUtils;

import java.util.LinkedHashMap;

import static com.amber.applivelib.live.service.abs.AbsInnerService.KEY_ID;

/**
 * Created by zhanghan on 2017/9/21.
 * <p>
 * 保活专用前台进程
 */

public abstract class AbsFrontService extends Service {
    private static final Object MAP_LOCK = new Object();
    private int mServiceNotificationId = 0;
    private static LinkedHashMap<String, Integer> mMaps;

    /**
     * 必须调用super.onCreate();
     */
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 首次启动服务需要初始化通知栏ID
         */
        LiveLog.log("AbsFrontService isUseFront", "" + AppLiveManager.isUseFront(this));
        if (AppLiveManager.isUseFront(this))
            addFront();
    }


    protected String getImplClassName() {
        return getClass().getSimpleName();
    }

    public abstract Class<? extends AbsInnerService> getInnerService();


    /**
     * 添加到前台进程
     */
    protected void addFront() {
        if (Build.VERSION.SDK_INT < 18) {
            setId(this);
            this.startForeground(mServiceNotificationId, NotificationUtils.emptyNotification());
        } else if (Build.VERSION.SDK_INT >= 18 && Build.VERSION.SDK_INT < 25) {
            setId(this);
            this.startForeground(mServiceNotificationId, NotificationUtils.smallNotification(this));
            // 前台进程 去处通知栏显示 只有在 api<25才生效
            Intent intent = new Intent(this, getInnerService());
            intent.putExtra(KEY_ID, mServiceNotificationId);
            this.startService(intent);
        }
    }

    /**
     * 先设置ID
     *
     * @param context
     */
    private void setId(Context context) {
        if (mServiceNotificationId != 0)
            return;
        synchronized (MAP_LOCK) {
            /**
             * 存在的刷  就直接写入
             * 不存在的话 就生成再写入
             */
            if (mServiceNotificationId != 0)
                return;
            if (mMaps == null) {
                mMaps = new LinkedHashMap();
            }
            if (mMaps.containsKey(getImplClassName())) {
                mServiceNotificationId = mMaps.get(getImplClassName()).intValue();
            } else {
                mServiceNotificationId = ServiceNotificationIdManager.getInstance().getServiceId(this);
                mMaps.put(getImplClassName(), Integer.valueOf(mServiceNotificationId));
            }
        }
    }
}