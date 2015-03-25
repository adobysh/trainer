package com.holypasta.trainer.levels;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.util.RuVerbs;
import com.holypasta.trainer.util.RuVerbs01;
import com.holypasta.trainer.util.RuVerbs03ToBe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by q1bot on 11.01.15.
 */
public abstract class AbstractLevel implements Constants {

    enum Verbs { Love, Live, Work, Open, Close, Start, Finish,
            See, Come, Go, Know, Think }

    protected static final int TIME_FUTURE = 0;
    protected static final int TIME_PRESENT = 1;
    protected static final int TIME_PAST = 2;
    protected static final int FORM_QUESTION = 0;
    protected static final int FORM_POSITIVE = 1;
    protected static final int FORM_NEGATIVE = 2;
    protected static final int PRONOUN_I = 0;
    protected static final int PRONOUN_YOU = 1;
    protected static final int PRONOUN_HE = 2;
    protected static final int PRONOUN_SHE = 3;
    protected static final int PRONOUN_IT = 4;
    protected static final int PRONOUN_THEY = 5;
    protected static final int PRONOUN_WE = 6;
    protected final String[][] verbs01 = new String[][]{
            // infinitive
            {"love", "live", "work", "open", "close", "start", "finish",
                    "see", "come", "go", "know", "think"},
            // present +
            {"loves", "lives", "works", "opens", "closes", "starts",
                    "finishes", "sees", "comes", "goes", "knows", "thinks"},
            // past +
            {"loved", "lived", "worked", "opened", "closed", "started",
                    "finished", "saw", "came", "went", "knew", "thought"}};
    protected final String[][] verbs03 = new String[][]{
            // infinitive
            {"want", "like"},
            // present +
            {"wants", "likes"},
            // past +
            {"wanted", "liked"}};
    protected final String[] pronounsRU = new String[]{"Я", "Ты", "Он", "Она", "Это", "Они", "Мы"}; // Кто?
    protected final String[] pronounsRU2 = new String[]{"Меня", "Тебя", "Его", "Её", "Этого", "Их", "Нас"}; // Кого?
    protected final String[] pronounsRU3 = new String[]{"Мне", "Тебе", "Ему", "Ей", "Этому", "Им", "Нам"}; // Кому?
    protected final  String[][] pronounsEN = new String[][]{
            {"I", "you", "he", "she", "it", "they", "we"},
            {"I", "You", "He", "She", "It", "They", "We"}};
    protected final  String[] pronounsEN2 = new String[]{"Me", "You", "Him", "Her", "It", "Them", "Us"}; // todo - It ?

    public abstract MultiSentenceData makeSentence(int mode);

    // | ? + - | future
    // | ? + - | present
    // | ? + - | past
    protected int genWhere(int form, int time) {
        return time * 3 + form;
    }

    protected MultiSentenceData genWrongSentence(MultiSentenceData resultSentence, MultiSentenceData incorrectSentence) {
        resultSentence.setIncorrectEnSentences(incorrectSentence.getAllCorrectEnSentences());
        return resultSentence;
    }

    protected MultiSentenceData makeBaseForm(RuVerbs ruVerbs, String[][] verbs, int time, int who,
                                             int verb, String suffixRus, String suffixEng) {
        return makeBaseForm(ruVerbs, verbs, FORM_POSITIVE, time, who, who, verb,
                pronounsRU, suffixRus, suffixEng);
    }

    protected MultiSentenceData makeBaseForm(RuVerbs ruVerbs, String[][] verbs, int form, int time,
                                             int who0, int who1, int verb, String[] pronounsRU) {
        return makeBaseForm(ruVerbs, verbs, form, time, who0, who1, verb,
                pronounsRU, null, null);
    }

    protected MultiSentenceData makeBaseForm(RuVerbs ruVerbs, String[][] verbs
            , int form, int time, int who0, int who1, int verb
            , String[] pronounsRU, String suffixRus, String suffixEng) {
        String sentenceRU = "ошибка";
        String[] sentenceEN = { "ошибка" };
        int where = genWhere(form, time);
        switch (where) {
            case 0:// future ?
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]
                        {"Will " + pronounsEN[0][who0] + " " + verbs[0][verb]};
                break;
            case 1:// future +
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]
                        {pronounsEN[1][who0] + " will " + verbs[0][verb]};
                break;
            case 2:// future -
                sentenceRU = pronounsRU[who0] + " не "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][who0] + " will not " + verbs[0][verb],
                        pronounsEN[1][who0] + " won't " + verbs[0][verb]};
                break;
            case 3:// present ?
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]{
                        (((who1 > 1) && (who1 < 5)) ? "Does " : "Do ")
                                + pronounsEN[0][who0] + " " + verbs[0][verb]};
                break;
            case 4:// present +
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0, verb);
                String sentenceEN_1;
                sentenceEN_1 = pronounsEN[1][who0] + " ";
                if ((who1 > 1) && (who1 < 5)) {//he she it
                    sentenceEN_1 += verbs[1][verb];
                } else {
                    sentenceEN_1 += verbs[0][verb];
                }
                sentenceEN = new String[]{sentenceEN_1};
                break;
            case 5:// present -
                sentenceRU = pronounsRU[who0] + " не "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][who0] + (((who1 > 1) && (who1 < 5)) ? " does" : " do") + " not "
                                + verbs[0][verb],
                        pronounsEN[1][who0] + (((who1 > 1) && (who1 < 5)) ? " does" : " do") + "n't "
                                + verbs[0][verb]};
                break;
            case 6:// past ?
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]{
                        "Did " + pronounsEN[0][who0] + " " + verbs[0][verb]};
                break;
            case 7:// past +
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]{pronounsEN[1][who0] + " " + verbs[2][verb]};
                break;
            case 8:// past -
                sentenceRU = pronounsRU[who0] + " не "
                        + ruVerbs.make(time, who0, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][who0] + " did not " + verbs[0][verb],
                        pronounsEN[1][who0] + " didn't " + verbs[0][verb]};
                break;
        }
        return addSuffixesAndQuestionMark(sentenceRU, sentenceEN, form, suffixRus, suffixEng);
    }

    protected MultiSentenceData makeToBe(int form, int time, int who0, int who1, String suffixRus, String suffixEng) {
        String sentenceRU = "ошибка";
        String[] sentenceEN = { "ошибка" };
        RuVerbs03ToBe ruVerbs = new RuVerbs03ToBe();
        int where = genWhere(form, time);
        switch (where) {
            case 0:// future ?
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0) + " " + suffixRus + "?";
                sentenceEN = new String[]
                        {"Will " + pronounsEN[0][who0] + " be " + suffixEng + "?"};
                break;
            case 1:// future +
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0) + " " + suffixRus;
                sentenceEN = new String[]
                        {pronounsEN[1][who0] + " will be " + suffixEng};
                break;
            case 2:// future -
                sentenceRU = pronounsRU[who0] + " не "
                        + ruVerbs.make(time, who0) + " " + suffixRus;
                sentenceEN = new String[]{
                        pronounsEN[1][who0] + " will not be " + suffixEng,
                        pronounsEN[1][who0] + " won't be " + suffixEng};
                break;
            case 3:// present ?
                sentenceRU = pronounsRU[who0] + " " + suffixRus + "?";
                String toBe;
                if (who1 == 0) {
                    toBe = "Am";
                } else if ((who1 > 1) && (who1 < 5)) {
                    toBe = "Is";
                } else {
                    toBe = "Are";
                }
                sentenceEN = new String[]{
                        toBe + " " + pronounsEN[0][who0] + " " + suffixEng + "?"};
                break;
            case 4:// present +
                sentenceRU = pronounsRU[who0] + " " + suffixRus;
                String sentenceEN_1 = pronounsEN[1][who0] + " ";
                if (who1 == 0) {
                    sentenceEN_1 += "am";
                } else if ((who1 > 1) && (who1 < 5)) {//he she it
                    sentenceEN_1 += "is";
                } else {
                    sentenceEN_1 += "are";
                }
                sentenceEN = new String[] { sentenceEN_1 + " " + suffixEng };
                break;
            case 5:// present -
                sentenceRU = pronounsRU[who0] + " не " + suffixRus;
                String sentenceEN_2 = pronounsEN[1][who0] + " ";
                if (who1 == 0) {
                    sentenceEN_2 += "am not";
                } else if ((who1 > 1) && (who1 < 5)) {//he she it
                    sentenceEN_2 += "is not";
                } else {
                    sentenceEN_2 += "are not";
                }
                String sentenceEN_3 = pronounsEN[1][who0];
                if (who1 == 0) {
                    sentenceEN_3 += "'m not";
                } else if ((who1 > 1) && (who1 < 5)) {//he she it
                    sentenceEN_3 += "'s not";
                } else {
                    sentenceEN_3 += "'re not";
                }
                String sentenceEN_4 = pronounsEN[1][who0] + " ";
                if (who1 == 0) {
                    sentenceEN_4 += "am not";
                } else if ((who1 > 1) && (who1 < 5)) {//he she it
                    sentenceEN_4 += "isn't";
                } else {
                    sentenceEN_4 += "aren't";
                }
                sentenceEN = new String[] {
                        sentenceEN_2 + " " + suffixEng,
                        sentenceEN_3 + " " + suffixEng,
                        sentenceEN_4 + " " + suffixEng };
                break;
            case 6:// past ?
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0) + " " + suffixRus + "?";
                String toBe2;
                if (who1 < 5) {
                    toBe2 = "Was";
                } else {
                    toBe2 = "Were";
                }
                sentenceEN = new String[]{
                        toBe2 + " " + pronounsEN[0][who0] + " " + suffixEng + "?"};
                break;
            case 7:// past +
                sentenceRU = pronounsRU[who0] + " "
                        + ruVerbs.make(time, who0) + " " + suffixRus;
                String toBe3;
                if (who1 < 5) {
                    toBe3 = "was";
                } else {
                    toBe3 = "were";
                }
                sentenceEN = new String[]{pronounsEN[1][who0] + " " + toBe3 + " " + suffixEng};
                break;
            case 8:// past - todo
                sentenceRU = pronounsRU[who0] + " не "
                        + ruVerbs.make(time, who0) + " " + suffixRus;
                String toBe4;
                if (who1 < 5) {
                    toBe4 = "was";
                } else {
                    toBe4 = "were";
                }
                sentenceEN = new String[]{
                        pronounsEN[1][who0] + " " + toBe4 + " not " + suffixEng,
                        pronounsEN[1][who0] + " " + toBe4 + "n't " + suffixEng};
                break;
        }
        return new MultiSentenceData(sentenceRU, sentenceEN);
    }

    private MultiSentenceData addSuffixesAndQuestionMark(String sentenceRU, String[] sentenceEN, int form, String suffixRus, String suffixEng) {
        if (suffixRus != null && !suffixRus.isEmpty()) {
            sentenceRU += " " + suffixRus;
        }
        if (suffixEng != null && !suffixEng.isEmpty()) {
            for (int i=0; i<sentenceEN.length; i++) {
                sentenceEN[i] += " " + suffixEng;
            }
        }
        if (form == FORM_QUESTION) {
            sentenceRU += "?";
            for (int i=0; i<sentenceEN.length; i++) {
                sentenceEN[i] += "?";
            }
        }
        return new MultiSentenceData(sentenceRU, sentenceEN);
    }

    protected String getToBeWithSpaces(int who) {
        String result = null;
        switch (who) {
            case PRONOUN_I:
                result = "am";
                break;
            case PRONOUN_YOU:
            case PRONOUN_THEY:
            case PRONOUN_WE:
                result = "are";
                break;
            case PRONOUN_HE:
            case PRONOUN_SHE:
            case PRONOUN_IT:
                result = "is";
                break;
        }
        return " " + result + " ";
    }

}
