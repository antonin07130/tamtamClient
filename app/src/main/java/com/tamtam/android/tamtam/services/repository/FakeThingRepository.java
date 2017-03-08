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

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.json.Mapper;
import com.tamtam.android.tamtam.services.json.MappingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by antoninpa on 13/01/17.
 */

public class FakeThingRepository implements Repository<ThingObject> {
    private static final String TAG = "FakeThingRepository";

    // conversion services (to/from json string) like in the real implementation.
    final Mapper<String, ThingObject> jsonToModel;


    // normal simultaneous access
    private static int SIMULTANEOUS_ACCESSES = 4;

    // static in memory collection to simulate a database (may be ugly)
    // it stores json Strings to look like real network request results
    public final static ConcurrentHashMap<String, String> inMemoryJsonThings =
            new ConcurrentHashMap<>(SIMULTANEOUS_ACCESSES);


    public FakeThingRepository(Mapper<String, ThingObject> jsonToModel) {
        this.jsonToModel = jsonToModel;
    }


    /**
     * returns all users in the repository regardless of specification argument
     *
     * @return all users in this repository
     */
    @Override
    public Collection<ThingObject> getAll() {
        // would have been a one liner with a Map function...
        ArrayList<ThingObject> objectList = new ArrayList<ThingObject>();
        try {
            for (String jsonValue : inMemoryJsonThings.values()) {
                objectList.add(jsonToModel.fromJson(jsonValue));
            }
        } catch (MappingException e) {
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
            } catch (MappingException e) {
                e.printStackTrace();
            }
        }
        return thing;
    }


    @Override
    public void add(ThingObject item) {
        try {
            Log.d(TAG, "add: " + item);
            inMemoryJsonThings.put(item.getThingId(), jsonToModel.toJson(item));
        } catch (MappingException e) {
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
    public void removeAll() {
        inMemoryJsonThings.clear();
    }

    @Override
    public Collection<ThingObject> getByIds(Iterable<String> ids) {
        return null;
    }

    @Override
    public ThingObject getById(String id) {
        return null;
    }

    @Override
    public void removeById(String id) {

    }

    @Override
    public void removeByIds(Iterable<String> ids) {

    }
}
