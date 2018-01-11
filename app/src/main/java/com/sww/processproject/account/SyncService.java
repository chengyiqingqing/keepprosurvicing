package com.sww.processproject.account;

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

/**
 * Created by shaowenwen on 2018/1/8.
 */

public class SyncService extends Service {

    private static final Object syncLock = new Object();
    private static SyncAdapter syncAdapter = null;
    private static final String TAG = "sww";

    @Override
    public void onCreate() {
        super.onCreate();
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
            //应用在这里开启。
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName(account.type, account.name);
            intent.setComponent(componentName);
            getContext().startService(intent);
            //上面的代码要是没有救他，那么就是它自己活的。

            //既然它不死，但又是隔段时间执行一次的。
                // 那么我就写一个轮询，来检查当前Service是不是死掉了。
        }
    }

    /**
     * 利用帐号同步机制拉活
     * @param context
     */
    public static void startAccountSync(Context context){
        Log.e(TAG, "startAccountSync: --- 1" );
        String accountType = "com.sww.processproject";//accountType是包名。
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

        }else {//走了这里，说明我设置的时间间隔没有生效。利用的是系统默认的时间间隔。
            Log.e(TAG, "startAccountSync: --- 4" );
        }
    }

}
