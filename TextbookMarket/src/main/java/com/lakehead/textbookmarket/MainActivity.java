package com.lakehead.textbookmarket;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Primary Activity Holding the tabs managed by TabsPagerAdapter , it also handles the action bar.
 */
public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

    public static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Courses", "Books", "Listings", "My Listings" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        //added to constantly keep all fragments in memory.
        viewPager.setOffscreenPageLimit(3);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */

         viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }



            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                Log.i(TAG, "SEARCH EXPANDED.");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Log.i(TAG, "SEARCH COLLAPSED");
                return true;
            }
        });

        SearchView searchView = (SearchView)searchItem.getActionView();
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        if(searchManager != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName("com.lakehead.textbookmarket", "com.lakehead.textbookmarket.SearchResultsActivity")));
            searchView.setQueryHint("Book/Course");
            searchView.setIconifiedByDefault(true);
        }



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
        else if(id == R.id.action_search){
            Log.i(TAG, "onOptionsItemSelected: " + "Search Selected!");
        }
        else if(id == R.id.action_new_listing){
            openIsbnSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openIsbnSearch() {
        Intent inputISBNIntent = new Intent(this, InputISBNActivity.class);
        startActivity(inputISBNIntent);

    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * Method is called when the Settings button is selected from the optionMenu; Starts new SettingsActivity.
     */
    private void openSettings(){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

}