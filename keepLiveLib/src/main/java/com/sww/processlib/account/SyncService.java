package com.sww.processlib.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.sww.processlib.onepix.OnePixLiveService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shaowenwen on 2018/1/8.
 */

public class SyncService extends Service {

    private static final Object syncLock = new Object();
    private static SyncAdapter syncAdapter = null;
    private static int count=0;
    private static final String TAG = "sww";
    private static Context mContext;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        timer=new Timer();
        synchronized (syncLock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

    class SyncAdapter extends AbstractThreadedSyncAdapter {

        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            getContext().getContentResolver().notifyChange(AppAccountProvider.CONTENT_URI, null, false);
            //do sync here
            Log.e("sww","onPerformSync");
            // 写一个轮询，来检查当前Service是不是死掉了。
            count=0;
            timer.cancel();
            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e(TAG, "run: First "+(++count) );
                }
            },200,2000);
        }
    }

    /**
     * 利用帐号同步机制拉活
     * @param context
     */
    public static void startAccountSync(Context context){
//        mContext=(MainActivity)context;
        Log.e(TAG, "startAccountSync: --- 1" );
        String accountType = "com.sww.processlib";//accountType是包名。
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account account = null;
        Account[] accounts = accountManager.getAccountsByType(accountType);
        if(accounts.length>0){
            Log.e(TAG, "startAccountSync: --- 2" +accounts[0] );
            account = accounts[0];
        }else {
            Log.e(TAG, "startAccountSync: --- 3" );
            account = new Account("sww", accountType);
        }

        if(accountManager.addAccountExplicitly(account,null,null)){
            Log.e(TAG, "startAccountSync: ---4 下面设置了authority" );
            String authority = accountType;

            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

            long sync_interval = 1;//15分钟  --  15*60
            ContentResolver.setIsSyncable(account,authority,1);

            ContentResolver.setSyncAutomatically(account, authority, true);  //网络连接状态下自动同步
            //new Bundle()
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), sync_interval);//设置同步时间间隔
            //AppAccountProvider.CONTENT_URI_BASE
            ContentResolver.requestSync(account, authority, settingsBundle);

        }else {
            Log.e(TAG, "startAccountSync: --- 4" );
        }
    }

}
