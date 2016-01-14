package com.shim.mysample;

import com.shim.mysample.log.SLog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PendingIntentNAlarmManager extends Activity {

    static AlarmManager mAlarmManager;
    private PendingIntent mPendingIntentService;
    private static PendingIntent mPendingIntentBroadcast;
    private PendingIntent mPendingIntentActivity;

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
                    break;
                case 1:
                    long firstTime = SystemClock.elapsedRealtime();
                    SLog.v(TAG, "position : " + firstTime);
                    mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 5 * 1000,
                                               mPendingIntentService);
                    break;
                case 2:
                    SLog.v(TAG, "cancel : ");
                    mAlarmManager.cancel(mPendingIntentService);
                    break;
                case 3:
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, mPendingIntentService);
                    break;
                case 4:
                    mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000,
                                           mPendingIntentService);
                    break;
                case 5:
                    break;
                case 6:
                    long firstTime1 = SystemClock.elapsedRealtime();
                    SLog.v(TAG, "position : " + firstTime1);
                    mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime1, 5 * 1000,
                                               mPendingIntentBroadcast);
                    break;
                case 7:
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, mPendingIntentBroadcast);
                    break;
                case 8:
                    mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000,
                                           mPendingIntentBroadcast);
                    break;
                default:
                    break;
                }
            }
        });

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mPendingIntentService = PendingIntent.getService(mThisContext, 1000,
                                                         new Intent(mThisContext, AlarmService.class), 0);
        mPendingIntentBroadcast = PendingIntent.getBroadcast(mThisContext, 1001,
                                                             new Intent(mThisContext, AlarmBroadcast.class), 0);
        mPendingIntentActivity = PendingIntent.getActivity(mThisContext, 1002,
                                                           new Intent(mThisContext, AlarmActivity.class), 0);
    }

    public static class AlarmService extends Service {

        public class LocalBinder extends Binder {
            AlarmService getService() {
                return AlarmService.this;
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

    public static class AlarmBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SLog.i("AlarmBroadcast", "onReceive() action : " + intent.getAction());
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, mPendingIntentBroadcast);
        }
    }

    public static class AlarmActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            SLog.v("AlarmActivity", "onCreate()");
        }
    }

    private String[] mListStrings = {
            "Service",
            "01 setRepeating ELAPSED_REALTIME_WAKEUP",
            "02 cancel Alarm",
            "03 set RTC_WAKEUP",
            "04 setExact RTC_WAKEUP",
            "BroadCast",
            "06 setRepeating ELAPSED_REALTIME_WAKEUP",
            "07 set RTC_WAKEUP",
            "08 setExact RTC_WAKEUP",
    };
    private ListView mListView;
    private TextView mTextView;
    private Context mThisContext;
    private final String TAG = this.getClass().getSimpleName();
}