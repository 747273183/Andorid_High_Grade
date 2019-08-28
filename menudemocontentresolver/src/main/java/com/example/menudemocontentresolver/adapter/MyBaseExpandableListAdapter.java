package com.example.menudemocontentresolver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menudemocontentresolver.R;
import com.example.menudemocontentresolver.bean.Menu;
import com.example.menudemocontentresolver.bean.MenuType;

import java.util.List;

public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MenuType> menuTypes;
    private LayoutInflater inflater;

    public MyBaseExpandableListAdapter(Context context, List<MenuType> menuTypes) {
        this.context = context;
        this.menuTypes = menuTypes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return menuTypes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menuTypes.get(groupPosition).getMenus().size();
    }

    @Override
    public MenuType getGroup(int groupPosition) {
        return menuTypes.get(groupPosition);
    }

    @Override
    public Menu getChild(int groupPosition, int childPosition) {
        return menuTypes.get(groupPosition).getMenus().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return menuTypes.get(groupPosition).getMenus().get(childPosition).getMenuId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder=null;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_parent_chapter,parent,false);
            parentViewHolder=new ParentViewHolder();
            parentViewHolder.tv_name=convertView.findViewById(R.id.tv_name);
            parentViewHolder.iv_indicator=convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(parentViewHolder);
        }
        else
        {
            parentViewHolder= (ParentViewHolder) convertView.getTag();
        }

        MenuType menuType = menuTypes.get(groupPosition);
        parentViewHolder.tv_name.setText(menuType.getTypeName());
        parentViewHolder.iv_indicator.setImageResource(R.drawable.group_indicator_bg);
        parentViewHolder.iv_indicator.setSelected(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder=null;
        if (convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.item_child_chapter,parent,false);
            childViewHolder=new ChildViewHolder();
            childViewHolder.tv_name=convertView.findViewById(R.id.tv_name);
            convertView.setTag(childViewHolder);
        }
        else
        {
            childViewHolder= (ChildViewHolder) convertView.getTag();
        }

        Menu menu = menuTypes.get(groupPosition).getMenus().get(childPosition);
        childViewHolder.tv_name.setText(menu.getMenuName());
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private  static  class ParentViewHolder
    {
        TextView tv_name;
        ImageView iv_indicator;
    }

    private  static  class ChildViewHolder
    {
        TextView tv_name;
    }
}
