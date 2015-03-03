package com.holypasta.trainer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by q1bot on 11.01.15.
 */
public class MultiSentence {

    private String ruSentence;
    private List<String> correctEnSentences;
    private List<String> incorrectEnSentences;

    public MultiSentence(String ruSentence, String... correctEnSentences) {
        this.ruSentence = ruSentence;
        this.correctEnSentences = new ArrayList<String>();
        for (String enSentence : correctEnSentences) {
            this.correctEnSentences.add(enSentence);
        }
    }

    public MultiSentence(String[] result) {
        this.ruSentence = result[0];
        this.correctEnSentences = new ArrayList<String>();
        for (int i = 1; i < result.length; i++) {
            this.correctEnSentences.add(result[i]);
        }
    }

    public MultiSentence(String ruSentence, List<String> correctEnSentences) {
        this.ruSentence = ruSentence;
        this.correctEnSentences = correctEnSentences;
    }

    public String getRuSentence() {
        return ruSentence;
    }

    public String getEnSentences() {
        List<String> enSentences = incorrectEnSentences == null ? correctEnSentences : incorrectEnSentences;
        enSentences.add(enSentences.get(0));
        enSentences.remove(0);
        return enSentences.get(0);
    }

    public String getCorrectEnSentence() {
        correctEnSentences.add(correctEnSentences.get(0));
        correctEnSentences.remove(0);
        return correctEnSentences.get(0);
    }

    public List<String> getAllCorrectEnSentences() {
        return correctEnSentences;
    }

    public void setIncorrectEnSentences(List<String> incorrectEnSentences) {
        this.incorrectEnSentences = incorrectEnSentences;
    }

    public boolean checkResult(String answer) {
        for (String enSentence : correctEnSentences) {
            if (enSentence.equalsIgnoreCase(answer)) {
                return true;
            }
        }
        return false;
    }
}
