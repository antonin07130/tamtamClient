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

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.PriceObject;
import com.tamtam.android.tamtam.model.ThingPicture;

import java.io.IOException;
import java.util.Currency;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by fcng1847 on 11/01/17.
 * This class implements a converter of {@link ThingObject} to/from Json {@link String}.
 * This is the class to modify if the Json is modified in the future.
 */
public class JsonThingConverter extends JsonObjectConverter<ThingObject> {
    public static final String LOG_TAG = "JsonThingConverter";

    public static final String ID_KEYNAME = "thingId";
    public static final String DESCRIPTION_KEYNAME = "description";
    public static final String PICTURE_KEYNAME = "pict";
    public static final String PRICE_OBJ_KEYNAME = "price";
    public static final String CURRENCY_KEYNAME = "currency";
    public static final String PRICE_KEYNAME = "price";
    public static final String POSITION_OBJ_KEYNAME = "position";
    public static final String LONGITUDE_KEYNAME = "lon";
    public static final String LATITUDE_KEYNAME = "lat";
    public static final String STUCK_KEYNAME = "stuck";


    protected void writeObject(JsonWriter writer, ThingObject thing) throws IOException {
        JsonObjectConverter<ThingPicture> thingPictureMapper = new JsonImageConverter();

        writer.beginObject();

        writer.name(ID_KEYNAME).value(thing.getThingId());

        writer.name(DESCRIPTION_KEYNAME).value(thing.getDescription());

        writer.name(POSITION_OBJ_KEYNAME);
        writePosition(writer, thing.getPosition());

        writer.name(PRICE_OBJ_KEYNAME);
        writePrice(writer, thing.getPrice());

        writer.name(PICTURE_KEYNAME);
        thingPictureMapper.writeObject(writer,thing.getPict());

        writer.name(STUCK_KEYNAME).value(thing.getStuck());

        writer.endObject();
    }


    private void writePosition(JsonWriter writer, PositionObject position) throws IOException{
        writer.beginObject();
        writer.name(LONGITUDE_KEYNAME).value(position.getLongitude());
        writer.name(LATITUDE_KEYNAME).value(position.getLatitude());
        writer.endObject();
    }


    private void writePrice(JsonWriter writer, PriceObject price) throws IOException{
        writer.beginObject();
        writer.name(PRICE_KEYNAME).value(price.getValue());
        writer.name(CURRENCY_KEYNAME).value(price.getCurrency().getCurrencyCode());
        writer.endObject();
    }


    protected ThingObject readObject(JsonReader reader) throws IOException{
        JsonObjectConverter<ThingPicture> thingPictureMapper = new JsonImageConverter();
        ThingObject.ThingBuilder thingBuilder = new ThingObject.ThingBuilder();

        reader.beginObject();
        // null json object "{}"
        if (!reader.hasNext()) {
            return null;
        }
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(ID_KEYNAME)) {
                thingBuilder.thingId(reader.nextString());

            } else if (name.equals(POSITION_OBJ_KEYNAME)) {
                thingBuilder.position(readPosition(reader));

            } else if (name.equals(PRICE_OBJ_KEYNAME)) {
                thingBuilder.price(readPrice(reader));

            } else if (name.equals(DESCRIPTION_KEYNAME) && reader.peek() != JsonToken.NULL) {
                // we test for JsonToken.NULL because this is an optional parameter of UserObject
                thingBuilder.description(reader.nextString());

            } else if (name.equals(PICTURE_KEYNAME) && reader.peek() != JsonToken.NULL) {
                thingBuilder.pict(thingPictureMapper.readObject(reader));

            } else if (name.equals(STUCK_KEYNAME) && reader.peek() != JsonToken.NULL) {
                thingBuilder.stuck(reader.nextBoolean());

            } else {
                reader.skipValue();

            }
        }
        reader.endObject();

        if (thingBuilder.isValid()) {
            return thingBuilder.build(); // valid object
        } else{
            throw new IOException("Unable to build a valid Thing from json"); // error
        }
    }


    private PositionObject readPosition(JsonReader reader) throws IOException{
        double longitude = 0.;
        double latitude = 0.;
        int initialized_values = 0;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case LONGITUDE_KEYNAME:
                    longitude = reader.nextDouble();
                    initialized_values++;
                    break;
                case LATITUDE_KEYNAME:
                    latitude = reader.nextDouble();
                    initialized_values++;
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        if (initialized_values == 2)
            return new PositionObject(longitude, latitude);
        else
            throw new IOException("PositionObject Json malformed");
    }


    private PriceObject readPrice(JsonReader reader) throws IOException{
        double price = 0.;
        Currency currency = null;
        int initialized_values = 0;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case CURRENCY_KEYNAME:
                    currency = readCurrency(reader.nextString());
                    initialized_values++;
                    break;
                case PRICE_KEYNAME:
                    price = reader.nextDouble();
                    initialized_values++;
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        if (initialized_values == 2)
            return new PriceObject(currency, price);
        else
            throw new IOException("PriceObject Json malformed");
    }


    Currency readCurrency(String currencyCode) throws IOException{
        try {
            return Currency.getInstance(currencyCode);
        }
        catch (IllegalArgumentException e){
            // chain illegal argument to IO exception (converter deals with IOExceptions)
            throw new IOException("parsing price : invalid currency code", e);
        }
    }
}
