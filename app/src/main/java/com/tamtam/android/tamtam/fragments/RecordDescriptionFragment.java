package com.tamtam.android.tamtam.fragments;


import android.content.Context;
import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordDescriptionFragment extends Fragment {
    private static final String TAG = "RecordDescr..Fragment";

    // reference to the EditText contained in this fragment
    // (validity on reloads not tested)
    EditText mDescriptionEditText;
    /**
     * Empty constructor
     */
    public RecordDescriptionFragment() { }


    //******************************
    // CALLBACKS WITH MAIN ACTIVITY
    //******************************

    /**
     * This callback object usually points to container action that implements the
     * callback method (see {@link OnDescriptionRecordedListener}).
     * it is used by the fragment to transmit data to the host activity.
     */
    OnDescriptionRecordedListener mCallback;

    /**
     * This interface must be implemented by container activity.
     * It is through this interface that container activity can get the description {@link String}.
     */
    public interface OnDescriptionRecordedListener {
        /**
         * Callback to provide to container activity the description {@link String}
         * @param description text {@link String} created by this fragment.
         */
        public void onDescriptionRecorded(String description);
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
            mCallback = (OnDescriptionRecordedListener) activityContext;
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
    private void setUpTextEditCallback(final EditText watchedEditText, final OnDescriptionRecordedListener mainActivityListener) {
        watchedEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        Log.d(TAG, "onEditorAction: actionNumber " + actionId);
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            Log.d(TAG, "onEditorAction : value" + watchedEditText.getText().toString());
                            String description = watchedEditText.getText().toString();
                            mainActivityListener.onDescriptionRecorded(description);
                            return true;
                        }
                        return false;
                    }
                });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_record_description, container, false);

        mDescriptionEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_description_description_et);
        setUpTextEditCallback(mDescriptionEditText, mCallback);

        return fragmentView;
    }
}
