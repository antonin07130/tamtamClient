package com.tamtam.android.tamtam.model;

/**
 * This class represents a thing which are real life objects exchanged in tamtam
 */
public class Thing {
    private String mThingId;
    private String mPict;
    private String mDescription;
    private Price mPrice;
    private Position mPosition;
    boolean mStuck;

    /**
     * This class represents a price and its currency.
     * todo : use android currency ? java.util.Currency;
     **/
    public static class Price{
        int mCurrency;
        float mPrice;
        /**
         * create e a new price bean object
         * @param currency : iso4217 code for this currency
         * @param price : price in the defined currency
         */
        public Price(int currency, float price){
            mCurrency = currency;
            mPrice = price;
        }
        public int getCurrency(){return mCurrency;}
        public float getPrice(){return mPrice;}
    }


    /**
     * this class represents a position
     */
    public static class Position{
        private double mLon, mLat;
        /**
         * to prepare GeoJson std, the order of positions should be lon, lat, [altitude]
         * @param lon longitude value
         * @param lat latitude value
         */
        Position(double lon, double lat){
            mLon = lon;
            mLat = lat;
        }
        public double getLon(){return mLon;}
        public double getLat(){return mLat;}
    }


    /**
     *
     * @param thingId unique id of this thing composed by
     * @param pict the picture encoded in base64 stored in a String
     * @param description a text description of the object completed by the seller
     * @param price the price of the thing TODO : pass reference value or copy ?
     * @param position the current known position of the thing TODO : pass reference value or copy ?
     * @param stuck true iff the thing is moving with the seller
     */
    public Thing(String thingId, String pict, String description, Price price, Position position, boolean stuck){
        mThingId = thingId;
        mPict = pict;
        mDescription = description;
        mPrice = price;
        mPosition = position;
        mStuck = stuck;
    }

    public Position getPosition(){return mPosition;}
    public Price getPrice(){return  mPrice;}
    public String getThingId(){return mThingId;}
    public String getPict(){return mPict;}
    public String getDescription(){return mDescription;}
    public boolean getStuck(){return mStuck;}

}