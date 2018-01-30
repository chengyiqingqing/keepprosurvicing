package com.amber.applivelib.account;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by chenwei on 2017/4/18.
 * 新方法(AccountSyncAdapter)
 *  建立SyncAdapter这个系统服务Service，利用系统的定时器对程序数据ContentProvider进行更新  --定时检查。
 */

public class AccountSyncService extends Service {

    //对象锁。
    private static final Object sSyncAdapterLock = new Object();
    private static AccountSyncAdapter sSyncAdapter = null;

    //初始化SyncAdapter;
    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new AccountSyncAdapter(getApplicationContext(), true);
            }
        }
        Log.d("AccountSyncService", "---sync-service---onCreate---- ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("AccountSyncService", "---sync-service---bind---- ");
        return sSyncAdapter.getSyncAdapterBinder();
    }

    /**
     * 重写构造方法。
     * 利用系统的定时器对程序数据ContentProvider进行更新
     */
    static class AccountSyncAdapter extends AbstractThreadedSyncAdapter {

        public AccountSyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
            Log.d("AccountSyncService", "---sync-adapter---onCreate---- ");

        }

        public AccountSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
            super(context, autoInitialize, allowParallelSyncs);
        }

        /**
         * 执行同步的方法。在这里启动服务。
         * @param account
         * @param extras
         * @param authority
         * @param provider
         * @param syncResult
         */
        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            //利用系统的定时器，对AppAccountProvider进行同步操作。
            getContext().getContentResolver().notifyChange(AppAccountProvider.getUri(account.type), null, false);
            Log.d("AccountSyncService", "---sync-adapter---PerformSync---- ");
            Log.d("AccountSyncAdapter", "---accountType--- " + account.type);
            Log.d("AccountSyncAdapter", "----acciountName---- " + account.name);
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName(account.type, account.name);
            intent.setComponent(componentName);
            getContext().startService(intent);
        }
    }
}
