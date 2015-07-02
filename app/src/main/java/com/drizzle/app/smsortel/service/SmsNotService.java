package com.drizzle.app.smsortel.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.drizzle.app.smsortel.model.Time;
import com.drizzle.app.smsortel.receiver.SmsNotReceiver;

public class SmsNotService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String smsnotyearservice=intent.getStringExtra("extra_smsnotyear");
        String smsnotmonthservice=intent.getStringExtra("extra_smsnotmonth");
        String smsnotdayservice=intent.getStringExtra("extra_smsnotday");
        String smsnothourservice=intent.getStringExtra("extra_smsnothour");
        String smsnotminuteservice=intent.getStringExtra("extra_smsnotminute");
        int smsnotidservice=intent.getIntExtra("extra_smsnotid",-1);
        Log.d("SmsService", smsnotidservice + "");
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        Time smsoksettime=new Time();
        int settime= smsoksettime.dTime(smsnotyearservice,smsnotmonthservice,smsnotdayservice,smsnothourservice,smsnotminuteservice);
        long triggerAtTime= SystemClock.elapsedRealtime()+settime;
        Intent intent1=new Intent(this, SmsNotReceiver.class);
        intent1.putExtra("pending_smsnotid",smsnotidservice);
        PendingIntent pi1=PendingIntent.getBroadcast(this,smsnotidservice,intent1,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi1);
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
