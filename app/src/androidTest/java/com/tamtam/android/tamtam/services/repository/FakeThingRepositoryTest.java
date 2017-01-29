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

package com.tamtam.android.tamtam.services.repository;

import com.tamtam.android.tamtam.JsonThingTestData;
import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.json.JsonThingConverter;
import com.tamtam.android.tamtam.services.json.MappingException;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by antoninpa on 14/01/17.
 */
public class FakeThingRepositoryTest {

    FakeThingRepository fakeThingRepo;
    JsonThingConverter thingConverter = new JsonThingConverter(); // todo should be mocked ?

    ThingObject thing1 = thingConverter.fromJson(JsonThingTestData.JSON_THING_STRING_1);
    ThingObject thing2 = thingConverter.fromJson(JsonThingTestData.JSON_THING_STRING_2);
    ThingObject thing3 = thingConverter.fromJson(JsonThingTestData.JSON_THING_STRING_3);

    public FakeThingRepositoryTest() throws MappingException {
    }


    @Test
    public synchronized void constructor() throws Exception {
        FakeThingRepository fakeThingRepo = new FakeThingRepository(thingConverter);

    }

    @Test
    public synchronized void query() throws Exception {
        FakeThingRepository fakeThingRepo = new FakeThingRepository(thingConverter);
        fakeThingRepo.add(thing1);
        fakeThingRepo.add(thing2);
        fakeThingRepo.add(thing3);

        assertTrue("TODO : implement this test",false);
    }

    @Test
    public synchronized void addSeveral() throws Exception {
        FakeThingRepository fakeThingRepo = new FakeThingRepository(thingConverter);
        HashSet<ThingObject> thingObjects = new HashSet<>(Arrays.asList(thing1,thing2,thing3));
        fakeThingRepo.add(thingObjects);

        assertEquals(thingObjects, new HashSet<>(fakeThingRepo.getAll()));
    }

    @Test
    public synchronized void addOne() throws Exception {
        FakeThingRepository fakeThingRepo = new FakeThingRepository(thingConverter);
        fakeThingRepo.add(thing1);

        assertEquals(thing1, fakeThingRepo.queryOne(thing1));
    }

    @Test
    public synchronized void update() throws Exception {
        FakeThingRepository fakeThingRepo = new FakeThingRepository(thingConverter);
        fakeThingRepo.add(thing1);
        fakeThingRepo.add(thing2);

        // create a modified version of thing1
        String newDescription = "Some new description of this object";
        ThingObject thing1bis = new ThingObject.ThingBuilder()
                .thingId(thing1.getThingId())
                .description(newDescription)
                .position(thing1.getPosition())
                .price(thing1.getPrice())
                .pict(null)
                .build();

        fakeThingRepo.update(thing1bis);

        assertEquals(newDescription, fakeThingRepo.queryOne(thing1bis).getDescription());
        assertEquals(thing1.getThingId(), fakeThingRepo.queryOne(thing1bis).getThingId());
    }

    @Test
    public synchronized void removeSeveral() throws Exception {
        FakeThingRepository fakeThingRepo = new FakeThingRepository(thingConverter);
        fakeThingRepo.add(thing1);
        fakeThingRepo.add(thing2);
        fakeThingRepo.add(thing3);
        assertTrue("TODO : implement this test",false);
    }

    @Test
    public synchronized void remove1() throws Exception {
        FakeThingRepository fakeThingRepo = new FakeThingRepository(thingConverter);
        fakeThingRepo.add(thing1);
        fakeThingRepo.add(thing2);
        fakeThingRepo.add(thing3);

        fakeThingRepo.remove(thing2);

        assertEquals(2, fakeThingRepo.getAll().size());
        assertNull(fakeThingRepo.queryOne(thing2));
    }

}