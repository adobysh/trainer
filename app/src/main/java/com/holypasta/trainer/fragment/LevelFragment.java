package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.MultiSentenceDataV2;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.MakeScore;
import com.holypasta.trainer.util.SentenceMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;

public class LevelFragment extends Fragment implements Constants, OnClickListener,
        Animation.AnimationListener, TextView.OnEditorActionListener {

    private final int BAD_LESSONS_COUNT = 5;

	private int lessonId;
    private boolean isHelped = false;
    private boolean failNow = false;
    private boolean isChecked = false;
    private int tv1darkColor;
    private SharedPreferences sharedPreferences;
    private MultiSentenceData multiSentence;
    private MultiSentenceDataV2 multiSentenceV2;
    private SentenceMaker sentenceMaker;
    private List<MultiSentenceData> pastMultiSentences;
    private TextView tvScore;
    private TextView taskField;
    private TextView resultField;
    private TextView message;
    private View button1Help;
    private View button2OK;
    private View button3Say;
    private View buttonNext;
    private View buttonNextFullscreen;
    private Animation fieldsAnimation;
    private Animation messageAnimation;
    private int score = 0;
    private int mode;
    private Button buttonTrue;
    private Button buttonFalse;
    private ProgressBar progressBar;
    private SingleActivity activity;
    private View rootView;
    private boolean isFirstOpen;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            resultField.setText(intent.getStringExtra(EXTRA_RECOGNITION_RESULT));
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        setHasOptionsMenu(true);
        Bundle extras = getArguments();
        lessonId = extras.getInt(EXTRA_LESSON_ID);
        mode = extras.getInt(EXTRA_MODE);
        int layoutId = mode == MODE_HARD ? R.layout.fragment_level_hard : R.layout.fragment_level_easy;
        rootView = inflater.inflate(layoutId, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        score = sharedPreferences.getInt(Constants.PREF_SCORE_0_15 + lessonId, 0);
        isFirstOpen = firstOpen(score);
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
                    if (mode == MODE_HARD) {
                        showSoftKeyboard((EditText) resultField);
                    }
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
        prepareToDialog(rootView);
        tv1darkColor = taskField.getCurrentTextColor();
        tvScore.setText(MakeScore.make(score));
        activity.getSupportActionBar().setTitle((lessonId + 1) + " урок");
        resultField.setOnEditorActionListener(this);
        if (lessonId >= BAD_LESSONS_COUNT) {
            sentenceMaker = new SentenceMaker(activity, lessonId);
        }
        buttonNext();
        registerReceiver();
        return rootView;
	}

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SPEECH_RECOGNITION_RESULT);
        try {

            activity.registerReceiver(broadcastReceiver, intentFilter);
        } catch (Exception e) {} // todo

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mode == MODE_HARD) {
            hideSoftKeyboard((EditText) resultField);
        }
        if (rootView != null) { // lolfix
            ViewGroup parentViewGroup = (ViewGroup) rootView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    private boolean firstOpen(int score) {
        return score == 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mode == MODE_HARD && !isFirstOpen) {
            showSoftKeyboard((EditText) resultField);
        }
    }

    protected void prepareToDialog(View rootView) {
        taskField = (TextView) rootView.findViewById(R.id.textView1);
        tvScore = (TextView) rootView.findViewById(R.id.tvScore);
        if (mode == MODE_HARD) {
            resultField = (EditText) rootView.findViewById(R.id.editText1);
            button1Help = rootView.findViewById(R.id.button1Help);
            button2OK = rootView.findViewById(R.id.button2OK);
            button3Say = rootView.findViewById(R.id.button3Say);
            buttonNext = rootView.findViewById(R.id.buttonNext);
            button1Help.setOnClickListener(this);
            button2OK.setOnClickListener(this);
            buttonNext.setOnClickListener(this);
//            if (activity.isRecognitionIsOn()) { // todo
                button3Say.setOnClickListener(this);
//            } else {
//                button3Say.setVisibility(View.GONE);
//                button1Help.setVisibility(View.GONE);
//            }
        } else if (mode == MODE_EASY) {
            resultField = (TextView) rootView.findViewById(R.id.result_easy);
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

    private void showSoftKeyboard(EditText editText) {
        resultField.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1Help:
            isHelped = true;
            try {

                activity.speakNow(multiSentence.getEnSentence());
            } catch (Exception e) {}
            System.out.println("!!! activity == null " + (activity == null));
            break;
        case R.id.button2OK:
        case R.id.answer_false:
        case R.id.answer_true:
			buttonOK(v.getId());
			break;
		case R.id.button3Say:
			activity.listenToSpeech();
			break;
        case R.id.buttonNext:
            buttonNext();
            break;
		}
	}

    public void buttonOK(int buttonId) {
        if (resultField.getText().length() != 0) {
            String answer = resultField.getText().toString();
            boolean checkResult = lessonId < BAD_LESSONS_COUNT ?
                    multiSentence.checkResult(answer) :
                    multiSentenceV2.checkResult(answer);
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
                    if (lessonId < BAD_LESSONS_COUNT) {
                        taskField.setText(multiSentence.getRuSentence() +
                                "\n" + multiSentence.getEnSentence());
                    } else {
                        taskField.setText(multiSentenceV2.getRuSentence() +
                                "\n" + multiSentenceV2.getEnSentence());
                    }
                }
                if (!failNow) { // если еще не фэйлил
                    failNow = true;
                }
                if (mode == MODE_EASY && score > 1) {
                    score--;
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
                if (lessonId < BAD_LESSONS_COUNT) {
                    message.setText(checkResult ? "+1" : (buttonId == R.id.answer_true
                            ? "Неправильно:\n" + multiSentence.getEnSentence()
                            + "\n\nПравильно:\n" + multiSentence.getRuSentence() + "\n" + multiSentence.getCorrectEnSentence()
                            : "Ошибки небыло"));
                } else {
                    message.setText(checkResult ? "+1" : (buttonId == R.id.answer_true
                            ? "Неправильно:\n" + answer
                            + "\n\nПравильно:\n" + multiSentenceV2.getRuSentence() + "\n" + multiSentenceV2.getEnSentence()
                            : "Ошибки небыло"));
                }
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

    public void generateText() {
        if (lessonId < BAD_LESSONS_COUNT) {
            // todo | Refactor It! Do It!!
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
        } else {
            multiSentenceV2 = sentenceMaker.makeSentence();
        }
        setSentence();
    }

    private void setSentence() {
        if (lessonId < BAD_LESSONS_COUNT) {
            // todo | Refactor It! Do It!!
            taskField.setText(multiSentence.getRuSentence());
            if (mode == MODE_EASY) {
                resultField.setText(multiSentence.getEnSentence());
            }
        } else {
            taskField.setText(multiSentenceV2.getRuSentence());
            if (mode == MODE_EASY) {
                if (new Random().nextBoolean()) { // 50% wrong
                    resultField.setText(multiSentenceV2.getEnSentence());
                } else {
                    resultField.setText(multiSentenceV2.getWrongSentence());
                }
            }
        }
    }

    public void buttonsEnabled(boolean isEnabled){
        if (mode == MODE_HARD) {
            button3Say.setEnabled(isEnabled);
            button2OK.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
            buttonNext.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
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
        AlertDialog aboutDialog;
        if (lessonId == LAST_LEVEL) {
            aboutDialog = new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.title_dialog_complete_all_levels))
                    .setPositiveButton(getString(R.string.button_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        } else if (lessonId == COMPLETE - 1) {
            // Congratulation! Wait more levels and repeating!
            aboutDialog = new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.title_dialog_complete_available_levels))
                    .setPositiveButton(getString(R.string.button_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        } else {
            aboutDialog = new AlertDialog.Builder(activity)
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
        }
        aboutDialog.show();
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
