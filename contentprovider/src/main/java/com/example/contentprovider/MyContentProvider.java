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

    private SQLiteDatabase db;
    private UriMatcher matcher;

    private static final String TAG = "MyContentProvider";

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext(), "mydb.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table " + TABLE + " (" +
                        "_id integer primary key autoincrement," +
                        "name varchar(8)," +
                        "gender varchar(2)," +
                        "age integer" +
                        ")");

                db.execSQL("insert into " + TABLE + " values(null,'张三','男',19)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        db = helper.getReadableDatabase();

        //路径匹配
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("com.wanglin.myprovider", "/insert", 1000);
        matcher.addURI("com.wanglin.myprovider", "/delete", 1001);
        matcher.addURI("com.wanglin.myprovider", "/update", 1002);
        matcher.addURI("com.wanglin.myprovider", "/query", 1003);
        matcher.addURI("com.wanglin.myprovider", "/query/byName", 1004);
        matcher.addURI("com.wanglin.myprovider", "/query/#", 1005);
        matcher.addURI("com.wanglin.myprovider", "/query/*", 1006);


        return true;
    }


    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        if (values.size()>0)
        {
            int match = matcher.match(uri);

            switch (match) {
                case 1000:
                    id = db.insert(TABLE, null, values);
                    break;
                default:
                    Log.d(TAG, "insert: path不匹配");
                    break;
            }
            return ContentUris.withAppendedId(uri, id);//返回新添加的数据id
        }
        else
        {
            String authority = uri.getAuthority();
            String path = uri.getPath();
            String query = uri.getQuery();
            String name = uri.getQueryParameter("name");
            String gender = uri.getQueryParameter("gender");
            String age = uri.getQueryParameter("age");
            Log.d(TAG, "authority="+authority+",path="+path+"query="+query+",name="+name+",gender="+gender+",age="+age);
            values.put("name",name);
            values.put("gender",gender);
            values.put("age",age);
            id = db.insert(TABLE, null, values);
            return ContentUris.withAppendedId(uri, id);//返回新添加的数据id
        }


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = matcher.match(uri);

        switch (match) {
            case 1001:
                return db.delete(TABLE, selection, selectionArgs);

            default:
                Log.d(TAG, "delete: path不匹配");
                return 0;

        }


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = matcher.match(uri);


        switch (match) {
            case 1002:
                return db.update(TABLE, values, selection, selectionArgs);

            default:
                Log.d(TAG, "update: path不匹配");
                return 0;


        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = matcher.match(uri);


        switch (match)
        {
            case 1003:
                Log.d(TAG, "匹配query");
                return db.rawQuery("select * from " + TABLE + " order by  _id desc", null);
            case 1004:
                Log.d(TAG, "匹配query/byName");
                return null;
            case 1005:
                Log.d(TAG, "匹配query/任意数字");
                return null;
            case 1006:
                Log.d(TAG, "匹配query/任意字符");
                return null;
            default:
                Log.d(TAG, "query:没有配置的uri");
                return null;
        }

    }

}
