package com.lakehead.textbookmarket;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.w3c.dom.Text;

public class AddListingActivity extends Activity implements OnTaskCompleted {

    public static final String TAG = "AddListingActivity";
    Book selected_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        Bundle bundle = getIntent().getExtras();
        selected_book = bundle.getParcelable("book");
        Log.i(TAG, "OnCreate() -> Received Parcelable book with title: " + selected_book.get_title());

        TextView titleTextView = (TextView)findViewById(R.id.textViewBookTitle);
        TextView authorTextView = (TextView)findViewById(R.id.textViewAuthor);
        TextView editionTextView = (TextView)findViewById(R.id.textViewEditionNumber);
        TextView msrpTextView = (TextView)findViewById(R.id.textViewMSRP);
        TextView publisherTextView = (TextView)findViewById(R.id.textViewPublisher);
        ImageView iconImageView = (ImageView)findViewById(R.id.bookImageIcon);
        TextView priceTextView = (TextView)findViewById(R.id.editTextPrice);
        priceTextView.setText("0");

        titleTextView.setText(selected_book.get_title());
        authorTextView.setText(selected_book.get_author());
        editionTextView.setText("Edition: "+ String.valueOf(selected_book.get_edition()));
        publisherTextView.setText(selected_book.get_publisher());

        if(selected_book.getBitmap() == null)
        {
            new GetImageTask(selected_book.get_image_url(), iconImageView, 190, 190,selected_book).execute();
        }
        else
        {
            iconImageView.setImageBitmap(selected_book.getBitmap());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submitSellClicked(View view){
        Log.i(TAG,"submitSellClicked() -> " + "Found Submit Clicked. ");

        EditText priceView = (EditText)findViewById(R.id.editTextPrice);
        Button submitButton = (Button)findViewById(R.id.submit_sell_button);
        ProgressBar pBar = (ProgressBar)findViewById(R.id.submitSellProgressBar);

        String priceText = priceView.getText().toString();
        if(Float.valueOf(priceText) > 0)
        {
            pBar.setVisibility(View.VISIBLE);
            submitButton.setEnabled(false);
            NameValuePair user_id = new BasicNameValuePair("user_id", "316d3e20-9456-41c2-b934-a973e60f6055");
            NameValuePair edition_id = new BasicNameValuePair("edition_id", String.valueOf(selected_book.get_id()));
            NameValuePair price = new BasicNameValuePair("price", priceText);
            new GetJSONArrayTask(this, "/api/create/sell").execute(user_id,edition_id,price);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You must enter a price greater than zero.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }


    @Override
    public void onTaskCompleted(Object obj) {
        JSONArray jArray = (JSONArray)obj;
        Log.i(TAG, "onTaskCompleted() -> " + "got jArray from TaskCompleted -> " + jArray.toString());
        ProgressBar pBar = (ProgressBar)findViewById(R.id.submitSellProgressBar);
        pBar.setVisibility(View.INVISIBLE);
        Button submitButton = (Button)findViewById(R.id.submit_sell_button);


        //change this later to ensure that the listing was created. Once they return meaningful messages.
        if(jArray != null)
        {
            try
            {
                String returnType = jArray.getJSONObject(0).getString("kind");
                if(returnType.equalsIgnoreCase("sell"))
                {
                    Log.i(TAG, "onTaskCompleted() -> Kind found: " + returnType);
                    Log.i(TAG, "onTaskCompleted() -> " + "Received jArray -> " + jArray.toString());
                    ImageView checkMark = (ImageView)findViewById(R.id.greenCheckImageView);

                    submitButton.setVisibility(View.INVISIBLE);
                    checkMark.setVisibility(View.VISIBLE);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Your book has been listed. It should show up under the My Listings heading shortly.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else
                {
                    Log.e(TAG, "onTaskCompleted() -> " + "return Type seems to not be a sell? : Return Type = " + returnType);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "There was an error listing your book. Please ensure you are logged in.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
            catch(Exception e )
            {
                Log.e(TAG, "onTaskCompleted() -> " + "unable to parse jsonObject. -> " + e.toString());
            }

        }
        else
        {
            //throw some shit.
        }

    }
}
