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
    ListView listingsListView;
    View rootView;
    JSONArray jArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_listings, container, false);
        listingsListView = (ListView)rootView.findViewById(R.id.listings_list_view);
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "20");
        new GetJSONArrayTask(this, "/api/sell").execute(ext, count);


        return rootView;
    }


    @Override
    public void onTaskCompleted(Object obj) {
        List<Listing> listingsList= new ArrayList<Listing>();
        List<Book> temporaryBookList = new ArrayList<Book>();

        jArray = (JSONArray)obj;
        Log.i("ListingsFragment","jArray length -> " + jArray.length());

        String nodeType;
        JSONObject nodeData;

        try{
            for(int i = 0 ; i < jArray.length(); i++ ){

                nodeType = jArray.getJSONObject(i).getString("kind");
                nodeData = jArray.getJSONObject(i).getJSONObject("data");
                Log.i("ListingsFragment", "OnTaskCompleted() -> Kind is: " + nodeType);
                if(nodeType.equals("sell"))
                {
                    Log.i("ListingsFragment", "OnTaskCompleted() -> Sell Data Polled -> " + nodeData.toString());
                    listingsList.add(generateListingFromJSONNode(nodeData));
                }
                else if(nodeType.equals("book"))
                {
                    Log.i("ListingsFragment", "OnTaskCompleted() -> Book Data Polled -> " + nodeData.toString());
                    temporaryBookList.add(generateBookFromJSONNode(nodeData));
                }
                else
                {
                    Log.e("ListingsFragment", "OnTaskCompleted() -> NODE IS NEITHER BOOK NOR SELL!!!! Node Data is: " + nodeData.toString());
                }
            }

            for(Listing current_listing : listingsList){
                for(Book current_book : temporaryBookList){
                    if(current_listing.get_book_id() == current_book.get_id()){
                        Log.i("ListingsFragment", "OnTaskCompleted() -> Associated Book with ID {" + current_book.get_id()
                                + "} with Listing {" + current_listing.get_id()+"} which was requesting Book with ID {"
                                + current_listing.get_book_id()+"}");
                        current_listing.set_book(current_book);

                    }
                }
                if(current_listing.get_book() == null){
                    Log.e("ListingsFragment","OnTaskCompleted() -> Could not associate a book to Listing with ID {"
                            + current_listing.get_id()+"} as it was requesting Book ID {" + current_listing.get_book_id()
                            +"} which does not exist in our Temporary Book List. CONTACT API TEAM!!!!");
                }
            }

        }catch(Exception e){
            Log.e("ListingsFragment", "OnTaskCompleted() -> HighLevel Catch -> "+ e.toString());
            e.printStackTrace();
        }

        ListingArrayAdapter listingsAdapter = new ListingArrayAdapter(this.getActivity(), listingsList);
        listingsListView.setAdapter(listingsAdapter);

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
            Log.e("ListingsFragment", "generateListingFromJSONNode() -> Couldn't parse JSON for listing_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            user_id = listingDataNode.getInt("user_id");
        }catch(Exception e){
            Log.e("ListingsFragment", "generateListingFromJSONNode() -> Couldn't parse JSON for user_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            edition_id =  listingDataNode.getInt("edition_id");
        }catch(Exception e){
            Log.e("ListingsFragment", "generateListingFromJSONNode() -> Couldn't parse JSON for edition_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            price = listingDataNode.getDouble("price");
        }catch(Exception e){
            price = 9999.99;
            Log.e("ListingsFragment", "generateListingFromJSONNode() -> Couldn't parse JSON for price: " + e.toString());
        }
        try{
            start_date = listingDataNode.getString("start_date");
        }catch(Exception e){
            start_date = "N/A";
            Log.e("ListingsFragment", "generateListingFromJSONNode() -> Couldn't parse JSON for start_date: " + e.toString());
        }
        try{
            end_date = listingDataNode.getString("end_date");
        }catch(Exception e){
            end_date = "N/A";
            Log.e("ListingsFragment", "generateListingFromJSONNode() -> Couldn't parse JSON for end_date: " + e.toString());
        }

        return new Listing(listing_id,user_id,edition_id,price,start_date,end_date);

    }

    /**
     * Generates a Book Object based on the JSON Node passed to it.
     *
     * @param bookDataNode JSON Node grabbed from the API that relates to a book.
     * @return Returns a Book object, or if it fails to parse important data, returns Null.
     */
    public Book generateBookFromJSONNode(JSONObject bookDataNode){
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

        try{
            book_id = bookDataNode.getInt("id");
        }catch(Exception e){
            book_id = 0;
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for id: " + e.toString());
        }
        try{
            title = bookDataNode.getString("title");
        }catch(Exception e){
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for title: " + e.toString());
            return null;
        }
        try{
            isbn = bookDataNode.getString("isbn");
        }catch(Exception e){
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for ISBN: " + e.toString());
            return null;
        }
        try{
            edition_group_id = bookDataNode.getInt("edition_group_id");
        }catch(Exception e){
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for edition_group_id: " + e.toString());
            return null;
        }
        try{
            author = bookDataNode.getString("author");
        }catch(Exception e){
            author = "N/A";
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for author: " + e.toString());
        }
        try{
            edition = bookDataNode.getInt("edition");
        }catch(Exception e){
            edition = 0;
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for edition. Error: : " + e.toString());
        }
        try{
            publisher = bookDataNode.getString("publisher");
        }catch(Exception e){
            publisher = "N/A";
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for publisher: " + e.toString());
        }
        try{
            cover = bookDataNode.getString("cover");
        }catch(Exception e){
            cover = "N/A";
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for cover: " + e.toString());
        }
        try{
            image = bookDataNode.getString("image");
        }catch(Exception e){
            image = "N/A";
            Log.e("ListingsFragment", "generateBookFromJSONNode() -> Couldn't parse JSON for image: " + e.toString());
        }


        return new Book(book_id, title, isbn, edition,
                author, edition, publisher, cover, image);
    }
}