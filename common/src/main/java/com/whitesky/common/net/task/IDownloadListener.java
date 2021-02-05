package com.whitesky.common.net.task;

public interface IDownloadListener
{
    void onDownloadProgress(int progress);
    
    void onSuccess();
    
    void onFailed(String error);
}
