package com.holypasta.trainer.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.data.MultiSentence;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.MakeScore;
import com.holypasta.trainer.util.SentenceMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.view.View.GONE;

public class LevelActivity extends ActionBarActivity implements Constants, OnClickListener,
        TextView.OnEditorActionListener, TextToSpeech.OnInitListener, Animation.AnimationListener {

	private int lessonId;
    private boolean voiceIsOn = false;
    private boolean isHelped = false;
    private boolean failNow = false;
    private boolean isChecked = false;
    private int tv1darkColor;
    private SharedPreferences sPref;
    private MultiSentence multiSentence;
    private TextView tvScore;
    private TextView taskField;
    private TextView resultField;
    private TextView message;
    private TextView button1Help;
    private TextView button2OK;
    private TextView button3Say;
    private TextView buttonNext;
    private Animation fielsdAnimation;
    private Animation messageAnimation;
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
    private int mode;
    private Button buttonTrue;
    private Button buttonFalse;
    private Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
		lessonId = extras.getInt(EXTRA_LESSON_ID);
        mode = extras.getInt(EXTRA_MODE);
        setContentView(mode == MODE_HARD ? R.layout.activity_level_hard : R.layout.activity_level_easy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sPref = getSharedPreferences(Constants.SCORES, Context.MODE_PRIVATE);
        myScore = sPref.getInt(Constants.SCORE_0_15 + lessonId, 0);
        voiceIsOn = mode == MODE_HARD;
        prepareToDialog();
        tv1darkColor = taskField.getCurrentTextColor();
        tvScore.setText(MakeScore.make(myScore));
//        getSupportActionBar().setTitle(getResources().getStringArray(R.array.contents)[lessonId]);
        getSupportActionBar().setTitle((lessonId+1) + " урок");
        resultField.setOnEditorActionListener(this);
        buttonNext();
	}

    protected void prepareToDialog() {
        taskField = (TextView) findViewById(R.id.textView1);
        tvScore = (TextView) findViewById(R.id.tvScore);
        resultField = mode == MODE_HARD ? (EditText) findViewById(R.id.editText1) : (TextView) findViewById(R.id.result_easy);
        resultField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(LevelActivity.this, "Edit", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        if (mode == MODE_HARD) {
            button1Help = (TextView) findViewById(R.id.button1Help);
            button2OK = (TextView) findViewById(R.id.button2OK);
            button3Say = (TextView) findViewById(R.id.button3Say);
            buttonNext = (TextView) findViewById(R.id.buttonNext);
            button1Help.setOnClickListener(this);
            button2OK.setOnClickListener(this);
            buttonNext.setOnClickListener(this);
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
                button3Say.setVisibility(GONE);
            }
        } else if (mode == MODE_EASY) {
            buttonTrue = (Button) findViewById(R.id.answer_true);
            buttonFalse = (Button) findViewById(R.id.answer_false);
            message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.GONE);
            message.setBackgroundResource(R.drawable.button_green);
            buttonTrue.setOnClickListener(this);
            buttonFalse.setOnClickListener(this);
        }
    }

    protected void installTTSfromGooglePlay() {
        Intent installTTSIntent = new Intent();
        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installTTSIntent);
    }

    @Override
    public void onInit(int initStatus) {
        if (voiceIsOn && initStatus == TextToSpeech.SUCCESS) {
            repeatTTS.setLanguage(Locale.US); // Язык
        }
    }

    private void listenToSpeech() {
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
        switch (mode) {
            case MODE_HARD:
                multiSentence = SentenceMaker.makeSentance(this, lessonId, myScore);
                taskField.setText(multiSentence.getRuSentance());
                break;
            case MODE_EASY:
                boolean isTrue = random.nextBoolean();
                if (isTrue) {
                    multiSentence = SentenceMaker.makeSentance(this, lessonId, myScore);
                    multiSentence = new MultiSentence(multiSentence.getRuSentance(), multiSentence.getEnSentances(), isTrue);
                    resultField.setText(multiSentence.getEnSentances());
                } else {
                    multiSentence = SentenceMaker.makeSentance(this, lessonId, myScore);
                    MultiSentence en = SentenceMaker.makeSentance(this, lessonId, myScore);
                    while (multiSentence.getRuSentance().equals(en.getRuSentance())) {
                        en = SentenceMaker.makeSentance(this, lessonId, myScore);
                    }
                    resultField.setText(en.getEnSentances());
                }
                taskField.setText(multiSentence.getRuSentance());
                break;
        }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1Help:
            isHelped = true;
            resultField.setTextColor(tv1darkColor);
			resultField.setText(multiSentence.getEnSentances());
			if (ttsIsOn && voiceIsOn) {
                speakNow(resultField.getText().toString());
            }
            break;
        case R.id.button2OK:
        case R.id.answer_false:
        case R.id.answer_true:
			buttonOK(v.getId());
			break;
		case R.id.button3Say:
			listenToSpeech();
			break;
        case R.id.buttonNext:
            buttonNext();
            break;
		}
	}

    public void buttonOK(int buttonId){
        if (resultField.getText().length() != 0) {
            String answer = resultField.getText().toString();
            final boolean checkResult = mode == MODE_HARD ? multiSentence.checkResult(answer)
                    : multiSentence.checkResult(buttonId == R.id.answer_true);
            if (checkResult) {
                resultField.setTextColor(getResources().getColor(R.color.material_green));
                if (!isHelped) {// если помощи не было
                    if (myScore < Constants.MAX_SCORE) {
                        myScore++;
                    }
                }
                isChecked = true;
            } else {
                resultField.setTextColor(getResources().getColor(R.color.material_red));
                if (!failNow) { // если еще не фэйлил
                    failNow = true;
                }
                if (myScore > 0) {
                    myScore--;
                }
            }
            buttonsEnabled(false);
            tvScore.setText(MakeScore.make(myScore));
            SharedPreferences.Editor ed = sPref.edit();
            ed.putInt(Constants.SCORE_0_15 + lessonId, myScore);
            ed.apply();
            if (mode == MODE_EASY) {
                fielsdAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);//todo
                fielsdAnimation.setAnimationListener(this);
                resultField.startAnimation(fielsdAnimation);
                taskField.startAnimation(fielsdAnimation);
                message.setText(checkResult ? "+1" : (buttonId == R.id.answer_true ? "Правильный вариант:\n" + multiSentence.getEnSentances() : "Ошибки небыло"));
                int drawableId = checkResult ? R.drawable.button_green : R.drawable.button_red;
                message.setBackgroundResource(drawableId);
                message.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
                message.startAnimation(animation);
                buttonTrue.setEnabled(false);
                buttonFalse.setEnabled(false);
                final int timeToNext = checkResult || buttonId == R.id.answer_false ? 3000 : 6000;
                new CountDownTimer(timeToNext, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (millisUntilFinished < 2000) {
                            messageAnimation = AnimationUtils.loadAnimation(LevelActivity.this, R.anim.scale_out);
                            messageAnimation.setAnimationListener(LevelActivity.this);
                            message.startAnimation(messageAnimation);
                            buttonNext();
                        }
                    }

                    @Override
                    public void onFinish() { }
                }.start();
            }
        }
    }

    public void buttonNext(){
        generateText();
        if (mode == MODE_HARD) {
            resultField.setText(null);
        } else if (mode == MODE_EASY) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
            resultField.setVisibility(View.VISIBLE);
            taskField.setVisibility(View.VISIBLE);
            resultField.startAnimation(animation);
            taskField.startAnimation(animation);
        }
        resultField.setTextColor(tv1darkColor);
        buttonsEnabled(true);
        isHelped = false;
        failNow = false;
        isChecked = false;
    }

    public void buttonsEnabled(boolean isEnabled){
        if (mode == MODE_HARD) {
            button3Say.setEnabled(isEnabled);
            button2OK.setVisibility(isEnabled ? View.VISIBLE : GONE);
            buttonNext.setVisibility(isEnabled ? GONE : View.VISIBLE);
        } else {
            buttonTrue.setEnabled(isEnabled);
            buttonFalse.setEnabled(isEnabled);
        }
    }

	@Override // Получаем результат распознавания
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
            resultField.setText(resultString);
		}
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (isTTSInstalled(resultCode)) {
				repeatTTS = new TextToSpeech(this, this);
				ttsIsOn = true;
			} else {
				installTTSfromGooglePlay();
			}
		}
	}

    private boolean isTTSInstalled(int resultCode) {
        return resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS;
    }

	public void speakNow(String text) {
		repeatTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.level_menu, menu);
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
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, getYouTubeUrl()));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, getVideoUrl()));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri getYouTubeUrl() {
        return Uri.parse(YOUTUBE_BASE_URL + VIDEO_ID[lessonId]);
    }

    private Uri getVideoUrl() {
        return Uri.parse(VIDEO_BASE_URL + VIDEO_ID[lessonId]);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) { // обрабатываем нажатие кнопки DONE на экранной клавиатуре
            if (!isChecked) {
                buttonOK(R.id.button2OK);
            } else {
                buttonNext();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationRepeat(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == fielsdAnimation) {
            resultField.setVisibility(View.INVISIBLE);
            taskField.setVisibility(View.INVISIBLE);
        } else if (animation == messageAnimation) {
            message.setVisibility(GONE);
        }
    }
}
