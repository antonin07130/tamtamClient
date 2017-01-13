package com.tamtam.android.tamtam;

import static android.R.attr.description;
import static android.view.View.X;

/**
 * Created by antoninpa on 08/01/17.
 */

public class FakeTamtamServer {

    public final String thingString1 = "{\"thingId\":\"thing1\",\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\",\"description\":\"cest un premier truc\",\"price\":{\"currency\":489,\"price\":10.10},\"position\":{\"lon\":7.05289,\"lat\":43.6166},\"stuck\":false}";
    public final String thingString2 = "{\"thingId\":\"thing2\",\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\",\"description\":\"cest un deuxieme truc\",\"price\":{\"currency\":489,\"price\":20.20},\"position\":{\"lon\":7.05334,\"lat\":43.61664},\"stuck\":false}";
    public final String thingString3 = "{\"thingId\":\"thing3\",\"pict\":\"AAaaaIaAMaBASEa64aENCODEDaaaag==\",\"description\":\"cest un troisieme truc\",\"price\":{\"currency\":489,\"price\":30.30},\"position\":{\"lon\":7.12153,\"lat\":43.65839},\"stuck\":false}";

    public String getThing(String thingId) {
        if (thingId.equals("thing1")){
            return thingString1;
        } else if (thingId.equals("thing2")){
            return thingString2;
        } else if (thingId.equals("thing3")){
            return thingString3;
        } else {
            return null;
        }
    }

    public String getThingJsonList() {
        return "["+thingString1 + "," +
                thingString2 + "," +
                thingString3 + "," + "]";
    }
}
