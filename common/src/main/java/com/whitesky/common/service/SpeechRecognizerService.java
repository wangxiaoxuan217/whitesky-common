package com.whitesky.common.service;

import android.app.Instrumentation;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.token.AccessToken;
import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechRecognizer;
import com.alibaba.idst.util.SpeechRecognizerCallback;
import com.alibaba.idst.util.SpeechRecognizerWithRecorder;
import com.alibaba.idst.util.SpeechRecognizerWithRecorderCallback;
import com.alibaba.idst.util.SpeechSynthesizer;
import com.whitesky.common.utils.SharedPreferencesUtil;
import com.whitesky.common.widget.WindowHeadToast;

import org.java_websocket.WebSocket;

import java.nio.ByteBuffer;

public class SpeechRecognizerService extends Service
{
    private static final String TAG = "SpeechRecognizerService";
    
    private WindowHeadToast mWindowHeadToast;
    
    private NlsClient client;
    
    private SpeechRecognizer speechRecognizer;
    
    private SpeechSynthesizer speechSynthesizer;
    
    private SpeechRecognizerWithRecorder speechRecognizerWithRecorder;
    
    private IntentFilter intentFilter;
    
    private WhiteskyReceiver whiteskyReceiver;
    
    private SRSBinder mBinder;
    
    private WhiteskyWebSocketServer mServer;
    
    private static SharedPreferencesUtil mSPAlibaba;
    
    private static boolean mVoiceKey;
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    startRecognizerWithRecord();
                    break;
                case 0x02:
                    stopRecognizerWithRecord();
                    break;
                case 0x03:
                    ByteBuffer message = (ByteBuffer)msg.obj;
                    startRecognizer(message);
                    break;
                case 0x04:
                    stopRecognizer();
                    break;
                case 0x05:
                    mWindowHeadToast.showInfo(dealResult((String)msg.obj));
                    nlp((String)msg.obj);
                    break;
                case 0x06:
                    mWindowHeadToast.showCustomToast();
                    break;
                case 0x07:
                    mWindowHeadToast.forceRemoveToast();
                    break;
                default:
                    break;
            }
        }
    };
    
    public SpeechRecognizerService()
    {
    }
    
    @Override
    public void onCreate()
    {
        mBinder = new SRSBinder();
        
        client = new NlsClient();
        mWindowHeadToast = new WindowHeadToast(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.whitesky.tvservice.SendHotKey");
        whiteskyReceiver = new WhiteskyReceiver();
        registerReceiver(whiteskyReceiver, intentFilter);
        
        mServer = new WhiteskyWebSocketServer(9001)
        {
            @Override
            public void onMessage(WebSocket conn, ByteBuffer message)
            {
                super.onMessage(conn, message);
                checkToken(0x03, message);// 1:蓝牙传输 3:网络传输
            }
            
            @Override
            public void onMessage(WebSocket conn, String arg1)
            {
                if (arg1.equals("startRecord"))
                {
                    handler.sendEmptyMessage(0x06);
                }
                else if (arg1.contains("keycode") && arg1.contains("end"))
                {
                    String[] str = arg1.split(":");
                    if (str.length == 0x03)
                    {
                        int keycode = Integer.valueOf(str[1]);
                        try
                        {
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(keycode);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                
                super.onMessage(conn, arg1);
            }
        };
        mServer.setReuseAddr(true);
        mServer.start();
        mSPAlibaba = new SharedPreferencesUtil(this, "Alibaba", Context.MODE_MULTI_PROCESS);
        
        super.onCreate();
        Log.d(TAG, "onCreate");
    }
    
    @Override
    public void onDestroy()
    {
        if (client != null)
        {
            client.release();
            client = null;
        }
        
        unregisterReceiver(whiteskyReceiver);
        
        mServer.stop();
        
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        if (mBinder != null)
        {
            return mBinder;
        }
        return null;
    }
    
    public class SRSBinder extends Binder
    {
        
        public void startWebsocket()
        {
        }
    }
    
    class WhiteskyReceiver extends BroadcastReceiver
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getBooleanExtra("down", false))
            {
                mVoiceKey = true;
                Log.e(this.toString(), "startRecognizer");
                handler.sendEmptyMessage(6);
                checkToken(0x01, null);// 1:遥控器识别 3:微信小程序识别
            }
            else
            {
                mVoiceKey = false;
                Log.e(this.toString(), "stopRecognizer");
                handler.sendEmptyMessage(0x02);
            }
        }
    }
    
    private void checkToken(final int after, final ByteBuffer buffer)
    {
        long currentTime = System.currentTimeMillis();
        if ((mSPAlibaba.getString("accessToken", "notready").equals("notready"))
            || ((mSPAlibaba.getLong("expireTime", 0) * 1000 - currentTime) < 600000))
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    AccessToken token = new AccessToken("", "");
                    try
                    {
                        token.apply();
                        String accessToken = token.getToken();
                        long expireTime = token.getExpireTime();
                        mSPAlibaba.putString("accessToken", accessToken);
                        mSPAlibaba.putLong("expireTime", expireTime);
                        // 1:蓝牙传输 3:网络传输
                        if (after == 0x01)
                        {
                            handler.sendEmptyMessage(after);
                        }
                        else if (after == 0x03)
                        {
                            Message msg = handler.obtainMessage();
                            msg.what = after;
                            msg.obj = buffer;
                            handler.sendMessage(msg);
                        }
                        Log.e(TAG, "refresh token " + accessToken + "  " + expireTime);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
            
        }
        else
        {
            if (after == 0x01)
            {
                handler.sendEmptyMessage(after);
            }
            else if (after == 0x03)
            {
                Message msg = handler.obtainMessage();
                msg.what = after;
                msg.obj = buffer;
                handler.sendMessage(msg);
            }
        }
    }
    
    private void startRecognizerWithRecord()
    {
        SpeechRecognizerWithRecorderCallback callback = new MySpeechRecognizerWithRecorderCallback();
        speechRecognizerWithRecorder = client.createRecognizerWithRecorder(callback);
        speechRecognizerWithRecorder.setToken(mSPAlibaba.getString("accessToken", "notready"));
        speechRecognizerWithRecorder.setAppkey("");
        speechRecognizerWithRecorder.enableInverseTextNormalization(true);
        speechRecognizerWithRecorder.enablePunctuationPrediction(false);
        speechRecognizerWithRecorder.enableIntermediateResult(false);
        speechRecognizerWithRecorder.start();
    }
    
    private void stopRecognizerWithRecord()
    {
        speechRecognizerWithRecorder.stop();
    }
    
    private void startRecognizer(ByteBuffer message)
    {
        SpeechRecognizerCallback callback = new MySpeechRecognizerCallback();
        speechRecognizer = client.createRecognizerRequest(callback);
        speechRecognizer.setToken(mSPAlibaba.getString("accessToken", "notready"));
        speechRecognizer.setAppkey("ZOnFX9rl8ukbtgcN");
        speechRecognizer.enableInverseTextNormalization(true);
        speechRecognizer.enablePunctuationPrediction(false);
        speechRecognizer.enableIntermediateResult(false);
        speechRecognizer.enableVoiceDetection(true);
        speechRecognizer.setMaxStartSilence(3000);
        speechRecognizer.setMaxEndSilence(600);
        speechRecognizer.start();
        byte[] test = decodeValue(message);
        speechRecognizer.sendAudio(test, test.length);
        handler.sendEmptyMessage(4);
    }
    
    private void stopRecognizer()
    {
        speechRecognizer.stop();
    }
    
    private class MySpeechRecognizerWithRecorderCallback implements SpeechRecognizerWithRecorderCallback
    {
        
        public MySpeechRecognizerWithRecorderCallback()
        {
        }
        
        @Override
        public void onRecognizedStarted(String msg, int code)
        {
            Log.d(TAG, "OnRecognizedStarted " + msg + ": " + String.valueOf(code));
        }
        
        @Override
        public void onTaskFailed(String msg, int code)
        {
            if (!mVoiceKey)
            {
                handler.sendEmptyMessage(0x07);
            }
            Log.d(TAG, "OnTaskFailed " + msg + ": " + String.valueOf(code));
        }
        
        @Override
        public void onRecognizedResultChanged(final String msg, int code)
        {
            Log.d(TAG, "OnRecognizedResultChanged " + msg + ": " + String.valueOf(code));
        }
        
        @Override
        public void onRecognizedCompleted(final String result, int code)
        {
            Message msg = handler.obtainMessage();
            msg.what = 0x05;
            msg.obj = result;
            handler.sendMessage(msg);
            Log.d(TAG, "OnRecognizedCompleted " + result + ": " + String.valueOf(code));
        }
        
        @Override
        public void onChannelClosed(String msg, int code)
        {
            Log.d(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }
        
        @Override
        public void onVoiceData(byte[] bytes, int i)
        {
            
        }
        
        @Override
        public void onVoiceVolume(int i)
        {
            
        }
    };
    
    private class MySpeechRecognizerCallback implements SpeechRecognizerCallback
    {
        
        public MySpeechRecognizerCallback()
        {
        }
        
        @Override
        public void onRecognizedStarted(String msg, int code)
        {
            Log.d(TAG, "OnRecognizedStarted " + msg + ": " + String.valueOf(code));
        }
        
        @Override
        public void onTaskFailed(String msg, int code)
        {
            if (!mVoiceKey)
            {
                handler.sendEmptyMessage(0x07);
            }
            Log.d(TAG, "OnTaskFailed: " + msg + ": " + String.valueOf(code));
        }
        
        @Override
        public void onRecognizedResultChanged(final String msg, int code)
        {
            Log.d(TAG, "OnRecognizedResultChanged " + msg + ": " + String.valueOf(code));
        }
        
        @Override
        public void onRecognizedCompleted(final String result, int code)
        {
            Message msg = handler.obtainMessage();
            msg.what = 0x05;
            msg.obj = result;
            handler.sendMessage(msg);
            Log.d(TAG, "OnRecognizedCompleted " + result + ": " + String.valueOf(code));
        }
        
        @Override
        public void onChannelClosed(String msg, int code)
        {
            
            Log.d(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }
    };
    
    private String dealResult(String fullResult)
    {
        String result = null;
        if (!fullResult.equals(""))
        {
            JSONObject jsonObject = JSONObject.parseObject(fullResult);
            if (jsonObject.containsKey("payload"))
            {
                result = jsonObject.getJSONObject("payload").getString("result");
                Log.e(TAG, result);
            }
        }
        if (result.isEmpty())
        {
            result = "您似乎没有说话";
        }
        return result;
    }
    
    private byte[] decodeValue(ByteBuffer bytes)
    {
        int len = bytes.limit() - bytes.position();
        byte[] bytes1 = new byte[len];
        bytes.get(bytes1);
        return bytes1;
    }
    
    private void nlp(String data)
    {

    }
    
    private void launcherApp(String packageName)
    {
        PackageManager manager = getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(packageName);
        if (intent == null)
            return;
        startActivity(intent);
    }
}
