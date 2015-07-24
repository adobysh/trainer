package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;

import java.util.Random;

public class Level01 extends AbstractLevel {

    public Level01(int mode) {
        super(mode);
    }

    public MultiSentenceData makeSentence() {
        Random random = new Random();
        SentenceParamData form = new SentenceParamData(3);
        SentenceParamData time = new SentenceParamData(3);
        SentenceParamData who = new SentenceParamData(PRONOUN_IT, pronounsEN[0].length, true);
        do {
            who.nextRandom();
        } while (time.value() == TIME_PAST && who.value() == PRONOUN_I);
        SentenceParamData verb = new SentenceParamData(verbs01[0].length);
        MultiSentenceData sentence = makeSentence(form.value(), time.value(), who.value(), who.value(), verb.value());
        if (mode == MODE_HARD) {
            return sentence;
        } else { // MODE_EASY
            if (random.nextBoolean()) { // 50% true
                return sentence;
            }
            System.out.println("!!! false");
            int who0 = who.value();
            if (random.nextInt(5) != 0) { // 80% change time
                time.nextRandom();
            } else { // 20%
                switch (random.nextInt(4)) {
                    case 0:
                        form.nextRandom();
                        break;
                    case 1:
                        who.nextRandom();
                        who0 = who.value();
                        break;
                    case 2:
                        verb.nextRandom();
                        break;
                    case 3:
                        who.nextRandom();
                        break;
                }
            }
            return genWrongSentence(sentence, makeSentence(form.value(), time.value(), who0, who.value(), verb.value()));
        }
    }

    public MultiSentenceData makeSentence(int form, int time, int who, int who1, int verb) {
        return makeBaseForm(new RuVerbs01(), super.verbs01, form, time, who, who1, verb, pronounsRU);
    }
}
