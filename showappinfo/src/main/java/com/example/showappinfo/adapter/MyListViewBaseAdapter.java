package com.example.showappinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.showappinfo.R;
import com.example.showappinfo.bean.App;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyListViewBaseAdapter extends BaseAdapter {
    private Context context;
    private List<App> apps;
    private LayoutInflater inflater;

    public MyListViewBaseAdapter(Context context, List<App> apps) {
        this.context = context;
        this.apps = apps;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public App getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        App app = getItem(position);
        viewHolder.ivIcon.setImageDrawable(app.getIcon());
        viewHolder.tvName.setText(app.getName());



        return convertView;
    }


    static
    class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
