package com.tamtam.android.tamtam.services.repository;

import java.util.List;

/**
 * Created by antoninpa on 13/01/17.
 * following https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006#.nuiz2kz30
 * guidelines
 */

public interface Repository<M> {

    /**
     * Get the items matching the querySpecification from the repository.
     *
     * @param ids of items to retrieve.
     * @return a {@link List} of items of type M that match the querySpecification.
     */
    public List<M> getByIds(Iterable<String> ids);


    /**
     * Returns the item if it exists in the repository, or null otherwise.
     *
     * @param id
     * @return item of type M if it exists in this repository or null.
     */
    public M getById(String id);


    /**
     * get all items from the repository.
     * @return all items of type M in a {@link List}.
     */
    public List<M> getAll();



    /**
     * Add one item to the repository.
     *
     * @param item item to add.
     */
    void add(M item);

    /**
     * Add several items to the repository.
     *
     * @param items {@link Iterable} collection of items.
     */
    void add(Iterable<M> items);

    /**
     * Update the corresponding item in the repository.
     *
     * @param item updated version of the item.
     */
    void update(M item);

    /**
     * remove the corresponding item from the repository.
     *
     * @param item item to remove.
     */
    void remove(M item);

    /**
     * remove all items from the repository.
     */
    void removeAll();

    /**
     * remove items matching the querySpecification from the repository.
     *
     * @param id id of item to delete.
     */
    void removeById(String id);

    /**
     * remove items matching the id in the {@link Iterable} ids.
     *
     * @param ids id of items to delete.
     */
    void removeByIds(Iterable<String> ids);

}
