package com.shim.mysample;

import com.shim.mysample.log.SLog;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class TemplateLocalService extends Service {

    public class LocalBinder extends Binder {
        TemplateLocalService getService() {
            return TemplateLocalService.this;
        }
    }

    @Override
    public void onCreate() {
        SLog.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SLog.i(TAG, "onStartCommand() action : " + intent.getAction() + " startId : " + startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        SLog.i(TAG, "onDestroy()");
        Toast.makeText(this, "Local service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        SLog.i(TAG, "onBind() action : " + intent.getAction());
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    private final String TAG = this.getClass().getSimpleName();
}

