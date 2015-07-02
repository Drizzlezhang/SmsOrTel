package com.drizzle.app.smsortel.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/19.
 */
public class SmsOrTelDB {

    private static final String DB_NAME="sms_or_tel_final2";
    private static final int VERSION=1;
    private static SmsOrTelDB smsortelDB;
    private SQLiteDatabase db;


    private SmsOrTelDB(Context context){
        SmsOrTelOpenHelper dbHelper=new SmsOrTelOpenHelper(context,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();
    }

    public synchronized static SmsOrTelDB getInstance(Context context){
        if(smsortelDB==null){
            smsortelDB=new SmsOrTelDB(context);

        }
        return smsortelDB;
    }

    public void saveMission(Mission mission){
        if(mission!=null){
            ContentValues values=new ContentValues();
            values.put("mission_id",mission.getMissionId());
            values.put("mission_number",mission.getMissionNumber());
            values.put("mission_word",mission.getMissionWord());
            values.put("mission_year",mission.getMissionYear());
            values.put("mission_month",mission.getMissionMonth());
            values.put("mission_date",mission.getMissionDay());
            values.put("mission_hour",mission.getMissionHour());
            values.put("mission_minute",mission.getMissionMinute());
            values.put("mission_imageId",mission.getImageId());
            values.put("mission_company",mission.getMissionCompany());
            db.insert("Mission",null,values);
        }


    }
    public Mission loadMission(int missionId){
        Cursor cursor=db.rawQuery("select * from Mission where mission_id=?",new String[]{String.valueOf(missionId)});

              if(cursor.moveToFirst()) {
                   Mission mission=new Mission();
                   mission.setMissionId(cursor.getInt(cursor.getColumnIndex("mission_id")));
                   mission.setMissionNumber(cursor.getString(cursor.getColumnIndex("mission_number")));
                   mission.setMissionWord(cursor.getString(cursor.getColumnIndex("mission_word")));
                   mission.setMissionHour(cursor.getString(cursor.getColumnIndex("mission_hour")));
                   mission.setMissionMinute(cursor.getString(cursor.getColumnIndex("mission_minute")));
                  mission.setMissionYear(cursor.getString(cursor.getColumnIndex("mission_year")));
                  mission.setMissionMonth(cursor.getString(cursor.getColumnIndex("mission_month")));
                  mission.setMissionDay(cursor.getString(cursor.getColumnIndex("mission_date")));
                  mission.setImageId(cursor.getInt(cursor.getColumnIndex("mission_imageId")));
                  mission.setMissionCompany(cursor.getString(cursor.getColumnIndex("mission_company")));


                   return mission;
               }
        return  null;

    }


    public void deleteMission(int missionId){
        db.delete(" Mission"," mission_id = ?",new String[]{String.valueOf(missionId)});
    }


    public List<Mission> findMissions(){
        List<Mission> list=new ArrayList<Mission>() ;
        Cursor cursor=db.query("Mission",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Mission mission=new Mission();
                mission.setMissionId(cursor.getInt(cursor.getColumnIndex("mission_id")));
                mission.setMissionNumber(cursor.getString(cursor.getColumnIndex("mission_number")));
                mission.setMissionWord(cursor.getString(cursor.getColumnIndex("mission_word")));
                mission.setMissionHour(cursor.getString(cursor.getColumnIndex("mission_hour")));
                mission.setMissionMinute(cursor.getString(cursor.getColumnIndex("mission_minute")));
                mission.setMissionYear(cursor.getString(cursor.getColumnIndex("mission_year")));
                mission.setMissionMonth(cursor.getString(cursor.getColumnIndex("mission_month")));
                mission.setMissionDay(cursor.getString(cursor.getColumnIndex("mission_date")));
                mission.setImageId(cursor.getInt(cursor.getColumnIndex("mission_imageId")));
                mission.setMissionCompany(cursor.getString(cursor.getColumnIndex("mission_company")));
                list.add(mission);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return  list;
    }


}
