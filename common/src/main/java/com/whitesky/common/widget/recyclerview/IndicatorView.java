package com.whitesky.common.widget.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whitesky.common.R;
import com.whitesky.common.utils.SizeUtil;


/**
 * @author Ying on 2020.10.20
 */
public class IndicatorView extends FrameLayout
{
    private View mSlider;
    
    private FrameLayout mFlIndicator;
    
    private int mSliderHeight, mSliderWidth;
    
    private boolean mIsAllow;
    
    private Handler handler;
    
    private Runnable runnable;
    
    private int mTotalPage;
    
    private final float[] endX = {0};
    
    private int mMainLineLength;
    
    private int range, transMaxRange;
    
    private float proportion;
    
    public IndicatorView(Context context)
    {
        super(context);
        init(context, null);
    }
    
    public IndicatorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }
    
    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs)
    {
        this.setChildrenDrawingOrderEnabled(true);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_recyclerview_slider, this);
        View mainLine = view.findViewById(R.id.view_main_line);
        mFlIndicator = (FrameLayout)view.findViewById(R.id.fl_indicator);
        mSlider = view.findViewById(R.id.view_slider);
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RcvSlider);
        if (attributes != null)
        {
            int mainLineColor = attributes.getResourceId(R.styleable.RcvSlider_main_line_color, R.color.white_60);
            int sliderColor = attributes.getResourceId(R.styleable.RcvSlider_slider_color, R.color.white);
            int width = attributes.getInteger(R.styleable.RcvSlider_slider_width, 10);
            int height = attributes.getInteger(R.styleable.RcvSlider_slider_width, 10);
            mMainLineLength = attributes.getInteger(R.styleable.RcvSlider_main_line_length, 1960);
            mSliderHeight = SizeUtil.px2dip(context, height);
            mSliderWidth = SizeUtil.px2dip(context, width);
            mainLine.setBackgroundResource(mainLineColor);
            mSlider.setBackgroundResource(sliderColor);
        }
        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                mFlIndicator.setVisibility(View.GONE);
            }
        };
    }
    
    /**
     * 重设 slider 的宽、高
     */
    public void setSliderSize(int totalPage)
    {
        mTotalPage = totalPage;
        LayoutParams lp = (LayoutParams)mSlider.getLayoutParams();
        mSliderWidth = Math.max(mMainLineLength / totalPage, 20);
        if (lp.height != mSliderHeight || lp.width != mSliderWidth)
        {
            lp.width = mSliderWidth;
            lp.height = mSliderHeight;
            mSlider.setLayoutParams(lp);
        }
        endX[0] = 0;
        mSlider.setTranslationX(0);
    }
    
    public void setIsAllowDisappear(boolean isAllow)
    {
        mIsAllow = isAllow;
        if (mIsAllow)
        {
            mFlIndicator.setVisibility(View.GONE);
        }
        else
        {
            mFlIndicator.setVisibility(View.VISIBLE);
        }
    }
    
    public void setupWithRecyclerView(@NonNull
    final RecyclerView recyclerView)
    {
        // 这里的mRvHx是需要绑定滚动条的RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (mIsAllow && mTotalPage > 1)
                {
                    switch (newState)
                    {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            handler.postDelayed(runnable, 1000);
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                            handler.removeCallbacks(runnable);
                            mFlIndicator.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
            
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                range = recyclerView.getWidth() * (mTotalPage - 1);
                // 滑动的距离
                endX[0] += dx;
                // 计算比例
                proportion = endX[0] / range;
                // 计算滚动条宽度
                transMaxRange = ((ViewGroup)mSlider.getParent()).getWidth() - mSlider.getWidth();
                // 设置滚动条移动
                mSlider.setTranslationX(transMaxRange * proportion);
            }
        });
    }
}
