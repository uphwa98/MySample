package com.shim.mysample;

import com.shim.mysample.log.SLog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SLog.v(TAG, "onCreate()");

        mThisContext = this;
        mTextView = (TextView) findViewById(R.id.text1);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListStrings));
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                SLog.v(TAG, "position : " + position);
                switch (position) {
                case 0:
                    // mTextView.append("----------------------");
                    MyAsyncTask task1 = new MyAsyncTask();
                    task1.execute("task1", "1");
                    MyAsyncTask task2 = new MyAsyncTask();
                    task2.execute("task2", "2");
                    break;
                case 1:
                    MyAsyncTask task3 = new MyAsyncTask();
                    task3.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "task3", "1");
                    MyAsyncTask task4 = new MyAsyncTask();
                    task4.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "task4", "2");
                    break;
                case 2:
                    doBindService();
                    break;
                case 3:
                    doUnbindService();
                    break;
                case 4:
                    MainActivity.this.startService(new Intent(MainActivity.this, LocalService.class).setAction("test"));
                    break;
                case 5:
                    boolean ret = MainActivity.this.stopService(new Intent(MainActivity.this, LocalService.class));
                    SLog.v(TAG, "stopService ret : " + ret);
                    break;
                case 6:
                    mThisContext.startActivity(new Intent(mThisContext, PendingIntentNAlarmManager.class));
                    break;
                case 7:
                    mThisContext.startActivity(new Intent(mThisContext, FileRead.class));
                    break;
                case 8:
                    mThisContext.startActivity(new Intent(mThisContext, VariousExample.class));
                    break;
                case 9:
                    mThisContext.startActivity(new Intent(mThisContext, BluetoothActivity.class));
                    break;
                default:
                    break;
                }
            }
        });
    }
    
    private LocalService mBoundService;
    private boolean mIsBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((LocalService.LocalBinder)service).getService();
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
            Toast.makeText(MainActivity.this, "Service disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        bindService(new Intent(MainActivity.this, LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SLog.v(TAG, "onRestart()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        SLog.v(TAG, "onStart()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        SLog.v(TAG, "onResume()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        SLog.v(TAG, "onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        SLog.v(TAG, "onStop()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SLog.v(TAG, "onDestroy()");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SLog.v(TAG, "onSaveInstanceState()");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SLog.v(TAG, "onRestoreInstanceState()");
    }
    @Override
    protected void onNewIntent(Intent intent) {
        SLog.v(TAG, "onNewIntent()");
    }

    private String[] mListStrings = {
            "00 AsyncTask test",
            "01 AsyncTask Executor",
            "02 bind service",
            "03 unbind service",
            "04 start service",
            "05 stop service",
            "06 PendingIntentNAlarmManager",
            "07 File Read",
            "08 Various Example",
            "09 BluetoothActivity",
    };
    private ListView mListView;
    private TextView mTextView;
    private Context mThisContext;
    private final String TAG = this.getClass().getSimpleName();
}