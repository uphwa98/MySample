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
                    mTts.speak("Hello", TextToSpeech.QUEUE_FLUSH, null, null);
                    break;
                case 1:
                    readFile("English_ABCD.txt");
                    break;
                case 2:
                    readFile("English_EFGH.txt");
                    break;
                case 3:
                    File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    SLog.v(TAG, "DOWNLOADS : " + mediaStorageDir);
                    for (String file : mediaStorageDir.list()) {
                        SLog.v(TAG, "fileName : " + file);
                    }
                    break;
                case 4:
                    File dir = Environment.getExternalStorageDirectory();
                    SLog.v(TAG, "getExternalStorageDirectory : " + dir);
                    for (String file : dir.list()) {
                        SLog.v(TAG, "fileName : " + file);
                    }
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

    public void readFile(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, Environment.DIRECTORY_DOWNLOADS + "/" + fileName);
                    SLog.v(TAG, "path : " + file.getPath());

                    BufferedReader in = new BufferedReader(new FileReader(file));
                    String s;

                    while ((s = in.readLine()) != null) {
                        SLog.v(TAG, "file : " + s);
                        mTts.speak(s, TextToSpeech.QUEUE_ADD, null, null);
                        try {
                            Thread.sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    in.close();

                } catch (IOException e) {
                    SLog.v(TAG, "IOException");
                }
            }
        }).start();
    }

    private String[] mListStrings = {
            "00 test TTS",
            "01 Listening ABCD",
            "02 Listening EFGH",
            "03 getExternalStoragePublicDirectory",
            "04 getExternalStorageDirectory",
            
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