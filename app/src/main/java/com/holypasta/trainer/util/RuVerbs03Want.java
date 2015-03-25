package com.holypasta.trainer.util;

public class RuVerbs03WantLike extends RuVerbs {

    @Override
    public String make(int time, int who, int verb) {
        // time [ 1(future) | 2(present) | 3(past) ]
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
                        tmp = new String[] { "любит", "живет",
                                "работает", "открывает", "закрывает",
                                "начинает", "заканчивает", "видет",
                                "приходит", "ходит", "знает",
                                "думает" };
                        break;
                    case 5: // They
                        tmp = new String[] { "любят", "живут",
                                "работают", "открывают", "закрывают",
                                "начинают", "заканчивают", "видят",
                                "приходят", "ходят", "знают",
                                "думают" };
                        break;
                    default: // We
                        tmp = new String[] { "любим", "живём",
                                "работаем", "открываем", "закрываем",
                                "начинаем", "заканчиваем", "видим",
                                "приходим", "ходим", "знаем",
                                "думаем" };
                        break;
                }
                return tmp[verb];
            case 2:  // past -----------------------------------
                switch (who) {
                    case 0: // I
                    case 1: // You
                    case 2: // He
                        tmp = new String[] { "любил", "жил",
                                "работал", "открывал", "закрывал",
                                "начинал", "заканчивал", "видел",
                                "приходил", "ходил", "знал",
                                "думал" };
                        break;
                    case 3: // She
                        tmp = new String[] { "любила", "жила",
                                "работала", "открывала", "закрывала",
                                "начинала", "заканчивала", "видела",
                                "приходила", "ходила", "знала",
                                "думала" };
                        break;
                    case 4: // It
                        tmp = new String[] { "любило", "жило",
                                "работало", "открывало", "закрывало",
                                "начинало", "заканчивало", "видело",
                                "приходило", "ходило", "знало",
                                "думало" };
                        break;
                    case 5: // They
                    default: // We
                        tmp = new String[] { "любили", "жили",
                                "работали", "открывали", "закрывали",
                                "начинали", "заканчивали", "видели",
                                "приходили", "ходили", "знали",
                                "думали" };
                        break;
                }
                return tmp[verb];
        }
        return "x";
    }

    public String make(int time, int who) {
        return make(time, who, -1);
    }

}
