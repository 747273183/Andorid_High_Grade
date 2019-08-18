package com.example.andorid_high_grade;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    private Intent service;

    private static final String TAG = "MyService-1";
    private ServiceConnection conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        service = new Intent(MainActivity.this,MyService.class);


    }

    @OnClick({R.id.btn_start, R.id.btn_stop, R.id.btn_bind, R.id.btn_unbind})
    public void
    onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startService(service);
                break;
            case R.id.btn_stop:
                stopService(service);
                break;
            case R.id.btn_bind:

                break;
            case R.id.btn_unbind:

                break;
        }
    }
}
