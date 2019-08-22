package com.example.contentresolver;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contentresolver.bean.Person;

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
    private Person person;
    private long id;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //获得内容解析器
        resolver=getContentResolver();

        //给listview设置适配器,让listview显示数据
        cursor = resolver.query(Uri.parse(URI+"/query"), null, null, null);
        Log.d(TAG, "onCreate: "+cursor.getCount());
        String[] from={"_id","name","gender","age"};
        int[] to={R.id.tv_id,R.id.tv_name,R.id.tv_gender,R.id.tv_age};
        adapter = new SimpleCursorAdapter(this,R.layout.item_listiview,
                cursor,from,to,SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);

        //list列表项被单击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Cursor cursor= (Cursor) parent.getItemAtPosition(position);
                int _id = cursor.getInt(0);
                String name = cursor.getString(1);
                String gender = cursor.getString(2);
                int age = cursor.getInt(3);

                tvId.setText(_id+"");
                etName.setText(name);
                if ("男".equals(gender))
                {
                    rbMan.setChecked(true);
                }
                else
                {
                    rbWoman.setChecked(true);
                }

                etAge.setText(age+"");

                MainActivity.this.id=_id;
            }
        });


    }

    @OnClick({R.id.btn_add, R.id.btn_update, R.id.btn_del})
    public void onClick(View view) {
        String name = etName.getText().toString();
        RadioButton radioButton=  rgSex.findViewById( rgSex.getCheckedRadioButtonId());
        String gender = radioButton.getText().toString();
        Integer age =new Integer(etAge.getText().toString()) ;
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("gender",gender);
        values.put("age",age);


        switch (view.getId()) {
            case R.id.btn_add:
                Uri uri = resolver.insert(Uri.parse(URI+"/insert"), values);
                long id = ContentUris.parseId(uri);
                Toast.makeText(this, "添加成功,新数据id="+id, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_update:
                values.put("_id", this.id +"");
                int update = resolver.update(Uri.parse(URI+"/insert"), values, "_id=?", new String[]{this.id + ""});
                Toast.makeText(this, "修改成功,受影响的记录行数="+update, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_del:
                int delete = resolver.delete(Uri.parse(URI+"/insert"), "_id=?", new String[]{this.id + ""});
                Toast.makeText(this, "删除成功,受影响的记录行数="+delete, Toast.LENGTH_SHORT).show();
                break;
        }
        adapter.notifyDataSetChanged();

    }
}
