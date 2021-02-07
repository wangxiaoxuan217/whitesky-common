package com.whitesky.common.widget.gesture;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

public class GestureListener extends GestureDetector.SimpleOnGestureListener
{
    private final WeakReference<IGestureListener> listener;
    
    public GestureListener(IGestureListener listener, Context context)
    {
        this.listener = new WeakReference<>(listener);
    }
    
    // 用户按下屏幕就会触发
    public boolean onDown(MotionEvent e)
    {
        Log.i("MyGesture", "onDown");
        return true;
    }
    
    // 如果是按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，那么onShowPress就会执行
    public void onShowPress(MotionEvent e)
    {
        Log.i("MyGesture", "onShowPress");
    }
    
    // 单击，只要手抬起就会执行
    public boolean onSingleTapUp(MotionEvent e)
    {
        Log.i("MyGesture", "onSingleTapUp");
        return true;
    }
    
    // 在屏幕上拖动事件
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        Log.i("MyGesture", "onScroll");
        return true;
    }
    
    // 长按触摸屏
    public void onLongPress(MotionEvent e)
    {
        Log.i("MyGesture", "onLongPress");
    }
    
    public boolean onSingleTapConfirmed(MotionEvent e)
    {
        if (listener.get() != null)
        {
            listener.get().onGestureSingleClick(e);
        }
        // 单击事件，在双击事件发生时不会产生这个事件，所以用这个回调作为播放器单击事件
        return super.onSingleTapConfirmed(e);
    }
    
    public boolean onDoubleTap(MotionEvent e)
    {
        if (listener.get() != null)
        {
            listener.get().onGestureDoubleClick(e);
        }
        // 双击事件
        return super.onDoubleTap(e);
    }
    
    public boolean onDoubleTapEvent(MotionEvent e)
    {
        Log.i("MyGesture", "onDoubleTapEvent");
        return true;
    }
    
}
