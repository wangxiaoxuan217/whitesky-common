package com.whitesky.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.whitesky.common.R;

import java.util.concurrent.Semaphore;

/**
 * 用于自定义一个Toast
 */
public class WindowHeadToast
{
    private final LinearLayout mWindowLayout;
    
    private final WindowManager mWindowManager;
    
    private final static int ANIM_DURATION = 600;
    
    private final static int ANIM_DISMISS = 0x01;
    
    private final static int SEARCH_START = 0x01;
    
    private final static int SEARCH_END = 0x02;
    
    private static int SEARCH_STATE = SEARCH_END;
    
    private final WindowManager.LayoutParams mWindowParams;

    private final TextView mTvInfo;

    private Semaphore semaphore;

    private final Handler mHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            switch (message.what)
            {
                case ANIM_DISMISS:
                    animDismiss();
                    break;
            }
            return false;
        }
    });

    public WindowHeadToast(Context context)
    {
        // 准备Window要添加的View
        mWindowLayout = new LinearLayout(context);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mWindowLayout.setLayoutParams(layoutParams);
        View headToastView = View.inflate(context, R.layout.header_toast, null);
        mTvInfo = headToastView.findViewById(R.id.tv_info);
        mWindowLayout.addView(headToastView);
        // 定义WindowManager 并且将View添加到WindowManagar中去
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.type =
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        mWindowParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mWindowParams.gravity = Gravity.CENTER | Gravity.TOP; // 显示在屏幕左上角
        // 显示位置与指定位置的相对位置差
        mWindowParams.x = 0;
        mWindowParams.y = 180;
        mWindowParams.width = 800;
        mWindowParams.height = 200;
        mWindowParams.format = -3; // 会影响Toast中的布局消失的时候父控件和子控件消失的时机不一致，比如设置为-1之后就会不同步
        mWindowParams.alpha = 1f;
        mWindowLayout.setVisibility(View.INVISIBLE);
        mWindowManager.addView(mWindowLayout, mWindowParams);
        mHandler.sendEmptyMessage(ANIM_DISMISS);
    }

    public void forceRemoveToast(){
        mHandler.removeMessages(ANIM_DISMISS);
        mHandler.sendEmptyMessageDelayed(ANIM_DISMISS,2000);
    }
    
    public void showCustomToast()
    {
        mHandler.removeMessages(ANIM_DISMISS);

        if(mWindowLayout!=null){
            mTvInfo.setText("");
            if(SEARCH_STATE == SEARCH_END){
                setHeadToastViewAnim();
            }
        }

        SEARCH_STATE = SEARCH_START;

        if (mWindowManager != null && SEARCH_STATE == SEARCH_END)
        {


        }
    }
    
    private void setHeadToastViewAnim()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mWindowLayout, "translationY", -700, 0);
        animator.setDuration(ANIM_DURATION);
        animator.start();
    }
    
    private void animDismiss()
    {
        if (mWindowLayout == null || mWindowLayout.getParent() == null)
        {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(mWindowLayout, "translationY", 0, -700);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        SEARCH_STATE = SEARCH_END;
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationCancel(Animator animation)
            {
                super.onAnimationCancel(animation);
            }
            
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                mWindowLayout.setVisibility(View.VISIBLE);
                //dismiss();
            }
            
            @Override
            public void onAnimationRepeat(Animator animation)
            {
                super.onAnimationRepeat(animation);
            }
            
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
            }
            
            @Override
            public void onAnimationPause(Animator animation)
            {
                super.onAnimationPause(animation);
            }
            
            @Override
            public void onAnimationResume(Animator animation)
            {
                super.onAnimationResume(animation);
            }
        });
    }
    
    /**
     * 移除HeaderToast (一定要在动画结束的时候移除,不然下次进来的时候由于wm里边已经有控件了，所以会导致卡死)
     */
    private void dismiss()
    {
        if (null != mWindowLayout && null != mWindowLayout.getParent())
        {
            mWindowManager.removeView(mWindowLayout);
        }
    }
    
    public void showInfo(String text)
    {
        mTvInfo.setText(text);
        mHandler.sendEmptyMessageDelayed(ANIM_DISMISS,2000);
        System.out.println("showInfo" + text);
    }
}
