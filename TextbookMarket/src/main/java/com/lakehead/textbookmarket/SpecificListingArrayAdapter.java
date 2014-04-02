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

import java.util.List;


public class SpecificListingArrayAdapter extends ArrayAdapter {
    Context mContext;
    int layoutResourceId;
    ObjectItem data[] = null;

    public SpecificListingArrayAdapter(Context mContext, ObjectItem[] data) {
        super(mContext, R.layout.activity_specific_listing_fragment, data);
        this.layoutResourceId = R.layout.activity_specific_listing_fragment;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        ObjectItem objectItem = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(objectItem.itemName);
        textViewItem.setTag(objectItem.itemId);

        return convertView;

    }

}