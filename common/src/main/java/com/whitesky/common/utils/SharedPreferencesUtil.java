package com.whitesky.common.utils;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil
{
    private SharedPreferences.Editor mEditor;
    
    private SharedPreferences mPreferences;
    
    public SharedPreferencesUtil(Context context, String fileName)
    {
        this.mPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
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
    
    public boolean putBoolean(String name, Boolean value)
    {
        mEditor.putBoolean(name, value);
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
    
    public boolean putString(String name, String value)
    {
        mEditor.putString(name, value);
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
    
    public Boolean getBoolean(String key, boolean defValue)
    {
        return mPreferences.getBoolean(key, defValue);
    }
    
    public int getInt(String key, int defValue)
    {
        return mPreferences.getInt(key, defValue);
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
