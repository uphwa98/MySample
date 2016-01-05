package com.shim.mysample;

import com.shim.mysample.log.SLog;

import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {
    @Override
    protected void onPreExecute() {
        SLog.i(TAG, "onPreExecute()");
    }

    @Override
    protected Boolean doInBackground(String... params) {
        SLog.i(TAG, "doInBackground() start - " + params[0]);

        publishProgress(1);

        try {
            SLog.i(TAG, "sleep 3 secons");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SLog.i(TAG, "doInBackground() end");
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        SLog.i(TAG, "onProgressUpdate()" + progress[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        SLog.i(TAG, "onPostExecute()");
    }

    @Override
    protected void onCancelled() {
        SLog.i(TAG, "onCancelled()");
    }

    private final String TAG = this.getClass().getSimpleName();
}