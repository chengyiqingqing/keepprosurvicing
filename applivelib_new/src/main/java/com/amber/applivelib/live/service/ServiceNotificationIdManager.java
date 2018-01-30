package com.amber.applivelib.live.service;

import android.content.Context;

/**
 * Created by zhanghan on 2017/9/21.
 * <p>
 * 用于管理Service的ID
 */
public class ServiceNotificationIdManager {

    private static final int START_ID = 21;
    private static volatile ServiceNotificationIdManager mInstance;
    private int mId;

    /**
     * 获取到管理实例
     *
     * @return
     */
    public static ServiceNotificationIdManager getInstance() {
        if (mInstance == null) {
            synchronized (ServiceNotificationIdManager.class) {
                if (mInstance == null) {
                    mInstance = new ServiceNotificationIdManager();
                }
            }
        }
        return mInstance;
    }

    private ServiceNotificationIdManager() {
        mId = START_ID;
    }

    /**
     * 设置ServiceID
     */
    public synchronized int getServiceId(Context context) {
        mId++;
        return mId;
    }


}
