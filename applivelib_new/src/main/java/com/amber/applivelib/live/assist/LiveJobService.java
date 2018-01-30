package com.amber.applivelib.live.assist;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.amber.applivelib.live.AppLiveManager;
import com.amber.applivelib.live.util.LiveLog;

/**
 * Created by zhanghan on 2017/9/5.
 * <p>
 * 保活服务，在5.0+以上大部分机型上生效，
 * 用于定时启动LiveService
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LiveJobService extends JobService {

    private static final int JOB_SERVICE_ID = 2122;
    /**
     * 两分钟时间  避免太频繁
     */
    private static final long JOB_SERVICE_TIME = 2 * 60 * 1000l;

    public static void startLiveJobService(Context context) {
        LiveLog.log("LiveJobService startLiveJobService");
        try {
            JobInfo.Builder builder = new JobInfo.Builder(JOB_SERVICE_ID, new ComponentName(context.getPackageName(), LiveJobService.class.getName()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setPeriodic(JOB_SERVICE_TIME, JOB_SERVICE_TIME);
            } else {
                builder.setPeriodic(JOB_SERVICE_TIME);
            }
            builder.setPersisted(true);
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
            LiveLog.log("LiveJobService startLiveJobService end");
            // Android24版本才有scheduleAsPackage方法， 期待中
            //Class clz = Class.forName("android.app.job.JobScheduler");
            //Method[] methods = clz.getMethods();
            //Method method = clz.getMethod("scheduleAsPackage", JobInfo.class , String.class, Integer.class, String.class);
            //Object obj = method.invoke(jobScheduler, builder.build(), "com.brycegao.autostart", "brycegao", "test");
        } catch (Exception ex) {
            ex.printStackTrace();
            LiveLog.log("LiveJobService", ex.toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LiveLog.log("LiveJobService onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LiveLog.log("LiveJobService onCreate");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LiveLog.log("LiveJobService onStartJob");
        /**
         * 尝试重启
         */
        AppLiveManager.tryRestart(getBaseContext());
        stopSelf();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LiveLog.log("LiveJobService onStopJob");
        return false;
    }
}
