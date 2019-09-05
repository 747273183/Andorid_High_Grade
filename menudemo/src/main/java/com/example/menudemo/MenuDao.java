package com.example.menudemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by Para_Huang on 1/15/2017.
 */

public class MenuDao extends SQLiteOpenHelper{

    private static SQLiteDatabase db;

    /**
     * 获取数据库操作对象的单例对象
     * @param context
     * @return
     */
    public static SQLiteDatabase getInstance(Context context){
        if(db == null){
            db = new MenuDao(context).getReadableDatabase();
        }
        return db;
    }

    private MenuDao(Context context){
        //super(context, Environment.getExternalStorageDirectory() + "/imooc_menu.db",null,1);
        //super(context, context.getExternalFilesDir("imooc")+File.separator+"imooc_menu.db",null,1);
        super(context, context.getFilesDir()+ File.separator+"imooc_menu.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
