package com.lakehead.textbookmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    View rootView;
    ListView listingsListView;
    JSONArray jArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_listings, container, false);
        listingsListView = (ListView)rootView.findViewById(R.id.listings_list_view);
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "2");
        new GetJSONArrayTask(this, "/api/sell").execute(ext, count);


        return rootView;
    }


    @Override
    public void onTaskCompleted(Object obj) {
        List<Listing> listingsList= new ArrayList<Listing>();
        List<Book> temporaryBookList = new ArrayList<Book>();

        JSONObject bookDataNode;
        JSONObject listingDataNode;


        jArray = (JSONArray)obj;

        Log.i("ListingsFragment","jArray length -> " + jArray.length());
        int bookStartIndex = jArray.length()/2;
        try{
            for(int i = 0 ; i < jArray.length() /2; i++ ){
                bookDataNode = jArray.getJSONObject(bookStartIndex + i).getJSONObject("data");
                Log.i("ListingsFragment", "Book Data Polled -> " + bookDataNode.toString());

                listingDataNode = jArray.getJSONObject(i).getJSONObject("data");
                Log.i("ListingsFragment", "Sell Data Polled -> " + listingDataNode.toString());

                //First initialize the Listing's book.
                int edition;
                try{
                    edition = bookDataNode.getInt("edition");
                }catch(Exception e){
                    edition = 0;
                }
                Book temporaryBook = new Book(
                rootView.getContext(),
                bookDataNode.getInt("id"),
                bookDataNode.getString("title"),
                bookDataNode.getString("isbn"),
                bookDataNode.getInt("edition_group_id"),
                bookDataNode.getString("author"),
                edition,
                bookDataNode.getString("publisher"),
                bookDataNode.getString("cover"),
                bookDataNode.getString("image"));

                //Next Initialize the listing, passing it the book.
                listingsList.add(new Listing(
                        listingDataNode.getInt("id"),
                        listingDataNode.getInt("user_id"),
                        temporaryBook,
                        listingDataNode.getDouble("price"),
                        listingDataNode.getString("start_date"),
                        listingDataNode.getString("end_date")));

            }
        }catch(Exception e){
            Log.e("ListingsFragment", "OnTaskCompleted() -> "+ e.toString());
            e.printStackTrace();
        }
        ListingArrayAdapter listingsAdapter = new ListingArrayAdapter(this.getActivity(), listingsList);
        listingsListView.setAdapter(listingsAdapter);

    }
}
