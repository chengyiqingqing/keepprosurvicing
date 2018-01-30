package com.sww.processproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private ArrayList<String> arrayList=new ArrayList<>();
    private Context mContext;
    private static final String TAG = "sww";
    private Bitmap originBitmap;
    private Bitmap bitmap;
    private Canvas canvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mContext=Main2Activity.this;
        Button click= (Button) findViewById(R.id.bt_click);
        final ImageView image= (ImageView) findViewById(R.id.iv_drawable);
//        final CircleImageDrawable circleImageDrawable=new CircleImageDrawable(bitmap);
        originBitmap=Bitmap
        bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.push_icon);
        canvas=new Canvas(bitmap);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageBitmap(getBitmap());
            }
        });
    }


    public Bitmap getBitmap(){

        int width=bitmap.getWidth();
        int Height=bitmap.getHeight();
        canvas.save();
        canvas.rotate(30,width/2,Height/2);
//        canvas.drawBitmap();
        canvas.restore();
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*arrayList=queryInstalledMarketPkgs(mContext);

                if (arrayList.size()!=0){
                    for (String str:arrayList){
                        Log.e(TAG, "onClick: "+str );
                    }
                }else{
                    Log.e(TAG, "onClick: 0" );
                }*/

    }

    /**
     * 获取当前手机上的应用商店数量
     *
     * @param context
     * @return
     */
    public static ArrayList<String> queryInstalledMarketPkgs(Context context) {
        ArrayList<String> pkgs = new ArrayList<>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("market://details?id="));
        PackageManager pm = context.getPackageManager();
        // 通过queryIntentActivities获取ResolveInfo对象
        List<ResolveInfo> infos = pm.queryIntentActivities(intent,
                PackageManager.MATCH_ALL);
//        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (infos == null || infos.size() == 0)
            return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);
        }
        return pkgs;
    }

}
