package com.whitesky.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class JsonUtil
{
    public static String getSongCategoryJson(Context context)
    {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try
        {
            is = context.getAssets().open("song_category.json");
            bos = new ByteArrayOutputStream();
            byte[] bytes = new byte[4 * 1024];
            int len;
            while ((len = is.read(bytes)) != -1)
            {
                bos.write(bytes, 0, len);
            }
            return new String(bos.toByteArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (is != null)
                    is.close();
                if (bos != null)
                    bos.close();
            }
            catch (IOException e)
            {
            }
        }
        return null;
    }
}
