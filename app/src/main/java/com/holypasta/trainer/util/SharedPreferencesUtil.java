package com.holypasta.trainer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.holypasta.trainer.Constants;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesUtil implements Constants {

    private static final int SCORE_SIZE = LAST_LEVEL + 1;
    private static SharedPreferencesUtil sharedPreferencesUtil;
    private SharedPreferences sharedPreferences;

    public static synchronized SharedPreferencesUtil getInstance(Context context) {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return sharedPreferencesUtil;
    }

    public SharedPreferencesUtil(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public List<Integer> getScores() {
        List<Integer> scores = new ArrayList<>(SCORE_SIZE);
        for (int i = 0; i < SCORE_SIZE; i++) {
            int score = sharedPreferences.getInt(PREF_SCORE_0_15 + i, i == 0 ? 0 : -1);
            if (score >= MAX_SCORE) {
                score = MAX_SCORE;
                unlockNextLesson(i);
            }
            scores.add(score);
        }
        return scores;
    }

    public int getLastOpenLessonId() {
        for (int lessonId = 0; lessonId < SCORE_SIZE; lessonId++) {
            int score = sharedPreferences.getInt(PREF_SCORE_0_15 + lessonId, lessonId == 0 ? 0 : -1);
            if (score == -1) {
                return lessonId-1;
            }
        }
        return LAST_LEVEL;
    }

    public int getLessonScore(int lessonId) {
        if (lessonId == REPEAT_LESSONS_LESSON) {
            return sharedPreferences.getInt(PREF_SCORE_REPEAT, 0);
        }
        return sharedPreferences.getInt(PREF_SCORE_0_15 + lessonId, 0);
    }

    public void unlockNextLesson(int lessonId) {
        if (lessonId == LAST_LEVEL) return;
        int nextLevelSore = sharedPreferences.getInt(PREF_SCORE_0_15 + (lessonId+1), -1);
        if (nextLevelSore == -1) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(PREF_SCORE_0_15 + (lessonId + 1), 0);
            edit.apply();
        }
    }

    public void saveScore(int lessonId, int score) {
        final String KEY = lessonId == REPEAT_LESSONS_LESSON ? PREF_SCORE_REPEAT : PREF_SCORE_0_15 + lessonId;
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(KEY, score);
        ed.apply();
    }

    public int getMode() {
        return sharedPreferences.getInt(PREF_HARDCORE_MODE, MODE_EASY);
    }

    public void saveMode(int mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_HARDCORE_MODE, mode);
        editor.apply();
    }

    public void regVisit() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        long time = System.currentTimeMillis();
        edit.putLong(PREF_LAST_VISIT, time);
        edit.apply();
    }
}
