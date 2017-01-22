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


public class ListThingsFragment extends Fragment {
    private static String TAG = "ListThingsFragment";
    
    private static String THING_REPOSITORY_URI_BUNDLEKEY = "thing_repo_URI";

    String mRepoURI;
    FakeThingRepository mThingRepo; // todo replace by Repository<ThingObject>

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    List<ThingObject> mThingList;

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
        mThingRepo.populateFakeThings(); // well this is cheating


        // ***********************************************

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        // refresh data here to handle coming back from other actovity (ugly)
        // todo do better
        Log.d(TAG, "onResume : refreshing data from repo"); //suboptimal...
        mThingList = mThingRepo.queryAll();
        Log.d(TAG, "onResume : notify dataset has changed");
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: refreshing data from repo");
        mThingList = mThingRepo.queryAll();

        Log.d(TAG, "onCreateView: creating fragment view");

        // mRepoURI can be used here to instanciante a repository ?

        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_list_things, container, false);

        //mRecyclerView = (RecyclerView) findViewById(R.id.thing_list_rv); // seems expensive

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ThingObjectAdapter itemsAdapter =
                new ThingObjectAdapter(mThingList);
        //(this, R.layout.thing_vignette, objectsAround);
        mRecyclerView.setAdapter(itemsAdapter);

       return mRecyclerView;
    }

}
