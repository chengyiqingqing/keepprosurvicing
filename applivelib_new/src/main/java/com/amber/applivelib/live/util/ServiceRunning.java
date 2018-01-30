package com.amber.applivelib.live.util;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhanghan on 2017/9/6.
 * <p>
 * 判断某个服务是否在运行
 */

public class ServiceRunning {


    public static boolean isServiceRunningNew(Context context, Class<?> serviceClass) {
        if (serviceClass == null || context == null) {
            return false;
        }
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName name = new ComponentName(context, serviceClass.getClass());
            PendingIntent pendingIntent = manager.getRunningServiceControlPanel(name);
            return pendingIntent != null;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        if (serviceClass == null || context == null) {
            return false;
        }
        LiveLog.log("ServiceRunning isServiceRunning " + serviceClass.getSimpleName());
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningService = manager.getRunningServices(Integer.MAX_VALUE);
            if (null != runningService && runningService.size() != 0) {
                for (ActivityManager.RunningServiceInfo serviceInfo : runningService) {
                    if (TextUtils.equals(serviceClass.getName(), serviceInfo.service.getClassName())){
                        return true;
                    }
                }
            }
            runningService.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, String className) {
        if (className == null || context == null) {
            return false;
        }
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningService = manager.getRunningServices(Integer.MAX_VALUE);
            if (null != runningService && runningService.size() != 0) {
                for (ActivityManager.RunningServiceInfo serviceInfo : runningService) {
                    if (className.equals(serviceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, String pkgName, String className[]) {
        if (className == null || context == null || pkgName == null) {
            return false;
        }
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningService = manager.getRunningServices(Integer.MAX_VALUE);
            if (null != runningService && runningService.size() != 0) {
                List classNames = Arrays.asList(className);
                for (ActivityManager.RunningServiceInfo serviceInfo : runningService) {
                    if (pkgName.equals(serviceInfo.service.getPackageName()) && classNames.contains(serviceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
