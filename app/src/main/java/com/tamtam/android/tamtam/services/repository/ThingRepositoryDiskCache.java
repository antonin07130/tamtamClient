package com.tamtam.android.tamtam.services.repository;

import com.tamtam.android.tamtam.model.ThingObject;

import java.io.File;
import java.util.List;

/**
 * Created by antoninpa on 28/01/17.
 */

public class ThingRepositoryDiskCache implements ThingRepository {

    private File mDiskCachePath = null;

    public ThingRepositoryDiskCache(File diskCachePath){
        if (diskCachePath.canRead() && diskCachePath.canWrite()) {
            mDiskCachePath = diskCachePath;
        }
        else {
            throw new IllegalArgumentException(
                    "Cache Repository can't read AND write to path :" + diskCachePath);
        }
    }


    @Override
    public List<ThingObject> getByIds(Iterable<String> ids) {
        return null;
    }

    @Override
    public ThingObject getById(String id) {

        return null;
    }

    @Override
    public List<ThingObject> getAll() {
        return null;
    }

    @Override
    public void add(ThingObject item) {

    }

    @Override
    public void add(Iterable<ThingObject> items) {

    }

    @Override
    public void update(ThingObject item) {

    }

    @Override
    public void remove(ThingObject item) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void removeById(String id) {

    }

    @Override
    public void removeByIds(Iterable<String> ids) {

    }
}
