package com.holypasta.trainer.levels;

import com.holypasta.trainer.data.MultiSentenceData;
import com.holypasta.trainer.data.SentenceParamData;
import com.holypasta.trainer.util.RuVerbs01;

import java.util.Random;

public class Level01 extends AbstractLevel {

    public MultiSentenceData makeSentence(int mode) {
        Random random = new Random();
        SentenceParamData form = new SentenceParamData(3);
        SentenceParamData time = new SentenceParamData(3);
        SentenceParamData who = new SentenceParamData(PRONOUN_IT, pronounsEN[0].length, true);
        who.nextRandom();
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
