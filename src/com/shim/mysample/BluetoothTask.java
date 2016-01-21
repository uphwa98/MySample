package com.shim.mysample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import com.shim.mysample.log.SLog;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

public class BluetoothTask {
    private final String TAG = this.getClass().getSimpleName();

    private final UUID MY_UUID_SECURE = UUID.fromString("a49eb41e-cb06-495c-9f4f-bb80a90cdf00");
    private final UUID MY_UUID_SECURE1 = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
//    private final String REMOTE_NAME = "SHV-E210L";
    private final String REMOTE_NAME = "SGH-I337";
    private final int MAX_PAYLOAD_LEN = 1024;

    private Handler mHandler;
    private Context mContext;
    private BluetoothManager mBtManager;
    private BluetoothAdapter mBtAdapter;
    private BluetoothDevice mRemoteDevice;
    private BluetoothSocket mSocket;
    private OutputStream mOutStream;
    private FileOutputStream output;

    public BluetoothTask(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        init();
    }

    private void init() {
        mBtManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = mBtManager.getAdapter();
    }

    public void connect(int type) {
        Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
        for (BluetoothDevice d : devices) {
            if (REMOTE_NAME.equals(d.getName())) {
                mRemoteDevice = d;
            }
        }

        try {
            if (type == 0)
                mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            else
                mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            SLog.sendMessageToUi(mHandler, 0, "Connection failed..");
            return;
        }

        File download =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/temp.txt");
        try {
            output = new FileOutputStream(download);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        SLog.sendMessageToUi(mHandler, 0, "Connected..");

        try {
            mOutStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new ReadThread(mSocket).start();
    }

    public void disconnect() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] buf) {
        try {
            mOutStream.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReadThread extends Thread {
        private BluetoothSocket mSocket;
        private InputStream mInStream;

        public ReadThread(BluetoothSocket socket) {
            mSocket = socket;
        }

        public void run() {
            try {
                mInStream = mSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    mSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            int readLen = -1;
            byte[] buf = new byte[MAX_PAYLOAD_LEN];
            while (true) {
                try {
                    readLen = mInStream.read(buf, 0, MAX_PAYLOAD_LEN);
                } catch (IOException e) {
                    e.printStackTrace();

                    try {
                        mSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }

                String str = new String(buf, 0, readLen);
                SLog.d(TAG, "[" + str + "]");
                SLog.sendMessageToUi(mHandler, 0, str);

                try {
                    output.write(buf, 0, readLen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SLog.sendMessageToUi(mHandler, 0, "Disconnected..");
        }
    }
}
