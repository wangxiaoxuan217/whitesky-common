package com.whitesky.common.widget.pagelayout;

/**
 * @author xiaoxuan 2020-05-26 用于Log的显示与关闭
 */
public class PagerConfig
{
    private static int sFlingThreshold = 1000; // Fling 阀值，滚动速度超过该阀值才会触发滚动
    
    private static float sMillisecondsPreInch = 60f; // 每一个英寸滚动需要的微秒数，数值越大，速度越慢
    
    /**
     * 获取当前滚动速度阀值
     *
     * @return 当前滚动速度阀值
     */
    public static int getFlingThreshold()
    {
        return sFlingThreshold;
    }
    
    /**
     * 设置当前滚动速度阀值
     *
     * @param flingThreshold 滚动速度阀值
     */
    public static void setFlingThreshold(int flingThreshold)
    {
        sFlingThreshold = flingThreshold;
    }
    
    /**
     * 获取滚动速度 英寸/微秒
     *
     * @return 英寸滚动速度
     */
    public static float getMillisecondsPreInch()
    {
        return sMillisecondsPreInch;
    }
    
}
