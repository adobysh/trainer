package com.holypasta.trainer.levels;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.data.MultiSentence;

/**
 * Created by q1bot on 11.01.15.
 */
public abstract class AbstractLevel implements Constants {

    public abstract MultiSentence makeSentence(int score, int mode);

    // | ? + - | future
    // | ? + - | present
    // | ? + - | past
    protected int genWhere(int form, int time) {
        return time * 3 + form;
    }

}
