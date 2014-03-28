package com.lakehead.textbookmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The fragment used in the "Listings" tab of MainActivity
 */
public class ListingsFragment extends Fragment implements OnTaskCompleted{
    ListView listingsListView;
    View rootView;
    JSONArray jArray;
    int currentOffset=0;
    boolean loadingMore=false;
    ArrayList<Listing> listingsList;
    ListingArrayAdapter listingsAdapter;

    //A dummy listing used to tell the adapter to add a "loading" row
    Listing loadingListing;

    public void executeSearch(String query)
    {
        Log.i("ListingsFragment", "executeSearch() -> Query Received: " + query);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_listings, container, false);
        listingsListView = (ListView)rootView.findViewById(R.id.listings_list_view);
        loadingListing = new Listing();
        listingsList= new ArrayList<Listing>();
        listingsAdapter = new ListingArrayAdapter(this.getActivity(), listingsList);
        listingsListView.setAdapter(listingsAdapter);
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "10");
        new GetJSONArrayTask(this, "/api/sell").execute(ext, count);

        listingsListView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            //dumdumdum
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                Log.d("Debug",""+lastInScreen);
                //is the bottom item visible & not loading more already? Load more!
                if((lastInScreen == totalItemCount) && !(loadingMore)){
                    currentOffset+=10;
                    loadingMore=true;
                    addLoadingListing();
                    //progressBar.setVisibility(View.VISIBLE);
                    makeAPICall();
                }
            }
        });
        

        if(savedInstanceState != null)
        {
            Log.d("ListingsFragment", "onCreateView() -> " + "Found Saved Instance state. Loading Course list from it...");
            listingsList = savedInstanceState.getParcelableArrayList("listingsList");
            ListingArrayAdapter listingsAdapter = new ListingArrayAdapter(this.getActivity(), listingsList);
            listingsListView.setAdapter(listingsAdapter);
        }
        else
        {
            Log.d("ListingsFragment", "onCreateView() -> " + "No Saved Instance state. Loading Course list from API...");
            makeAPICall();
        }
        return rootView;
    }


    private void addLoadingListing()
    {

        listingsList.add(loadingListing);
        listingsAdapter.notifyDataSetChanged();
    }

    private void removeLoadingListing()
    {
        listingsList.remove(loadingListing);
        listingsAdapter.notifyDataSetChanged();
    }

    private void makeAPICall()
    {
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "10");
        NameValuePair offset = new BasicNameValuePair("offset", Integer.toString(currentOffset));
        new GetJSONArrayTask(this, "/api/sell").execute(ext, count, offset);
    }


    @Override
    public void onPause()
    {
        Log.d("ListingsFragment", "onPause() -> " + "paused fragment.");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.d("ListingsFragment", "onResume() -> " + "resumed fragment.");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d("ListingsFragment", "onSaveInstanceState() -> " + "state saved for fragment.");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("listingsList", listingsList);


    }

    @Override
    public void onTaskCompleted(Object obj) {
        ArrayList<Book> temporaryBookList = new ArrayList<Book>();

        jArray = (JSONArray)obj;
        Log.i("ListingsFragment","jArray length -> " + jArray.length());

        String nodeType;
        JSONObject nodeData;

        try{
            for(int i = 0 ; i < jArray.length(); i++ ){

                nodeType = jArray.getJSONObject(i).getString("kind");
                nodeData = jArray.getJSONObject(i).getJSONObject("data");

                //Log.v("ListingsFragment", "OnTaskCompleted() -> Kind is: " + nodeType);
                if(nodeType.equals("sell"))
                {
                    Log.v("ListingsFragment", "OnTaskCompleted() -> Sell Data Polled -> " + nodeData.toString());
                    listingsList.add(Listing.generateListingFromJSONNode(nodeData));
                }
                else if(nodeType.equals("book"))
                {
                    Log.v("ListingsFragment", "OnTaskCompleted() -> Book Data Polled -> " + nodeData.toString());
                    temporaryBookList.add(Book.generateBookFromJSONNode(nodeData));
                }
                else
                {
                    Log.e("ListingsFragment", "OnTaskCompleted() -> NODE IS NEITHER BOOK NOR SELL!!!! Node Data is: " + nodeData.toString());
                }
            }
            //Final step of associating the listings with their actual books objects.
            Listing.associateBooksToListings(listingsList, temporaryBookList);

        }catch(Exception e){
            Log.e("ListingsFragment", "OnTaskCompleted() -> HighLevel Catch -> "+ e.toString());
            e.printStackTrace();
        }


        removeLoadingListing();
        listingsAdapter.notifyDataSetChanged();
        loadingMore=false;
    }
}