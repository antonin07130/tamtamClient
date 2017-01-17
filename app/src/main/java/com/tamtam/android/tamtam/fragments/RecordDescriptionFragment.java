package com.tamtam.android.tamtam.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tamtam.android.tamtam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordDescriptionFragment extends Fragment {
    private static final String TAG = "RecordDescriptionFragment";


    //*************
    // PRICE VALUE
    //*************
    // Bundle Key to store mCurrentPhotoPath in Bundles on fragment reloads
    private static String M_CURRENT_DESCRIPTION_BK = "current_description";
    EditText mDescriptionEditText;

    public RecordDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_record_description, container, false);

        mDescriptionEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_description_description_et);


        if (savedInstanceState != null) {
            String currentDescription = savedInstanceState.getString(M_CURRENT_DESCRIPTION_BK);
            if (currentDescription != null) {
                mDescriptionEditText.setText(currentDescription);
            } else {
                throw new IllegalStateException("savedInstance not null but Price Value not stored");
            }
        }
        return fragmentView;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(M_CURRENT_DESCRIPTION_BK, mDescriptionEditText.getText().toString());
    }
}
