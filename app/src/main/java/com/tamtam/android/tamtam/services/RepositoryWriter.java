package com.tamtam.android.tamtam.services;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by fcng1847 on 13/01/17.
 * following https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006#.nuiz2kz30
 * guidelines
 */

public interface RepositoryWriter<M> {
    void add(M item);

    void add(Iterable<M> items);

    void update(M item);

    void remove(M item);

    void remove(Specification specification);
}
