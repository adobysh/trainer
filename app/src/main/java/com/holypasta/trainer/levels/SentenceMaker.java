package com.holypasta.trainer.levels;

import android.content.Context;
import android.util.SparseArray;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.data.AbstractMultiSentence;
import com.holypasta.trainer.util.AppState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SentenceMaker implements Constants {

    private final static int UNIQUE_COUNT = 10; // 20 take looping
    private final int lessonId;
    private final int mode;
    private int lastOpenLesson;
    private List<Integer> remainingLessonIds;
    private SparseArray<SentenceMaker> sentenceMakers;
    private Context context;
    private AbstractLevel level;
    private List<AbstractMultiSentence> pastMultiSentences;

    public SentenceMaker(Context context, int lessonId, int mode) {
        this.lessonId = lessonId;
        this.mode = mode;
        switch (lessonId) {
            case REPEAT_LESSONS_LESSON:
                this.context = context;
                lastOpenLesson = AppState.getInstance(context).getLastOpenLessonId();
                if (lastOpenLesson == 0) {
                    throw new IllegalArgumentException("wtf repeat?! open only first lesson");
                }
                remainingLessonIds = new ArrayList<>(lastOpenLesson);
                sentenceMakers = new SparseArray<>(lastOpenLesson);
                break;
            case 0:
                level = new Level01(mode);
                break;
            case 1:
                level = new Level02(mode);
                break;
            case 2:
                level = new Level03(mode);
                break;
            case 3:
                level = new Level04(mode);
                break;
            case 4:
                level = new Level05(mode);
                break;
            default:
                level = new LevelFromList(context, lessonId);
                break;
        }
    }

	public AbstractMultiSentence makeSentence() {
        if (lessonId == REPEAT_LESSONS_LESSON) {
            return makeSentenceRepeatLessons();
        } else if (lessonId > LAST_OLD_LOGIC_LESSON) {
            return makeSentenceNewLogic();
        } else {
            return makeSentenceOldLogic();
        }
	}

    private AbstractMultiSentence makeSentenceRepeatLessons() {
        if (!remainingLessonIds.isEmpty()) {
            remainingLessonIds.remove(0);
        }
        if (remainingLessonIds.isEmpty()) {
            for (int i = 0; i < lastOpenLesson; i++) {
                remainingLessonIds.add(i);
            }
            Collections.shuffle(remainingLessonIds);
        }
        int currentLessonId = remainingLessonIds.get(0);
        if (sentenceMakers.get(currentLessonId) == null) {
            sentenceMakers.put(currentLessonId, new SentenceMaker(context, currentLessonId, mode));
        }
        return sentenceMakers.get(currentLessonId).makeSentence();
    }

    private AbstractMultiSentence makeSentenceNewLogic() {
        return level.makeSentence();
    }

    private AbstractMultiSentence makeSentenceOldLogic() {
        AbstractMultiSentence multiSentence;
        if (pastMultiSentences == null) {
            pastMultiSentences = new ArrayList<>();
        }
        do {
            multiSentence = level.makeSentence();
        } while (pastMultiSentences.contains(multiSentence));
        pastMultiSentences.add(multiSentence);
        if (pastMultiSentences.size() >= UNIQUE_COUNT) {
            pastMultiSentences.remove(0);
        }
        return multiSentence;
    }

    public int getLessonId() {
        if (lessonId == REPEAT_LESSONS_LESSON) {
            int currentLessonId = remainingLessonIds.get(0);
            return currentLessonId;
        }
        return lessonId;
    }
}
