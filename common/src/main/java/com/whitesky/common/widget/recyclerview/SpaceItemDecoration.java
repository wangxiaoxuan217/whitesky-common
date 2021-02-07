package com.whitesky.common.widget.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration
{
    private int space;
    
    private boolean isLeft;
    
    public SpaceItemDecoration(int space, boolean isLeft)
    {
        this.space = space;
        this.isLeft = isLeft;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        if (isLeft)
        {
            if (parent.getChildAdapterPosition(view) > 0)
            {
                // 从第二个条目开始，距离左方Item的距离
                outRect.left = space;
            }
        }
        else
        {
            outRect.right = space;
            outRect.top = space;
        }
        
    }
}
