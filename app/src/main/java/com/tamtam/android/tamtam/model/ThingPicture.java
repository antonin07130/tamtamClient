package com.tamtam.android.tamtam.model;

/**
 * Created by antoninpa on 16/01/17.
 */

import android.graphics.Bitmap;

/**
 * Class representing a picture in our application
 *
 * is it bad that is stores files ? really have no idea how to model a picture...
 *
 */
public class ThingPicture {
    public static final int THUMBNAIL_WIDTH = 500;
    public static final int THUMBNAIL_HEIGHT = 500;

    final Bitmap picureBitmap;
    final String pictureId;

    public ThingPicture(String pictureId, Bitmap pictureBitmap) {
        if (pictureBitmap != null
                && pictureBitmap.getWidth() > 0 // todo verify bitmap not too big ?
                && pictureId != null
                && pictureId.isEmpty()){
        this.picureBitmap = pictureBitmap;
        this.pictureId = pictureId;
        } else {
            throw new IllegalArgumentException("Tried to build an invalid ThingPicture");
        }
    }

    public Bitmap getPicureBitmap(){
        return picureBitmap;
    }
    public String getPictureId() { return pictureId; }

    @Override
    public String toString() {
        return "ThingPicture{" +
                "picureBitmap (Width / Height) =" + picureBitmap.getWidth() + " / " + picureBitmap.getHeight() +
                ", pictureId='" + pictureId + '\'' +
                '}';
    }
}
