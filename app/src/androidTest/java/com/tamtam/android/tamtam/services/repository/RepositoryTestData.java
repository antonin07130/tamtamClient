package com.tamtam.android.tamtam.services.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tamtam.android.tamtam.JsonThingTestData;
import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.PriceObject;
import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.model.ThingPicture;

import java.util.Currency;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by antoninpa on 28/01/17.
 */

public class RepositoryTestData {


    public static final Bitmap testBitmap1 =
            BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                    com.tamtam.android.tamtam.test.R.drawable.test_png_1);

    public static final Bitmap testBitmap2 =
            BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                    com.tamtam.android.tamtam.test.R.drawable.test_png_2);

    public static final Bitmap testBitmap3 =
            BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                    com.tamtam.android.tamtam.test.R.drawable.test_png_3);


    public static final PositionObject positionObject1 =
            new PositionObject(7.05289, 43.6166);
    public static final PositionObject positionObject1bis =
            new PositionObject(7.00, 44.00);
    public static final PriceObject priceObject1 =
            new PriceObject(Currency.getInstance("EUR"), 10.10);
    public static final ThingPicture thingPicture1 =
            new ThingPicture("picture1", JsonThingTestData.testBitmap1);

    public static final ThingObject thingObject1 = new ThingObject.ThingBuilder()
            .thingId("thing1")
            .pict(thingPicture1)
            .description("cest un premier truc")
            .position(positionObject1)
            .price(priceObject1)
            .stuck(false)
            .build();

    public static final ThingObject thingObject1bis = new ThingObject.ThingBuilder()
            .thingId("thing1")
            .pict(thingPicture1)
            .description("truc modifie !")
            .position(positionObject1bis)
            .price(priceObject1)
            .stuck(false)
            .build();

    public static final ThingObject thingObject2 = new ThingObject.ThingBuilder()
            .thingId("thing2")
            .pict(new ThingPicture("picture2", JsonThingTestData.testBitmap2))
            .description("cest un deuxieme truc")
            .position(new PositionObject(7.05334, 43.61664))
            .price(new PriceObject(Currency.getInstance("EUR"), 20.20))
            .stuck(false)
            .build();

    public static final ThingObject thingObject3 = new ThingObject.ThingBuilder()
            .thingId("thing3")
            .pict(new ThingPicture("picture3", JsonThingTestData.testBitmap3))
            .description("cest un troisieme truc")
            .position(new PositionObject(7.12153, 43.65839))
            .price(new PriceObject(Currency.getInstance("EUR"), 30.30))
            .stuck(false)
            .build();

}
