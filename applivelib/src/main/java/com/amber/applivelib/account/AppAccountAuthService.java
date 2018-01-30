package com.amber.applivelib.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by chenwei on 2017/4/18.
 */

public class AppAccountAuthService extends Service {

    private WeatherAccountAuthenticator mAuthenticator;
    public static final String ACCOUNT_NAME = "Account";

    public static Account GetAccount(String accountType) {
        final String accountName = ACCOUNT_NAME;
        return new Account(accountName, accountType);
    }

    /**
     * 初始化账户类。
     *
     */
    @Override
    public void onCreate() {
        mAuthenticator = new WeatherAccountAuthenticator(this);
        Log.d("WeatherAccountAuthServi", "------auth---service---- ");
    }

    /**
     * 获取这个账号对象。
     * @return
     */
    private WeatherAccountAuthenticator getAuthenticator() {
        if (mAuthenticator == null)
            mAuthenticator = new WeatherAccountAuthenticator(this);
        return mAuthenticator;
    }

    /**
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return getAuthenticator().getIBinder();
    }

    /**
     *
     */
    class WeatherAccountAuthenticator extends AbstractAccountAuthenticator {

        public WeatherAccountAuthenticator(Context context) {
            super(context);
            Log.d("WeatherAccountAuthentic", "------auth---cator---- ");
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options)
                throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options)
                throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }
    }
}
