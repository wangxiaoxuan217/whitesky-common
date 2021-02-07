package com.wx.discovery.inter;

import com.wx.discovery.data.Device;

public interface DeviceListener
{
    void onDeviceAdd(Device device);
    
    void onDeviceRemove(Device device);
    
}
