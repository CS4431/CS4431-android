package com.lakehead.textbookmarket;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import java.util.HashSet;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.settings, menu);
        //We need no menu here, so the inflation is commented out.
        return true;
    }

    /**
     * Our settings fragment that gets loaded by the main settings activity
     */
    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private static final String TAG = "SettingsActivity";

        public SettingsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.i(TAG, "Fired up Fragment...");

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_general);
            PreferenceManager.setDefaultValues(this.getActivity(), R.xml.pref_general, false);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


            //TODO this commented section is the start of programmatic modification of the list. Eventually we want to load all the departments by API call.
            /*
            MultiSelectListPreference listPreference = (MultiSelectListPreference)findPreference("department_list");
            String[] entries = {"one","two"};
            String[] entryValues = {"1","2"};
            listPreference.setEntries(entries);
            listPreference.setEntryValues(entryValues);
            listPreference.setValues(new HashSet<String>());
            */


        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.i(TAG, "onSharedPreferenceChanged() -> " + "Key found: " + key + ". Values: " + sharedPreferences.getStringSet(key, new HashSet<String>()));
        }
    }

}
