package com.example.socket.tcp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MsgPool {

    private static MsgPool sInstance=new MsgPool();
    private LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<>();//消息队列
    private List<MsgComingListener> msgComingListeners=new ArrayList<>();//消息监听器

    public static MsgPool getInstance()
    {
        return  sInstance;
    }
    private MsgPool()
    {

    }

    //开启子线程发送从队列中取出的消息
    public void start()
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true)
                    {
                        String msg = queue.take();
                        notifyMsgComing(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //向所有socket转发消息
    private void notifyMsgComing(String msg) {
        for (MsgComingListener msgComingListener:msgComingListeners)
        {
            msgComingListener.onMsgComing(msg);
        }
    }


    public interface MsgComingListener
    {
        void onMsgComing(String msg);
    }

    //添加事件监听
    public void addMsgComingListener(MsgComingListener msgComingListener)
    {
        msgComingListeners.add(msgComingListener);
    }

    public void sendMsg(String msg)
    {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
