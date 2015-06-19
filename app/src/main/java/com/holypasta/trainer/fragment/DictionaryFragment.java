package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.adapter.WordsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.DictionaryGenerator;

import java.util.List;

/**
 * Created by q1bot on 16.04.2015.
 */
public class DictionaryFragment extends Fragment implements Constants
//        TextToSpeech.OnInitListener todo speech
    {

//    private int MY_DATA_CHECK_CODE = 0; todo speech
//    private TextToSpeech repeatTTS;
//    private boolean ttsIsOn;
    private SingleActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity.getSupportActionBar().setTitle(getString(R.string.title_activity_dictionary));
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_dictionary, container, false);
        final Bundle extras = getArguments();
        int levelId = extras.getInt(EXTRA_LESSON_ID);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        List<String> words = getDictionaryItems(levelId);
        if (words == null) return rootView;
        final WordsAdapter wordsAdapter = new WordsAdapter(words, activity);
        listView.setAdapter(wordsAdapter);
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { todo speech
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String words = (String) wordsAdapter.getItem(position);
//                speakNow(words.split(" - ")[0]);
//            }
//        });
//        // подготовка движка TTS для проговаривания слов
//        Intent checkTTSIntent = new Intent();
//        // проверка наличия TTS
//        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        // запуск checkTTSIntent интента
//        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        return rootView;
    }

    private List<String> getDictionaryItems(int levelId) {
        DictionaryGenerator generator = new DictionaryGenerator(levelId);
        return generator.getDictionaryItems();
    }

//    public void speakNow(String text) { todo speech
//        if (ttsIsOn) {
//            repeatTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//        }
//    }
//
//    @Override
//    public void onInit(int initStatus) {
//        if (initStatus == TextToSpeech.SUCCESS) {
//            repeatTTS.setLanguage(Locale.US); // Язык
//        }
//    }
//
//    private boolean isTTSInstalled(int resultCode) {
//        return resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MY_DATA_CHECK_CODE) {
//            if (isTTSInstalled(resultCode)) {
//                repeatTTS = new TextToSpeech(activity, this);
//                ttsIsOn = true;
//            } else {
//                installTTSFromGooglePlay();
//            }
//        }
//    }
//
//    private void installTTSFromGooglePlay() {
//        Intent installTTSIntent = new Intent();
//        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//        startActivity(installTTSIntent);
//    }

}
