package com.lakehead.textbookmarket;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    Fragment booksFragment;
    Fragment coursesFragment;
    Fragment listingsFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        booksFragment = new BooksFragment();
        coursesFragment = new CoursesFragment();
        listingsFragment = new ListingsFragment();
    }



    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return coursesFragment;
            case 1:
                return listingsFragment;
            case 2:
                return booksFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
