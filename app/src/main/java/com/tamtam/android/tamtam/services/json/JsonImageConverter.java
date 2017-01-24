package com.tamtam.android.tamtam.services.json;

/**
 * Created by fcng1847 on 24/01/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.tamtam.android.tamtam.model.ThingPicture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.R.attr.path;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.M;

/**
 * This converter is very special :
 * it takes an ancoded image as an input,
 * records the input to local storage and returns a reference to this image.
 */
public class JsonImageConverter extends JsonObjectConverter<ThingPicture> {
    private static final String TAG = "JsonImageConverter";

    static final int[] encodings = {Base64.DEFAULT, Base64.URL_SAFE, Base64.CRLF,
            Base64.NO_PADDING, Base64.NO_CLOSE, Base64.NO_WRAP };


    @Override
    protected ThingPicture readObject(JsonReader reader) throws IOException {
        byte[] decodedString = null;
        Bitmap decodedByte = null;
        for (int encoding : encodings) {
            try {
                decodedString = Base64.decode(reader.nextString(), encoding);
            } catch (IllegalArgumentException e) {
                if (! e.getMessage().contains("bad base-64")) {
                    throw e;
                }
                Log.d(TAG, "readObject: trying other base 64 decode : " + encoding);
            } finally { // todo make sure finally close is needed here
                reader.close();
            }
        }

        //Do we need to transfor it to bitmap ?
        /**
        if (decodedString != null){
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
         **/

        String filename = "myfile";
        File fileToWrite = new File("PQTHOFTHISFILE");
        FileOutputStream outputStream = new FileOutputStream(fileToWrite);

        try {
            //outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(decodedString);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // todo maybe add the "image to repo" that deals with caching, bitmap convesion for display...

        // read a Json
        // write it to disk
        // return the file url in a file object in a ThingPicture
        return new ThingPicture(fileToWrite);
    }

    @Override
    protected void writeObject(JsonWriter writer, ThingPicture modelObject) throws IOException {

    }
}
