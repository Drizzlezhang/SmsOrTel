package com.drizzle.app.smsortel.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/5/19.
 */
public class SmsOrTelOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_MISSION="create table Mission("
            +"id integer primary key autoincrement,"
            +"mission_id integer,"
            +"mission_number text,"
            +"mission_word text,"
            +"mission_year text,"
            +"mission_month text,"
            +"mission_date text,"
            +"mission_hour text,"
            +"mission_imageId integer,"
            +"mission_company text,"
            +"mission_minute text)";

    public SmsOrTelOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context, name, factory, version);

    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_MISSION);
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
