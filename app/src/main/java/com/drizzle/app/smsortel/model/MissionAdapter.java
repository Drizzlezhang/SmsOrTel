package com.drizzle.app.smsortel.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.drizzle.app.smsortel.R;

import java.util.List;

/**
 * Created by Administrator on 2015/5/24.
 */
public class MissionAdapter extends ArrayAdapter<Mission>{

    private int resourceId;
    public MissionAdapter(Context context,int missionViewId,List<Mission> objects){
        super(context,missionViewId,objects);
        resourceId=missionViewId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mission mission=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView missionImage=(ImageView)view.findViewById(R.id.item_image);
        TextView missionNumber=(TextView)view.findViewById(R.id.item_number);
        TextView missionWord=(TextView)view.findViewById(R.id.item_word);
        TextView missionTime=(TextView)view.findViewById(R.id.item_time);
        TextView missionDate=(TextView)view.findViewById(R.id.item_date);
        missionImage.setImageResource(mission.getImageId());
        missionNumber.setText(mission.getMissionNumber());
        missionWord.setText(mission.getMissionWord());
        missionTime.setText(mission.getMissionHour()+":"+mission.getMissionMinute());
        missionDate.setText(mission.getMissionYear()+"年"+mission.getMissionMonth()+"月"+mission.getMissionDay()+"日");
        return view;
    }
}
