package com.tamtam.android.tamtam.model;

import android.util.Log;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Currency;
import static android.R.attr.name;


/**
 * Created by antoninpa on 09/01/17.
 * This class represents a thing which are real life objects exchanged in tamtam
 * We store these data object directly as {@link JSONObject}.
 */
 public class ThingObject implements java.io.Serializable {


    // todo : should we keep LOG_TAG in a Model object ? should it be transient ?
    public static String LOG_TAG = "ThingObject";

    private final String thingId; // never empty
    private final String pict;
    private final String description;
    private final PriceObject price; // never null
    private final PositionObject position; // never null
    private final boolean stuck;


    /**
     * This class represents a price and its currency.
     * todo : use android currency ? java.util.Currency;
     **/
     public static class PriceObject implements java.io.Serializable {
        private final Currency currency;
        private final double price;

        /**
         * create a new price object
         * @param currency : iso4217 code for this currency
         * @param price : price in the defined currency
         */
         public PriceObject(Currency currency, double price) {
             if (currency !=null) {
                 this.currency = currency;
                 this.price = price;
             } else {
                 throw new IllegalArgumentException("currency must be an iso4217 currency");
             }
        }
         public Currency getCurrency(){ return currency; }
         public double getValue(){ return price; }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PriceObject that = (PriceObject) o;

            if (Double.compare(that.price, price) != 0) return false;
            return currency.equals(that.currency);

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = currency.hashCode();
            temp = Double.doubleToLongBits(price);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

    /**
     * This class represents a position (longitude, latitude)
     */
     public static class PositionObject implements java.io.Serializable{
        private final double longitude;
        private final double latitude;
        /**
         * to prepare GeoJson std, the order of positions should be lon, lat, [altitude]
         * @param longitude longitude value
         * @param latitude latitude value
         */
        public PositionObject(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
        public double getLongitude() { return longitude; }
        public double getLatitude(){
                return latitude;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PositionObject that = (PositionObject) o;

            if (Double.compare(that.longitude, longitude) != 0) return false;
            return Double.compare(that.latitude, latitude) == 0;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(longitude);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(latitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }


    /**
     * Use this class to build a new thing from scratch :
     * {@code new ThingObject.ThingBuilder("ID_KEYNAME")
    .pict("base64encodedPicture")
    .description("description")
    .position(positionObject)
    .price(priceObject)
    .stuck(true)
    .build(); }
     */
    public static class ThingBuilder {

        private String thingId = "";
        private String pict;
        private String description;
        private PriceObject price;
        private PositionObject position;
        private boolean stuck;

        /**
         * Thing Builder constructor, thingId and price and position methods must be
         * used, then build() to create a valid {@link ThingObject}.
         */
        public ThingBuilder(){}


        /**
         * @param thingId unique id of this thing (required)
         */
        public ThingBuilder thingId(String thingId) {
            this.thingId = thingId;
            return this;
        }

        /**
         * @param pict the picture encoded in base64 stored in a String
         */
         public ThingBuilder pict(String pict) {
            this.pict = pict;
            return this;
        }
        /**
         * @param description a text description of the object completed by the seller
         */
         public ThingBuilder description(String description) {
            this.description = description;
            return this;
        }
        /**
         * @param price the price of the thing (required)
         */
         public ThingBuilder price(PriceObject price) {
            this.price = price;
            return this;
        }
        /**
         * @param position the current known position of the thing (required)
         */
         public ThingBuilder position(PositionObject position) {
            this.position = position;
            return this;
        }
        /**
         * @param stuck true if the thing is moving with the seller
         */
         public ThingBuilder stuck(boolean stuck) {
            this.stuck = stuck;
            return this;
        }

        /**
         * Check that we can build a Thing Object with the provided arguments.
         * thingId must not be empty,
         * price must have been provided and
         * position must have been provided.
         * @return true if the builder is valid and we can build the ThingObject using build()
         */
        public boolean isValid() {
            return (!thingId.isEmpty() && price != null && position != null);
        }

        /**
         * Build a new {@link ThingObject} with information provided to {@link ThingBuilder}
         * thingId must not be empty,
         * price must have been provided and
         * position must have been provided.
         * @return a new immutable instance of {@link ThingObject}
         */
         public ThingObject build(){
             if (this.isValid() ) {
                 return new ThingObject(this);
             } else {
                 throw new IllegalArgumentException("thingId or price or position is null or empty");
             }
         }
    }

    /**
     * This constuctor should not be called directly, see {@link ThingBuilder}
     * @param builder documented {@link ThingBuilder} to be used.
     */
    private ThingObject(ThingBuilder builder) {
        // we will user the builder DP instead (too many arguments)
        this.thingId = builder.thingId;
        this.pict = builder.pict;
        this.description = builder.description;
        this.price = builder.price;
        this.position = builder.position;
        this.stuck = builder.stuck;
    }

     public boolean hasPict() {return (this.pict != null);}
     public PositionObject getPosition(){return this.position;}
     public PriceObject getPrice(){return  this.price;}
     public String getThingId(){return this.thingId;}
     public String getPict(){return this.pict;}
     public String getDescription(){return this.description;}
     public boolean getStuck(){return this.stuck;}

    @Override
    public String toString() {
        return "ThingObject{" +
                "thingId='" + thingId + '\'' +
                ", pict='" + pict + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", position=" + position +
                ", stuck=" + stuck +
                '}';
    }

    // todo : maybe enough to just verify ids ? Bad situation if ids are equals but not the rest
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThingObject that = (ThingObject) o;

        /*
        if (stuck != that.stuck) return false;
        if (!thingId.equals(that.thingId)) return false;
        if (pict != null ? !pict.equals(that.pict) : that.pict != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (!price.equals(that.price)) return false;
        return position.equals(that.position);
        */
        return thingId.equals(that.thingId);
    }



    @Override
    public int hashCode() {
        int result = thingId.hashCode();
        //result = 31 * result + (pict != null ? pict.hashCode() : 0);
        //result = 31 * result + (description != null ? description.hashCode() : 0);
        //result = 31 * result + price.hashCode();
        //result = 31 * result + position.hashCode();
        //result = 31 * result + (stuck ? 1 : 0);
        return result;
    }
}