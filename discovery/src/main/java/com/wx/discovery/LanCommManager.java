package com.wx.discovery;

import com.wx.discovery.broadcast.Broadcaster;
import com.wx.discovery.broadcast.BroadcasterImpl;
import com.wx.discovery.common.Dispatcher;
import com.wx.discovery.ptop.Communicator;
import com.wx.discovery.ptop.CommunicatorImpl;
import com.wx.discovery.receive.Receiver;
import com.wx.discovery.receive.ReceiverImpl;
import com.wx.discovery.search.Searcher;
import com.wx.discovery.search.SearcherImpl;

import java.util.concurrent.ThreadPoolExecutor;

public class LanCommManager
{
    public static final ThreadPoolExecutor thread_pool = Dispatcher.newThreadPool("LanCommTask");
    
    /**
     * 获取广播器
     *
     * @return Broadcaster
     */
    public static Broadcaster getBroadcaster()
    {
        return BroadcasterImpl.getImpl();
    }
    
    /**
     * 获取接收器
     *
     * @return
     */
    public static Receiver getReceiver()
    {
        return ReceiverImpl.getImpl();
    }
    
    /**
     * 获取搜索器
     *
     * @return Searcher
     */
    public static Searcher getSearcher()
    {
        return SearcherImpl.getImpl();
    }
    
    /**
     * 获取点对点通讯器
     *
     * @return
     */
    public static Communicator getCommunicator()
    {
        return CommunicatorImpl.getImpl();
    }
    
}
