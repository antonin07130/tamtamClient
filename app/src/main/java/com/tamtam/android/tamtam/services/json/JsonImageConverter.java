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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * This converter is very special :
 * it takes an ancoded image as an input,
 * records the input to local storage and returns a reference to this image.
 */
public class JsonImageConverter extends JsonObjectConverter<ThingPicture> {
    private static final String TAG = "JsonImageConverter";

    private static final String PICTUREID_KEYNAME = "pictureId";
    private static final String PICTUREENCODED_KEYNAME = "picture_bitmap";


    @Override
    protected ThingPicture readObject(JsonReader reader) throws IOException {
        Bitmap parsedBitmap = null;

        String parsedPictureId = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case PICTUREID_KEYNAME:
                    parsedPictureId = reader.nextString();
                    break;
                case PICTUREENCODED_KEYNAME:
                    parsedBitmap  = readBitmap(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return new ThingPicture(parsedPictureId, parsedBitmap);
    }


    protected Bitmap readBitmap(JsonReader reader) throws IOException {
        byte[] decodedBytes = null;
        Bitmap decodedBitmap = null;

        try {
            Log.d(TAG, "readObject: reading base64 byte[] from json");
            decodedBytes = Base64.decode(reader.nextString(), Base64.NO_WRAP | Base64.URL_SAFE);
            decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "readObject: trying to read base64 String error make sure it was created with NO_WRAP and URL_SAFE arguments");
            throw new IOException("image base64 decoding error was it created with NO_WRAP and URL_SAFE flags ?", e);
        }

        return decodedBitmap;
    }


        @Override
    protected void writeObject(JsonWriter writer, ThingPicture modelObject) throws IOException {
        if (modelObject != null ) {
            writer.beginObject();

            writer.name(PICTUREID_KEYNAME);
            writer.value(modelObject.getPictureId());

            writer.name(PICTUREENCODED_KEYNAME);
            Log.d(TAG, "writeObject: compressing bitmap, writing byte[] to base64 String");
            writer.value(encodeToBase64(modelObject.getPicureBitmap(),
                    Bitmap.CompressFormat.JPEG, 100,
                    Base64.NO_WRAP | Base64.URL_SAFE));

            writer.endObject();
        }
        else {
            Log.d(TAG, "writeObject: writing null value to json");
            writer.nullValue();
        }
    }


    /**
     * aggressively copied from <a href="http://stackoverflow.com/questions/9768611/encode-and-decode-bitmap-object-in-base64-string-in-android">stackoverflow</>
     * @param image
     * @param compressFormat
     * @param quality
     * @return
     */
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality, int base64Flags)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
