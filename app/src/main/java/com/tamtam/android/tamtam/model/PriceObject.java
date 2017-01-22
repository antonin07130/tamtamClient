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

    @Override
    public String toString() {
        return "PriceObject{" +
                "currency=" + currency +
                ", price=" + price +
                '}';
    }
}
