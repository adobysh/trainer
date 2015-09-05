package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.adapter.LevelsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.JesusSaves;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_main)
public class MainFragment extends AbstractFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private class SOSLesson {

        private int lessonId;
        private int count;

        private SOSLesson() {
            lessonId = -1;
        }

        public boolean pip(int lessonId, Context context) {
            if (lessonId == this.lessonId) {
                count++;
            } else {
                this.lessonId = lessonId;
                count = 0;
            }

            if (count == 5) {
                JesusSaves.getInstance(context).unlockUntoLesson(lessonId);
                return true;
            } else {
                return false;
            }
        }

    }

    @ViewById
    ListView lv1main;
    @ViewById
    Button buttonRepeatLessons;
    private LevelsAdapter adapter;
    private List<Integer> scores;
    private String[] parts;
    private int mode;
    private SingleActivity activity;
    private SOSLesson sosLesson;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @AfterViews
    void calledAfterViewInjection() {
        setHasOptionsMenu(true);
        if (JesusSaves.getInstance(getActivity()).getLastOpenLessonId() == 0) {
            buttonRepeatLessons.setVisibility(View.GONE);
        }
        parts = getResources().getStringArray(R.array.contents);
        mode = JesusSaves.getInstance(getActivity()).getMode();
        sosLesson = new SOSLesson();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_lessons);
    }

    @Override
    public void onResume() {
        super.onResume();
        scores = JesusSaves.getInstance(getActivity()).getScores();
        adapter = new LevelsAdapter(parts, activity, scores);
        lv1main.setAdapter(adapter);
        lv1main.setOnItemClickListener(this);
        lv1main.setOnItemLongClickListener(this);
    }

    @Click
    void buttonRepeatLessons() {
        openLesson(REPEAT_LESSONS_LESSON);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, final int lessonId, long arg3) {
        if (lessonId < Constants.COMPLETE) {
            int score = scores.get(lessonId);
            if (score > -1) {
                openLesson(lessonId);
            } else {
                final AlertDialog aboutDialog = new AlertDialog.Builder(activity)
                        .setMessage("Вам необходимо завершить предыдущие уроки")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
//                        })
//                        .setNegativeButton("Разблокировать", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                JesusSaves.getInstance(getActivity()).unlockUntoLesson(lessonId);
//                                openLesson(lessonId);
//                            }
                        }).create();
                aboutDialog.show();
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (sosLesson.pip(position, getActivity())) {
            scores = JesusSaves.getInstance(getActivity()).getScores();
            adapter = new LevelsAdapter(parts, activity, scores);
            lv1main.setAdapter(adapter);
        }
        return false;
    }

    private void openLesson(int lessonId) {
        SingleActivity singleActivity = (SingleActivity) getActivity();
        if (singleActivity == null) return;

        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId);
        AbstractLevelFragment levelFragment = mode == MODE_EASY ? new LevelEasyFragment() : new LevelHardFragment();
        singleActivity.openFragment(levelFragment, arguments);
    }
}
