package com.sww.processlib.onepix;

/**
 * Created by Administrator on 2018/1/11.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;

public class OnePixActivityManager {

    private Context mContext;

    private WeakReference<Activity> mActivityWref;

    public static OnePixActivityManager instance;

    private static final String TAG = "sww";

    public static OnePixActivityManager getInstance(Context pContext) {
        if (instance == null) {
            instance = new OnePixActivityManager(pContext.getApplicationContext());
        }
        return instance;
    }

    private OnePixActivityManager(Context pContext) {
        this.mContext = pContext;
    }

    public void setActivity(Activity pActivity) {
        Log.e(TAG, "setActivity: 给设置activity" );
        mActivityWref = new WeakReference<Activity>(pActivity);
    }

    public void startActivity() {
        Log.e(TAG, "startActivity: -------" );
        OnePixActivity.actionToLiveActivity(mContext);
    }

    public void finishActivity() {
        Log.e(TAG, "finishActivity: -------" );
        //结束掉LiveActivity
        if (mActivityWref != null) {
            Activity activity = mActivityWref.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}

