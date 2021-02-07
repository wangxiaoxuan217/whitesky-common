package com.whitesky.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;

@SuppressLint("AppCompatCustomView")
public class TvTimerView extends TextView
{
    private CountDownTimerSupport mMenuShowTimer;
    
    private OnTimerFinishListener mOnTimerFinish;
    
    public TvTimerView(Context context)
    {
        super(context);
    }
    
    public TvTimerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public void setOnTimerFinish(OnTimerFinishListener onTimerFinish)
    {
        this.mOnTimerFinish = onTimerFinish;
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
                if (getVisibility() == VISIBLE)
                {
                    setVisibility(View.GONE);
                }
                else
                {
                    setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onFinish()
            {
                // 计时器停止
                if (mMenuShowTimer != null)
                {
                    mMenuShowTimer.stop();
                    mMenuShowTimer = null;
                }
                mOnTimerFinish.onTimerFinish();
            }
        });
    }
    
    public void updateTimer()
    {
        if (mMenuShowTimer != null)
        {
            mMenuShowTimer.reset();
        }
        mMenuShowTimer.start();
    }
    
    public interface OnTimerFinishListener
    {
        void onTimerFinish();
    }
}
