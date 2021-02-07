package com.whitesky.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.whitesky.common.R;


/**
 * @author xiaoxuan 2020.05.07 封装控件，适用于投影仪的Item布局使用
 */
public class TitleShowItemLayout extends ConstraintLayout
{
    private int position = 0;
    
    private TextView mTvTitleTip, mTvTitleSongName, mTvTitleNext, mTvTitleMode, mTvTitleWarning;
    
    private ImageView mIvTitleStatus, mIvTitleWarning;
    
    private LinearLayout mLlTitleWarning;
    
    public TitleShowItemLayout(Context context)
    {
        super(context);
    }
    
    public TitleShowItemLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs)
    {
        this.setChildrenDrawingOrderEnabled(true);
        // 加载布局
        View view = LayoutInflater.from(context).inflate(R.layout.widget_title_info, this);
        mTvTitleTip = (TextView)view.findViewById(R.id.tv_title_song_tip);
        mTvTitleSongName = (TextView)view.findViewById(R.id.tv_title_song_name);
        mTvTitleNext = (TextView)view.findViewById(R.id.tv_title_next_song);
        mTvTitleMode = (TextView)view.findViewById(R.id.tv_title_play_mode);
        mIvTitleStatus = (ImageView)view.findViewById(R.id.iv_title_play_status);
        mTvTitleWarning = (TextView)view.findViewById(R.id.tv_title_warning);
        mIvTitleWarning = (ImageView)view.findViewById(R.id.iv_title_warning);
        mLlTitleWarning = (LinearLayout)view.findViewById(R.id.ll_title_warning);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TitleShowItemLayout);
        if (attributes != null)
        {
            boolean hasWarning = attributes.getBoolean(R.styleable.TitleShowItemLayout_has_warning, false);
            boolean idleMode = attributes.getBoolean(R.styleable.TitleShowItemLayout_idle_mode, false);
            boolean otherMode = attributes.getBoolean(R.styleable.TitleShowItemLayout_other_mode, false);
            if (hasWarning)
            {
                setHasWarning();
            }
            if (idleMode)
            {
                setIdleMode();
            }
            if (otherMode)
            {
                setOtherMode();
            }
            attributes.recycle();
        }
    }
    
    // 显示提示信息
    public void setHasWarning()
    {
        mLlTitleWarning.setVisibility(VISIBLE);
        mTvTitleTip.setVisibility(GONE);
        mTvTitleSongName.setVisibility(GONE);
        mTvTitleNext.setVisibility(GONE);
        mTvTitleMode.setVisibility(GONE);
        mIvTitleStatus.setVisibility(GONE);
    }
    
    // 暖场模式
    public void setIdleMode()
    {
        mLlTitleWarning.setVisibility(GONE);
        mTvTitleTip.setVisibility(VISIBLE);
        mTvTitleSongName.setVisibility(VISIBLE);
        mTvTitleNext.setVisibility(GONE);
        mTvTitleMode.setVisibility(VISIBLE);
        mIvTitleStatus.setVisibility(GONE);
    }
    
    // 其他模式
    public void setOtherMode()
    {
        mLlTitleWarning.setVisibility(GONE);
        mTvTitleTip.setVisibility(GONE);
        mTvTitleSongName.setVisibility(VISIBLE);
        mTvTitleNext.setVisibility(VISIBLE);
        mTvTitleMode.setVisibility(VISIBLE);
        mIvTitleStatus.setVisibility(VISIBLE);
    }
    
    // 设置提示信息
    public void setWarningText(String warning)
    {
        if (!TextUtils.isEmpty(warning))
        {
            mTvTitleWarning.setText(warning);
        }
    }
    
    // 设置提示颜色
    public void setWarningUI(boolean isSuccess)
    {
        if (isSuccess)
        {
            mTvTitleWarning.setTextColor(getResources().getColor(R.color.color_title_tip));
            mIvTitleWarning.setImageResource(R.drawable.img_iy_title_tip_success);
        }
        else
        {
            mTvTitleWarning.setTextColor(getResources().getColor(R.color.color_title_no_wifi));
            mIvTitleWarning.setImageResource(R.drawable.img_iy_title_tip);
        }
    }
    
    // 设置暖场时，前端的提示
    public void setTitleTip(String tip)
    {
        if (!TextUtils.isEmpty(tip))
        {
            mTvTitleTip.setText(tip);
        }
    }
    
    // true：正在播放；false：暂停
    public void setTitleStatusImg(boolean status)
    {
        if (status)
        {
            mIvTitleStatus.setImageResource(R.drawable.img_iy_title_playing);
        }
        else
        {
            mIvTitleStatus.setImageResource(R.drawable.img_iy_title_pausing);
        }
    }
    
    // 设置当前播放的歌曲名
    public void setSongName(String songName)
    {
        mTvTitleSongName.setText(songName);
    }
    
    // 设置下一首的歌曲名
    public void setNextSong(String nextSong)
    {
        mTvTitleNext.setText(nextSong);
    }
    
    // 在其他模式时，设置当前播放模式
    public void setPlayMode(String playMode)
    {
        mTvTitleMode.setText(playMode);
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
