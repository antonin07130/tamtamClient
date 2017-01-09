package com.tamtam.android.tamtam;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by pascalbodin on 17/09/16.
 * An object used to represnet a Thing in Json.
 */
public class ThingJson {

    private final static String LOG_TAG = "ThingJson";

    private JSONObject mJsonObject;

    /**
     * This constructor takes all mandatory fields to create a Thing object :
     * @param thingId unique identifier of this object,
     * @param pict a string representation of the picture of the object
     * @param desc a small text description of the object
     * @param currency the currency code identifying the object
     * @param lon longitude of the position where the object can be sold
     * @param lat latitude of the position where the object can be sold
     * @param locationType (@warning : deprecated)
     * @param price the amount of currency needed to buy this object
     * @param stuck is this object following the seller (@warning : deprecated)
     */
    public ThingJson(String thingId, String pict, String desc, int currency, float price,
                     double lon, double lat, String locationType, boolean stuck) {

        mJsonObject = new JSONObject();

        JSONObject priceJson, locationJson;

        priceJson = new JSONObject();
        locationJson = new JSONObject();
        try {
            priceJson.put("currency", currency);
            priceJson.put("price", price);
            locationJson.put("lon", lon);
            locationJson.put("lat", lat);
            //locationJson.put("locType", locationType);
            mJsonObject.put("thingId", thingId);
            mJsonObject.put("pict", pict);
            mJsonObject.put("description", desc);
            mJsonObject.put("price", priceJson);
            mJsonObject.put("position", locationJson);
            mJsonObject.put("stuck", stuck);
        } catch (JSONException e) {
            //AppLog.d(LOG_TAG, e.getMessage());
            Log.d(LOG_TAG, "ThingJson: " + e.getMessage());
            mJsonObject = null;
        }

    }

    /**
     * Get a @{@link JSONObject} representing this Thing.
     * TODO : Isn't it leaking a reference to an internal object ?
     */
    public JSONObject getJson() {

        return mJsonObject;

    }
}
