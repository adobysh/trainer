package com.holypasta.trainer.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by q1bot on 17.04.2015.
 */
public class DictionaryGenerator {

    private int levelId;

    public DictionaryGenerator(int levelId) {
        this.levelId = levelId;
    }

    public List<String> getDictionaryItems() {
        switch (levelId) {
            case 0:
                return getItems1();
            case 1:
                return getItems2();
            case 2:
                return getItems3();
            case 3:
                return getItems4();
            case 4:
                return getItems5();
            case 5:
                return getItems6();
            default:
                return null;
        }
    }

    public List<String> getItems1() {
        String[] itemsArray = new String[]{
                "I - я",
                "you - вы, ты",
                "he - он",
                "she - она",
                "we - мы",
                "they - они",

                "love - любить",
                "live - жить",
                "work - работать",
                "open - открыть",
                "close - закрыть",
                "see (saw) - видеть",
                "come (came) - приходить",
                "go (went) - идти",
                "know (knew) - знать",
                "think (thought) - думать",
                "start - начать",
                "finish - закончить"
        };
        return Arrays.asList(itemsArray);
    }

    public List<String> getItems2() {
        String[] itemsArray = new String[]{
                "me - меня, мне",
                "you - вас, вам",
                "him - его, ему",
                "her - её, ей",
                "us - нас, нам",
                "them - им",

                "ask - просить, спрашивать",
                "answer - отвечать",
                "give (gave) - давать",
                "take (took) - брать",
                "help - помогать",
                "hope - надеяться",
                "speak (spoke) - говорить",
                "travel - путешествовать",
                "think (thought) - думать",
                "work - работать",
                "come (came) - приходить",
                "live - жить",

                "what - что? какой?",
                "who - кто?",
                "where - где? куда?",
                "when - когда?",
                "why - почему? зачем?",
                "how - как?",

                "in - в (внутри)",
                "to - к, куда, к чему, к кому",
                "from - откуда, от кого, из чего"
        };
        return Arrays.asList(itemsArray);
    }

    public List<String> getItems3() {
        String[] itemsArray = new String[]{
                "my - моё, мой",
                "your - твоё, твой",
                "his - его",
                "her - её",
                "our - наше, наш",
                "their - их"
        };
        return Arrays.asList(itemsArray);
    }

    public List<String> getItems4() {
        String[] itemsArray = new String[]{
                "study - изучать",
                "art history - историю искуства",
                "work - работать",
                "museum - музей",
                "PR-manager - пиарменеджер",
                "writer - писатель",
                "actor - актёр, актриса",
                "theater - театре",
                "hello, hi - привет",
                "good morning - доброе утро (до 12:00)",
                "good afternoon - добрый день (до 18:00)",
                "good evening - добрый вечер",
                "good night - доброй ночи / спокойной ночи",
                "thank you, thanks - спасибо",
                "please - пожалуйста (сделай, помоги)",
                "welcome - пожалуйста (в ответ)",
                "nice to meet you - приятно познакомиться",
                "I'm sorry - приношу извинения",
                "I apologize for coming late - приношу извинение за опоздание",
                "I regret - я сожалею",
                "excuse me - извините (обратить внимание)",
                "forgive me - простите (переспросить)",
                "goodbye, bye - до свиданья"
        };
        return Arrays.asList(itemsArray);
    }

    public List<String> getItems5() {
        String[] itemsArray = new String[]{
                "old - старый",
                "older - старше",
                "oldest - старейший",
                "beautiful - красивый",
                "more beautiful - красивее",
                "most beautiful - самый красивый",
                "young - молодой",
                "younger - моложе",
                "youngest - самый молодой",
                "short - короткий",
                "shorter - короче",
                "shortest - самый короткий",
                "big - большой",
                "bigger - больше",
                "biggest - самый большой",
                "good - хороший",
                "better - лучше",
                "best - лучший",
                "bad - плахой",
                "worse - хуже",
                "worst - наихудший",
                "yesterday - вчера",
                "today - сегодня",
                "tomorrow - завтра",
                "now - сейчас",
                "in - через",
                "ago - назад",
                "day - день",
                "week - неделя",
                "month - месяц",
                "year - год",
                "Norway - Норвегия",
                "Moscow - Москва",
                "Kiev - Киев",
                "home - дом",
                "at - в (время)",
                "on - в (день недели)",
                "in - в (месяц, время года)",
                "before - до",
                "after - после",

                "Monday - понедельник",
                "Tuesday - вторник",
                "Wednesday - среда",
                "Thursday - четверг",
                "Friday - пятница",
                "Saturday - суббота",
                "Sunday - воскресенье",

                "January - январь",
                "February - февраль",
                "March - март",
                "April - апрель",
                "May - май",
                "June - июнь",
                "July - июль",
                "August - август",
                "September - сентябрь",
                "October - октябрь",
                "November - ноябрь",
                "December - декабрь",

                "last - прошлый",
                "this - текущий",
                "next - следующий",

                "winter - зима",
                "spring - весна",
                "summer - лето",
                "autumn - осень",

                "jewelry designer - ювелирный дизайнер"
        };
        return Arrays.asList(itemsArray);
    }

    public List<String> getItems6() {
        String[] itemsArray = new String[]{
                "time - время",
                "dollar - доллар",
                "rouble - рубль",
                "money - деньги",
                "hour - час",
                "love - любовь",
                "day - день",
                "people - люди",
                "have, has (had) - иметь (с местоимениями he, she - has)",
                "much - много (нельзя посчитать)",
                "many - много (можно посчитать)",
                "everybody - все, всякий, каждый",
                "somebody - кто-то, кто-нибудь, кое-кто",
                "nobody - никто",
                "everything - всё",
                "something - что-нибудь, что-то, кое-что",
                "nothing - ничего",
                "everywhere - везде, всюду",
                "somewhere - где-то, куда-то, где-нибудь",
                "nowhere - нигде, никуда",
                "always - всегда",
                "sometimes - иногда",
                "never - никогда"
        };
        return Arrays.asList(itemsArray);
    }

}
