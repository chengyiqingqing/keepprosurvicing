package com.sww.processproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by shaowenwen on 2018/1/26.
 */

public class CircleImageDrawable extends Drawable {

    private Paint mPaint;
    private int mWidth;
    private Bitmap mBitmap ;
    public android.os.Handler handler=new android.os.Handler();
    public static int count=0;
    public int[] countArr=new int[]{2};

    public CircleImageDrawable(Bitmap bitmap){
        mBitmap = bitmap ;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
    }

    int i=2;
    @Override
    public void draw(@NonNull Canvas canvas) {
        this.draw(canvas);
//        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth /2, mPaint);
        executeCanvas(canvas,countArr);
    }

    public void executeCanvas(final Canvas canvas, final int[] countArr){
        Log.e("sww", "run: "+countArr[0] );
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth /2, mPaint);

    }

    //asd
    @Override
    public int getIntrinsicWidth()
    {
        return mWidth;
    }

    //asd
    @Override
    public int getIntrinsicHeight()
    {
        return mWidth;
    }

    @Override
    public void setAlpha(int alpha)
    {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        mPaint.setColorFilter(cf);
    }
    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }
}
