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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
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
        NameValuePair count = new BasicNameValuePair("count", "10");
        new GetJSONArrayTask(this, "/api/book").execute(ext, count);

        return rootView;
    }

    @Override
    public void onTaskCompleted(Object obj)
    {
        this.jArray = (JSONArray)obj;

        List<Book> bookList = new ArrayList<Book>();

        //Add all the books in our JSONArray to our bookList
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

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*
                        Book parameters
                        private final int _id;
                        private final String _title;
                        private final String _isbn;
                        private final int _edition_group_id;
                        private final String _author;
                        private final int _edition;
                        private final String _publisher;
                        private final String _cover;
                        private final String _image_url;//a URL pointing to the image.
                         */
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