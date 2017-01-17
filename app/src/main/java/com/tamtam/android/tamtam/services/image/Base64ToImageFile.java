package com.tamtam.android.tamtam.services.image;

import java.io.File;
import java.util.Collection;

/**
 * Created by antoninpa on 17/01/17.
 */


/**
 * I see a FileReader and a Base64OutputStream
 */
public class Base64ToImageFile implements EncodedToImage<String, File> {
    @Override
    public File Decode(String inputEncoded) {


        return null;
    }

    @Override
    public Collection<File> Decode(Collection<String> inputEncoded) {
        return null;
    }
}
