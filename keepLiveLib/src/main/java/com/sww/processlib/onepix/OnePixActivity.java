package com.sww.processlib.onepix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sww.processlib.R;

/**
 * Created by shaowenwen on 2018/1/5.
 */

public class OnePixActivity extends AppCompatActivity {
    private static final String TAG = "sww";

    public static final String TAG1 = OnePixActivity.class.getSimpleName();

    public static void actionToLiveActivity(Context pContext) {
        Log.e(TAG, "actionToLiveActivity: ☆☆☆" );
        Intent intent = new Intent(pContext, OnePixActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG1+" -- onCreate");
        setContentView(R.layout.activity_pix);

        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        //宽高设计为1个像素
        attributes.width = 1;
        attributes.height = 1;
        //起始坐标
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);
        OnePixActivityManager.getInstance(this).setActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG1+" -- onDestroy");
    }

}