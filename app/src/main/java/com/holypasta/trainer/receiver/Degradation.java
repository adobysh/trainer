package com.holypasta.trainer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.util.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by q1bot on 02.04.2015.
 */
public class Degradation extends BroadcastReceiver implements Constants {

    @Override
    public void onReceive(Context context, Intent intent) {
        List<Integer> scores = SharedPreferencesUtil.getScores(context);
        for (int i = 0; i < scores.size(); i++) {
            int score = scores.get(i);
            if (score > 1) {
                score = score - DEGRADATION_STEP;
                if (score < 1) {
                    score = 1;
                }
            }
            scores.set(i, score);
        }
    }
}
