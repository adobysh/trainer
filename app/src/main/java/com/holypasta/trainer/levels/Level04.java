package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;

import java.util.IllegalFormatCodePointException;
import java.util.Random;

/**
 * Created by q1bot on 10.03.2015.
 */
public class Level04 extends AbstractLevel {

    public Level04(int mode) {
        super(mode);
    }

    enum Parts { ABOUT_MYSELF, STABLE_EXPRESSIONS, GOOD_TIME_OF_DAY, PLEASE_WELCOME }

    private final int VARIANTS_1_ABOUT_MYSELF = 8;
    private final String[][] VARIANTS_2_STABLE_EXPRESSIONS = new String[][] {
            { "Спасибо", "Thank you", "Thanks" },
            { "Приятно познакомиться", "Nice to meet you" },
            { "Приношу извинение за опоздание", "I apologize for coming late" },
            { "Я сожалею", "I regret" },
            { "До свиданья", "Goodbye", "Bye" }
    };
    private final String[][] VARIANTS_3_GOOD_TIME_OF_DAY = new String[][] {
            { "Привет", "Hello", "Hi" },
            { "Доброе утро (до\u00A012:00)", "Good morning" },
            { "Добрый день (до\u00A018:00)", "Good afternoon" },
            { "Добрый вечер", "Good evening" },
            { "Доброй\u00A0ночи / Спокойной\u00A0ночи", "Good night" }

    };
    private final String[][] VARIANTS_4_PLEASE_WELCOME = new String[][] {
            { "Извините (обратить\u00A0внимание)", "Excuse Me" },
            { "Простите (переспросить)", "Forgive Me" },
            { "Приношу извинения", "I am sorry", "I'm sorry" },
            { "Пожалуйста (сделай,\u00A0помоги)", "Please" },
            { "Пожалуйста (в\u00A0ответ\u00A0на\u00A0спасибо)", "Welcome" }
    };

    @Override
    public MultiSentenceData makeSentence() {
        SentenceParamData partOfLesson = new SentenceParamData(Parts.values().length);
        SentenceParamData part1variant = new SentenceParamData(VARIANTS_1_ABOUT_MYSELF);
        SentenceParamData part2variant = new SentenceParamData(VARIANTS_2_STABLE_EXPRESSIONS.length);
        SentenceParamData part3variant = new SentenceParamData(VARIANTS_3_GOOD_TIME_OF_DAY.length);
        SentenceParamData part4variant = new SentenceParamData(VARIANTS_4_PLEASE_WELCOME.length);
        int variant = 0;
        switch (partOfLesson.value()) {
            case 0:
                variant = part1variant.value();
                break;
            case 1:
                variant = part2variant.value();
                break;
            case 2:
                variant = part3variant.value();
                break;
            case 4:
                variant = part3variant.value();
                break;
        }
        MultiSentenceData sentence = makeSentence(partOfLesson.value(), variant, false);
        if (mode == MODE_HARD) {
            return sentence;
        } else { // MODE_EASY
            Random random = new Random();
            if (random.nextBoolean()) { // 50% true
                return sentence;
            }
            boolean isWrong = false;
            switch (partOfLesson.value()) {
                case 0:
                    isWrong = true;
                    break;
                case 1:
                    variant = part2variant.nextRandom();
                    break;
                case 2:
                    variant = part3variant.nextRandom();
                    break;
                case 3:
                    variant = part4variant.nextRandom();
                    break;
            }
            return genWrongSentence(sentence, makeSentence(partOfLesson.value(), variant, isWrong));
        }
    }

    public MultiSentenceData makeSentence(int partOfLesson, int variant, boolean isWrong) {
        System.out.println("!!! part = " + partOfLesson + ", variant = " + variant + ", isWrong = " + isWrong);
        switch (partOfLesson) {
            case 0:
                return part01_about_myself(variant, isWrong);
            case 1:
                return part02_stable_expression(variant);
            case 2:
                return part03_good_time_of_day(variant);
            case 3:
                return part04_please_welcome(variant);
        }
        return null;
    }

    private MultiSentenceData part01_about_myself(int variant, boolean isWrong) {
        String[] wrongSentences;
        String[] correctSentences;
        switch (variant) {
            case 0:
                wrongSentences = new String[] { "I study art history" };
                correctSentences = new String[] { "Я изучал историю искуства", "I studied art history" };
                break;
            case 1:
                wrongSentences = new String[] { "I study history" };
                correctSentences = new String[] { "Я изучал историю", "I studied history" };
                break;
            case 2:
                wrongSentences = new String[] { "I work PR-manager in museum" };
                correctSentences = new String[] { "Я работаю пиарменеджером в музее", "I work in the museum as a PR-manager" };
                break;
            case 3:
                wrongSentences = new String[] { "I work PR-manager in museum" };
                correctSentences = new String[] { "Я пиарменеджер", "I am a PR-manager" };
                break;
            case 4:
                wrongSentences = new String[] { "I work for museum" };
                correctSentences = new String[] { "Я работаю в музее", "I work in the museum" };
                break;
            case 5:
                wrongSentences = new String[] { "I am a write", "I writer", "I write" };
                correctSentences = new String[] { "Я писатель", "I am a writer" };
                break;
            case 6:
                wrongSentences = new String[] { "I actor" };
                correctSentences = new String[] { "Я актёр", "I am an actor" };
                break;
            case VARIANTS_1_ABOUT_MYSELF - 1:
                wrongSentences = new String[] { "I work on theatre", "I work for theatre" };
                correctSentences = new String[] { "Я работаю в театре", "I work in theatre" };
                break;
            default:
                throw new IllegalArgumentException("incorrect variant! variant = " + variant);
        }
        if (isWrong) {
            return new MultiSentenceData(correctSentences[0], wrongSentences[new Random().nextInt(wrongSentences.length)]);
        } else {
            return new MultiSentenceData(correctSentences);
        }
    }

    private MultiSentenceData part02_stable_expression(int variant) {
        return new MultiSentenceData(VARIANTS_2_STABLE_EXPRESSIONS[variant]);
    }

    private MultiSentenceData part03_good_time_of_day(int variant) {
        return new MultiSentenceData(VARIANTS_3_GOOD_TIME_OF_DAY[variant]);
    }

    private MultiSentenceData part04_please_welcome(int variant) {
        return new MultiSentenceData(VARIANTS_4_PLEASE_WELCOME[variant]);
    }
}
