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
    public static final int THUMBNAIL_WIDTH = 50;
    public static final int THUMBNAIL_HEIGHT = 50;

    final Bitmap picureBitmap;
    final String pictureId;

    public ThingPicture(String pictureId, Bitmap pictureBitmap) {
        if (pictureBitmap != null
                && (pictureBitmap.getWidth() > 0) // todo verify bitmap not too big ?
                && pictureId != null
                && !pictureId.isEmpty()
                ){
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
                "pictureId='" + pictureId + '\'' +
                ", picureBitmap (Width / Height) =" + picureBitmap.getWidth() +
                                              " / " + picureBitmap.getHeight() +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThingPicture that = (ThingPicture) o;

        return pictureId.equals(that.pictureId);

    }

    @Override
    public int hashCode() {
        return pictureId.hashCode();
    }
}
