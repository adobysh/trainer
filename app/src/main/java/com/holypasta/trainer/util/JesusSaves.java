package com.holypasta.trainer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.holypasta.trainer.Constants;

import java.util.ArrayList;
import java.util.List;

public class JesusSaves implements Constants {

    private static final String PREF_SCORE_REPEAT = PREF + "repeat";
    private static final String PREF_SCORE_0_15 = PREF + "score_";
    private static final String PREF_HARDCORE_MODE = PREF + "hardcore.mode";
    private static final String PREF_LAST_VISIT = PREF + "last.visit";
    private static final int SCORE_SIZE = LAST_LEVEL + 1;
    
    private static JesusSaves jesusSaves;
    private SharedPreferences sharedPreferences;
    private List<Integer> scores;
    private int repeatLessonsLessonScore;
    private int mode;
    private long lastVisit;

    public static synchronized JesusSaves getInstance(Context context) {
        if (jesusSaves == null) {
            jesusSaves = new JesusSaves(context);
        }
        return jesusSaves;
    }

    public JesusSaves(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        loadScores();
        loadMode();
        loadVisit();
    }

    public synchronized List<Integer> getScores() {
        return scores;
    }

    public synchronized int getLessonScore(int lessonId) {
        if (lessonId == REPEAT_LESSONS_LESSON) {
            return repeatLessonsLessonScore;
        } else {
            return scores.get(lessonId);
        }
    }

    public synchronized void setScore(int lessonId, int score) {
        if (lessonId == REPEAT_LESSONS_LESSON) {
            repeatLessonsLessonScore = score;
        } else {
            scores.set(lessonId, score);
        }
        saveScore(lessonId, score);
    }

    public synchronized boolean unlockNextLesson(int currentLessonId) {
        if (currentLessonId != LAST_LEVEL) {
            int nextLessonId = currentLessonId + 1;
            if (scores.size() > nextLessonId && scores.get(nextLessonId) == SCORE_LESSON_CLOSE) {
                scores.set(nextLessonId, SCORE_LESSON_OPEN);
                saveScore(nextLessonId, SCORE_LESSON_OPEN);
                return true;
            }
        }
        return false;
    }

    public synchronized void unlockUntoLesson(int currentLessonId) {
        for (int lessonId = 0; lessonId <= currentLessonId; lessonId++) {
            if (scores.get(lessonId) == SCORE_LESSON_CLOSE) {
                scores.set(lessonId, SCORE_LESSON_OPEN);
                saveScore(lessonId, SCORE_LESSON_OPEN);
            }
        }
    }

    public synchronized int getMode() {
        return mode;
    }

    public synchronized void setMode(int mode) {
        this.mode = mode;
        saveMode();
    }

    public synchronized int getLastOpenLessonId() {
        for (int lessonId = 0; lessonId < SCORE_SIZE; lessonId++) {
            int score = scores.get(lessonId);
            if (score == -1 && lessonId <= COMPLETE) {
                return lessonId-1;
            }
        }
        return COMPLETE-1;
    }

    public synchronized long getVisit() {
        return lastVisit;
    }

    public synchronized void regVisit() {
        lastVisit = System.currentTimeMillis();
        saveVisit();
    }

    private void loadScores() {
        scores = new ArrayList<>(SCORE_SIZE);
        for (int i = 0; i < SCORE_SIZE; i++) {
            int score = sharedPreferences.getInt(PREF_SCORE_0_15 + i, i == 0 ? 0 : -1);
            if (score >= SCORE_MAX) {
                score = SCORE_MAX;
                unlockNextLesson(i);
            }
            scores.add(score);
        }
        repeatLessonsLessonScore = sharedPreferences.getInt(PREF_SCORE_REPEAT, SCORE_MAX);
    }

    private void saveScore(int lessonId, int score) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (lessonId == REPEAT_LESSONS_LESSON) {
            editor.putInt(PREF_SCORE_REPEAT, score);
        } else {
            editor.putInt(PREF_SCORE_0_15 + lessonId, score);
        }
        editor.apply();
    }

    private void loadMode() {
        mode = sharedPreferences.getInt(PREF_HARDCORE_MODE, MODE_EASY);
    }

    private void saveMode() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_HARDCORE_MODE, mode);
        editor.apply();
    }

    private void loadVisit() {
        sharedPreferences.getLong(PREF_LAST_VISIT, -1);
    }

    private void saveVisit() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(PREF_LAST_VISIT, lastVisit);
        edit.apply();
    }
}
