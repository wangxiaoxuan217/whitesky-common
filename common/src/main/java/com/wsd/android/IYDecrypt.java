package com.wsd.android;

public class IYDecrypt {
    static {
        System.loadLibrary("iydecrypt");
    }

    //native方法
    public static native void test();
    public static native int DecryptStream(byte[] data, int size, String key_type);
}