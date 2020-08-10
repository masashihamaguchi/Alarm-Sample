package com.masashi.hamaguchi.simplealarmsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Masashi Hamaguchi on 2020/07/20.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // アラームを設定する

        Intent wakeUpIntent = new Intent(context, WakeUpActivity.class);
        wakeUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(wakeUpIntent);


//        // あらーむ
//        Alarm

    }
}
