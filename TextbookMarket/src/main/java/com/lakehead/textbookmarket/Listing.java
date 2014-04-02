package com.lakehead.textbookmarket;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class holding all listing information. Each object represents a book for sale by a specific user.
 */
public class Listing implements Parcelable {
    public static final String TAG = "Listing";
    private final int _id;
    private final int _user_id;
    private Book _book;
    //TODO honestly we should probably make Price an INT. Makes it much nicer to look at when browsing.
    private final double _price;
    private final String _start_date;
    private final String _end_date;

    private final int _book_id;


    public Listing(int id, int user_id, int book_id, double price, String start_date, String end_date) {
        _id = id;
        _user_id = user_id;
        _book_id = book_id;
        _book = null;
        _price = price;
        _start_date = start_date;
        _end_date = end_date;
    }
    // Parcelling part
    public Listing(Parcel in){
        _id = in.readInt();
        _user_id = in.readInt();
        _book_id = in.readInt();
        _book = (Book)in.readParcelable(ClassLoader.getSystemClassLoader());
        _price = in.readDouble();
        _start_date = in.readString();
        _end_date = in.readString();
    }


    /**
     * A class necessary for parcelable to work. Handles Creating Parceled Versions of books
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };


    /**
     * This constructor must only be used to create sentinel listings used to tell the ListingArrayAdapter
     * to insert a "loading" row.
     */
    public Listing() {
        _id = -1;
        _user_id = -1;
        _book_id = -1;
        _book = null;
        _price = -1;
        _start_date = null;
        _end_date = null;
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

    public int get_book_id() {return _book_id;}

    public void set_book(Book book){if(_book == null)_book = book;}

    public int get_user_id() {
        return _user_id;
    }

    public int get_id() {
        return _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this._id);
        dest.writeInt(this._user_id);
        dest.writeInt(this._book_id);
        dest.writeParcelable(this._book,0);
        dest.writeDouble(this._price);
        dest.writeString(this._start_date);
        dest.writeString(this._end_date);
    }

    static public Listing generateListingFromJSONNode(JSONObject listingDataNode){

        //temporary listing variables.
        int listing_id;
        int user_id;
        int edition_id;
        double price;
        String start_date;
        String end_date;

        try{
            listing_id =  listingDataNode.getInt("id");
        }catch(Exception e){
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for listing_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            user_id = listingDataNode.getInt("user_id");
        }catch(Exception e){
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for user_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            edition_id =  listingDataNode.getInt("edition_id");
        }catch(Exception e){
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for edition_id!!! Failing: " + e.toString());
            return null;
        }
        try{
            price = listingDataNode.getDouble("price");
        }catch(Exception e){
            price = 9999.99;
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for price: " + e.toString());
        }
        try{
            start_date = listingDataNode.getString("start_date");
        }catch(Exception e){
            start_date = "N/A";
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for start_date: " + e.toString());
        }
        try{
            end_date = listingDataNode.getString("end_date");
        }catch(Exception e){
            end_date = "N/A";
            Log.e(TAG, "generateListingFromJSONNode() -> Couldn't parse JSON for end_date: " + e.toString());
        }

        return new Listing(listing_id,user_id,edition_id,price,start_date,end_date);

    }

    static public ArrayList<Listing> associateBooksToListings(ArrayList<Listing> listingsList, ArrayList<Book>temporaryBookList){
        for(Listing current_listing : listingsList){
            if(current_listing._id > 0){ //this is here to handle the dummy listings. 
                for(Book current_book : temporaryBookList){
                    if(current_listing.get_book_id() == current_book.get_id()){
                        Log.v(TAG, "OnTaskCompleted() -> Associated Book with ID {" + current_book.get_id()
                                + "} with Listing {" + current_listing.get_id()+"} which was requesting Book with ID {"
                                + current_listing.get_book_id()+"}");
                        current_listing.set_book(current_book);

                    }
                }
                if(current_listing.get_book() == null){
                    Log.e(TAG,"OnTaskCompleted() -> Could not associate a book to Listing with ID {"
                            + current_listing.get_id()+"} as it was requesting Book ID {" + current_listing.get_book_id()
                            +"} which does not exist in our Temporary Book List. CONTACT API TEAM!!!!");
                }
            }
        }
        return listingsList;
    }
}
