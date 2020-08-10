package com.masashi.hamaguchi.simplealarmsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class SetTimeActivity extends AppCompatActivity {

    // UI
    private TimePicker timePicker;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        // TimePickerの関連付け
        timePicker = findViewById(R.id.timePicker);

        // SharedPreferencesの設定
        sharedPreferences = getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    // アラームをセットする
    public void setTime(View view) {

        // 時間を取得する
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Calenderを作る
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MONTH, 8);

        // 現在時刻を取得
        Calendar nowCalender = Calendar.getInstance();
        nowCalender.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
//        nowCalender.setTimeInMillis(System.currentTimeMillis());


        // 時間を比較する
        int diff = calendar.compareTo(nowCalender);

        if (diff <= 0) {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        }

        // Alarmをセットする
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, WakeUpActivity.class);
//        intent.setData(Uri.parse(String.valueOf(-1)));
        PendingIntent alarmIntent = PendingIntent.getActivity(this, 0, intent, 0);

        alarmManager.cancel(alarmIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), alarmIntent);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        }


        // SharedPreferencesに値を保存する
        long time = calendar.getTimeInMillis();
        editor.putLong("time", time);
        editor.apply();


        // MainActivityに値を渡す
        Intent resultIntent = new Intent();
        resultIntent.putExtra("time", calendar.getTimeInMillis());
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    // 戻るボタン
    public void back(View view) {
        onBackPressed();
    }
}