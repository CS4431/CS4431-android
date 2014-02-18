package com.lakehead.textbookmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Master on 2/17/14.
 */
public class ListingArrayAdapter extends ArrayAdapter<Listing> {
    private Listing[] listings;
    private Context context;

    public ListingArrayAdapter(Context context, Listing[] listings){
        super(context, R.layout.books_item_view,  listings);
        this.listings = listings;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listings_item_view, parent, false);

        TextView listingTitleView = (TextView)rowView.findViewById(R.id.listingTitle);


        //TODO gotta figure out best way to grab relevant book. API or use or our own records?
        //listingTitleView.setText(listings[position].get_book_id());
        listingTitleView.setText("TEST");


        TextView listingCourseView = (TextView)rowView.findViewById(R.id.listingCourseInfo);
        listingCourseView.setText(listings[position].get_start_date());

        TextView listingPriceView = (TextView)rowView.findViewById(R.id.listingPrice);
        listingPriceView.setText("Price: $" + listings[position].get_price());

        return rowView;

    }

}
