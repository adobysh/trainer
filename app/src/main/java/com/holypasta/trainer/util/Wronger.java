package com.holypasta.trainer.util;

import android.content.Context;

import com.holypasta.trainer.data.SentenceParamData;
import com.holypasta.trainer.english.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kaschey on 02.06.15.
 */
public class Wronger {

    private Context context;
    private int lessonId;
    private List<List<String>> lessonWordsCollection;

    public Wronger(Context context, int lessonId) {
        this.context = context;
        this.lessonId = lessonId;
        lessonWordsCollection = makeWordsCollection();
    }

    public String makeWrongSentence(String rightSentence) {
        Collections.shuffle(lessonWordsCollection);
        int i = 0;
        for (List<String> words : lessonWordsCollection) {
            for (String word : words) {
                if (contains(rightSentence, word)) {
                    SentenceParamData param = new SentenceParamData(words.indexOf(word), words.size());
                    param.nextRandom();
                    return rightSentence.replace(word, words.get(param.value()));
                }
            }
        }
        throw new IllegalArgumentException("not found word in: " + rightSentence);
    }

    public boolean contains(String line, String word){
        Pattern p = Pattern.compile("(^|(.+\\s))" + word + "($|(\\s.+))");
        Matcher m = p.matcher(line);
        return m.matches();
    }

    private List<List<String>> makeWordsCollection() {
        List<List<String>> lessonWordsCollection = new ArrayList<>();
        String raw = RawReader.getStringFromRawFile(context, R.raw.word_collection);
        String[] wordPacks = raw.split("\n\n");
        for (String wordPack : wordPacks) {
            List<String> words = new ArrayList<>(Arrays.asList(wordPack.split("\n")));
            removeComments(words);
            if (words.isEmpty()) continue;
            if (words.get(0).contains(" " + (lessonId+1) + " ")) {
                words.remove(0);
                lessonWordsCollection.add(words);
            }
        }
        return lessonWordsCollection;
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
