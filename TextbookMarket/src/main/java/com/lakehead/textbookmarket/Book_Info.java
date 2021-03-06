package com.lakehead.textbookmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Book_Info extends Activity implements OnTaskCompleted {
    private String name;
    private JSONArray jArray;
    private boolean inStock;
    private ImageView stockImage;
    private Button stockButton;
    private AlertDialog alertDialogStores;
    ArrayList<Listing> listingsList;
    ListView listViewItems;
    SpecificListingArrayAdapter adapter;
    private int bid =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);
        listingsList = new ArrayList<Listing>();
        ObjectItem[] ObjectItemData = new ObjectItem[20];

        ObjectItemData[0] = new ObjectItem(91, "Mercury");
        ObjectItemData[1] = new ObjectItem(92, "Watson");
        ObjectItemData[2] = new ObjectItem(93, "Nissan");
        ObjectItemData[3] = new ObjectItem(94, "Puregold");
        ObjectItemData[4] = new ObjectItem(95, "SM");
        ObjectItemData[5] = new ObjectItem(96, "7 Eleven");
        ObjectItemData[6] = new ObjectItem(97, "Ministop");
        ObjectItemData[7] = new ObjectItem(98, "Fat Chicken");
        ObjectItemData[8] = new ObjectItem(99, "Master Siomai");
        ObjectItemData[9] = new ObjectItem(100, "Mang Inasal");
        ObjectItemData[10] = new ObjectItem(101, "Mercury 2");
        ObjectItemData[11] = new ObjectItem(102, "Watson 2");
        ObjectItemData[12] = new ObjectItem(103, "Nissan 2");
        ObjectItemData[13] = new ObjectItem(104, "Puregold 2");
        ObjectItemData[14] = new ObjectItem(105, "SM 2");
        ObjectItemData[15] = new ObjectItem(106, "7 Eleven 2");
        ObjectItemData[16] = new ObjectItem(107, "Ministop 2");
        ObjectItemData[17] = new ObjectItem(108, "Fat Chicken 2");
        ObjectItemData[18] = new ObjectItem(109, "Master Siomai 2");
        ObjectItemData[19] = new ObjectItem(110, "Mang Inasal 2");

        // our adapter instance
        adapter = new SpecificListingArrayAdapter(Book_Info.this, listingsList);

        // create a new ListView, set the adapter and item click listener
        listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);

        /*
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });
        */

        getActionBar().setTitle("Book Information");


        stockImage = (ImageView) findViewById(R.id.availableListings);
        stockButton = (Button) findViewById(R.id.ListingWishlist);
        stockButton.setBackgroundResource(R.drawable.bluebuttonlooking);
        stockButton.setClickable(false);
        Intent intent = getIntent();
        Book myBook = intent.getParcelableExtra("books");
        name = myBook.get_title();




        stockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(inStock){
                Log.d("debug","There is a listing for the book and we would go to the listing here");
                alertDialogStores = new AlertDialog.Builder(Book_Info.this).setView(listViewItems).setTitle("Listings").show();


                }else{
                Log.d("debug","There is no listing for the book so we would add it to the wishlist here");
                }
            }
        });

        bid = myBook.get_id();
        ((ImageView)findViewById(R.id.image)).setImageBitmap(myBook.getBitmap());
        ((TextView) findViewById(R.id.BookTitle)).setText("Title: " + myBook.get_title());
        ((TextView)findViewById(R.id.Author)).setText("Author: " + myBook.get_author());
        ((TextView)findViewById(R.id.ISBN)).setText("ISBN: " + myBook.get_isbn());
        ((TextView)findViewById(R.id.edition)).setText("Edition: " + Integer.toString(myBook.get_edition()));
        ((TextView)findViewById(R.id.coverType)).setText("Cover Style: " + myBook.get_cover());
        ((TextView)findViewById(R.id.Publisher)).setText("Publisher: " + myBook.get_publisher());
        makeAPICall();
        makeAPICall2();

    }


    private void makeAPICall()
    {
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair title = new BasicNameValuePair("title", name);
        new GetJSONArrayTask(this, "/api/book").execute(ext, title );

    }
    private  void makeAPICall2(){
        Log.d("debug","check 1 called api 2");
        NameValuePair ext = new BasicNameValuePair("ext","json");
        NameValuePair id = new BasicNameValuePair("id",Integer.toString(bid));
        new GetJSONArrayTask(this, "/api/sell").execute(ext,id);
    }

    public void onTaskCompleted(Object obj)
    {
        listingsList = new ArrayList<Listing>();

        this.jArray = (JSONArray)obj;
        int numOfListings = 0;
        String nodeType;
        JSONObject bookData;
        List<Book> temporaryBookList = new ArrayList<Book>();

        try{
        nodeType = jArray.getJSONObject(0).getString("kind");
        Log.d("debug","check 1 nodetype is " + nodeType);
        if(jArray.getJSONObject(0).getString("kind").equals("book")) {
            try {
                bookData = jArray.getJSONObject(0).getJSONObject("data");
                numOfListings = bookData.getInt("for_sale");
                for (int i = 0; i < jArray.length(); i++) {
                    nodeType = jArray.getJSONObject(i).getString("kind");
                    bookData = jArray.getJSONObject(i).getJSONObject("data");
                    if (nodeType.equals("book")) {

                    }
                    Log.d("debug", "number of listings the node type is " + nodeType);
                }
                Log.d("debug", "the number of listings for this book is " + numOfListings + " the length of the list is " + listingsList.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (numOfListings != 0) {
                stockImage.setBackgroundResource(R.drawable.instock);
                inStock = true;
                stockButton.setBackgroundResource(R.drawable.bluebuttonviewlisting);
                stockButton.setClickable(true);
            } else {
                stockImage.setBackgroundResource(R.drawable.outstock);
                inStock = false;
                stockButton.setBackgroundResource(R.drawable.bluebuttonwishlist);
                stockButton.setClickable(false);
            }
        }else if(jArray.getJSONObject(0).getString("kind").equals("sell")){
            for (int i = 0; i < jArray.length();i++){
                if(jArray.getJSONObject(i).getString("kind").equals("sell")){
                    try {
                        bookData = jArray.getJSONObject(i).getJSONObject("data");
                        listingsList.add(generateListingFromJSONNode(bookData));
                    }catch (Exception e){

                    }
                }
            }
            adapter = new SpecificListingArrayAdapter(Book_Info.this, listingsList);
            listViewItems.setAdapter(adapter);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Listing generateListingFromJSONNode(JSONObject listingDataNode) {

                    int id =0;
                    int user_id =0;
                    int book_id=0;
                    double price=0;
                    String start_date="";
                    String end_date="";

        try{
            id = listingDataNode.getInt("id");
            user_id = listingDataNode.getInt("user_id");
            book_id = listingDataNode.getInt("book_id");
            price = listingDataNode.getDouble("price");
            start_date = listingDataNode.getString("start_date");
            end_date = listingDataNode.getString("end_date");

        }catch (Exception e){

        }
        return new Listing(id,user_id,book_id,price,start_date,end_date);

    }
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book__info, menu);
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

}
