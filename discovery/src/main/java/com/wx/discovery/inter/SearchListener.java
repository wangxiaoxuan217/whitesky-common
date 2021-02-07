package com.wx.discovery.inter;

import android.util.ArrayMap;

import com.wx.discovery.data.Device;


public interface SearchListener
{
    /**
     * 开始搜索
     */
    void onSearchStart();
    
    /**
     * 发现新设备
     *
     * @param device
     */
    void onSearchedNewOne(Device device);
    
    /**
     * 搜索结束
     */
    void onSearchFinish(ArrayMap<String, Device> devices);


}
