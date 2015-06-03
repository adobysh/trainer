package com.holypasta.trainer.data;

import com.holypasta.trainer.util.Wronger;

import java.util.Random;

/**
 * Created by q1bot on 11.01.15.
 */
public class MultiSentenceDataV2 {

    private String ruSentence;
    private String enSentence;
    private boolean isQuestion;
    private Wronger wronger;

    public MultiSentenceDataV2(String ruSentence, String enSentence, Wronger wronger) {
        this.ruSentence = ruSentence;
        this.enSentence = enSentence;
        this.wronger = wronger;
        this.isQuestion = checkIsQuestion(enSentence);
    }
    
    private boolean checkIsQuestion(String enSentence) {
        String[] exceptions = new String[] { "don't go" };
        for (String word : exceptions) {
            if (enSentence.equals(word)) return false;
        }
        String[] questionWords = new String[] {"will","do","does","did","what","who","where",
                "when","why","how"};
        for (String questionWord : questionWords) {
            if (enSentence.startsWith(questionWord)) return true;
        }
        return false;
    }


    public String getRuSentence() {
        return getSentence(ruSentence);
    }

    public String getEnSentence() {
        return getSentence(enSentence);
    }

    public String getWrongSentence() {
//        lessonId
        if (new Random().nextBoolean()) {
            return getSentence(wrongSentence(enSentence) + " LIE!");
        } else {
            return getSentence(wrongSentence(enSentence) + " LIE!"); // todo | change this
        }
    }

    private String wrongSentence(String rightSentence) {
        return wronger.makeWrongSentence(rightSentence);
    }

    private String wordMixing(String sentence) {
        String[] words = sentence.split(" ");
        Random random = new Random();
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
