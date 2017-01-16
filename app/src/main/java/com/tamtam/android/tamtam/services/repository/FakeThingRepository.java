package com.tamtam.android.tamtam.services.repository;

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.json.JsonToModelReader;
import com.tamtam.android.tamtam.services.json.JsonToModelReaderException;
import com.tamtam.android.tamtam.services.json.ModelToJsonWriter;
import com.tamtam.android.tamtam.services.json.ModelToJsonWriterException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by antoninpa on 13/01/17.
 */

public class FakeThingRepository implements Repository<ThingObject> {

    // conversion services (to/from json string) like in the real implementation.
    final JsonToModelReader<String, ThingObject> jsonToModel;
    final ModelToJsonWriter<ThingObject, String> modelToJson;

    // static in memory collection to simulate a database (may be ugly)
    // it stores json Strings to look like real network request results
    final static ConcurrentHashMap<String, String> inMemoryJsonThings = new ConcurrentHashMap<>();

    //constant strings
    public static final String JSON_THING_STRING_1 = "{\"thingId\":\"thing1\"," +
            "\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\"," +
            "\"description\":\"cest un premier truc\"," +
            "\"price\":{\"currency\":\"USD\",\"price\":10.10}," +
            "\"position\":{\"lon\":7.05289,\"lat\":43.6166}," +
            "\"stuck\":false}";
    public static final String JSON_THING_STRING_2 = "{\"thingId\":\"thing2\"," +
            "\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\"," +
            "\"description\":\"cest un deuxieme truc\"," +
            "\"price\":{\"currency\":\"EUR\",\"price\":20.20}," +
            "\"position\":{\"lon\":7.05334,\"lat\":43.61664}," +
            "\"stuck\":false}";
    public static final String JSON_THING_STRING_3 = "{\"thingId\":\"thing3\"," +
            "\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\"," +
            "\"description\":\"cest un troisieme truc\"," +
            "\"price\":{\"currency\":\"INR\",\"price\":30.30}," +
            "\"position\":{\"lon\":7.12153,\"lat\":43.65839}," +
            "\"stuck\":false}";


    public FakeThingRepository(JsonToModelReader<String, ThingObject> jsonToModel,
                               ModelToJsonWriter<ThingObject, String> modelToJson) {
        this.jsonToModel = jsonToModel;
        this.modelToJson = modelToJson;
    }


    public void populateFakeThings() {
        try {
            // add 3 users to simulate a BIG database
            inMemoryJsonThings.put(jsonToModel.fromJson(JSON_THING_STRING_3).getThingId(),
                    JSON_THING_STRING_3);
            inMemoryJsonThings.put(jsonToModel.fromJson(JSON_THING_STRING_2).getThingId(),
                    JSON_THING_STRING_2);
            inMemoryJsonThings.put(jsonToModel.fromJson(JSON_THING_STRING_1).getThingId(),
                    JSON_THING_STRING_1);
        } catch (JsonToModelReaderException e) {
            e.printStackTrace();
        }
    }


    public List<ThingObject> queryAll() {
        // would have been a one liner with a Map function...
        ArrayList<ThingObject> objectList = new ArrayList<ThingObject>();
        try {
            for (String jsonValue : inMemoryJsonThings.values()) {
                objectList.add(jsonToModel.fromJson(jsonValue));
            }
        } catch (JsonToModelReaderException e) {
            e.printStackTrace();
        }

        return objectList;
    }

    // todo : replace by specification implementations
    public ThingObject queryOne(ThingObject item) {
        String jsonObject = inMemoryJsonThings.get(item.getThingId());
        ThingObject thing = null;
        if (jsonObject != null) {
            try {
                thing = jsonToModel.fromJson(jsonObject);
            } catch (JsonToModelReaderException e) {
                e.printStackTrace();
            }
        }
        return thing;
    }


    /**
     * returns all users in the repository regardless of specification argument
     *
     * @param querySpecification unused {@link Specification}
     * @return all users in this repository
     */
    @Override
    public List<ThingObject> query(Specification querySpecification) {
        // cast to specifications accepted by this repository
        // final FakeSpecification fakeSpecification = (FakeSpecification) querySpecificationl;

        return queryAll();
    }


    @Override
    public void add(ThingObject item) {
        try {
            inMemoryJsonThings.put(item.getThingId(), modelToJson.toJson(item));
        } catch (ModelToJsonWriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Iterable<ThingObject> items) {
        for (ThingObject item : items) {
            add(item);
        }
    }

    @Override
    public void update(ThingObject item) {
        remove(item);
        add(item);
    }

    @Override
    public void remove(ThingObject item) {
        inMemoryJsonThings.remove(item.getThingId());
    }

    @Override
    public void remove(Specification querySpecification) {
        inMemoryJsonThings.clear();
    }
}
