package com.tamtam.android.tamtam.services;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.model.ThingObject.PositionObject;
import com.tamtam.android.tamtam.model.ThingObject.PriceObject;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;
import static android.os.Build.ID;
import static com.tamtam.android.tamtam.services.JsonUserConverter.INTERESTEDLIST_KEYNAME;
import static com.tamtam.android.tamtam.services.JsonUserConverter.SELLINGLIST_KEYNAME;

/**
 * Created by fcng1847 on 11/01/17.
 * This class implements a converter of {@link ThingObject} to/from Json {@link String}.
 */
public class JsonThingConverter implements JsonToModelReader<String, ThingObject>, ModelToJsonWriter<ThingObject, String> {
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


    /**
     * Reads a Json String and aextracts the first UserObject found.
     * @param inputJson input Json String.
     * @return the first {@link ThingObject} contained in inputJson or null if none was found.
     */
    @Override
    public ThingObject fromJson(String inputJson) {
        // "streamize" input String and setup a JsonReader on it
        StringReader jsonStream = new StringReader(inputJson);
        ThingObject parsedThing = null;
        try {
            parsedThing = readThingFromJsonStream(jsonStream);
        } catch (IOException e){
            Log.e(TAG, "fromJson: Parsing error with", e);
        }
        return parsedThing;
    }

    /**
     * Writes a Json String containing the input Object
     * @param inputModelObject {@link ThingObject} to convert to a Json String
     * @return Json String encoding of the input UserObject
     */
    @Override
    public String toJson(ThingObject inputModelObject) {
        StringWriter jsonOutputStream = new StringWriter();
        String resultJson = null;
        try {
            writeJsonStreamFromThing(jsonOutputStream, inputModelObject);
            resultJson = jsonOutputStream.toString();
        }catch (IOException e){
            Log.e(TAG, "toJson: writing to json error", e);
        }
        return resultJson;
    }


    private void writeJsonStreamFromThing(Writer out, ThingObject modelObject) throws IOException {
        JsonWriter writer = new JsonWriter(out);
        //writer.setIndent("  ");
        writeThing(writer, modelObject);
        writer.close();
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

    private void writeThing(JsonWriter writer, ThingObject thing) throws IOException {
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

    private ThingObject readThingFromJsonStream(Reader jsonStream) throws IOException{
        JsonReader reader = new JsonReader(jsonStream);
        try{
            return readThing(reader);
        } finally {
            reader.close();
        }
    }

    private ThingObject readThing(JsonReader reader) throws IOException{
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

        return thingBuilder.build();
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
