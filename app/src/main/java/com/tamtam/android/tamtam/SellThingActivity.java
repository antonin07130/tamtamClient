package com.tamtam.android.tamtam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SellThingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_thing);

        //***********
        // SETUP GUI
        //***********

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.activity_sell_thing) != null) {

            // if restored : do not create a new fragment
            if (savedInstanceState != null) {
                return;
            }
            // instantiate a new TakePictureFragment
            TakePictureFragment takePictureFragment = new TakePictureFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sell_thing, takePictureFragment).commit();
        }
    }
}
