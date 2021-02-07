package com.whitesky.common.asynctask;

public interface IProgressUpdate<Progress>
{
    void onProgressUpdate(Progress... values);
}