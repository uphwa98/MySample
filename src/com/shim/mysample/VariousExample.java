package com.shim.mysample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.shim.mysample.log.SLog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class VariousExample extends Activity {
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
                    DateFormat[] formats = new DateFormat[] {
                            DateFormat.getDateInstance(),
                            DateFormat.getDateTimeInstance(),
                            DateFormat.getTimeInstance(),
                    };

                    for (DateFormat df : formats) {
                        SLog.v(TAG, "default : " + df.format(new Date(0)));

                        df.setTimeZone(TimeZone.getTimeZone("UTC"));
                        SLog.v(TAG, "UTC : " + df.format(new Date(0)));

                        df.setTimeZone(TimeZone.getTimeZone("GMT+09"));
                        SLog.v(TAG, "GMT+09 : " + df.format(new Date(0)));
                    }
                    break;
                case 1:
                    String DATE_FORMAT = "MM-dd HH:mm:ss.SSS";
                    long curTime = System.currentTimeMillis();
                    Date date = new Date(curTime);
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.KOREA);
                    SLog.v(TAG, "SimpleDateFormat : " + sdf.format(date));
                    break;
                default:
                    break;
                }
            }
        });
    }

    private String[] mListStrings = {
            "00 DateFormat",
            "01 SimpleDateFormat",
            "02 bind service",
            "03 unbind service",
            "04 start service",
            "05 stop service",
    };
    private ListView mListView;
    private TextView mTextView;
    private Context mThisContext;
    private final String TAG = this.getClass().getSimpleName();
}