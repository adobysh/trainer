package com.holypasta.trainer.util;

import android.content.Context;
import android.content.res.Resources;
import java.io.IOException;
import java.io.InputStream;

public class RawReader {

    public static String  getStringFromRawFile(Context context, int resId) {
        Resources r = context.getResources();
        InputStream is = r.openRawResource(resId); //openRawResource(resId);
        String myText = convertStreamToString(is);
        try {
            is.close();
        } catch (IOException e) {}
        return  myText;
    }

    private static String  convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
