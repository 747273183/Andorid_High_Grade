package com.example.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
    private Intent intent;
    private IMyAidlInterface myAidlInterface;
    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        intent = new Intent(this,MyService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
                try {
                    myAidlInterface.showProgress();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

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
