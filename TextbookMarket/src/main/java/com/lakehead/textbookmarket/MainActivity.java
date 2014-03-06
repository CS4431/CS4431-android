package com.lakehead.textbookmarket;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Courses", "Listings", "Books" };
    private Handler mHandler;
    private ConnectivityManager conn;
    private android.net.NetworkInfo wifi;
    private android.net.NetworkInfo mobile;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        conn = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        wifi =  conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobile =  conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        progress = new ProgressDialog(this);
        progress.setTitle("No Internet");
        progress.setMessage("Scanning for Wifi or Cellular Data");
        progress.setCancelable(false);

        if (!wifi.isAvailable()&&!mobile.isAvailable()) {
            progress.show();
            startRepeatingTask();
        } else {


        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
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
        });}
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            mHandler.postDelayed(mStatusChecker, 5000);
            progress.show();
            conn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            wifi =  conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobile =  conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (!wifi.isAvailable()&&!mobile.isAvailable()) {
                //Log.d("Debug", "NO INTERNET wifi");

            } else {
                //Log.d("Debug","WIFI OR DATA DETECTED");
                Intent goToMain = new Intent(getBaseContext()  , MainActivity.class);
                startActivity(goToMain);
                stopRepeatingTask();
                progress.dismiss();
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
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

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
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
            Log.i("MainActivity", "onOptionsItemSelected: " + "Search Selected!");
        }
        else if(id == R.id.action_new_listing){
            openIsbnSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openIsbnSearch() {
        Intent addListingIntent = new Intent(this, AddListingActivity.class);
        startActivity(addListingIntent);

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