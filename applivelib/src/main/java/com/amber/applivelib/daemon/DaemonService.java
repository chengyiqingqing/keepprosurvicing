package com.amber.applivelib.daemon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amber.applivelib.live.service.abs.AbsFrontService;
import com.amber.applivelib.live.service.abs.AbsInnerService;

/**
 * Created by chenwei on 2017/4/28.
 */

public class DaemonService extends AbsFrontService {

    private Context context;
    public static final String INTENT_KEY_SERVICE_NAME = "Service_Name";
    private String serviceName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public Class<? extends AbsInnerService> getInnerService() {
        return DaemonServiceInner.class;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
        } else {
            serviceName = intent.getStringExtra(INTENT_KEY_SERVICE_NAME);
            if (serviceName == null) {
                stopSelf();
            } else {
                startAppService(context);
                setAlarm();
            }
        }
        return START_REDELIVER_INTENT;
    }

    private void startAppService(Context context) {
        try {
            Intent heartService = new Intent();
            ComponentName componentName = new ComponentName(context.getPackageName(), serviceName);
            heartService.setComponent(componentName);
            context.startService(heartService);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent restartIntent = new Intent(this, DaemonService.class);
        startService(restartIntent);
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent1 = new Intent(context, DaemonService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0x0002d003, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        long REPEAT_TIME = 60000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + REPEAT_TIME, REPEAT_TIME * 10, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + REPEAT_TIME, REPEAT_TIME, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + REPEAT_TIME, REPEAT_TIME * 3, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + REPEAT_TIME, REPEAT_TIME * 7, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + REPEAT_TIME, REPEAT_TIME * 60, pendingIntent);
    }

    public static class DaemonServiceInner extends AbsInnerService {

    }
}
