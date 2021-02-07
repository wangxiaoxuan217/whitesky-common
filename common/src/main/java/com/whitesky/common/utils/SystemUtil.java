package com.whitesky.common.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import com.wsd.android.WsdSerialnum;

public class SystemUtil
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
}
