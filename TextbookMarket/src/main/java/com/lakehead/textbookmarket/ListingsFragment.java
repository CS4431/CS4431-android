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
        NameValuePair count = new BasicNameValuePair("count", "10");
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

        //temporary book variables
        int edition;
        int book_id;
        String title;
        String isbn;
        int edition_group_id;
        String author;
        String publisher;
        String cover;
        String image;

        //temporary listing variables.
        int listing_id;
        int user_id;
        double price;
        String start_date;
        String end_date;


        try{
            for(int i = 0 ; i < jArray.length() /2; i++ ){
                bookDataNode = jArray.getJSONObject(bookStartIndex + i).getJSONObject("data");
                Log.i("ListingsFragment", "Book Data Polled -> " + bookDataNode.toString());

                listingDataNode = jArray.getJSONObject(i).getJSONObject("data");
                Log.i("ListingsFragment", "Sell Data Polled -> " + listingDataNode.toString());

                //First initialize the Listing's book.

                try{
                    book_id = bookDataNode.getInt("id");
                }catch(Exception e){
                    book_id = 0;
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for id: " + e.toString());
                }
                try{
                    title = bookDataNode.getString("title");
                }catch(Exception e){
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for title: " + e.toString());
                    continue;
                }
                try{
                    isbn = bookDataNode.getString("isbn");
                }catch(Exception e){
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for ISBN: " + e.toString());
                    continue;
                }
                try{
                    edition_group_id = bookDataNode.getInt("edition_group_id");
                }catch(Exception e){
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for edition_group_id: " + e.toString());
                    continue;
                }
                try{
                    author = bookDataNode.getString("author");
                }catch(Exception e){
                    author = "N/A";
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for author: " + e.toString());

                }
                try{
                    edition = bookDataNode.getInt("edition");
                }catch(Exception e){
                    edition = 0;
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for edition: " + e.toString());
                }
                try{
                    publisher = bookDataNode.getString("publisher");
                }catch(Exception e){
                    publisher = "N/A";
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for publisher: " + e.toString());
                }
                try{
                    cover = bookDataNode.getString("cover");
                }catch(Exception e){
                    cover = "N/A";
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for cover: " + e.toString());
                }
                try{
                    image = bookDataNode.getString("image");
                }catch(Exception e){
                    image = "N/A";
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for image: " + e.toString());
                }


                Book temporaryBook = new Book(rootView.getContext(), book_id, title, isbn, edition,
                                              author, edition, publisher, cover, image);


                //Next Initialize the listing, passing it the book.
                try{
                    listing_id =  listingDataNode.getInt("id");
                }catch(Exception e){
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for listing_id: " + e.toString());
                    continue;
                }
                try{
                    user_id = listingDataNode.getInt("user_id");
                }catch(Exception e){
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for user_id: " + e.toString());
                    continue;
                }
                try{
                    price = listingDataNode.getDouble("price");
                }catch(Exception e){
                    price = 9999.99;
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for price: " + e.toString());
                }
                try{
                    start_date = listingDataNode.getString("start_date");
                }catch(Exception e){
                    start_date = "N/A";
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for start_date: " + e.toString());
                }
                try{
                    end_date = listingDataNode.getString("end_date");
                }catch(Exception e){
                    end_date = "N/A";
                    Log.e("ListingsFragment", "OnTaskCompleted() -> Couldn't parse JSON for end_date: " + e.toString());
                }

                listingsList.add(new Listing(
                        listing_id,
                        user_id,
                        temporaryBook,
                        price,
                        start_date,
                        end_date));

            }
        }catch(Exception e){
            Log.e("ListingsFragment", "OnTaskCompleted() -> "+ e.toString());
            e.printStackTrace();
        }
        ListingArrayAdapter listingsAdapter = new ListingArrayAdapter(this.getActivity(), listingsList);
        listingsListView.setAdapter(listingsAdapter);

    }
}
