package com.holypasta.trainer;

public interface Constants {

    String PACKAGE_NAME = "com.holypasta.trainer.";
    String PREF = PACKAGE_NAME + "pref.";
    String EXTRA = PACKAGE_NAME + "extra.";
    String ACTION = PACKAGE_NAME + "action.";

    int MODE_EASY = 0;
    int MODE_HARD = 1;

    int REMINDER_HOUR = 12;
    int REMINDER_MINUTE = 0;
    int DEGRADATION_HOUR = 0;
    int DEGRADATION_MINUTE = 0;

    int MAX_SCORE = 10;
    int DEGRADATION_STEP = MAX_SCORE;

    int COMPLETE = 8;
    int LAST_LEVEL = 15;

    String ACTION_REMINDER = ACTION + "reminder";
    String ACTION_DEGRADATION = ACTION + "degradation";

    String EXTRA_LESSON_ID = EXTRA + "lesson.id";
    String EXTRA_MODE = EXTRA + "mode";

    String PREF_SCORE_0_15 = PREF + "score_";
    String PREF_HARDCORE_MODE = PREF + "hardcore.mode";
    String PREF_LAST_VISIT = PREF + "last.visit";

    String YOUTUBE_APP_BASE_URL = "vnd.youtube:";
    String YOUTUBE_SITE_BASE_URL = "https://www.youtube.com/watch?v=";
    String[] VIDEO_ID = new String [] { "y9fFDpSqKdQ", "KpNy_DBth6k", "qg9NhobhbTg",
            "Bq6zgXdAc7Q", "VMwVt6ohKFE", "B93DPIcRV2o", "xCNIdZewu2s", "yr5KPLPXOVs",
            "2x4WP4owvkw", "vkO2B8p_jy4", "mq0Bz4YR45k", "bSVEllZUFTw", "ASz1-RT-zgc",
            "rW6YhEjABbw", "KAP66olZSQA", "kY4_9sXfcSo" };
}
