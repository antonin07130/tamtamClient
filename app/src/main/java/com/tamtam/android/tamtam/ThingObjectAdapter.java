package com.tamtam.android.tamtam;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.image.BitmapUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import java.util.Currency;

import static android.R.attr.data;

/**
 * Created by antoninpa on 24/12/16.
 * followed these guidelines :
 * https://developer.android.com/training/material/lists-cards.html
 */

/**
 * This adapter works for showing small lists of {@link ThingObject}
 * (it relies in an on memory list of Things).
 * The list can be replaced by another by using the swap command.
 * The same interal list is reused preventing a garbage collection.
 */
public class ThingObjectAdapter extends RecyclerView.Adapter<ThingObjectAdapter.ViewHolder> {

     private List<ThingObject> mDataset;


        public ThingObjectAdapter(List<ThingObject> objects) {
            mDataset = objects;
        }

        public void swap(List<ThingObject> objects){
            mDataset.clear();
            mDataset.addAll(objects);
            notifyDataSetChanged();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ThingObjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.thing_vignette, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ThingObject currentThing = mDataset.get(position);
            holder.mDescriptionTV.setText(currentThing.getDescription());
            // picture size can vary depending on a parameter (distance, popularity...)
            // holder.mPictIV.setMinimumHeight((int)(currentThing.getValue().getValue()*10));
            holder.mPriceTV.setText(currentThing.getPrice().getValue() +
                    " " + currentThing.getPrice().getCurrency().getSymbol());
            if (currentThing.hasPict()) {
                holder.mPictIV.setImageBitmap(currentThing.getPict().getPicureBitmap());
                //holder.mPictIV.setImageResource(R.mipmap.ic_launcher);
                holder.mPictIV.setVisibility(View.VISIBLE);
            } else {
                holder.mPictIV.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    // Provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mView;
        public TextView mDescriptionTV;
        public TextView mPriceTV;
        public AppCompatImageView mPictIV;
        public ViewHolder(View v) {
            super(v);
            mView = (CardView) v.findViewById(R.id.vignette_thing_cv);
            mDescriptionTV = (TextView) v.findViewById(R.id.vignette_thing_description_tv);
            mPriceTV = (TextView) v.findViewById(R.id.vignette_thing_price_tv);
            mPictIV = (AppCompatImageView) v.findViewById(R.id.vignette_thing_picture_iv);
        }
    }















    }
