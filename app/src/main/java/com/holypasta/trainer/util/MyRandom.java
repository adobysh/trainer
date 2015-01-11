package com.holypasta.trainer.util;

import java.util.ArrayList;
import java.util.Collections;

public class MyRandom {
    
    ArrayList<Integer> variants;
    
    public MyRandom(int maxSize) {
        variants = new ArrayList<Integer>();
        for (int i = 0; i < maxSize; i++) {
            variants.add(i);
        }
        Collections.shuffle(variants);
    }
}
