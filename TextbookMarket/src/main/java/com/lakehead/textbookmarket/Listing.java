package com.lakehead.textbookmarket;

/**
 * Class holding all listing information. Each object represents a book for sale by a specific user.
 */
public class Listing {
    private final int _id;
    private final int _user_id;
    private final Book _book;
    //TODO honestly we should probably make Price an INT. Makes it much nicer to look at when browsing.
    private final double _price;
    private final String _start_date;
    private final String _end_date;


    public Listing(int id, int user_id, Book book, double price, String start_date, String end_date) {
        _id = id;
        _user_id = user_id;
        _book = book;
        _price = price;
        _start_date = start_date;
        _end_date = end_date;
    }

    public String get_end_date() {
        return _end_date;
    }

    public String get_start_date() {
        return _start_date;
    }

    public double get_price() {
        return _price;
    }

    public Book get_book() {
        return _book;
    }

    public int get_user_id() {
        return _user_id;
    }

    public int get_id() {
        return _id;
    }
}
