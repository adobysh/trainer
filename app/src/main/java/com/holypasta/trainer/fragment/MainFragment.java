package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.adapter.LevelsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.JesusSaves;

import java.util.List;

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

    private ListView lv1main;
    private Button buttonRepeatLessons;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lv1main = (ListView) rootView.findViewById(R.id.lv1main);
        buttonRepeatLessons = (Button) rootView.findViewById(R.id.buttonRepeatLessons);
        // ------- after view --------
        buttonRepeatLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLesson(REPEAT_LESSONS_LESSON);
            }
        });
        if (JesusSaves.getInstance(getActivity()).getLastOpenLessonId() == 0) {
            buttonRepeatLessons.setVisibility(View.GONE);
        }
        parts = getResources().getStringArray(R.array.contents);
        mode = JesusSaves.getInstance(getActivity()).getMode();
        sosLesson = new SOSLesson();
        return rootView;
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
