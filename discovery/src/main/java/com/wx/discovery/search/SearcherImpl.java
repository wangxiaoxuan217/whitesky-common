package com.wx.discovery.search;

import com.wx.discovery.LanCommManager;
import com.wx.discovery.broadcast.BroadcastRunnable;
import com.wx.discovery.data.Const;
import com.wx.discovery.inter.SearchListener;

public class SearcherImpl implements Searcher
{
    private static final String TAG = "SearcherImpl";
    
    private boolean canBeSearched = false;
    
    private SearcherImpl()
    {
    }
    
    public static SearcherImpl getImpl()
    {
        return SearcherImplHolder.sInstance;
    }
    
    private static class SearcherImplHolder
    {
        private static final SearcherImpl sInstance = new SearcherImpl();
    }
    
    @Override
    public void startSearch(SearchListener searchListener)
    {
        LanCommManager.thread_pool.execute(new SearchRunnable(searchListener));
    }
    
    @Override
    public void setCanBeSearched(boolean canBeSearched)
    {
        if (this.canBeSearched != canBeSearched)
        {
            if (canBeSearched)
            {
                // 设备上线->广播通知其他设备
                LanCommManager.thread_pool.execute(new BroadcastRunnable().setType(Const.PACKET_TYPE_DEVICE_ADD));
            }
            else
            {
                // 设备下线->广播通知其他设备
                LanCommManager.thread_pool.execute(new BroadcastRunnable().setType(Const.PACKET_TYPE_DEVICE_REMOVE));
            }
        }
        this.canBeSearched = canBeSearched;
        if (canBeSearched)
        {
            SearchRspThread.open();
        }
        else
        {
            SearchRspThread.close();
        }
    }
    
    @Override
    public boolean isCanBeSearched()
    {
        return canBeSearched;
    }
}
