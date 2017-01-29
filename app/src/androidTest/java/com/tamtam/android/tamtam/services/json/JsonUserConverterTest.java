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
import com.tamtam.android.tamtam.services.json.JsonUserConverter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


/**
 * Created by fcng1847 on 11/01/17.
 */
public class JsonUserConverterTest {

    //public String jsonThingString1 = "{\"thingId\":\"Thing1\",\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\",\"description\":\"cest un premier truc\",\"price\":{\"currency\":489,\"price\":60},\"position\":{\"lon\":7.05289,\"lat\":43.6166},\"stuck\":false}";

    private String jsonUserString1 = "{\"userId\":\"user1\"," +
            "\"sellingThings\":[\"thing3\",\"thing4\"]," +
            "\"interestedIn\":[\"thing1\",\"thing2\"]" +
            "}";
    private String jsonUserString2 = "{\"userId\":\"user2\"," +
            "\"sellingThings\":[\"thing23\",\"thing24\"]," +
            "\"interestedIn\":[\"thing21\",\"thing22\"]" +
            "}";
    private String jsonUserString3 = "{\"userId\":\"user3\"," +
            "\"sellingThings\":[\"thing33\",\"thing34\"]," +
            "\"interestedIn\":[\"thing31\",\"thing32\"]" +
            "}";
    private UserObject userObject1 = new UserObject(
            "user1",
            Arrays.asList("thing1", "thing2"),
            Arrays.asList("thing3", "thing4"));
    private UserObject userObject2 = new UserObject(
            "user2",
            Arrays.asList("thing21", "thing22"),
            Arrays.asList("thing23", "thing24"));
    private UserObject userObject3 = new UserObject(
            "user3",
            Arrays.asList("thing31", "thing32"),
            Arrays.asList("thing33", "thing34"));




    private String jsonUsersArray = "[" + jsonUserString1 + "," +
                                                 jsonUserString2 + "," +
                                                 jsonUserString3 + "]" ;

    private List<UserObject> userObjects =
            Arrays.asList(userObject1, userObject2, userObject3);

    private JsonUserConverter jsonUserConverter;


    @Before
    public void constructor() throws Exception {
        jsonUserConverter = new JsonUserConverter();
    }

    @Test
    public void fromJson() throws Exception {
        UserObject user1 = jsonUserConverter.fromJson(jsonUserString1);

        assertEquals("user1", user1.getUserId());

        // we convert Collections to hashsets because order does not matter
        assertEquals(new HashSet<>(Arrays.asList("thing1", "thing2")),
                     new HashSet<>(user1.getInterestedIn()));

        assertEquals(new HashSet<>(Arrays.asList("thing3", "thing4")),
                     new HashSet<>(user1.getSellingThings()));
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


    @Test
    public void fromJsonArray() throws Exception{
        List<UserObject> generatedThingsList = jsonUserConverter.fromJsonArray(jsonUsersArray);
        assertEquals(userObjects, generatedThingsList);
    }

    @Test
    public void fromEmptyJsonArray() throws Exception{
        List<UserObject> emptyList = new ArrayList<>();
        List<UserObject> generatedUsersList = jsonUserConverter.fromJsonArray("[]");
        assertEquals(emptyList, generatedUsersList);
    }

    @Test
    public void fromEmptyJson() throws Exception{
        assertNull(jsonUserConverter.fromJson("{}"));
    }

    @Test
    public void fromMalformedJsonArray() throws Exception{
        try {
            jsonUserConverter.fromJsonArray("[%*sdcccq]");
            fail("Json reading exception should have been thrown");
        }
        catch(MappingException e){
            assert(e.getMessage().contains("Reading json array"));
        }
    }


    @Test
    public void fromMalformedJson() throws Exception{
        try {
            jsonUserConverter.fromJson("{toto%*sd:cccq}");
            fail("Json reading exception should have been thrown");
        }
        catch(MappingException e){
            assert(e.getMessage().contains("Reading json"));
        }
    }



    /***
     * ugly utility function to verify that a Json String is parsable
     * @param test json String to verify.
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