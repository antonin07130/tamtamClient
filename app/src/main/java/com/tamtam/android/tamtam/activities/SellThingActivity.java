package com.tamtam.android.tamtam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.fragments.RecordPriceFragment;
import com.tamtam.android.tamtam.fragments.TakePictureFragment;

public class SellThingActivity extends AppCompatActivity {
    private static final String TAG = "SellThingActivity";

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
            TakePictureFragment takePictureFragment = new TakePictureFragment();
            RecordPriceFragment recordPriceFragment = new RecordPriceFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // firstFragment.setArguments(getIntent().getExtras());

            Log.d(TAG, "onCreate: adding fragments");
            // Add the fragments to the 'fragment_container's FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sell_thing_take_picture_frame, takePictureFragment)
                    .add(R.id.activity_sell_thing_record_price_frame, recordPriceFragment)
                    .commit();
        }
    }
}
