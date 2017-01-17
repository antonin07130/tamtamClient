package com.tamtam.android.tamtam.services.image;

import java.util.Collection;

/**
 * Created by antoninpa on 16/01/17.
 */

public interface ImageToEncoded<I, E> {

    E Encode(I inputImage);

    Collection<E> Encode(Collection<I> inputImage);
}
