package com.drizzle.app.smsortel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.drizzle.app.smsortel.model.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResourceActivity extends ActionBarActivity {
    ExpandableListView expandableListView=null;
    List<String> parentList=null;
    Map<String,List<String>> map=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        expandableListView=(ExpandableListView)findViewById(R.id.resource);
        initData();
        expandableListView.setAdapter(new MyAdapter(this, map, parentList));
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent=new Intent();
                intent.putExtra("sms",map.get(parentList.get(groupPosition)).get(childPosition));
                setResult(RESULT_OK,intent);
                finish();
                return true;
            }
        });


    }
    public void initData() {
        parentList = new ArrayList<String>();
        parentList.add("节日祝福");
        parentList.add("生日祝福");
        parentList.add("短信模板");
        parentList.add("爱情祝福");
        map = new HashMap<String, List<String>>();
        List<String> list1 = new ArrayList<String>();
        list1.add("中秋节快乐");
        list1.add("端午节快乐");
        list1.add("春节快乐");
        list1.add("国庆节快乐");
        map.put("节日祝福", list1);
        List<String> list2 = new ArrayList<String>();
        list2.add("生日快乐！");
        list2.add("福如东海，寿比南山。");
        map.put("生日祝福", list2);
        List<String> list3 = new ArrayList<String>();
        list3.add("开会中");
        list3.add("稍后给你回复");
        list3.add("你好");
        map.put("短信模板", list3);
        List<String> list4 = new ArrayList<String>();
        list4.add("我爱你");
        list4.add("I love you");
        list4.add("我想你");
        map.put("爱情祝福", list4);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return true;
    }


}
