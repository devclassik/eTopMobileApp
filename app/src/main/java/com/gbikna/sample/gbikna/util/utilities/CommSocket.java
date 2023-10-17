package com.gbikna.sample.gbikna.util.utilities;


import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class CommSocket {
    private static final String TAG = CommSocket.class.getSimpleName();

    private SocketChannel mSocketChannel;
    private Selector mSelector;
    private boolean mIsGoOn = true;

    public boolean open(String host, String port) {
        if (port == null) return false;
        int intPort = Integer.parseInt(port);
        Log.i(TAG, "ABOUT OPENING PORT");
        return open(host, intPort);
    }

    public void setStop() {
        mIsGoOn = false;
    }

    public boolean open(String host, int port) {
        Log.d(TAG, "host="+host+", port="+port);
        if (TextUtils.isEmpty(host) || port <= 0) {
            Log.i(TAG, "host or port error.");
            return false;
        }
        try {
            mSocketChannel = SocketChannel.open();
            mSocketChannel.configureBlocking(false);
            long mEndtTime = System.currentTimeMillis() + 5000;
            mSocketChannel.connect(new InetSocketAddress(host, port));
            while(!mSocketChannel.finishConnect() && (System.currentTimeMillis() <= mEndtTime)){}
            if(System.currentTimeMillis() > mEndtTime){
                Log.i(TAG, "TIME ERROR WHILE OPENING PORT");
                return false;
            }
            mSelector = Selector.open();
            mSocketChannel.register(mSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "EXCEPTION WHILE OPENING PORT");
            return false;
        }
        Log.i(TAG, "DONE OPENING PORT");
        return true;
    }


    public int send(byte[] sendPacket) {
        //Log.i(TAG, "send = "+BCDASCII.bytesToHexString(sendPacket));
        Log.i(TAG, "ABOUT SENDING BYTES");
        int count = 0;
        try {
            mIsGoOn = true;
            //TODO:
            long mEndtTime = System.currentTimeMillis() + 60000;
            while (mIsGoOn && mSelector.select() > 0
                    && (System.currentTimeMillis() <= mEndtTime)) {

                if(System.currentTimeMillis() > mEndtTime){
                    Log.i(TAG, "TIME OUT WHILE SENDING BYTES");
                    return 0;
                }

                Iterator it = mSelector.selectedKeys().iterator();
                while (mIsGoOn && it.hasNext()) {
                    SelectionKey sk = (SelectionKey) it.next();
                    it.remove();
                    if (sk.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) sk.channel();
                        ByteBuffer bb = ByteBuffer.wrap(sendPacket, 0, sendPacket.length);
                        Log.i(TAG, "LENGTH OF SENT: " + sendPacket.length);
                        count = socketChannel.write(bb);
                        Log.d(TAG, "Send ok.");
                        return count;
                    }
                    mSelector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "EXCEPTION HAPPENED WHILE SENDING BYTES");
            return -1;
        }
        Log.i(TAG, "SENT: " + count);
        return count;
    }

    public byte[] recv() {
        byte[] receive = null;
        int count = 0;
        Log.i(TAG, "ABOUT RECEIVING BYTES");
        try {
            mIsGoOn = true;
            //TODO:
            long mEndtTime2 = System.currentTimeMillis() + 60000;
            while (mIsGoOn && mSelector.select() > 0
                    && (System.currentTimeMillis() <= mEndtTime2)) {
                Iterator it = mSelector.selectedKeys().iterator();
                if(System.currentTimeMillis() > mEndtTime2){
                    Log.i(TAG, "A: TIME OUT WHILE RECEIVING BYTES");
                    return null;
                }

                long mEndtTime = System.currentTimeMillis() + 60000;
                while (mIsGoOn && it.hasNext()
                        && (System.currentTimeMillis() <= mEndtTime)) {

                    if(System.currentTimeMillis() > mEndtTime){
                        Log.i(TAG, "TIME OUT WHILE RECEIVING BYTES");
                        return null;
                    }

                    SelectionKey sk = (SelectionKey) it.next();
                    it.remove();
                    if (sk.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) sk.channel();
                        ByteBuffer bb = ByteBuffer.allocate(ISO8583.MAXBUFFERLEN);
                        bb.clear();
                        count = socketChannel.read(bb);
                        bb.flip();
                        if (count>0) {
                            receive = new byte[count];
                            System.arraycopy(bb.array(), 0, receive, 0, count);
                            //Log.i(TAG, "RECV: " + socketChannel.socket().getRemoteSocketAddress() + ": " + BCDASCII.bytesToHexString(receive));
                            Log.i(TAG, "DONE RECEIVING BYTES");
                            return receive;
                        } else {
                            Log.i(TAG, "CHECKING... A");
                            Log.i(TAG, "COULD NOT RECEIVE BYTES");
                            return null;
                        }
                    }
                    mSelector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException e) {
            Log.i(TAG, "EXCEPTION OCCURRED WHILE RECEIVING BYTES");
            e.printStackTrace();
        }
        Log.i(TAG, "RECEIVED SUCCESSFULLY");
        return receive;
    }

    public void close() {
        Log.i(TAG, "ABOUT CLOSING SOCKET");
        try {
            if (mSocketChannel != null && mSocketChannel.isConnected()) {
                mSocketChannel.finishConnect();
                mSelector.close();
                mSocketChannel.close();
                Log.i(TAG, "SOCKET CLOSED");
            }
        } catch (IOException e) {
            Log.i(TAG, "COUNLD NOT CLOSE SOCKET");
            e.printStackTrace();
        }
    }
}

