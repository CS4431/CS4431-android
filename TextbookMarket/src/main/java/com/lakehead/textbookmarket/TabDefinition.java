package com.lakehead.textbookmarket;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

/**
 * Created by Master on 2/10/14.
 */
    public abstract class TabDefinition {

    private final int _tabContentViewId;
    private final String _tabUuid;

    public TabDefinition(int tabContentViewId){
        _tabContentViewId = tabContentViewId;
        _tabUuid = UUID.randomUUID().toString();
    }

    public int get_tabContentViewId(){
        return _tabContentViewId;
    }

    public String getId(){
        return _tabUuid;
    }

    public abstract Fragment getFragment();

    public abstract View createTabView(LayoutInflater inflater, ViewGroup tabsView);
}
