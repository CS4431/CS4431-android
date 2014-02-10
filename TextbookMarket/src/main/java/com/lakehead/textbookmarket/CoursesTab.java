package com.lakehead.textbookmarket;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Master on 2/10/14.
 */
public class CoursesTab extends TabDefinition {

    private final int _tabTitleResourceId;
    private final int _tabTitleViewId;
    private final int _tabLayoutId;
    private final Fragment _fragment;

    public CoursesTab(int tabContentViewId, int tabLayoutId, int tabTitleResourceId, int tabTitleViewId, Fragment fragment) {
        super(tabContentViewId);

        _tabLayoutId = tabLayoutId;
        _tabTitleResourceId = tabTitleResourceId;
        _tabTitleViewId = tabTitleViewId;
        _fragment = fragment;
    }

    @Override
    public Fragment getFragment() {
        return _fragment;
    }

    @Override
    public View createTabView(LayoutInflater inflater, ViewGroup tabsView) {
        View indicator = inflater.inflate(_tabLayoutId, tabsView, false);

        TextView titleView = (TextView)indicator.findViewById(_tabTitleViewId);
        titleView.setText(_tabTitleResourceId);
        titleView.setGravity(Gravity.CENTER);

        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        //        LinearLayout.LayoutParams.WRAP_CONTENT,
         //       LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                250);
        layoutParams.weight = 1;
        indicator.setLayoutParams(layoutParams);

        return indicator;
    }
}
