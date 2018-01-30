package com.amber.applivelib.memory;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chenwei on 2017/4/28.
 */

public class UiHistoryUtil {

    private static final String FILE_SELF_OOM_ADJ = "/proc/self/oom_adj";
    private static final long EXIT_DELAY = 10000L;
    private static Integer mHistory = Integer.valueOf(0);
    private static UiHistoryUtil.ExitHandler mHandler = new UiHistoryUtil.ExitHandler();
    public static final int MAIN_UI_EXIT = 100001;
    public static final int MAIN_ALL_EXIT = 100002;

    public UiHistoryUtil() {
    }

    public static void push() {
        removeExitMsg();
        Integer var0 = mHistory;
        Integer var1 = mHistory;
        synchronized(mHistory) {
            mHistory = Integer.valueOf(mHistory.intValue() + 1);
        }
    }

    public static void pop() {
        Integer var0 = mHistory;
        Integer var1 = mHistory;
        synchronized(mHistory) {
            if(mHistory.intValue() != 0 && (mHistory = Integer.valueOf(mHistory.intValue() - 1)).intValue() >= 0) {
                sendExitMsg();
            }

        }
    }

    public static void cleanUpStack() {
        Integer var0 = mHistory;
        synchronized(mHistory) {
            mHistory = Integer.valueOf(0);
        }
    }

    public static void removeExitMsg() {
        mHandler.removeMessages(100001);
    }

    public static void sendExitMsg() {
        mHandler.sendEmptyMessageDelayed(100001, 10000L);
    }

    public static void sendExitAllMsgNow() {
        mHandler.sendEmptyMessageDelayed(100002, 0L);
    }

    public static void sendExitAllMsg() {
        mHandler.sendEmptyMessageDelayed(100002, 10000L);
    }

    public static void checkAndSendExitMsg() {
        if(UiHistoryUtil.ExitHandler.canExit()) {
            mHandler.sendEmptyMessageDelayed(100001, 30000L);
        }

    }

    public static void SendExitAndStartMsg(Context context) {
        mHandler.sendEmptyMessageDelayed(100003, 30000L);
    }

    private static class ExitHandler extends Handler {
        private ExitHandler() {
        }

        public static boolean canExit() {
            BufferedReader var0 = null;

            boolean var61;
            try {
                Runtime var6 = Runtime.getRuntime();
                Process process = var6.exec("cat /proc/self/oom_adj");
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                var0 = new BufferedReader(isr);
                String line;
                if(null == (line = var0.readLine())) {
                    return false;
                }

                Log.d("UiHistoryUtil", "------line------ " + line);
                var61 = Integer.parseInt(line) > 0;
            } catch (Exception var17) {
                var17.printStackTrace();
                return false;
            } finally {
                if(var0 != null) {
                    try {
                        var0.close();
                    } catch (Exception var16) {
                        var16.printStackTrace();
                    }
                }

            }

            return var61;
        }

        public void handleMessage(Message var1) {
            switch(var1.what) {
                case 100001:
                    if(canExit()) {
                        System.runFinalization();
                        System.exit(0);
                        return;
                    }

                    this.removeMessages(100001);
                    this.sendEmptyMessageDelayed(100001, 10000L);
                    return;
                case 100002:
                    this.removeMessages(100002);
                    System.runFinalization();
                    System.exit(0);
                    break;
                case 100003:
                    this.removeMessages(100003);
            }

        }
    }
}
