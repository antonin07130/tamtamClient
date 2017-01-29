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

package com.tamtam.android.tamtam.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Currency;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Created by antoninpa on 09/01/17.
 * Test class for {@link ThingObject},
 * {@link com.tamtam.android.tamtam.model.PositionObject} and
 * {@link com.tamtam.android.tamtam.model.PriceObject}.
 */
@RunWith(AndroidJUnit4.class)
public class ThingObjectTest {
    private static final String TAG = "ThingObjectTest";

    private static final Bitmap testBitmap1 =
            BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                    com.tamtam.android.tamtam.test.R.drawable.test_png_1);

    ThingObject testThingObject;
    PositionObject positionObject;

    @Before
    @Test
    public void constructor() throws Exception {

        Log.d(TAG, "constructor: " + testBitmap1);

        positionObject = new PositionObject(10.,15.);

        testThingObject = new ThingObject.ThingBuilder()
                .thingId("thingId1")
                .pict(new ThingPicture("pictureId1", testBitmap1))
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
        assertEquals("thingId1", testThingObject.getThingId());

    }

    @Test
    public void getPict() throws Exception {
        assertEquals(new ThingPicture("pictureId1", testBitmap1), testThingObject.getPict());

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