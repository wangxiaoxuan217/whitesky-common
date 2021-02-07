package com.wx.discovery.ptop;

import com.wx.discovery.LanCommManager;

public class CommunicatorImpl implements Communicator
{
    private CommunicatorImpl()
    {
    }
    
    public static CommunicatorImpl getImpl()
    {
        return CommunicatorImplHolder.sInstance;
    }
    
    private static class CommunicatorImplHolder
    {
        private static final CommunicatorImpl sInstance = new CommunicatorImpl();
    }
    
    @Override
    public void sendCommand(Command command)
    {
        LanCommManager.thread_pool.execute(new CommandRunnable(command));
    }
}
