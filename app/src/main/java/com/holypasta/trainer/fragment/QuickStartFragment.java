package com.holypasta.trainer.fragment;

import android.os.Bundle;
import android.widget.Button;

import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.JesusSaves;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_quick_start)
public class QuickStartFragment extends AbstractFragment {

    @ViewById
    Button buttonHardcoreMode;

    @InstanceState
    int mode;

    @AfterViews
    void calledAfterViewInjection() {
        mode = JesusSaves.getInstance(getActivity()).getMode();
        if (mode == MODE_HARD) {
            buttonHardcoreMode.setText("Hardcore Mode ON");
        } else {
            buttonHardcoreMode.setText("Hardcore Mode OFF");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JesusSaves.getInstance(getActivity()).setMode(mode);
    }

    @Click
    void buttonQuickStart() {
        SingleActivity singleActivity = (SingleActivity) getActivity();
        if (singleActivity == null) return;

        int lessonId;
        int lastOpenLessonId = JesusSaves.getInstance(getActivity()).getLastOpenLessonId();
        int lastOpenLessonScore = JesusSaves.getInstance(getActivity()).getLessonScore(lastOpenLessonId);
        int repeatLessonsLessonScore = JesusSaves.getInstance(getActivity()).getLessonScore(REPEAT_LESSONS_LESSON);
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
    void buttonChoiceLesson() {
        SingleActivity singleActivity = (SingleActivity) getActivity();
        if (singleActivity != null) {
            singleActivity.openFragment(new MainFragment_());
        }
    }

    @Click
    void buttonHardcoreMode() {
        if (mode == MODE_HARD) {
            mode = MODE_EASY;
            buttonHardcoreMode.setText("Hardcore Mode OFF");
        } else {
            mode = MODE_HARD;
            buttonHardcoreMode.setText("Hardcore Mode ON");
        }
    }

//    @Click
//    void imageView() {
//        SingleActivity activity = (SingleActivity) getActivity();
//        if (activity != null) {
//            activity.openFragment(new DonateFragment());
//        }
//    }

    @Override
    protected String getTitle() {
        return getString(R.string.app_name);
    }
}
