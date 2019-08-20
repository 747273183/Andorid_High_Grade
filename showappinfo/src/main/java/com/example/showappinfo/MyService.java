package com.example.showappinfo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.showappinfo.bean.App;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {



    private List<App> apps;
    private static final String TAG = "MyService";
    private Intent intent;
    private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 服务创建成功");


    }

    @Override
    public int onStartCommand(Intent intent1, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 服务开始运行 开启子线程获得系统app信息");
        apps=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager pm = getPackageManager();
                List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                for(PackageInfo packageInfo:pakageinfos)
                {
                    //获取应用程序的快捷方式图标
                    Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
                    // 获取应用程序的名称，不是包名，而是清单文件中的labelname
                    String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();

                    App app=new App(drawable,str_name);

                    apps.add(app);
                }

                //发送本地广播,通知MainActivity数据查询完成
                Log.d(TAG, "onStartCommand run: 数据查询服务完成,已发送本地广播");
                intent = new Intent("com.example.showappinfo.action.QUERY_COMPLETE");
                broadcastManager = LocalBroadcastManager.getInstance(MyService.this);
                broadcastManager.sendBroadcast(intent);

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 服务被销毁了");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind:绑定服务成功 "+apps.size());
        return  new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: 解绑服务成功");
        return super.onUnbind(intent);
    }

    class MyBinder extends Binder
    {
        public List<App> getApps(String name)
        {
            if (name!=null && name.length()>0)
            {
               List<App>  appsPart=new ArrayList<>();
               for (App app:apps)
               {
                   //查询不区分大小写
                   if (app.getName().toLowerCase().contains(name.toLowerCase()))
                   {
                       appsPart.add(app);
                   }
               }
               Log.d(TAG, "调用服务中在方法-按名称查询:"+appsPart.size());
               return appsPart;
            }
            else
            {
                Log.d(TAG, "调用服务中在方法-查询全部:"+apps.size());
                return apps;
            }


        }
    }


}
