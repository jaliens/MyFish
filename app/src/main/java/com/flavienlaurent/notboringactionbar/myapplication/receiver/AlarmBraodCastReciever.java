package com.flavienlaurent.notboringactionbar.myapplication.receiver;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

//        import com.example.gorchg.alarmscheduler.Constants;
        import com.flavienlaurent.notboringactionbar.myapplication.Constants;


/**
 * Created by Ch on 2016-05-13.
 */
public class AlarmBraodCastReciever extends BroadcastReceiver {
    public static boolean isLaunched = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        isLaunched = true;

        // 현재 시간을 화면에 보낸다.
        saveTime(context);
    }

    private void saveTime(Context context) {
        long currentTime = System.currentTimeMillis();
        Intent intent = new Intent(Constants.INTENTFILTER_BROADCAST_TIMER);
        intent.putExtra(Constants.KEY_DEFAULT, currentTime);
        context.sendBroadcast(intent);
    }
}
