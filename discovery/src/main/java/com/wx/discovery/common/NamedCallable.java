package com.wx.discovery.common;

import java.util.concurrent.Callable;

public abstract class NamedCallable<T> implements Callable<T>
{
    protected final String name;
    
    public NamedCallable(String name)
    {
        this.name = name;
    }
    
    @Override
    public T call()
        throws Exception
    {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name);
        T t = null;
        try
        {
            t = execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Thread.currentThread().setName(oldName);
        }
        return t;
    }
    
    protected abstract T execute();
}