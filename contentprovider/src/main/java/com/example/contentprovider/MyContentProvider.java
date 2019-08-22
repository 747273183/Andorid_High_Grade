package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    public static final String TABLE = "info_tb";
    public static final int MATCHER_CODE = 1000;
    private SQLiteDatabase db;
    private UriMatcher matcher;

    private static final String TAG = "MyContentProvider";

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = matcher.match(uri);
        if (match==MATCHER_CODE)
        {
            return db.delete(TABLE,selection, selectionArgs);
        }
        else
        {
            Log.d(TAG, "delete: path不匹配");
        }
        return 0;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = matcher.match(uri);
        long id=0;
        if (match==MATCHER_CODE)
        {
          id = db.insert(TABLE, null, values);

        }
        else
        {
            Log.d(TAG, "insert: path不匹配");

        }

        return ContentUris.withAppendedId(uri,id);//返回新添加的数据id

    }

    @Override
    public boolean onCreate() {
        SQLiteOpenHelper helper=new SQLiteOpenHelper(getContext(),"mydb.db",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table "+TABLE+" (" +
                        "_id integer primary key autoincrement," +
                        "name varchar(8)," +
                        "gender varchar(2)," +
                        "age integer" +
                        ")");

                db.execSQL("insert into "+TABLE+" values(null,'张三','男',19)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        db = helper.getReadableDatabase();

        //路径匹配
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("com.wanglin.myprovider","/insert",MATCHER_CODE);
        matcher.addURI("com.wanglin.myprovider","/delete",MATCHER_CODE);
        matcher.addURI("com.wanglin.myprovider","/update",MATCHER_CODE);
        matcher.addURI("com.wanglin.myprovider","/query",MATCHER_CODE);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

            return  db.rawQuery("select * from "+TABLE+" order by  _id desc",null);


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = matcher.match(uri);
        if (match==MATCHER_CODE)
        {
            return db.update(TABLE,values,selection,selectionArgs);
        }
        else
        {
            Log.d(TAG, "update: path不匹配");
            return 0;
        }

    }
}
