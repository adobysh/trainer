package com.holypasta.trainer.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.adapter.LevelsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.SharedPreferencesUtil;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends ActionBarActivity implements Constants, AdapterView.OnItemClickListener {

    private ListView lv1main;
    private LevelsAdapter adapter;
    private List<Integer> scores;
    private SharedPreferences sPref;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv1main = (ListView) findViewById(R.id.lv1main);
        regVisit();
        startReminder();
        startDegradation();
    }

    private void regVisit() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        long time = System.currentTimeMillis();
        edit.putLong(PREF_LAST_VISIT, time);
        edit.commit();
    }

    private void startReminder() {
        Intent intent = new Intent(ACTION_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, timeToMillis(REMINDER_HOUR, REMINDER_MINUTE), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void startDegradation() {
        Intent intent = new Intent(ACTION_DEGRADATION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, timeToMillis(DEGRADATION_HOUR, DEGRADATION_MINUTE), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private long timeToMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] parts = getResources().getStringArray(R.array.contents);
        scores = SharedPreferencesUtil.getScores(this);
        adapter = new LevelsAdapter(parts, this, scores);
        lv1main.setAdapter(adapter);
        lv1main.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(Constants.PREF_HARDCORE_MODE, mode);
        ed.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int levelId, long arg3) {
        if (levelId < Constants.COMPLETE) {
            if (scores.get(levelId) > -1 || DEBUG_MODE) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_LESSON_ID, levelId);
                intent.putExtra(EXTRA_MODE, mode);
                intent.setClass(this, LevelActivity.class);
                startActivity(intent);
            } else {
                final AlertDialog aboutDialog = new AlertDialog.Builder(
                        MainActivity.this)
                        .setMessage("Вам необходимо завершить предыдущие уроки")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
                aboutDialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        mode = sPref.getInt(Constants.PREF_HARDCORE_MODE, 0);
        if (mode == 1) {
            menu.findItem(R.id.action_hardcore_mode).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hardcore_mode:
                if (item.isChecked() == true) {
                    item.setChecked(false);
                    mode = MODE_EASY;
                } else {
                    item.setChecked(true);
                    mode = MODE_HARD;
                }
                break;
		}
        return super.onOptionsItemSelected(item);
    }

}
