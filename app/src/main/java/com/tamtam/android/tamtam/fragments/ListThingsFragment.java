/*
 *     Android application to create and display localized objects.
 *     Copyright (C) 2017  pascal bodin, antonin perrot-audet
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tamtam.android.tamtam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamtam.android.tamtam.R;
import com.tamtam.android.tamtam.ThingObjectAdapter;
import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.json.JsonThingConverter;
import com.tamtam.android.tamtam.services.repository.FakeThingRepository;

import java.util.List;

// TODO DEBUG

/**
 * have a look at :
 * http://square.github.io/picasso/
 * may help doing things the easy way ?
 * look at licensing as well
 *
 * this as well :
 * http://stackoverflow.com/questions/26890190/images-in-recyclerview
 */
public class ListThingsFragment extends Fragment {
    private static String TAG = "ListThingsFragment";
    
    private static String THING_REPOSITORY_URI_BUNDLEKEY = "thing_repo_URI";

    String mRepoURI;
    FakeThingRepository mThingRepo; // todo replace by Repository<ThingObject>

    RecyclerView mRecyclerView = null;
    LinearLayoutManager mLayoutManager = null;
    ThingObjectAdapter mItemsAdapter = null;
    List<ThingObject> mThingList = null; // to remove ?
    private boolean mDataRefreshed = false; // have we already refreshed data for this lifecycle ?

    // Factory constructor to save arguments to the Bundle
    public static ListThingsFragment newInstance(String repoURI) {
        ListThingsFragment myFragment = new ListThingsFragment();

        // mRepoURI will be initialized in the onCreate Method using savedInstanceState
        Bundle args = new Bundle();
        args.putString(THING_REPOSITORY_URI_BUNDLEKEY, repoURI);
        myFragment.setArguments(args);// todo use it
        return myFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // trick to pass arguments to fragments
        // todo use it
        if (getArguments() != null) {
            mRepoURI = getArguments().getString(THING_REPOSITORY_URI_BUNDLEKEY);
        }


        Log.d(TAG, "onCreate: Creating Fragment, data fetching");

        //***************
        // DATA FETCHING
        //***************
        // maybe should be async

        // declare the repository
        JsonThingConverter jsonConverter = new JsonThingConverter();
        mThingRepo = new FakeThingRepository(jsonConverter);
        // todo remove this fake data
        //mThingRepo.populateFakeThings(); // well this is cheating


        // ***********************************************

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: refreshing data from repo");
        //mThingList = mThingRepo.queryAll();

        Log.d(TAG, "onCreateView: creating fragment view");

        // mRepoURI can be used here to instanciante a repository ?

        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_list_things, container, false);

        //mRecyclerView = (RecyclerView) findViewById(R.id.thing_list_rv); // seems expensive

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Log.d(TAG, "onCreateView: refresh data");
        mItemsAdapter = new ThingObjectAdapter(mThingRepo.getAll());
        // data is all fresh from creation
        mDataRefreshed = true;
        Log.d(TAG, "onCreateView: set mDataRefreshed flag to true");

        //(this, R.layout.thing_vignette, objectsAround);
        mRecyclerView.setAdapter(mItemsAdapter);

       return mRecyclerView;
    }



    @Override
    public void onResume() {
        super.onResume();
        // was data refreshed by onCreateView ?
        if (!mDataRefreshed){
            // refresh data here to handle coming back from other actovity (ugly)
            // todo do better
            Log.d(TAG, "onResume : refresh data from repo"); //suboptimal...
            // todo remove ref to mThingList
            //mThingList.clear();
            //mThingList.addAll(mThingRepo.queryAll());
            mItemsAdapter.swap(mThingRepo.getAll());
            Log.d(TAG, "onResume: set mDataRefreshed to true");
            mDataRefreshed = true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        // when we start exiting/hiding this view, consider that the data is not fresh
        mDataRefreshed = false;
        Log.d(TAG, "onPause: set mDataRefreshed to false");
    }
}
