package com.drizzle.app.smsortel.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/5/17.
 */
//该算法计算出给出时间与当前时间的时间差（毫秒）
public class Time {


    public static int dTime(String givenYear,String givenMonth,String givenDay,String givenHour,String givenMinute){
        long currentTimeMillis=System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String givenTime=givenYear+"-"+givenMonth+"-"+givenDay+" "+givenHour+":"+givenMinute+":00";
        long givenTimeInMillis= 0;
        try {
            givenTimeInMillis = sdf.parse(givenTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        int result=(int)(givenTimeInMillis-currentTimeMillis);
        return result;
    }
    public static boolean tTime(String givenYear,String givenMonth,String givenDay,String givenHour,String givenMinute){
        long currentTimeMillis=System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String givenTime=givenYear+"-"+givenMonth+"-"+givenDay+" "+givenHour+":"+givenMinute+":00";
        long givenTimeInMillis= 0;
        try {
            givenTimeInMillis = sdf.parse(givenTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean result=false;
        if(currentTimeMillis<givenTimeInMillis){
            result=true;
        }else{
            result=false;
        }

        return result;
    }
}
