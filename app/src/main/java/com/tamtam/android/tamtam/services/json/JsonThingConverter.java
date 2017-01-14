package com.tamtam.android.tamtam.services.json;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.model.ThingObject.PositionObject;
import com.tamtam.android.tamtam.model.ThingObject.PriceObject;

import java.io.IOException;

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
        writer.beginObject();
        writer.name(ID_KEYNAME).value(thing.getThingId());
        writer.name(DESCRIPTION_KEYNAME).value(thing.getDescription());
        writer.name(POSITION_OBJ_KEYNAME);
        writePosition(writer, thing.getPosition());
        writer.name(PRICE_OBJ_KEYNAME);
        writePrice(writer, thing.getPrice());
        writer.name(PICTURE_KEYNAME).value(thing.getPict());
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
        writer.name(PRICE_KEYNAME).value(price.getPrice());
        writer.name(CURRENCY_KEYNAME).value(price.getCurrency());
        writer.endObject();
    }


    protected ThingObject readObject(JsonReader reader) throws IOException{
        ThingObject.ThingBuilder thingBuilder = new ThingObject.ThingBuilder();

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(ID_KEYNAME)) {
                thingBuilder.thingId(reader.nextString());
            } else if (name.equals(POSITION_OBJ_KEYNAME)) {
                thingBuilder.position(readPosition(reader));
            } else if (name.equals(PRICE_OBJ_KEYNAME)) {
                thingBuilder.price(readPrice(reader));
            } else if (name.equals(DESCRIPTION_KEYNAME)) {
                thingBuilder.description(reader.nextString());
            } else if (name.equals(PICTURE_KEYNAME)) {
                thingBuilder.pict(reader.nextString());
            } else if (name.equals(STUCK_KEYNAME)) {
                thingBuilder.stuck(reader.nextBoolean());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if (thingBuilder.isValid()) {
            return thingBuilder.build();
        } else{
            return null; // todo maybe we could throw an exception here ? but beware of empty Jsons
        }
    }


    private PositionObject readPosition(JsonReader reader) throws IOException{
        double longitude = 0.;
        double latitude = 0.;
        int initialized_values = 0;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(LONGITUDE_KEYNAME)) {
                longitude = reader.nextDouble();
                initialized_values++;
            } else if (name.equals(LATITUDE_KEYNAME)) {
                latitude = reader.nextDouble();
                initialized_values++;
            } else reader.skipValue();
        }
        reader.endObject();

        if (initialized_values == 2)
            return new PositionObject(longitude, latitude);
        else
            throw new IOException("PositionObject Json malformed");
    }


    private PriceObject readPrice(JsonReader reader) throws IOException{
        double price = 0.;
        int currency = 0;
        int initialized_values = 0;
        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(CURRENCY_KEYNAME)) {
                currency = reader.nextInt();
                initialized_values++;
            } else if (name.equals(PRICE_KEYNAME)) {
                price = reader.nextDouble();
                initialized_values++;
            } else reader.skipValue();
        }
        reader.endObject();

        if (initialized_values == 2)
          return new PriceObject(currency, price);
        else
            throw new IOException("PriceObject Json malformed");
    }
}
