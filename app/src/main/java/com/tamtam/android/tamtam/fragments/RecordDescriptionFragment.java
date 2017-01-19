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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tamtam.android.tamtam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordDescriptionFragment extends Fragment implements
        EditText.OnEditorActionListener, // for EditText callbacks
        Button.OnClickListener
{

    private static final String TAG = "RecordDescr..Fragment";

    public RecordDescriptionFragment() {}

    EditText mDescriptionEditText;
    Button mButtonValidate;

    /**
     * This callback object usually points to container action that implements the
     * callback method (see {@link OnDescriptionRecordedListener}).
     */
    OnDescriptionRecordedListener mCallback;




    //******************************
    // CALLBACKS WITH MAIN ACTIVITY
    //******************************

    /**
     * This interface must be implemented by container activity.
     * It is through this interface that container activity can get the Price Value
     */
    public interface OnDescriptionRecordedListener {
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
            mCallback = (OnDescriptionRecordedListener) activityContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(activityContext.toString()
                    + " must implement OnDescriptionRecordedListener");
        }
    }



    //*******************
    // TEXTEDIT CALLBACK
    //*******************

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            Log.d(TAG, "onEditorAction " + v.getText().toString());
            String description = v.getText().toString();
            mCallback.onDescriptionRecorded(description);

            return true;
        }
        return false;
    }

    //*****************
    // BUTTON CALLBACK
    //*****************

    @Override
    public void onClick(View v) {
        // call the same callback that is called by the virtual keyboard with "IME action"
        mDescriptionEditText.onEditorAction(EditorInfo.IME_ACTION_SEND);
        
        // complex set of commands to hide the virtual keyboard.
        InputMethodManager imm =
                (InputMethodManager) this.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_record_description, container, false);

        // wire callback between controls and this.
        mDescriptionEditText = (EditText) fragmentView.findViewById(R.id.fragment_record_description_description_et);
        mDescriptionEditText.setOnEditorActionListener(this);
        mButtonValidate = (Button) fragmentView.findViewById(R.id.fragment_record_description_validate_btn);
        mButtonValidate.setOnClickListener(this);

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
