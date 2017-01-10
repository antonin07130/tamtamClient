package com.tamtam.android.tamtam.services;

import com.tamtam.android.tamtam.model.UserObject;


import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Created by fcng1847 on 11/01/17.
 */
public class JsonUserConverterTest {

    //public String jsonThingString1 = "{\"thingId\":\"Thing1\",\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\",\"description\":\"cest un premier truc\",\"price\":{\"currency\":489,\"price\":60},\"position\":{\"lon\":7.05289,\"lat\":43.6166},\"stuck\":false}";

    private static String jsonUserString1 = "{\"userId\":\"user1\"," +
            "\"sellingThings\":[\"thing3\",\"thing4\"]," +
            "\"interestedIn\":[\"thing1\",\"thing2\"]" +
            "}";
    private static UserObject userObject1 = new UserObject(
            "user1",
            new HashSet<String>(Arrays.asList("thing1", "thing2")),
            new HashSet<String>(Arrays.asList("thing3", "thing4")));
    private JsonUserConverter jsonUserConverter;

    @Before
    public void constructor() throws Exception {
        jsonUserConverter = new JsonUserConverter();
    }

    @Test
    public void fromJson() throws Exception {
        UserObject user1 = jsonUserConverter.fromJson(jsonUserString1);

        assertEquals("user1", user1.getUserId());

        assertEquals(new HashSet<String>(Arrays.asList("thing1", "thing2")),
                     user1.getInterestedIn());

        assertEquals(new HashSet<String>(Arrays.asList("thing3", "thing4")),
                     user1.getSellingThings());
    }

    @Test
    public void toJson() throws Exception {
        String json1 = jsonUserConverter.toJson(userObject1);

        assertEquals(jsonUserString1,json1);
    }

    @Test
    public void fomJsonToJson() throws Exception {
        String parsedReconstructedJson = jsonUserConverter.toJson(
                jsonUserConverter.fromJson(jsonUserString1));

        assertTrue(ParsableJson(parsedReconstructedJson));
        assertTrue(parsedReconstructedJson.contains(JsonUserConverter.ID_KEYNAME));
        assertTrue(parsedReconstructedJson.contains(JsonUserConverter.INTERESTEDLIST_KEYNAME));
        assertTrue(parsedReconstructedJson.contains(JsonUserConverter.SELLINGLIST_KEYNAME));
    }

    @Test
    public void toJsonFromJson() throws Exception {
        UserObject generatedUser = jsonUserConverter.fromJson(
                jsonUserConverter.toJson(userObject1));

        assertEquals(userObject1, generatedUser);
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