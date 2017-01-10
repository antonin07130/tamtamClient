package com.tamtam.android.tamtam.services;

import com.tamtam.android.tamtam.model.UserObject;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by fcng1847 on 11/01/17.
 * This class implements a converter of {@link UserObject} to/from Json {@link String}.
 */
public class JsonUserConverter implements JsonToModelReader<String, UserObject>, ModelToJsonWriter<UserObject, String> {
    public static final String LOG_TAG = "JsonUserConverter";

    public static final String ID_KEYNAME = "userId";
    public static final String SELLINGLIST_KEYNAME = "sellingThings";
    public static final String INTERESTEDLIST_KEYNAME = "interestedIn";

    /**
     * Reads a Json String and aextracts the first UserObject found.
     * @param inputJson input Json String.
     * @return the first {@link UserObject} contained in inputJson or null if none was found.
     */
    @Override
    public UserObject fromJson(String inputJson) {
        // "streamize" input String and setup a JsonReader on it
        StringReader jsonStream = new StringReader(inputJson);
        UserObject parsedUser = null;
        try {
            parsedUser = readUserFromJsonStream(jsonStream);
        } catch (IOException e){
            Log.e(TAG, "fromJson: Parsing error with", e);
        }
        return parsedUser;
    }

    /**
     * Writes a Json String containing the input Object
     * @param inputModelObject {@link UserObject} to convert to a Json String
     * @return Json String encoding of the input UserObject
     */
    @Override
    public String toJson(UserObject inputModelObject) {
        StringWriter jsonOutputStream = new StringWriter();
        String resultJson = null;
        try {
            writeJsonStreamFromUser(jsonOutputStream, inputModelObject);
            resultJson = jsonOutputStream.toString();
        }catch (IOException e){
            Log.e(TAG, "toJson: writing to json error", e);
        }
        return resultJson;
    }


    private void writeJsonStreamFromUser(Writer out, UserObject modelObject) throws IOException {
        JsonWriter writer = new JsonWriter(out);
        //writer.setIndent("  ");
        writeUser(writer, modelObject);
        writer.close();
    }

    private void writeUser(JsonWriter writer, UserObject user) throws IOException {
        writer.beginObject();
        writer.name(ID_KEYNAME).value(user.getUserId());
        if (user.getSellingThings() != null) {
            writer.name(SELLINGLIST_KEYNAME);
            writeStringArray(writer, user.getSellingThings() );
        }
        if (user.getInterestedIn() != null) {
            writer.name(INTERESTEDLIST_KEYNAME);
            writeStringArray(writer, user.getInterestedIn());
        }
        writer.endObject();
    }

    private void writeStringArray(JsonWriter writer, Set<String> strings) throws IOException {
        writer.beginArray();
        for (String value : strings) {
            writer.value(value);
        }
        writer.endArray();
    }


    private UserObject readUserFromJsonStream(Reader jsonStream) throws IOException{
        JsonReader reader = new JsonReader(jsonStream);
        try{
            return readUser(reader);
        } finally {
            reader.close();
        }
    }

    private UserObject readUser(JsonReader reader) throws IOException{
        String userId = null;
        Set<String> interestedIn = null;
        Set<String> sellingThings = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(ID_KEYNAME)) {
                userId = reader.nextString();
            } else if (name.equals(INTERESTEDLIST_KEYNAME)) {
                interestedIn = readStringArray(reader);
            } else if (name.equals(SELLINGLIST_KEYNAME)) {
                sellingThings = readStringArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new UserObject(userId, interestedIn, sellingThings);
    }

    private Set<String> readStringArray(JsonReader reader) throws IOException{
        Set<String> strings = new HashSet<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            strings.add(reader.nextString());
        }
        reader.endArray();

        return strings;
    }

}
