package com.whitesky.common.net.bean;

public class BaseObjectBean<T>
{
    /**
     * status : 1 msg : 获取成功 result : {} 对象
     */
    
    private int errorcode;
    
    private String errormessage;
    
    private T result;
    
    public int getErrorCode()
    {
        return errorcode;
    }
    
    public void setErrorCode(int errorCode)
    {
        this.errorcode = errorCode;
    }
    
    public String getErrorMsg()
    {
        return errormessage;
    }
    
    public void setErrorMsg(String errorMsg)
    {
        this.errormessage = errorMsg;
    }
    
    public T getResult()
    {
        return result;
    }
    
    public void setResult(T result)
    {
        this.result = result;
    }
    
}
