package com.whitesky.common.widget.gesture;


import com.whitesky.common.base.BaseView;

public interface IGestureLayerListener extends BaseView
{
    void onOneFlingLeft(float y, float xDelta);
    
    void onOneFlingRight(float y, float xDelta);
    
    void onOneFlingUp(float y, float yDelta);
    
    void onOneFlingDown(float y, float yDelta);
    
    void onThreeFlingLeft();
    
    void onThreeFlingRight();
}
