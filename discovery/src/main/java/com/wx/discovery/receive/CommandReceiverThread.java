package com.wx.discovery.receive;

import android.app.Service;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.wx.discovery.common.ContextVal;
import com.wx.discovery.common.ConvertUtils;
import com.wx.discovery.data.CommData;
import com.wx.discovery.data.Const;
import com.wx.discovery.data.Device;
import com.wx.discovery.inter.DataListener;
import com.wx.discovery.utils.Utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class CommandReceiverThread extends Thread
{
    
    private static final String TAG = "CommandReceiverThread";
    
    private static CommandReceiverThread receiverThread;
    
    private String tarIp = "";
    
    /**
     * 启动接收线程
     */
    public static void open()
    {
        if (receiverThread == null)
        {
            receiverThread = new CommandReceiverThread();
            receiverThread.start();
        }
    }
    
    /**
     * 关闭接收线程
     */
    public static void close()
    {
        if (receiverThread != null)
        {
            receiverThread.destory();
            receiverThread = null;
        }
    }
    
    public void destory()
    {
        releaseLock();
    }
    
    volatile boolean openFlag;
    
    @Override
    public void run()
    {
        wakeLock();
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(Const.COMMAND_PORT);
            openFlag = true;
            while (openFlag)
            {
                Socket socket = serverSocket.accept();
                DataInputStream is = new DataInputStream(socket.getInputStream());
                OutputStream os = socket.getOutputStream();
                byte[] buffer = new byte[1024];
                int len = is.read(buffer);
                if (len != -1)
                {
                    if (verifyCommandRequest(buffer))
                    {
                        // 发送收到响应
                        os.write(packCommandRspData());
                        // 解析点对点消息内容
                        parseCommandRequest(buffer);
                    }
                }
                else
                {
                    
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    private byte[] packCommandRspData()
        throws SocketException
    {
        byte[] localIPAddress = Utils.getLocalIPAddress();
        byte[] data = new byte[2 + localIPAddress.length];
        data[0] = Const.PACKET_PREFIX;
        data[1] = Const.PACKET_TYPE_COMMAND_RSP;
        System.arraycopy(localIPAddress, 0, data, 2, localIPAddress.length);
        Log.v(TAG, Utils.getDeviceIp() + Const.SEND_SYMBOL + tarIp + " 点对点消息 通知已收到，data:\r\n" + Arrays.toString(data));
        return data;
    }
    
    private boolean verifyCommandRequest(byte[] data)
        throws UnknownHostException
    {
        if (data[0] != Const.PACKET_PREFIX)
        {
            return false;
        }
        
        if (data[1] != Const.PACKET_TYPE_COMMAND)
        {
            return false;
        }
        
        byte[] ipData = new byte[4];
        System.arraycopy(data, 2, ipData, 0, 4);
        tarIp = Utils.ipbyteToString(ipData);
        Log.v(TAG, Utils.getDeviceIp() + Const.RECEIVE_SYMBOL + tarIp + " 点对点消息,data:\r\n" + Arrays.toString(data));
        return true;
    }
    
    private void parseCommandRequest(byte[] data)
        throws UnknownHostException
    {
        if (data[0] != Const.PACKET_PREFIX)
        {
            return;
        }
        
        if (data[1] != Const.PACKET_TYPE_COMMAND)
        {
            return;
        }
        
        byte[] ipData = new byte[4];
        System.arraycopy(data, 2, ipData, 0, 4);
        String ip = Utils.ipbyteToString(ipData);
        byte[] lengthData = new byte[4];
        System.arraycopy(data, 6, lengthData, 0, 4);
        int dataLength = ConvertUtils.byteArrayToInt(lengthData);
        byte[] msgData = new byte[dataLength];
        System.arraycopy(data, 10, msgData, 0, dataLength);
        
        CommData commData = new CommData();
        Device device = new Device(ip);
        commData.setDevice(device);
        commData.setData(msgData);
        
        List<DataListener> receivers = ReceiverImpl.getImpl().getReceivers();
        for (DataListener receiver : receivers)
        {
            receiver.onCommandArrive(commData);
        }
    }
    
    private WakeLock wakelock;
    
    private void wakeLock()
    {
        PowerManager pm = (PowerManager)ContextVal.getContext().getSystemService(Service.POWER_SERVICE);
        wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        wakelock.acquire();
    }
    
    private void releaseLock()
    {
        if (wakelock != null && wakelock.isHeld())
        {
            wakelock.release();
        }
    }
}
