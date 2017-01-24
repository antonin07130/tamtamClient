package com.tamtam.android.tamtam.model;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Currency;

import static org.junit.Assert.*;

/**
 * Created by antoninpa on 09/01/17.
 * Test class for {@link ThingObject},
 * {@link com.tamtam.android.tamtam.model.PositionObject} and
 * {@link com.tamtam.android.tamtam.model.PriceObject}.
 */
@RunWith(AndroidJUnit4.class)
public class ThingObjectTest {

    private static String LOG_TAG = "ThingJSONObjectTest";

    ThingObject testThingObject;
    PositionObject positionObject;

    @Before
    @Test
    public void constructor() throws Exception {

        positionObject = new PositionObject(10.,15.);

        testThingObject = new ThingObject.ThingBuilder()
                .thingId("thingId0")
                .pict("base64encodedPicture")
                .description("test description")
                .position(positionObject)
                .price(new PriceObject(Currency.getInstance("EUR"), 23.5))
                .stuck(true)
                .build();

    }

    @Test
    public void getPosition() throws Exception {
        // check that we return the same position object that we created in constructor()
        assertSame(positionObject, testThingObject.getPosition());
        assertSame(PositionObject.class, testThingObject.getPosition().getClass());

        assertEquals(testThingObject.getPosition().getLatitude(), 15., 0.000005); //meter precision;
        assertEquals(testThingObject.getPosition().getLongitude(), 10., 0.000005); //meter precision;
    }

    @Test
    public void getPrice() throws Exception {
        assertSame(PriceObject.class, testThingObject.getPrice().getClass());
        assertEquals(testThingObject.getPrice().getCurrency(), Currency.getInstance("EUR"));
        assertEquals(testThingObject.getPrice().getValue(), 23.5, 0.00001); //storing prices as double : bad idea anto..
    }

    @Test
    public void getThingId() throws Exception {
        assertEquals("thingId0", testThingObject.getThingId());

    }

    @Test
    public void getPict() throws Exception {
        assertEquals("base64encodedPicture", testThingObject.getPict());

    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("test description", testThingObject.getDescription());
    }

    @Test
    public void getStuck() throws Exception {
        assertEquals(true, testThingObject.getStuck());

    }

}