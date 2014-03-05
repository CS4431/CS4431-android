package com.lakehead.textbookmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Master on 2/10/14.
 */
public class BookArrayAdapter extends ArrayAdapter<Book> {
    private final Context context;
    private final List<Book> books;
    private final int THUMBNAIL_SIZE = 96;

    /**
     *
     * @param context
     * @param books
     */
    public BookArrayAdapter(Context context, List<Book> books) {
        super(context, R.layout.books_item_view,  books);
        this.context = context;
        this.books = books;
    }



    /**
     *
     * @param position The Array position of the iterable list
     * @param convertView unused
     * @param parent The parent of the current View.
     * @return Returns the now-formulated row of data.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.books_item_view, parent, false);

        TextView titleTextView = (TextView)rowView.findViewById(R.id.bookTitle);
        titleTextView.setText(books.get(position).get_title());

        ImageView iconImageView = (ImageView)rowView.findViewById(R.id.icon);

        //If the book doesn't have a bitmap yet, then fetch it. Otherwise, just display the one we have
        if(books.get(position).getBitmap() == null)
        {
            new GetImageTask(books.get(position).get_image_url(), iconImageView, THUMBNAIL_SIZE, THUMBNAIL_SIZE, books.get(position)).execute();
        }
        else
        {
            iconImageView.setImageBitmap(books.get(position).getBitmap());
        }

        TextView detailTextView = (TextView)rowView.findViewById(R.id.bookInfo);
        detailTextView.setText(books.get(position).get_author());

        return rowView;
    }

}
