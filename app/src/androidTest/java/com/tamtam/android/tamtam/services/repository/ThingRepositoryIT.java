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

import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.ThingObject;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;


/**
 * Created by antoninpa on 28/01/17.
 */

/**
 * Runs interface tests on following implementations of IThingRepository :
 *  - {@link ThingRepositoryMemoryCache}
 *  - ...
 *  to verify that they conform to the base API.
 *
 *  More specific tests (testing responses delays etcc...) can be found in
 *  - {@link ThingRepositoryMemoryCacheIT}
 *  - ...
 *
 *  implementation tips from :
 *  <a href="https://moepad.wordpress.com/tutorials/testing-multiple-interface-implementations-w-junit-4/">
 *      moepad tutorials
 *  </a>, thanks to them !
 *
 */
@RunWith(Parameterized.class)
public class ThingRepositoryIT {


    private IThingRepository mThingRepository;


    // will generate implementations of IThingRepository
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                // this is where other implementations of IThingRepository are added.
                { new ThingRepositoryMemoryCache() } //, { new ThingRepositoryServer() }
        });
    }

    // test class constructor : takes the getParameters value as argument
    public ThingRepositoryIT(IThingRepository thingRepository){
        this.mThingRepository = thingRepository;
    }

    // would be better to use getContext and have a test context with different folders...
    File cacheDir = getInstrumentation().getTargetContext().getCacheDir();

    ThingObject thingObject1 = RepositoryTestData.thingObject1;

    ThingObject thingObject2 = RepositoryTestData.thingObject2;

    ThingObject thingObject3 = RepositoryTestData.thingObject3;

    ThingObject thingObject1bis = RepositoryTestData.thingObject1bis;
    PositionObject positionObject1 = RepositoryTestData.positionObject1;
    PositionObject positionObject1bis = RepositoryTestData.positionObject1bis;

    private List<ThingObject> thingObjects1and2 =
            Arrays.asList(thingObject1, thingObject2);

    private List<ThingObject> thingObjects2and3 =
            Arrays.asList(thingObject2, thingObject3);

    private List<ThingObject> thingObjects1and3 =
            Arrays.asList(thingObject1, thingObject3);

    private List<ThingObject> thingObjects123 =
            Arrays.asList(thingObject1, thingObject2, thingObject3);


    private List<String> Ids1and2 =
            Arrays.asList(thingObject1.getThingId(), thingObject2.getThingId());

    private List<String> Ids2and3 =
            Arrays.asList(thingObject2.getThingId(), thingObject3.getThingId());


    @Test
    public void constructor() throws Exception {
        //File cacheDir = getInstrumentation().getContext().getCacheDir();
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();
    }


    @Test
    public void getByIds() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        memCache.add(thingObject1);
        memCache.add(thingObject2);
        memCache.add(thingObject3);

        Collection<ThingObject> fromRepo = memCache.getByIds(Ids2and3);
        HashSet<ThingObject> fromRepoSet = new HashSet<>(fromRepo);
        // we use HashSets because order does not matter
        assertEquals(new HashSet<>(thingObjects2and3), fromRepoSet);

    }

    @Test
    public void getById() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        memCache.add(thingObject1);
        memCache.add(thingObject2);
        memCache.add(thingObject3);

        ThingObject fromRepo = memCache.getById(thingObject2.getThingId());
        assertEquals(thingObject2,fromRepo);

        fromRepo = memCache.getById(thingObject3.getThingId());
        assertEquals(thingObject3,fromRepo);
    }

    @Test
    public void add() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        memCache.add(thingObject1);

        Collection<ThingObject> fromRepo = memCache.getAll();
        assertEquals(new HashSet<ThingObject>(Arrays.asList(thingObject1)),
                     new HashSet<ThingObject>(fromRepo));


        memCache.add(thingObject2);

        fromRepo = memCache.getAll();
        assertEquals(new HashSet<ThingObject>(thingObjects1and2),
                     new HashSet<ThingObject>(fromRepo));


        memCache.add(thingObject3);

        fromRepo = memCache.getAll();
        assertEquals(new HashSet<ThingObject>(thingObjects123),
                     new HashSet<ThingObject>(fromRepo));

    }

    @Test
    public void add1() throws Exception {

    }

    @Test
    public void update() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        memCache.add(thingObject1);
        memCache.add(thingObject2);
        memCache.add(thingObject3);

        assertEquals(thingObject1.getThingId(), thingObject1bis.getThingId());
        assertEquals( positionObject1, memCache.getById(thingObject1.getThingId()).getPosition());


        memCache.update(thingObject1bis);

        assertEquals( positionObject1bis,
                      memCache.getById(thingObject1bis.getThingId()).getPosition());

        // no change on other items
        assertEquals( thingObject2.getPosition(),
                      memCache.getById(thingObject2.getThingId()).getPosition());
        assertEquals( thingObject1.getPrice(),
                memCache.getById(thingObject1.getThingId()).getPrice());
    }

    @Test
    public void remove() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        memCache.add(thingObject1);
        memCache.add(thingObject2);
        memCache.add(thingObject3);


        memCache.remove(thingObject2);

        Collection<ThingObject> fromRepo = memCache.getAll();
        assertEquals(new HashSet<>(thingObjects1and3),
                     new HashSet<>(fromRepo));


        memCache.remove(thingObject3);

        fromRepo = memCache.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject1)),
                     new HashSet<>(fromRepo));

    }

    // used as a test cleanup
    @After
    @Test
    public void removeAll() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        // cleanup after test
        memCache.removeAll();
        //Collection<ThingObject> emptyList = new ArrayList<>();
        assertEquals(0, memCache.getAll().size());
    }

    @Test
    public void removeById() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        memCache.add(thingObject1);
        memCache.add(thingObject2);
        memCache.add(thingObject3);

        memCache.removeById(thingObject2.getThingId());

        Collection<ThingObject> fromRepo = memCache.getAll();
        assertEquals(new HashSet<>(thingObjects1and3),
                new HashSet<>(fromRepo));


        memCache.removeById(thingObject3.getThingId());

        fromRepo = memCache.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject1)),
                new HashSet<>(fromRepo));

    }

    @Test
    public void removeByIds() throws Exception {
        ThingRepositoryMemoryCache memCache = new ThingRepositoryMemoryCache();

        memCache.add(thingObject1);
        memCache.add(thingObject2);
        memCache.add(thingObject3);

        memCache.removeByIds(Ids1and2);

        Collection<ThingObject> fromRepo = memCache.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject3)),
                new HashSet<>(fromRepo));


        memCache.removeByIds(Ids2and3);
        fromRepo = memCache.getAll();
        assertEquals(new HashSet<ThingObject>(), // Empty
                     new HashSet<>(fromRepo));

    }

}