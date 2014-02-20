package com.lakehead.textbookmarket;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddListingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        ProgressBar bar = (ProgressBar)findViewById(R.id.isbnProgressBar);
        bar.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_listing, menu);
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

    public void scanClicked(View view){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan(IntentIntegrator.BOOK_CODE_TYPES);
    }

    public void onActivityResult(int requestCode, int resultCode,Intent intent){
        try{
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            //had to do this stupid double-if statement because occasionally the scanner will return
            //null as the STRING and occasionally will return null as the scanningResult
            String scanContent = scanningResult.getContents();
            if(scanContent != null && isISBN13Valid(scanContent)){
                Log.d("AddListingActivity","This is the ISBN " + scanContent);
                Log.d("AddListingActivity", "This is the scanFormat " + scanningResult.getFormatName());
                TextView isbnTextView = (TextView)findViewById(R.id.isbnText);
                isbnTextView.setText(scanContent);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No ISBN data received. Is this a valid ISBN13?", Toast.LENGTH_SHORT);
                toast.show();
            }

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
        }catch(Exception e){Log.e("AddListingActivity","Error during reading of scan data: " + e.toString());}
    }

    public void okClicked(View view){
        TextView isbnTextView = (TextView)findViewById(R.id.isbnText);
        isbnTextView.clearFocus();
        ProgressBar bar = (ProgressBar)findViewById(R.id.isbnProgressBar);
        bar.setVisibility(View.VISIBLE);
        Log.i("AddListingActivity", "AddListingActivity.okClicked() - isbn text is: " + isbnTextView.getText());
        //API CALL OR DB LOOKUP FOR BOOKS.


    }

    /**
     *
     * @param isbn
     * @return whether or not the scanned barcode is a valid ISBN13 number.
     */
    public boolean isISBN13Valid(String isbn) {
        int check = 0;
        for (int i = 0; i < 12; i += 2) {
            check += Integer.valueOf(isbn.substring(i, i + 1));
        }
        for (int i = 1; i < 12; i += 2) {
            check += Integer.valueOf(isbn.substring(i, i + 1)) * 3;
        }
        check += Integer.valueOf(isbn.substring(12));
        return check % 10 == 0;
    }

}
