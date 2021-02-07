package com.wx.discovery.search;

import android.util.ArrayMap;
import android.util.Log;

import com.wx.discovery.data.Const;
import com.wx.discovery.data.Device;
import com.wx.discovery.inter.SearchListener;
import com.wx.discovery.utils.UIThreadUtil;
import com.wx.discovery.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class SearchRunnable implements Runnable
{
    private static final String TAG = "SearchRunnable";
    
    private SearchListener listener;
    
    public SearchRunnable(SearchListener listener)
    {
        this.listener = listener;
    }
    
    @Override
    public void run()
    {
        Log.v(TAG, Utils.getDeviceIp() + Const.SEND_SYMBOL + "局域网搜索设备");
        UIThreadUtil.postUI(new Runnable()
        {
            @Override
            public void run()
            {
                listener.onSearchStart();
            }
        });
        
        ArrayMap<String, Device> devices = new ArrayMap<>();
        try
        {
            DatagramSocket socket = new DatagramSocket();
            // 设置接收等待时长
            socket.setSoTimeout(Const.RECEIVE_TIME_OUT);
            byte[] sendData = new byte[1024];
            byte[] receData = new byte[1024];
            DatagramPacket recePack = new DatagramPacket(receData, receData.length);
            // 使用广播形式（目标地址设为255.255.255.255）的udp数据包
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                InetAddress.getByName("255.255.255.255"), Const.DEVICE_SEARCH_PORT);
            // 用于存放已经应答的设备
            byte[] localIPAddress = Utils.getLocalIPAddress();
            // 搜索指定次数
            sendPacket.setData(packSearchData(localIPAddress));
            // 发送udp数据包
            socket.send(sendPacket);
            try
            {
                // 限定搜索设备的最大数量
                int rspCount = Const.SEARCH_DEVICE_MAX;
                while (rspCount > 0)
                {
                    socket.receive(recePack);
                    final Device device = parseRespData(recePack);
                    
                    if (devices.get(device.getIp()) == null)
                    {
                        // 保存新应答的设备
                        devices.put(device.getIp(), device);
                        UIThreadUtil.postUI(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                listener.onSearchedNewOne(device);
                            }
                        });
                    }
                    rspCount--;
                }
            }
            catch (SocketTimeoutException e)
            {
            }
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        listener.onSearchFinish(devices);
    }
    
    private Device parseRespData(DatagramPacket recePack)
        throws SocketException
    {
        if (recePack.getLength() < 2)
        {
            return null;
        }
        byte[] data = recePack.getData();
        if (data[0] != Const.PACKET_PREFIX || data[1] != Const.PACKET_TYPE_SEARCH_DEVICE_RSP)
        {
            return null;
        }
        byte[] ip = new byte[4];
        System.arraycopy(data, 2, ip, 0, 4);
        // Trace.d(TAG, "search request respond ip: " + Arrays.toString(ip));
        // try {
        // Trace.d(TAG, "search request respond ip:" + Utils.ipbyteToString(ip));
        // } catch (UnknownHostException e) {
        // e.printStackTrace();
        // }
        String ip1 = recePack.getAddress().getHostAddress();
        Log.v(TAG, "发现新设备: ip:" + ip1);
        return new Device(ip1);
    }
    
    private byte[] packSearchData(byte[] hostAddress)
    {
        // Trace.d(TAG, "pack search data, ip ：" + Utils.ipbyteToString(hostAddress));
        byte[] data = new byte[2 + hostAddress.length];
        data[0] = Const.PACKET_PREFIX;
        data[1] = Const.PACKET_TYPE_SEARCH_DEVICE;
        System.arraycopy(hostAddress, 0, data, 2, hostAddress.length);
        Log.v(TAG, Utils.getDeviceIp() + Const.SEND_SYMBOL + "局域网搜索设备,package data: \r\n" + Arrays.toString(data));
        return data;
    }
}
