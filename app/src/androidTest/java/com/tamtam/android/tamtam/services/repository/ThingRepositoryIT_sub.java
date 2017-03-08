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

import android.util.Log;

import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.ThingObject;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


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
public class ThingRepositoryIT_sub {
    private static String TAG = "ThingRepositoryIT";

    private IThingRepository mThingRepository;

    // will generate implementations of IThingRepository
    @Parameters
    public static Collection<Object[]> getParameters() {
        URI URIparam = null;
        try {
            URIparam = new URI("http://10.0.2.2:9000");
        }catch (URISyntaxException e){
            Log.e(TAG, "getParameters: URI definition error", e);
        }

        return Arrays.asList(new Object[][] {
                // this is where other implementations of IThingRepository are added.
                { new ThingRepositoryServer(URIparam) }
        });
    }

    // test class constructor : takes the getParameters value as argument
    public ThingRepositoryIT_sub(IThingRepository thingRepository){
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
        IThingRepository repo = mThingRepository;
    }


    @Test
    public void getByIds() throws Exception {
        IThingRepository repo = mThingRepository;

        repo.add(thingObject1);
        repo.add(thingObject2);
        repo.add(thingObject3);

        Collection<ThingObject> fromRepo = repo.getByIds(Ids2and3);
        HashSet<ThingObject> fromRepoSet = new HashSet<ThingObject>(fromRepo);
        // we use HashSets because order does not matter
        assertEquals(new HashSet<>(thingObjects2and3), fromRepoSet);

    }

    @Test
    public void getById() throws Exception {
        IThingRepository repo = mThingRepository;


        repo.add(thingObject1);
        repo.add(thingObject2);
        repo.add(thingObject3);

        ThingObject fromRepo = repo.getById(thingObject2.getThingId());
        assertEquals(thingObject2,fromRepo);

        fromRepo = repo.getById(thingObject3.getThingId());
        assertEquals(thingObject3,fromRepo);
    }

    @Test
    public void add() throws Exception {
        IThingRepository repo = mThingRepository;

        repo.add(thingObject1);

        ThingObject fromRepo = repo.getById(thingObject1.getThingId());
        assertEquals(thingObject1, fromRepo);


        repo.add(thingObject2);

        Collection<ThingObject> fromRepoCollection = repo.getByIds(
                Arrays.asList(thingObject1.getThingId(),
                              thingObject2.getThingId()));
        assertEquals(new HashSet<ThingObject>(thingObjects1and2),
                     new HashSet<ThingObject>(fromRepoCollection));


        repo.add(thingObject3);

        fromRepoCollection = repo.getByIds(
                Arrays.asList(thingObject1.getThingId(),
                              thingObject2.getThingId(),
                              thingObject3.getThingId()));
        assertEquals(new HashSet<ThingObject>(thingObjects123),
                     new HashSet<ThingObject>(fromRepoCollection));

    }
/*
    @Test
    public void add1() throws Exception {

    }

    @Test
    public void update() throws Exception {
        IThingRepository repo = mThingRepository;

        repo.add(thingObject1);
        repo.add(thingObject2);
        repo.add(thingObject3);

        assertEquals(thingObject1.getThingId(), thingObject1bis.getThingId());
        assertEquals( positionObject1, repo.getById(thingObject1.getThingId()).getPosition());


        repo.update(thingObject1bis);

        assertEquals( positionObject1bis,
                      repo.getById(thingObject1bis.getThingId()).getPosition());

        // no change on other items
        assertEquals( thingObject2.getPosition(),
                      repo.getById(thingObject2.getThingId()).getPosition());
        assertEquals( thingObject1.getPrice(),
                repo.getById(thingObject1.getThingId()).getPrice());
    }

    @Test
    public void remove() throws Exception {
        IThingRepository repo = mThingRepository;

        repo.add(thingObject1);
        repo.add(thingObject2);
        repo.add(thingObject3);


        repo.remove(thingObject2);

        Collection<ThingObject> fromRepo = repo.getAll();
        assertEquals(new HashSet<>(thingObjects1and3),
                     new HashSet<>(fromRepo));


        repo.remove(thingObject3);

        fromRepo = repo.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject1)),
                     new HashSet<>(fromRepo));

    }

    // used as a test cleanup
    @Test
    public void removeAll() throws Exception {
        IThingRepository repo = mThingRepository;

        // cleanup after test
        repo.removeAll();
        //Collection<ThingObject> emptyList = new ArrayList<>();
        assertNotNull(repo.getAll());
        assertEquals(0, repo.getAll().size());
    }

    @Test
    public void removeById() throws Exception {
        IThingRepository repo = mThingRepository;

        repo.add(thingObject1);
        repo.add(thingObject2);
        repo.add(thingObject3);

        repo.removeById(thingObject2.getThingId());

        Collection<ThingObject> fromRepo = repo.getAll();
        assertEquals(new HashSet<>(thingObjects1and3),
                new HashSet<>(fromRepo));


        repo.removeById(thingObject3.getThingId());

        fromRepo = repo.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject1)),
                new HashSet<>(fromRepo));

    }

    @Test
    public void removeByIds() throws Exception {
        IThingRepository repo = mThingRepository;

        repo.add(thingObject1);
        repo.add(thingObject2);
        repo.add(thingObject3);

        repo.removeByIds(Ids1and2);

        Collection<ThingObject> fromRepo = repo.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject3)),
                new HashSet<>(fromRepo));


        repo.removeByIds(Ids2and3);
        fromRepo = repo.getAll();
        assertEquals(new HashSet<ThingObject>(), // Empty
                     new HashSet<>(fromRepo));

    }
*/
}