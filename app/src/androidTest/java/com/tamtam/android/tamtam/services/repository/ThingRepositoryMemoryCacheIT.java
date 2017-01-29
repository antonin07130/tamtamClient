package com.tamtam.android.tamtam.services.repository;

import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.ThingObject;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;



/**
 * Created by antoninpa on 28/01/17.
 */
public class ThingRepositoryMemoryCacheIT {


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
        // we use Hashsets because order does not matter
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