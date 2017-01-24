package com.tamtam.android.tamtam.services.repository;

import java.io.File;
import java.util.List;

/**
 * Created by antoninpa on 16/01/17.
 */


/**
 * Stores a list of pointers to Files. In our case all these files are imagefiles.
 * We have to choose android persistnece system for this repository. It has to live through reboots
 * of the app.
 */
public class ImageFileRepository implements Repository<File> {
    @Override
    public List<File> query(Specification querySpecification) {

        // could return file based on imagefilename ?
        // could return file based on path
        // could return file associated with a specific thing
        return null;
    }

    @Override
    public void add(File item) {
        // store file information to local database

        // write file in the disk

    }

    @Override
    public void add(Iterable<File> items) {
        // store file information to local database

    }

    @Override
    public void update(File item) {
        // update a file (based on ... what, it's URI ?)
    }

    @Override
    public void remove(File item) {

    }

    @Override
    public void remove(Specification querySpecification) {

    }
}
