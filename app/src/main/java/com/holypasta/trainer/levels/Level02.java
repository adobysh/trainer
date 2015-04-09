package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Level02 extends AbstractLevel {

    @Override
    public MultiSentenceData makeSentence(int mode) {
        SentenceParamData time = new SentenceParamData(3);
        SentenceParamData form = new SentenceParamData(3);
        SentenceParamData partOfLesson = new SentenceParamData(3);
        SentenceParamData part1variant = new SentenceParamData(7);
        SentenceParamData part2variant = new SentenceParamData(18);
        SentenceParamData part3variant = new SentenceParamData(9);
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
        }
        MultiSentenceData sentence = makeSentence(partOfLesson.value(), time.value(), form.value(), variant, false);
        if (mode == MODE_HARD) {
            return sentence;
        } else { // MODE_EASY
            Random random = new Random();
            if (random.nextBoolean()) { // 50% true
                return sentence;
            }
            if (random.nextBoolean()) { // 50% change time
                time.nextRandom();
                return genWrongSentence(sentence, makeSentence(partOfLesson.value(), time.value(), form.value(), variant, false));
            } else { // 50% change key word
                return genWrongSentence(sentence, makeSentence(partOfLesson.value(), time.value(), form.value(), variant, true));
            }
        }
    }

    public MultiSentenceData makeSentence(int partOfLesson, int time, int form, int variant, boolean wrong) {
        System.out.println("!!! time = " + time + ", wrong = " + wrong);
        List<String> result = new ArrayList<>();
        switch (partOfLesson) {
            case 0:
                result = part01_pronoun(genWhere(form, time), variant);
                break;
            case 1:
                result = part02_interrogative(time, variant);
                break;
            case 2:
                result = part03_interrogative_pronoun(time, variant);
                break;
        }
        if (wrong) {
            return new MultiSentenceData(result.get(1), result.get(0));
        } else {
            result.remove(0);
            return new MultiSentenceData(result);
        }
    }

    private List<String> part01_pronoun(int where, int variant) {
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
                        {"Он знал их?", "Did he know them?"},
                        {"Ты спрашивал её?", "Did you ask her?"},
                        {"Они отвечали нам?", "Did they answer us?"},
                        {"Ты давал мне?", "Did you give me?"},
                        {"Она говорила им?", "Did she speak them?"}};
                break;
            case 7: // past +
                result = new String[][]{
                        {"Она любила его", "She loved him"},
                        {"Мы видели тебя", "We saw you"},
                        {"Он знал их", "He knew them"},
                        {"Ты спрашивал её", "You asked her"},
                        {"Они отвечали нам", "They answered us"},
                        {"Ты давал мне", "You gave me"},
                        {"Она говорила им", "She spoke them"}};
                break;
            case 8: // past -
                result = new String[][]{
                        {"Она не любила его", "She did not love him", "She didn't love him"},
                        {"Мы не видели тебя", "We did not see you", "We didn't see you"},
                        {"Он не знал их", "He did not know them", "He didn't know them"},
                        {"Ты не спрашивал её", "You did not ask her", "You didn't ask her"},
                        {"Они не отвечали нам", "They did not answer us", "They didn't answer us"},
                        {"Ты не давал мне", "You did not give me", "You didn't give me"},
                        {"Она не говорила им", "She did not speak them", "She didn't speak them"}};
                break;
            default:
                result = new String[][]
                        { { ERROR_MESSAGE }, { ERROR_MESSAGE }, { ERROR_MESSAGE } };
        }
        List<String> resultList = new ArrayList<String>(Arrays.asList(result[variant]));
        String sentence = resultList.get(resultList.size() - 1);
        String wrong = replacePronouns(sentence);
        resultList.add(0, wrong);
        return resultList;
    }

    private List<String> part02_interrogative(int time, int variant) {
        String[][] result;
        switch (time) {
            case 0: // future ?
                result = new String[][]{
                        {"Когда ты полюбишь?", "When Will you love?"},
                        {"Как он будет жить?", "How will he live?"},
                        {"Когда ты будешь работать?", "When will you work?"},
                        {"Что она откроет?", "What will she open?"},
                        {"Почему они закроют?", "Why will they close?"},
                        {"Где мы начнем?", "Where will we start?"},
                        {"Когда они закончат?", "When will they finish?"},
                        {"Что я увижу?", "What will I see?"},
                        {"Почему ты придешь?", "Why will you come?"},
                        {"Как он будет ходить?", "How will he go?"},
                        {"Что мы будем знать?", "What will we know?"},
                        {"Как они будут думать?", "How will they think?"},

                        {"Что он спросит?", "What will he ask?"},
                        {"Что она ответит?", "What will she answer?"},
                        {"Что мы дадим?", "What will we give?"},
                        {"Почему я буду надеяться?", "Why will I hope?"},
                        {"Как они будут говорить?", "How will they speak?"},
                        {"Когда мы будем путешествовать?", "When will we travel?"}};
                break;
            case 1: // present ?
                result = new String[][]{
                        {"Почему ты любишь?", "Why do you love?"},
                        {"Как он живет?", "How does he live?"},
                        {"Как ты работаешь?", "How do you work?"},
                        {"Что она открывает?", "What does she open?"},
                        {"Почему они закрывают?", "Why do they close?"},
                        {"Как мы начинаем?", "How do we start?"},
                        {"Что они заканчивают?", "What do they finish?"},
                        {"Что я вижу?", "What do I see?"},
                        {"Почему ты пришел?", "Why do you come?"},
                        {"Как он ходит?", "How does he go?"},
                        {"Что мы знаем?", "What do we know?"},
                        {"Как они думают?", "How do they think?"},

                        {"Что он спрашивает?", "What does he ask?"},
                        {"Что она отвечает?", "What does she answer?"},
                        {"Что мы даём?", "What do we give?"},
                        {"Почему я надеюсь?", "Why do I hope?"},
                        {"Как они говорят?", "How do they speak?"},
                        {"Почему мы путешествуем?", "Why do we travel?"}};
                break;
            default: // past ?
                result = new String[][]{
                        {"Как ты любил?", "How did you love?"},
                        {"Как он жил?", "How did he live?"},
                        {"Когда ты работал?", "When did you work?"},
                        {"Что она открыла?", "What did she open?"},
                        {"Почему они закрыли?", "Why did they close?"},
                        {"Где мы начинали?", "Where did we start?"},
                        {"Когда они заканчивали?", "When did they finish?"},
                        {"Что он видел?", "What did he see?"},
                        {"Почему ты приходил?", "Why did you come?"},
                        {"Как он ходил?", "How did he go?"},
                        {"Что мы знали?", "What did we know?"},
                        {"Как они думали?", "How did they think?"},

                        {"Что он спрашивал?", "What did he ask?"},
                        {"Что она отвечала?", "What did she answer?"},
                        {"Что мы давали?", "What did we give?"},
                        {"Почему он надеялся?", "Why did he hope?"},
                        {"Как они говорили?", "How did they speak?"},
                        {"Когда мы путешествовали?", "When did we travel?"}};
                break;
        }
        List<String> resultList = new ArrayList<>(Arrays.asList(result[variant]));
        String sentence = resultList.get(resultList.size() - 1);
        String wrong = replaceQuestion(sentence);
        resultList.add(0, wrong);
        return resultList;
    }

    private List<String> part03_interrogative_pronoun(int time, int variant) {
        String[][] result;
        switch (time) {
            case 0: // future ?
                result = new String[][]{
                        {"Когда ты полюбишь их?", "When will you love them?"},
                        {"Что она откроет ему?", "What will she open him?"},
                        {"Почему они закроют нас?", "Why will they close us?"},
                        {"Когда я увижу его?", "When will I see him?"},
                        {"Когда мы узнаем её?", "When will we know her?"},

                        {"Когда он спросит её?", "When will he ask her?"},
                        {"Что она ответит ему?", "What will she answer him?"},
                        {"Что мы дадим ему?", "What will we give him?"},
                        {"Как они будут говорить нам?", "How will they speak us?"}};
                break;
            case 1: // present ?
                result = new String[][]{
                        {"Почему ты любишь их?", "Why do you love them?"},
                        {"Что она открывает ему?", "What does she open him?"},
                        {"Почему они закрывают нас?", "Why do they close us?"},
                        {"Почему я вижу его?", "Why do I see him?"},
                        {"Почему мы знаем её?", "Why do we know her?"},

                        {"Как он спросит её?", "How does he ask her?"},
                        {"Что она отвечает ему?", "What does she answer him?"},
                        {"Что мы даём ему?", "What do we give him?"},
                        {"Как они говорят нам?", "How do they speak us?"}};
                break;
            default: // past ?
                result = new String[][]{
                        {"Когда ты любила их?", "When did you love them?"},
                        {"Что она открывала ему?", "What did she open him?"},
                        {"Почему они закрыли нас?", "Why did they close us?"},
                        {"Когда она видела его?", "When did she see him?"},
                        {"Когда мы узнали её?", "When did we know her?"},

                        {"Когда он спрашивал её?", "When did he ask her?"},
                        {"Что она отвечала ему?", "What did she answer him?"},
                        {"Что мы дали ему?", "What did we give him?"},
                        {"Как они говорили нам?", "How did they speak us?"}};
                break;
        }
        List<String> resultList = new ArrayList<String>(Arrays.asList(result[variant]));
        String sentence = resultList.get(resultList.size() - 1);
        String wrong;
        if (new Random().nextBoolean()) {
            wrong = replaceQuestion(sentence);
        } else {
            wrong = replacePronouns(sentence);
        }
        resultList.add(0, wrong);
        return resultList;
    }

    private String replacePronouns(String sentence) {
        final String[] pronouns = { "me", "you", "him", "her", "us", "them" };
        for (int i = 0; i < pronouns.length; i++) {
            if (sentence.endsWith(pronouns[i])
                    || sentence.endsWith(pronouns[i] + "?")) {
                boolean question = sentence.endsWith(pronouns[i] + "?");
                String oldSuffix = pronouns[i];
                oldSuffix += question ? "?$" : "$";
                SentenceParamData spd = new SentenceParamData(i, pronouns.length);
                String newSuffix = pronouns[spd.nextRandom()];
                newSuffix += question ? "?" : "";
                String wrong = sentence.replaceAll(oldSuffix, newSuffix);
                return wrong;
            }
        }
        return ERROR_MESSAGE;
    }

    private String replaceQuestion(String sentence) {
        final String[] questions = { "What", "Who", "Where", "When", "Why", "How" };
        for (int i = 0; i < questions.length; i++) {
            if (sentence.startsWith(questions[i])) {
                String oldPrefix = questions[i];
                SentenceParamData spd = new SentenceParamData(i, questions.length);
                String newPrefix = questions[spd.nextRandom()];
                String wrong = sentence.replaceAll("^"+oldPrefix, newPrefix);
                return wrong;
            }
        }
        return ERROR_MESSAGE;
    }
}
