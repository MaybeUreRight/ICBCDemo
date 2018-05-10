package xinshiyeweixin.cn.icbcdemo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Date;

import xinshiyeweixin.cn.icbcdemo.http.HttpManager;
import xinshiyeweixin.cn.icbcdemo.receiver.AlarmReceiver;

/**
 * 九分钟更新一次
 */
public class HorizonService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpManager.update("test1234567890");
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int five = 9 * 60 * 1000; // 这是5s
        long triggerAtTime = SystemClock.elapsedRealtime() + five;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
