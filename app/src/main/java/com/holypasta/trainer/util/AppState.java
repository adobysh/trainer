package com.holypasta.trainer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.holypasta.trainer.Constants;

import java.util.ArrayList;
import java.util.List;

public class AppState implements Constants {

    private static final String PREF_SCORE_REPEAT = PREF + "repeat";
    private static final String PREF_SCORE_0_15 = PREF + "score_";
    private static final String PREF_HARDCORE_MODE = PREF + "hardcore.mode";
    private static final String PREF_LAST_VISIT = PREF + "last.visit";
    private static final int SCORE_SIZE = LAST_LEVEL + 1;
    
    private static AppState appState;
    private SharedPreferences sharedPreferences;
    private List<Integer> scores;
    private int repeatLessonsLessonScore;
    private int mode;
    private long lastVisit;

    public static synchronized AppState getInstance(Context context) {
        if (appState == null) {
            appState = new AppState(context);
        }
        return appState;
    }

    public AppState(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        loadScores();
        loadMode();
        loadVisit();
    }

    public void saveState() {
        saveScores();
        saveMode();
        saveVisit();
    }

    public List<Integer> getScores() {
        return scores;
    }

    public int getLessonScore(int lessonId) {
        if (lessonId == REPEAT_LESSONS_LESSON) {
            return repeatLessonsLessonScore;
        } else {
            return scores.get(lessonId);
        }
    }

    public void setScore(int lessonId, int score) {
        if (lessonId == REPEAT_LESSONS_LESSON) {
            repeatLessonsLessonScore = score;
        } else {
            scores.set(lessonId, score);
        }
    }

    public boolean unlockNextLesson(int currentLessonId) {
        if (currentLessonId != LAST_LEVEL) {
            int nextLessonId = currentLessonId + 1;
            if (scores.size() > nextLessonId && scores.get(nextLessonId) == SCORE_LESSON_CLOSE) {
                scores.set(nextLessonId, SCORE_LESSON_OPEN);
                return true;
            }
        }
        return false;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getLastOpenLessonId() {
        for (int lessonId = 0; lessonId < SCORE_SIZE; lessonId++) {
            int score = scores.get(lessonId);
            if (score == -1 && lessonId <= COMPLETE) {
                return lessonId-1;
            }
        }
        return COMPLETE-1;
    }

    public long getVisit() {
        return lastVisit;
    }

    public void regVisit() {
        lastVisit = System.currentTimeMillis();
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

    private void saveScores() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int lessonId = 0; lessonId < scores.size(); lessonId++) {
            editor.putInt(PREF_SCORE_0_15 + lessonId, scores.get(lessonId));
        }
        editor.putInt(PREF_SCORE_REPEAT, repeatLessonsLessonScore);
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        saveState();
    }
}
