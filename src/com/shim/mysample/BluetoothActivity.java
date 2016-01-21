package com.shim.mysample;

import com.shim.mysample.log.SLog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BluetoothActivity extends Activity {

    private BluetoothTask btTask;

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
                    btTask.connect(0);
                    break;
                case 1:
                    btTask.connect(1);
                    break;
                case 2:
                    btTask.write("Hello".getBytes());
                    break;
                case 3:
                    btTask.disconnect();
                    break;
                default:
                    break;
                }
            }
        });

        btTask = new BluetoothTask(mThisContext, mUiHandler);
    }

    private Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                mTextView.append(msg.obj + "\n");
                break;
            }
        }
    };

    private String[] mListStrings = {
            "00 connect 1",
            "01 connect 2",
            "02 send data",
            "03 disconnect",
    };
    private ListView mListView;
    private TextView mTextView;
    private Context mThisContext;
    private final String TAG = this.getClass().getSimpleName();
}