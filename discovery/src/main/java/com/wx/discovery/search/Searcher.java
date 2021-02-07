package com.wx.discovery.search;

import com.wx.discovery.inter.SearchListener;

public interface Searcher
{
    /**
     * 开始搜索设备
     *
     * @param searchListener
     */
    void startSearch(SearchListener searchListener);
    
    /**
     * 设置能否被其他设备搜索到
     *
     * @param canBeSearched
     */
    void setCanBeSearched(boolean canBeSearched);
    
    /**
     * 是否能被其他设备搜索到
     *
     * @return
     */
    boolean isCanBeSearched();
    
}
