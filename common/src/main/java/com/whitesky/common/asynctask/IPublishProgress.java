package com.whitesky.common.asynctask;

public interface IPublishProgress<Progress>
{
    void showProgress(Progress... values);
}
