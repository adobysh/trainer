package com.holypasta.trainer.fragment;

import android.os.Bundle;
import android.widget.Button;

import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.AppState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_quick_start)
public class QuickStartFragment extends AbstractFragment {

    @ViewById
    protected Button buttonHardcoreMode;

    @InstanceState
    protected int mode;

    @AfterViews
    void calledAfterViewInjection() {
        mode = AppState.getInstance(getActivity()).getMode();
        if (mode == MODE_HARD) {
            buttonHardcoreMode.setText("Hardcore Mode ON");
        } else {
            buttonHardcoreMode.setText("Hardcore Mode OFF");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AppState.getInstance(getActivity()).setMode(mode);
    }

    @Click
    protected void buttonQuickStart() {
        SingleActivity singleActivity = (SingleActivity) getActivity();
        if (singleActivity == null) return;

        int lessonId;
        int lastOpenLessonId = AppState.getInstance(getActivity()).getLastOpenLessonId();
        int lastOpenLessonScore = AppState.getInstance(getActivity()).getLessonScore(lastOpenLessonId);
        int repeatLessonsLessonScore = AppState.getInstance(getActivity()).getLessonScore(REPEAT_LESSONS_LESSON);
        if ((lastOpenLessonId >= 1 && repeatLessonsLessonScore < SCORE_MAX)
                || (lastOpenLessonId == COMPLETE-1 && lastOpenLessonScore > 0)) {
            lessonId = REPEAT_LESSONS_LESSON;
        } else {
            lessonId = lastOpenLessonId;
        }
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId);
        AbstractLevelFragment fragment = (mode == MODE_EASY) ? new LevelEasyFragment() : new LevelHardFragment();
        singleActivity.openFragment(fragment, arguments);
    }

    @Click
    protected void buttonChoiceLesson() {
        SingleActivity singleActivity = (SingleActivity) getActivity();
        if (singleActivity != null) {
            singleActivity.openFragment(new MainFragment_());
        }
    }

    @Click
    protected void buttonHardcoreMode() {
        if (mode == MODE_HARD) {
            mode = MODE_EASY;
            buttonHardcoreMode.setText("Hardcore Mode OFF");
        } else {
            mode = MODE_HARD;
            buttonHardcoreMode.setText("Hardcore Mode ON");
        }
    }

    @Override
    protected void setTitle() {
        ((SingleActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
    }
}
