package com.lakehead.textbookmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Master on 2/10/14.
 */
public class BookArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public BookArrayAdapter(Context context, String[] values) {
        super(context, R.layout.item_view,  values);
        this.context = context;
        this.values = values;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_view, parent, false);

        TextView titleTextView = (TextView)rowView.findViewById(R.id.bookTitle);
        titleTextView.setText(values[position]);

        TextView detailTextView = (TextView)rowView.findViewById(R.id.bookInfo);
        detailTextView.setText("This is some placeholder info");

        return rowView;


    }

}
