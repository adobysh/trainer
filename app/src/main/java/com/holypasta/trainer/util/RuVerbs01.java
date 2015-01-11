package com.holypasta.trainer.util;

public class RuVerbs01 {

	public static String make(int time, int why, int c) {
		// time [ 1(future) | 2(present) | 3(past) ]
		String[] tmp;
		switch (time) {
		case 1: // future ----------------------------------------
			switch (why) {
			case 0: // I
				tmp = new String[] { "буду любить", "буду жить",
						"буду работать", "буду открывать", "буду закрывать",
						"буду начинать", "буду заканчивать", "буду видеть",
						"буду приходить", "буду ходить", "буду знать",
						"буду думать" };
				break;
			case 1: // You
				tmp = new String[] { "будешь любить", "будешь жить",
						"будешь работать", "будешь открывать",
						"будешь закрывать", "будешь начинать",
						"будешь заканчивать", "будешь видеть",
						"будешь приходить", "будешь ходить", "будешь знать",
						"будешь думать" };
				break;
			case 2: // He
			case 3: // she
			case 4: // It
				tmp = new String[] { "будет любить", "будет жить",
						"будет работать", "будет открывать", "будет закрывать",
						"будет начинать", "будет заканчивать", "будет видеть",
						"будет приходить", "будет ходить", "будет знать",
						"будет думать" };
				break;
			case 5: // they
				tmp = new String[] { "будут любить", "будут жить",
						"будут работать", "будут открывать", "будут закрывать",
						"будут начинать", "будут заканчивать", "будут видеть",
						"будут приходить", "будут ходить", "будут знать",
						"будут думать" };
				break;
			default: // We
				tmp = new String[] { "будем любить", "будем жить",
						"будем работать", "будем открывать", "будем закрывать",
						"будем начинать", "будем заканчивать", "будем видеть",
						"будем приходить", "будем ходить", "будем знать",
						"будем думать" };
				break;
			}
			return tmp[c];
		case 2: // present ------------------------------------
			switch (why) {
			case 0: // I
				tmp = new String[] { "люблю", "живу",
						"работаю", "открываю", "закрываю",
						"начинаю", "заканчиваю", "вижу",
						"прихожу", "хожу", "знаю",
						"думаю" };
				break;
			case 1: // You
				tmp = new String[] { "любишь", "живешь",
						"работаешь", "открываешь", "закрываешь",
						"начинаешь", "заканчиваешь", "видешь",
						"приходишь", "ходишь", "знаешь",
						"думаешь" };
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
			return tmp[c];
		default: // past -----------------------------------
			switch (why) {
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
			return tmp[c];
		}
		

	}

}
