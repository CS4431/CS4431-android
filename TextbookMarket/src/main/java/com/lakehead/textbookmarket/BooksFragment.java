package com.lakehead.textbookmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The fragment used in the "Books" tab of MainActivity
 */
public class BooksFragment extends Fragment implements OnTaskCompleted {

    ListView bookListView;
    View rootView;
    JSONArray jArray;

    int currentOffset=0;
    boolean loadingMore=false;

    List<Book> bookList;
    BookArrayAdapter bookAdapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_books, container, false);
        bookListView = (ListView)rootView.findViewById(R.id.book_list_view);
        progressBar = (ProgressBar)rootView.findViewById(R.id.loading_more_bar);
        progressBar.setVisibility(View.GONE);
        //bookListView.setEmptyView(rootView.findViewById(R.id.empty));
        bookList = new ArrayList<Book>();
        bookAdapter = new BookArrayAdapter(this.getActivity(), bookList);
        bookListView.setAdapter(bookAdapter);



        //These NameValuePairs are the POST parameters for the API call
        makeAPICall();

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
                    progressBar.setVisibility(View.VISIBLE);
                    makeAPICall();
                }
            }
        });
        return rootView;
    }

    private void makeAPICall()
    {
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "10");
        NameValuePair offset = new BasicNameValuePair("offset", Integer.toString(currentOffset));
        new GetJSONArrayTask(this, "/api/book").execute(ext, count, offset);
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

        JSONObject bookDataNode;
        //Add all the books in our JSONArray to our bookList
        try{
            for(int i = 0 ; i < jArray.length(); i++ ){
                bookDataNode = jArray.getJSONObject(i).getJSONObject("data");
                Log.i("BooksFragment", "Book Data Polled -> " + bookDataNode.toString());


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


                bookList.add(new Book(rootView.getContext(), book_id, title, isbn, edition,
                        author, edition, publisher, cover, image));
            }
        }
        catch(JSONException e)
        {
            Log.e("BooksFragment", "OnTaskCompleted() -> " + e.toString());
            e.printStackTrace();
        }
        bookAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        loadingMore=false;
    }

}