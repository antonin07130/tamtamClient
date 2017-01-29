package com.tamtam.android.tamtam.services.repository;

import android.support.v4.util.LruCache;

import com.tamtam.android.tamtam.model.ThingObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by antoninpa on 28/01/17.
 */

public class ThingRepositoryMemoryCache implements ThingRepository {

    private File mDiskCachePath = null;

    // only one memory cache for all instances
    private static LruCache<String, ThingObject> mMemoryCache = null;

    // todo : implement memory limits (and count size in terms of thingObject size
    /*
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
    */

    //todo warning : does not prevent memory overflow
    final int cacheSize = 20;

    public ThingRepositoryMemoryCache(){
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<String, ThingObject>(cacheSize);
        }

    }
    
    @Override
    public Collection<ThingObject> getByIds(Iterable<String> ids) {
        ArrayList<ThingObject> foundThingList = new ArrayList<ThingObject>();
        for (String id:ids){
            foundThingList.add(getById(id));
        }
        return foundThingList;
    }

    @Override
    public ThingObject getById(String id) {
        return mMemoryCache.get(id);
    }

    @Override
    public Collection<ThingObject> getAll()
    {
        return mMemoryCache.snapshot().values();
    }

    @Override
    public void add(ThingObject item) {
        if (item != null) {
            mMemoryCache.put(item.getThingId(), item);
        }else {
            throw new NullPointerException("item can't be null");
        }
    }

    @Override
    public void add(Iterable<ThingObject> items) {
        for (ThingObject item:items ){
            add(item);
        }
    }

    @Override
    public void update(ThingObject item) {
        mMemoryCache.put(item.getThingId(),item);
    }

    @Override
    public void remove(ThingObject item) {
        mMemoryCache.remove(item.getThingId());
    }

    @Override
    public void removeAll() {
        mMemoryCache.evictAll();
    }

    @Override
    public void removeById(String id) {
        mMemoryCache.remove(id);
    }

    @Override
    public void removeByIds(Iterable<String> ids) {
        for (String id:ids){
            removeById(id);
        }
    }
}
