package com.tamtam.android.tamtam.services;

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.JsonThingConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by fcng1847 on 11/01/17.
 */
public class JsonThingConverterTest {

    private static String jsonThingString1 = "{\"thingId\":\"thing1\"," +
            "\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\"," +
            "\"description\":\"cest un premier truc\"," +
            "\"price\":{\"currency\":489,\"price\":60}," +
            "\"position\":{\"lon\":7.05289,\"lat\":43.6166}," +
            "\"stuck\":false}";

    private ThingObject.PositionObject positionObject1 =
            new ThingObject.PositionObject(7.05289, 43.6166);
    private ThingObject.PriceObject priceObject1 =
            new ThingObject.PriceObject(489, 60.);

    private ThingObject thingObject1 = new ThingObject.ThingBuilder()
            .thingId("thing1")
            .pict("AAaaaIaAMaBASEa64aENCODEDaaaag==")
            .description("cest un premier truc")
            .position(positionObject1)
            .price(priceObject1)
            .stuck(false)
            .build();


    private JsonThingConverter jsonThingConverter;

    @Before
    public void constructor() throws Exception {
        jsonThingConverter = new JsonThingConverter();
    }

    @Test
    public void fromJson() throws Exception {
        ThingObject thing1 = jsonThingConverter.fromJson(jsonThingString1);

        assertEquals("thing1", thing1.getThingId());
        assertEquals(thingObject1, thing1);
    }

    @Test
    public void toJson() throws Exception {
        String resultJson = jsonThingConverter.toJson(thingObject1);

        assertTrue(ParsableJson(resultJson));
        assertTrue(resultJson.contains(JsonThingConverter.ID_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.CURRENCY_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.DESCRIPTION_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.LATITUDE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.LONGITUDE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.PICTURE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.PRICE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.POSITION_OBJ_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.PRICE_OBJ_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.STUCK_KEYNAME));

    }

    @Test
    public void fomJsonToJson() throws Exception {
        String resultJson = jsonThingConverter.toJson(
                jsonThingConverter.fromJson(jsonThingString1));

        assertTrue(ParsableJson(resultJson));
        assertTrue(resultJson.contains(JsonThingConverter.ID_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.CURRENCY_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.DESCRIPTION_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.LATITUDE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.LONGITUDE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.PICTURE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.PRICE_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.POSITION_OBJ_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.PRICE_OBJ_KEYNAME));
        assertTrue(resultJson.contains(JsonThingConverter.STUCK_KEYNAME));

    }

    @Test
    public void toJsonFromJson() throws Exception {
        ThingObject generatedThing = jsonThingConverter.fromJson(
                jsonThingConverter.toJson(thingObject1));

        assertEquals(thingObject1, generatedThing);
    }

    /***
     * ugly utility function to verify that a Json String is parsable
     * @param test
     * @return true if parsable by {@link JSONObject} or {@link JSONArray}, false otherwise
     */
    boolean ParsableJson(String test) {
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