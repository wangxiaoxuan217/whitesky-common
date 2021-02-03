package com.whitesky.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class TimeUtil
{
    public static String formatSeconds(long seconds)
    {
        String standardTime;
        if (seconds <= 0)
        {
            standardTime = "00:00";
        }
        else if (seconds < 60)
        {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60);
        }
        else if (seconds < 3600)
        {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        }
        else
        {
            standardTime =
                String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60);
        }
        return standardTime;
    }
    
    /**
     * 根据日期获取 星期 （2019-05-06 ——> 星期一）
     * 
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime)
    {
        
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try
        {
            date = f.parse(datetime);
            cal.setTime(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        // 一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    
    // 将长度转换为时间
    public static String stringForTime(long timeMs)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.getDefault());
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        stringBuilder.setLength(0);
        if (hours > 0)
        {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }
        else
        {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
