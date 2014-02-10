package com.lakehead.textbookmarket;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

/**
 * Created by Master on 2/10/14.
 */
public class TabsFragment extends Fragment implements TabHost.OnTabChangeListener {
    private TabDefinition[] tab_definitions = new TabDefinition[] {
            new CoursesTab(R.id.tab1, R.layout.simple_tab, R.string.tab_title_1, R.id.tabTitle, new Fragment() ),
            new CoursesTab(R.id.tab2, R.layout.simple_tab, R.string.tab_title_2, R.id.tabTitle, new Fragment() ),
            new CoursesTab(R.id.tab3, R.layout.simple_tab, R.string.tab_title_3, R.id.tabTitle, new Fragment() )
    };

    private View _rootView;
    private TabHost _tabHost;


    @Override
    public void onTabChanged(String tabId) {
        for(TabDefinition tab : tab_definitions){
            if(tabId != tab.getId()){
                continue;
            }
            updateTab(tabId, tab.getFragment(), tab.get_tabContentViewId());
            return;
        }
        throw new IllegalArgumentException("The specified Tab id: " + tabId + " does not exist.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        _rootView = inflater.inflate(R.layout.fragment_tabs, null);

        _tabHost = (TabHost)_rootView.findViewById(android.R.id.tabhost);
        _tabHost.setup();

        for(TabDefinition tab : tab_definitions){
            _tabHost.addTab(createTab(inflater, _tabHost, _rootView, tab));
        }
        return _rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
        _tabHost.setOnTabChangedListener(this);

        if(tab_definitions.length > 0){
            onTabChanged(tab_definitions[0].getId());
        }

    }


    private TabHost.TabSpec createTab(LayoutInflater inflater, TabHost tabHost, View root, TabDefinition tabDefinition){
        ViewGroup tabsView = (ViewGroup)root.findViewById(android.R.id.tabs);
        View tabView = tabDefinition.createTabView(inflater, tabsView);

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabDefinition.getId());
        tabSpec.setIndicator(tabView);
        tabSpec.setContent(tabDefinition.get_tabContentViewId());
        return tabSpec;
    }

    private void updateTab(String tabId, Fragment fragment, int containerId){
        final android.support.v4.app.FragmentManager manager = getFragmentManager();
        if(manager.findFragmentByTag(tabId) == null){
            manager.beginTransaction().replace(containerId, fragment,tabId).commit();
        }

    }
}
