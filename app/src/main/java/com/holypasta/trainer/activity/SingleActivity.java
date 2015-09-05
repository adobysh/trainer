package com.holypasta.trainer.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.fragment.QuickStartFragment_;
import com.holypasta.trainer.util.JesusSaves;

import org.androidannotations.annotations.*;

import java.util.Calendar;
import java.util.Locale;

@EActivity(R.layout.activity_single)
public class SingleActivity extends ActionBarActivity implements Constants, TextToSpeech.OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech repeatTTS;
    private boolean ttsIsOn;

    @AfterViews
    protected void afterViews() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, new QuickStartFragment_())
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
//        checkSpeechSynthesis();
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

    private void checkSpeechSynthesis() {
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void installTTSFromGooglePlay() {
        Intent installTTSIntent = new Intent();
        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installTTSIntent);
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            repeatTTS.setLanguage(Locale.US);
        }
    }

    @OptionsItem(android.R.id.home)
    protected void homeSelected() {
        onBackPressed();
    }

    @OnActivityResult(MY_DATA_CHECK_CODE)
    protected void onResult(int resultCode, Intent data) {
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
            repeatTTS = new TextToSpeech(this, this);
            ttsIsOn = true;
        } else {
            installTTSFromGooglePlay();
        }
    }

	public void speakNow(String text) {
        if (ttsIsOn) {
            repeatTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
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
