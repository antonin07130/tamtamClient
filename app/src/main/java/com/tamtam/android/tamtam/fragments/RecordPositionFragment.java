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

import com.tamtam.android.tamtam.BuildConfig;
import com.tamtam.android.tamtam.R;


import com.tamtam.android.tamtam.model.PositionObject;
/**
 * A simple {@link Fragment} subclass.
 */
public class RecordPositionFragment extends android.support.v4.app.Fragment implements
        LocationListener // to respond to position events
{
    private static final String TAG = "RecordPositionFragment";

    //*********************
    // LOCATION PARAMETERS
    //*********************

    // location provider parameters
    //private static final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER; // coarse
    private static final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER; // fine
    // DO NOT FORGET THE MANIFEST FILE !

    // request code used for location permission checks callbacks
    //private static final int LOCATION_PERMISSION_REQUEST_CODE = 1; // coarse
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2; // fine

    private static final int MIN_LOC_UPDATE = 0;
    private static final int  MIN_LOC_DISTANCE = 0;
    //private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;// coarse
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;// fine



    // reference to EditTexts contained in this fragment
    // (validity on reloads not tested)
    EditText mLongitudeEditText;
    EditText mLatitudeEditText;

    PositionObject mCurrentPosition = null;
    LocationManager mLocationManager;
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
         * @param position created by this fragment.
         */
        public void onPositionRecorded(PositionObject position);
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
                    + " must implement OnPositionRecordedListener");
        }
    }


    //*********************
    // LIFECYCLE CALLBACKS
    //*********************

    /**
     * start the location service on fragment creation.
     */
    @Override
    public void onStart() {
        super.onStart();
        // store the location manager
        mLocationManager = (LocationManager)
                this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "onStart: try to setup location updates");
        setupLocalizationCallbackWithPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_record_position, container, false);

        mLongitudeEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_postion_longitude_et);
        mLatitudeEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_postion_latitude_et);

        // simulate a location update if we are in debug
        if (BuildConfig.DEBUG) {
            Location fakeLocation = new Location("fakeProvider");
            fakeLocation.setLongitude(7.05334);
            fakeLocation.setLatitude(43.61664);
            onLocationFound(fakeLocation);
        }


        return fragmentView;
    }

    /**
     * Don't forget to stop position updates when stopping the activity
     */
    @Override
    public void onStop() {
        super.onStop();
        if (hasPermission(LOCATION_PERMISSION)) {
            mLocationManager.removeUpdates(this);
            Log.d(TAG, "onStop: remove location updates");
        }
    }


    //***************
    // LOCATION CODE
    //***************

    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        onLocationFound(location);
        Log.d(TAG, "onLocationChanged: found a location");
        mLocationManager.removeUpdates(this);
        Log.d(TAG, "onLocationChanged: remove location updates");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    @RequiresPermission(LOCATION_PERMISSION)
    void setupLocalizationCallback() {
        // Acquire a reference to the host activity's system Location Manager
        mLocationManager.requestLocationUpdates( LOCATION_PROVIDER, // GPS or network ?
                                                 MIN_LOC_UPDATE,    // update rate
                                                 MIN_LOC_DISTANCE,  // accuracy
                                                 this );            // requester (implements callback)
        Log.d(TAG, "setupLocalizationCallback: location updates started");
    }


    /**
     * Callback to communicate position to the host activity
     * @param location last measured location
     */
    void onLocationFound(Location location){
        Log.d(TAG, "onLocationFound: " + location);
        mLatitudeEditText.setText(String.valueOf(location.getLatitude()));
        mLongitudeEditText.setText(String.valueOf(location.getLongitude()));
        mCurrentPosition = new PositionObject(
                location.getLongitude(), location.getLatitude());
        mCallback.onPositionRecorded(mCurrentPosition);
    }




    //***********************
    // PERMISSION CHECK CODE
    //***********************

    void setupLocalizationCallbackWithPermission() {
        Log.d(TAG, "setupLocalizationCallbackWithPermission");
        if (hasPermission(LOCATION_PERMISSION)) {
            Log.d(TAG, "setupLocalizationCallbackWithPermission : permission OK, setting up localization");
            setupLocalizationCallback();
        } else {
            Log.d(TAG, "setupLocalizationCallbackWithPermission : permission NOK requesting");
            requestPermissionAsync(LOCATION_PERMISSION,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Check if we have the permissionToCheck.
     * @param permissionToCheck the permission reference to check according to {@link Manifest}.permission
     * @return {@code true} if we have the permissionToCheck, {@code false} otherwise
     */
    boolean hasPermission(String permissionToCheck) {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                permissionToCheck)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "hasPermission: " + permissionToCheck + " GRANTED");
            return true;
        } else {
            Log.d(TAG, "hasPermission: permission " + permissionToCheck + " NOT granted");
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
                Log.d(TAG, "hasPermission: explaining why localization is needed");
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
                Log.d(TAG, "hasPermission: requesting permission with request code : "
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

        Log.d(TAG, "onRequestPermissionsResult: grantResult = " + grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
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
