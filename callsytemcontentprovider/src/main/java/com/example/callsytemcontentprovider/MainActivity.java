package com.example.callsytemcontentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_readsms)
    Button btnReadsms;
    @BindView(R.id.btn_readcontact)
    Button btnReadcontact;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.btn_addcontact)
    Button btnAddcontact;
    private ContentResolver resolver;
    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        requestPermissions(new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS}, 1101);

    }

    @OnClick({R.id.btn_readsms, R.id.btn_readcontact,R.id.btn_addcontact})
    public void onClick(View view) {
        resolver = getContentResolver();
        switch (view.getId()) {
            case R.id.btn_readsms:

                //inbox   收件箱
                // sent     发件箱
                // draft     草稿箱
//                所有文件夹：content://sms/all
//            收件箱：content://sms/inbox
//            已发送：content://sms/sent
//            草稿：content://sms/draft
//            发件箱：content://sms/outbox
//            发送失败：content://sms/failed
//            排队消息:content://sms/queued
//            未送达：content://sms/undelivered
//
//            对话：content://sms/conversations
//                Uri uri=Uri.parse("content://sms/inbox");
//                Uri uri=Uri.parse("content://sms/sent");
                Uri uri = Uri.parse("content://sms/failed");
                Cursor cursor = resolver.query(uri, null, null, null, null);
                while (cursor.moveToNext()) {

                    String msg = "";
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        String body = cursor.getString(cursor.getColumnIndex("body"));
                        msg = address + body;
                    }
                    Log.d(TAG, msg);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                }
                cursor.close();
                break;
            case R.id.btn_readcontact:
                Cursor cursor1 = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                        null, null);

                while (cursor1.moveToNext())
                {
                    //姓名和手机号是在多个contentprovider中的
                    //先查询出姓名和_id
                    String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String _id = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    //再根据id查询出number
                    String selection= ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?";
                   Cursor cursor2= resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            selection,
                            new String[]{_id},
                            null);
                   while (cursor2.moveToNext())
                   {
                       String number = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                       name+="-->"+number;

                   }
                   cursor2.close();
                    Log.d(TAG, "onClick: "+name);
                }
                cursor1.close();
                break;
            case R.id.btn_addcontact:
                addContact("孙桂红","13733555566");
                break;
        }
    }
    // 一个添加联系人信息的例子
    public void addContact(String name, String phoneNumber) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);

        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        //清空
        values.clear();
        // 联系人的电话号码
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);


        Toast.makeText(this, "联系人数据添加成功", Toast.LENGTH_SHORT).show();
    }


}
