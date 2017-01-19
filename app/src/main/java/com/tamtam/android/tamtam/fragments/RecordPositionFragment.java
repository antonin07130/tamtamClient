package com.tamtam.android.tamtam.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tamtam.android.tamtam.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordPositionFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "RecordPositionFragment";

    // request code used for location permission checks callbacks
    private static final int COARSELOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int FINELOCATION_PERMISSION_REQUEST_CODE = 2;

    // type of location provider
    private static final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;


    // reference to EditTexts contained in this fragment
    // (validity on reloads not tested)
    EditText mLongitudeEditText;
    EditText mLatitudeEditText;

    boolean mLongitudeRecorded = false;
    boolean mLatitudeRecorded = false;

    boolean mPositionTrackingOnGoing = false;


    double mLongitudeValue;
    double mLatitudeValue;


    /**
     * Empty constructor
     */
    public RecordPositionFragment() {
    }


    //******************************
    // CALLBACKS WITH MAIN ACTIVITY
    //******************************

    /**
     * This callback object usually points to container action that implements the
     * callback method (see {@link OnPositionRecordedListener}).
     */
    OnPositionRecordedListener mCallback;

    /**
     * This interface must be implemented by container activity.
     * It is through this interface that container activity can get the Price Value
     */
    public interface OnPositionRecordedListener {
        /**
         * Callback to provide to container with the position
         * @param location created by this fragment.
         */
        public void onPositionRecorded(Location location);
    }

    /**
     * Called by android system to
     * setup callback with the host activity. If the callback is not defined in the host activity,
     * an exception is raised.
     * @param activityContext host activity (filled by the system)
     */
    @Override
    public void onAttach(Context activityContext) {
        super.onAttach(activityContext);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPositionRecordedListener) activityContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activityContext.toString()
                    + " must implement OnDescriptionRecordedListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_record_position, container, false);

        mLongitudeEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_postion_longitude_et);
        //setUpTextEditCallback(mLongitudeEditText, mCallback);

        mLatitudeEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_postion_latitude_et);
        //setUpTextEditCallback(mLatitudeEditText, mCallback);


        if (savedInstanceState == null) { // if this is the first time setup :
            // can we use localization service
            setupLocalizationCallbackWithPermission();
            // not much to save and restore here
        }
        return fragmentView;
    }


    //***************
    // LOCATION CODE
    //***************


    @RequiresPermission(ACCESS_COARSE_LOCATION)
    void setupLocalizationCallback() {
        Log.d(TAG, "setupLocalizationCallback: started");
        // Acquire a reference to the host activity's system Location Manager
        final LocationManager locationManager = (LocationManager)
                this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                onLocationFound(location);
                Log.d(TAG, "onLocationChanged: found a location");
                locationManager.removeUpdates(this);
                mPositionTrackingOnGoing = false;
                Log.d(TAG, "onLocationChanged: locationManager.removeUpdates");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        mPositionTrackingOnGoing = true;
        Log.d(TAG, "setupLocalizationCallback: done");
    }

    void onLocationFound(Location location){
        Log.d(TAG, "onLocationFound: " + location);
        mLatitudeEditText.setText(String.valueOf(location.getLatitude()));
        mLongitudeEditText.setText(String.valueOf(location.getLongitude()));
        mCallback.onPositionRecorded(location);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPositionTrackingOnGoing){
            // todo correct unsafe method : user may have disallowed position while the applciaitoon was tracking

            // todo stop location manager !!!
        }
    }

//***********************
    // PERMISSION CHECK CODE
    //***********************

    void setupLocalizationCallbackWithPermission() {
        Log.d(TAG, "setupLocalizationCallbackWithPermission");
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            setupLocalizationCallback();
        } else {
            requestPermissionAsync(Manifest.permission.ACCESS_COARSE_LOCATION,
                    COARSELOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Check if we have the permissionToCheck.
     * @param permissionToCheck
     * @return {@code true} if we have the permissionToCheck, {@code false} otherwise
     */
    boolean checkPermission(String permissionToCheck) {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                permissionToCheck)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: " + permissionToCheck + " GRANTED");
            return true;
        } else {
            Log.d(TAG, "checkPermission: permission " + permissionToCheck + " NOT granted");
            return false;
        }
    }

    /**
     * Asks the user for authorization.
     * User's answer is provided in onRequestPermissionsResult callback
     * @param permissionToRequest permission to ask as in {@link android.Manifest.permission}
     * @param permissionRequestCode a unique code for this permission request
     */
    void requestPermissionAsync(final String permissionToRequest, final int permissionRequestCode){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    permissionToRequest)) {
                Log.d(TAG, "checkPermission: explaining why localization is needed");
                showMessageOKCancel(getContext().getString(
                        R.string.record_position_fragment_authorization_explanation),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ok was clicked :
                        // from fragment do not use appcompat
                        requestPermissions( new String[]{permissionToRequest},
                                            permissionRequestCode );
                    }
                });
            } else {
                requestPermissions( new String[]{permissionToRequest},
                                    permissionRequestCode);
                Log.d(TAG, "checkPermission: requesting permission with request code : "
                        + permissionRequestCode);
            }

    }


    /**
     * this was not called because of appcompat API compatibility issues
     * {@see <a href="http://stackoverflow.com/questions/32714787/android-m-permissions-onrequestpermissionsresult-not-being-called">
     *     stackoverflow </a>}
     * @param requestCode same as requestPermissionAsync's request code
     * @param permissions array with provided Permission (if several were requested)
     * @param grantResults result to compare to {@link PackageManager}'s PERMISSION_GRANTED constant
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case COARSELOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: new permission granted with response code : "
                            + requestCode);
                    Log.d(TAG, "onRequestPermissionsResult: asking to set localization callbacks");
                    setupLocalizationCallback();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: permission explicitely refused with response code : "
                            + requestCode);
                    // todo : propose another interface to set localization
                    Log.d(TAG, "onRequestPermissionsResult: TODO : propose another interface to set localization");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /**
     * code from
     * {@see <a href="https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en">inthecheesefactory</a>}
     * @param message
     * @param okListener
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
