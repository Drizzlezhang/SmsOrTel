package com.drizzle.app.smsortel.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.drizzle.app.smsortel.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/17.
 */
public class MyAdapter extends BaseExpandableListAdapter {
    private List<String> parentList=null;
    private Map<String,List<String>> map=null;
    private Context mContext;

    public MyAdapter(Context mContext,Map<String,List<String>> map, List<String> parentList) {
        this.mContext = mContext;
        this.map=map;
        this.parentList=parentList;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String key=parentList.get(groupPosition);
        return map.get(key).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key=parentList.get(groupPosition);
        return (map.get(key).get(childPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater1=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater1.inflate(R.layout.parent_layout,null);
        }
        TextView textView=(TextView) convertView.findViewById(R.id.parent_textview);
        textView.setText(parentList.get(groupPosition));
        return convertView;


    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String string=parentList.get(groupPosition);
        String info=map.get(string).get(childPosition);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.child_layout,null);
        }
        TextView textView1=(TextView) convertView.findViewById(R.id.child_textview);
        textView1.setText(info);
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
