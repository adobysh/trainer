package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;
import com.holypasta.trainer.util.RuVerbs01;

import java.util.Random;

/**
 * Created by q1bot on 25.03.2015.
 */
public class Level05Time_DayOfWeek_Month_Season extends Level05 {

    private enum DayOfWeekRus { Понедельник, Вторник, Среда, Четверг, Пятница, Суббота, Воскресенье }
    private enum DayOfWeekRus_1 { Понедельник, Вторник, Среду, Четверг, Пятницу, Субботу, Воскресенье }
    private enum DayOfWeekEng { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }
    private enum MonthRus { Январь, Февраль, Март, Апрель, Май, Июнь, Июль, Август, Сентябрь, Октябрь, Ноябрь, Декабрь }
    private enum MonthRus_1 { Январе, Феврале, Марте, Апреле, Мае, Июне, Июле, Августе, Сентябре, Октябре, Ноябре, Декабре }
    private enum MonthEng { January, February, March, April, May, June, July, August, September, October, November, December }
    private enum SeasonRus { Зимой, Весной, Летом, Осенью }
    private enum SeasonEng { Winter, Spring, Summer, Autumn }

    private final int TYPE_DAY_OF_WEEK = 0;
    private final int TYPE_MONTH = 1;
    private final int TYPE_SEASON = 2;
    private final int TYPES_COUNT = 3;

    public MultiSentenceData makeSentence() {
        SentenceParamData time = new SentenceParamData(TIME_PRESENT, 3, true);
        SentenceParamData dayOfWeek = new SentenceParamData(DayOfWeekRus.values().length);
        SentenceParamData month = new SentenceParamData(MonthRus.values().length);
        SentenceParamData season = new SentenceParamData(SeasonRus.values().length);
        SentenceParamData who = new SentenceParamData(PRONOUN_IT, pronounsRU.length, true);
        SentenceParamData who1 = new SentenceParamData(PRONOUN_IT, who.value(), pronounsRU.length, true);
        who1.nextRandom();
        while(who.value() == who1.value() ||
                (who.value() == PRONOUN_I && who1.value() == PRONOUN_WE) ||
                (who.value() == PRONOUN_WE && who1.value() == PRONOUN_I)) {
            who1.nextRandom();
        }
        SentenceParamData verb = new SentenceParamData(TIME_VERBS.length);
        Random random = new Random();
        int type = random.nextInt(TYPES_COUNT);
        int item = -1;
        switch (type) {
            case TYPE_DAY_OF_WEEK:
                item = dayOfWeek.value();
                break;
            case TYPE_MONTH:
                item = month.value();
                break;
            case TYPE_SEASON:
                item = season.value();
                break;
        }
        return genWrongSentence(makeSentence(time.value(), type, item, who.value(), who1.value(),
                        verb.value(), false),
                makeSentence(time.value(), type, item, who.value(), who1.value(),
                        verb.value(), true));
    }

    private MultiSentenceData makeSentence(int time, int type, int item,
            int who, int who1, int verb, boolean isWrong) {
        String suffixRus;
        String suffixEng;
        String in = "in";
        String on = "on";
        if (isWrong) {
            String[] wrongIn = { "on", "at" };
            in = wrongIn[new Random().nextInt(wrongIn.length)];
            String[] wrongOn = { "in", "at" };
            on = wrongOn[new Random().nextInt(wrongOn.length)];
        }
        switch (type) {
            case TYPE_DAY_OF_WEEK:
                suffixRus = (item == DayOfWeekRus_1.Вторник.ordinal() ? "во " : "в ")
                        + DayOfWeekRus_1.values()[item].toString().toLowerCase();
                suffixEng = on + " " + DayOfWeekEng.values()[item].toString();
                break;
            case TYPE_MONTH:
                suffixRus = "в " + MonthRus_1.values()[item].toString().toLowerCase();
                suffixEng = in + " " + MonthEng.values()[item].toString();
                break;
            default: //case TYPE_SEASON:
                suffixRus = SeasonRus.values()[item].toString().toLowerCase();
                suffixEng = in + " " + SeasonEng.values()[item].toString().toLowerCase();
                break;
        }
        if (TIME_VERBS[verb] == VERB_SEE) {
            suffixRus = pronounsRU2[who1].toLowerCase() + " " + suffixRus;
            suffixEng = pronounsEN2[who1].toLowerCase() + " " + suffixEng;
        }
        return makeBaseForm(new RuVerbs01(), verbs01, time, who, TIME_VERBS[verb], suffixRus, suffixEng);
    }
}
