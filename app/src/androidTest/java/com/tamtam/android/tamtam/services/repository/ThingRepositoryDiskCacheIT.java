package com.tamtam.android.tamtam.services.repository;

import com.tamtam.android.tamtam.JsonThingTestData;
import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.ThingObject;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;



/**
 * Created by antoninpa on 28/01/17.
 */
public class ThingRepositoryDiskCacheIT {


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
        File cacheDir = getInstrumentation().getContext().getCacheDir();
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);
    }


    @Test
    public void getByIds() throws Exception {

        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        diskCache.add(thingObject1);
        diskCache.add(thingObject2);
        diskCache.add(thingObject3);

        List<ThingObject> fromRepo = diskCache.getByIds(Ids2and3);

        // we use Hashsets because order does not matter
        assertEquals(new HashSet<>(thingObjects2and3), new HashSet<>(fromRepo));

    }

    @Test
    public void getById() throws Exception {
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        diskCache.add(thingObject1);
        diskCache.add(thingObject2);
        diskCache.add(thingObject3);

        ThingObject fromRepo = diskCache.getById(thingObject2.getThingId());
        assertEquals(thingObject2,fromRepo);

        fromRepo = diskCache.getById(thingObject3.getThingId());
        assertEquals(thingObject3,fromRepo);
    }

    @Test
    public void add() throws Exception {
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        diskCache.add(thingObject1);

        List<ThingObject> fromRepo = diskCache.getAll();
        assertEquals(new HashSet<ThingObject>(Arrays.asList(thingObject1)),
                     new HashSet<ThingObject>(fromRepo));


        diskCache.add(thingObject2);

        fromRepo = diskCache.getAll();
        assertEquals(new HashSet<ThingObject>(thingObjects1and2),
                     new HashSet<ThingObject>(fromRepo));


        diskCache.add(thingObject3);

        fromRepo = diskCache.getAll();
        assertEquals(new HashSet<ThingObject>(thingObjects123),
                     new HashSet<ThingObject>(fromRepo));

    }

    @Test
    public void add1() throws Exception {

    }

    @Test
    public void update() throws Exception {
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        diskCache.add(thingObject1);
        diskCache.add(thingObject2);
        diskCache.add(thingObject3);

        assertEquals(thingObject1.getThingId(), thingObject1bis.getThingId());
        assertEquals( positionObject1, diskCache.getById(thingObject1.getThingId()).getPosition());


        diskCache.update(thingObject1bis);

        assertEquals( positionObject1bis,
                      diskCache.getById(thingObject1bis.getThingId()).getPosition());

        // no change on other items
        assertEquals( thingObject2.getPosition(),
                      diskCache.getById(thingObject2.getThingId()).getPosition());
        assertEquals( thingObject1.getPrice(),
                diskCache.getById(thingObject1.getThingId()).getPrice());
    }

    @Test
    public void remove() throws Exception {
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        diskCache.add(thingObject1);
        diskCache.add(thingObject2);
        diskCache.add(thingObject3);


        diskCache.remove(thingObject2);

        List<ThingObject> fromRepo = diskCache.getAll();
        assertEquals(new HashSet<>(thingObjects1and3),
                     new HashSet<>(fromRepo));


        diskCache.remove(thingObject3);

        fromRepo = diskCache.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject1)),
                     new HashSet<>(fromRepo));

    }

    // used as a test cleanup
    @After
    @Test
    public void removeAll() throws Exception {
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        // cleanup after test
        diskCache.removeAll();
        List<ThingObject> emptyList = new ArrayList<>();
        assertEquals(emptyList, diskCache.getAll());
    }

    @Test
    public void removeById() throws Exception {
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        diskCache.add(thingObject1);
        diskCache.add(thingObject2);
        diskCache.add(thingObject3);

        diskCache.removeById(thingObject2.getThingId());

        List<ThingObject> fromRepo = diskCache.getAll();
        assertEquals(new HashSet<>(thingObjects1and3),
                new HashSet<>(fromRepo));


        diskCache.removeById(thingObject3.getThingId());

        fromRepo = diskCache.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject1)),
                new HashSet<>(fromRepo));

    }

    @Test
    public void removeByIds() throws Exception {
        ThingRepositoryDiskCache diskCache = new ThingRepositoryDiskCache(cacheDir);

        diskCache.add(thingObject1);
        diskCache.add(thingObject2);
        diskCache.add(thingObject3);

        diskCache.removeByIds(Ids1and2);

        List<ThingObject> fromRepo = diskCache.getAll();
        assertEquals(new HashSet<>(Arrays.asList(thingObject1)),
                new HashSet<>(fromRepo));


        diskCache.removeByIds(Ids2and3);
        fromRepo = diskCache.getAll();
        assertEquals(new HashSet<ThingObject>(), // Empty
                     new HashSet<>(fromRepo));

    }

}