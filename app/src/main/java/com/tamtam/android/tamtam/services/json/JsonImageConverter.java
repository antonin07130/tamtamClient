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
import android.util.LruCache;

import com.tamtam.android.tamtam.model.ThingPicture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.R.attr.mode;
import static android.R.attr.path;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.M;

/**
 * This converter is very special :
 * it takes an ancoded image as an input,
 * records the input to local storage and returns a reference to this image.
 */
public class JsonImageConverter extends JsonObjectConverter<byte[]> {
    private static final String TAG = "JsonImageConverter";

    private LruCache<File, Bitmap> mBitmapCache;

    static final int[] encodings = {Base64.DEFAULT, Base64.URL_SAFE, Base64.CRLF,
            Base64.NO_PADDING, Base64.NO_CLOSE, Base64.NO_WRAP };



    @Override
    protected byte[] readObject(JsonReader reader) throws IOException {
        byte[] decodedString = null;
        //Bitmap decodedByte = null;
        try {
            Log.d(TAG, "readObject: reading base64 byte[] from json");
            decodedString = Base64.decode(reader.nextString(), Base64.NO_WRAP | Base64.URL_SAFE);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "readObject: trying to read base64 String error make sure it was created with NO_WRAP and URL_SAFE arguments");
            throw new IOException("image base64 decoding error was it created with NO_WRAP and URL_SAFE flags ?", e);
        } 
        return decodedString;
    }

    
    @Override
    protected void writeObject(JsonWriter writer, byte[] modelObject) throws IOException {

        if (modelObject != null && modelObject.length >0) {
            Log.d(TAG, "writeObject: writing byte[] to base64 String");
            writer.value(Base64.encodeToString(modelObject, Base64.NO_WRAP | Base64.URL_SAFE));
        }
        else {
            Log.d(TAG, "writeObject: writing null value to json");
            writer.nullValue();
        }
    }
}
