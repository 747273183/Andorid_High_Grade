package com.imooc.udpdemo.utils.tcp.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhanghongyang01 on 17/4/23.
 */

public class MsgPool {

    private MsgPool() {
    }

    private void notifyMsgComing(String msg) {
        List<MsgComingListener> diedListeners = new ArrayList<>();

        for (MsgComingListener listener : mListeners) {
            try {
                listener.onMsgComing(msg);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage() + "==> remove " + listener);
                diedListeners.add(listener);
            }
        }
        mListeners.removeAll(diedListeners);

    }

    private static MsgPool sInstance = new MsgPool();

    public static MsgPool getInstance() {
        return sInstance;
    }

    public void sendMsg(String msg) {
        try {
            mQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private LinkedBlockingQueue<String> mQueue = new LinkedBlockingQueue<>();

    public void start() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String msg = mQueue.take();
                        notifyMsgComing(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    public interface MsgComingListener {
        void onMsgComing(String msg) throws IOException;
    }

    private List<MsgComingListener> mListeners = new ArrayList<>();

    public void addMsgComingListener(MsgComingListener listener) {
        mListeners.add(listener);
    }


}
