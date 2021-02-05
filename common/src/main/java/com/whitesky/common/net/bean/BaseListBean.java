package com.whitesky.common.net.bean;

import java.util.List;

public class BaseListBean<T>
{
    private int errorcode;

    private String errormessage;
    
    private List<T> data;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public void setResult(List<T> result)
    {
        this.data = result;
    }
    
    public List<T> getResult()
    {
        return data;
    }
    
}
