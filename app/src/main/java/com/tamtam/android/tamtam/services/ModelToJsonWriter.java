package com.tamtam.android.tamtam.services;

import org.json.JSONObject;

/**
 * Created by antoninpa on 11/01/17.
 * This interface defines a converter able to convert model objects to Json objects
 * @param <M> Model type
 * @param <J> Json type
 */
public interface ModelToJsonWriter<M, J> {

    /**
     * This function must be implemented in subclasses :
     * Converts the inputModelObject to a Json Object of type M
     * @param inputModelObject
     * @return
     */
    J toJson(M inputModelObject);
}
