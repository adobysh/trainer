package com.holypasta.trainer.util;

public class RuVerbs03ToBe extends RuVerbs {

    @Override
	public String make(int time, int who, int verb) {
		// time [ 1(future) | 2(present) | 3(past) ]
		String tmp = "";
		switch (time) {
		case 0: // future ----------------------------------------
			switch (who) {
			case 0: // I
				tmp = "буду";
				break;
			case 1: // You
				tmp = "будешь";
				break;
			case 2: // He
			case 3: // she
			case 4: // It
				tmp = "будет";
				break;
			case 5: // they
				tmp = "будут";
				break;
			default: // We
				tmp = "будем";
				break;
			}
            break;
		case 1: // present ------------------------------------
			tmp = "";
            break;
        case 2:  // past -----------------------------------
			switch (who) {
			case 0: // I
			case 1: // You
			case 2: // He
				tmp = "был";
				break;
			case 3: // She
                tmp = "была";
				break;
			case 4: // It
                tmp = "было";
				break;
			case 5: // They
			default: // We
                tmp = "были";
				break;
			}
            break;
		}
        return tmp;
	}

    public String make(int time, int who) {
        return make(time, who, -1);
    }

}
