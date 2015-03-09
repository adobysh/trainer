package com.holypasta.trainer.util;

public class RuVerbs03WantLike extends RuVerbs {

    protected final String[] FORMS_LIKE = { "нравится", "нравилось" };
    public final int VERB_WANT = 0;
    public final int VERB_LIKE = 1;

    @Override
    public String make(int time, int who, int verb) {
        if (verb == VERB_LIKE) {
            return FORMS_LIKE[time-1];
        }
        String tmp;
        switch (time) {
            case 1: // present ------------------------------------
                switch (who) {
                    case 0: // I
                        tmp = "хочу";
                        break;
                    case 1: // You
                        tmp = "хочешь";
                        break;
                    case 2: // He
                    case 3: // She
                    case 4: // It
                        tmp = "хочет";
                        break;
                    case 5: // They
                        tmp = "хотят";
                        break;
                    default: // We
                        tmp = "хотим";
                        break;
                }
                return tmp;
            case 2:  // past -----------------------------------
                switch (who) {
                    case 0: // I
                    case 1: // You
                    case 2: // He
                        tmp = "хотел";
                        break;
                    case 3: // She
                        tmp = "хотела";
                        break;
                    case 4: // It
                        tmp = "хотело";
                        break;
                    case 5: // They
                    default: // We
                        tmp = "хотели";
                        break;
                }
                return tmp;
        }
        return "x";
    }

}
