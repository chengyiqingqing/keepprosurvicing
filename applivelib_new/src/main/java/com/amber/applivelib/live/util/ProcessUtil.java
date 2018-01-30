package com.amber.applivelib.live.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanghan on 2017/9/11.
 * <p>
 * 进程相关的工具库
 */

public class ProcessUtil {
    /**
     * 判断是不是主进程
     * 一些子进程可能不需要初始化一些工作
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        String nowProcess = getCurProcessName(context);
        return TextUtils.isEmpty(nowProcess) || !nowProcess.contains(":");
    }

    /**
     * 判断是不是其他的进程
     *
     * @param context
     * @param other
     * @return
     */
    public static boolean isOtherProcess(Context context, String other) {
        String otherProcess = getCurProcessName(context);
        if (TextUtils.isEmpty(otherProcess))
            return true;
        if (otherProcess.contains(":")) {
            Pattern p = Pattern.compile("[^:]+:([^:]+)");
            Matcher m = p.matcher(otherProcess);
            if (!m.find())
                return TextUtils.isEmpty(other);
            for (int i = 0; i <= m.groupCount(); i++) {
                if (TextUtils.equals(m.group(i), other))
                    return true;
            }
        }
        return false;
    }


    /**
     * 获取进程名称
     *
     * @param context
     * @return
     */
    @Nullable
    public static String getCurProcessName(@NonNull Context context) {
        if (context == null)
            return null;
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (mActivityManager == null)
                return "";
            List<ActivityManager.RunningAppProcessInfo> list = mActivityManager.getRunningAppProcesses();
            if (list == null || list.size() == 0)
                return "";
            for (ActivityManager.RunningAppProcessInfo appProcess : list) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
