package com.holypasta.trainer.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by q1bot on 08.07.14.
 */
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
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            int i = is.read();
//            while( i != -1) {
//                baos.write(i);
//                i = baos.size();
//            }
//            return  baos.toString();
//        } catch (IOException e) {}
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
