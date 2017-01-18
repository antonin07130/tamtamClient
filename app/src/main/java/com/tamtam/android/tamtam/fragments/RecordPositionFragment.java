package com.tamtam.android.tamtam.fragments;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.model.ThingObject.PriceObject;

import java.util.Currency;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordPositionFragment extends Fragment {
    private static final String TAG = "RecordPriceFragment";

    // reference to EditTexts contained in this fragment
    // (validity on reloads not tested)
    EditText mLongitudeEditText;
    EditText mLatitudeEditText;

    boolean mLongitudeRecorded = false;
    boolean mLatitudeRecorded = false;

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
         * Callback to provide to container activity the price value
         * @param priceObject created by this fragment.
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


    //**************************
    // TEXT EDIT CALLBACK SETUP
    //**************************

    /**
     * sets up a callback to the host activity when the input is "validated" by the user.
     * @param watchedEditText EditText to watch, from where the value is read
     * @param mainActivityListener Callback to reach the host activity
     */
    private void setUpTextEditCallback(final EditText watchedEditText, final OnPositionRecordedListener mainActivityListener) {
        watchedEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            Log.d(TAG, "onEditorAction " + watchedEditText.getText().toString());
                            String priceValueString = watchedEditText.getText().toString();
                            if (priceValueString.isEmpty()) {
                                priceValueString = "0.";
                            }
                            return true;
                        }
                        return false;
                    }
                });
    }

    @RequiresPermission(ACCESS_COARSE_LOCATION)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_record_price, container, false);

        mLongitudeEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_postion_longitude_et);
        setUpTextEditCallback(mLongitudeEditText, mCallback);

        mLatitudeEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_postion_latitude_et);
        setUpTextEditCallback(mLatitudeEditText, mCallback);


        if (savedInstanceState == null) { // if this is the first time setup :
            // Acquire a reference to the host activity's system Location Manager
            LocationManager locationManager = (LocationManager)
                    this.getActivity().getSystemService(Context.LOCATION_SERVICE);

            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    onLocationFound(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

            /*
            // Register the listener with the Location Manager to receive location updates
            if (ActivityCompat.checkSelfPermission(this, ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return TODO;
            }*/

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);




            // not much to save and restore here
        }
        return fragmentView;
    }




    void onLocationFound(Location location){
        mCallback.onPositionRecorded(location);
    }

}
