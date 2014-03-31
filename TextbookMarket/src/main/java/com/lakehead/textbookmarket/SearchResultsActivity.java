package com.lakehead.textbookmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultsActivity extends Activity implements OnTaskCompleted, ExpandableListView.OnChildClickListener {

    JSONArray jArray;
    ListView searchResultsListView;
    View rootView;

    SearchResultsExpandableListAdapter searchResultsAdapter;
    ExpandableListView expListView;

    //Holds Kind -> ArrayList<Book/Course/Listing>
    HashMap<String, ArrayList> resultHash;

    //Arrays to hold onto the search results before we throw them to the adapter.
    ArrayList<Book> bookList;
    ArrayList<Course> courseList;
    ArrayList<Listing> listingList;
    //holds the names of nonzero headers.
    ArrayList<String> headers;


    private static final String TAG = "SearchResultsActivity";
    private String query;

    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_expand);
        Log.i(TAG, "STARTED NEW ACTIVITY");
        Intent intent = getIntent();

        //grabbing search query from the original Activity. Passed along with the intent.
        query = intent.getStringExtra("query");

        //initializing all the views we need
        searchResultsListView = (ListView)findViewById(R.id.search_results_list_view);
        expListView = (ExpandableListView)findViewById(R.id.search_results_expand_lv);
        expListView.setOnChildClickListener(this);

        bookList = new ArrayList<Book>();
        courseList = new ArrayList<Course>();
        listingList = new ArrayList<Listing>();

        Log.i(TAG, "onCreateView() -> " + "Loading Search Results from API.");

        makeAPICall();


    }

    public void makeAPICall()
    {
        NameValuePair searchQuery = new BasicNameValuePair("query", this.query);
        //new GetJSONArrayTask(this, "/api/search").execute(searchQuery);
        NameValuePair count = new BasicNameValuePair("count", "10");
        new GetJSONArrayTask(this, "/api/book").execute(count);
        new GetJSONArrayTask(this, "/api/course").execute(count);
        new GetJSONArrayTask(this, "/api/sell").execute(count);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(Object obj) {
        jArray = (JSONArray)obj;

        if(jArray == null){
            Log.e(TAG, "onTaskCompleted() -> Received nothing from the API call!");
        }


       // resultTypeHeaders = new ArrayList<String>();
        resultHash = new HashMap<String, ArrayList>();
        for(int i = 0; i < jArray.length(); i++)
        {
            try{
                JSONObject superNode = jArray.getJSONObject(i);
                String nodeType = superNode.getString("kind");
                JSONObject dataNode = superNode.getJSONObject("data");
                if(nodeType.equals("book"))
                {
                    Log.i(TAG,"onTaskCompleted() -> " + "Found Book Node. Parsing...");
                    bookList.add(Book.generateBookFromJSONNode(dataNode));
                }
                else if(nodeType.equals("course"))
                {
                    Log.i(TAG,"onTaskCompleted() -> " + "Found Course Node. Parsing...");
                    courseList.add(Course.generateCourseFromJSONNode(dataNode));
                }
                else if(nodeType.equals("sell"))
                {
                    Log.i(TAG,"onTaskCompleted() -> " + "Found Sell Node. Parsing...");
                    listingList.add(Listing.generateListingFromJSONNode(dataNode));
                }
            }catch(Exception e){
                Log.e(TAG, "FAILED TO ADD SOME DATA TO LIST: " + e.toString());
            }

        }
        listingList = Listing.associateBooksToListings(listingList, bookList);

        headers = new ArrayList<String>();
        if(bookList.size() > 0){
            resultHash.put("Books", bookList);
            headers.add("Books");
        }
        if(courseList.size()>0){
            resultHash.put("Courses", courseList);
            headers.add("Courses");
        }
        if(listingList.size()>0){
            resultHash.put("Listings", listingList);
            headers.add("Listings");
        }


        Log.i(TAG, "bookList -> " + bookList);
        Log.i(TAG, "courseList -> " + courseList);
        Log.i(TAG, "listingList -> " + listingList);





        final SearchResultsExpandableListAdapter searchAdapter = new SearchResultsExpandableListAdapter(this, headers, resultHash);
        expListView.setAdapter(searchAdapter);


    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if(groupPosition == headers.indexOf("Books")){
            Book relatedObject = (Book)resultHash.get("Books").get(childPosition);
            Intent bookDetail = new Intent(v.getContext(), Book_Info.class);
            Bundle extras = new Bundle();
            bookDetail.putExtra("books", relatedObject);
            startActivity(bookDetail);
        }
        else if(groupPosition == headers.indexOf("Courses")){
            Course relatedObject = (Course)resultHash.get("Courses").get(childPosition);
            Intent courseDetail = new Intent(v.getContext(), Course_Info.class);
            Bundle extras = new Bundle();
            courseDetail.putExtra("course", relatedObject);
            startActivity(courseDetail);
        }
        else if(groupPosition == headers.indexOf("Listings")){
            Listing relatedObject = (Listing)resultHash.get("Listings").get(childPosition);
            Intent listingDetail = new Intent(v.getContext(), Book_Info.class);
            Bundle extras = new Bundle();
            listingDetail.putExtra("books", relatedObject);
            startActivity(listingDetail);
        }
        return true;
    }
}
