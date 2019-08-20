package com.example.showappinfo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.showappinfo.adapter.MyListViewBaseAdapter;
import com.example.showappinfo.bean.App;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.lv)
    ListView lv;

    private MyService.MyBinder binder;
    private MyListViewBaseAdapter adapter;
    private Intent intent;
    private List<App> apps;
    private static final String TAG = "MainActivity";

    //创建服务连接,用于创建服务中的方法
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected:与后台服务建立连接成功 ");
            binder = (MyService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: 服务出现意外断开了连接");

        }
    };
    private MyReceiver receiver;
    private LocalBroadcastManager broadcastManager;
    private IntentFilter filter;

    //创建广播接收器用于接收服务中的查询完成的消息,接收到这个广播后再去在listview中显示数据
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

                apps = binder.getApps(null);



            Log.d(TAG, "onReceive:接收到后台服务的广播数据 " + apps.size());
            //使用listView显示数据
            if (apps != null && apps.size() >= 0) {
                adapter = new MyListViewBaseAdapter(MainActivity.this, apps);
                lv.setAdapter(adapter);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 开启服务(为了让服务长驻留内存),绑定服务(为了调用服务中的方法)");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);

        //搜索
        searchView.setQueryHint("输入应用名称回车");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                apps = binder.getApps(query);
                Log.d(TAG, "onQueryTextSubmit:查询文本提交 "+apps.size());

                if (apps != null && apps.size() >= 0) {
                    adapter = new MyListViewBaseAdapter(MainActivity.this, apps);
                    lv.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                apps = binder.getApps(newText);
                Log.d(TAG, "onQueryTextChange: 查询文本改变"+apps.size());

                if (apps != null && apps.size() >= 0) {
                    adapter = new MyListViewBaseAdapter(MainActivity.this, apps);
                    lv.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 注册本地广播,用于接收服务中查询出来的应用数据");

        receiver = new MyReceiver();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        filter = new IntentFilter("com.example.showappinfo.action.QUERY_COMPLETE");
        broadcastManager.registerReceiver(receiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop:注销广播,以免耗电");
        broadcastManager.unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy:解绑服务,以免造成内存泄露 ");
        unbindService(connection);

    }
}
