package com.tamtam.android.tamtam.activities;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.fragments.RecordDescriptionFragment;
import com.tamtam.android.tamtam.fragments.RecordPositionFragment;
import com.tamtam.android.tamtam.fragments.RecordPriceFragment;
import com.tamtam.android.tamtam.fragments.TakePictureFragment;
import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.model.PriceObject;
import com.tamtam.android.tamtam.model.PositionObject;
import com.tamtam.android.tamtam.model.ThingPicture;
import com.tamtam.android.tamtam.services.image.BitmapUtils;
import com.tamtam.android.tamtam.services.json.JsonThingConverter;
import com.tamtam.android.tamtam.services.repository.FakeThingRepository;
import com.tamtam.android.tamtam.services.repository.Repository;

import java.lang.reflect.Array;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.tamtam.android.tamtam.services.image.BitmapUtils.calculateInSampleSize;
import static com.tamtam.android.tamtam.services.image.BitmapUtils.decodeSampledBitmapFromPath;
import static java.util.UUID.randomUUID;


public class SellThingActivity extends AppCompatActivity
        implements RecordPriceFragment.OnPriceRecordedListener, // read Price Value
                   RecordDescriptionFragment.OnDescriptionRecordedListener, // read Description value
                   RecordPositionFragment.OnPositionRecordedListener, // read Position value
                   TakePictureFragment.OnPictureTakenListener,
                   Button.OnClickListener // listen to button click
{
    private static final String TAG = "SellThingActivity";


    // our access to Thing database
    Repository<ThingObject> mThingRepo;


    Fragment mTakePictureFragment,
            mRecordPriceFragment,
            mRecordDescriptionFragment,
            mRecordPositionFragment;

    EditText mDescriptionEditText;
    EditText mPriceEditText;
    Button mSellThingBtn;

    private String mDescription = "";
    private static final String M_POSITION_BK = "position";
    private PriceObject mPrice;
    private static final String M_PRICE_BK = "price";
    private PositionObject mPosition;
    private static final String M_DESCRIPTION_BK = "description";
    private URI mPictURI;
    private static final String M_PICT_URI_BK = "picture";


    //*******************
    // LIFECYCLE METHODS
    //*******************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_thing);

        //***********
        // SETUP GUI
        //***********
        Log.d(TAG, "onCreate: SettingUp GUI");

        mSellThingBtn = (Button)findViewById(R.id.activity_sell_thing_sell_button);
        mSellThingBtn.setOnClickListener(this); // setup btn callback

        //*******************
        // SETUP REPO ACCESS
        //*******************

        mThingRepo = new FakeThingRepository(new JsonThingConverter());

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.activity_sell_thing) != null) {

            // if restored : do not create a new fragment
            if (savedInstanceState != null) {
                Log.d(TAG, "onCreate: Reloading GUI : reloading parameters");
                mDescription = savedInstanceState.getString(M_DESCRIPTION_BK,"");
                mPosition = (PositionObject) savedInstanceState.getSerializable(M_POSITION_BK);
                mPrice = (PriceObject) savedInstanceState.getSerializable(M_PRICE_BK);
                mPictURI = (URI) savedInstanceState.getSerializable(M_PICT_URI_BK);
                Log.d(TAG, "onCreate: Reloading GUI : parameters reloaded");
                return;
            }

            Log.d(TAG, "onCreate: creating fragments");
            // instantiate fragments for this activity
            mTakePictureFragment = new TakePictureFragment();
            mRecordPriceFragment = new RecordPriceFragment();
            mRecordDescriptionFragment = new RecordDescriptionFragment();
            mRecordPositionFragment = new RecordPositionFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // firstFragment.setArguments(getIntent().getExtras());

            Log.d(TAG, "onCreate: adding fragments");
            // Add the fragments to the 'fragment_container's FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sell_thing_take_picture_frame, mTakePictureFragment)
                    .add(R.id.activity_sell_thing_record_price_frame, mRecordPriceFragment)
                    .add(R.id.activity_sell_thing_record_description_frame, mRecordDescriptionFragment)
                    .add(R.id.activity_sell_thing_record_position_frame, mRecordPositionFragment)
                    .commit();
        }


    }


    /**
     * deprecated
     * keep a reference to edittexts objects for focus management
     * (when validating one edittext, to which edittext go...)
     */
    @Deprecated
    @Override
    protected void onStart() {
        super.onStart();
        //mPriceEditText = (EditText) findViewById(R.id.fragment_record_price_price_et);
        //mDescriptionEditText = (EditText)findViewById(R.id.fragment_record_description_description_et);
        //mPriceEditText.isFocusable();
    }

    /**
     * Save Position, Price, Description that we may have gathered from fragments already.
     * @param outState Bundle where we save this data.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(M_POSITION_BK, mPosition);
        outState.putSerializable(M_PRICE_BK, mPrice);
        outState.putString(M_DESCRIPTION_BK, mDescription);
        outState.putSerializable(M_PICT_URI_BK, mPictURI);
    }

    //************************************
    // GET OBJECTS GENERATED BY FRAGMENTS
    //************************************


    @Override
    public void onPictureTaken(URI pictureURI) {
        Log.d(TAG, "onPictureTaken: got Picture URI : "+ pictureURI);
        mPictURI = pictureURI;
    }

    @Override
    public void onPriceRecorded(PriceObject priceObject) {
        Log.d(TAG, "onPriceRecorded: got PriceObject :" + priceObject);
        //mPriceEditText.setFocusable(false);
        //mDescriptionEditText.requestFocus();
        mPrice = priceObject;
    }

    @Override
    public void onDescriptionRecorded(String description) {
        Log.d(TAG, "onDescriptionRecorded: got Description" + description);
        mDescription = description;
    }

    @Override
    public void onPositionRecorded(PositionObject position) {
        Log.d(TAG, "onPositionRecorded: got position" + position);
        mPosition = position;
    }



    //********************
    // CREATE A NEW THING
    //********************

    @Override
    public void onClick(View v) {
        Log.d(TAG, " values " + mPictURI + mPrice + mPosition);
        if (mPictURI == null || mPrice ==null || mPosition ==null){
            // todo provide user feedback
            Log.d(TAG, "onClick: tried to build invalid ThingObject : one param is null");
            Log.d(TAG, " values " + mPictURI + mPrice + mPosition);
            return;
        }


        //******************
        // IMAGE PROCESSING
        //******************

        // todo move that to another class

        // build snapshot from image file

        // create ThingPicture
        Bitmap resizedBM = decodeSampledBitmapFromPath(mPictURI.getPath(),
                ThingPicture.THUMBNAIL_WIDTH, ThingPicture.THUMBNAIL_HEIGHT);

        // create image id
        String pictureId = "fakePictureId_" + UUID.randomUUID().toString();
        // save ThingPicture
        ThingPicture fakePicture = new ThingPicture(pictureId, resizedBM);


        // todo in the future : put full size image in imagerepo (to store somewhere)






        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // start building a thingobject with available info
        ThingObject.ThingBuilder thingBuilder = new ThingObject.ThingBuilder()
                .thingId("NewThing_" + timeStamp)
                .description(mDescription)
                .position(mPosition)
                .price(mPrice)
                .pict(fakePicture); // todo finish working on picture model

        Log.d(TAG, "onClick: thingbuilder = " + thingBuilder);


        if (thingBuilder.isValid()){
            ThingObject newThing = thingBuilder.build();
            mThingRepo.add(newThing); // build a ThingObject and add it to the repo
            // todo : reset views etc...
            Log.d(TAG, "onClick: new ThingObject added to repo : " + newThing);
        } else { // todo provide user feedback if object is not valid
            Log.d(TAG, "onClick: tried to build invalid ThingObject");
        }
    }


}
