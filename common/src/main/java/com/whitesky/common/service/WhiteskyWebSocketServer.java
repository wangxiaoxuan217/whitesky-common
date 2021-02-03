package com.whitesky.common.service;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class WhiteskyWebSocketServer extends WebSocketServer
{
    public WebSocket mClientSession = null;
    
    public WhiteskyWebSocketServer(int port)
    {
        super(new InetSocketAddress(port));
    }
    
    @Override
    public void start()
    {
        super.start();
    }
    
    @Override
    public void stop()
    {
        try
        {
            super.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onOpen(WebSocket conn, ClientHandshake arg1)
    {
        // TODO Auto-generated method stub
        // log.d(TAG, "Server onOpen getHostAddress:" + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        mClientSession = conn;
    }
    
    @Override
    public void onMessage(WebSocket conn, String arg1)
    {
        Log.e(this.toString(), "onMessage  String");
        // TODO Auto-generated method stub
        // log.d(TAG, "Server onMessage getHostAddress:" + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        // log.d(TAG, "Server onMessage msg:" + arg1);
    }
    
    @Override
    public void onMessage(WebSocket conn, ByteBuffer message)
    {
        Log.e(this.toString(), "onMessage  ByteBuffer");
    }
    
    @Override
    public void onClose(WebSocket conn, int arg1, String arg2, boolean arg3)
    {
        // TODO Auto-generated method stub
        // log.d(TAG, "Server onClose getHostAddress:" + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        mClientSession = null;
    }
    
    @Override
    public void onError(WebSocket conn, Exception arg1)
    {
        // TODO Auto-generated method stub
        // log.d(TAG, "Server client onError:" + arg1);
        mClientSession = null;
    }
    
    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        // log.d(TAG, "Server client onStart");
    }
}
