package com.example.menudemo;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final String SDKF = "sdkf";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateDatabase();

       /*查看测试数据*/
        SQLiteDatabase db = MenuDao.getInstance(this);
        Cursor c = db.query("dish_tb",null,null,null,null,null,null);
        StringBuilder sb=new StringBuilder();
        while(c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String type = c.getString(2);
            sb.append(name+",");
            Log.e("TAG",name);
        }
        TextView tv= (TextView) this.findViewById(R.id.menu_name);
        tv.setText(sb.toString());
    }

    public void generateDatabase(){
       try {
            InputStream in = getAssets().open("menu.db");
            byte[] b = new byte[512];
            int len = 0;

            File f = new File(getFilesDir()+File.separator + "imooc_menu.db");
            FileOutputStream fos = new FileOutputStream(f);
            while ((len = in.read(b))!=-1){
                fos.write(b,0,len);
            }

            fos.close();
            in.close();
           Log.d(SDKF,"成功："+f.getAbsolutePath());
        } catch (IOException e) {
           Log.d(SDKF,"出错了:"+e.getMessage());
            e.printStackTrace();
        }
    }

}
