package com.whitesky.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.whitesky.common.R;

public class PickerView extends ScrollView
{
    private static final int TONE_MALE = 0;
    
    private static final int TONE_FEMALE = 1;
    
    private static final int TONE_ORIGINAL = 2;
    
    private static final int TONE_COMBINE = 3;
    
    private Context mContext;
    
    /**
     * 数据源
     */
    private String[] mDataArray;
    
    /**
     * 两条线的画笔
     */
    private Paint mLinePaint;
    
    /**
     * 宽,高,还有显示的TextView的高度（注意:这里只是TextView的高度,不算margin）
     */
    private int mWidth, mHeight, mTextHeight;
    
    /**
     * TextView的父容器
     */
    private LinearLayout mTextGroup;
    
    /**
     * 获取屏幕的密度
     */
    private float mDensity;
    
    /**
     * 字体的大小
     */
    private float mTextSize;
    
    /**
     * 根据字体大小来控制整个控件的高度
     */
    private float mWrapContentHeight;
    
    /**
     * 当前选中的position
     */
    private int position, tempPosition = -1;
    
    /**
     * 设置监听
     */
    private onSelectListener mSelectListener;
    
    private int scrollY;
    
    private int mDefaultColor, mCheckedColor, mLineColor;
    
    private boolean mIsTone;
    
    public PickerView(Context context)
    {
        this(context, null);
    }
    
    public PickerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public PickerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        /**
         * 这句话设置ScrollView滑动到极限的时候不显示提示（就是那个阴影）
         */
        setOverScrollMode(OVER_SCROLL_NEVER);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView, defStyleAttr, 0);
        mDefaultColor = a.getColor(R.styleable.PickerView_default_textColor,
            getResources().getColor(R.color.color_picker_unchecked));
        mCheckedColor =
            a.getColor(R.styleable.PickerView_checked_textColor, getResources().getColor(R.color.color_picker_checked));
        mIsTone = a.getBoolean(R.styleable.PickerView_is_tone, false);
        mLineColor = a.getColor(R.styleable.PickerView_line_Color, getResources().getColor(R.color.color_picker_line));
        try
        {
            /**
             * 获取到用户设置的字体大小,默认24
             */
            mTextSize = a.getDimension(R.styleable.PickerView_default_textSize, 24);
            /**
             * 获取到屏幕的密度来设置TextView的高度
             */
            mDensity = getResources().getDisplayMetrics().density;
            mTextHeight = (int)((mDensity + 0.5) * mTextSize);
            /**
             * 整个控件的高度为6个TextView的高度
             */
            mWrapContentHeight = mTextHeight * 6;
        }
        finally
        {
            a.recycle();
        }
    }
    
    public void setPvTextColor(int defaultColor, int checkedColor)
    {
        this.mCheckedColor = checkedColor;
        this.mDefaultColor = defaultColor;
        refreshTextView();
    }
    
    private void refreshTextView()
    {
        if (mTextGroup != null)
        {
            int size = mTextGroup.getChildCount();
            for (int i = 0; i < size; i++)
            {
                TextView tv = (TextView)mTextGroup.getChildAt(i);
                /**
                 * 因为我们在数据开头添加了一个空的数据,所以这里position要+1
                 */
                if (tempPosition + 1 == i)
                {
                    tv.setTextColor(mCheckedColor);
                }
                else
                {
                    tv.setTextColor(mDefaultColor);
                }
            }
        }
    }
    
    private void init()
    {
        /**
         * 初始化数据,首先添加Group
         */
        mTextGroup = new LinearLayout(mContext);
        mTextGroup.setOrientation(LinearLayout.VERTICAL);
        mTextGroup.setGravity(Gravity.CENTER);
        addView(mTextGroup);
        
        /**
         * 由于我们需要给自身的数据在选中的框里显示 所以这里需要添加前面和后面的空数据
         */
        mTextGroup.addView(createTextView(""));
        for (int i = 0; i < mDataArray.length; i++)
        {
            mTextGroup.addView(createTextView("" + mDataArray[i]));
        }
        mTextGroup.addView(createTextView(""));
        
        /**
         * 设置背景,这里选择画一个,两条线
         */
        setBackground(new Drawable()
        {
            @Override
            public void draw(Canvas canvas)
            {
                /**
                 * 这里把两条线之间的距离设置为了一个TextView的高度
                 */
                canvas.drawLine(0,
                    mHeight / 2 - mTextHeight / 2,
                    mWidth * 1.0f,
                    mHeight / 2 - mTextHeight / 2,
                    mLinePaint);
                canvas.drawLine(0,
                    mHeight / 2 + mTextHeight / 2,
                    mWidth * 1.0f,
                    mHeight / 2 + mTextHeight / 2,
                    mLinePaint);
            }
            
            @Override
            public void setAlpha(int i)
            {
                
            }
            
            @Override
            public void setColorFilter(ColorFilter colorFilter)
            {
                
            }
            
            @SuppressLint("WrongConstant")
            @Override
            public int getOpacity()
            {
                return 0;
            }
        });
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(mLineColor);
        setPosition(mDataArray.length / 2);
    }
    
    /**
     * 动态创建TextView
     */
    private TextView createTextView(String s)
    {
        TextView tv = new TextView(mContext);
        tv.setText(s);
        tv.setTextSize(mTextSize);
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mTextHeight);
        params.bottomMargin = mTextHeight / 2;
        params.topMargin = mTextHeight / 2;
        tv.setLayoutParams(params);
        return tv;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 更改测量方法 指定高度为mWrapContentHeight
         */
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = (int)mWrapContentHeight;
        setMeasuredDimension(mWidth, (int)mWrapContentHeight);
    }
    
    private int measureWidth(int size)
    {
        int mode = MeasureSpec.getMode(size);
        switch (mode)
        {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                return (int)mWrapContentHeight;
            case MeasureSpec.EXACTLY:
            default:
                return MeasureSpec.getSize(size);
        }
    }
    
    /**
     * ScrollView的滑动事件监听
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        
        /**
         * 计算出当前在两条线里的position
         */
        position = (t + mTextHeight) / (mTextHeight * 2);
        /**
         * 因为此方法会在滑动的时候不停的调用,所以这里设置一个临时的变量来控制
         */
        if (tempPosition != position)
        {
            int size = mTextGroup.getChildCount();
            for (int i = 0; i < size; i++)
            {
                TextView tv = (TextView)mTextGroup.getChildAt(i);
                /**
                 * 因为我们在数据开头添加了一个空的数据,所以这里position要+1
                 */
                if (position + 1 == i)
                {
                    if (mIsTone)
                    {
                        switch (position)
                        {
                            case TONE_MALE:
                                tv.setTextColor(getResources().getColor(R.color.color_song_tone_key_male));
                                break;
                            case TONE_FEMALE:
                                tv.setTextColor(getResources().getColor(R.color.color_picker_tone_key_female));
                                break;
                            case TONE_COMBINE:
                                tv.setTextColor(getResources().getColor(R.color.color_song_tone_key_chorus));
                                break;
                            case TONE_ORIGINAL:
                                tv.setTextColor(getResources().getColor(R.color.color_lead_song_text));
                                break;
                            default:
                                break;
                        }
                    }
                    else
                    {
                        tv.setTextColor(mCheckedColor);
                    }
                }
                else
                {
                    if (mIsTone)
                    {
                        switch (i - 1)
                        {
                            case TONE_MALE:
                                tv.setTextColor(getResources().getColor(R.color.color_song_tone_key_male_uncheck));
                                break;
                            case TONE_FEMALE:
                                tv.setTextColor(getResources().getColor(R.color.color_picker_tone_key_female_uncheck));
                                break;
                            case TONE_COMBINE:
                                tv.setTextColor(getResources().getColor(R.color.color_song_tone_key_chorus_uncheck));
                                break;
                            case TONE_ORIGINAL:
                                tv.setTextColor(getResources().getColor(R.color.color_song_tone_key_original_uncheck));
                                break;
                            default:
                                break;
                        }
                    }
                    else
                    {
                        tv.setTextColor(mDefaultColor);
                    }
                }
            }
        }
        tempPosition = position;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        /**
         * 因为ScrollView没有停止滑动的监听,所以这里取巧 在手指离开屏幕的30ms后判断是否和原来的scrollY一样 如果一样则进入,如果不一样则设置为一样
         */
        if (ev.getAction() == MotionEvent.ACTION_UP)
        {
            scrollY = getScrollY();
            postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if (scrollY == getScrollY())
                    {
                        /**
                         * 获得每次松手后scrollY相对于TextView高度的偏移量
                         */
                        int offset = scrollY % (mTextHeight * 2);
                        
                        /**
                         * 如果偏移量大于TextView高度的一半 则进入到下一个
                         */
                        if (offset > mTextHeight)
                        {
                            smoothScrollTo(0, scrollY - offset + (mTextHeight * 2));
                        }
                        else
                        {
                            smoothScrollTo(0, scrollY - offset);
                        }
                    }
                    else
                    {
                        scrollY = getScrollY();
                        post(this);
                    }
                    if (mSelectListener != null)
                    {
                        mSelectListener.onSelect(tempPosition);
                    }
                }
            }, 30);
        }
        return super.onTouchEvent(ev);
    }
    
    /**
     * 设置fling的速度为原来的1/3
     */
    @Override
    public void fling(int velocityY)
    {
        super.fling(velocityY / 3);
    }
    
    /**
     * 设置数据源
     */
    public void setData(String[] data)
    {
        this.mDataArray = data;
        init();
    }
    
    /**
     * 设置position
     */
    public void setPosition(int position)
    {
        this.position = position;
        if (position == 0)
        {
            post(new Runnable()
            {
                @Override
                public void run()
                {
                    scrollTo(0, 1);
                }
            });
            return;
        }
        post(new Runnable()
        {
            
            @Override
            public void run()
            {
                scrollTo(0, PickerView.this.position * (mTextHeight * 2));
            }
            
        });
    }
    
    /**
     * 获取position
     *
     * @return
     */
    public int getPosition()
    {
        return position;
    }
    
    public interface onSelectListener
    {
        void onSelect(int currentSelect);
    }
    
    public void setOnSelectListener(onSelectListener listener)
    {
        mSelectListener = listener;
    }
}