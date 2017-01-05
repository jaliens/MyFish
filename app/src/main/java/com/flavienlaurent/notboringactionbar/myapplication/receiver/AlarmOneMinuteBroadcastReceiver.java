package com.flavienlaurent.notboringactionbar.myapplication.receiver;

import android.content.Context;
import android.content.Intent;

import com.flavienlaurent.notboringactionbar.myapplication.AlarmUtils;
import com.flavienlaurent.notboringactionbar.myapplication.SelectActivity;

//import com.example.gorchg.alarmscheduler.AlarmUtils;
//import com.example.gorchg.alarmscheduler.MainActivity;
/**
 * Created by Ch on 2016-05-13.
 */

public class AlarmOneMinuteBroadcastReceiver extends AlarmBraodCastReciever{
    static int i=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context,intent);

        // 알람 스타트
        AlarmUtils.getInstance().startOneMinuteAlram(context);
        //Toast.makeText(context,"Passed one minute.", Toast.LENGTH_SHORT).show();
        if(i==0){
        SelectActivity.getInstace().TIMER_INIT();
        SelectActivity.getInstace().getPerson();
        }else{
            SelectActivity.getInstace().REMOVE();
            SelectActivity.getInstace().TIMER_INIT();
            SelectActivity.getInstace().getPerson();
        }
        i++;
    }
}