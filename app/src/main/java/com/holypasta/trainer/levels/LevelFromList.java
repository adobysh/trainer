package com.holypasta.trainer.levels;

import android.content.Context;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.RawReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by q1bot on 11.01.15.
 */
public class LevelFromList extends AbstractLevel {

    private int levelId;
    private Context context;

    public LevelFromList(Context context, int levelId) {
        this.levelId = levelId;
        this.context = context;
    }

    @Override
    public MultiSentenceData makeSentence(int mode) {
        List<MultiSentenceData> sentenceList = getAllSentences();
        int random = new Random().nextInt(sentenceList.size());
        return sentenceList.get(random);
    }

    private List<MultiSentenceData> getAllSentences() {
        int[] ids = {0, 0, R.raw.sentenses3, R.raw.sentenses4, R.raw.sentenses5};
        String raw = RawReader.getStringFromRawFile(context, ids[levelId]);
        String[] lines = raw.split("\n\n");
        List<MultiSentenceData> sentences = new ArrayList<MultiSentenceData>();
        for (String line : lines) {
            List<String> enSentences = new ArrayList<String>(Arrays.asList(line.split("\n")));
            String ruSentence = enSentences.get(0);
            enSentences.remove(0);
            sentences.add(new MultiSentenceData(ruSentence, enSentences));
        }
        return sentences;
    }
}
