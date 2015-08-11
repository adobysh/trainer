package com.holypasta.trainer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.util.JesusSaves;

import java.util.Calendar;
import java.util.List;

public class Degradation extends BroadcastReceiver implements Constants {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("!!! Degradation now");
        if (!isDegradationTime()) {
            System.out.println("!!! Degradation not now!");
            return;
        }
        List<Integer> scores = JesusSaves.getInstance(context).getScores();
        for (int levelId = 0; levelId < scores.size(); levelId++) {
            int score = scores.get(levelId);
            System.out.println("!!! Degradation level " + (levelId+1) + " with score " + score);
            if (score > 1) {
                score = score - DEGRADATION_STEP;
                if (score < 1) {
                    score = 1;
                }
                JesusSaves.getInstance(context).setScore(levelId, score);
            }
        }
        JesusSaves.getInstance(context).setScore(REPEAT_LESSONS_LESSON, 0);
    }

    private boolean isDegradationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(Calendar.HOUR_OF_DAY) == DEGRADATION_HOUR) {
            if (calendar.get(Calendar.MINUTE) == DEGRADATION_MINUTE) {
                return true;
            }
        }
        return false;
    }
}
