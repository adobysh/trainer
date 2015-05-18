package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.adapter.LevelsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.SharedPreferencesUtil;

import java.util.Calendar;
import java.util.List;

public class MainFragment extends Fragment implements Constants, AdapterView.OnItemClickListener {

    private ListView lv1main;
    private LevelsAdapter adapter;
    private List<Integer> scores;
    private SharedPreferences sPref;
    private int mode;
    private SingleActivity activity;

    public MainFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        activity.getSupportActionBar().setTitle(getString(R.string.app_name));
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        lv1main = (ListView) rootView.findViewById(R.id.lv1main);
        regVisit();
        startReminder();
        startDegradation();
        return rootView;
    }

    private void regVisit() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        long time = System.currentTimeMillis();
        edit.putLong(PREF_LAST_VISIT, time);
        edit.commit();
    }

    private void startReminder() {
        Intent intent = new Intent(ACTION_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToMillis(REMINDER_HOUR, REMINDER_MINUTE), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void startDegradation() {
        Intent intent = new Intent(ACTION_DEGRADATION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
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

    @Override
    public void onResume() {
        super.onResume();
        String[] parts = getResources().getStringArray(R.array.contents);
        scores = SharedPreferencesUtil.getScores(activity);
        adapter = new LevelsAdapter(parts, activity, scores);
        lv1main.setAdapter(adapter);
        lv1main.setOnItemClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(Constants.PREF_HARDCORE_MODE, mode);
        ed.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int levelId, long arg3) {
        if (levelId < Constants.COMPLETE) {
            int score = scores.get(levelId);
            if (score > -1) {
                Bundle arguments = new Bundle();
                arguments.putInt(EXTRA_LESSON_ID, levelId);
                arguments.putInt(EXTRA_MODE, mode);
                Fragment fragment = new LevelFragment();
                fragment.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                final AlertDialog aboutDialog = new AlertDialog.Builder(activity)
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        sPref = PreferenceManager.getDefaultSharedPreferences(activity);
        mode = sPref.getInt(Constants.PREF_HARDCORE_MODE, 0);
        if (mode == 1) {
            menu.findItem(R.id.action_hardcore_mode).setChecked(true);
        }
        super.onCreateOptionsMenu(menu,inflater);
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
