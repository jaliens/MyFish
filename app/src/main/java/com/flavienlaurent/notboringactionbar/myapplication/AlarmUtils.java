package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

//import com.example.gorchg.alarmscheduler.receiver.AlarmOneMinuteBroadcastReceiver;
import com.flavienlaurent.notboringactionbar.myapplication.receiver.AlarmOneMinuteBroadcastReceiver;
/**
 * Created by Ch on 2016-05-13.
 */
public class AlarmUtils {
    private static AlarmUtils _instance;

    public static AlarmUtils getInstance() {
        if (_instance == null) _instance = new AlarmUtils();
        return _instance;
    }

    public void startOneMinuteAlram(Context context) {

        // AlarmOneMinuteBroadcastReceiver 초기화
        Intent alarmIntent = new Intent(context, AlarmOneMinuteBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//        Log.d("ere","here");
        // 1분뒤에 AlarmOneMinuteBroadcastReceiver 호출 한다.
        startAlram(context, pendingIntent,40*1000);
    }

    private void startAlram(Context context, PendingIntent pendingIntent, int delay) {

        // AlarmManager 호출
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 1분뒤에 AlarmOneMinuteBroadcastReceiver 호출 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }
    }
}
