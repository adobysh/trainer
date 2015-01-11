package com.holypasta.trainer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.MakeScore;
import com.holypasta.trainer.util.SentenceMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LevelActivity extends ActionBarActivity implements Constants, OnClickListener,
        TextView.OnEditorActionListener, TextToSpeech.OnInitListener {

	private int lessonId;
    private boolean voiceIsOn = false;
    private boolean isHelped = false;
    private boolean failNow = false;
    private boolean isChecked = false;
    private int tv1darkColor;
    private SharedPreferences sPref;
    // speaking
    private LinearLayout ll_next;
    private String ruText;
    private String[] enText = new String[] {};
    private TextView tv1ruText;
    private TextView tvScore;
    private EditText et1enText;
    private TextView button1Help;
    private TextView button2OK;
    private TextView button3Say;
    private TextView button4Next;
    private int myScore = 0;
    // переменная для проверки возможности
    // распознавания голоса в телефоне
    private final int VR_REQUEST = 999;
    // переменные для работы TTS:
    // переменная для проверки данных для TTS
    private int MY_DATA_CHECK_CODE = 0;
    // Text To Speech интерфейс
    protected TextToSpeech repeatTTS;
    protected boolean ttsIsOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		lessonId = getIntent().getExtras().getInt(EXTRA_LESSON_ID);
        sPref = getSharedPreferences(Constants.SCORES, Context.MODE_PRIVATE);
        myScore = sPref.getInt(Constants.SCORE_0_15 + lessonId, 0);
        voiceIsOn = (sPref.getInt(Constants.VOICE, 0) == 1);
        prepareToDialog();
        tv1darkColor = tv1ruText.getCurrentTextColor();
        tvScore.setText(MakeScore.make(myScore));
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.contents)[lessonId]);
        et1enText.setOnEditorActionListener(this);
        buttonNext();
	}

    protected void prepareToDialog() {
        ll_next = (LinearLayout)findViewById(R.id.ll_next);
        ll_next.setOnClickListener(this);
        tv1ruText = (TextView) findViewById(R.id.textView1);
        tvScore = (TextView) findViewById(R.id.tvScore);
        et1enText = (EditText) findViewById(R.id.editText1);
        button1Help = (TextView) findViewById(R.id.button1Help);
        button1Help.setOnClickListener(this);
        button2OK = (TextView) findViewById(R.id.button2OK);
        button2OK.setOnClickListener(this);
        button3Say = (TextView) findViewById(R.id.button3Say);
        button4Next = (TextView) findViewById(R.id.button4Next);
//		button4Next.setOnClickListener(this);
        if (voiceIsOn) {
            // проверяем, поддерживается ли распознование речи
            PackageManager packManager = getPackageManager();
            List<ResolveInfo> intActivities = packManager.queryIntentActivities(
                    new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            if (intActivities.size() != 0) {
                // распознавание поддерживается, будем отслеживать событие щелчка по
                // кнопке
                button3Say.setOnClickListener(this);
            } else {
                // распознавание не работает. Заблокируем
                // кнопку и выведем соответствующее
                // предупреждение.
                button3Say.setEnabled(false);
                // Toast.makeText(this, "Oops - Speech recognition not supported!",
                // Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Упс - Распознавание речи не поддерживается!",
                        Toast.LENGTH_LONG).show();
            }
            // подготовка движка TTS для проговаривания слов
            Intent checkTTSIntent = new Intent();
            // проверка наличия TTS
            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            // запуск checkTTSIntent интента
            startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        } else {
            button3Say.setVisibility(View.GONE);
        }
    }

    protected void installTTS() {
        // интент, перебрасывающий пользователя на страницу TSS в Google
        // Play
        Intent installTTSIntent = new Intent();
        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installTTSIntent);
    }

    @Override
    public void onInit(int initStatus) {
        if (voiceIsOn) {
            if (initStatus == TextToSpeech.SUCCESS)
                repeatTTS.setLanguage(Locale.US); // Язык
        }
    }

    protected void listenToSpeech() {
        // запускаем интент, распознающий речь и передаем ему требуемые данные
        Intent listenIntent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // указываем пакет
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getClass().getPackage().getName());
        // В процессе распознования выводим сообщение
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите!");
        // set language
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        // устанавливаем модель речи
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // указываем число результатов, которые могут быть получены
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        // начинаем прослушивание
        startActivityForResult(listenIntent, VR_REQUEST);
    }

	public void generateText() {
		String[] textArray = SentenceMaker.makeSentance(lessonId, myScore);
		ruText = textArray[0];
        enText = new String[textArray.length-1];
        for (int i = 1; i < textArray.length; i++) {
            enText[i-1] = textArray[i];
        }
		tv1ruText.setText(ruText);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1Help:
            isHelped = true;
            et1enText.setTextColor(tv1darkColor);
			et1enText.setText(enText[0]);
			if (ttsIsOn && voiceIsOn)
				speackNow(enText[0]);
            for (int i = 0; i < enText.length-1;i++) { // перемешка результатов
                String tmp = enText[i];
                enText[i]=enText[i+1];
                enText[i+1]=tmp;
            }
            break;
		case R.id.button2OK:
			buttonOK();
			break;
		case R.id.button3Say:
			listenToSpeech();
			break;
		case R.id.ll_next:
			buttonNext();
			break;
		}
	}

    public void buttonOK(){
        if (!"".equalsIgnoreCase(et1enText.getText().toString())) {
            boolean isTrue = false;
            for (int i = 0; i < enText.length; i++) {
                if (enText[i].equalsIgnoreCase(et1enText.getText().toString())
                        || (enText[i]+".").equalsIgnoreCase(et1enText.getText().toString())) {
                    isTrue = true;
                    continue;
                }
            }
            if (isTrue){
                et1enText.setTextColor(getResources().getColor(R.color.myGreen));
                if (!isHelped) {// если помощи не было
                    if (myScore < Constants.MAX_SCORE) {
                        myScore++;
                        tvScore.setText(MakeScore.make(myScore));
//                        sPref = getSharedPreferences(MyConst.SCORES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putInt(Constants.SCORE_0_15 + lessonId, myScore);
                        ed.apply();
                    }
                }
                buttonsEnabled(false);
                isChecked = true;
            } else {
                et1enText.setTextColor(getResources().getColor(R.color.myRed));
                if (!failNow) { // если еще не фэйлил
                    failNow = true;
                }
            }
        }
    }

    public void buttonNext(){
        generateText();
        et1enText.setText("");
        et1enText.setTextColor(tv1darkColor);
        buttonsEnabled(true);
        isHelped = false;
        failNow = false;
        isChecked = false;
    }

    public void buttonsEnabled(boolean isEnabled){
        if (isEnabled){
            button1Help.setEnabled(true);
            button3Say.setEnabled(true);
            button2OK.setEnabled(true);
        } else {
            button3Say.setEnabled(false);
            button2OK.setEnabled(false);
        }
    }

	// Получаем результат распознавания
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// проверяем результат распознавания речи
		if (requestCode == VR_REQUEST && resultCode == RESULT_OK) {
			// Добавляем распознанные слова в список результатов
			ArrayList<String> suggestedWords = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultString = suggestedWords.get(0).intern();
            if (lessonId < 2) {
                    String[] tmp = resultString.split(" ");
			        if (tmp[0].equalsIgnoreCase("will")
					    || tmp[0].equalsIgnoreCase("do")
					    || tmp[0].equalsIgnoreCase("does")
					    || tmp[0].equalsIgnoreCase("did")
                        || tmp[0].equalsIgnoreCase("what")
                        || tmp[0].equalsIgnoreCase("who")
                        || tmp[0].equalsIgnoreCase("where")
                        || tmp[0].equalsIgnoreCase("when")
                        || tmp[0].equalsIgnoreCase("why")
                        || tmp[0].equalsIgnoreCase("how")) {
                        resultString+="?";
			        }
			}
            et1enText.setText(resultString);
		}
		// tss код здесь
		// returned from TTS data check
		if (requestCode == MY_DATA_CHECK_CODE) {
			// все необходимые приложения установлены, создаем TTS
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				repeatTTS = new TextToSpeech(this, this);
				ttsIsOn = true;
				// движок не установлен, предположим пользователю установить его
			} else {
				installTTS();
			}
		}
	}

	public void speackNow(String text) {
		repeatTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_theory:
                Intent intent = new Intent();
                intent.putExtra(EXTRA_LESSON_ID, lessonId);
                intent.setClass(this, TheoryActivity.class);
                startActivity(intent);
                break;
            case R.id.action_video:
//              startActivity(new  createPlayVideoIntent(this, urlVideo));
                startActivity(new Intent(Intent.ACTION_VIEW, getVideoUrl()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri getVideoUrl() {
        return Uri.parse(VIDEO_BASE_URL + VIDEO_ID[lessonId]);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            // обрабатываем нажатие кнопки DONE
            if (!isChecked)
                buttonOK();
            else
                buttonNext();
            return true;
        }
        return false;
    }
}
