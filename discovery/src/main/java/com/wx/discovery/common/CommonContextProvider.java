package com.wx.discovery.common;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 无侵入式获取全局Context
 */
public class CommonContextProvider extends ContentProvider
{
    @Override
    public boolean onCreate()
    {
        ContextVal.setContext(getContext());
        ContextVal.getContext().sendBroadcast(new Intent(ContextVal.ACTION_APP_BOOT_COMPLETED));
        return false;
    }
    
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
        String sortOrder)
    {
        return null;
    }
    
    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }
    
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values)
    {
        return null;
    }
    
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs)
    {
        return 0;
    }
    
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }
}
