package com.holypasta.trainer.data;

public abstract class AbstractMultiSentence {

    public abstract boolean checkResult(String answer);

    public abstract String getRuSentence();

    public abstract String getEnSentence();

    public abstract String getCorrectEnSentence();

}
