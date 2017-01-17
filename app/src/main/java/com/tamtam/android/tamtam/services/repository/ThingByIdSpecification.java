package com.tamtam.android.tamtam.services.repository;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by fcng1847 on 17/01/17.
 */

public class ThingByIdSpecification implements TamTamServerSpecification {

    private final URI queryURI;

    @Override
    public URI toTamTamsQuery() {
        return  queryURI;
    }

    ThingByIdSpecification(String thingId) {
        if (thingId == null || thingId.isEmpty()) {
            throw new IllegalArgumentException("thingId must not be null or empty");
        } else {
            try {
                this.queryURI = new URI( null, null, null, -1,
                        "/things/" + thingId,
                        null, null);
            } catch (URISyntaxException e){
                throw new IllegalArgumentException("thingId is not URI safe");
            }
        }
    }


}
