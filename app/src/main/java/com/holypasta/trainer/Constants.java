package com.holypasta.trainer;

public interface Constants {

    static final String PACKAGE_NAME = "com.holypasta.trainer.";
    static final String PREF = PACKAGE_NAME + "pref.";
    static final String EXTRA = PACKAGE_NAME + "extra.";
    static final String ACTION = PACKAGE_NAME + "action.";

    static final int MODE_EASY = 0;
    static final int MODE_HARD = 1;

    static final int REMINDER_HOUR = 12;
    static final int REMINDER_MINUTE = 0;
    static final int DEGRADATION_HOUR = 0;
    static final int DEGRADATION_MINUTE = 0;

    static final int MAX_SCORE = 10;
    static final int DEGRADATION_STEP = MAX_SCORE;
    static final int UNIQUE_COUNT = 10; // 20 take looping

    static final int COMPLETE = 8;
    static final int LAST_LEVEL = 15;

    static final String ACTION_REMINDER = ACTION + "reminder";
    static final String ACTION_DEGRADATION = ACTION + "degradation";

    static final String EXTRA_LESSON_ID = EXTRA + "lesson.id";
    static final String EXTRA_MODE = EXTRA + "mode";
    static final String EXTRA_FIRST_OPEN = EXTRA + "first.open";

    static final String PREF_SCORE_0_15 = PREF + "score_";
    static final String PREF_HARDCORE_MODE = PREF + "hardcore.mode";
    static final String PREF_LAST_VISIT = PREF + "last.visit";
    static final String PREF_SPEECH_RECOGNITION_FIRST_CHECK = PREF + "speech.recognition.first.check";

    static final String YOUTUBE_APP_BASE_URL = "vnd.youtube:";
    static final String YOUTUBE_SITE_BASE_URL = "https://www.youtube.com/watch?v=";
    static final String[] VIDEO_ID = new String [] { "y9fFDpSqKdQ", "KpNy_DBth6k", "qg9NhobhbTg",
            "Bq6zgXdAc7Q", "VMwVt6ohKFE", "B93DPIcRV2o", "xCNIdZewu2s", "yr5KPLPXOVs",
            "2x4WP4owvkw", "vkO2B8p_jy4", "mq0Bz4YR45k", "bSVEllZUFTw", "ASz1-RT-zgc",
            "rW6YhEjABbw", "KAP66olZSQA", "kY4_9sXfcSo" };

    static final String ERROR_MESSAGE = "ошибка :(";
}
