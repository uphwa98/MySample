package com.shim.mysample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.shim.mysample.log.SLog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FileRead extends Activity implements TextToSpeech.OnInitListener {
    TextToSpeech mTts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SLog.v(TAG, "onCreate()");

        mTts = new TextToSpeech(this, this);

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
                    mTts.speak("Hello", TextToSpeech.QUEUE_ADD, null, null);
                    break;
                case 1:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            readFile();
                        }
                    }).start();
                    break;
                default:
                    break;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    public void readFile() {
        try {
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, Environment.DIRECTORY_DOWNLOADS + "/English.txt");
            SLog.v(TAG, "path : " + file.getPath());

            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;

            while ((s = in.readLine()) != null) {
                SLog.v(TAG, "file : " + s);
                mTts.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            in.close();
            
        } catch (IOException e) {
            SLog.v(TAG, "IOException");
        }
    }

    private String[] mListStrings = {
            "00 test",
            "01 Listening",
    };
    private ListView mListView;
    private TextView mTextView;
    private Context mThisContext;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onInit(int status) {
        SLog.v(TAG, "onInit : " + status);
    }
}