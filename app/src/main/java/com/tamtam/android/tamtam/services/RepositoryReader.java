package com.tamtam.android.tamtam.services;

import android.location.Criteria;

import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Created by fcng1847 on 13/01/17.
 * following
 * https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006#.nuiz2kz30
 * guidelines
 */

/**
 * This interfaces defines a repository reader of model objects of type M
 * @param <M> type of model objects stored in this repository
 */
public interface RepositoryReader<M> {
    /**
     * main method to read objects form the repository.
     * Returns a {@link List} of M that matches the querySpecification
     * @param querySpecification
     * @return
     */
    public List<M> query(Specification querySpecification);
}
