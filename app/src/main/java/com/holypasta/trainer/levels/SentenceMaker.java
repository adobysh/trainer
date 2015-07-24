package com.holypasta.trainer.levels;


import android.content.Context;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.data.AbstractMultiSentence;

import java.util.ArrayList;
import java.util.List;

public class SentenceMaker implements Constants {

    private final static int UNIQUE_COUNT = 10; // 20 take looping
    private final int lessonId;
    private AbstractLevel level;
    private List<AbstractMultiSentence> pastMultiSentences;

    public SentenceMaker(Context context, int lessonId, int mode) {
        this.lessonId = lessonId;
        switch (lessonId) {
            case 0:
                level = new Level01(mode);
                break;
            case 1:
                level = new Level02(mode);
                break;
            case 2:
                level = new Level03(mode);
                break;
            case 3:
                level = new Level04(mode);
                break;
            case 4:
                level = new Level05(mode);
                break;
            default:
                level = new LevelFromList(context, lessonId);
                break;
        }
    }

	public AbstractMultiSentence makeSentence() {
        if (lessonId >= 5) {
            return level.makeSentence();
        }
        AbstractMultiSentence multiSentence;
        if (pastMultiSentences == null) {
            pastMultiSentences = new ArrayList<>();
        }
        do {
            multiSentence = level.makeSentence();
        } while (pastMultiSentences.contains(multiSentence));
        pastMultiSentences.add(multiSentence);
        if (pastMultiSentences.size() >= UNIQUE_COUNT) {
            pastMultiSentences.remove(0);
        }
        return multiSentence;
	}

}
