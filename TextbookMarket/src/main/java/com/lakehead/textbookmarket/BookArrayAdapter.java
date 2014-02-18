package com.lakehead.textbookmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Master on 2/10/14.
 */
public class BookArrayAdapter extends ArrayAdapter<Book> {
    private final Context context;
    private final Book[] books;

    /**
     *
     * @param context
     * @param books
     */
    public BookArrayAdapter(Context context, Book[] books) {
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
        titleTextView.setText(books[position].get_title());

        ImageView iconImageView = (ImageView)rowView.findViewById(R.id.icon);
        iconImageView.setImageDrawable(books[position].get_icon_drawable());

        TextView detailTextView = (TextView)rowView.findViewById(R.id.bookInfo);
        detailTextView.setText(books[position].get_author());

        return rowView;


    }

}
