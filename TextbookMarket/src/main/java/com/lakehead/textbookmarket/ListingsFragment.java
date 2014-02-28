package com.lakehead.textbookmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * The fragment used in the "Listings" tab of MainActivity
 */
public class ListingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);
        ListView listingListView = (ListView)rootView.findViewById(R.id.course_list_view);

        Listing listing1 = new Listing(1,2,1,21.00, "Dec 15, 2013","April 30, 2014");
        Listing listing2 = new Listing(2,2,1,20.00, "Dec 10, 2013","April 30, 2014");
        Listing listing3 = new Listing(3,2,2,100.00, "Dec 31, 2013","April 30, 2014");
        Listing listing4 = new Listing(4,2,3,35.00, "Jan 1, 2014","April 30, 2014");
        Listing listing5 = new Listing(5,2,3,30.00, "Jan 15, 2014","April 30, 2014");
        Listing listing6 = new Listing(6,2,4,20.00, "Jan 16, 2014","April 30, 2014");
        Listing listing7 = new Listing(7,2,6,55.00, "Feb 1, 2014","April 30, 2014");
        Listing listing8 = new Listing(8,2,6,45.00, "Feb 14, 2014","April 30, 2014");

        Listing[] listingList = new Listing[]{listing1,listing2, listing3, listing4, listing5, listing6, listing7, listing8};

        final ListingArrayAdapter listingAdapter = new ListingArrayAdapter(this.getActivity(), listingList);
        listingListView.setAdapter(listingAdapter);

        return rootView;
    }
}
