package com.tamtam.android.tamtam.services.repository;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fcng1847 on 17/01/17.
 */
public class ThingByIdSpecificationTest {
    @Test
    public void toTamTamsQuery() throws Exception {
        assertEquals("/things/thingid0", new ThingByIdSpecification("thingId0").toTamTamsQuery());

    }

}