package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;
import com.holypasta.trainer.util.RuVerbs03WantLike;

import java.util.Random;

/**
 * Created by q1bot on 05.03.2015.
 */
public class Level03 extends AbstractLevel {

    private enum PronounsI      { I, Me, My };
    private enum PronounsYou    { You, Your };
    private enum PronounsHeShe  { He, Him, His, She, Her };
    private enum PronounsWe     { We, Us, Our };
    private enum PronounsThey   { They, Them, Their };

    private final String[][] VARIANTS_1_TOBE = new String[][] {
            { "здесь", "в Индии", "в Москве", "c ней", "твоим другом" },
            { "here", "in India", "in Moscow", "with her", "your friend" }};
    private final String[][] VARIANTS_2_WANT_LIKE = new String[][] {
            { "", "летать", "говорить", "танцевать", "быть твоим другом" },
            { "", "to fly", "to speak", "to dance", "to be your friend" }};
    private final int VARIANTS_3_SIZE = 11;

    @Override
    public MultiSentenceData makeSentence(int mode) {
        SentenceParamData time = new SentenceParamData(3);
        SentenceParamData form = new SentenceParamData(3);
        SentenceParamData verb = new SentenceParamData(2);
        SentenceParamData who = new SentenceParamData(PRONOUN_IT, pronounsEN[0].length, true);
        who.nextRandom();
        int who0 = who.value();
        SentenceParamData partOfLesson = new SentenceParamData(3);
        if (partOfLesson.value() == 2) { // part 3 ~ 11%
            partOfLesson.nextRandom();
        }
        SentenceParamData part1variant = new SentenceParamData(VARIANTS_1_TOBE[0].length);
        SentenceParamData part2variant = new SentenceParamData(VARIANTS_2_WANT_LIKE[0].length);
        SentenceParamData part3variant = new SentenceParamData(VARIANTS_3_SIZE);
        int variant = 0;
        switch (partOfLesson.value()) {
            case 0:
                variant = part1variant.value();
                if (variant == 4) { // "быть твоим другом"
                    int[] whoEnum = new int[] { 0, 2, 3 };
                    who = new SentenceParamData(PRONOUN_IT,
                            whoEnum[new Random().nextInt(whoEnum.length)],
                            pronounsEN[0].length, true);
                }
                break;
            case 1:
                variant = part2variant.value();
                time = new SentenceParamData(TIME_FUTURE, 3, true);
                break;
            case 2:
                variant = part3variant.value();
                break;
        }
        MultiSentenceData sentence = makeSentence(partOfLesson.value(), time.value(), form.value(),
                who.value(), who.value(), variant, verb.value());
        if (mode == MODE_HARD) {
            return sentence;
        } else { // MODE_EASY
            Random random = new Random();
            if (random.nextBoolean()) { // 50% true
                return sentence;
            }
            switch (partOfLesson.value()) {
                case 0:
                    if (random.nextBoolean()) { // 50% change time
                        time.nextRandom();
                    } else { // 50% change key word
                        who.nextRandom();
                    }
                    break;
                case 1:
                    int randomInt = random.nextInt(3);
                    if (randomInt == 0) { // 50% change time
                        time.nextRandom();
                    } else if (randomInt == 1) { // 50% change key word
                        who.nextRandom();
                    } else {
                        verb.nextRandom();
                    }
                    break;
                case 2:
                    who.nextRandom();
                    break;
            }
            return genWrongSentence(sentence, makeSentence(partOfLesson.value(), time.value(), form.value(), who0, who.value(), variant, verb.value()));
        }
    }

    public MultiSentenceData makeSentence(int partOfLesson, int time, int form, int who, int who1, int variant, int verb) {
        System.out.println("!!! part = " + partOfLesson + ", time = " + time);
        switch (partOfLesson) {
            case 0:
                return part01_to_be(form, time, who, who1, variant);
            case 1:
                return part02_want_like(form, time, who, who1, verb, variant);
            case 2:
                return part03_my_your_his_her_our_their(who, who1, variant);
        }
        return null;
    }

    private MultiSentenceData part01_to_be(int form, int time, int who, int who1, int variant) {
        return makeToBe(form, time, who, who1, VARIANTS_1_TOBE[0][variant], VARIANTS_1_TOBE[1][variant]);
    }

    private MultiSentenceData part02_want_like(int form, int time, int who, int who1, int verb, int variant) {
        if (time == TIME_FUTURE) {
            return new MultiSentenceData(ERROR_MESSAGE, ERROR_MESSAGE);
        }
        RuVerbs03WantLike ruVerbs = new RuVerbs03WantLike();
        MultiSentenceData sentence = makeBaseForm(ruVerbs, verbs03, form, time, who, who1, verb
                , verb == ruVerbs.VERB_LIKE ? pronounsRU3 : pronounsRU
                , VARIANTS_2_WANT_LIKE[0][variant], VARIANTS_2_WANT_LIKE[1][variant]);
        return sentence;
    }

    private MultiSentenceData part03_my_your_his_her_our_their(int who0, int who1, int variant) {
        String wrongSentence = ERROR_MESSAGE;
        String[] sentence = new String[] { ERROR_MESSAGE, ERROR_MESSAGE };
        switch (variant) {
            case 0:
                sentence = new String[] { "Его зовут Олег", "His name is Oleg" };
                wrongSentence = wrong(PronounsHeShe.His) + " name is Oleg";
                break;
            case 1:
                sentence = new String[] { "Ее зовут Дарья", "Her name is Darya" };
                wrongSentence = wrong(PronounsHeShe.Her) + " name is Darya";
                break;
            case 2:
                sentence = new String[] { "Как ее зовут?", "What is her name?" };
                wrongSentence = "What is " + wrong(PronounsHeShe.Her).toLowerCase() + " name?";
                break;
            case 3:
                sentence = new String[] { "Он наш друг", "He is our friend" };
                wrongSentence = "He is " + wrong(PronounsWe.Our).toLowerCase() + " friend";
                break;
            case 4:
                sentence = new String[] { "Это их проблема", "It's their problem" };
                wrongSentence = "It's " + wrong(PronounsThey.Their).toLowerCase() + " problem";
                break;
            case 5:
                sentence = new String[] { "Он не мой друг",
                        "He is not my friend", "He's not my friend", "He isn't my friend" };
                wrongSentence = "He's not " + wrong(PronounsI.My).toLowerCase() + " friend";
                break;
            case 6:
                sentence = new String[] { "Как тебя зовут?", "What is your name?" };
                wrongSentence = "What is " + wrong(PronounsYou.Your).toLowerCase() + " name?";
                break;
            case 7:
                sentence = new String[] { "Его зовут Данила", "His name is Danila" };
                wrongSentence = wrong(PronounsHeShe.His) + " name is Danila";
                break;
            case 8:
                sentence = new String[] { "Ее зовут Ольга", "Her name is Olga" };
                wrongSentence = wrong(PronounsHeShe.Her) + " name is Olga";
                break;
            case 9:
                sentence = new String[] { "Как его зовут?", "What is his name?" };
                wrongSentence = "What is " + wrong(PronounsHeShe.His).toLowerCase() + " name?";
                break;
            case VARIANTS_3_SIZE - 1:
                sentence = new String[] { "Он ее друг", "He is her friend" };
                wrongSentence = "He is " + wrong(PronounsHeShe.Her).toLowerCase() + " friend";
                break;
        }
        if (who0 == who1) {
            return new MultiSentenceData(sentence);
        } else {
            return new MultiSentenceData(sentence[0], wrongSentence);
        }
    }

    private String wrong(PronounsI pr) {
        SentenceParamData spd = new SentenceParamData(pr.ordinal(), PronounsI.values().length);
        spd.nextRandom();
        return PronounsI.values()[spd.value()].toString();
    }

    private String wrong(PronounsYou pr) {
        SentenceParamData spd = new SentenceParamData(pr.ordinal(), PronounsYou.values().length);
        spd.nextRandom();
        return PronounsYou.values()[spd.value()].toString();
    }

    private String wrong(PronounsHeShe pr) {
        SentenceParamData spd = new SentenceParamData(pr.ordinal(), PronounsHeShe.values().length);
        spd.nextRandom();
        return PronounsHeShe.values()[spd.value()].toString();
    }

    private String wrong(PronounsWe pr) {
        SentenceParamData spd = new SentenceParamData(pr.ordinal(), PronounsWe.values().length);
        spd.nextRandom();
        return PronounsWe.values()[spd.value()].toString();
    }

    private String wrong(PronounsThey pr) {
        SentenceParamData spd = new SentenceParamData(pr.ordinal(), PronounsThey.values().length);
        spd.nextRandom();
        return PronounsThey.values()[spd.value()].toString();
    }

//    Я поговорю с тобой
//    I will talk with you
//
//    Я тебе помогу
//    I will help you
//
//    Он не помог мне
//    He didn't help me
//    He did not help me
//
//    Я возьму твою машину
//    I will take your car
//
//    Она пришла ко мне
//    She came to me
//
//    Когда ты пойдешь к нему?
//    When will you go to him?
//
//    Я видела его вчера
//    I saw him yesterday
//
//    Когда ты видел ее?
//    When did you see her?
//
//    Я буду в Москве
//    I will be in Moscow
//
//    Я был в Индии
//    I was in India
//
//    Она сдесь
//    She is here
//
//    Ты сдесь?
//    Are you here?
//
//    Она сдесь?
//    Is she here?
//
//    Он был сдесь?
//    Was he here?
//
//    Они были с вами?
//    Were they with you?
//
//    Ты будешь сдесь?
//    Will you be here?
//
//    Когда ты пойдешь туда?
//    When will you go there?
//
//    Он мой друг
//    He is my friend
//
//    Я говорю
//    I am speaking
//
//    Я говорил
//    I was speaking
//
//    Я буду говорить
//    I will be speaking
//
//    Я хочу говорить
//    I want to speak
//
//    Мне нравится танцевать
//    I like to dance
//
//    Я хочу быть твоим другом
//    I want to be your friend
//
//    Я буду твоим другом
//    I will be your friend
//
//    Я не хочу быть твоим другом
//    I don't want to be your friend
//
//    Может быть я захочу быть твоим другом
//    Maybe I will want to be your friend
//
//    Почему ты не хочешь?
//    Why don't you want?
//
//    Ты хочешь?
//    Do you want?
//
//    Ты не хочешь
//    You didn't want
//
//    Ты хочешь?
//    Do you want?
//
//    Я тебя поцелую, только потом, если захочешь
//    I will kiss you if you want, later
//
//    Его зовут олег
//    His name is Oleg
//
//    Ее зовут Дарья
//    Her name is Darya
//
//    Как ее зовут?
//    What is her name?
//
//    Он наш друг
//    He is our friend
//
//    Это их проблема
//    It's their problem
//
//    Он ее друг
//    He is her friend

}
