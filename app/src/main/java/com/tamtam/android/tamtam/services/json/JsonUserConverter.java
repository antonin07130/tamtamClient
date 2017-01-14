package com.tamtam.android.tamtam.services.json;

import com.tamtam.android.tamtam.model.UserObject;

import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.util.Set;

/**
 * Created by fcng1847 on 11/01/17.
 * This class implements a converter of {@link UserObject} to/from Json {@link String}.
 * This is the class to modify if the Json is modified in the future.
 */
public class JsonUserConverter extends JsonObjectConverter<UserObject>  {
    public static final String LOG_TAG = "JsonUserConverter";

    public static final String ID_KEYNAME = "userId";
    public static final String SELLINGLIST_KEYNAME = "sellingThings";
    public static final String INTERESTEDLIST_KEYNAME = "interestedIn";


    protected void writeObject(JsonWriter writer, UserObject user) throws IOException {
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


    protected UserObject readObject(JsonReader reader) throws IOException{
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

        if (userId == null || userId.isEmpty()) {
            return null;
            //throw new IOException("no valid user found");
        } else {
            return new UserObject(userId, interestedIn, sellingThings);
        }
    }
}
