package com.shim.mysample;

import com.shim.mysample.log.SLog;

import android.content.Context;
import android.os.Handler;

public class TemplateClass {
    private Context mContext;
    private Handler mHandler;

    public TemplateClass(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        init();
    }

    private void init() {
        SLog.sendMessageToUi(mHandler, 0, "init..");
    }
}
