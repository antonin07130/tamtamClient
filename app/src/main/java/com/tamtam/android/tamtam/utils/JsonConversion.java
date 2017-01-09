package com.tamtam.android.tamtam.utils;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;
import com.tamtam.android.tamtam.model.*;


/**
 * Utility methods to convert models (things, users) to and from json
 */
public final class JsonConversion {
    /**
     * Fake static class (static classes do not exist in Java) :
     * constructor is private to prevent instantiating.
    */
    private JsonConversion(){}

    /**
     * construct a new {@link JSONObject} using values from {@link Thing} object
     * @param thing Thing to use to create the {@link JSONObject}
     * @return a new {@link JSONObject} corresponding to the schema provided in the online documentation
     * @see <a href="https://gitlab.systev.com/pascalb/PrimeZone2016/wikis/serverAPI#json-representation-1>https://gitlab.systev.com/</a>
     */
    public static JSONObject thingToJsonObject(Thing thing){
        JSONObject thingJsonObj = new JSONObject();
        JSONObject locationJson = positionToJsonObject(thing.getPosition());
        JSONObject priceJson = locationToJsonObject(thing.getPrice());
        try {
            //locationJson.put("locType", locationType);
            thingJsonObj.put("thingId", thing.getThingId());
            thingJsonObj.put("pict", thing.getPict());
            thingJsonObj.put("description", thing.getDescription());
            thingJsonObj.put("price", priceJson);
            thingJsonObj.put("position", locationJson);
            thingJsonObj.put("stuck", thing.getStuck());
        } catch (JSONException e) {
            //AppLog.d(LOG_TAG, e.getMessage());
            //Log.d(LOG_TAG, "ThingJson: " + e.getMessage());
            thingJsonObj = null;
        }
        return thingJsonObj;
    }

    public static JSONObject positionToJsonObject(Thing.Position position){
        JSONObject locationJson = new JSONObject();
        try {
            locationJson.put("lon", position.getLon());
            locationJson.put("lat", position.getLat());
        } catch (JSONException e) {
            locationJson = null;
        }
        return locationJson;
    }

    public static JSONObject locationToJsonObject(Thing.Price price){
        JSONObject priceJson = new JSONObject();
        try {
            priceJson.put("currency", price.getCurrency());
            priceJson.put("price", price.getPrice());
        } catch (JSONException e) {
            priceJson = null;
        }
        return priceJson;
    }


    public static thingFromJsonObject(JsonReader)
}
