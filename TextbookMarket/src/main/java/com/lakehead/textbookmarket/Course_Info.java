package com.lakehead.textbookmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Course_Info extends Activity {
    Button bookButton;
    private AlertDialog alertDialogStores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__info);
        bookButton = (Button) findViewById(R.id.bookButton);
        bookButton.setBackgroundResource(R.drawable.showbooksbutton);


        ObjectItem[] ObjectItemData = new ObjectItem[20];

        ObjectItemData[0] = new ObjectItem(91, "Mercury");
        ObjectItemData[1] = new ObjectItem(92, "Watson");
        ObjectItemData[2] = new ObjectItem(93, "Nissan");
        ObjectItemData[3] = new ObjectItem(94, "Puregold");
        ObjectItemData[4] = new ObjectItem(95, "SM");
        ObjectItemData[5] = new ObjectItem(96, "7 Eleven");
        ObjectItemData[6] = new ObjectItem(97, "Ministop");
        ObjectItemData[7] = new ObjectItem(98, "Fat Chicken");
        ObjectItemData[8] = new ObjectItem(99, "Master Siomai");
        ObjectItemData[9] = new ObjectItem(100, "Mang Inasal");
        ObjectItemData[10] = new ObjectItem(101, "Mercury 2");
        ObjectItemData[11] = new ObjectItem(102, "Watson 2");
        ObjectItemData[12] = new ObjectItem(103, "Nissan 2");
        ObjectItemData[13] = new ObjectItem(104, "Puregold 2");
        ObjectItemData[14] = new ObjectItem(105, "SM 2");
        ObjectItemData[15] = new ObjectItem(106, "7 Eleven 2");
        ObjectItemData[16] = new ObjectItem(107, "Ministop 2");
        ObjectItemData[17] = new ObjectItem(108, "Fat Chicken 2");
        ObjectItemData[18] = new ObjectItem(109, "Master Siomai 2");
        ObjectItemData[19] = new ObjectItem(110, "Mang Inasal 2");

        // our adapter instance
        SpecificListingArrayAdapter adapter = new SpecificListingArrayAdapter(Course_Info.this, ObjectItemData);

        // create a new ListView, set the adapter and item click listener
        final ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    Log.d("debug","There is a listing for the book and we would go to the listing here");
                    alertDialogStores = new AlertDialog.Builder(Course_Info.this).setView(listViewItems).setTitle("Stores").show();

            }
        });

        Intent intent = getIntent();
        Course thisCourse = intent.getParcelableExtra("course");

        ((TextView)findViewById(R.id.Title)).setText("Title: " + thisCourse.get_title());
        ((TextView)findViewById(R.id.CCode)).setText("Course Code: " + thisCourse.get_code());
        ((TextView)findViewById(R.id.Professor)).setText("Instructor: " + thisCourse.get_instructor());
        ((TextView)findViewById(R.id.courseSection)).setText("Section: " + thisCourse.get_section());
        ((TextView)findViewById(R.id.courseTerm)).setText("Term: " + thisCourse.get_term());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course__info, menu);
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

}
