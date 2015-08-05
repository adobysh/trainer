package com.holypasta.trainer.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.SharedPreferencesUtil;

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
        mode = SharedPreferencesUtil.getInstance(getActivity()).getMode();
        if (mode == MODE_HARD) {
            buttonHardcoreMode.setText("Hardcore Mode ON");
        } else {
            buttonHardcoreMode.setText("Hardcore Mode OFF");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferencesUtil.getInstance(getActivity()).saveMode(mode);
    }

    @Click
    protected void buttonQuickStart() {
        SingleActivity singleActivity = (SingleActivity) getActivity();
        if (singleActivity == null) return;

        int lessonId;
        int lastOpenLessonId = SharedPreferencesUtil.getInstance(getActivity()).getLastOpenLessonId();
        int lastOpenLessonScore = SharedPreferencesUtil.getInstance(getActivity()).getLessonScore(lastOpenLessonId);
        int repeatLessonsLessonScore = SharedPreferencesUtil.getInstance(getActivity()).getLessonScore(REPEAT_LESSONS_LESSON);
        if ((lastOpenLessonId >= 1 && repeatLessonsLessonScore < MAX_SCORE)
                || (lastOpenLessonId == COMPLETE-1 && lastOpenLessonScore > 0)) {
            lessonId = REPEAT_LESSONS_LESSON;
        } else {
            lessonId = lastOpenLessonId;
        }
        if (lastOpenLessonId == 0) {
            SharedPreferencesUtil.getInstance(singleActivity).saveScore(REPEAT_LESSONS_LESSON, MAX_SCORE);
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
