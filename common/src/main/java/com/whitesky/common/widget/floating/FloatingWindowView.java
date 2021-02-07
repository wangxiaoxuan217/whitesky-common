package com.whitesky.common.widget.floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class FloatingWindowView extends FrameLayout
{
    private static final String TAG = "FloatingWindowView";
    
    private int mDragStartX;
    
    private int mDragStartY;
    
    private int mXPos;
    
    private int mYPos;
    
    private int mWidth;
    
    private int mHeight;
    
    private double zoomRadio = 1;
    
    private Context mContext;
    
    public FloatingWindowView(Context c)
    {
        super(c);
        mContext = c;
    }
    
    public FloatingWindowView(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        mContext = c;
    }
    
    public FloatingWindowView(Context c, AttributeSet attrs, int defStyleAttr)
    {
        super(c, attrs, defStyleAttr);
        mContext = c;
    }
    
    public FloatingWindowView(Context c, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(c, attrs, defStyleAttr, defStyleRes);
        mContext = c;
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        final int x = (int)event.getRawX();
        final int y = (int)event.getRawY();
        
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
            {
                WindowManager.LayoutParams lParams = (WindowManager.LayoutParams)getLayoutParams();
                mXPos = lParams.x;
                mYPos = lParams.y;
                mWidth = lParams.width;
                mHeight = lParams.height;
                mDragStartX = x;
                mDragStartY = y;
                Log.d(TAG, "view pos " + mXPos + " " + mYPos + " - " + mDragStartX + " " + mDragStartY);
            }
                break;
            case MotionEvent.ACTION_MOVE:
            {
                int dragX = x - mDragStartX;
                int dragY = y - mDragStartY;
                
                int newPosX = mXPos + dragX;
                int newPosY = mYPos + dragY;
                int flag = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                
                // update layout
                WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams(mWidth, // WindowManager.LayoutParams.MATCH_PARENT,
                    mHeight, // WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, flag, PixelFormat.TRANSLUCENT);
                wmParams.x = newPosX;
                wmParams.y = newPosY;
                wmParams.gravity = Gravity.TOP | Gravity.LEFT;
                WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
                wm.updateViewLayout(this, wmParams);
            }
                break;
            default:
                break;
        }
        return true;
    }
    
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        // The input source is a pointing device associated with a display.
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER))
        {
            switch (event.getAction())
            {
                // process the scroll wheel movement..
                case MotionEvent.ACTION_SCROLL:
                    // Log.i(TAG,"action fortest::onGenericMotionEvent:"+event.getAxisValue(MotionEvent.AXIS_VSCROLL));
                    if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f)
                    {
                        zoomRadio = zoomRadio - 0.1;
                    }
                    else
                    {
                        zoomRadio = zoomRadio + 0.1;
                    }
                    WindowManager.LayoutParams lParams = (WindowManager.LayoutParams)getLayoutParams();
                    mXPos = lParams.x;
                    mYPos = lParams.y;
                    mWidth = lParams.width;
                    mHeight = lParams.height;
                    int newWidth = (int)(mWidth * zoomRadio);
                    int newHeight = (int)(mHeight * zoomRadio);
                    int flag = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams(newWidth, // WindowManager.LayoutParams.MATCH_PARENT,
                        newHeight, // WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, // WindowManager.LayoutParams .TYPE_TOAST,
                        flag, PixelFormat.TRANSLUCENT);
                    
                    wmParams.x = mXPos;
                    wmParams.y = mYPos;
                    wmParams.gravity = Gravity.TOP | Gravity.LEFT;
                    WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
                    wm.updateViewLayout(this, wmParams);
                    zoomRadio = 1;
                    return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }
}
