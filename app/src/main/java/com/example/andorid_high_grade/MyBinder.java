package com.example.andorid_high_grade;

import android.os.Binder;
import android.util.Log;

//服务中的方法
public class MyBinder extends Binder {
    private static final String TAG = "MyBinder";
    public void taskA() {
        Log.d(TAG, "taskA: ");
    }

    public void taskB() {
        Log.d(TAG, "taskB: ");
    }
}
