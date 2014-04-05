package com.lakehead.textbookmarket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Michael on 4/3/14.
 */
public class RequisiteBooksArrayAdapter extends ArrayAdapter {
        Context mContext;
        int layoutResourceId;
        ArrayList<Book> myBooks;
        public RequisiteBooksArrayAdapter(Context mContext,ArrayList<Book> myBooks) {
            super(mContext, R.layout.activity_requisite_books_fragment, myBooks);
            this.layoutResourceId = R.layout.activity_requisite_books_fragment;
            this.mContext = mContext;
            this.myBooks = myBooks;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position

            Book tempBook = myBooks.get(position);
            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            textViewItem.setText(tempBook.get_title());
            textViewItem.setTag(tempBook.get_id());

            return convertView;

        }

    }

