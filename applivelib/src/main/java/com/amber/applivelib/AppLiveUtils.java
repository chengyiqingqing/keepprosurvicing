package com.amber.applivelib;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.amber.applivelib.daemon.DaemonService;
import com.amber.applivelib.jobservice.AmberJobService;
import com.amber.applivelib.memory.SuicideService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenwei on 2017/4/28.
 *
 * 注：改成静态方法
 */

public class AppLiveUtils {
    public static final String LOCKER_MAIN_HEART_SERVICE_NAME = "com.amber.lockscreen.LockerHeartService";
    public static final String LOCKER_SKIN_HEART_SERVICE_NAME = "com.amber.lockscreen.them.ThemeHeartService";
    public static final String WIDGET_MAIN_HEART_SERVICE_NAME = "mobi.infolife.ezweather.widget.common.mulWidget.HeartService";
    public static final String WIDGET_SKIN_HEART_SERVICE_NAME = "mobi.infolife.ezweather.widget.common.active.live.ThemeHeartService";
    public static final String WIDGET_LITTLE_WINDOW_ACTIVITY_NAME = "mobi.infolife.ezweather.widget.common.active.live.LittleWindowActivity";
    public static final String LOCKER_LITTLE_WINDOW_ACTIVITY_NAME = "com.amber.lockscreen.them.LittleWindowActivity";
    public static final String EZWEATHER_PLUGIN = "EZWEATHER_PLUGIN";
    public static final String META_DATA_VALUE = "mobi.infolife.ezweather.plugin.widgetskin";
    public static final String LOCKER_SKIN = "LOCKER_SKIN";
    private static final String TAG = AppLiveUtils.class.getSimpleName();
    private Context mContext;

    public AppLiveUtils(Context context) {
        mContext = context;
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        if (serviceClass == null || mContext == null) {
            Log.d(TAG, "isServiceRunning: serviceClass or mContext is null!");
            return false;
        }
        try {
            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningService = manager.getRunningServices(Integer.MAX_VALUE);
            if (null != runningService && runningService.size() != 0) {
                for (ActivityManager.RunningServiceInfo serviceInfo : runningService) {
                    if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
            Log.d(TAG, serviceClass + " isServiceRunning: " + false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isServiceRunning(String className) {
        if (className == null || mContext == null) {
            Log.d(TAG, "isServiceRunning: className or mContext is null!");
            return false;
        }
        try {
            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningService = manager.getRunningServices(Integer.MAX_VALUE);
            if (null != runningService && runningService.size() != 0) {
                for (ActivityManager.RunningServiceInfo serviceInfo : runningService) {
                    if (className.equals(serviceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
            Log.d(TAG, className + " isServiceRunning: " + false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isServiceRunning(String pkgName, String className[]) {
        if (className == null || mContext == null || pkgName == null) {
            Log.d(TAG, "isServiceRunning: serviceClass or mContext is null!");
            return false;
        }
        try {
            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
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

    public void startJobService(Context context, Class serviceClass) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(context, AmberJobService.class);
            intent.putExtra(AmberJobService.INTENT_KEY_NAME, serviceClass.getName());
            context.startService(intent);
        }
    }

    public void startDaemonService(Context context, Class serviceClass) {
        Intent intent = new Intent(context, DaemonService.class);
        intent.putExtra(DaemonService.INTENT_KEY_SERVICE_NAME, serviceClass.getName());
        context.startService(intent);
    }

    public void startAccountSync(String accountName, String accountType, String accountAuthority, int syncable) {
        Account account = new Account(accountName, accountType);
        AccountManager accountManager = (AccountManager) mContext.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {

        } else {

        }
        ContentResolver.setIsSyncable(account, accountAuthority, syncable);
        ContentResolver.setSyncAutomatically(account, accountAuthority, true);
        ContentResolver.addPeriodicSync(account, accountAuthority, Bundle.EMPTY, 10);
    }

    public void startSuicideService(Context context, Class serviceClass) {
        Intent intent = new Intent(context, SuicideService.class);
        intent.putExtra(SuicideService.INTENT_KEY_SERVICE_NAME, serviceClass.getName());
        context.startService(intent);
    }

    public static List<String> getInstalledAppList(Context context, int flag) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageList = null;
        try {
            packageList = pm.getInstalledPackages(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<>();
        ApplicationInfo aInfo = null;
        if (packageList != null) {
            for (PackageInfo pi : packageList) {
                String pkgName = pi.packageName;
                try {
                    aInfo = pm.getApplicationInfo(pkgName,
                            PackageManager.GET_META_DATA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (aInfo == null || aInfo.metaData == null) {
                    continue;
                }
                String metaData = aInfo.metaData.getString(EZWEATHER_PLUGIN);
                if (aInfo.metaData.getBoolean(LOCKER_SKIN) || (null != metaData && metaData.equals(META_DATA_VALUE))) {
                    list.add(pi.packageName);
                }
            }
        }
        return list;
    }

    public void clear() {
        mContext = null;
    }
}
