package com.amber.applivelib.live.util;

import android.content.Context;

/**
 * Created by zhanghan on 2017/10/13.
 */

public class GroupUtil {

    /**
     * 实验对照使用，区分A／B组
     */
    private static final String FIRST_OPEN_TIME = "fist_open_time";

    /**
     * 是否是B组
     *
     * @param context
     * @return
     */
    public synchronized static boolean isGroupB(Context context) {
        return true;
    }

}
