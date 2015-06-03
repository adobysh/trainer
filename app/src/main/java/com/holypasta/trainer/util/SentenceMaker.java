package com.holypasta.trainer.util;


import android.content.Context;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.MultiSentenceDataV2;
import com.holypasta.trainer.levels.AbstractLevel;
import com.holypasta.trainer.levels.Level01;
import com.holypasta.trainer.levels.Level02;
import com.holypasta.trainer.levels.Level03;
import com.holypasta.trainer.levels.Level04;
import com.holypasta.trainer.levels.Level05;
import com.holypasta.trainer.levels.LevelFromList;
import com.holypasta.trainer.levels.LevelFromListV2;

import java.util.Random;

public class SentenceMaker {

    private LevelFromListV2 lesson;
    private int lessonId;

    public SentenceMaker(Context context, int lessonId) {
        lesson = new LevelFromListV2(context, lessonId);
    }

    public MultiSentenceDataV2 makeSentence() {
        return lesson.makeSentence();
    }

	public static MultiSentenceData makeSentence(Context context, int levelId, int mode, int score) {
        AbstractLevel level = null;
        String[] badSentence = null;
		switch (levelId) {
            case 0:
                level = new Level01();
                break;
            case 1:
                level = new Level02();
                break;
            case 2:
                level = new Level03();
                break;
            case 3:
                level = new Level04();
                break;
            case 4:
                level = new Level05();
                break;
            default:
                level = new LevelFromList(context, levelId);
                break;
        }
        if (level != null) {
            return level.makeSentence(mode);
        } else {
            return new MultiSentenceData(badSentence);
        }
	}
}
