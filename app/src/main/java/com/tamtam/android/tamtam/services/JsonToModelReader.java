package com.tamtam.android.tamtam.services;

/**
 * Created by antoninpa on 11/01/17.
 * This interface defines a converter able to read Json objects and convert to model objects.
 * @param <M> Model type
 * @param <J> Json type
 */
public interface JsonToModelReader<J, M> {
    /**
     * This function must be implemented in sublclasses :
     * Converts the inputJsonObject of type J to a M ModelObject
     * @param inputJsonObject
     * @return instance of the model object or null
     */
    M fromJson(J inputJsonObject);
}
