package com.amber.applivelib.memory;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;

/**
 * Created by chenwei on 2017/4/28.
 */

public class SuicideService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SuicideService(String name) {
        super(name);
    }

    public SuicideService() {
        super("SuicideService");
    }

    public static final String INTENT_KEY_SERVICE_NAME = "Service_Name";
    private String serviceName;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
        } else {
            serviceName = intent.getStringExtra(INTENT_KEY_SERVICE_NAME);
            if (serviceName == null) {
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Intent serviceIntent = new Intent();
            ComponentName componentName = new ComponentName(getApplicationInfo().packageName, serviceName);
            serviceIntent.setComponent(componentName);
            startService(serviceIntent);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        Process.killProcess(android.os.Process.myPid());
    }
}
