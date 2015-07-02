package com.drizzle.app.smsortel.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.drizzle.app.smsortel.R;
import com.drizzle.app.smsortel.model.Mission;
import com.drizzle.app.smsortel.model.SmsOrTelDB;

/**
 * Created by Administrator on 2015/5/17.
 */
public class SmsNotReceiver extends BroadcastReceiver {

    private SmsOrTelDB smsortelDB;

    @Override
    public void onReceive(Context context, Intent intent) {
        int smsnotidreceiver=intent.getIntExtra("pending_smsnotid", -1);
        smsortelDB=SmsOrTelDB.getInstance(context);
        Mission mission=smsortelDB.loadMission(smsnotidreceiver);
        if(mission==null){
            Log.v("SmsNotReceiver","mission canceled");
        }else {
            String smsnotNumberreceiver = mission.getMissionNumber();
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.mipmap.ic_sms_not, "发送短信", System.currentTimeMillis());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setType("vnd.android-dir/mms-sms");
            i.setData(Uri.parse("smsto:" + smsnotNumberreceiver));
            PendingIntent pi = PendingIntent.getActivity(context, smsnotidreceiver, i, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setLatestEventInfo(context, "发送短信", smsnotNumberreceiver, pi);
            notification.defaults = Notification.DEFAULT_ALL;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(smsnotidreceiver, notification);
            smsortelDB.deleteMission(smsnotidreceiver);
        }
    }
}
