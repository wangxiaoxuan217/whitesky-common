package com.whitesky.common.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration
{
    private static final int DECORATION_IS_NON = 0;
    
    private static final int DECORATION_IS_LEFT = 1;
    
    private static final int DECORATION_IS_NOT_LEFT = 2;
    
    private static final int DECORATION_IS_lEFT_RIGHT = 3;
    
    private static final int DECORATION_IS_TOP_RIGHT = 4;
    
    private int mSpace, mStatus;
    
    public SpaceItemDecoration(int space, int status)
    {
        this.mSpace = space;
        this.mStatus = status;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        switch (mStatus)
        {
            case DECORATION_IS_NON:
                outRect.left = mSpace;
                outRect.bottom = mSpace;
                outRect.top = mSpace;
                outRect.right = mSpace;
                break;
            case DECORATION_IS_LEFT:
                if (parent.getChildAdapterPosition(view) > 0)
                {
                    // 从第二个条目开始，距离左方Item的距离
                    outRect.left = mSpace;
                }
                break;
            case DECORATION_IS_NOT_LEFT:
                outRect.right = mSpace;
                outRect.top = mSpace;
                break;
            case DECORATION_IS_lEFT_RIGHT:
                if (parent.getChildAdapterPosition(view) == 0)
                {
                    outRect.left = 112;
                    outRect.right = mSpace;
                }
                if (parent.getChildAdapterPosition(view) > 0)
                {
                    // 从第二个条目开始，距离左方Item的距离
                    outRect.right = mSpace;
                }
                break;
            case DECORATION_IS_TOP_RIGHT:
                outRect.right = mSpace * 4 + 5;
                outRect.top = mSpace;
                break;
            default:
                break;
        }
        
    }
}
