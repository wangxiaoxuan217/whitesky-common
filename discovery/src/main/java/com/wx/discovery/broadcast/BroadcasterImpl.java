package com.wx.discovery.broadcast;

import com.wx.discovery.LanCommManager;

public class BroadcasterImpl implements Broadcaster
{
    private BroadcasterImpl()
    {
    }
    
    public static BroadcasterImpl getImpl()
    {
        return BroadcasterImplHolder.sInstance;
    }
    
    @Override
    public void broadcast(byte[] bytes)
    {
        LanCommManager.thread_pool.execute(new BroadcastRunnable().setData(bytes));
    }
    
    private static class BroadcasterImplHolder
    {
        private static final BroadcasterImpl sInstance = new BroadcasterImpl();
    }
}
