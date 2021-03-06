package com.lakehead.textbookmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import android.graphics.drawable.Drawable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;

/**
 * The fragment used in the "Books" tab of MainActivity
 */
public class BooksFragment extends Fragment implements OnTaskCompleted {

    public static final String TAG = "BooksFragment";
    ListView bookListView;
    View rootView;
    JSONArray jArray;
    //ArrayList<Book> bookList;

    int currentOffset=0;
    int unfilteredOffset = 0;
    int searchResultsOffset = 0;

    boolean loadingMore=false;

    ArrayList<Book> bookList;
    ArrayList<Book> searchResultsBookList;
    BookArrayAdapter bookAdapter;

    //A dummy book used to tell the adapter to add a "loading" row
    Book loadingBook;

    public void executeSearch(String query)
    {
        Log.i(TAG, "executeSearch() -> Query Received: " + query);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_books, container, false);
        bookListView = (ListView)rootView.findViewById(R.id.book_list_view);
        //bookListView.setEmptyView(rootView.findViewById(R.id.empty));
        loadingBook = new Book();
        bookList = new ArrayList<Book>();
        bookAdapter = new BookArrayAdapter(this.getActivity(), bookList);
        bookListView.setAdapter(bookAdapter);
        //set the original offset to the unfiltered one. We have to hold onto both for when a search
        //occurs so that we can backtrack to the original list once the search is done.
        currentOffset = unfilteredOffset;

        //These NameValuePairs are the POST parameters for the API call
        //makeAPICall();

        //Here is where the magic happens
        bookListView.setOnScrollListener(new AbsListView.OnScrollListener(){
            //useless here, skip!
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            //dumdumdum
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //what is the bottom iten that is visible
                int lastInScreen = firstVisibleItem + visibleItemCount;
                Log.d("Debug",""+lastInScreen);
                //is the bottom item visible & not loading more already ? Load more !
                if((lastInScreen == totalItemCount) && !(loadingMore)){
                    currentOffset+=10;
                    loadingMore=true;
                    addLoadingBook();
                    //progressBar.setVisibility(View.VISIBLE);
                    makeAPICall();
                }
            }
        });


        if(savedInstanceState != null)
        {
            Log.i(TAG, "onCreateView() -> " + "Found saved instance state. Loading Book list from it...");
            bookList = savedInstanceState.getParcelableArrayList("bookList");
            final BookArrayAdapter bookAdapter = new BookArrayAdapter(this.getActivity(), bookList);
            bookListView.setAdapter(bookAdapter);

        }
        else
        {
            //These NameValuePairs are the POST parameters for the API call
            Log.i(TAG, "onCreateView() -> " + "No Saved instance state. Loading Book list from API...");
            makeAPICall();
        }


        return rootView;
    }


    private void addLoadingBook()
    {

        bookList.add(loadingBook);
        bookAdapter.notifyDataSetChanged();
    }

    private void removeLoadingBook()
    {
        bookList.remove(loadingBook);
        bookAdapter.notifyDataSetChanged();
    }

    private void makeAPICall()
    {
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "10");
        NameValuePair offset = new BasicNameValuePair("offset", Integer.toString(currentOffset));
        new GetJSONArrayTask(this, "/api/book").execute(ext, count, offset);

    }

    private void makeAPICallWithSearchQuery(String query){
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "20");
        //todo make sure sean has implemented some Query Post parameter for searching
        NameValuePair searchString = new BasicNameValuePair("query", query);
        NameValuePair offset = new BasicNameValuePair("offset", Integer.toString(currentOffset));
        //todo make sure Sean has developed a searching route for all Hashable objects.
        new GetJSONArrayTask(this, "/api/book/search").execute(ext, count, offset,searchString);
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
        outState.putParcelableArrayList("bookList", bookList);
        super.onSaveInstanceState(outState);

    }

    /**
     * @param obj
     * Override function for a callback used to receive data from AsyncTasks. The Object passed into
     * this function is cast to a JSONArray so that the data may be extracted from it. The extracted
     * data is then passed into a BookArrayAdapter so that it may then be applied to a ListView.
     */
    @Override
    public void onTaskCompleted(Object obj)
    {
        this.jArray = (JSONArray)obj;
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
        //List<Book> bookList = new ArrayList<Book>();
        //bookList = new ArrayList<Book>();

        JSONObject bookDataNode;
        //Add all the books in our JSONArray to our bookList
        try{
            for(int i = 0 ; i < jArray.length(); i++ ){
                bookDataNode = jArray.getJSONObject(i).getJSONObject("data");
                Log.i(TAG, "Book Data Polled -> " + bookDataNode.toString());


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


                bookList.add(new Book(book_id, title, isbn, edition,
                        author, edition, publisher, cover, image));
            }
        }
        catch(JSONException e)
        {
            Log.e(TAG, "OnTaskCompleted() -> " + e.toString());
            e.printStackTrace();
        }
        removeLoadingBook();
        bookAdapter.notifyDataSetChanged();
        loadingMore=false;
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Book_Info.class);
                Bundle extras = new Bundle();
                extras.putParcelable("books",bookAdapter.getItem(position));
                intent.putExtras(extras);
                startActivity(intent);
            }});
    }

}