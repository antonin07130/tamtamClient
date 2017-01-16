package com.tamtam.android.tamtam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.fragments.ListThingsFragment;

public class AroundMe extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_me);


        //***********
        // SETUP GUI
        //***********

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.activity_around_me) != null) {

            // if restored : do not create a new fragment
            if (savedInstanceState != null) {
                return;
            }
            // instanciate a new ListThingsFragment
            ListThingsFragment listThingsFragment = new ListThingsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_around_me, listThingsFragment).commit();
        }

        //*****************
        // SETUP CALLBACKS
        //*****************

        AppCompatButton mBtn = (AppCompatButton) findViewById(R.id.around_me_startsell_btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AroundMe.this, SellThingActivity.class);
                startActivity(i);
            }
        });


    }
}
