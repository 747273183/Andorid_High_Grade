package com.example.menudemocontentresolver;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menudemocontentresolver.adapter.MyBaseExpandableListAdapter;
import com.example.menudemocontentresolver.bean.Menu;
import com.example.menudemocontentresolver.bean.MenuType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String URI = "content://com.imooc.menuprovider";
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.eListView)
    ExpandableListView eListView;
    private ContentResolver contentResolver;
    private ExpandableListAdapter adapter;
    private List<MenuType> menuTypes;
    private ArrayList<String> strMenuTypes;

    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //查询
        contentResolver = getContentResolver();

    }

    @Override
    protected void onStart() {
        super.onStart();
        showList();

        //长按子列表提示是否删除
        eListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Log.d(TAG, "onItemLongClick: "+(item instanceof MenuType));
                Log.d(TAG, "onItemLongClick: "+(item instanceof Menu));
                if (item instanceof Menu)
                {
                    final Menu menu= (Menu) item;
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("警告!");
                    builder.setMessage("确定要删除:"+menu.toString()+"吗?");

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int delete = contentResolver.delete(Uri.parse(URI), null, new String[]{menu.getMenuId() + ""});
                            if (delete>0)
                            {
                                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                //刷新
                                showList();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }
                return true;
            }
        });
    }

    private void showList() {
        Cursor cursor = contentResolver.query(Uri.parse(URI), null,
                null, null, null);
        //封装
        menuTypes = new ArrayList<>();
        //先得到不重复的菜品分类
        strMenuTypes = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();
        while (cursor.moveToNext()) {
            int menuId = cursor.getInt(0);
            String menuName = cursor.getString(1);
            String strMenuType = cursor.getString(2);

            if (!strMenuTypes.contains(strMenuType)) {
                strMenuTypes.add(strMenuType);
            }
            menus.add(new Menu(menuId, menuName, strMenuType));
        }

        //再添加每个菜品分类下的菜品
        for (int i = 0; i < strMenuTypes.size(); i++) {
            List<Menu> menuList = new ArrayList<>();
            MenuType menuType = new MenuType(strMenuTypes.get(i), menuList);
            for (int j = 0; j < menus.size(); j++) {
                if (strMenuTypes.get(i).equals(menus.get(j).getType())) {
                    menuList.add((menus.get(j)));
                }
            }
            menuTypes.add(menuType);

        }
        adapter = new MyBaseExpandableListAdapter(this, menuTypes);
        eListView.setAdapter(adapter);
    }

    @OnClick(R.id.tv_add)
    public void onClick() {
        Intent intent = new Intent(this, AddActivity.class);
        intent.putStringArrayListExtra("menuTypes", strMenuTypes);
        startActivity(intent);
    }
}
