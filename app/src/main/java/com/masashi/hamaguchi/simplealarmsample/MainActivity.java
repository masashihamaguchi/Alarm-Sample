package com.masashi.hamaguchi.simplealarmsample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // UI
    private TextView timeTextView;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // AlarmManager
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("アラームアプリ");

        // ビューの関連付け
        timeTextView = findViewById(R.id.timeTextView);

        // SharedPreferencesの設定
        sharedPreferences =getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //　AlarmManagerの取得
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        // alarmの時間を取得
        long time = sharedPreferences.getLong("time", 0);

        // TextViewに表示
        if (time != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            String timeText = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            timeTextView.setText(timeText);

        } else {
            timeTextView.setText("アラームはセットされていません。");

        }

    }


    // SetTimeActivityでアラームを設定する
    public void setTime(View view) {
        Intent intent = new Intent(this, SetTimeActivity.class);
        startActivityForResult(intent, 1);
    }


    // アラームを解除するメソッド
    public void release(View view) {
        // アラームを解除する
        Intent intent = new Intent(this, WakeUpActivity.class);
//        intent.setData(Uri.parse(String.valueOf(-1)));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.cancel(alarmIntent);

        // TextViewに表示する
        timeTextView.setText("アラームはセットされていません。");

        // SharedPreferencesの値を削除
        editor.remove("time");
        editor.commit();
    }

    // アラームをセットした時間を受け取る
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // リクエストコードを確認する
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // データ取得
            long millis = data.getLongExtra("time", 0);

            // カレンダー型を精製して時間を取り出す。
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

            // TextViewに表示する
            timeTextView.setText(time);

        }

    }

}