package com.lakehead.textbookmarket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Listing_Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing__info);
        Intent intent = getIntent();
        Listing myListing = intent.getParcelableExtra("listings");
        /*
        String get_end_date()
        String get_start_date()
        double get_price()
        //Book get_book()
        int get_book_id()
        int get_user_id()
        int get_id()
         */

        /*
        Book info
        listing price
        date listed
        date expired
        message seller(button)
         */

        ((ImageView)findViewById(R.id.image)).setImageBitmap(myListing.get_book().getBitmap());
        ((TextView) findViewById(R.id.BookTitle)).setText(myListing.get_book().get_title());
        ((TextView)findViewById(R.id.Author)).setText("Author: " + myListing.get_book().get_author());
        ((TextView)findViewById(R.id.ISBN)).setText("ISBN: " + myListing.get_book().get_isbn());
        ((TextView)findViewById(R.id.edition)).setText("Edition: " + Integer.toString(myListing.get_book().get_edition()));
        ((TextView)findViewById(R.id.coverType)).setText("Cover Style: " + myListing.get_book().get_cover());
        ((TextView)findViewById(R.id.Publisher)).setText("Publisher: " + myListing.get_book().get_publisher());
        ((TextView)findViewById(R.id.ListingPrice)).setText("Listing Price: $" + myListing.get_price() + "0");
        ((TextView)findViewById(R.id.DateListed)).setText("Date Listed: " + myListing.get_start_date().substring(8,10) + "/" + myListing.get_start_date().substring(5,7) + "/" + myListing.get_start_date().substring(0,4));
        ((TextView)findViewById(R.id.DateExpires)).setText("Date Expires: " + myListing.get_end_date().substring(8,10) + "/" + myListing.get_end_date().substring(5,7) + "/" + myListing.get_end_date().substring(0,4));
        //((TextView)findViewById(R.id.Seller)).setText("Seller: " + myListing.get_user_id());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listing__info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}