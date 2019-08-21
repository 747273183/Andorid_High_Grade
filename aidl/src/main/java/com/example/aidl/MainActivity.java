package com.example.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.service.IMyAidlInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_startService)
    Button btnStartService;
    @BindView(R.id.btn_stopService)
    Button btnStopService;
    @BindView(R.id.btn_bindService)
    Button btnBindService;
    @BindView(R.id.btn_unBindService)
    Button btnUnBindService;
    private ServiceConnection conn;
    private Intent intent;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        intent = new Intent("com.example.service.ACTION_MYSERVICE");
        intent.setPackage("com.example.service");

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IMyAidlInterface myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
                try {
                    myAidlInterface.showProgress();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ");
            }
        };
    }

    @OnClick({R.id.btn_startService, R.id.btn_stopService, R.id.btn_bindService, R.id.btn_unBindService})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startService:
                startService(intent);
                break;
            case R.id.btn_stopService:
                stopService(intent);
                break;
            case R.id.btn_bindService:
                bindService(intent,conn,BIND_AUTO_CREATE);
                break;
            case R.id.btn_unBindService:
                unbindService(conn);
                break;
        }
    }
}
