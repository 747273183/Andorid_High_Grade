package com.example.contentresolver;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String URI = "content://com.wanglin.myprovider";
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_woman)
    RadioButton rbWoman;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_del)
    Button btnDel;
    @BindView(R.id.lv)
    ListView lv;
    private ContentResolver resolver;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    private static final String TAG = "MainActivity";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //获得内容解析器
        resolver=getContentResolver();

        //给listview设置适配器,让listview显示数据
        cursor = resolver.query(Uri.parse(URI), null, null, null);
        Log.d(TAG, "onCreate: "+cursor.getCount());
        String[] from={"_id","name","gender","age"};
        int[] to={R.id.tv_id,R.id.tv_name,R.id.tv_gender,R.id.tv_age};
        adapter = new SimpleCursorAdapter(this,R.layout.item_listiview,
                cursor,from,to,SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);


    }

    @OnClick({R.id.btn_add, R.id.btn_update, R.id.btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:

                break;
            case R.id.btn_update:
                break;
            case R.id.btn_del:
                break;
        }
    }
}
