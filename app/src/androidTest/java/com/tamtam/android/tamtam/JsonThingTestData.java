package com.tamtam.android.tamtam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by antoninpa on 14/01/17.
 */

/**
 * Utility static class containing
 *  - 3 bitmaps
 *  - 3 bitmaps encoded in base64
 *  - 3 ThingObjects Json representations (Strings)
 *  - 3 ThingPicture Json representations (Strings)
 *  - some utility functions.
 */
public class JsonThingTestData {


    public static final Bitmap testBitmap1 =
            BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                    com.tamtam.android.tamtam.test.R.drawable.test_png_1);

    public static final Bitmap testBitmap2 =
            BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                    com.tamtam.android.tamtam.test.R.drawable.test_png_2);

    public static final Bitmap testBitmap3 =
            BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                    com.tamtam.android.tamtam.test.R.drawable.test_png_3);

    static public String bitmapToString(Bitmap inputBitmap){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        inputBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP | Base64.URL_SAFE);
    }



    //constant strings
    public static final String JSON_PICTURE_STRING_1 =
            "{\"pictureId\":\"pictureId1\",\"bitmap\":\"" + bitmapToString(testBitmap1) + "\"}";

    public static final String JSON_PICTURE_STRING_2 =
            "{\"pictureId\":\"pictureId2\",\"bitmap\":\"" + bitmapToString(testBitmap2) + "\"}";

    public static final String JSON_PICTURE_STRING_3 =
            "{\"pictureId\":\"pictureId3\",\"bitmap\":\"" + bitmapToString(testBitmap3) + "\"}";



    public static final String JSON_THING_STRING_1 = "{\"thingId\":\"thing1\"," +
            "\"pict\":" + JSON_PICTURE_STRING_1 + "," +
            "\"description\":\"cest un premier truc\"," +
            "\"price\":{\"currency\":\"EUR\",\"price\":10.10}," +
            "\"position\":{\"lon\":7.05289,\"lat\":43.6166}," +
            "\"stuck\":false}";
    public static final String JSON_THING_STRING_2 = "{\"thingId\":\"thing2\"," +
            "\"pict\":" + JSON_PICTURE_STRING_2 + "," +
            "\"description\":\"cest un deuxieme truc\"," +
            "\"price\":{\"currency\":\"EUR\",\"price\":20.20}," +
            "\"position\":{\"lon\":7.05334,\"lat\":43.61664}," +
            "\"stuck\":false}";
    public static final String JSON_THING_STRING_3 = "{\"thingId\":\"thing3\"," +
            "\"pict\":" + JSON_PICTURE_STRING_3 + "," +
            "\"description\":\"cest un troisieme truc\"," +
            "\"price\":{\"currency\":\"EUR\",\"price\":30.30}," +
            "\"position\":{\"lon\":7.12153,\"lat\":43.65839}," +
            "\"stuck\":false}";



    /***
     * ugly utility function to verify that a Json String is parsable
     * @param test json string to verify.
     * @return true if parsable by {@link JSONObject} or {@link JSONArray}, false otherwise
     */
    public static boolean ParsableJson(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
