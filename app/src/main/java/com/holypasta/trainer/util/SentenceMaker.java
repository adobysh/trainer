package com.holypasta.trainer.util;


import com.holypasta.trainer.levels.Level01;
import com.holypasta.trainer.levels.Level02;

import java.util.Random;

public class SentenceMaker {

	public SentenceMaker() {

	}

	public static String[] makeSentance(int level, int score) {
		switch (level) {
            case 0:
                return Level01.makeSentance();
            case 1:
                return Level02.makeSentance(score);
            case 2:
                switch (new Random().nextInt(5)) {
                    case 0:
                        return new String[] {"Я здесь", "I am here"};
                    case 1:
                        return new String[] {"Я был здесь", "I was here"};
                    case 2:
                        return new String[] {"Я буду здесь", "I will be here"};
                    case 3:
                        return new String[] {"Мне нравится летать", "I like to fly"};
                    case 4:
                        return new String[] {"Ему хочется летать", "He wants to fly"};
                }
        }
        return new String[]{"x","y"};
	}
}
