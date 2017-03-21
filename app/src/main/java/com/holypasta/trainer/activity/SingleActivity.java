package com.holypasta.trainer.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.fragment.QuickStartFragment;
import com.holypasta.trainer.util.JesusSaves;

import java.util.Calendar;

public class SingleActivity extends ActionBarActivity implements Constants {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, new QuickStartFragment())
                .commit();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    if (backCount == 0) {
                        actionBar.setDisplayHomeAsUpEnabled(false);
                    } else {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    }
                }
            }
        });
        regVisit();
        startReminder();
        startDegradation();
    }

    public void openFragment(Fragment fragment) {
        openFragment(fragment, null);
    }

    public void openFragment(Fragment fragment, Bundle arguments) {
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void regVisit() {
        JesusSaves.getInstance(this).regVisit();
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
