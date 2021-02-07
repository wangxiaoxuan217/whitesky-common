package com.whitesky.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;

/**
 * @author xiaoxuan 2020.05.07 封装控件，适用于投影仪的Item布局使用
 */
public class MainMenuView extends FrameLayout
{
    private CountDownTimerSupport mMenuShowTimer;
    
    private boolean mIsSwitch = true;
    
    public void setSwitch(boolean isSwitch)
    {
        this.mIsSwitch = isSwitch;
    }
    
    public MainMenuView(Context context)
    {
        super(context);
        if (mIsSwitch)
        {
            setTimerCount(15 * 1000);
        }
    }
    
    public MainMenuView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (mIsSwitch)
        {
            setTimerCount(15 * 1000);
        }
    }
    
    public void setTimerCount(long timerMillis)
    {
        mMenuShowTimer = new CountDownTimerSupport(timerMillis, 1000);
        mMenuShowTimer.setOnCountDownTimerListener(new OnCountDownTimerListener()
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                // 间隔回调
            }
            
            @Override
            public void onFinish()
            {
                // 计时器停止
                if (mIsSwitch)
                {
                    if (getVisibility() == VISIBLE)
                    {
                        setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        if (mIsSwitch)
        {
            if (visibility == VISIBLE)
            {
                mMenuShowTimer.start();
            }
            else
            {
                mMenuShowTimer.stop();
            }
        }
    }
    
    public void updateTimer()
    {
        if (mIsSwitch)
        {
            if (getVisibility() == VISIBLE)
            {
                mMenuShowTimer.reset();
                mMenuShowTimer.start();
            }
        }
    }
}
