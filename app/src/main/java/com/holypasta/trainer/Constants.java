package com.holypasta.trainer;

public interface Constants {

    static final int MODE_EASY = 0;
    static final int MODE_HARD = 1;

    static final String EXTRA_LESSON_ID = "extra.lesson.id";
    static final String EXTRA_MODE = "extra.mode";

    static final int COMPLETE = 5;
    static final String SCORE_0_15 = "SCORE_";
    static final String SCORES = "SCORES";

    static final String PREF_HARDCORE_MODE = "PREF_HARDCORE_MODE";

    static final int MAX_SCORE = 100;
    static final int PASS_SCORE = 100;

    static final String YOUTUBE_BASE_URL = "vnd.youtube:";
    static final String VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";
    static final String[] VIDEO_ID = new String [] { "y9fFDpSqKdQ", "KpNy_DBth6k", "qg9NhobhbTg",
            "Bq6zgXdAc7Q", "VMwVt6ohKFE", "B93DPIcRV2o", "xCNIdZewu2s", "yr5KPLPXOVs",
            "2x4WP4owvkw", "vkO2B8p_jy4", "mq0Bz4YR45k", "bSVEllZUFTw", "ASz1-RT-zgc",
            "rW6YhEjABbw", "KAP66olZSQA", "kY4_9sXfcSo" };

    static final int TIME_FUTURE = 0;
    static final int TIME_PRESENT = 1;
    static final int TIME_PAST = 2;
    static final int FORM_QUESTION = 0;
    static final int FORM_POSITIVE = 1;
    static final int FORM_NEGATIVE = 2;

    static final String ERROR_MESSAGE = "ошибка :(";
}
