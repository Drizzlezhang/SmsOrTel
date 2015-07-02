package com.drizzle.app.smsortel.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.drizzle.app.smsortel.model.Time;
import com.drizzle.app.smsortel.receiver.SmsReceiver;

public class SmsService extends Service {

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
        Log.d("service", "adfasdf");
        String smsokyearservice=intent.getStringExtra("extra_smsokyear");
        String smsokmonthservice=intent.getStringExtra("extra_smsokmonth");
        String smsokdayservice=intent.getStringExtra("extra_smsokday");
        String smsokhourservice=intent.getStringExtra("extra_smsokhour");
        String smsokminuteservice=intent.getStringExtra("extra_smsokminute");
        int smsokidservice=intent.getIntExtra("extra_smsokid",-1);
        Log.d("SmsService",smsokidservice+"");
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        Time smsoksettime=new Time();
        int settime= smsoksettime.dTime(smsokyearservice,smsokmonthservice,smsokdayservice,smsokhourservice,smsokminuteservice);
        long triggerAtTime= SystemClock.elapsedRealtime()+settime;
        Intent intent2=new Intent(this, SmsReceiver.class);
        intent2.putExtra("pending_smsokid", smsokidservice);
        PendingIntent pi2=PendingIntent.getBroadcast(this,smsokidservice,intent2,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi2);
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
