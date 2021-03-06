package com.shim.mysample.log;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SLog {
    private static final int sLevel = 5;

    public static void v(String tag, String msg) {
        if (sLevel >= 5) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sLevel >= 4) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLevel >= 3) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sLevel >= 2) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sLevel >= 1) {
            Log.e(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        Log.wtf(tag, msg);
    }

    public static String dumpHexString(byte[] ba, int len, int lineLength) {
        StringBuilder sb = new StringBuilder();
        String hexNumber;

        for (int x = 0; x < len; x++) {
            if (x % lineLength == 0)
                hexNumber = String.format("\n%02x:", ba[x]);
            else
                hexNumber = String.format("02x:", ba[x]);
            sb.append(hexNumber);
        }

        return sb.toString();
    }

    public static void sendMessageToUi(Handler handler, int what, String str) {
        Message msg = handler.obtainMessage(what, str);
        handler.sendMessage(msg);
    }
}
