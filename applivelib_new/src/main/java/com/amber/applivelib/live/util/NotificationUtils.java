package com.amber.applivelib.live.util;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.amber.applivelib.live.R;

/**
 * Created by zhanghan on 2017/9/5.
 */

public class NotificationUtils {

    public static Notification emptyNotification() {
        return new Notification();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Notification smallNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.push_icon);
        builder.setContentInfo("");
        builder.setContentTitle("");
        return builder.build();
    }
}
