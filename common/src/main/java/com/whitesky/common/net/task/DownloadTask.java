package com.whitesky.common.net.task;

import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends AsyncTask<String, String, String> {
    private final static String TAG = "DownloadTask";

    private IDownloadListener mListener;

    private int mProgressValue;

    private File mDst;

    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            int lengthOfFile = conection.getContentLength();
            Log.d(TAG, "downloading length of file: " + lengthOfFile);
            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), lengthOfFile);

            File saveDir = new File("/sdcard/");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            // output stream
            Log.d(TAG, "downloading filename: " + "/sdcard/");
            mDst = new File(saveDir + File.separator + "skin.zip");
            OutputStream output = new FileOutputStream(mDst);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                mProgressValue = (int) ((total * 100) / lengthOfFile);
                // writing data to file
                output.write(data, 0, count);
                // show progress
                mListener.onDownloadProgress(mProgressValue);
            }
            // flushing output
            output.flush();
            // closing streams
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            mListener.onFailed(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        mProgressValue = 0;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (mProgressValue != 0) {
                mListener.onSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    public void setOnDownloadListener(IDownloadListener downloadListener) {
        this.mListener = downloadListener;
    }
}
