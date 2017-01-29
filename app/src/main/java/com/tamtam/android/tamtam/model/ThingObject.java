/*
 *     Android application to create and display localized objects.
 *     Copyright (C) 2017  pascal bodin, antonin perrot-audet
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    public static String LOG_TAG = "ThingObject";


    private final String thingId; // never empty
    private final ThingPicture pict;
    private final String description;
    private final PriceObject price; // never null
    private final PositionObject position; // never null
    private final boolean stuck;


    /**
     * Use this class to build a new thing from scratch :
     * {@code new ThingObject.ThingBuilder("ID_KEYNAME")
    .pict(Byte[] of the jpeg file)
    .description("description")
    .position(positionObject)
    .price(priceObject)
    .stuck(true)
    .build(); }
     */
    public static class ThingBuilder {


        private String thingId = "";
        private ThingPicture pict;
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
         public ThingBuilder pict(ThingPicture pict) {
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

        @Override
        public String toString() {
            return "ThingBuilder{" +
                    "thingId='" + thingId + '\'' +
                    ", pict='" + pict + '\'' +
                    ", description='" + description + '\'' +
                    ", price=" + price +
                    ", position=" + position +
                    ", stuck=" + stuck +
                    '}';
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
     public ThingPicture getPict(){return this.pict;}
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