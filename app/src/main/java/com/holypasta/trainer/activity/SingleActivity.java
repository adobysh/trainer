package com.holypasta.trainer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.data.MultiSentenceDataV2;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by q1bot on 03.05.2015.
 */
public class SingleActivity extends ActionBarActivity implements Constants, TextToSpeech.OnInitListener {

    private final int VR_REQUEST = 999;
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech repeatTTS;
    private boolean ttsIsOn;
    private boolean recognitionIsOn;

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
        recognitionIsOn = checkSpeechRecognition();
        if (recognitionIsOn) {

        } else {
            if (firstCheckSpeechRecognition()) {
//                        AlertDialog aboutDialog = new AlertDialog.Builder(
//                                activity)
//                                .setMessage("Распознавание речи не поддерживается!")
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                }).create();
//                        aboutDialog.show();
            }
        }
        checkSpeechSynthesis();
    }


    private boolean checkSpeechRecognition() { // проверяем, поддерживается ли распознование речи
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return intActivities.size() != 0;
    }

    private void checkSpeechSynthesis() {
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private boolean firstCheckSpeechRecognition() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean first = sharedPreferences.getBoolean(PREF_SPEECH_RECOGNITION_FIRST_CHECK, true);
        if (first) {
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean(PREF_SPEECH_RECOGNITION_FIRST_CHECK, false);
            ed.apply();
        }
        return first;
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

    private void listenToSpeech() {
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите!");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        startActivityForResult(listenIntent, VR_REQUEST);
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
		if (requestCode == VR_REQUEST && resultCode == RESULT_OK) {
            ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultString = suggestedWords.get(0).intern();
            if (MultiSentenceDataV2.checkIsQuestion(resultString)) {
                resultString+="?";
            }
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
		repeatTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
}
