package com.holypasta.trainer.levels;

import java.util.Random;

public class Level02 {

    public static String[] makeSentance(int score) {
        int where = new Random().nextInt(9);
        if (score < 17) {
            return part01_pronoun(where);
        } else {
            if (score < 34) {
                return part02_interrogative(where/3);
            } else {
                if (score < 50) {
                    return part03_interrogative_pronoun(where/3);
                } else {
                    switch (new Random().nextInt(3)) {
                        case 0:
                            return part01_pronoun(where);
                        case 1:
                            return part02_interrogative(where/3);
                        case 2:
                            return part03_interrogative_pronoun(where/3);
                    }
                }
            }
        }
        return new String[]{"Почему мы знаем его?", "Why do we know him?"};
    }

    private static String[] part01_pronoun(int where) {
        String[][] result;
        switch (where) {
            case 0: // future ?
                result = new String[][]{
                        {"Она полюбит его?", "Will she love him?"},
                        {"Мы увидим тебя?", "Will we see you?"},
                        {"Я буду знать их?", "Will I know them?"},
                        {"Ты спросишь её?", "Will you ask her?"},
                        {"Они ответят нам?", "Will they answer us?"},
                        {"Ты дашь мне?", "Will you give me?"},
                        {"Я буду говорить им?", "Will I speak them?"}};
                break;
            case 1: // future +
                result = new String[][]{
                        {"Она полюбит его", "She will love him"},
                        {"Мы увидим тебя", "We will see you"},
                        {"Я буду знать их", "I will know them"},
                        {"Ты спросишь её", "You will ask her"},
                        {"Они ответят нам", "They will answer us"},
                        {"Ты дашь мне", "You will give me"},
                        {"Я буду говорить им", "I will speak them"}};
                break;
            case 2: // future -
                result = new String[][]{
                        {"Она не полюбит его", "She will not love him", "She won't love him"},
                        {"Мы не увидим тебя", "We will not see you", "We won't see you"},
                        {"Я не буду знать их", "I will not know them", "I won't know them"},
                        {"Ты не спросишь её", "You will not ask her", "You won't ask her"},
                        {"Они не ответят нам", "They will not answer us", "They won't answer us"},
                        {"Ты не дашь мне", "You will not give me", "You won't give me"},
                        {"Я не буду говорить им", "I will not speak them", "I won't speak them"}};
                break;
            case 3: // present ?
                result = new String[][]{
                        {"Она любит его?", "Does she love him?"},
                        {"Мы видим тебя?", "Do we see you?"},
                        {"Я знаю их?", "Do I know them?"},
                        {"Ты спрашиваешь её?", "Do you ask her?"},
                        {"Они отвечают нам?", "Do they answer us?"},
                        {"Ты даёшь мне?", "Do you give me?"},
                        {"Я говорю им?", "Do I speak them?"}};
                break;
            case 4: // present +
                result = new String[][]{
                        {"Она любит его", "She loves him"},
                        {"Мы видим тебя", "We see you"},
                        {"Я знаю их", "I know them"},
                        {"Ты спрашиваешь её", "You ask her"},
                        {"Они отвечают нам", "They answer us"},
                        {"Ты даёшь мне", "You give me"},
                        {"Я говорю им", "I speak them"}};
                break;
            case 5: // present -
                result = new String[][]{
                        {"Она не любит его", "She does not love him", "She don't love him"},
                        {"Мы не видим тебя", "We do not see you", "We don't see you"},
                        {"Я не знаю их", "I do not know them", "I don't know them"},
                        {"Ты не спрашиваешь её", "You do not ask her", "You don't ask her"},
                        {"Они не отвечают нам", "They do not answer us", "They don't answer us"},
                        {"Ты не даёшь мне", "You do not give me", "You don't give me"},
                        {"Я не говорю им", "I do not speak them", "I don't speak them"}};
                break;
            case 6: // past ?
                result = new String[][]{
                        {"Она любила его?", "Did she love him?"},
                        {"Мы видели тебя?", "Did we see you?"},
                        {"Я знали их?", "Did I know them?"},
                        {"Ты спрашивал её?", "Did you ask her?"},
                        {"Они отвечали нам?", "Did they answer us?"},
                        {"Ты давала мне?", "Did you give me?"},
                        {"Я говорил им?", "Did I speak them?"}};
                break;
            case 7: // past +
                result = new String[][]{
                        {"Она любила его", "She loved him"},
                        {"Мы видели тебя", "We saw you"},
                        {"Я знали их", "I knew them"},
                        {"Ты спрашивал её", "You asked her"},
                        {"Они отвечали нам", "They answered us"},
                        {"Ты давала мне", "You gave me"},
                        {"Я говорил им", "I spoke them"}};
                break;
            default: // past -
                result = new String[][]{
                        {"Она не любила его", "She did not love him", "She didn't love him"},
                        {"Мы не видели тебя", "We did not see you", "We didn't see you"},
                        {"Я не знали их", "I did not know them", "I didn't know them"},
                        {"Ты не спрашивал её", "You did not ask her", "You didn't ask her"},
                        {"Они не отвечали нам", "They did not answer us", "They didn't answer us"},
                        {"Ты не давала мне", "You did not give me", "You didn't give me"},
                        {"Я не говорил им", "I did not speak them", "I didn't speak them"}};
                break;
        }
        return result[new Random().nextInt(result.length)];
    }

//        String[][] verbs01 = new String[][]{
//                // infinitive
//                {"love", "see",   "know", },
//                // present +
//                {"loves", "sees",  "knows", },
//                // past +
//                {"loved", "saw",  "knew", }};
//        String[][] verbs02 = new String[][]{
//                // infinitive
//                {"ask","answer","give","hope","speak","travel"},
//                // present +
//                {"asks","answers","gives","hopes","speaks","travels"},
//                // past +
//                {"asked","answered","gave","hoped","spoke","traveled"}};

    private static String[] part02_interrogative(int where) {
        String[][] result;
        switch (where) {
            case 0: // future ?
                result = new String[][]{
                        {"Кого ты полюбишь?", "Who Will you love?"},
                        {"Как он будет жить?", "How will he live?"},
                        {"Когда это будем работать?", "When will it work?"},
                        {"Что она откроет?", "What will she open?"},
                        {"Почему они закроют?", "Why will they close?"},
                        {"Где мы начнем?", "Where will we start?"},
                        {"Когда они закончат?", "When will they finish?"},
                        {"Что я увижу?", "What will I see?"},
                        {"Куда ты придешь?", "Where will you come?"},
                        {"Как он будет ходить?", "How will he go?"},
                        {"Когда мы будем знать?", "When will we know?"},
                        {"Когда они будут думать?", "When will they think?"},

                        {"Кого он спросит?", "Who will he ask?"},
                        {"Что она ответит?", "What will she answer?"},
                        {"Что мы дадим?", "What will we give?"},
                        {"Почему я буду надеяться?", "Why will I hope?"},
                        {"Как они будут говорить?", "How will they speak?"},
                        {"Когда мы будем путешествовать?", "When will we travel?"}};
                break;
            case 1: // present ?
                result = new String[][]{
                        {"Кого ты любишь?", "Who do you love?"},
                        {"Как он живет?", "How does he live?"},
                        {"Что она открывает?", "What does she open?"},
                        {"Почему они закрывают?", "Why do they close?"},
                        {"Что я вижу?", "What do I see?"},
                        {"Как он ходит?", "How does he go?"},
                        {"Когда они думают?", "When do they think?"},

                        {"Кого он спрашивает?", "Who does he ask?"},
                        {"Что она отвечает?", "What does she answer?"},
                        {"Что мы даём?", "What do we give?"},
                        {"Почему я надеюсь?", "Why do I hope?"},
                        {"Как они говорят?", "How do they speak?"}};
                break;
            default: // past ?
                result = new String[][]{
                        {"Кого ты любил?", "Who did you love?"},
                        {"Как он жил?", "How did he live?"},
                        {"Когда это работало?", "When did it work?"},
                        {"Что она открыла?", "What did she open?"},
                        {"Почему они закрыли?", "Why did they close?"},
                        {"Где мы начинали?", "Where did we start?"},
                        {"Когда они заканчивали?", "When did they finish?"},
                        {"Что я видел?", "What did I see?"},
                        {"Куда ты приходил?", "Where did you come?"},
                        {"Как он ходила?", "How did he go?"},
                        {"Когда мы знали?", "When did we know?"},
                        {"Когда они думали?", "When did they think?"},

                        {"Кого он спрашивал?", "Who did he ask?"},
                        {"Что она отвечала?", "What did she answer?"},
                        {"Что мы даём?", "What did we give?"},
                        {"Почему я надеялся?", "Why did I hope?"},
                        {"Как они говорили?", "How did they speak?"},
                        {"Когда мы путешествовали?", "When did we travel?"}};
                break;
        }
        return result[new Random().nextInt(result.length)];
    }

    private static String[] part03_interrogative_pronoun(int where) {
        String[][] result;
        switch (where) {
            case 0: // future ?
                result = new String[][]{
                        {"Когда ты полюбишь их?", "When Will you love them?"},
                        {"Что она откроет ему?", "What will she open his?"},
                        {"Почему они закроют нас?", "Why will they close us?"},
                        {"Когда я увижу его?", "When will I see his?"},
                        {"Когда мы будем знать её?", "When will we know her?"},

                        {"Когда он спросит её?", "When will he ask her?"},
                        {"Что она ответит ему?", "What will she answer his?"},
                        {"Что мы дадим ему?", "What will we give his?"},
                        {"Как они будут говорить нам?", "How will they speak us?"}};
                break;
            case 1: // present ?
                result = new String[][]{
                        {"Что она открывает ему?", "What does she open his?"},
                        {"Почему они закрывают нас?", "Why do they close us?"},
                        {"Почему мы знаем её?", "Why do we know her?"},

                        {"Как он спросит её?", "How does he ask her?"},
                        {"Что она отвечает ему?", "What does she answer his?"},
                        {"Что мы даём ему?", "What do we give his?"},
                        {"Как они говорят нам?", "How do they speak us?"}};
                break;
            default: // past ?
                result = new String[][]{
                        {"Когда ты любила их?", "When did you love them?"},
                        {"Что она открывала ему?", "What did she open his?"},
                        {"Почему они закрыли нас?", "Why did they close us?"},
                        {"Когда я видел его?", "When did I see his?"},

                        {"Когда он спрашивал её?", "When did he ask her?"},
                        {"Что она отвечала ему?", "What did she answer his?"},
                        {"Что мы дали ему?", "What did we give his?"},
                        {"Как они говорили нам?", "How did they speak us?"}};
                break;
        }
        return result[new Random().nextInt(result.length)];
    }

//    private String[] trash() {
//        String[][] verbs01 = new String[][]{
//                // infinitive
//                {"love", "live", "work", "open", "close", "start", "finish",
//                        "see", "come", "go", "know", "think"},
//                // present +
//                {"loves", "lives", "works", "opens", "closes", "starts",
//                        "finishes", "sees", "comes", "goes", "knows", "thinks"},
//                // past +
//                {"loved", "lived", "worked", "opened", "closed", "started",
//                        "finished", "saw", "came", "went", "knew", "thought"}};
//        String[][] verbs02 = new String[][]{
//                // infinitive
//                {"ask","answer","give","hope","speak","travel"},
//                // present +
//                {"asks","answers","gives","hopes","speaks","travels"},
//                // past +
//                {"asked","answered","gave","hoped","spoke","traveled"}};
//        String[] pronounsRU = new String[]{"Я", "Ты", "Он", "Она", "Это",
//                "Они", "Мы"};
//        String[][] pronounsEN = new String[][]{
//                {"I", "you", "he", "she", "it", "they", "we"},
//                {"I", "You", "He", "She", "It", "They", "We"}};
//        String sentenceRU;
//        String[] sentenceEN;
//        int where = ((int) (Math.random() * 9)) + 1;
//        int time = ((where + 2) / 3);
//        int why = (int) (Math.random() * 7);
//        int verb = (int) (Math.random() * 12);
//        switch (where) {
//            case 1:// future ?
//                sentenceRU = pronounsRU[why] + " "
//                        + RuVerbs01.make(time, why, verb) + "?";
//                sentenceEN = new String[]
//                        {"Will " + pronounsEN[0][why] + " " + verbs01[0][verb] + "?"};
//                break;
//            case 2:// future +
//                sentenceRU = pronounsRU[why] + " "
//                        + RuVerbs01.make(time, why, verb);
//                sentenceEN = new String[]
//                        {pronounsEN[1][why] + " will " + verbs01[0][verb]};
//                break;
//            case 3:// future -
//                sentenceRU = pronounsRU[why] + " не "
//                        + RuVerbs01.make(time, why, verb);
//                sentenceEN = new String[]{
//                        pronounsEN[1][why] + " will not " + verbs01[0][verb],
//                        pronounsEN[1][why] + " won't " + verbs01[0][verb]};
//                break;
//            case 4:// present ?
//                sentenceRU = pronounsRU[why] + " "
//                        + RuVerbs01.make(time, why, verb) + "?";
//                sentenceEN = new String[]{
//                        (((why > 1) && (why < 5)) ? "Does " : "Do ")
//                                + pronounsEN[0][why] + " " + verbs01[0][verb] + "?"};
//                break;
//            case 5:// present +
//                sentenceRU = pronounsRU[why] + " "
//                        + RuVerbs01.make(time, why, verb);
//                String sentenceEN_1;
//                sentenceEN_1 = pronounsEN[1][why] + " ";
//                if ((why > 1) && (why < 5)) {//he she it
//                    sentenceEN_1 += verbs01[1][verb];
//                } else {
//                    sentenceEN_1 += verbs01[0][verb];
//                }
//                sentenceEN = new String[]{sentenceEN_1};
//                break;
//            case 6:// present -
//                sentenceRU = pronounsRU[why] + " не "
//                        + RuVerbs01.make(time, why, verb);
//                sentenceEN = new String[]{
//                        pronounsEN[1][why] + (((why > 1) && (why < 5)) ? " does" : " do") + " not "
//                                + verbs01[0][verb],
//                        pronounsEN[1][why] + (((why > 1) && (why < 5)) ? " does" : " do") + "n't "
//                                + verbs01[0][verb]};
//                break;
//            case 7:// past ?
//                sentenceRU = pronounsRU[why] + " "
//                        + RuVerbs01.make(time, why, verb) + "?";
//                sentenceEN = new String[]{
//                        "Did " + pronounsEN[0][why] + " " + verbs01[0][verb] + "?"};
//                break;
//            case 8:// past +
//                sentenceRU = pronounsRU[why] + " "
//                        + RuVerbs01.make(time, why, verb);
//                sentenceEN = new String[]{pronounsEN[1][why] + " " + verbs01[2][verb]};
//                break;
//            default:// past -
//                sentenceRU = pronounsRU[why] + " не "
//                        + RuVerbs01.make(time, why, verb);
//                sentenceEN = new String[]{
//                        pronounsEN[1][why] + " did not " + verbs01[0][verb],
//                        pronounsEN[1][why] + " didn't " + verbs01[0][verb]};
//                break;
//        }
//
//        String[] resultString = new String[sentenceEN.length + 1];
//        resultString[0] = sentenceRU;
//        for (int i = 1; i < sentenceEN.length + 1; i++) {
//            resultString[i] = sentenceEN[i - 1];
//        }
//        return resultString;
//    }


}
