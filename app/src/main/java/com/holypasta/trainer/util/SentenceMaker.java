package com.holypasta.trainer.util;


import android.content.Context;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.levels.AbstractLevel;
import com.holypasta.trainer.levels.Level01;
import com.holypasta.trainer.levels.Level02;
import com.holypasta.trainer.levels.Level03;
import com.holypasta.trainer.levels.Level04;
import com.holypasta.trainer.levels.LevelFromList;

public class SentenceMaker {

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
                level = new LevelFromList(context, levelId);
                break;
            default:
                badSentence = new String[] {"x","y"};
                break;
        }
        if (level != null) {
            return level.makeSentence(mode);
        } else {
            return new MultiSentenceData(badSentence);
        }
	}
}
