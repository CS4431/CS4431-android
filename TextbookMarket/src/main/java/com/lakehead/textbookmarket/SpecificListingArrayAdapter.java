package com.lakehead.textbookmarket;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SpecificListingArrayAdapter extends ArrayAdapter {
    Context mContext;
    int layoutResourceId;
    ArrayList<Listing> mylisting;

    public SpecificListingArrayAdapter(Context mContext,ArrayList<Listing> myBooks) {
        super(mContext, R.layout.activity_specific_listing_fragment, myBooks);
        this.layoutResourceId = R.layout.activity_specific_listing_fragment;
        this.mContext = mContext;
        this.mylisting = myBooks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position

        Listing tempListing = mylisting.get(position);
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(Double.toString(tempListing.get_price()));
        textViewItem.setTag(tempListing.get_id());

        return convertView;

    }

}