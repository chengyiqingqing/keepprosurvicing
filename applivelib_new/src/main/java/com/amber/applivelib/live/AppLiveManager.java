package com.amber.applivelib.live;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.amber.applivelib.live.core.kitkat.LiveServiceK;
import com.amber.applivelib.live.core.lollipop.LiveServiceL;
import com.amber.applivelib.live.util.ServiceRunning;

/**
 * Created by zhanghan on 2017/9/5.
 * <p>
 * 保活库 使用累
 */

public class AppLiveManager {

    private static final String ABS_WORK_SERVICE = "AbsWorkService";
    private static boolean USE_FRONT = true;
    private static boolean USE_UI = false;

    /**
     * 初始化,只有第一次生效
     *
     * @param context 上下文环境
     */
    public static void init(final Context context) {

        /**
         * 不能使用的话  直接返回
         */
        if (!canUseLib() || (!USE_FRONT && !USE_UI)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 延迟执行   避免集中启动服务
                 */
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startLiveService(context);
            }
        }).start();
    }

    /**
     * 开启保活服务
     *
     * @param context
     */
    private static void startLiveService(Context context) {
        /**
         * 开启了保活服务
         * 当版本号低于修复BUG版本时候 不采用
         */
        if (canUseLib()) {
            /**
             * 可能还没使用就关闭了
             */
            try {
                context.startService(new Intent(context, getLiveService()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否使用前台进程保活
     *
     * @return
     */
    public static boolean isUseFront(Context context) {
        return USE_FRONT;
    }

    /**
     * 是否使用一像素UI保活
     *
     * @return
     */
    public static boolean isUseUi(Context context) {
        return USE_UI;
    }

    /**
     * 添加服务
     *
     * @param services
     */
    public static void addService(Context context, Class<? extends Service>... services) {
        if (services == null || services.length == 0) {
            return;
        }
        String[] serviceNames = new String[services.length];
        for (int index = 0; index < services.length; index++) {
            Class<? extends Service> service = services[index];
            if (service != null) {
                serviceNames[index] = service.getName();
            } else {
                serviceNames[index] = null;
            }
        }
        addService(context, serviceNames);
    }

    /**
     * 添加服务
     *
     * @param services
     */
    public static void addService(Context context, String... services) {
        if (services == null || services.length == 0) {
            return;
        }
        Intent intent = new Intent(context, getLiveService());
        intent.putExtra(ABS_WORK_SERVICE, services);
        context.startService(intent);
    }

    /**
     * 获取传输的服务
     *
     * @param context
     * @param intent
     * @return
     */
    public static String[] getServices(Context context, Intent intent) {
        if (intent == null) {
            return null;
        }
        try {
            String[] services = intent.getStringArrayExtra(ABS_WORK_SERVICE);
            return services;
        } catch (Exception e) {
            // 参数不正确
            return null;
        }
    }

    /**
     * 尝试在必要的时候  重启
     *
     * @param context
     */
    public static void tryRestart(Context context) {
        if (ServiceRunning.isServiceRunning(context, getLiveService())) {
            return;
        }
        /**
         * 进行重启操作
         */
        startLiveService(context);
    }

    /**
     * 根据系统版本 决定使用那个Services
     *
     * @return
     */
    public static Class<?> getLiveService() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return LiveServiceK.class;
        } else {
            return LiveServiceL.class;
        }
    }

    /**
     * 获取当前保活进程名称
     *
     * @return
     */
    public static String getLiveProcessName() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return "";
        } else {
            return "new_live";
        }
    }

    /**
     * 保活库是否可用
     * 因为使用范围是 < 25
     *
     * @return
     */
    private static boolean canUseLib() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1;
    }

}
