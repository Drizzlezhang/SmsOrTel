package com.drizzle.app.smsortel.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.drizzle.app.smsortel.model.Time;
import com.drizzle.app.smsortel.receiver.TelReceiver;

public class TelService extends Service {

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
        String telokyearservice=intent.getStringExtra("extra_telokyear");
        String telokmonthservice=intent.getStringExtra("extra_telokmonth");
        String telokdayservice=intent.getStringExtra("extra_telokday");
        String telokhourservice=intent.getStringExtra("extra_telokhour");
        String telokminuteservice=intent.getStringExtra("extra_telokminute");
        int telokidservice=intent.getIntExtra("extra_telokid",-1);
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        Time teloksettime=new Time();
        int settime= teloksettime.dTime(telokyearservice,telokmonthservice,telokdayservice,telokhourservice,telokminuteservice);
        long triggerAtTime= SystemClock.elapsedRealtime()+settime;
        Intent intent2=new Intent(this, TelReceiver.class);
        intent2.putExtra("pending_telokid",telokidservice);
        PendingIntent pi2=PendingIntent.getBroadcast(this,telokidservice,intent2,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi2);
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
