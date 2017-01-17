package com.tamtam.android.tamtam.services.repository;

import java.util.List;

/**
 * Created by antoninpa on 13/01/17.
 * following https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006#.nuiz2kz30
 * guidelines
 */

public interface Repository<M> {

    /**
     * Get the items matching the querySpecification from the repository
     * @param querySpecification {@link Specification} to filter items from the repository
     * @return a {@link List} of items of type M that match the querySpecification
     */
    public List<M> query(Specification querySpecification);

    /**
     * Add one item to the repository.
     * @param item item to add.
     */
    void add(M item);

    /**
     * Add several items to the repository.
     * @param items {@link Iterable} collection of items.
     */
    void add(Iterable<M> items);

    /**
     * Update the corresponding item in the repository.
     * @param item updated version of the item.
     */
    void update(M item);

    /**
     * remove the corresponding item from the repository.
     * @param item item to remove.
     */
    void remove(M item);

    /**
     * remove items matching the querySpecification from the repository.
     * @param querySpecification specification to select repository items to delete.
     */
    void remove(Specification querySpecification);
}
