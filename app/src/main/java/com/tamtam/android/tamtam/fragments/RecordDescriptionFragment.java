package com.tamtam.android.tamtam.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tamtam.android.tamtam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordDescriptionFragment extends Fragment {
    private static final String TAG = "RecordDescr..Fragment";

    public RecordDescriptionFragment() { }


    /**
     * This callback object usually points to container action that implements the
     * callback method (see {@link RecordDescriptionFragment.OnPriceRecordedListener}).
     */
    RecordDescriptionFragment.OnPriceRecordedListener mCallback;

    /**
     * This interface must be implemented by container activity.
     * It is through this interface that container activity can get the Price Value
     */
    public interface OnPriceRecordedListener {
        /**
         * Callback to provide to container activity the price value
         * @param description text {@link String} created by this fragment.
         */
        public void onDescriptionRecorded(String description);
    }

    @Override
    public void onAttach(Context activityContext) {
        super.onAttach(activityContext);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (RecordDescriptionFragment.OnPriceRecordedListener) activityContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activityContext.toString()
                    + " must implement OnDescriptionRecordedListener");
        }
    }


    EditText mDescriptionEditText;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_record_description, container, false);

        mDescriptionEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_description_description_et);
        mDescriptionEditText.setOnClickListener(
                new EditText.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        mCallback.onDescriptionRecorded(mDescriptionEditText.getText().toString());
                        Log.d(TAG, "onClick" + mDescriptionEditText.getText().toString());
                    }
                });


        if (savedInstanceState != null) {
            // well do nothing...
        }
        return fragmentView;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
