package com.wx.discovery.broadcast;

import android.util.Log;

import com.wx.discovery.common.ConvertUtils;
import com.wx.discovery.data.Const;
import com.wx.discovery.data.LanCommConfig;
import com.wx.discovery.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class BroadcastRunnable implements Runnable
{
    
    private static final String TAG = "BroadcastRunnable";
    
    private byte[] bytes = new byte[0];
    
    private int type = Const.PACKET_TYPE_BROADCAST;
    
    /**
     * 设置广播的消息内容
     *
     * @param bytes
     * @return
     */
    public BroadcastRunnable setData(byte[] bytes)
    {
        this.bytes = bytes;
        return this;
    }
    
    /**
     * 设置广播包类型
     *
     * @param type
     * @return
     */
    public BroadcastRunnable setType(int type)
    {
        this.type = type;
        return this;
    }
    
    /**
     * 协议规范 #(1) + packageType(1) + ip(4) + dataLength(4) + [data]
     *
     * @param bytes
     * @param hostAddress
     * @return
     */
    private byte[] packBroadcastData(byte[] hostAddress, byte[] bytes)
    {
        byte[] lengthBytes = ConvertUtils.intToByteArray(bytes.length);
        byte[] data = new byte[2 + hostAddress.length + bytes.length + lengthBytes.length];
        data[0] = Const.PACKET_PREFIX;
        data[1] = (byte)type;
        // Trace.d(TAG, "packBroadcastData() : " + Arrays.toString(lengthBytes));
        System.arraycopy(hostAddress, 0, data, 2, hostAddress.length);
        System.arraycopy(lengthBytes, 0, data, 2 + hostAddress.length, lengthBytes.length);
        if (bytes != null && bytes.length != 0)
        {
            System.arraycopy(bytes, 0, data, 2 + hostAddress.length + lengthBytes.length, bytes.length);
        }
        Log.v(TAG, Utils.getDeviceIp() + Const.SEND_SYMBOL + "局域网广播 package data:\r\n" + Arrays.toString(data));
        return data;
    }
    
    @Override
    public void run()
    {
        try
        {
            Log.v(TAG, Utils.getDeviceIp() + Const.SEND_SYMBOL + "局域网广播");
            DatagramSocket socket = new DatagramSocket();
            // 设置接收等待时长
            socket.setSoTimeout(LanCommConfig.RECEIVE_TIME_OUT);
            byte[] sendData = new byte[1024];
            // 使用广播形式（目标地址设为255.255.255.255）的udp数据包
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                InetAddress.getByName("255.255.255.255"), Const.DEVICE_BROADCAST_PORT);
            byte[] hostAddress = Utils.getLocalIPAddress();
            byte[] broadcastData = packBroadcastData(hostAddress, this.bytes);
            sendPacket.setData(broadcastData);
            // 发送udp数据包
            socket.send(sendPacket);
            Log.v(TAG, Utils.getDeviceIp() + Const.SEND_SYMBOL + "局域网广播 成功");
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}