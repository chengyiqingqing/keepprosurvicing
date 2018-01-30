package com.amber.applivelib.live.assist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amber.applivelib.live.AppLiveManager;
import com.amber.applivelib.live.util.LiveLog;

/**
 * Created by zhanghan on 2017/9/5.
 * <p>
 * 保活服务，< 5.0 的大部分机型上生效，
 * 用于定时启动LiveService
 */

public class LiveAlarmService extends Service {

    public static void startLiveAlarmService(Context context) {
        LiveLog.log("LiveAlarmService startLiveAlarmService");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, LiveAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0x0002d003, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        long REPEAT_TIME = 2 * 60 * 1000l;
        // 错开与JobService的机制
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + REPEAT_TIME / 2, REPEAT_TIME * 2, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + REPEAT_TIME / 2, REPEAT_TIME * 3, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + REPEAT_TIME / 2, REPEAT_TIME * 4, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + REPEAT_TIME / 2, REPEAT_TIME * 1, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + REPEAT_TIME / 2, REPEAT_TIME * 2, pendingIntent);
        LiveLog.log("LiveAlarmService startLiveAlarmService end");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LiveLog.log("LiveAlarmService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LiveLog.log("LiveAlarmService onStartCommand");
        /**
         * 尝试重启
         */
        AppLiveManager.tryRestart(getBaseContext());
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
