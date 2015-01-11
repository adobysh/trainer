package com.holypasta.trainer.levels;

import com.holypasta.trainer.util.RuVerbs01;

public class Level01 {

    public static String[] makeSentance() {
        String[][] verbs = new String[][]{
                // infinitive
                {"love", "live", "work", "open", "close", "start", "finish",
                        "see", "come", "go", "know", "think"},
                // present +
                {"loves", "lives", "works", "opens", "closes", "starts",
                        "finishes", "sees", "comes", "goes", "knows", "thinks"},
                // past +
                {"loved", "lived", "worked", "opened", "closed", "started",
                        "finished", "saw", "came", "went", "knew", "thought"}};
        String[] pronounsRU = new String[]{"Я", "Ты", "Он", "Она", "Это",
                "Они", "Мы"};
        String[][] pronounsEN = new String[][]{
                {"I", "you", "he", "she", "it", "they", "we"},
                {"I", "You", "He", "She", "It", "They", "We"}};
        String sentenceRU;
        String[] sentenceEN;
        int where = ((int) (Math.random() * 9)) + 1;
        int time = ((where + 2) / 3);
        int why = (int) (Math.random() * 7);
        int verb = (int) (Math.random() * 12);
        switch (where) {
            case 1:// future ?
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb) + "?";
                sentenceEN = new String[]
                        {"Will " + pronounsEN[0][why] + " " + verbs[0][verb] + "?"};
                break;
            case 2:// future +
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]
                        {pronounsEN[1][why] + " will " + verbs[0][verb]};
                break;
            case 3:// future -
                sentenceRU = pronounsRU[why] + " не "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][why] + " will not " + verbs[0][verb],
                        pronounsEN[1][why] + " won't " + verbs[0][verb]};
                break;
            case 4:// present ?
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb) + "?";
                sentenceEN = new String[]{
                        (((why > 1) && (why < 5)) ? "Does " : "Do ")
                                + pronounsEN[0][why] + " " + verbs[0][verb] + "?"};
                break;
            case 5:// present +
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
            case 6:// present -
                sentenceRU = pronounsRU[why] + " не "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][why] + (((why > 1) && (why < 5)) ? " does" : " do") + " not "
                                + verbs[0][verb],
                        pronounsEN[1][why] + (((why > 1) && (why < 5)) ? " does" : " do") + "n't "
                                + verbs[0][verb]};
                break;
            case 7:// past ?
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb) + "?";
                sentenceEN = new String[]{
                        "Did " + pronounsEN[0][why] + " " + verbs[0][verb] + "?"};
                break;
            case 8:// past +
                sentenceRU = pronounsRU[why] + " "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{pronounsEN[1][why] + " " + verbs[2][verb]};
                break;
            default:// past -
                sentenceRU = pronounsRU[why] + " не "
                        + RuVerbs01.make(time, why, verb);
                sentenceEN = new String[]{
                        pronounsEN[1][why] + " did not " + verbs[0][verb],
                        pronounsEN[1][why] + " didn't " + verbs[0][verb]};
                break;
        }

        String[] resultString = new String[sentenceEN.length + 1];
        resultString[0] = sentenceRU;
        for (int i = 1; i < sentenceEN.length + 1; i++) {
            resultString[i] = sentenceEN[i - 1];
        }
        return resultString;
    }
}
