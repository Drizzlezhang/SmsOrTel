package com.drizzle.app.smsortel.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.drizzle.app.smsortel.R;
import com.drizzle.app.smsortel.model.Mission;
import com.drizzle.app.smsortel.model.SmsOrTelDB;

/**
 * Created by Administrator on 2015/5/17.
 */
public class SmsReceiver extends BroadcastReceiver {

    private SmsOrTelDB smsortelDB;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SmsReceiver","sadfasdfd");
        int smsokidreceiver=intent.getIntExtra("pending_smsokid", -1);
        Log.d("SmsReceiver",smsokidreceiver+"");
        smsortelDB=SmsOrTelDB.getInstance(context);
        Mission mission=smsortelDB.loadMission(smsokidreceiver);
        if(mission==null){
            Log.v("SmsReceiver","mission canceled");
        }else {
            String smsokNumberreceiver = mission.getMissionNumber();
            String smsokWordreceiver = mission.getMissionWord();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(smsokNumberreceiver, null, smsokWordreceiver, null, null);
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.mipmap.ic_sms_ok, "短信已发送", System.currentTimeMillis());
            notification.setLatestEventInfo(context, "短信已发送", smsokNumberreceiver, null);
            notification.defaults = Notification.DEFAULT_ALL;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(smsokidreceiver, notification);
            smsortelDB.deleteMission(smsokidreceiver);
        }
    }


}
