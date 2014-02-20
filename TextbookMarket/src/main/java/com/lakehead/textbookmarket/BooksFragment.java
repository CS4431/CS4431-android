package com.lakehead.textbookmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment implements OnTaskCompleted {

    ListView bookListView;
    View rootView;
    JSONArray jArray;

    Book tempBook1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_books, container, false);
        bookListView = (ListView)rootView.findViewById(R.id.book_list_view);

        NameValuePair ext = new BasicNameValuePair("ext", "json");
        NameValuePair count = new BasicNameValuePair("count", "10");
        new GetJSONArrayTask(this, "/api/book").execute(ext, count);

        return rootView;
    }



    @Override
    public void onTaskCompleted(Object obj)
    {
        this.jArray = (JSONArray)obj;

        List<Book> bookList = new ArrayList<Book>();

        try
        {
            for(int i = 0; i < jArray.length(); i++)
            {
                bookList.add(new Book(rootView.getContext(),
                        jArray.getJSONObject(i).getJSONObject("data").getInt("id"),
                        jArray.getJSONObject(i).getJSONObject("data").getString("title"),
                        jArray.getJSONObject(i).getJSONObject("data").getString("isbn"),
                        jArray.getJSONObject(i).getJSONObject("data").getInt("edition_group_id"),
                        jArray.getJSONObject(i).getJSONObject("data").getString("author"),
                        jArray.getJSONObject(i).getJSONObject("data").getInt("edition"),
                        jArray.getJSONObject(i).getJSONObject("data").getString("publisher"),
                        jArray.getJSONObject(i).getJSONObject("data").getString("cover"),
                        jArray.getJSONObject(i).getJSONObject("data").getString("image")));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        final BookArrayAdapter bookAdapter = new BookArrayAdapter(this.getActivity(), bookList);
        bookListView.setAdapter(bookAdapter);

    }

}