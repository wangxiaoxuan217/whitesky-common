package com.whitesky.common.widget.floating;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

/**
 * Descriptions: the control class of a floating Window with MediaPlayer.
 *
 */
public class FloatingWindowViewGroup2
{
    private static final int TYPE_SURFACEVIEW = SettingWidgets.TYPE_SURFACEVIEW;
    
    private static final int TYPE_TEXTUREVIEW = SettingWidgets.TYPE_TEXTUREVIEW;
    
    private static final int TYPE_NOPREVIEW = SettingWidgets.TYPE_NOPREVIEW;
    
    public FloatingWindowView mFloatingView;
    
    public View mPreview;
    
    public SurfaceView mSurfaceView;
    
    public SurfaceHolder mSurfaceHolder;
    
    public TextureView mTextureView;
    
    public FloatingWindowSurfaceCallback mCallback;
    
    public FloatingWindowTextureListener mListener;
    
    public WindowManager.LayoutParams wmParams;
    
    public int x;
    
    public int y;
    
    public int width;
    
    public int height;
    
    private int mViewType = TYPE_SURFACEVIEW;
    
    private Context mContext;
    
    private WindowManager mWindowManager = null;
    
    private MediaPlayer mPlayer;
    
    private String mFilePath;
    
    private boolean mBRTOn;
    
    public FloatingWindowViewGroup2(Context c, Bundle arg)
    {
        mContext = c;
        mWindowManager = (WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
        int winSize = arg.getInt(SettingWidgets.KEY_SIZE);
        width = (winSize == SettingWidgets.WINDOW_HD) ? 1920 : 640;
        height = (winSize == SettingWidgets.WINDOW_HD) ? 1080 : 480;
        mFilePath = arg.getString(SettingWidgets.KEY_FILE_PATH);
        mBRTOn = arg.getBoolean(SettingWidgets.KEY_RTMEDIAPLAYER);
    }
    
    private void play(Surface surface)
    {
        if (mPlayer == null)
        {
            try
            {
                mPlayer = new MediaPlayer();
                mPlayer.setSurface(surface);
                mPlayer.setLooping(true);
                if (mBRTOn)
                {
                    // mPlayer.useRTMediaPlayer(MediaPlayer.FORCE_RT_MEDIAPLAYER);
                }
                mPlayer.setDataSource(mFilePath);
                mPlayer.prepare();
                mPlayer.start();
            }
            catch (IOException e)
            {
                Toast.makeText(mContext, "MediaPlayer error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    public void destroy()
    {
        if (mPlayer != null)
        {
            mPlayer.release();
            mPlayer = null;
        }
        
        if (mWindowManager != null && mFloatingView != null)
        {
            mWindowManager.removeView(mFloatingView);
        }
    }
    
    public void setup(int type, Context c)
    {
        
        mViewType = type;
        
        if (type == TYPE_SURFACEVIEW)
        {
            mSurfaceView = new SurfaceView(c);
            mSurfaceHolder = mSurfaceView.getHolder();
            mCallback = new FloatingWindowSurfaceCallback();
            mSurfaceHolder.addCallback(mCallback);
            mPreview = mSurfaceView;
        }
        else if (type == TYPE_TEXTUREVIEW)
        {
            mTextureView = new TextureView(c);
            mListener = new FloatingWindowTextureListener();
            mTextureView.setSurfaceTextureListener(mListener);
            mPreview = mTextureView;
        }
        else
        {
        }
    }
    
    public void attachPreview()
    {
        RelativeLayout.LayoutParams param =
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        
        if (mViewType != FloatingWindowViewGroup2.TYPE_NOPREVIEW)
        {
            mPreview.setLayoutParams(param);
            mFloatingView.addView(mPreview);
        }
    }
    
    class FloatingWindowTextureListener implements TextureView.SurfaceTextureListener
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
        {
            play(new Surface(surface));
        }
        
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
        {
        }
        
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
        {
            return true;
        }
        
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface)
        {
        }
    }
    
    class FloatingWindowSurfaceCallback implements SurfaceHolder.Callback
    {
        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int width, int height)
        {
        }
        
        @Override
        public void surfaceCreated(SurfaceHolder arg0)
        {
            play(arg0.getSurface());
        }
        
        @Override
        public void surfaceDestroyed(SurfaceHolder arg0)
        {
        }
    }
    
}
