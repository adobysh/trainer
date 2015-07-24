package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.data.AbstractMultiSentence;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.levels.SentenceMaker;
import com.holypasta.trainer.util.SharedPreferencesUtil;

public abstract class AbstractLevelFragment extends Fragment implements Constants, View.OnClickListener {

    protected SingleActivity activity;
    protected View rootView;
    protected TextView taskField;
    protected TextView resultField;
    protected View buttonCheck;
    protected View buttonNext;
    protected View welcomeView;
    protected int lessonId;
    protected int score = 0;
    protected int colorGreen;
    protected int colorRed;
    protected AbstractMultiSentence multiSentence;
    protected SentenceMaker sentenceMaker;

    protected abstract void buttonNext();

    protected abstract void setSentence();

    protected abstract void setProgress(int score);

    protected abstract boolean check(int buttonId);

    protected abstract void buttonsEnabled(boolean isEnabled);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    protected void findCommonViews(View rootView) {
        taskField = (TextView) rootView.findViewById(R.id.textView1);
        buttonCheck = rootView.findViewById(R.id.button2OK);
        buttonNext = rootView.findViewById(R.id.buttonNext);
    }

    protected void postFindViews() {
        buttonNext.setOnClickListener(this);
        buttonCheck.setOnClickListener(this);
        Resources resources = getResources();
        colorGreen = resources.getColor(R.color.material_green);
        colorRed = resources.getColor(R.color.material_red);
        score = SharedPreferencesUtil.getLessonScore(activity, lessonId);
        setHasOptionsMenu(true);
        setProgress(score);
        activity.getSupportActionBar().setTitle((lessonId + 1) + " урок");
        boolean isFirstOpen = firstOpen(score);
        if (isFirstOpen) {
            welcomeView = rootView.findViewById(R.id.welcome);
            welcomeView.setVisibility(View.VISIBLE);
            Button firstStartButton = (Button)welcomeView.findViewById(R.id.button_start_lesson);
            Button videoButton = (Button)welcomeView.findViewById(R.id.button_video_lesson);
            Button theoryButton = (Button)welcomeView.findViewById(R.id.button_theory);
            firstStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    welcomeView.setVisibility(View.GONE);
                    buttonNext();
                }
            });
            videoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openVideo();
                }
            });
            theoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTheory();
                }
            });
        } else {
            buttonNext();
        }
    }

    protected boolean firstOpen(int score) {
        return score == 0;
    }

    protected void generateText() {
        multiSentence = sentenceMaker.makeSentence();
        setSentence();
    }

    protected void buttonCheck() {
        buttonCheck(-1);
    }

    protected void buttonCheck(int buttonId) {
        if (resultField.getText().length() == 0) return;
        boolean checkResult = check(buttonId);
        if (checkResult) {
            resultField.setTextColor(colorGreen);
            if (score < MAX_SCORE) {
                score++;
                if (score == MAX_SCORE) {
                    showNextLevelDialog();
                }
            }
            if (score >= MAX_SCORE) {
                unlockNextLesson();
            }
        } else {
            resultField.setTextColor(colorRed);
        }
        buttonsEnabled(false);
        setProgress(score);
        SharedPreferencesUtil.saveScore(activity, lessonId, score);
    }

    protected void unlockNextLesson() {
        SharedPreferencesUtil.unlockNextLesson(activity, lessonId);
    }

    protected void showNextLevelDialog() {
        AlertDialog aboutDialog;
        if (lessonId == LAST_LEVEL) {
            aboutDialog = new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.title_dialog_complete_all_levels))
                    .setPositiveButton(getString(R.string.button_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { /* nothing */ }
                    }).create();
        } else if (lessonId == COMPLETE - 1) {
            aboutDialog = new AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.title_dialog_complete_available_levels))
                    .setPositiveButton(getString(R.string.button_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { /* nothing */ }
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
                        public void onClick(DialogInterface dialog, int which) { /* nothing */ }
                    }).create();
        }
        aboutDialog.show();
    }

    private void openNextLevel() {
        removeFragmentFromBackStack();
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId + 1);
        Fragment fragment = this;
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
                openTheory();
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

    protected void openTheory() {
        Fragment fragment = new TheoryFragment_();
        openInformationFragment(fragment);
    }

    protected void openDictionary() {
        Fragment fragment = new DictionaryFragment();
        openInformationFragment(fragment);
    }

    protected void openInformationFragment(Fragment fragment) {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId);
        fragment.setArguments(arguments);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }

    protected void openVideo() { // need refactoring todo
        Uri videoUri;
        try {
            videoUri = Uri.parse(YOUTUBE_APP_BASE_URL + VIDEO_ID[lessonId]);
            startActivity(new Intent(Intent.ACTION_VIEW, videoUri));
        } catch (ActivityNotFoundException e) {
            videoUri = Uri.parse(YOUTUBE_SITE_BASE_URL + VIDEO_ID[lessonId]);
            startActivity(new Intent(Intent.ACTION_VIEW, videoUri));
        }
    }
}
