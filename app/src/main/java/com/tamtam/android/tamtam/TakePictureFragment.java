package com.tamtam.android.tamtam;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakePictureFragment extends Fragment {
    private static final String TAG = "TakePictureFragment";

    // Path where photos are saved
    String mCurrentPhotoPath;
    private static String CURRENT_PHOTOPATH_BUNDLEKEY = "current_photo_path";

    // request code for picture taking activity
    // (only usefull if several intents are dispatched fom this)
    static final int REQUEST_CODE_IMAGE_CAPTURE = 1;



    // the button to start pictures taking
    AppCompatImageButton mTakePictureIVBTN;


    // don't fully understand this...
    private static final String fileProviderAuthority = "com.tamtam.android.tamtam.fileprovider";



    public TakePictureFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_take_picture, container, false);

        // setup callback for clicking on take picture button
        mTakePictureIVBTN = (AppCompatImageButton) fragmentView.findViewById(R.id.fragment_take_picture_ivbtn);
        mTakePictureIVBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {sendTakePictureIntent();} });

        // ugly hack to wait for imageview dimensions to be ready : NO CALLBACK IN THE FUCKING API !!!
        fragmentView.post(new Runnable() {
            @Override
            public void run() {
                setImageButtonSrcToPic(mCurrentPhotoPath); //height is ready
            }
        });

        return fragmentView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // if we recreate the view after a reorientation
        if (savedInstanceState != null){
            // restore the taken photo in the button
            mCurrentPhotoPath = savedInstanceState.getString(CURRENT_PHOTOPATH_BUNDLEKEY);
        }
    }

    public void sendTakePictureIntent(){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    Log.d(TAG, "sendTakePictureIntent: empty file created at " +
                            photoFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e(TAG, "sendTakePictureIntent: creating temporary file", e);
                    // todo : should we throw another exception or are we high enough ?
                }

                if (photoFile != null){
                    Uri photoURI = FileProvider.getUriForFile(
                            this.getContext(),
                            fileProviderAuthority,
                            photoFile);
                    Log.d(TAG, "sendTakePictureIntent: photoFile Uri is " + photoURI.getPath());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
                startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
            }
    }
    
    

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE){
            setImageButtonSrcToPic(mCurrentPhotoPath); // display the picture instead of the button
            Log.d(TAG, "onActivityResult: picture taken");
        } else if (requestCode == RESULT_CANCELED ){
            Log.d(TAG, "onActivityResult: picture activity canceled");
        } else{
            Log.d(TAG, "onActivityResult: other un-managed activity result");
        }
        
    }

    /**
     * creates a unique filename that ressembles :
     * JPEG_19880212_032714_{filemane}.jpg
     * in the app-private storage directory specified by {@link Environment}.DIRECTORY_PICTURES
     * @return a temporary empty file to be filled with image data
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    private void setImageButtonSrcToPic(String currentPhotoPath) {

        if (currentPhotoPath == null) //silently do nothing if something is wrong
                return;

        // Get the dimensions of the View
        int targetW = mTakePictureIVBTN.getWidth();
        int targetH = mTakePictureIVBTN.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        mTakePictureIVBTN.setImageBitmap(bitmap);
    }


    // may be better to override on pause, I just use it for screen reorientations
    // save the current taken picture before destroying instance
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_PHOTOPATH_BUNDLEKEY, mCurrentPhotoPath);
    }




}
