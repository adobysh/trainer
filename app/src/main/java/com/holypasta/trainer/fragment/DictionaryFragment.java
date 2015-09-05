package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.adapter.WordsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.DictionaryGenerator;

import java.util.List;
import java.util.Locale;

public class DictionaryFragment extends AbstractFragment {

    private SingleActivity activity;
    private int lessonId;
//    private TextToSpeech tts;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_dictionary, container, false);
        final Bundle extras = getArguments();
        lessonId = extras.getInt(EXTRA_LESSON_ID);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        List<String> words = getDictionaryItems(lessonId);
        if (words == null) return rootView;
        final WordsAdapter wordsAdapter = new WordsAdapter(words, activity);
        listView.setAdapter(wordsAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String words = (String) wordsAdapter.getItem(position);
//                activity.speakNow(words.split(" - ")[0]);
//
//                tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);
//            }
//        });
//        tts = new TextToSpeech(activity, this);
//        tts.setLanguage(Locale.US);
        return rootView;
    }

    private List<String> getDictionaryItems(int levelId) {
        DictionaryGenerator generator = new DictionaryGenerator(levelId);
        return generator.getDictionaryItems();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_activity_dictionary) + ". " + (lessonId+1) + " урок ";
    }
}
