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
    }

}