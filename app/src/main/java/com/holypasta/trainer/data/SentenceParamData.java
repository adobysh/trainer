package com.holypasta.trainer.data;

import java.util.Random;

/**
 * Created by q1bot on 04.03.2015.
 */
public class SentenceParamData {

    private int value;
    private int size;

    public SentenceParamData(int size) {
        this.size = size;
        value = new Random().nextInt(size);
    }

    public SentenceParamData(int value, int size) {
        this.size = size;
        this.value = value;
    }

    public int value() {
        return value;
    }

    public int nextRandom() {
        value += new Random().nextInt(size - 2) + 1;
        value %= size;
        return value;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
