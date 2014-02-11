package com.lakehead.textbookmarket;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Master on 2/10/14.
 */
public class Book {

    private final String _name;
    private final String _edition;
    private final String _author;
    private final Drawable _icon;

    public Drawable get_icon() {
        return _icon;
    }

    public String get_name() {
        return _name;
    }

    public String get_edition() {
        return _edition;
    }

    public String get_author() {
        return _author;
    }



    public Book(Context context, String name, String edition, String author, int icon){
        _name = name;
        _edition = edition;
        _author = author;
        _icon = context.getResources().getDrawable(icon);

    }


}
