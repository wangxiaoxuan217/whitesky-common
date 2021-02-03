package com.whitesky.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitmapUtils;
import com.whitesky.common.R;
import com.whitesky.common.utils.Utils;

public class QrDialog extends Dialog
{
    
    public QrDialog(@NonNull Context context)
    {
        super(context);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qr);
        ImageView qrCode = findViewById(R.id.iv_phone_qr);
        Bitmap bitmap = null;
        try
        {
            bitmap = BitmapUtils.create2DCode("http://projector.whitesky.com.cn/rc?ip="+ Utils.getIP()+"&id=2");
            qrCode.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void show()
    {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);
    }
    
}
