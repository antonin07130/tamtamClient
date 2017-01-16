package com.tamtam.android.tamtam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.json.JsonThingConverter;
import com.tamtam.android.tamtam.services.repository.FakeThingRepository;

import java.util.List;



public class ListThingsFragment extends Fragment {

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
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRepoURI = getArguments().getString(THING_REPOSITORY_URI_BUNDLEKEY);
        }





        //***************
        // DATA FETCHING
        //***************
        // maybe should be async

        // declare the repository
        JsonThingConverter jsonConverter = new JsonThingConverter();
        mThingRepo = new FakeThingRepository(jsonConverter,jsonConverter);
        mThingRepo.populateFakeThings(); // well this is cheating

        // fetch things around me from the repository.
        mThingList = mThingRepo.queryAll();
        // ***********************************************

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
