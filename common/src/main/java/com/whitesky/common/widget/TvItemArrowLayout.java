package com.whitesky.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.whitesky.common.R;


/**
 * @author xiaoxuan 2020.05.07 封装控件，适用于投影仪的Item布局使用
 */
public class TvItemArrowLayout extends RelativeLayout
{
    private int position = 0;

    public TvItemArrowLayout(Context context)
    {
        super(context);
    }
    
    public TvItemArrowLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs)
    {
        this.setChildrenDrawingOrderEnabled(true);
        // 加载布局
        View view = LayoutInflater.from(context).inflate(R.layout.widget_layout_top, this);
        ImageView mIvTopSetup = (ImageView) view.findViewById(R.id.iv_top_setup);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TvItemArrowLayout);
        if (attributes != null)
        {
            boolean selectSetup = attributes.getBoolean(R.styleable.TvItemArrowLayout_select_setup, false);
            if (selectSetup)
            {
                mIvTopSetup.setVisibility(VISIBLE);
            }
            else
            {
                mIvTopSetup.setVisibility(GONE);
            }
            attributes.recycle();
        }
    }
    
    public void setCurrentPosition(int pos)
    {
        this.position = pos;
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        View focused = findFocus();
        int pos = indexOfChild(focused);
        if (pos >= 0 && pos < getChildCount())
        {
            setCurrentPosition(pos);
            postInvalidate();
        }
        return super.dispatchKeyEvent(event);
    }
    
    @Override
    protected int getChildDrawingOrder(int childCount, int i)
    {
        View v = getFocusedChild();
        int pos = indexOfChild(v);
        if (pos >= 0 && pos < childCount)
            setCurrentPosition(pos);
        if (i == childCount - 1)
        {// 这是最后一个需要刷新的item
            return position;
        }
        if (i == position)
        {// 这是原本要在最后一个刷新的item
            return childCount - 1;
        }
        return i;// 正常次序的item
    }
}
