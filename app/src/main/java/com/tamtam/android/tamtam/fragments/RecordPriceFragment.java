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
import android.widget.EditText;
import android.widget.TextView;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.model.ThingObject.PriceObject;

import java.util.Currency;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordPriceFragment extends Fragment {
    private static final String TAG = "RecordPriceFragment";

    /**
     * Action Id for keyboard events (when pressing validation key)
     */
    private static final int OK_ACTION = 1;

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

    @Override
    public void onAttach(Context activityContext) {
        super.onAttach(activityContext);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPriceRecordedListener) activityContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activityContext.toString()
                    + " must implement OnPriceRecordedListener");
        }
    }



    //*************
    // PRICE VALUE
    //*************
    // Bundle Key to store mCurrentPhotoPath in Bundles on fragment reloads
    private static String M_CURRENT_PRICE_VALUE_BK = "current_price_value";
    EditText mPriceValueEditText;

    public RecordPriceFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_record_price, container, false);
        mPriceValueEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_price_price_et);

        /**
         * setup validation label and action code
         */
        mPriceValueEditText.setImeActionLabel(
                getResources().getString(R.string.record_price_fragment_keyboard_ok),
                OK_ACTION);
        mPriceValueEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener(){
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        Log.d(TAG, "onEditorAction " + mPriceValueEditText.getText().toString());
                        String priceValueString = mPriceValueEditText.getText().toString();
                        if (actionId == OK_ACTION){
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
                });


        if (savedInstanceState != null) {
            // not much to save and restore here
        }
        return fragmentView;

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // not much to save and restore here
    }
}
