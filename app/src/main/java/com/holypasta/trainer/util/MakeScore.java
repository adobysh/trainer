package com.holypasta.trainer.util;

public class MakeScore {

    static public String make(int score) {
        score = score == -1 ? 0 : score;
        return (int)(score * 6.25) + "%";
    }
}
