package com.whitesky.common.widget.gesture;

import android.view.MotionEvent;

public interface IGestureListener
{
    void onGestureSingleClick(MotionEvent event);
    
    void onGestureDoubleClick(MotionEvent event);
}
