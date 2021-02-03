package com.whitesky.common.utils;



import com.wsd.android.WsdSerialnum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Locale;

public class Utils
{
    // 系统版本号SN号
    public static String getSnInfo()
    {
        String strDevice = new String(WsdSerialnum.read());
        return strDevice.toUpperCase();
    }
    
    // 有线网MAC地址
    public static String getMacAddress()
    {
        try
        {
            return loadFileAsString("/sys/class/net/eth0/address").toUpperCase(Locale.ENGLISH)
                .substring(0, 17)
                .replaceAll(":", "");
        }
        catch (IOException e)
        {
            return null;
        }
    }
    
    private static String loadFileAsString(String filePath)
        throws IOException
    {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
    
    /**
     * 获取SDK版本
     */
    public static int getSDKVersion()
    {
        int version = 0;
        try
        {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        }
        catch (NumberFormatException e)
        {
        }
        return version;
    }
    
    public static String getSha1(String sourceStr)
    {
        if (sourceStr == null || sourceStr.length() == 0)
        {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        
        try
        {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(sourceStr.getBytes("UTF-8"));
            
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    public static String getMd5(String sourceStr)
    {
        String result = "";
        if (sourceStr == null || sourceStr.length() == 0)
        {
            return null;
        }
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++)
            {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println(e);
        }
        return result;
    }
    
    // 获取IP地址
    public static String getIP()
    {
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements())
            {
                NetworkInterface info = (NetworkInterface)en.nextElement();
                if (info.getName().toLowerCase().equals("eth0") || info.getName().toLowerCase().equals("wlan0"))
                {
                    Enumeration<InetAddress> enumIpAddr = info.getInetAddresses();
                    while (enumIpAddr.hasMoreElements())
                    {
                        InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress())
                        {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getJsonFromSD()
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            File file = new File("/sdcard/EvideoDesktopFormat.json");
            InputStream in = null;
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1)
            {
                sb.append((char)tempbyte);
            }
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
}