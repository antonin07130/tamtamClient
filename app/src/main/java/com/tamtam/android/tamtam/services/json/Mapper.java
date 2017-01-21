package com.tamtam.android.tamtam.services.json;

import java.util.List;


/**
 * Created by antoninpa on 11/01/17.
 * This interface defines a converter able to read Json objects and convert to model objects.
 * @param <M> Model type
 * @param <J> Json type
 */
public interface Mapper<J, M> {
    /**
     * This function must be implemented in subclasses :
     * Converts the inputJsonObject of type J to Model Object of type M.
     * @param inputJsonObject the Json object to convert to a single Model Object.
     * @return instance of the model object or null.
     * @throws MappingException if input Json is malformed.
     */
    M fromJson(J inputJsonObject) throws MappingException;

    /**
     * This function must be implemented in subclasses :
     * Converts the inputJsonObject of type J to a {@link List} of Model Objects of type M.
     * @param inputJsonArrayObject the Json array object to convert to a List of Model Objects.
     * @return a list containing parsed model objects or an empty list.
     * @throws MappingException if input Json is malformed.
     */
    List<M> fromJsonArray(J inputJsonArrayObject) throws MappingException;


    /**
     * This function must be implemented in subclasses :
     * Converts the inputModelObject to a Json representation of the object.
     * @param inputModelObject Model object to convert to Json.
     * @return Json object of type J
     */
    J toJson(M inputModelObject) throws MappingException;

    /**
     * This function must be implemented in subclasses :
     * Converts the inputModelObjects to a Json Array representation of the model objects.
     * @param inputModelObjects {@link Iterable} of Model objects to convert to a Json Array
     * @return Json array object of type J
     */
    J toJsonArray(Iterable<M> inputModelObjects) throws MappingException;

}
