package com.tamtam.android.tamtam.services.repository;

import java.net.URI;

/**
 * Created by fcng1847 on 17/01/17.
 */

public interface TamTamServerSpecification extends Specification {
    URI toTamTamsQuery();
}
