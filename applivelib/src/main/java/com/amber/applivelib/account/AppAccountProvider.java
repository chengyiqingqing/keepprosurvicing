package com.amber.applivelib.account;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by chenwei on 2017/4/18.
 *
 * 通过ContentProvider用作数据同步，由于并没有实际数据同步，--（模拟数据同步）
 * 所以此处就直接建立一个空的ContentProvider即可。
 *
 * 之后就是在Manifest中声明。
 */

public class AppAccountProvider extends ContentProvider {

    public static final String AUTHORITY = "com.amber.weather.accout.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;
    public static final String TABLE_NAME = "data";
    //数据。
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_BASE + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return new String();
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    //最后返回一个Uri;
    public static  Uri getUri(String pkgName) {
        return Uri.parse("content://" + pkgName + ".account.provider" + "/" + TABLE_NAME);
    }

}
