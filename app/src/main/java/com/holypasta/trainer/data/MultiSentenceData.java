package com.holypasta.trainer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by q1bot on 11.01.15.
 */
public class MultiSentenceData {

    private String ruSentence;
    private List<String> correctEnSentences;
    private List<String> incorrectEnSentences;

    public MultiSentenceData(String ruSentence, String... correctEnSentences) {
        this.ruSentence = ruSentence;
        this.correctEnSentences = new ArrayList<String>();
        for (String enSentence : correctEnSentences) {
            this.correctEnSentences.add(enSentence);
        }
    }

    public MultiSentenceData(String[] result) {
        this.ruSentence = result[0];
        this.correctEnSentences = new ArrayList<String>();
        for (int i = 1; i < result.length; i++) {
            this.correctEnSentences.add(result[i]);
        }
    }

    public MultiSentenceData(String ruSentence, List<String> correctEnSentences) {
        this.ruSentence = ruSentence;
        this.correctEnSentences = correctEnSentences;
    }

    public MultiSentenceData(List<String> sentences) {
        this.ruSentence = sentences.get(0);
        sentences.remove(0);
        this.correctEnSentences = sentences;
    }

    public String getRuSentence() {
        return ruSentence;
    }

    public String getEnSentence() {
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

    // todo delete this method
    public List<String> getAllIncorrectEnSentences() {
        return incorrectEnSentences;
    }

    public void setIncorrectEnSentences(List<String> incorrectEnSentences) {
        this.incorrectEnSentences = incorrectEnSentences;
    }

    public void clearIncorrectEnSentences() {
        incorrectEnSentences = null;
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
