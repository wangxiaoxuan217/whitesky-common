package com.whitesky.common.widget;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;

public class AudioRecordAndPlayThread extends Thread
{
    private final int AUDIO_MIC_SAMPLE_RATE = 44100;
    
    private final int AUDIO_MIC_CHANNEL_NUM = 2;
    
    private boolean stopCapture = false;
    
    private int mAudioSource = 0;
    
    @Override
    public void run()
    {
        int bufferSize = AudioRecord
            .getMinBufferSize(AUDIO_MIC_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord mAudioRecord = new AudioRecord.Builder().setAudioSource(mAudioSource)
            .setAudioFormat(new AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(AUDIO_MIC_SAMPLE_RATE)
                .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                .build())
            .setBufferSizeInBytes(bufferSize)
            .build();
        
        bufferSize = AudioTrack
            .getMinBufferSize(AUDIO_MIC_SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE)
        {
            // For some readon we couldn't obtain a buffer size
            bufferSize = AUDIO_MIC_SAMPLE_RATE * AUDIO_MIC_CHANNEL_NUM * 2;
        }
        AudioTrack mAudioTrack = new AudioTrack.Builder()
            .setAudioFormat(new AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(AUDIO_MIC_SAMPLE_RATE)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build())
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build();
        
        mAudioRecord.startRecording();
        mAudioTrack.play();
        int num = 0;
        short[] buf = new short[bufferSize];
        while (true)
        {
            num = mAudioRecord.read(buf, 0, bufferSize);
            mAudioTrack.write(buf, 0, num);
            if (stopCapture)
                break;
        }
    }
    
    public void stopThread()
    {
        stopCapture = true;
    }
    
    public void setAudioSource(int source)
    {
        mAudioSource = source;
    }
    
}
