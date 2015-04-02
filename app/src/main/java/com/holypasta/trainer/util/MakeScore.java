package com.holypasta.trainer.util;

import java.text.DecimalFormat;

/**
 * Created by q1bot on 04.06.14.
 */
public class MakeScore {

    static public String make(int score) {
//        return ("\u2606 " + (new DecimalFormat("0.0")).format(score/10.0));
        return "\u2606 " + (score == -1 ? 0 : score);
    }
}
