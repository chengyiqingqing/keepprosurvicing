package com.amber.applivelib.live.util;

import android.util.Log;

/**
 * Created by zhanghan on 2017/9/5.
 */

public class LiveLog {

    private static final String TAG = "LiveLog";

    public static void log(String... info) {
        if (true) {
            if (info == null || info.length == 0)
                return;
            StringBuffer stringBuffer = new StringBuffer();
            for (String item : info) {
                stringBuffer.append(item);
                stringBuffer.append(" ");
            }
            Log.d(TAG, stringBuffer.toString());
        }
    }
}
