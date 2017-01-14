package com.tamtam.android.tamtam.services.json;

import java.util.List;

/**
 * Created by antoninpa on 11/01/17.
 * This interface defines a converter able to read Json objects and convert to model objects.
 * @param <M> Model type
 * @param <J> Json type
 */
public interface JsonToModelReader<J, M> {
    /**
     * This function must be implemented in sublclasses :
     * Converts the inputJsonObject of type J to Model Object of type M.
     * @param inputJsonObject the Json object to convert to a single Model Object
     * @return instance of the model object or null
     */
    M fromJson(J inputJsonObject);

    /**
     * This function must be implemented in sublclasses :
     * Converts the inputJsonObject of type J to a {@link List} of Model Objects of type M.
     * @param inputJsonArrayObject the Json array object to convert to a List of Model Objects
     * @return a list containing parsed model objects or null
     */
    List<M> fromJsonArray(J inputJsonArrayObject);
}
