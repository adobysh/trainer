package com.holypasta.trainer.data;

import com.holypasta.trainer.levels.Wronger;

import java.util.Random;

public class MultiSentenceDataV2 extends AbstractMultiSentence {

    private String ruSentence;
    private String enSentence;
    private boolean isQuestion;
    private Wronger wronger;
    private Random random;

    public MultiSentenceDataV2(String ruSentence, String enSentence, Wronger wronger) {
        this.ruSentence = ruSentence;
        this.enSentence = enSentence;
        this.wronger = wronger;
        random = new Random();
        this.isQuestion = checkIsQuestion(enSentence);
    }
    
    public static boolean checkIsQuestion(String enSentence) {
        enSentence = enSentence.toLowerCase();
        String[] questionWords = new String[] {"will","do","does","did","what","who","where",
                "when","why","how"};
        for (String questionWord : questionWords) {
            if (enSentence.startsWith(questionWord + " ")) return true;
        }
        return false;
    }


    public String getRuSentence() {
        return getSentence(ruSentence);
    }

    public String getEnSentence() {
        if (random.nextBoolean()) { // 50% wrong
            return getCorrectEnSentence();
        } else {
            return getWrongSentence();
        }
    }

    public String getCorrectEnSentence() {
        return getSentence(enSentence);
    }

    public String getWrongSentence() {
        if (onlyOneWord() || random.nextBoolean()) {
            return getSentence(wrongSentence(enSentence)); //  + " LIE! wrong"
        } else {
            return getSentence(wordMixing(enSentence)); //  + " LIE! mix"
        }
    }

    private boolean onlyOneWord() {
        boolean onlyOneWord = !enSentence.contains(" ");
        return onlyOneWord;
    }

    private String wrongSentence(String rightSentence) {
        return wronger.makeWrongSentence(rightSentence);
    }

    private String wordMixing(String sentence) {
        String[] words = sentence.split(" ");
        int wordOneIndex = random.nextInt(words.length);
        int wordTwoIndex = random.nextInt(words.length);
        while (wordOneIndex == wordTwoIndex) {
            wordTwoIndex++;
            wordTwoIndex %= words.length;
        }
        String wordOne = words[wordOneIndex];
        words[wordOneIndex] = words[wordTwoIndex];
        words[wordTwoIndex] = wordOne;
        String result = words[0];
        for (int i = 1; i < words.length; i++) {
            result += " " + words[i];
        }
        return result;
    }

    private String getSentence(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1) + (isQuestion ? "?":"");
    }

    public boolean checkResult(String answer) {
        String convertedSentence = convertToFullSentence(getSentence(enSentence));
        String convertedAnswer = convertToFullSentence(answer);
        if (convertedSentence.equalsIgnoreCase(convertedAnswer)) {
            return true;
        }
        return false;
    }

    private String convertToFullSentence(String sentence) {
        String resultSentence;
        if (sentence.contains("'t")) {
            resultSentence = sentence.replace("won't", "will not");
            resultSentence = resultSentence.replace("Won't", "Will not");
            resultSentence = resultSentence.replace("don't", "do not");
            resultSentence = resultSentence.replace("Don't", "Do not");
            resultSentence = resultSentence.replace("doesn't", "does not");
            resultSentence = resultSentence.replace("Doesn't", "Does not");
            resultSentence = resultSentence.replace("didn't", "did not");
            resultSentence = resultSentence.replace("Didn't", "Did not");
        } else {
            resultSentence = sentence;
        }
        // etc.
        return resultSentence;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MultiSentenceDataV2)) {
            return false;
        } else if (ruSentence.equals(((MultiSentenceDataV2) o).ruSentence)) {
            return true;
        }
        return false;
    }
}
