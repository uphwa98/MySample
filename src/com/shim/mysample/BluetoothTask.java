package com.shim.mysample;

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

public class BluetoothTask {
    private final String TAG = this.getClass().getSimpleName();

    private final UUID MY_UUID_SECURE = UUID.fromString("a49eb41e-cb06-495c-9f4f-bb80a90cdf00");
    private final String REMOTE_NAME = "";
    private final int MAX_PAYLOAD_LEN = 1024;

    private Context mContext;
    private BluetoothManager mBtManager;
    private BluetoothAdapter mBtAdapter;
    private BluetoothDevice mRemoteDevice;
    private BluetoothSocket mSocket;
    private OutputStream mOutStream;

    public BluetoothTask(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mBtManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = mBtManager.getAdapter();
    }

    public void connect() {
        Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
        for (BluetoothDevice d : devices) {
            if (REMOTE_NAME.equals(d.getName())) {
                mRemoteDevice = d;
            }
        }

        try {
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mOutStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new ReadThread(mSocket).start();
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
            }

            SLog.d(TAG, "[" + new String(buf, 0, readLen) + "]");
        }
    }
}
