package com.amber.applivelib.jobservice;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amber.applivelib.AppLiveUtils;
import com.amber.applivelib.R;
import com.amber.applivelib.live.service.abs.AbsFrontJobService;
import com.amber.applivelib.live.service.abs.AbsInnerService;

import java.util.List;

import static com.amber.applivelib.AppLiveUtils.LOCKER_LITTLE_WINDOW_ACTIVITY_NAME;
import static com.amber.applivelib.AppLiveUtils.LOCKER_MAIN_HEART_SERVICE_NAME;
import static com.amber.applivelib.AppLiveUtils.LOCKER_SKIN_HEART_SERVICE_NAME;
import static com.amber.applivelib.AppLiveUtils.WIDGET_LITTLE_WINDOW_ACTIVITY_NAME;
import static com.amber.applivelib.AppLiveUtils.WIDGET_MAIN_HEART_SERVICE_NAME;
import static com.amber.applivelib.AppLiveUtils.WIDGET_SKIN_HEART_SERVICE_NAME;

/**
 * Created by chenwei on 2017/4/28.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AmberJobService extends AbsFrontJobService {

    public static final String INTENT_KEY_NAME = "Service_Name";
    private String serviceName;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public Class<? extends AbsInnerService> getInnerService() {
        return AmberJobServiceInner.class;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
        } else {
            serviceName = intent.getStringExtra(INTENT_KEY_NAME);
            if (serviceName == null) {
                stopSelf();
            }
            startJobScheduler();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        if (serviceName != null) {
            try {
                AppLiveUtils appLiveUtils = new AppLiveUtils(mContext);
                if (!appLiveUtils.isServiceRunning(serviceName)) {
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName(getApplicationInfo().packageName, serviceName);
                    intent.setComponent(componentName);
                    startService(intent);
                }
                appLiveUtils.clear();
                startThemeHeartService();

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public void startJobScheduler() {
        Log.v("DaemonService", "start job");
        try {
            JobInfo.Builder builder = new JobInfo.Builder(1221, new ComponentName(getPackageName(), AmberJobService.class.getName()));
            builder.setPeriodic(1000 * 2);
            builder.setPersisted(true);
            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
            // Android24版本才有scheduleAsPackage方法， 期待中
            //Class clz = Class.forName("android.app.job.JobScheduler");
            //Method[] methods = clz.getMethods();
            //Method method = clz.getMethod("scheduleAsPackage", JobInfo.class , String.class, Integer.class, String.class);
            //Object obj = method.invoke(jobScheduler, builder.build(), "com.brycegao.autostart", "brycegao", "test");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startThemeHeartService() {
        if (isScreenOn()) {
            Log.d("AmberJobService", "------isScreenOn-----");
            return;
        }
        AppLiveUtils appLiveUtils = new AppLiveUtils(this);
        List<String> installPlugin = AppLiveUtils.getInstalledAppList(this, 0);
        if (installPlugin != null) {
            ComponentName componentName;
            Intent intent;
            for (String pkgName : installPlugin) {
                if (!(getApplicationContext().getPackageName().equals(pkgName))) {
                    String serviceNameMain;
                    String serviceNameSkin;
                    String activityName;
                    if (pkgName.startsWith("mobi.infolife.ezweather.locker")) {
                        serviceNameMain = LOCKER_MAIN_HEART_SERVICE_NAME;
                        serviceNameSkin = LOCKER_SKIN_HEART_SERVICE_NAME;
                        activityName = LOCKER_LITTLE_WINDOW_ACTIVITY_NAME;
                    } else {
                        serviceNameMain = WIDGET_MAIN_HEART_SERVICE_NAME;
                        serviceNameSkin = WIDGET_SKIN_HEART_SERVICE_NAME;
                        activityName = WIDGET_LITTLE_WINDOW_ACTIVITY_NAME;
                    }
                    if (!appLiveUtils.isServiceRunning(pkgName, new String[]{serviceNameMain, serviceNameSkin})) {
                        Log.e("LittleWindowActivity", "-----AmberJobService----- ");
                        componentName = new ComponentName(pkgName, activityName);
                        intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(componentName);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        appLiveUtils.clear();
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        return powerManager.isScreenOn();
    }

    public static class AmberJobServiceInner extends AbsInnerService {
    }

}
