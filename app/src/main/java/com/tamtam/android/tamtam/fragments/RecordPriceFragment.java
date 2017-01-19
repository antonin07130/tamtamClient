package com.tamtam.android.tamtam.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.AppCompatEditText;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.model.ThingObject.PriceObject;

import java.util.Currency;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordPriceFragment extends Fragment implements
        EditText.OnEditorActionListener // for TextEdit callbacks
{
    private static final String TAG = "RecordPriceFragment";

    // reference to the EditText contained in this fragment
    // (validity on reloads not tested)
    EditText mPriceValueEditText;

    /**
     * Empty constructor
     */
    public RecordPriceFragment() {}


    //*****************************
    // CALLBACK WITH MAIN ACTIVITY
    //*****************************

    /**
     * This callback object usually points to container action that implements the
     * callback method (see {@link OnPriceRecordedListener}).
     */
    OnPriceRecordedListener mCallback;

    /**
     * This interface must be implemented by container activity.
     * It is through this interface that container activity can get the Price Value
     */
    public interface OnPriceRecordedListener {
        /**
         * Callback to provide to container activity the price value
         * @param priceObject created by this fragment.
         */
        public void onPriceRecorded(ThingObject.PriceObject priceObject);
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
            mCallback = (OnPriceRecordedListener) activityContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activityContext.toString()
                    + " must implement OnDescriptionRecordedListener");
        }
    }


    //********************
    // TEXT EDIT CALLBACK
    //********************


    /**
     * Callback when our {@link EditText} receives an action to record inputs and send it to host activity
     * using mCallback.onPriceRecorded
     * @param v the {@link EditText} that received an action
     * @param actionId the aciton id (check IME action in layout)
     * @param event not used
     * @return true if we use the event and don't want it to be propagated elsewhere. false otherwise.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d(TAG, "onEditorAction: actionNumber " + actionId);
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            Log.d(TAG, "onEditorAction : value : " + v.getText().toString());
            String priceValueString = v.getText().toString();
            if (priceValueString.isEmpty()) {
                priceValueString = "0.";
            }
            mCallback.onPriceRecorded(
                    new PriceObject(
                            Currency.getInstance("EUR"),
                            Double.parseDouble(priceValueString)));
            return true;
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_record_price, container, false);

        mPriceValueEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_price_price_et);
        mPriceValueEditText.setOnEditorActionListener(this);

        return fragmentView;
    }
}
