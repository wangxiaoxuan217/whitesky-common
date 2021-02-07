package com.whitesky.common.widget.window;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by xiaoxuan 2020.08.06
 */
public class PopupController
{
    private View mPopupView;// 弹窗布局View
    
    private int mLayoutResId;// 布局id
    
    private final Context mContext;
    
    private final PopupWindow mPopupWindow;
    
    private View mView;
    
    private Window mWindow;
    
    PopupController(Context context, PopupWindow popupWindow)
    {
        this.mContext = context;
        this.mPopupWindow = popupWindow;
    }
    
    public void setView(int mLayoutResId)
    {
        mView = null;
        this.mLayoutResId = mLayoutResId;
        installContent();
    }
    
    public void setView(View view)
    {
        mView = view;
        this.mLayoutResId = 0;
        installContent();
    }
    
    public View getPopupWindowView()
    {
        return mPopupView;
    }
    
    private void installContent()
    {
        if (mLayoutResId != 0)
        {
            mPopupView = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
        }
        else if (mView != null)
        {
            mPopupView = mView;
        }
        mPopupWindow.setContentView(mPopupView);
    }
    
    /**
     * 设置宽度
     *
     * @param width 宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height)
    {
        if (width == 0 || height == 0)
        {
            // 如果没设置宽高，默认是WRAP_CONTENT
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        else
        {
            mPopupWindow.setWidth(width);
            mPopupWindow.setHeight(height);
        }
    }
    
    /**
     * 设置背景灰色程度
     *
     * @param level 0.0f-1.0f
     */
    void setBackGroundLevel(float level)
    {
        mWindow = ((Activity)mContext).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }
    
    /**
     * 设置动画
     */
    private void setAnimationStyle(int animationStyle)
    {
        mPopupWindow.setAnimationStyle(animationStyle);
    }
    
    /**
     * 设置Outside是否可点击
     *
     * @param touchable 是否可点击
     */
    private void setOutsideTouchable(boolean touchable)
    {
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置透明背景
        mPopupWindow.setOutsideTouchable(touchable);// 设置outside可点击
        mPopupWindow.setFocusable(touchable);
    }
    
    static class PopupParams
    {
        public int mLayoutResId;// 布局id
        
        public Context mContext;
        
        public int mWidth, mHeight;// 弹窗的宽和高
        
        public boolean isShowBg, isShowAnim;
        
        public float mBgLevel;// 屏幕背景灰色程度
        
        public int mAnimationStyle;// 动画Id
        
        public View mView;
        
        public boolean isTouchable = true;
        
        public long mTimerMillis;
        
        public PopupParams(Context mContext, long mTimerMillis)
        {
            this.mContext = mContext;
            this.mTimerMillis = mTimerMillis;
        }
        
        public void apply(PopupController controller)
        {
            if (mView != null)
            {
                controller.setView(mView);
            }
            else if (mLayoutResId != 0)
            {
                controller.setView(mLayoutResId);
            }
            else
            {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }
            controller.setWidthAndHeight(mWidth, mHeight);
            controller.setOutsideTouchable(isTouchable);// 设置outside可点击
            if (isShowBg)
            {
                // 设置背景
                controller.setBackGroundLevel(mBgLevel);
            }
            if (isShowAnim)
            {
                controller.setAnimationStyle(mAnimationStyle);
            }
        }
    }
}
