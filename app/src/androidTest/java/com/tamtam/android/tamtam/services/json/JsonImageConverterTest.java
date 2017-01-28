package com.tamtam.android.tamtam.services.json;

import com.tamtam.android.tamtam.JsonThingTestData;
import com.tamtam.android.tamtam.model.ThingPicture;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tamtam.android.tamtam.JsonThingTestData.JSON_PICTURE_STRING_1;
import static com.tamtam.android.tamtam.JsonThingTestData.ParsableJson;
import static org.junit.Assert.*;

/**
 * Created by fcng1847 on 26/01/17.
 */
public class JsonImageConverterTest {

    JsonImageConverter jsonImageConverter = null;

    private final String jsonPictureArray = "[" +
            JsonThingTestData.JSON_PICTURE_STRING_1 + "," +
            JsonThingTestData.JSON_PICTURE_STRING_2 + "," +
            JsonThingTestData.JSON_PICTURE_STRING_3 + "]";

    ThingPicture thingPicture1 = new ThingPicture("pictureId1", JsonThingTestData.testBitmap1);
    ThingPicture thingPicture2 = new ThingPicture("pictureId2", JsonThingTestData.testBitmap2);
    ThingPicture thingPicture3 = new ThingPicture("pictureId3", JsonThingTestData.testBitmap3);

    private List<ThingPicture> pictureList =
            Arrays.asList(thingPicture1, thingPicture2, thingPicture3);

    @Before
    @Test
    public void Constructor() throws Exception {
        jsonImageConverter = new JsonImageConverter();
    }

    @Test
    public void fromJson() throws Exception {
        ThingPicture picture1 = jsonImageConverter.fromJson(JSON_PICTURE_STRING_1);

        assertEquals("pictureId1", picture1.getPictureId());
        assertEquals(thingPicture1, picture1);
    }

    @Test
    public void fromJsonArray() throws Exception {
        List<ThingPicture> generatedPictureList = jsonImageConverter.fromJsonArray(jsonPictureArray);
        assertEquals(pictureList, generatedPictureList);
    }

    @Test
    public void toJson() throws Exception {
        String resultJson = jsonImageConverter.toJson(thingPicture1);
        assertTrue(ParsableJson(resultJson));
        assertTrue(resultJson.contains(JsonImageConverter.PICTUREID_KEYNAME));
        assertTrue(resultJson.contains(JsonImageConverter.PICTUREENCODED_KEYNAME));
    }

    @Test
    public void toJsonArray() throws Exception {
        // this test is not complete
        String resultJson = jsonImageConverter.toJsonArray(pictureList);
        assertTrue(ParsableJson(resultJson));
        assertTrue(resultJson.contains("["));
        assertTrue(resultJson.contains("]"));
        assertTrue(resultJson.contains("pictureId1"));
        assertTrue(resultJson.contains("pictureId2"));
        assertTrue(resultJson.contains("pictureId3"));
        assertTrue(resultJson.contains(JsonImageConverter.PICTUREID_KEYNAME));
        assertTrue(resultJson.contains(JsonImageConverter.PICTUREENCODED_KEYNAME));
    }


    @Test
    public void toJsonArrayFromJsonArray() throws Exception {
        List<ThingPicture> generatedImageList =
                jsonImageConverter.fromJsonArray(
                        jsonImageConverter.toJsonArray(pictureList));
        assertEquals(pictureList, generatedImageList);
    }

    @Test
    public void fomJsonToJson() throws Exception {
        String resultJson = jsonImageConverter.toJson(
                jsonImageConverter.fromJson(JSON_PICTURE_STRING_1));
        assertTrue(ParsableJson(resultJson));
        assertTrue(resultJson.contains(JsonImageConverter.PICTUREID_KEYNAME));
        assertTrue(resultJson.contains(JsonImageConverter.PICTUREENCODED_KEYNAME));
    }


    @Test
    public void toJsonFromJson() throws Exception {
        ThingPicture generatedPicture = jsonImageConverter.fromJson(
                jsonImageConverter.toJson(thingPicture1));

        assertEquals(thingPicture1, generatedPicture);
    }

    @Test
    public void fromEmptyJson() throws Exception{
        assertNull(jsonImageConverter.fromJson("{}"));
    }

    @Test
    public void fromEmptyJsonArray() throws Exception{
        List<ThingPicture> emptyList = new ArrayList<>();
        List<ThingPicture> generatedPicturesList = jsonImageConverter.fromJsonArray("[]");
        assertEquals(emptyList, generatedPicturesList);
    }

    @Test
    public void fromMalformedJsonArray() throws Exception{
        try {
            List<ThingPicture> pictures = jsonImageConverter.fromJsonArray("[%*sdcccq]");
            fail("Should throw a MappingException when attempting to parse malformed Json");
        }
        catch(MappingException e){
            assert(e.getMessage().contains("Reading json array"));
        }
    }

    @Test
    public void fromMalformedJson() throws Exception{
        try {
            ThingPicture pictures = jsonImageConverter.fromJson("{toto%*sd:cccq}");
            fail("Should throw a MappingException when attempting to parse malformed Json");
        }
        catch(MappingException e){
            assert(e.getMessage().contains("Reading json"));
        }
    }




}