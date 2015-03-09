package com.holypasta.trainer.util;


import android.content.Context;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.levels.AbstractLevel;
import com.holypasta.trainer.levels.Level01;
import com.holypasta.trainer.levels.Level02;
import com.holypasta.trainer.levels.Level03;
import com.holypasta.trainer.levels.Level04to05;

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
            case 4:
                level = new Level04to05(context, levelId);
                //                switch (new Random().nextInt(5)) {
//                    case 0:
//                        badSentance = new String[] {"Я здесь", "I am here"};
//                        break;
//                    case 1:
//                        badSentance = new String[] {"Я был здесь", "I was here"};
//                        break;
//                    case 2:
//                        badSentance = new String[] {"Я буду здесь", "I will be here"};
//                        break;
//                    case 3:
//                        badSentance = new String[] {"Мне нравится летать", "I like to fly"};
//                        break;
//                    case 4:
//                        badSentance = new String[] {"Ему хочется летать", "He wants to fly"};
//                        break;
//                }
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
