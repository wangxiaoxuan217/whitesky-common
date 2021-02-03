package com.whitesky.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

public class SharedPreferencesUtil
{
    private SharedPreferences.Editor mEditor;

    private SharedPreferences mPreferences;

    public SharedPreferencesUtil(Context context, String fileName)
    {
        this.mPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        this.mEditor = this.mPreferences.edit();
    }

    public SharedPreferencesUtil(Context context, String fileName, int mode)
    {
        this.mPreferences = context.getSharedPreferences(fileName, mode);
        this.mEditor = this.mPreferences.edit();
    }

    private void syncFlush()
    {
        try
        {
            Runtime.getRuntime().exec("sync");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // 读写配置文件
    public boolean putString(String name, String value)
    {
        mEditor.putString(name, value);
        boolean result = mEditor.commit();
        syncFlush();
        return result;
    }

    public boolean putLong(String name, Long value)
    {
        mEditor.putLong(name, value);
        boolean result = mEditor.commit();
        syncFlush();
        return result;
    }

    public boolean putInt(String name, int value)
    {
        mEditor.putInt(name, value);
        boolean result = mEditor.commit();
        syncFlush();
        return result;
    }

    public boolean putBoolean(String name, Boolean value)
    {
        mEditor.putBoolean(name, value);
        boolean result = mEditor.commit();
        syncFlush();
        return result;
    }

    public boolean remove(String name)
    {
        mEditor.remove(name);
        boolean result = mEditor.commit();
        syncFlush();
        return result;
    }

    public boolean clear()
    {
        mEditor.clear();
        boolean result = mEditor.commit();
        syncFlush();
        return result;
    }

    public long getLong(String key)
    {
        return mPreferences.getLong(key, 0);
    }

    public int getInt(String key)
    {
        return mPreferences.getInt(key, 0);
    }

    public Boolean getBoolean(String key)
    {
        return mPreferences.getBoolean(key, false);
    }

    public String getString(String key)
    {
        return mPreferences.getString(key, "");
    }

    public long getLong(String key, long defValue)
    {
        return mPreferences.getLong(key, defValue);
    }

    public int getInt(String key, int defValue)
    {
        return mPreferences.getInt(key, defValue);
    }

    public Boolean getBoolean(String key, boolean defValue)
    {
        return mPreferences.getBoolean(key, defValue);
    }

    public String getString(String key, String defValue)
    {
        return mPreferences.getString(key, defValue);
    }

    public SharedPreferences.Editor getEditor()
    {
        return mEditor;
    }
}