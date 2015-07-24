package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;

import java.util.Random;

/**
 * Created by q1bot on 10.03.2015.
 */
public class Level05 extends AbstractLevel {

    public Level05(int mode) {
        super(mode);
    }

    enum Parts {
        comparison_x_then_x,
        comparison_the_best,
        time_base_form }

    enum DayWeekMonthEng { day, week, month }
    enum DayWeekMonthRus_5_10 { дней, недель, месяцев }
    enum DayWeekMonthRus_2_4 { дня, недели, месяца }
    enum DayWeekMonthRus_1 { день, неделя, месяц }

    final int VARIANTS_1_COMPARISON_X_THEN_X = 7;
    final int VARIANTS_2_COMPARISON_THE_BEST = 4;
    final int VERB_SEE = 7;
    final int VERB_COME = 8;
    final int[] TIME_VERBS = { VERB_SEE, VERB_COME };
    final int PART_comparison_X_then_X_static = 0;
    final int PART_comparison_the_best = 1;
    final int PART_time_in_3_days_ago = 2;
    final int PART_time_yesterday_today_tomorrow = 3;
    final int PART_time_dayOfWeek_month_season = 4;
    final int PARTS_COUNT = 5;

    @Override
    public MultiSentenceData makeSentence() {
        SentenceParamData partOfLesson = new SentenceParamData(PARTS_COUNT);
        SentenceParamData verb = new SentenceParamData(TIME_VERBS.length);
        SentenceParamData time = new SentenceParamData(TIME_PRESENT, 3, true);
        SentenceParamData who = new SentenceParamData(PRONOUN_IT, pronounsRU.length, true);
        SentenceParamData who1 = new SentenceParamData(PRONOUN_IT, who.value(), pronounsRU.length, true);
        who1.nextRandom();
        while(who.value() == who1.value() ||
                (who.value() == PRONOUN_I && who1.value() == PRONOUN_WE) ||
                (who.value() == PRONOUN_WE && who1.value() == PRONOUN_I)) {
            who1.nextRandom();
        }
        SentenceParamData count = new SentenceParamData(10);
        SentenceParamData dayWeekMonth = new SentenceParamData(3);
        SentenceParamData part1variant = new SentenceParamData(VARIANTS_1_COMPARISON_X_THEN_X);
        SentenceParamData part2variant = new SentenceParamData(VARIANTS_2_COMPARISON_THE_BEST);
        int variant = 0;
        switch (partOfLesson.value()) {
            case 0:
                variant = part1variant.value();
                break;
            case 1:
                variant = part2variant.value();
                break;
            case 2:
            case 3:
            case 4:
                variant = verb.value();
                break;
        }
        MultiSentenceData sentence = makeSentence(partOfLesson.value(), variant, time.value(),
                dayWeekMonth.value(), count.value(), who.value(), who1.value(), false);
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
                case 1:
                case 2:
                case 3:
                    isWrong = true;
                    break;
                case 4:
                    isWrong = true;
                    return makeSentence(partOfLesson.value(), variant, time.value(),
                            dayWeekMonth.value(), count.value(), who.value(), who1.value(), isWrong);
            }
            return genWrongSentence(sentence, makeSentence(partOfLesson.value(), variant, time.value(),
                    dayWeekMonth.value(), count.value(), who.value(), who1.value(), isWrong));
        }
    }

    private MultiSentenceData makeSentence(int partOfLesson, int variant, int time,
            int dayWeekMonth, int count, int who, int who1, boolean isWrong) {
        System.out.println("!!! part = " + partOfLesson + ", variant = " + variant + ", isWrong = " + isWrong);
        switch (partOfLesson) {
            case 0:
                return part_comparison_X_then_X_static(variant, isWrong);
            case 1:
                return part_comparison_the_best(variant, isWrong);
            case 2:
                return part_time_in_3_days_ago(time, dayWeekMonth, count, who, who1, variant, isWrong);
            case 3:
                return part_time_yesterday_today_tomorrow(time, dayWeekMonth, count, who, who1, variant, isWrong);
            case 4:
                return part_time_dayOfWeek_month_season(isWrong);
        }
        return null;
    }

    private MultiSentenceData part_comparison_X_then_X_static(int variant, boolean isWrong) {
        String[][] sentences = new String[][]{
                { "Я моложе чем он", "I am younger then him",
                        "I am young then him", "I am youngest then him" },
                { "Он старше чем ты", "He is older then you",
                        "He is old then you", "He is oldest then you" },
                {"Сегодня я говорю лучше чем вчера", "Today I speak better than yesterday",
                        "Today I speak good than yesterday", "Today I speak best than yesterday"},
                {"Вчера я говорила хуже чем сегодня", "Yesterday I spoke worse than today",
                        "Yesterday I spoke bad than today", "Yesterday I spoke worst than today"},
                {"Сегодня она более красива чем вчера", "Today she is more beautiful than yesterday",
                        "Today she is beautiful than yesterday", "Today she is most beautiful than yesterday"},
                {"Ноябрь короче чем октябрь", "November shorter than October",
                        "November short than October", "November shortest than October"},
                {"Москва больше чем Киев", "Moscow bigger than Kiev",
                        "Moscow big than Kiev", "Moscow greatest than Kiev"}
        };
        if (sentences.length != VARIANTS_1_COMPARISON_X_THEN_X) {
            throw new IllegalArgumentException("length != length");
        }
        if (isWrong) {
            final int WITHOUT_THEN = 0;
            final int WRONG_FORM = 1;
            switch (new Random().nextInt(2)) {
                case WITHOUT_THEN:
                    return new MultiSentenceData(sentences[variant][0], sentences[variant][1].replace(" than ", " "));
                case WRONG_FORM:
                    int wrong = new Random().nextInt(2) + 2;
                    return new MultiSentenceData(sentences[variant][0], sentences[variant][wrong]);
            }
            return null;
        } else {
            return new MultiSentenceData(sentences[variant][0], sentences[variant][1]);
        }
    }

    private MultiSentenceData part_comparison_the_best(int variant, boolean isWrong) {
        String[][] sentences = new String[][]{
                {"Февраль самый короткий месяц", "February is the shortest month",
                        "February is shortest month", "February is the short month", "February is the shorter month"},
                {"Я лучший ювелирный дизайнер", "I am the best jewelry designer",
                        "I am best jewelry designer", "I am the good jewelry designer", "I am the better jewelry designer"},
                {"Она самая красивая девушка", "She is the most beautiful girl",
                        "She is most beautiful girl", "She is the beautiful girl", "She is the more beautiful girl"},
                {"Наиболее вероятный", "Most likely",
                        "The most likely", "Likely", "More likely"},
        };
        if (sentences.length != VARIANTS_2_COMPARISON_THE_BEST) {
            throw new IllegalArgumentException("length != length");
        }
        if (isWrong) {
            int wrong = new Random().nextInt(3) + 2;
            return new MultiSentenceData(sentences[variant][0], sentences[variant][wrong]);
        } else {
            return new MultiSentenceData(sentences[variant][0], sentences[variant][1]);
        }
    }

    private MultiSentenceData part_time_in_3_days_ago(int time, int dayWeekMonth, int count, int who, int who1, int verb, boolean isWrong) {
        String suffixRus;
        String suffixEng;
        String prepositionFuture = "in";
        String prepositionPast = "ago";
        if (isWrong) {
            if (time == TIME_PAST) {
                String[] wrongPreposition = { "on", "at", "in" };
                prepositionPast = wrongPreposition[new Random().nextInt(wrongPreposition.length)];
            } else {
                String[] wrongPreposition = { "on", "at", "ago" };
                prepositionFuture = wrongPreposition[new Random().nextInt(wrongPreposition.length)];
            }
        }
        if (time == TIME_FUTURE) {
            suffixRus = "через " + getDayWeekMonth(count, dayWeekMonth, false);
            suffixEng = prepositionFuture + " " + getDayWeekMonth(count, dayWeekMonth, true);
            if (isWrong && new Random().nextBoolean()) {
                suffixEng = getDayWeekMonth(count, dayWeekMonth, true) + " " + prepositionFuture;
            }
        } else {
            suffixRus = getDayWeekMonth(count, dayWeekMonth, false) + " назад";
            suffixEng = getDayWeekMonth(count, dayWeekMonth, true) + " " + prepositionPast;
            if (isWrong && new Random().nextBoolean()) {
                suffixEng = prepositionPast + " " + getDayWeekMonth(count, dayWeekMonth, true);
            }
        }
        if (TIME_VERBS[verb] == VERB_SEE) {
            suffixRus = pronounsRU2[who1].toLowerCase() + " " + suffixRus;
            suffixEng = pronounsEN2[who1].toLowerCase() + " " + suffixEng;
        }
        return makeBaseForm(new RuVerbs01(), verbs01, time, who, TIME_VERBS[verb], suffixRus, suffixEng);
    }

//        вчера сегодня завтра сейчас
//        yesterday today tomorrow now
//      todo может добавить вариантов с today и now
    private MultiSentenceData part_time_yesterday_today_tomorrow(int time, int dayWeekMonth, int count, int who, int who1, int verb, boolean isWrong) {
        String suffixRus;
        String suffixEng;
        String prepositionFuture = "tomorrow";
        String prepositionPast = "yesterday";
        if (isWrong) {
            if (time == TIME_PAST) {
                String[] wrongPreposition = { "tomorrow", "today" };
                prepositionFuture = wrongPreposition[new Random().nextInt(wrongPreposition.length)];
            } else {
                String[] wrongPreposition = { "yesterday", "today" };
                prepositionFuture = wrongPreposition[new Random().nextInt(wrongPreposition.length)];
            }
        }
        if (time == TIME_FUTURE) {
            suffixRus = "завтра";
            suffixEng = prepositionFuture;
        } else {
            suffixRus = "вчера";
            suffixEng = prepositionPast;
        }
        if (TIME_VERBS[verb] == VERB_SEE) {
            suffixRus = pronounsRU2[who1].toLowerCase() + " " + suffixRus;
            suffixEng = pronounsEN2[who1].toLowerCase() + " " + suffixEng;
        }
        return makeBaseForm(new RuVerbs01(), verbs01, time, who, TIME_VERBS[verb], suffixRus, suffixEng);
    }

    // todo !add last next this | before after
    private MultiSentenceData part_time_dayOfWeek_month_season(boolean isWrong) {
        MultiSentenceData sentence = new Level05Time_DayOfWeek_Month_Season(mode).makeSentence();
        if (isWrong) {
            return sentence;
        } else {
            sentence.clearIncorrectEnSentences();
            return sentence;
        }
    }

    private String getDayWeekMonth(int count, int dayWeekMonth, boolean isEnglish) {
        count++;
        if (isEnglish) {
            if (count == 1) {
                return "a " + DayWeekMonthEng.values()[dayWeekMonth].toString();
            } else {
                return count + " " + DayWeekMonthEng.values()[dayWeekMonth].toString() + "s";
            }
        } else {
            if (count == 1) {
                return count + " " + DayWeekMonthRus_1.values()[dayWeekMonth].toString();
            } else if (count >= 2 && count <= 4) {
                return count + " " + DayWeekMonthRus_2_4.values()[dayWeekMonth].toString();
            } else {
                return count + " " + DayWeekMonthRus_5_10.values()[dayWeekMonth].toString();
            }
        }
    }

}
//
//        Мне лучше
//        I feel better
//
//        Лучше позно чем никогда
//        Better late then never