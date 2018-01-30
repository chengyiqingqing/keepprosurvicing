package com.amber.applivelib.live.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by zhanghan on 2017/9/5.
 * <p>
 * 保活服务
 */
public class HandlerUtils {

    public interface OnReceiveMessageListener {
        void handlerMessage(Message message);
    }

    public static class HandlerReceiver extends Handler {
        WeakReference<OnReceiveMessageListener> mOnReceiveMessageListener;

        public HandlerReceiver(HandlerUtils.OnReceiveMessageListener onReceiveMessageListener) {
            /**
             * 获取主要的 避免内存泄漏
             */
            super(Looper.getMainLooper());
            /**
             * 主要的消息
             */
            this.mOnReceiveMessageListener = new WeakReference(onReceiveMessageListener);
        }

        public void setOnReceiveMessageListener(HandlerUtils.OnReceiveMessageListener onReceiveMessageListener) {
            if (this.mOnReceiveMessageListener != null)
                this.mOnReceiveMessageListener.clear();
            if (onReceiveMessageListener != null)
                this.mOnReceiveMessageListener = new WeakReference(onReceiveMessageListener);
        }

        public void handleMessage(Message message) {
            if (this.mOnReceiveMessageListener == null)
                return;
            try {
                OnReceiveMessageListener messageListener = this.mOnReceiveMessageListener.get();
                if (messageListener == null)
                    return;
                messageListener.handlerMessage(message);
            } catch (Exception e) {
            }
        }
    }

}
