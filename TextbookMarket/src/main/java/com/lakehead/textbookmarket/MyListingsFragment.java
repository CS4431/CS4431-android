package com.lakehead.textbookmarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The fragment used in the "My Listings" tab of MainActivity
 */
public class MyListingsFragment extends Fragment implements OnTaskCompleted{
    public static final String TAG = "MyListingsFragment";
    ListView listingsListView;
    View rootView;
    JSONArray jArray;

    SharedPreferences prefs;
    String tokenString;
    int currentOffset=0;
    boolean loadingMore=false;

    ArrayList<Listing> listingsList;
    MyListingsArrayAdapter listingsAdapter;

    //A dummy listing used to tell the adapter to add a "loading" row
    Listing loadingListing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_listings, container, false);
        listingsListView = (ListView)rootView.findViewById(R.id.listings_list_view);
        prefs = this.getActivity().getSharedPreferences("com.lakehead.textbookmarket", Context.MODE_PRIVATE);
        tokenString = prefs.getString("remember_token", "");
        loadingListing = new Listing();
        listingsList= new ArrayList<Listing>();
        listingsAdapter = new MyListingsArrayAdapter(this.getActivity(), listingsList);
        listingsListView.setAdapter(listingsAdapter);

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
            Log.d(TAG, "onCreateView() -> " + "Found Saved Instance state. Loading Course list from it...");
            listingsList = savedInstanceState.getParcelableArrayList("listingsList");
            MyListingsArrayAdapter listingsAdapter = new MyListingsArrayAdapter(this.getActivity(), listingsList);
            listingsListView.setAdapter(listingsAdapter);
        }
        else
        {
            Log.d(TAG, "onCreateView() -> " + "No Saved Instance state. Loading Course list from API...");
            NameValuePair ext = new BasicNameValuePair("ext", "json");
            NameValuePair count = new BasicNameValuePair("count", "20");
            new GetJSONArrayTask(this, "/api/sell").execute(ext, count);
            if(!tokenString.equals(""))
            {
                //this call is just to test whether the token is valid/not expired. We don't actually care about sells.
                //new GetJSONArrayTask(this, "/api/sell").execute(ext, count);

                makeAPICall();
            }
            else
            {
                //If you have an invalid token, but you've somehow gotten this far, just bring you back to the login screen
                this.getActivity().finish();
            }
        }
        return rootView;
    }


    private void makeAPICall()
    {
        NameValuePair userID = new BasicNameValuePair("user_id", tokenString);
        NameValuePair count = new BasicNameValuePair("count", "10");
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair offset = new BasicNameValuePair("offset", Integer.toString(currentOffset));
        new GetJSONArrayTask(this, "/api/sell").execute(ext, count, offset, userID);
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
    @Override
    public void onPause()
    {
        Log.d(TAG, "onPause() -> " + "paused fragment.");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.d(TAG, "onResume() -> " + "resumed fragment.");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d(TAG, "onSaveInstanceState() -> " + "state saved for fragment.");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("listingsList", listingsList);


    }

    @Override
    public void onTaskCompleted(Object obj) {
        List<Book> temporaryBookList = new ArrayList<Book>();

        jArray = (JSONArray)obj;
        Log.i(TAG,"jArray length -> " + jArray.length());

        String nodeType;
        JSONObject nodeData;

        try{
            for(int i = 0 ; i < jArray.length(); i++ ){

                nodeType = jArray.getJSONObject(i).getString("kind");
                nodeData = jArray.getJSONObject(i).getJSONObject("data");
                Log.v(TAG, "OnTaskCompleted() -> Kind is: " + nodeType);
                if(nodeType.equals("sell"))
                {
                    Log.v(TAG, "OnTaskCompleted() -> Sell Data Polled -> " + nodeData.toString());
                    listingsList.add(generateListingFromJSONNode(nodeData));
                }
                else if(nodeType.equals("book"))
                {
                    Log.v(TAG, "OnTaskCompleted() -> Book Data Polled -> " + nodeData.toString());
                    temporaryBookList.add(Book.generateBookFromJSONNode(nodeData));
                }
                else
                {
                    Log.e(TAG, "OnTaskCompleted() -> NODE IS NEITHER BOOK NOR SELL!!!! Node Data is: " + nodeData.toString());
                }
            }

            for(Listing current_listing : listingsList){
                for(Book current_book : temporaryBookList){
                    if(current_listing.get_book_id() == current_book.get_id()){
                        Log.v(TAG, "OnTaskCompleted() -> Associated Book with ID {" + current_book.get_id()
                                + "} with Listing {" + current_listing.get_id()+"} which was requesting Book with ID {"
                                + current_listing.get_book_id()+"}");
                        current_listing.set_book(current_book);

                    }
                }
                if(current_listing.get_book() == null){
                    Log.e(TAG,"OnTaskCompleted() -> Could not associate a book to Listing with ID {"
                            + current_listing.get_id()+"} as it was requesting Book ID {" + current_listing.get_book_id()
                            +"} which does not exist in our Temporary Book List. CONTACT API TEAM!!!!");
                }
            }

        }catch(Exception e){
            Log.e(TAG, "OnTaskCompleted() -> HighLevel Catch -> "+ e.toString());
            e.printStackTrace();
        }


        removeLoadingListing();
        listingsAdapter.notifyDataSetChanged();
        loadingMore=false;
        listingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MyListings_Info.class);
                Bundle extras = new Bundle();
                extras.putParcelable("listings",listingsAdapter.getItem(position));
                intent.putExtras(extras);
                startActivity(intent);
            }});
    }

    /**
     * Generates a Listing Object based on the JSON Node passed to it.
     *
     * @param listingDataNode the associated listing JSON Node pulled from the API.
     * @return Returns a listing based on the JSON Node information. Returns null if it failed to parse
     * important data.
     */
    public Listing generateListingFromJSONNode(JSONObject listingDataNode){

        //temporary listing variables.
        int listing_id;
        int user_id;
        int edition_id;
        double price;
        String start_date;
        String end_date;

        try{
            listing_id =  listingDataNode.getInt("id");
        }catch(Exception e){
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for listing_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            user_id = listingDataNode.getInt("user_id");
        }catch(Exception e){
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for user_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            edition_id =  listingDataNode.getInt("edition_id");
        }catch(Exception e){
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for edition_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            price = listingDataNode.getDouble("price");
        }catch(Exception e){
            price = 9999.99;
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for price: " + e.toString());
        }
        try{
            start_date = listingDataNode.getString("start_date");
        }catch(Exception e){
            start_date = "N/A";
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for start_date: " + e.toString());
        }
        try{
            end_date = listingDataNode.getString("end_date");
        }catch(Exception e){
            end_date = "N/A";
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for end_date: " + e.toString());
        }

        return new Listing(listing_id,user_id,edition_id,price,start_date,end_date);

    }

    public void executeSearch(String query)
    {
        Log.i(TAG, "executeSearch() -> Query Received: " + query);
    }

}