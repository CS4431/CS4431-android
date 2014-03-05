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

public class Book_Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);
        /*
        int bid = bookAdapter.getItem(position).get_id();
                int bedition = bookAdapter.getItem(position).get_edition();
                String bTitle = bookAdapter.getItem(position).get_title();
                String bisbn = bookAdapter.getItem(position).get_isbn();
                String bauthor = bookAdapter.getItem(position).get_author();
                String bpublisher = bookAdapter.getItem(position).get_publisher();
                String bcover = bookAdapter.getItem(position).get_cover();
                String burl = bookAdapter.getItem(position).get_image_url();
                */
        Intent intent = getIntent();
        int bid = intent.getIntExtra("bint",0);
        int bedition = intent.getIntExtra("bedition",0);
        String title = intent.getStringExtra("Title");
        String bisbn = intent.getStringExtra("bisbn");
        String bauthor = intent.getStringExtra("bauthor");
        String bpublisher = intent.getStringExtra("bpublisher");
        String bcover = intent.getStringExtra("bcover");
        Bitmap myBit = intent.getParcelableExtra("bimage");
        ((ImageView)findViewById(R.id.image)).setImageBitmap(myBit);
        ((TextView) findViewById(R.id.BookTitle)).setText("Title: " + title);
        ((TextView)findViewById(R.id.Author)).setText("Author: " + bauthor);
        ((TextView)findViewById(R.id.ISBN)).setText("ISBN: " + bisbn);
        ((TextView)findViewById(R.id.edition)).setText("Edition: " + Integer.toString(bedition));
        ((TextView)findViewById(R.id.coverType)).setText("Cover Style: " + bcover);
        ((TextView)findViewById(R.id.Publisher)).setText("Publisher: " + bpublisher);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book__info, menu);
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
