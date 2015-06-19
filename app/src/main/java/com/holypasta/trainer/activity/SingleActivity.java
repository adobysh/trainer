package com.holypasta.trainer.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.data.MultiSentenceDataV2;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.fragment.MainFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static android.widget.Toast.makeText;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by q1bot on 03.05.2015.
 */
public class SingleActivity extends ActionBarActivity implements Constants,
        TextToSpeech.OnInitListener, RecognitionListener {

    private final int REQ_CODE_SPEECH_INPUT = 999;
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech repeatTTS;
    private boolean ttsIsOn;

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String FORECAST_SEARCH = "forecast";
    private static final String DIGITS_SEARCH = "digits";
    private static final String PHONE_SEARCH = "phones";
    private static final String MENU_SEARCH = "menu";
    private static final String WORDS_SEARCH = "words";

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "oh mighty computer";

    private SpeechRecognizer recognizer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, new MainFragment(), MainFragment.class.getSimpleName())
                .commit();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backCount == 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        });
        checkSpeechSynthesis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }

    private void checkSpeechSynthesis() {
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void installTTSFromGooglePlay() {
        Intent installTTSIntent = new Intent();
        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installTTSIntent);
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            repeatTTS.setLanguage(Locale.US);
        }
    }

    public void listenToSpeech() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(SingleActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    ((TextView) findViewById(R.id.editText1))
                            .setText("Failed to init recognizer " + result);
                } else {
                    switchSearch(WORDS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case android.R.id.home:
            onBackPressed();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("!!! onActivityResult " + (requestCode == REQ_CODE_SPEECH_INPUT) + " " + (resultCode == RESULT_OK));
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK) {
            ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultString = suggestedWords.get(0).intern();
            if (MultiSentenceDataV2.checkIsQuestion(resultString)) {
                resultString+="?";
            }
            System.out.println("!!! send result");
            sendRecognitionResult(resultString);
		}
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (isTTSInstalled(resultCode)) {
				repeatTTS = new TextToSpeech(this, this);
				ttsIsOn = true;
			} else {
				installTTSFromGooglePlay();
			}
		}
	}

    private void sendRecognitionResult(String result) {
        Intent intent = new Intent(ACTION_SPEECH_RECOGNITION_RESULT);
        intent.putExtra(EXTRA_RECOGNITION_RESULT, result);
        sendBroadcast(intent);
    }

    private boolean isTTSInstalled(int resultCode) {
        return resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS;
    }

	public void speakNow(String text) {
        try {

            repeatTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception e) {}
    }

    @Override // Pocketsphinx | CMU Sphinx
    public void onBeginningOfSpeech() { }

    // Pocketsphinx | CMU Sphinx
    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
//        if (!recognizer.getSearchName().equals(WORDS_SEARCH))
//            switchSearch(WORDS_SEARCH);
    }

    // Pocketsphinx | CMU Sphinx
    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        ((TextView) findViewById(R.id.editText1)).setText(text);
    }

    // Pocketsphinx | CMU Sphinx
    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.editText1)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override // Pocketsphinx | CMU Sphinx
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.editText1)).setText(error.getMessage());
    }

    @Override // Pocketsphinx | CMU Sphinx
    public void onTimeout() {
//        switchSearch(WORDS_SEARCH);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                        // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(assetsDir)

                        // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)

                        // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

//          поиск по ключевой фразе не нужен
//        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

//          поиск одного из нескольких слов не нужен
//        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

//        // Create grammar-based search for digit recognition
        File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);

//        // Create grammar-based search for word recognition
        File wordsGrammar = new File(assetsDir, "words.gram");
        recognizer.addGrammarSearch(WORDS_SEARCH, wordsGrammar);

//      какойто фонетический поиск, хз как он работает
//        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);
//
//        // Phonetic search
        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
//        if (searchName.equals(KWS_SEARCH))
//            recognizer.startListening(searchName);
//        else
            recognizer.startListening(searchName, 10000);


    }
}
