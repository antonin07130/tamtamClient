package com.tamtam.android.tamtam.model;

/**
 * Created by antoninpa on 16/01/17.
 */

import java.io.File;
import java.net.URI;

/**
 * Class representing a picture in our application
 *
 * is it bad that is stores files ? really have no idea how to model a picture...
 *
 */
public class ThingPicture {
    final Byte[] picureFile;
    public ThingPicture(Byte[] pictureFile) {
        //if (pictureFile != null
        //        && pictureFile.isFile()
        //        && pictureFile.canRead()
        //        && pictureFile.length() > 0) {
        this.picureFile = pictureFile;
        //} else {
        //    throw new IllegalArgumentException("Tried to build an invalid ThingPicture");
        //}
    }

    Byte[] getPicureFile(){
        return picureFile;
    }
}
