package com.lakehead.textbookmarket;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);
        }


        final ListView courseList = (ListView)findViewById(R.id.courseListView);
        Book tempBook1 = new Book(this, "C How to Program", "4th", "Deitel&Deitel", R.drawable.book1);
        Book tempBook2 = new Book(this, "Operating Systems", "3rd", "Frank Allaire", R.drawable.book2);
        Book tempBook3 = new Book(this, "Database Management", "4th", "Francis Allairington", R.drawable.book3);
        Book tempBook4 = new Book(this, "Game Design Patterns", "2nd", "Klein & Co.", R.drawable.book4);
        Book tempBook5 = new Book(this, "Compiler Design", "1st", "Dragon", R.drawable.book2);
        Book tempBook6 = new Book(this, "Object-Oriented Design", "4th", "Deitel&Deitel", R.drawable.book1);
        Book tempBook7 = new Book(this, "Why's Poignant Guide to Ruby", "1st", "_why the lucky stiff", R.drawable.book4);
        Book[] bookList = new Book[] {tempBook1, tempBook2, tempBook3, tempBook4, tempBook5, tempBook6, tempBook7};



        final BookArrayAdapter bookAdapter = new BookArrayAdapter(this, bookList);
        courseList.setAdapter(bookAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * Method is called when the Settings button is selected from the optionMenu; Starts new SettingsActivity.
     */
    public void openSettings(){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


}
