package com.tamtam.android.tamtam.services.image;

import java.util.Collection;

/**
 * Created by antoninpa on 16/01/17.
 */


/**
 * This interface represents a converter from encoded Images to Images
 * @param <E> Encoded type (can be a base64 String for instance)
 * @param <I> Input Image type (can be a Jpeg for instance)
 */
public interface EncodedToImage <E, I> {
    /**
     * decode the provided encodedInput to an image of type I.
     * @param inputEncoded encoded Image.
     * @return an image of type I.
     */
    I Decode (E inputEncoded);

    /**
     * decode the provided colleciton of encodedInput to an image of type I.
     * @param inputEncoded encoded Images.
     * @return a collection of decoded Images (memory, beware !).
     */
    Collection<I> Decode(Collection<E> inputEncoded);

}
