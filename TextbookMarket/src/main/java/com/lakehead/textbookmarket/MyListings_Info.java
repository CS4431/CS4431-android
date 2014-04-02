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

public class MyListings_Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylistings__info);
        Intent intent = getIntent();
        Listing myListing =intent.getParcelableExtra("listings");
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
        change listing price(button)
        remove listings(button)
         */

        //((ImageView)findViewById(R.id.image)).setImageBitmap(myListing.get_book().getBitmap());
        //((TextView) findViewById(R.id.BookTitle)).setText("Title: ");// + myListing.get_price());



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mylistings__info, menu);
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