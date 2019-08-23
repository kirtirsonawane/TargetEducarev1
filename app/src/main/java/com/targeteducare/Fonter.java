package com.targeteducare;

import android.content.Context;
import android.graphics.Typeface;
import java.util.Hashtable;

public class Fonter {
	private final static String FONT_BOLD = "bold.otf";
	private final static String FONT_DIR = "fonts/";
	private final static String REGULAR = "opensansregular.ttf";
	private final static String LIGHT = "opensanslight.ttf";
	private final static String SEMIBOLD = "opensanssemibold.ttf";
	private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(2);

	public static Typeface getTypeFace(Context context, String fileName) {
		Typeface tempTypeface = sTypeFaces.get(fileName);

		if (tempTypeface == null) {
			String fontPath = FONT_BOLD;
			tempTypeface = Typeface.createFromAsset(context.getAssets(), fontPath);
			sTypeFaces.put(fileName, tempTypeface);
		}

		return tempTypeface;
	}

	public static Typeface getTypefacebold(Context context){
		Typeface tempTypeface  = sTypeFaces.get(FONT_BOLD);
		if (tempTypeface == null) {
			tempTypeface = Typeface.createFromAsset(context.getAssets(), FONT_DIR + FONT_BOLD);
			sTypeFaces.put(FONT_BOLD, tempTypeface);
		}
		return tempTypeface;
	}

	public static Typeface getTypefaceregular(Context context){
		Typeface tempTypeface = sTypeFaces.get(REGULAR);
		if (tempTypeface == null) {
			tempTypeface = Typeface.createFromAsset(context.getAssets(), FONT_DIR + REGULAR);
			sTypeFaces.put(REGULAR, tempTypeface);
		}
		return tempTypeface;
	//	return Typeface.createFromAsset(context.getAssets(), FONT_DIR+FONT_REGULAR);
	}

	public static Typeface getTypefaceLight(Context context){
		return Typeface.createFromAsset(context.getAssets(), FONT_DIR+ LIGHT);
	}


	public static Typeface getTypefacesemibold(Context context){
		Typeface tempTypeface = sTypeFaces.get(SEMIBOLD);
		if(tempTypeface==null) {
			tempTypeface = Typeface.createFromAsset(context.getAssets(), FONT_DIR + SEMIBOLD);
			sTypeFaces.put(SEMIBOLD, tempTypeface);
		}
		return tempTypeface;
	}
}
