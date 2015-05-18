package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.MakeScore;
import com.holypasta.trainer.util.SentenceMaker;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class LevelFragment extends Fragment implements Constants, OnClickListener,
        Animation.AnimationListener, TextView.OnEditorActionListener
//        TextToSpeech.OnInitListener todo speech
    {

	private int lessonId;
    private boolean voiceIsOn = false;
    private boolean isHelped = false;
    private boolean failNow = false;
    private boolean isChecked = false;
    private int tv1darkColor;
    private SharedPreferences sharedPreferences;
    private MultiSentenceData multiSentence;
    private List<MultiSentenceData> pastMultiSentences;
    private TextView tvScore;
    private TextView taskField;
    private TextView resultField;
    private TextView message;
//    private View button1Help; todo speech
    private View button2OK;
//    private View button3Say; todo speech
    private View buttonNext;
    private View buttonNextFullscreen;
    private View buttonNextLevel;
    private Animation fieldsAnimation;
    private Animation messageAnimation;
    private int score = 0;
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
    private ProgressBar progressBar;
    private SingleActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Bundle extras = getArguments();
        lessonId = extras.getInt(EXTRA_LESSON_ID);
        mode = extras.getInt(EXTRA_MODE);
        int layoutId = mode == MODE_HARD ? R.layout.activity_level_hard : R.layout.activity_level_easy;
        View rootView = inflater.inflate(layoutId, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        score = sharedPreferences.getInt(Constants.PREF_SCORE_0_15 + lessonId, 0);
        final boolean isFirstOpen = firstOpen(score);
        if (isFirstOpen) {
            final View welcomeView = rootView.findViewById(R.id.welcome);
            welcomeView.setVisibility(View.VISIBLE);
            Button firstStartButton = (Button)welcomeView.findViewById(R.id.button_start_lesson);
            Button videoButton = (Button)welcomeView.findViewById(R.id.button_video_lesson);
            Button theoryButton = (Button)welcomeView.findViewById(R.id.button_theory);
            firstStartButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    welcomeView.setVisibility(View.GONE);
                }
            });
            videoButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openVideo();
                }
            });
            theoryButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTheory(isFirstOpen);
                }
            });
        }
        voiceIsOn = mode == MODE_HARD;
        prepareToDialog(rootView);
        tv1darkColor = taskField.getCurrentTextColor();
        tvScore.setText(MakeScore.make(score));
        activity.getSupportActionBar().setTitle((lessonId + 1) + " урок");
        resultField.setOnEditorActionListener(this);
        buttonNext();
        return rootView;
	}

    private boolean firstOpen(int score) {
        return score == 0;
    }

    protected void prepareToDialog(View rootView) {
        taskField = (TextView) rootView.findViewById(R.id.textView1);
        tvScore = (TextView) rootView.findViewById(R.id.tvScore);
        resultField = mode == MODE_HARD ? (EditText) rootView.findViewById(R.id.editText1) : (TextView) rootView.findViewById(R.id.result_easy);
        if (mode == MODE_HARD) {
//            button1Help = rootView.findViewById(R.id.button1Help); todo speech
            button2OK = rootView.findViewById(R.id.button2OK);
//            button3Say = rootView.findViewById(R.id.button3Say); todo speech
            buttonNext = rootView.findViewById(R.id.buttonNext);
//            button1Help.setOnClickListener(this); todo speech
            button2OK.setOnClickListener(this);
            buttonNext.setOnClickListener(this);
//            if (voiceIsOn) { todo speech
//                if (checkSpeechRecognition()) {
//                    button3Say.setOnClickListener(this);
//                } else {
//                    button3Say.setVisibility(View.GONE);
//                    button1Help.setVisibility(View.GONE);
//                    if (firstCheckSpeechRecognition()) {
//                        AlertDialog aboutDialog = new AlertDialog.Builder(
//                                activity)
//                                .setMessage("Распознавание речи не поддерживается!")
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                }).create();
//                        aboutDialog.show();
//                    }
//                }
//                // подготовка движка TTS для проговаривания слов
//                Intent checkTTSIntent = new Intent();
//                // проверка наличия TTS
//                checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//                // запуск checkTTSIntent интента
//                startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
//            } else {
//                button3Say.setVisibility(GONE);
//            }
        } else if (mode == MODE_EASY) {
            buttonNextFullscreen = rootView.findViewById(R.id.button_next_fullscreen);
            buttonTrue = (Button) rootView.findViewById(R.id.answer_true);
            buttonFalse = (Button) rootView.findViewById(R.id.answer_false);
            message = (TextView) rootView.findViewById(R.id.message);
            message.setVisibility(View.GONE);
            message.setBackgroundResource(R.drawable.button_green);
            buttonTrue.setOnClickListener(this);
            buttonFalse.setOnClickListener(this);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            progressBar.setMax(MAX_SCORE);
            progressBar.setProgress(score);
            tvScore.setVisibility(View.INVISIBLE);
        }
    }
/* todo speech
    private boolean checkSpeechRecognition() { // проверяем, поддерживается ли распознование речи
        PackageManager packManager = activity.getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return intActivities.size() != 0;
    }

    private boolean firstCheckSpeechRecognition() {
        boolean first = sharedPreferences.getBoolean(PREF_SPEECH_RECOGNITION_FIRST_CHECK, true);
        if (first) {
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean(PREF_SPEECH_RECOGNITION_FIRST_CHECK, false);
            ed.apply();
        }
        return first;
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
*/
	public void generateText() {
        if (pastMultiSentences == null) {
            pastMultiSentences = new ArrayList<>();
        }
        do {
            multiSentence = SentenceMaker.makeSentence(activity, lessonId, mode, score);
        } while (pastMultiSentences.contains(multiSentence));
        pastMultiSentences.add(multiSentence);
        if (pastMultiSentences.size() >= UNIQUE_COUNT) {
            pastMultiSentences.remove(0);
        }
        taskField.setText(multiSentence.getRuSentence());
        if (mode == MODE_EASY) {
            resultField.setText(multiSentence.getEnSentence());
        }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.button1Help: todo speech
//            isHelped = true;
//			if (ttsIsOn && voiceIsOn) {
//                speakNow(multiSentence.getEnSentence());
//            }
//            break;
        case R.id.button2OK:
        case R.id.answer_false:
        case R.id.answer_true:
			buttonOK(v.getId());
			break;
//		case R.id.button3Say: todo speech
//			listenToSpeech();
//			break;
        case R.id.buttonNext:
            buttonNext();
            break;
		}
	}

    public void buttonOK(int buttonId) {
        if (resultField.getText().length() != 0) {
            String answer = resultField.getText().toString();
            boolean checkResult = multiSentence.checkResult(answer);
            System.out.println("!!! answer: " + answer);
            System.out.println("!!! checkResult " + checkResult);
            if (mode == MODE_EASY) {
                boolean answerButton = buttonId == R.id.answer_true;
                System.out.println("!!! answerButton " + answerButton);
                checkResult = answerButton == checkResult;
                System.out.println("!!! checkResult " + checkResult);
            }
            if (checkResult) {
                resultField.setTextColor(getResources().getColor(R.color.material_green));
                if (!isHelped) {// если помощи не было
                    if (score < MAX_SCORE) {
                        score++;
                        if (score == MAX_SCORE) {
                            showNextLevelDialog();
                        }
                    }
                    if (score >= MAX_SCORE) {
                        unlockNextLesson();
                    }
                }
            } else {
                resultField.setTextColor(getResources().getColor(R.color.material_red));
                if (mode == MODE_HARD) {
                    taskField.setText(multiSentence.getRuSentence() +
                            "\n" + multiSentence.getEnSentence());
                }
                if (!failNow) { // если еще не фэйлил
                    failNow = true;
                }
                if (mode == MODE_EASY && score > 1) {
//                    score--; todo
                }
            }
            isChecked = true;
            if (mode == MODE_EASY) {
                progressBar.setProgress(score);
            }
            System.out.println("!!! score = " + score);
            buttonsEnabled(false);
            tvScore.setText(MakeScore.make(score));
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putInt(Constants.PREF_SCORE_0_15 + lessonId, score);
            ed.apply();
            if (mode == MODE_EASY) {
                buttonNextFullscreen.setVisibility(View.VISIBLE);
                if (!checkResult) {
                    buttonNextFullscreen.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modeEasyNext();
                        }
                    });
                }
                fieldsAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out);
                fieldsAnimation.setAnimationListener(this);
                resultField.startAnimation(fieldsAnimation);
                taskField.startAnimation(fieldsAnimation);
                message.setText(checkResult ? "+1" : (buttonId == R.id.answer_true
                        ? "Неправильно:\n" + multiSentence.getEnSentence()
                        + "\n\nПравильно:\n" + multiSentence.getRuSentence() + "\n" + multiSentence.getCorrectEnSentence()
                        : "Ошибки небыло"));
                int drawableId = checkResult ? R.drawable.button_green : R.drawable.button_red;
                message.setBackgroundResource(drawableId);
                message.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.scale_in);
                message.startAnimation(animation);
                buttonTrue.setEnabled(false);
                buttonFalse.setEnabled(false);
                final int timeToNext = checkResult || buttonId == R.id.answer_false ? 3000 : 15000;
                new CountDownTimer(timeToNext, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (millisUntilFinished < 2000) {
                            if (buttonNextFullscreen.getVisibility() == View.VISIBLE) {
                                modeEasyNext();
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                    }
                }.start();
            }
        }
    }

    private void modeEasyNext() {
        buttonNextFullscreen.setVisibility(View.GONE);
        messageAnimation = AnimationUtils.loadAnimation(activity, R.anim.scale_out);
        messageAnimation.setAnimationListener(LevelFragment.this);
        message.startAnimation(messageAnimation);
        buttonNext();
    }

    public void buttonNext(){
        generateText();
        if (mode == MODE_HARD) {
            resultField.setText(null);
        } else if (mode == MODE_EASY) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in);
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
//            button3Say.setEnabled(isEnabled); todo speech
            button2OK.setVisibility(isEnabled ? View.VISIBLE : GONE);
            buttonNext.setVisibility(isEnabled ? GONE : View.VISIBLE);
        } else {
            buttonTrue.setEnabled(isEnabled);
            buttonFalse.setEnabled(isEnabled);
        }
    }

    private void unlockNextLesson() {
        if (lessonId == LAST_LEVEL) {
            return;
        }
        int nextLevelSore = sharedPreferences.getInt(PREF_SCORE_0_15 + (lessonId + 1), -1);
        if (nextLevelSore == -1) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(PREF_SCORE_0_15 + (lessonId + 1), 0);
            edit.commit();
            System.out.println("!!! next level opened");
        }
    }

    private void showNextLevelDialog() {
        if (lessonId == LAST_LEVEL) {
            AlertDialog aboutDialog = new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.title_dialog_complete_all_levels))
                    .setPositiveButton(getString(R.string.button_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        } else if (lessonId == COMPLETE - 1) {
            // Congratulation! Wait more levels and repeating!
            AlertDialog aboutDialog = new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.title_dialog_complete_available_levels))
                    .setPositiveButton(getString(R.string.button_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
            aboutDialog.show();
        } else {
            AlertDialog aboutDialog = new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.title_dialog_next_lesson))
                    .setPositiveButton(getString(R.string.button_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openNextLevel();
                        }
                    })
                    .setNegativeButton(getString(R.string.button_negative), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
            aboutDialog.show();
        }
    }

    private void openNextLevel() {
        removeFragmentFromBackStack();
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId + 1);
        arguments.putInt(EXTRA_MODE, mode);
        Fragment fragment = new LevelFragment();
        fragment.setArguments(arguments);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void removeFragmentFromBackStack() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

//	@Override // Получаем результат распознавания todo speech
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// проверяем результат распознавания речи
//        try {
//            ArrayList<String> suggestedWords = data
//                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            String resultString = suggestedWords.get(0).intern();
//            System.out.println("!!! " + resultString);
//        } catch (Exception e) {
//            System.out.println("!!! on result error");
//            System.out.println("!!! VR_REQUEST = " + VR_REQUEST + " requestCode = " + requestCode);
//            System.out.println("!!! RESULT_OK = " + activity.RESULT_OK + " resultCode = " + resultCode);
//        }
//
//        System.out.println("!!! on result fragment");
//		if (requestCode == VR_REQUEST && resultCode == activity.RESULT_OK) {
//            System.out.println("!!! on result word");
//			// Добавляем распознанные слова в список результатов
//            ArrayList<String> suggestedWords = data
//					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            String resultString = suggestedWords.get(0).intern();
//            String[] tmp = resultString.split(" ");
//            if (tmp[0].equalsIgnoreCase("will")
//                || tmp[0].equalsIgnoreCase("do")
//                || tmp[0].equalsIgnoreCase("does")
//                || tmp[0].equalsIgnoreCase("did")
//                || tmp[0].equalsIgnoreCase("what")
//                || tmp[0].equalsIgnoreCase("who")
//                || tmp[0].equalsIgnoreCase("where")
//                || tmp[0].equalsIgnoreCase("when")
//                || tmp[0].equalsIgnoreCase("why")
//                || tmp[0].equalsIgnoreCase("how")) {
//                resultString+="?";
//            }
//            resultField.setText(resultString);
//		}
//		if (requestCode == MY_DATA_CHECK_CODE) {
//			if (isTTSInstalled(resultCode)) {
//				repeatTTS = new TextToSpeech(activity, this);
//				ttsIsOn = true;
//			} else {
//				installTTSfromGooglePlay();
//			}
//		}
//	}
//
//    private boolean isTTSInstalled(int resultCode) {
//        return resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS;
//    }
//
//	public void speakNow(String text) {
//		repeatTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.level_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_theory:
                boolean isFirstOpen = false;
                openTheory(isFirstOpen);
                break;
            case R.id.action_video:
                openVideo();
                break;
            case R.id.action_dictionary:
                openDictionary();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openTheory(boolean isFirstOpen) {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId);
        arguments.putBoolean(EXTRA_FIRST_OPEN, isFirstOpen);
        Fragment fragment = new TheoryFragment();
        fragment.setArguments(arguments);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openDictionary() {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId);
        Fragment fragment = new DictionaryFragment();
        fragment.setArguments(arguments);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openVideo() {
        Uri videoUri;
        try {
            videoUri = Uri.parse(YOUTUBE_APP_BASE_URL + VIDEO_ID[lessonId]);
            startActivity(new Intent(Intent.ACTION_VIEW, videoUri));
        } catch (ActivityNotFoundException e) {
            videoUri = Uri.parse(YOUTUBE_SITE_BASE_URL + VIDEO_ID[lessonId]);
            startActivity(new Intent(Intent.ACTION_VIEW, videoUri));
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) { // обрабатываем нажатие кнопки DONE на экранной клавиатуре
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
        if (animation == fieldsAnimation) {
            resultField.setVisibility(View.INVISIBLE);
            taskField.setVisibility(View.INVISIBLE);
        } else if (animation == messageAnimation) {
            message.setVisibility(GONE);
        }
    }
}
