package com.tamtam.android.tamtam.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.fragments.RecordDescriptionFragment;
import com.tamtam.android.tamtam.fragments.RecordPriceFragment;
import com.tamtam.android.tamtam.fragments.TakePictureFragment;
import com.tamtam.android.tamtam.model.ThingObject;

public class SellThingActivity extends AppCompatActivity
        implements RecordPriceFragment.OnPriceRecordedListener,
        RecordDescriptionFragment.OnPriceRecordedListener{
    private static final String TAG = "SellThingActivity";

    Fragment mTakePictureFragment,
            mRecordPriceFragment,
            mRecordDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_thing);

        //***********
        // SETUP GUI
        //***********
        Log.d(TAG, "onCreate: SettingUp GUI");

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.activity_sell_thing) != null) {

            // if restored : do not create a new fragment
            if (savedInstanceState != null) {
                Log.d(TAG, "onCreate: Reloading GUI : do nothing");
                return;
            }

            Log.d(TAG, "onCreate: creating fragments");
            // instantiate fragments for this activity
            mTakePictureFragment = new TakePictureFragment();
            mRecordPriceFragment = new RecordPriceFragment();
            mRecordDescriptionFragment = new RecordDescriptionFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // firstFragment.setArguments(getIntent().getExtras());

            Log.d(TAG, "onCreate: adding fragments");
            // Add the fragments to the 'fragment_container's FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sell_thing_take_picture_frame, mTakePictureFragment)
                    .add(R.id.activity_sell_thing_record_price_frame, mRecordPriceFragment)
                    .add(R.id.activity_sell_thing_record_description_frame, mRecordDescriptionFragment)
                    .commit();

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        setUpFocus();
    }

    /**
     * Setup tab order for textedits focus to chain when validating.
     */
    private void setUpFocus(){
        EditText priceEditText = (EditText) findViewById(R.id.fragment_record_price_price_et);
        //EditText descriptionEditText = (EditText)findViewById(R.id.fragment_record_description_description_et);
        priceEditText.setNextFocusDownId(R.id.fragment_record_description_description_et);
    }




    //************************************
    // GET OBJECTS GENERATED BY FRAGMENTS
    //************************************

    @Override
    public void onPriceRecorded(ThingObject.PriceObject priceObject) {
        Log.d(TAG, "onPriceRecorded: got PriceObject :" + priceObject);
    }

    @Override
    public void onDescriptionRecorded(String description) {
        Log.d(TAG, "onDescriptionRecorded: got Description" + description);
    }
}
