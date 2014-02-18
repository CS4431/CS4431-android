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
        Book tempBook1 = new Book(rootView.getContext(),1, "C How to Program","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book2);
        Book tempBook2 = new Book(rootView.getContext(),2, "Compiler Design","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book1);
        Book tempBook3 = new Book(rootView.getContext(),3, "Algorithm Analysis","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book4);
        Book tempBook4 = new Book(rootView.getContext(),4, "Data Structures","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book3);
        Book tempBook5 = new Book(rootView.getContext(),5, "Database Management Systems","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book1);
        Book tempBook6 = new Book(rootView.getContext(),6, "Object-Oriented Design and Methodologies","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book2);
        Book tempBook7 = new Book(rootView.getContext(),7, "Software Engineering Techniques","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book1);
        Book tempBook8 = new Book(rootView.getContext(),8, "The Dragon Book","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book4);
        Book tempBook9 = new Book(rootView.getContext(),9, "Clean Code 2","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book3);
        Book tempBook10 = new Book(rootView.getContext(),10, "Operating Systems in Depth","1232345234",1,"Deitel&Deitel",4,"Penguin Publishing","Paperback","Some_url.com/image.jpg", R.drawable.book2);
        Book[] bookList = new Book[] {tempBook1, tempBook2, tempBook3, tempBook4, tempBook5, tempBook6, tempBook7,
                tempBook8, tempBook9, tempBook10};

        final BookArrayAdapter bookAdapter = new BookArrayAdapter(this.getActivity(), bookList);
        bookListView.setAdapter(bookAdapter);
        return rootView;
    }

}