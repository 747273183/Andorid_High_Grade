package com.example.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.btn_bind)
    Button btnBind;
    @BindView(R.id.btn_unbind)
    Button btnUnbind;

    private static final String TAG = "MyService-1";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ServiceConnection conn;
    private MyService.MyBinder myBinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (MyService.MyBinder) service;

                //利用订时器订时获得服务中的进度
                //第一个参数是订时器总的运行时间,第二个参数是订时器间隔调用时间
                CountDownTimer countDownTimer = new CountDownTimer(100000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int progress = myBinder.getProgress();
                        progressBar.setProgress(progress);

                    }

                    @Override
                    public void onFinish() {
                        progressBar.setProgress(100);
                    }
                };

                countDownTimer.start();


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ");
            }
        };





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @OnClick({R.id.btn_start, R.id.btn_stop, R.id.btn_bind, R.id.btn_unbind})
    public void
    onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                Intent it1 = new Intent(MainActivity.this, MyService.class);
                startService(it1);
                break;
            case R.id.btn_stop:
                Intent it2 = new Intent(MainActivity.this, MyService.class);
                stopService(it2);
                break;
            case R.id.btn_bind:
                Intent it3 = new Intent(MainActivity.this, MyService.class);
                bindService(it3, conn, BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind:
                unbindService(conn);
                break;
        }
    }
}
