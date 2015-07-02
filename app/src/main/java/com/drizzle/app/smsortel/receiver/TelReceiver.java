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
public class TelReceiver extends BroadcastReceiver {

    private SmsOrTelDB smsortelDB;

    @Override
    public void onReceive(Context context, Intent intent) {
        int telokidreceiver=intent.getIntExtra("pending_telokid", -1);
        smsortelDB=SmsOrTelDB.getInstance(context);
        Mission mission=smsortelDB.loadMission(telokidreceiver);
        if(mission==null){
            Log.v("TelReceiver", "mission canceled");
        }else {
            String telokNumberreceiver = mission.getMissionNumber();
            smsortelDB.deleteMission(mission.getMissionId());
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.mipmap.ic_tel_ok, "拨打电话", System.currentTimeMillis());
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + telokNumberreceiver));
            PendingIntent pi = PendingIntent.getActivity(context, telokidreceiver, i, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setLatestEventInfo(context, "拨打电话", telokNumberreceiver, pi);
            notification.defaults = Notification.DEFAULT_ALL;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(telokidreceiver, notification);
            smsortelDB.deleteMission(telokidreceiver);
        }
    }
}
