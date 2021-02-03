package com.whitesky.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil
{
    public static boolean deleteFile(String filePath)
    {
        File file = new File(filePath);
        if (file.isFile() && file.exists())
        {
            return file.delete();
        }
        if (!file.exists())
        {
            return true;
        }
        return false;
    }
    
    /**
     * 复制文件
     * 
     * @param fromFile 要复制的文件目录
     * @param toFile 要粘贴的文件目录
     * @return 是否复制成功
     */
    public static boolean copy(String fromFile, String toFile)
    {
        // 要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        // 如同判断SD卡是否存在或者文件是否存在
        // 如果不存在则 return出去
        if (!root.exists())
        {
            return false;
        }
        // 如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
        // 目标目录
        File targetDir = new File(toFile);
        // 创建目录
        if (!targetDir.exists())
        {
            targetDir.mkdirs();
        }
        // 遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++)
        {
            if (currentFiles[i].isDirectory())
            {// 如果当前项为子目录 进行递归
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");
            }
            else
            {// 如果当前项为文件则进行文件拷贝
                copySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return true;
    }
    
    // 文件拷贝
    // 要复制的目录下的所有非子目录(文件夹)文件拷贝
    public static boolean copySdcardFile(String fromFile, String toFile)
    {
        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
    
    public static void writeJson(Object json)
    {
        BufferedWriter writer = null;
        File file = new File("/data/hotelconfig/update.json");
        // 如果文件不存在，则新建一个
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        // 写入
        try
        {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (writer != null)
                {
                    writer.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }
}
