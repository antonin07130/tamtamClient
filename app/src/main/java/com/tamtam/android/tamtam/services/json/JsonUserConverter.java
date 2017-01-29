/*
 *     Android application to create and display localized objects.
 *     Copyright (C) 2017  pascal bodin, antonin perrot-audet
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tamtam.android.tamtam.services.json;

import com.tamtam.android.tamtam.model.UserObject;

import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.util.Set;

/**
 * Created by antoninpa on 11/01/17.
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
        if (user.getSellingThings() != null && user.getInterestedIn() != null) {
            writer.name(SELLINGLIST_KEYNAME);
            writeStringArray(writer, user.getSellingThings() );
            writer.name(INTERESTEDLIST_KEYNAME);
            writeStringArray(writer, user.getInterestedIn());
        } else {
            throw new IOException("input UserObject invalid : a member collection is null"
                    + user.toString());
        }
        writer.endObject();
    }


    protected UserObject readObject(JsonReader reader) throws IOException {
        String userId = null;
        Set<String> interestedIn = null;
        Set<String> sellingThings = null;
        boolean emptyJson = true;

        reader.beginObject();
        if (reader.hasNext()) emptyJson = false;
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case ID_KEYNAME:
                    userId = reader.nextString();
                    break;
                case INTERESTEDLIST_KEYNAME:
                    interestedIn = readStringArray(reader);
                    break;
                case SELLINGLIST_KEYNAME:
                    sellingThings = readStringArray(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        if (emptyJson){
            return null;
        } else if (userId == null || userId.isEmpty()) {
            throw new IOException("No valid user found in input String");
        } else {
            return new UserObject(userId, interestedIn, sellingThings);
        }
    }
}
