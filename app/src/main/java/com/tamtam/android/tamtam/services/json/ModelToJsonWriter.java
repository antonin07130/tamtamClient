package com.tamtam.android.tamtam.services.json;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by antoninpa on 11/01/17.
 * This interface defines a converter able to convert model objects to Json objects
 * @param <M> Model type
 * @param <J> Json type
 */
public interface ModelToJsonWriter<M, J> {

    /**
     * This function must be implemented in subclasses :
     * Converts the inputModelObject to a Json representation of the object.
     * @param inputModelObject Model object to convert to Json.
     * @return Json object of type J
     */
    J toJson(M inputModelObject) throws ModelToJsonWriterException;

    /**
     * This function must be implemented in subclasses :
     * Converts the inputModelObjects to a Json Array representation of the model objects.
     * @param inputModelObjects {@link Iterable} of Model objects to convert to a Json Array
     * @return Json array object of type J
     */
    J toJsonArray(Iterable<M> inputModelObjects) throws ModelToJsonWriterException;
}
