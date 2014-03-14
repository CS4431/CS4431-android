package com.lakehead.textbookmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

    ListView bookListView;
    View rootView;
    JSONArray jArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_books, container, false);
        bookListView = (ListView)rootView.findViewById(R.id.book_list_view);

        //These NameValuePairs are the POST parameters for the API call
        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "100");
        new GetJSONArrayTask(this, "/api/book").execute(ext, count);

        return rootView;
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
        List<Book> bookList = new ArrayList<Book>();

        JSONObject bookDataNode;
        //Add all the books in our JSONArray to our bookList
        try{
            for(int i = 0 ; i < jArray.length(); i++ ){
                bookDataNode = jArray.getJSONObject(i).getJSONObject("data");
                Log.i("BooksFragment", "Book Data Polled -> " + bookDataNode.toString());
                bookList.add(Book.generateBookFromJSONNode(bookDataNode));
            }
        }
        catch(JSONException e)
        {
            Log.e("BooksFragment", "OnTaskCompleted() -> " + e.toString());
            e.printStackTrace();
        }

        final BookArrayAdapter bookAdapter = new BookArrayAdapter(this.getActivity(), bookList);
        bookListView.setAdapter(bookAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Book_Info.class);
                Bundle extras = new Bundle();
                int bid = bookAdapter.getItem(position).get_id();
                int bedition = bookAdapter.getItem(position).get_edition();
                String btitle = bookAdapter.getItem(position).get_title();
                String bisbn = bookAdapter.getItem(position).get_isbn();
                String bauthor = bookAdapter.getItem(position).get_author();
                String bpublisher = bookAdapter.getItem(position).get_publisher();
                String bcover = bookAdapter.getItem(position).get_cover();
                Bitmap bbitmap = bookAdapter.getItem(position).getBitmap();

                extras.putInt("bid",bid);
                extras.putInt("bedition",bedition);
                extras.putString("Title", btitle);

                extras.putString("bisbn",bisbn);
                extras.putString("bauthor",bauthor);
                extras.putString("bpublisher",bpublisher);
                extras.putString("bcover",bcover);
                intent.putExtra("bimage",bbitmap);
                intent.putExtras(extras);
                startActivity(intent);
            }});
    }

}