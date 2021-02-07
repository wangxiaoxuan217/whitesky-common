package com.whitesky.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import com.whitesky.common.R;


public class CircleProgressView extends ProgressBar
{
    // 定义默认值
    private static final int DEFAULT_TEXT_SIZE = 10; // sp
    
    private static final int DEFAULT_TEXT_COLOR = 0xFFFc00d1;
    
    private static final int DEFAULT_UNREACH_COLOR = 0xffd3d6da;
    
    private static final int DEFAULT_UNREACH_HEIGHT = 2; // dp
    
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    
    private static final int DEFAULT_REACH_HEIGHT = 3; // dp
    
    private static final int DEFAULT_TEXT_OFFSET = 10; // dp
    
    private static final int DEFAULT_CIRCLE_RADIU = 50; // dp
    // 使用默认值
    
    private float mTextSize = (float)sp2px(DEFAULT_TEXT_SIZE);
    
    private int mTextColor = DEFAULT_TEXT_COLOR;
    
    private int mUnreachColor = DEFAULT_UNREACH_COLOR;
    
    private int mUnreachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    
    private int mReachColor = DEFAULT_REACH_COLOR;
    
    private int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    
    private int mCircleRadius = dp2px(DEFAULT_CIRCLE_RADIU);
    
    private Paint mPaint = new Paint();
    
    public CircleProgressView(Context context)
    {
        // super(context);
        this(context, null);
    }
    
    public CircleProgressView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        obtainStylable(attrs);
    }
    
    private void obtainStylable(AttributeSet attrs)
    {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mTextSize = ta.getDimension(R.styleable.CircleProgressView_progress_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.CircleProgressView_progress_text_color, mTextColor);
        mUnreachColor = ta.getColor(R.styleable.CircleProgressView_progress_unreach_color, mUnreachColor);
        mUnreachHeight = (int)ta.getDimension(R.styleable.CircleProgressView_progress_unreach_height, mUnreachHeight);
        mReachColor = ta.getColor(R.styleable.CircleProgressView_progress_reach_color, mReachColor);
        mReachHeight = (int)ta.getDimension(R.styleable.CircleProgressView_progress_reach_height, mReachHeight);
        mTextOffset = (int)ta.getDimension(R.styleable.CircleProgressView_progress_text_offset, mTextOffset);
        mCircleRadius = (int)ta.getDimension(R.styleable.CircleProgressView_progress_circle_radius, mCircleRadius);
        ta.recycle();
        // 设置字体大小 ，用于后面计算控件的高度
        mPaint.setTextSize(mTextSize);
    }
    
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // 测量宽度
        int width = getMeasureWidth(widthMeasureSpec);
        
        // 测量高度
        int height = getMeasureHeight(heightMeasureSpec);
        
        // 设置宽高
        setMeasuredDimension(width, height);
        
        // 实际宽度
    }
    
    private int getMeasureHeight(int heightMeasureSpec)
    {
        int height = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY)
        {
            height =
                (mCircleRadius + Math.max(mUnreachHeight, mReachHeight)) * 2 + getPaddingTop() + getPaddingBottom();
            height = Math.max(height, heightSize);
        }
        else
        {
            height =
                (mCircleRadius + Math.max(mUnreachHeight, mReachHeight)) * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST)
            {
                height = Math.min(height, heightSize);
            }
        }
        return height;
    }
    
    private int getMeasureWidth(int widthMeasureSpec)
    {
        int width = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = (mCircleRadius + Math.max(mUnreachHeight, mReachHeight)) * 2 + getPaddingLeft() + getPaddingRight();
            width = Math.max(width, widthSize);
        }
        else
        {
            width = (mCircleRadius + Math.max(mUnreachHeight, mReachHeight)) * 2 + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST)
            {
                width = Math.min(width, widthSize);
            }
        }
        return width;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        canvas.save();
        // 绘制其实坐标,将坐标原点移到画布的中心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 代表是否需要绘制onReachCircle
        boolean isNeedUnReach = false;
        float radio = getProgress() * 1.0f / getMax();
        // 绘制unReachbar
        if (!isNeedUnReach)
        {
            float startAng = 0;
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            canvas.drawArc(-mCircleRadius, -mCircleRadius, mCircleRadius, mCircleRadius, 0f, 360, false, mPaint);
        }
        // 绘制文本
        mPaint.setTextSize(mTextSize); // 设置字体的
        mPaint.setStrokeWidth(mUnreachHeight); // 设置画笔的粗细
        mPaint.setStyle(Paint.Style.FILL); // 设置绘制是填充 还是镂空
        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text); // 测量文本宽度
        mPaint.setColor(mTextColor); // 设置画笔颜色
        // mPaint.setAntiAlias(true); //设置画笔抗锯齿
        int y = (int)(-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, -textWidth / 2, y, mPaint);
        
        float progressAng = radio * 360;
        // 绘制进度条的进度长度
        
        // /* 设置渐变色 这个正方形的颜色是改变的 */
        // Shader mShader = new LinearGradient(0, 0, 100, 100,
        // new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
        // Color.LTGRAY }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        // mPaint.setShader(mShader);
        
        if (progressAng > 0)
        {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            // canvas.drawCircle(0,0,mCircleRadius,mPaint);
            canvas
                .drawArc(-mCircleRadius, -mCircleRadius, mCircleRadius, mCircleRadius, 0f, progressAng, false, mPaint);
        }
        canvas.restore();
    }
    
    private float sp2px(int spVal)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
    
    private int dp2px(int dpVal)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }
}
