package com.lakehead.textbookmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BooksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);
        ListView bookListView = (ListView)rootView.findViewById(R.id.book_list_view);
        Book tempBook1 = new Book(this.getActivity(), "C How to Program", "4th", "Deitel&Deitel", R.drawable.book1);
        Book tempBook2 = new Book(this.getActivity(), "Operating Systems", "3rd", "Frank Allaire", R.drawable.book2);
        Book tempBook3 = new Book(this.getActivity(), "Database Management", "4th", "Francis Allairington", R.drawable.book3);
        Book tempBook4 = new Book(this.getActivity(), "Game Design Patterns", "2nd", "Klein & Co.", R.drawable.book4);
        Book tempBook5 = new Book(this.getActivity(), "Compiler Design", "1st", "Dragon", R.drawable.book2);
        Book tempBook6 = new Book(this.getActivity(), "Object-Oriented Design", "4th", "Deitel&Deitel", R.drawable.book1);
        Book tempBook7 = new Book(this.getActivity(), "Why's Poignant Guide to Ruby", "1st", "_why the lucky stiff", R.drawable.book4);
        Book tempBook8 = new Book(this.getActivity(), "Why's Poignant Guide to Ruby", "1st", "_why the lucky stiff", R.drawable.book4);
        Book tempBook9 = new Book(this.getActivity(), "Why's Poignant Guide to Ruby", "1st", "_why the lucky stiff", R.drawable.book4);
        Book tempBook10 = new Book(this.getActivity(), "Why's Poignant Guide to Ruby", "1st", "_why the lucky stiff", R.drawable.book4);
        Book[] bookList = new Book[] {tempBook1, tempBook2, tempBook3, tempBook4, tempBook5, tempBook6, tempBook7,
                tempBook8, tempBook9, tempBook10};

        final BookArrayAdapter bookAdapter = new BookArrayAdapter(this.getActivity(), bookList);
        bookListView.setAdapter(bookAdapter);
        return rootView;
    }

}