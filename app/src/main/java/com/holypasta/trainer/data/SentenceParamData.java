package com.holypasta.trainer.data;

import java.util.Random;

/**
 * Created by q1bot on 04.03.2015.
 */
public class SentenceParamData {

    private int value = -1;
    private int size;
    private boolean excludedOn;
    private int excluded = -1;

    public SentenceParamData(int size) {
        this(-1, size, false);
    }

    public SentenceParamData(int value, int size) {
        this(-1, value, size, false);
    }

    public SentenceParamData(int excluded, int size, boolean excludedOn) {
        this(excluded, -1, size, excludedOn);
        nextRandom();
    }

    public SentenceParamData(int excluded, int value, int size, boolean excludedOn) {
        if (size < 2) {
            throw new IllegalArgumentException("size < 2. size = " + size);
        }
        if (excludedOn && size < 3) {
            throw new IllegalArgumentException("excluded is ON and size < 3. size = " + size);
        }
        this.excluded = excluded;
        this.value = value;
        this.size = size;
        this.excludedOn = excludedOn;
    }

    public int value() {
        return value;
    }

    public int nextRandom() {
        int value = new Random().nextInt(size);
        while (this.value == value || (excludedOn && excluded == value)) {
            value++;
            value %= size;
        }
        this.value = value;
        return this.value;
    }
}
