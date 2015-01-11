package com.holypasta.trainer.util;

import android.app.Activity;
import android.content.res.Resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by q1bot on 08.07.14.
 */
public class RawReader {

    public static String  getStringFromRawFile(Activity activity, int resId)
    {
        Resources r = activity.getResources();
        InputStream is = r.openRawResource(resId);
        String myText = convertStreamToString(is);
        try {
            is.close();
        } catch (IOException e) {}
        return  myText;
    }

    private static String  convertStreamToString(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int i = is.read();
            while( i != -1)
            {
                baos.write(i);
                i = baos.size();
            }
            return  baos.toString();
        } catch (IOException e) {}
        return null;
    }
}
