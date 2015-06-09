package com.holypasta.trainer.levels;

import android.content.Context;

import com.holypasta.trainer.data.MultiSentenceDataV2;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.RawReader;
import com.holypasta.trainer.util.Wronger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by q1bot on 11.01.15.
 */
public class LevelFromListV2 extends AbstractLevelV2 {

    private final int RANDOM_LIMIT = 10;
    private List<MultiSentenceDataV2> variants;
    private List<Integer> lastTenIds;
    private Wronger wronger;

    public LevelFromListV2(Context context, int levelId) {
        lastTenIds = new ArrayList<>();
        wronger = new Wronger(context, levelId);
        variants = getAllSentences(context, levelId, wronger);
        if (variants.size() <= RANDOM_LIMIT) {
            throw new IllegalArgumentException("variants size <= " + RANDOM_LIMIT + ". size = " + variants.size());
        }
    }

    @Override
    public MultiSentenceDataV2 makeSentence() {
        int randomVariant = nextRandomVariant();
        while (lastTenIds.contains(randomVariant)) {
            randomVariant++;
            randomVariant %= variants.size();
        }
        lastTenIds.add(randomVariant);
        if (lastTenIds.size() > RANDOM_LIMIT) {
            lastTenIds.remove(0);
        }
        return variants.get(randomVariant);
    }

    private int nextRandomVariant() {
        return new Random().nextInt(variants.size());
    }

    private List<MultiSentenceDataV2> getAllSentences(Context context, int levelId, Wronger wronger) {
        int[] ids = { -1, -1, -1, -1, -1, R.raw.sentenses6, R.raw.sentenses7, R.raw.sentenses8 };
        String raw = RawReader.getStringFromRawFile(context, ids[levelId]);
        String[] lines = raw.split("\n\n");
        List<MultiSentenceDataV2> variants = new ArrayList<>();
        for (String line : lines) {
            List<String> variant = new ArrayList<>(Arrays.asList(line.split("\n")));
            removeComments(variant);
            if (variant.isEmpty()) continue;
            String ruSentence = variant.get(0);
            String enSentence = variant.get(1);
            variants.add(new MultiSentenceDataV2(ruSentence, enSentence, wronger));
        }
        return variants;
    }

    private void removeComments(List<String> variant) {
        for (int i = 0; i < variant.size(); i++) {
            if (variant.get(i).startsWith("#")) {
                variant.remove(i);
                i--;
            }
        }
    }
}