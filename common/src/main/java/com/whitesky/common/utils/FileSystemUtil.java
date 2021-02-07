package com.whitesky.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xiaoxuan 2020-05-26
 */
public class FileSystemUtil
{
    private static Boolean isExternalStorageWriteable()
    {
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED.equals(state) && Environment.getExternalStorageDirectory().canWrite())
        {// 已经插入了sd卡，并且可以读写
            mExternalStorageWriteable = true;
        }
        
        return mExternalStorageWriteable;
    }
    
    public static String getCurrentAvailableStorageDir()
        throws IOException
    {
        if (isExternalStorageWriteable())
        {
            return Environment.getExternalStorageDirectory().getCanonicalPath();
        }
        else
        {
            return Environment.getDataDirectory().getCanonicalPath();
        }
    }
    
    public static long getAvailableStore()
    {
        
        String filePath = "/data";
        
        try
        {
            filePath = getCurrentAvailableStorageDir();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        // 取得sdcard文件路径
        StatFs statFs = new StatFs(filePath);
        
        // 获取block的SIZE
        long blocSize = statFs.getBlockSize();
        
        // 获取BLOCK数量
        // long totalBlocks = statFs.getBlockCount();
        
        // 可使用的Block的数量
        long availaBlock = statFs.getAvailableBlocks();
        
        // long total = totalBlocks * blocSize;
        long availableSpare = availaBlock * blocSize;
        
        return availableSpare;
    }
    
    /**
     * 删除文件夹所有内容
     */
    public static void deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            File[] files = dir.listFiles();
            for (File f : files)
            {
                deleteDir(f);
            }
            dir.delete();
        }
        else
            dir.delete();
    }
    
    public static String getFileNameWithSuffix(String pathandname)
    {
        int start = pathandname.lastIndexOf("/");
        if (start != -1)
        {
            return pathandname.substring(start + 1);
        }
        else
        {
            return null;
        }
    }
    
    public static String getFileName(String pathandname)
    {
        if (TextUtils.isEmpty(pathandname))
        {
            return null;
        }
        int start = pathandname.lastIndexOf(".");
        if (start != -1)
        {
            return pathandname.substring(0, start);
        }
        else
        {
            return null;
        }
    }
    
    public static boolean isGrantExternalRW(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity
            .checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            
            activity.requestPermissions(
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
            
            return false;
        }
        
        return true;
    }
    
    public static String composeLocation(Context context, String fileName)
    {
        String dataLocation = "/data/data/" + context.getPackageName() + "/";
        return new StringBuilder().append(dataLocation).append(fileName).toString();
    }
    
    public static void copyAssetsToSD(Context context, String fileName, File strOutFile)
        throws IOException
    {
        // FileOutputStream传入必须是file,不能是String
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFile);
        myInput = context.getAssets().open(fileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
    
    /**
     * 换算文件的大小
     */
    public static String formetFileSize(long fileSize)
    {// 转换文件大小
        if (fileSize <= 0)
        {
            return "0M";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024)
        {
            fileSizeString = df.format((double)fileSize) + "B";
        }
        else if (fileSize < 1048576)
        {
            fileSizeString = df.format((double)fileSize / 1024) + "K";
        }
        else if (fileSize < 1073741824)
        {
            fileSizeString = df.format((double)fileSize / 1048576) + "M";
        }
        else
        {
            fileSizeString = df.format((double)fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }
    
    public static long getExternalTotalSize(String path)
    {
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSizeLong();
        long totalSize = statFs.getBlockCountLong();
        long total = 0l;
        total = blockSize * totalSize;
        return total;
    }
    
    public static long getAvailableSize(String path)
    {
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSizeLong();
        long availableSize = statFs.getAvailableBlocksLong();
        long total = 0l;
        total = blockSize * availableSize;
        return total;
    }
    
    public static List<Integer> getRa0Path()
    {
        List<Integer> filesIndex = new ArrayList<>();
        File sceneFile = new File("/storage/");
        File[] files = sceneFile.listFiles();
        if (null != files)
        {
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isDirectory() && files[i].getName().startsWith("ra0_sda"))
                {
                    filesIndex.add(Integer.parseInt(files[i].getName().replace("ra0_sda", "")));
                }
            }
        }
        if (filesIndex.size() > 0)
        {
            Collections.sort(filesIndex, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer lhs, Integer rhs)
                {
                    if (lhs > rhs)
                    {
                        return 1;
                    }
                    else
                    {
                        return -1;
                    }
                }
            });
        }
        return filesIndex;
    }
    
    public static void downloadFileByIO(String url, File saveFile, String fileName)
    {
        BufferedOutputStream bos = null;
        InputStream is = null;
        try
        {
            byte[] buff = new byte[8192];
            is = new URL(url).openStream();
            
            if (!saveFile.exists())
            {
                saveFile.mkdir();
            }
            File file = new File(saveFile + File.separator + fileName);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int count = 0;
            while ((count = is.read(buff)) != -1)
            {
                bos.write(buff, 0, count);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String getPreviewVideoFile(String[] fileList)
    {
        String filePath = "";
        if (fileList.length > 0)
        {
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].endsWith(".mpeg") || fileList[i].endsWith(".mkv") || fileList[i].endsWith(".mp4")
                    || fileList[i].endsWith(".vcf6") || fileList[i].endsWith(".mvh5") || fileList[i].endsWith(".mp36")
                    || fileList[i].endsWith(".ck6") || fileList[i].endsWith(".vh5"))
                {
                    filePath = fileList[i];
                    break;
                }
            }
        }
        return filePath;
    }
    
    public static String getPreviewAudioFile(String[] fileList)
    {
        String filePath = "";
        if (fileList.length > 0)
        {
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].endsWith("-v.wav"))
                {
                    filePath = fileList[i];
                    break;
                }
            }
        }
        return filePath;
    }
    
    public static String getPreviewPicFile(String[] fileList)
    {
        String filePath = "";
        if (fileList.length > 0)
        {
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].endsWith(".jpg") || fileList[i].endsWith(".png"))
                {
                    filePath = fileList[i];
                    break;
                }
            }
        }
        return filePath;
    }
}
