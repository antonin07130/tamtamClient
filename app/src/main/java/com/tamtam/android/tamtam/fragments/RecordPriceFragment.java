package com.tamtam.android.tamtam.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.AppCompatEditText;
import android.widget.EditText;

import com.tamtam.android.tamtam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordPriceFragment extends Fragment {
    private static final String TAG = "RecordPriceFragment";


    //*************
    // PRICE VALUE
    //*************
    // Bundle Key to store mCurrentPhotoPath in Bundles on fragment reloads
    private static String M_CURRENT_PRICE_VALUE_BK = "current_price_value";
    EditText mPriceValueEditText;

    public RecordPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_record_price, container, false);

        mPriceValueEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_price_price_et);


        if (savedInstanceState != null) {
            String currentPriceValue = savedInstanceState.getString(M_CURRENT_PRICE_VALUE_BK);
            if (currentPriceValue != null) {
                mPriceValueEditText.setText(currentPriceValue);
            } else {
                throw new IllegalStateException("savedInstance not null but Price Value not stored");
            }
        }
        return fragmentView;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(M_CURRENT_PRICE_VALUE_BK, mPriceValueEditText.getText().toString());
    }
}
