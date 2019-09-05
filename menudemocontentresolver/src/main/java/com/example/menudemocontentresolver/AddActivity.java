package com.example.menudemocontentresolver;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.sp_menutype)
    Spinner spMenutype;
    @BindView(R.id.et_menuname)
    EditText etMenuname;
    @BindView(R.id.btn_add)
    Button btnAdd;
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        ArrayList<String> menuTypes = intent.getStringArrayListExtra("menuTypes");

        spMenutype.setAdapter(new ArrayAdapter<>(this,R.layout.item_spinner,menuTypes));

        resolver = getContentResolver();

    }

    @OnClick({R.id.tv_back, R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                onBackPressed();
                break;
            case R.id.btn_add:
                String menuType = spMenutype.getSelectedItem().toString();
                String menuName = etMenuname.getText().toString();
                ContentValues values=new ContentValues();
                values.put("dish_type",menuType);
                values.put("dish_name",menuName);
                Uri insert = resolver.insert(Uri.parse(MainActivity.URI), values);
                long id = ContentUris.parseId(insert);
                if (id>0)
                {
                    Toast.makeText(this, "添加成功,id="+id, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
