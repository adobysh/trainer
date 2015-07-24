package com.holypasta.trainer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.holypasta.trainer.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by q1bot on 02.04.2015.
 */
public class SharedPreferencesUtil implements Constants {

    public static List<Integer> getScores(Context context) {
        int size = LAST_LEVEL + 1;
        List<Integer> scores = new ArrayList<Integer>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        for (int i = 0; i < size; i++) {
            int score = sharedPreferences.getInt(Constants.PREF_SCORE_0_15 + i, i == 0 ? 0 : -1);
            if (score >= MAX_SCORE) {
                score = MAX_SCORE;
                unlockNextLesson(context, i);
            }
            scores.add(score);
        }
        return scores;
    }

    public static int getLessonScore(Context context, int lessonId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(Constants.PREF_SCORE_0_15 + lessonId, 0);
    }

    public static void unlockNextLesson(Context context, int lessonId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (lessonId == LAST_LEVEL) return;
        int nextLevelSore = sharedPreferences.getInt(PREF_SCORE_0_15 + (lessonId+1), -1);
        if (nextLevelSore == -1) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(PREF_SCORE_0_15 + (lessonId + 1), 0);
            edit.apply();
        }
    }

    public static void saveScore(Context context, int level, int score) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(Constants.PREF_SCORE_0_15 + level, score);
        ed.apply();
    }
}
