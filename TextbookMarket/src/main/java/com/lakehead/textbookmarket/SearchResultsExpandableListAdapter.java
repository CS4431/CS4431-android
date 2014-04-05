package com.lakehead.textbookmarket;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Master on 3/28/14.
 */
public class SearchResultsExpandableListAdapter extends BaseExpandableListAdapter{

    private static final String TAG = "SearchResultsExpandableListAdapter";
    private Context context;
    private HashMap<String, ArrayList> resultsHash;
    private ArrayList<String> groupHeaders;
    private final int THUMBNAIL_SIZE = 96;


    public SearchResultsExpandableListAdapter(Context context, ArrayList<String> groupHeaders,
                                              HashMap<String, ArrayList> resultsHash){

        this.resultsHash = resultsHash;
        this.groupHeaders = groupHeaders;
        this.context = context;
    }
    @Override
    public int getGroupCount() {
        return this.groupHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.resultsHash.get(this.groupHeaders.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groupHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = groupHeaders.get(groupPosition);
        return resultsHash.get(key).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.search_results_item_group, null);
        }

        TextView headerTextView = (TextView) convertView.findViewById(R.id.headers_textview);
        headerTextView.setTypeface(null, Typeface.BOLD);
        headerTextView.setTextSize(30);
        headerTextView.setGravity(Gravity.CENTER_VERTICAL);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        Object childElement = resultsHash.get(groupHeaders.get(groupPosition)).get(childPosition);
        if(groupPosition == groupHeaders.indexOf("Books"))
        {
            Log.i(TAG,"getChildView() -> " + "Type of Node is Book, populating row...");
              Book book = (Book)childElement;
              rowView = inflater.inflate(R.layout.books_item_view, parent, false);
            TextView titleTextView = (TextView)rowView.findViewById(R.id.bookTitle);
            titleTextView.setText(book.get_title());

            ImageView iconImageView = (ImageView)rowView.findViewById(R.id.icon);

            //If the book doesn't have a bitmap yet, then fetch it. Otherwise, just display the one we have
            if(book.getBitmap() == null)
            {
                new GetImageTask(book.get_image_url(), iconImageView, THUMBNAIL_SIZE, THUMBNAIL_SIZE, book).execute();
            }
            else
            {
                iconImageView.setImageBitmap(book.getBitmap());
            }

            TextView detailTextView = (TextView)rowView.findViewById(R.id.bookInfo);
            detailTextView.setText(book.get_author());

        }
        else if(groupPosition == groupHeaders.indexOf("Courses"))
        {
            Log.i(TAG,"getChildView() -> " + "Type of Node is Course, populating row...");
            Course course = (Course)childElement;
            rowView = inflater.inflate(R.layout.courses_item_view, parent, false);
            TextView titleTextView = (TextView)rowView.findViewById(R.id.courseTitle);
            TextView sectionTextView = (TextView)rowView.findViewById(R.id.courseInfo);
            TextView instructorTextView = (TextView)rowView.findViewById(R.id.courseInstructor);

            titleTextView.setText(course.get_title());
            sectionTextView.setText( course.get_section() );
            instructorTextView.setText( course.get_instructor() );


        }
        else if(groupPosition == groupHeaders.indexOf("Listings"))
        {
            Listing listing = (Listing)childElement;
            Log.i(TAG,"getChildView() -> " + "Type of Node is Listing, populating row...");
            rowView = inflater.inflate(R.layout.listings_item_view, parent, false);

            //grabbing XML elements
            TextView listingTitleView = (TextView)rowView.findViewById(R.id.listingTitle);
            TextView listingCourseView = (TextView)rowView.findViewById(R.id.listingCourseInfo);
            TextView listingPriceView = (TextView)rowView.findViewById(R.id.listingPrice);
            ImageView bookIconImageView = (ImageView)rowView.findViewById(R.id.listing_icon);

            //getting the listing's book for future usage.
            Book relatedBook = listing.get_book();
            if(relatedBook != null)
            {
                Log.i(TAG," getChildView() -> " + "Book Found for listing {" + listing.get_id()+"} : " + relatedBook.toString());

                //populating XML elements
                listingTitleView.setText(relatedBook.get_title());
                listingPriceView.setText("Price: $" + listing.get_price());
                listingCourseView.setText(listing.get_start_date());

                //If the book doesn't have a bitmap yet, then fetch it. Otherwise, just display the one we have
                if(listing.get_book().getBitmap() == null)
                {
                    new GetImageTask(relatedBook.get_image_url(), bookIconImageView, THUMBNAIL_SIZE, THUMBNAIL_SIZE,relatedBook).execute();
                }
                else
                {
                    bookIconImageView.setImageBitmap(relatedBook.getBitmap());
                }
            }
        }
        else
        {
            rowView = null;
        }

        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
