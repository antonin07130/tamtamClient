package com.tamtam.android.tamtam.fragments;


import android.app.Activity;
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
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tamtam.android.tamtam.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.bitmap;
import static android.app.Activity.RESULT_CANCELED;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakePictureFragment extends Fragment {
    private static final String TAG = "TakePictureFragment";

    //*****************
    // PICTURE STORAGE
    //*****************


    // Path where the current photo is saved
    String mCurrentPhotoPath = null;
    // Path where the previous photo was saved
    String mPreviousPhotoPath = null;
    // Bundle Key to store mCurrentPhotoPath in Bundles on fragment reloads
    private static String M_CURRENT_PHOTO_PATH_BK = "m_current_photopath";
    private static String M_PREVIOUS_PHOTO_PATH_BK = "m_previous_photopath";
    // Is a valid picture taken already
    Boolean mValidPictureTaken = false;
    private static String M_VALID_PICTURE_TAKEN_BK = "m_valid_picture_taken";
    // Don't fully understand this... related to file prover and file sharing between camera and
    // this application : todo : understand this
    private static final String fileProviderAuthority = "com.tamtam.android.tamtam.fileprovider";

    //*****************
    // PICTURE CAPTURE
    //*****************
    // request code for picture taking activity
    // (only useful if several intents are dispatched fom this)
    static final int REQUEST_CODE_IMAGE_CAPTURE = 1;

    //**************
    // GUI ELEMENTS
    //**************
    // the button to start pictures taking
    AppCompatImageButton mTakePictureIVBTN;



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


        // if we recreate the view after a reorientation
        if (savedInstanceState != null) {
            // restore the taken photo in the button
            mCurrentPhotoPath = savedInstanceState.getString(M_CURRENT_PHOTO_PATH_BK, null);
            mValidPictureTaken = savedInstanceState.getBoolean(M_VALID_PICTURE_TAKEN_BK, false);
            mPreviousPhotoPath = savedInstanceState.getString(M_PREVIOUS_PHOTO_PATH_BK, null);
            if (mValidPictureTaken) {
                Log.d(TAG, "onCreateView: recreating view with a picture");
                // ugly hack to wait for imageview dimensions to be computed to call getWidth etc.. :
                // NO CALLBACK IN THE FUCKING API !!!
                fragmentView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Layout is ready, we can use it :
                        setImageButtonSrcToPic(mCurrentPhotoPath, mTakePictureIVBTN);
                    }
                });
            } else {
                Log.d(TAG, "onCreateView: recreating view but no picture taken yet");
            }
        }
        return fragmentView;
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


    // Called immediately before onResume() (so after onStart()) : members are initialized.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                mValidPictureTaken = true;

                // we already had taken a picture : we discard it
                if (mPreviousPhotoPath != null){
                    Log.d(TAG, "onActivityResult: deleting previously taken photograph at "+ mPreviousPhotoPath);
                    new File(mPreviousPhotoPath).delete();
                    mPreviousPhotoPath = null;
                }

                // Display the picture instead of the button
                setImageButtonSrcToPic(mCurrentPhotoPath, mTakePictureIVBTN);
                Log.d(TAG, "onActivityResult: picture taken");
                Log.d(TAG, "onActivityResult: " + resultCode);
            } else {
                Log.d(TAG, "onActivityResult: picture activity canceled");
                Log.d(TAG, "onActivityResult: deleting empty file at" + mCurrentPhotoPath);
                new File(mCurrentPhotoPath).delete();
                mCurrentPhotoPath = null;
                Toast.makeText(
                        getContext(),
                        getString(R.string.take_picture_fragment_photo_not_taken),
                        Toast.LENGTH_LONG).show();
            }
        } else {
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
        File storageDir =  getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // if we already had taken a picture in the past
        if (mValidPictureTaken){
            mPreviousPhotoPath = mCurrentPhotoPath;
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    /**
     * Resizes and applies the image at currentPhotoPath to the provided ImageView
     * From Android doc :
     * @see <a href="https://developer.android.com/training/camera/photobasics.html#TaskScalePhoto"></a>
     * @param currentPhotoPath path of the image to add to imageView
     * @param imageView imageView that will take the resized Photo as "src"
     */
    public void setImageButtonSrcToPic(final String currentPhotoPath, final ImageView imageView) {

        Log.d(TAG, "setImageButtonSrcToPic: Trying to recompute photo Bitmap");
        if (currentPhotoPath == null || currentPhotoPath.isEmpty()) {
            Log.d(TAG, "setImageButtonSrcToPic: invalid currentPhotoPath");
            return;
        }
        /*
        imageView.setImageURI(FileProvider.getUriForFile(this.getContext(),
                fileProviderAuthority,
                new File(currentPhotoPath)));
        */
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // check if this function can safely compute image sizes
        if (targetH == 0 || targetW ==0) {
            Log.d(TAG, "setImageButtonSrcToPic: invalid imageView Width or Height (is Layout Ready?)");
            return;
        }
        // Get the dimensions of the bitmap
        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        // Does not decode but only set bitmap metadata
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Actually decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        //bmOptions.inBitmap = true;

        Log.d(TAG, "setImageButtonSrcToPic: New Bitmap computed");
        //bmOptions.inBitmap = true;
        imageView.setImageDrawable(null);
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);

    }


    // may be better to override on pause, I just use it for screen reorientations
    // save the current taken picture before destroying instance
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(M_CURRENT_PHOTO_PATH_BK, mCurrentPhotoPath);
        outState.putBoolean(M_VALID_PICTURE_TAKEN_BK, mValidPictureTaken);
        outState.putString(M_PREVIOUS_PHOTO_PATH_BK, mPreviousPhotoPath);
    }




}
