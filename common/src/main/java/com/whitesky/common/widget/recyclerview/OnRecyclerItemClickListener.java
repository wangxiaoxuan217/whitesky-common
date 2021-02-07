package com.whitesky.common.widget.recyclerview;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener
{
    private GestureDetectorCompat mGestureDetectorCompat;
    
    private RecyclerView mRecyclerView;
    
    public OnRecyclerItemClickListener(RecyclerView recyclerView)
    {
        mRecyclerView = recyclerView;
        mGestureDetectorCompat =
            new GestureDetectorCompat(mRecyclerView.getContext(), new ItemTouchHelperGestureListener());
    }
    
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }
    
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e)
    {
        mGestureDetectorCompat.onTouchEvent(e);
    }
    
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {
        
    }
    
    public abstract void onLongClick(RecyclerView.ViewHolder viewHolder);
    
    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public void onLongPress(MotionEvent e)
        {
            View childViewUnder = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childViewUnder != null)
            {
                RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(childViewUnder);
                onLongClick(childViewHolder);
            }
        }
    }
}
