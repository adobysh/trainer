package com.holypasta.trainer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.holypasta.trainer.english.R;
import com.holypasta.trainer.levels.SentenceMaker;
import com.holypasta.trainer.util.MakeScore;

public class LevelHardFragment extends AbstractLevelFragment implements TextView.OnEditorActionListener {

    private TextView textProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) { // lolfix
            return rootView;
        }
        Bundle extras = getArguments();
        lessonId = extras.getInt(EXTRA_LESSON_ID);
        rootView = inflater.inflate(R.layout.fragment_level_hard, container, false);

        super.findCommonViews(rootView);
        findViews(rootView);
        sentenceMaker = new SentenceMaker(activity, lessonId, MODE_HARD);
        super.postFindViews();
        return rootView;
	}

    protected void findViews(View rootView) {
        textProgress = (TextView) rootView.findViewById(R.id.tvScore);
        resultField = (EditText) rootView.findViewById(R.id.editText1);
    }

    @Override
    protected void setProgress(int score) {
        textProgress.setText(MakeScore.make(score));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftKeyboard((EditText) resultField);
        if (rootView != null) { // lolfix
            ViewGroup parentViewGroup = (ViewGroup) rootView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (welcomeView != null && welcomeView.getVisibility() != View.VISIBLE) {
            showSoftKeyboard((EditText) resultField);
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
    protected boolean check(int buttonId) {
        String answer = resultField.getText().toString();
        boolean checkResult = multiSentence.checkResult(answer);
        if (!checkResult) {
            taskField.setText(multiSentence.getRuSentence() + "\n" + multiSentence.getCorrectEnSentence());
        }
        return checkResult;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2OK:
                buttonCheck();
                break;
            case R.id.buttonNext:
                buttonNext();
                break;
        }
    }

    public void buttonNext(){
        generateText();
        resultField.setText(null);
        buttonsEnabled(true);
    }

    protected void setSentence() {
        taskField.setText(multiSentence.getRuSentence());
    }

    public void buttonsEnabled(boolean isEnabled){
        buttonCheck.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
        buttonNext.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) { // обрабатываем нажатие кнопки DONE на экранной клавиатуре
            int currentTextColor = resultField.getCurrentTextColor();
            if (currentTextColor == colorGreen || currentTextColor == colorRed) {
                buttonNext();
            } else {
                buttonCheck();
            }
            return true;
        }
        return false;
    }
}
