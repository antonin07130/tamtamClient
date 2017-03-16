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

/**
 * Created by antoninpa on 22/01/17.
 */

import java.util.Currency;

/**
 * This class represents a price and its currency.
 * todo : use android currency ? java.util.Currency;
 **/
public class PriceObject implements java.io.Serializable {
    private static final double precision = 0.00001; // when comparing pricevalues
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
        // todo use string to store price values instead of doubles to prevent truncation errors
        //if (Double.compare(that.price, price) != 0) return false;
        if ((that.price - price) / price > precision) return false;
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

    @Override
    public String toString() {
        return "PriceObject{" +
                "currency=" + currency +
                ", price=" + price +
                '}';
    }
}
