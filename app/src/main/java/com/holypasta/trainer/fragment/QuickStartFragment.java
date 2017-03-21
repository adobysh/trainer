package com.holypasta.trainer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.JesusSaves;

public class QuickStartFragment extends AbstractFragment {

    private Button buttonHardcoreMode;
    private Button buttonQuickStart;
    private Button buttonChoiceLesson;
    private int mode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_quick_start, container, false);
        buttonHardcoreMode = (Button) rootView.findViewById(R.id.buttonHardcoreMode);
        buttonQuickStart = (Button) rootView.findViewById(R.id.buttonQuickStart);
        buttonChoiceLesson = (Button) rootView.findViewById(R.id.buttonChoiceLesson);
        // ------ after views --------
        buttonHardcoreMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == MODE_HARD) {
                    mode = MODE_EASY;
                    buttonHardcoreMode.setText("Hardcore Mode OFF");
                } else {
                    mode = MODE_HARD;
                    buttonHardcoreMode.setText("Hardcore Mode ON");
                }
            }
        });
        buttonQuickStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        buttonChoiceLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleActivity singleActivity = (SingleActivity) getActivity();
                if (singleActivity != null) {
                    singleActivity.openFragment(new MainFragment());
                }
            }
        });
        mode = JesusSaves.getInstance(getActivity()).getMode();
        if (mode == MODE_HARD) {
            buttonHardcoreMode.setText("Hardcore Mode ON");
        } else {
            buttonHardcoreMode.setText("Hardcore Mode OFF");
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        JesusSaves.getInstance(getActivity()).setMode(mode);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.app_name);
    }
}
