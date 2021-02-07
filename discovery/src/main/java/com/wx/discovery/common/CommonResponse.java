package com.wx.discovery.common;

import androidx.annotation.Nullable;

/**
 * CommonResponse默认无result
 */
public class CommonResponse<T>
{
    
    public static final int DEFAULT_ERROR_CODE = 12345678;
    
    private boolean success = false;
    
    private int errorCode = DEFAULT_ERROR_CODE;
    
    private T result;
    
    @Nullable
    public T getResult()
    {
        return result;
    }
    
    public void setResult(T t)
    {
        success = true;
        result = t;
    }
    
    public void setErrorCode(int code)
    {
        success = false;
        errorCode = code;
    }
    
    public int getErrorCode()
    {
        return errorCode;
    }
    
    public boolean isSuccess()
    {
        return success;
    }
    
    public void setSuccess(boolean success)
    {
        this.success = success;
    }
}
