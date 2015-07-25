package com.holypasta.trainer.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.holypasta.trainer.english.R;
import com.holypasta.trainer.levels.SentenceMaker;

import java.util.Random;

import static android.view.View.GONE;

public class LevelEasyFragment extends AbstractLevelFragment implements Animation.AnimationListener {

    private TextView message;
    private View buttonFalseCheck;
    private ProgressBar progressBar;
    private Animation fieldsAnimationIn;
    private Animation fieldsAnimationOut;
    private Animation messageAnimationIn;
    private Animation messageAnimationOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) { // lolfix
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_level_easy, container, false);

        super.findCommonViews(rootView);
        findViews(rootView);
        buttonFalseCheck.setOnClickListener(this);
        loadAnimations();
        sentenceMaker = new SentenceMaker(activity, lessonId, MODE_EASY);
        super.postFindViews();
        return rootView;
	}

    private void loadAnimations() {
        fieldsAnimationIn = AnimationUtils.loadAnimation(activity, R.anim.slide_in);
        fieldsAnimationOut = AnimationUtils.loadAnimation(activity, R.anim.slide_out);
        messageAnimationIn = AnimationUtils.loadAnimation(activity, R.anim.scale_in);
        messageAnimationOut = AnimationUtils.loadAnimation(activity, R.anim.scale_out);
        fieldsAnimationOut.setAnimationListener(this);
        messageAnimationOut.setAnimationListener(this);
    }

    protected void findViews(View rootView) {
        resultField = (TextView) rootView.findViewById(R.id.result_easy);
        buttonFalseCheck = rootView.findViewById(R.id.answer_false);
        message = (TextView) rootView.findViewById(R.id.message);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
    }

    @Override
    protected void setProgress(int score) {
        progressBar.setProgress(score);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) { // lolfix
            ViewGroup parentViewGroup = (ViewGroup) rootView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    protected boolean check(int buttonId) {
        String answer = resultField.getText().toString();
        boolean checkSentence = multiSentence.checkResult(answer);
        boolean checkResult = buttonId == R.id.button2OK ? checkSentence : !checkSentence;

        if (checkResult) {
            buttonNextWithTimer();
        } else {
//            if (score > 1) { todo
//                score--;
//            }
            if (buttonId == R.id.answer_false) {
                buttonNextWithTimer();
            }
        }
        buttonNext.setVisibility(View.VISIBLE);
        showMessage(checkResult, buttonId);

        return checkResult;
    }

    protected void buttonFalseCheck() {
        buttonCheck(R.id.answer_false);
    }

    protected void buttonCheck() {
        buttonCheck(R.id.button2OK);
    }

    private void showMessage(boolean isTrue, int buttonId) {
        String correctEnSentence = multiSentence.getCorrectEnSentence();
        String text;
        if (isTrue) {
            text = "+1";
        } else {
            if (buttonId == R.id.button2OK) {
                text = "Неправильно:\n" + resultField.getText().toString() +
                        "\n\nПравильно:\n" + multiSentence.getRuSentence() + "\n" + correctEnSentence;
            } else {
                text = "Ошибки небыло";
            }
        }
        message.setText(text);
        int drawableId = isTrue ? R.drawable.button_green : R.drawable.button_red;
        message.setBackgroundResource(drawableId);
        message.setVisibility(View.VISIBLE);
        message.startAnimation(messageAnimationIn);
        resultField.startAnimation(fieldsAnimationOut);
        taskField.startAnimation(fieldsAnimationOut);
    }

    private void showFields() {
        message.startAnimation(messageAnimationOut);
        resultField.setVisibility(View.VISIBLE);
        taskField.setVisibility(View.VISIBLE);
        resultField.startAnimation(fieldsAnimationIn);
        taskField.startAnimation(fieldsAnimationIn);
    }

    public void buttonNext() {
        buttonNext.setVisibility(View.GONE);
        showFields();
        generateText();
        buttonsEnabled(true);
    }

    public void buttonNextWithTimer() {
        final int timeToNext = 3000;
        new CountDownTimer(timeToNext, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 2000) {
                    if (buttonNext.getVisibility() == View.VISIBLE) {
                        buttonNext();
                    }
                }
            }

            @Override
            public void onFinish() { }
        }.start();
    }

    protected void setSentence() {
        taskField.setText(multiSentence.getRuSentence());
        resultField.setText(multiSentence.getEnSentence());
    }

    public void buttonsEnabled(boolean isEnabled){
        buttonCheck.setEnabled(isEnabled);
        buttonFalseCheck.setEnabled(isEnabled);
    }

    @Override
    public void onAnimationStart(Animation animation) { }

    @Override
    public void onAnimationRepeat(Animation animation) { }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == fieldsAnimationOut) {
            resultField.setVisibility(View.INVISIBLE);
            taskField.setVisibility(View.INVISIBLE);
        } else if (animation == messageAnimationOut) {
            message.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2OK:
                buttonCheck();
                break;
            case R.id.answer_false:
                buttonFalseCheck();
                break;
            case R.id.buttonNext:
                buttonNext();
                break;
        }
    }
}
