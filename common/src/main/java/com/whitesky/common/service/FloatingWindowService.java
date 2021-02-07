package com.whitesky.common.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.whitesky.common.R;
import com.whitesky.common.widget.floating.FloatingWindowView;
import com.whitesky.common.widget.floating.FloatingWindowViewGroup;
import com.whitesky.common.widget.floating.FloatingWindowViewGroup2;
import com.whitesky.common.widget.floating.SettingWidgets;


public class FloatingWindowService extends Service {
    public static final int ID_HDMI_RX = 0;

    public static final int ID_USB_CAM = 1;

    private static final String TAG = "FloatingWindowService2";

    public static boolean bServiceIsAlive = false;

    private final IBinder mBinder = new LocalBinder();

    private FloatingWindowViewGroup[] mViewGroup = new FloatingWindowViewGroup[3];

    private FloatingWindowViewGroup2[] mViewGroup2 = new FloatingWindowViewGroup2[3];

    private MyReceiver mReceiver;

    public static String dumpBundleInfo(Bundle bundle) {
        if (bundle == null) {
            return "Bundle is null";
        }

        return "Enabled:" + bundle.getBoolean(SettingWidgets.KEY_ENABLED) + " ViewType:"
                + bundle.getInt(SettingWidgets.KEY_VIEWTYPE) + " Record:" + bundle.getBoolean(SettingWidgets.KEY_RECORD)
                + " WindowSize:" + bundle.getInt(SettingWidgets.KEY_SIZE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bServiceIsAlive = true;
        IntentFilter filter = new IntentFilter();
        mReceiver = new MyReceiver(this);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction("rtk.intent.action.POWER_SUSPEND");
        registerReceiver(mReceiver, filter /* , null, mHandler */);
    }

    private void createFloatingWindowEx(int winId, Bundle arg) {
        boolean enabled = arg.getBoolean(SettingWidgets.KEY_ENABLED);
        if (!enabled) {
            Log.d(TAG, "WinID:" + winId + " is not enabled");
            return;
        }

        mViewGroup[winId] = new FloatingWindowViewGroup(this, arg);
        FloatingWindowViewGroup group = mViewGroup[winId];

        int sourceType = winId;
        group.mCameraType = sourceType;

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        group.mFloatingView = (FloatingWindowView) li.inflate(R.layout.floatingwindow, null);

        TextView infoText = (TextView) group.mFloatingView.findViewById(R.id.tv_floating_info);
        if (winId == ID_HDMI_RX) {
            infoText.setText("HDMIRx: No preview.. (ˊ_>ˋ)");
        } else if (winId == ID_USB_CAM) {
            infoText.setText("USBCam: No preview.. (ˊ_>ˋ)");
        } else {
            infoText.setText("No preview.. (ˊ_>ˋ)");
        }

        // setup view type
        int viewType = arg.getInt(SettingWidgets.KEY_VIEWTYPE);
        group.setup(viewType, this);
        group.attachPreview();
        int flag = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        int windowSize = arg.getInt(SettingWidgets.KEY_SIZE);
        int width = 0;
        int height = 0;
        switch (windowSize) {
            case SettingWidgets.WINDOW_HD_1920:
                width = 576;
                height = 324;
                break;
            case SettingWidgets.WINDOW_HD_1280:
                width = 448;
                height = 252;
                break;
            case SettingWidgets.WINDOW_SD:
                width = 256;
                height = 144;
                break;
        }
        group.wmParams = new WindowManager.LayoutParams(width, // 192*3,//WindowManager.LayoutParams.MATCH_PARENT,
                height, // 108*3,//WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, flag, PixelFormat.TRANSLUCENT);

        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        switch (winId) {
            case ID_HDMI_RX:
                group.wmParams.x = 0;
                group.wmParams.y = screenHeight - height;
                break;
            case ID_USB_CAM:
                group.wmParams.x = screenWidth - width;
                group.wmParams.y = screenHeight - height;
                break;
        }

        group.wmParams.gravity = Gravity.TOP | Gravity.LEFT;
        group.mFloatingView.setBackgroundColor(Color.BLACK);
        windowManager.addView(group.mFloatingView, group.wmParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("usbcam");
//        LogUtil.d(TAG, dumpBundleInfo(bundle));
        if (bundle != null) {
            createFloatingWindowEx(ID_USB_CAM, bundle);
        }
        // TODO create floating window for MediaPlayer
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        for (int i = 0; i < 3; i++) {
            if (mViewGroup[i] != null) {
                mViewGroup[i].destroy();
                mViewGroup[i] = null;
            }

            if (mViewGroup2[i] != null) {
                mViewGroup2[i].destroy();
                mViewGroup2[i] = null;
            }
        }
        super.onDestroy();
        bServiceIsAlive = false;
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    public class LocalBinder extends Binder {
        FloatingWindowService getService() {
            return FloatingWindowService.this;
        }
    }

    // add BroadcastReceiver
    class MyReceiver extends BroadcastReceiver {
        FloatingWindowService mService;

        MyReceiver(FloatingWindowService service) {
            mService = service;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "stopSelf dummy");
        }
    }
}
