package com.lakehead.textbookmarket;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Master on 2/10/14.
 */
public class Book {
    private final int _id;
    private final String _title;
    private final String _isbn;
    private final int _book_id;
    private final String _author;
    private final int _edition;
    private final String _publisher;
    private final String _cover;
    private final String _image_url;//a URL pointing to the image.
    private final Drawable _icon_drawable;


    /**
     * @param id        Internally assigned ID.
     * @param title     Title of the book.
     * @param isbn      The ISBN used to distinguish from other editions of same book.
     * @param book_id   Internally assigned Book ID.
     * @param author    Author of the book.
     * @param edition   Edition number expressed as an integer.
     * @param publisher The publishing company
     * @param cover     Paperback/Hardcover
     * @param image_url The URL pointing to the image hosted by LakeheadU's Bookstore
     */
    public Book(Context context, int id, String title, String isbn, int book_id, String author, int edition, String publisher, String cover, String image_url, int icon) {
        _id = id;
        _title = title;
        _isbn = isbn;
        _book_id = book_id;
        _author = author;
        _edition = edition;
        _publisher = publisher;
        _cover = cover;
        _image_url = image_url;
        _icon_drawable = context.getResources().getDrawable(icon);
    }

    public String get_image_url() {
        return _image_url;
    }

    public int get_id() {
        return _id;
    }

    public String get_isbn() {
        return _isbn;
    }

    public int get_book_id() {
        return _book_id;
    }

    public String get_author() {
        return _author;
    }

    public int get_edition() {
        return _edition;
    }

    public String get_publisher() {
        return _publisher;
    }

    public String get_cover() {
        return _cover;
    }

    public String get_title() {
        return _title;
    }

    public Drawable get_icon_drawable() {
        return _icon_drawable;
    }
}
