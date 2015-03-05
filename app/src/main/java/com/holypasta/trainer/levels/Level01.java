package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;
import com.holypasta.trainer.util.RuVerbs01;

import java.util.Random;

public class Level01 extends AbstractLevel {

    private final String[][] verbs = new String[][]{
            // infinitive
            {"love", "live", "work", "open", "close", "start", "finish",
                    "see", "come", "go", "know", "think"},
            // present +
            {"loves", "lives", "works", "opens", "closes", "starts",
                    "finishes", "sees", "comes", "goes", "knows", "thinks"},
            // past +
            {"loved", "lived", "worked", "opened", "closed", "started",
                    "finished", "saw", "came", "went", "knew", "thought"}};
    private final String[] pronounsRU = new String[]{"Я", "Ты", "Он", "Она", "Это", "Они", "Мы"};
    private final  String[][] pronounsEN = new String[][]{
            {"I", "you", "he", "she", "it", "they", "we"},
            {"I", "You", "He", "She", "It", "They", "We"}};

    public MultiSentenceData makeSentence(int mode) {
        Random random = new Random();
        SentenceParamData form = new SentenceParamData(3);
        SentenceParamData time = new SentenceParamData(3);
        SentenceParamData why = new SentenceParamData(pronounsEN[0].length);
        SentenceParamData verb = new SentenceParamData(verbs[0].length);
        MultiSentenceData sentence = makeSentence(form.value(), time.value(), why.value(), verb.value());
        if (mode == MODE_HARD) {
            return sentence;
        } else { // MODE_EASY
            if (random.nextBoolean()) { // 50% true
                return sentence;
            }
            System.out.println("!!! false");
            if (random.nextInt(10) != 0) { // 90% change time
                time.nextRandom();
            } else { // 10%
                switch (random.nextInt(3)) {
                    case 0:
                        form.nextRandom();
                        break;
                    case 1:
                        why.nextRandom();
                        break;
                    case 2:
                        verb.nextRandom();
                        break;
                }
            }
            return genWrongSentence(sentence, makeSentence(form.value(), time.value(), why.value(), verb.value()));
        }
    }

    public MultiSentenceData makeSentence(int form, int time, int why, int verb) {
        System.out.println("!!! make " + form + " " + time + " " + why + " " + verb);
        String sentenceRU = "ошибка";
        String[] sentenceEN = { "ошибка" };
        int where = genWhere(form, time);
        System.out.println("!!! where " + where);
        switch (where) {
            case 0:// future ?
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb) + "?";
                sentenceEN = new String[]
                        {"Will " + pronounsEN[0][why] + " " + verbs[0][verb] + "?"};
                break;
            case 1:// future +
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]
                        {pronounsEN[1][why] + " will " + verbs[0][verb]};
                break;
            case 2:// future -
                sentenceRU = pronounsRU[why] + " не "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][why] + " will not " + verbs[0][verb],
                        pronounsEN[1][why] + " won't " + verbs[0][verb]};
                break;
            case 3:// present ?
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb) + "?";
                sentenceEN = new String[]{
                        (((why > 1) && (why < 5)) ? "Does " : "Do ")
                                + pronounsEN[0][why] + " " + verbs[0][verb] + "?"};
                break;
            case 4:// present +
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb);
                String sentenceEN_1;
                sentenceEN_1 = pronounsEN[1][why] + " ";
                if ((why > 1) && (why < 5)) {//he she it
                    sentenceEN_1 += verbs[1][verb];
                } else {
                    sentenceEN_1 += verbs[0][verb];
                }
                sentenceEN = new String[]{sentenceEN_1};
                break;
            case 5:// present -
                sentenceRU = pronounsRU[why] + " не "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][why] + (((why > 1) && (why < 5)) ? " does" : " do") + " not "
                                + verbs[0][verb],
                        pronounsEN[1][why] + (((why > 1) && (why < 5)) ? " does" : " do") + "n't "
                                + verbs[0][verb]};
                break;
            case 6:// past ?
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb) + "?";
                sentenceEN = new String[]{
                        "Did " + pronounsEN[0][why] + " " + verbs[0][verb] + "?"};
                break;
            case 7:// past +
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{pronounsEN[1][why] + " " + verbs[2][verb]};
                break;
            case 8:// past -
                sentenceRU = pronounsRU[why] + " не "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][why] + " did not " + verbs[0][verb],
                        pronounsEN[1][why] + " didn't " + verbs[0][verb]};
                break;
        }
        return new MultiSentenceData(sentenceRU, sentenceEN);
    }
}
