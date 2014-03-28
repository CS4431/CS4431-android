package com.lakehead.textbookmarket;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;

        import java.util.ArrayList;

/**
 * Handles the logic of switching between tabs.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    Fragment booksFragment;
    Fragment coursesFragment;
    Fragment listingsFragment;
    Fragment myListingsFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        booksFragment = new BooksFragment();
        coursesFragment = new CoursesFragment();
        listingsFragment = new ListingsFragment();
        myListingsFragment = new MyListingsFragment();
    }

    public void executeSearch(String query){
        ((BooksFragment)booksFragment).executeSearch(query);
        ((CoursesFragment)coursesFragment).executeSearch(query);
        ((ListingsFragment)listingsFragment).executeSearch(query);
        ((MyListingsFragment)myListingsFragment).executeSearch(query);

    }





    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return coursesFragment;
            case 1:
                return booksFragment;
            case 2:
                return listingsFragment;
            case 3:
                return myListingsFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}
