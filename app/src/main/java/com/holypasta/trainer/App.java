package com.holypasta.trainer;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.holypasta.trainer.helper.HelperFactory;
import com.holypasta.trainer.util.AppState;

import java.util.Calendar;

public class App extends Application implements Constants {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
        regVisit();
        startReminder();
        startDegradation();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        HelperFactory.releaseHelper();
    }

    private void regVisit() {
        AppState.getInstance(this).regVisit();
    }

    private void startReminder() {
        Intent intent = new Intent(ACTION_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToMillis(REMINDER_HOUR, REMINDER_MINUTE), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void startDegradation() {
        Intent intent = new Intent(ACTION_DEGRADATION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToMillis(DEGRADATION_HOUR, DEGRADATION_MINUTE), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private long timeToMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }
}
