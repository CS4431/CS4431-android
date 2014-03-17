package com.lakehead.textbookmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Course_Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__info);

        /*
                    int cid = courseAdapter.getItem(position).get_id();
                    int departmentid = courseAdapter.getItem(position).get_department_id();
                    String ctitle = courseAdapter.getItem(position).get_title();
                    String ccode = courseAdapter.getItem(position).get_code();
                    String csection = courseAdapter.getItem(position).get_section();
                    String cinstructor = courseAdapter.getItem(position).get_instructor();
                    String cterm = courseAdapter.getItem(position).get_term();

         */


        Intent intent = getIntent();
        int cid = intent.getIntExtra("cid", 0);
        int departmentid = intent.getIntExtra("deparmentid", 0);
        String ctitle = intent.getStringExtra("ctitle");
        String ccode = intent.getStringExtra("ccode");
        String csection = intent.getStringExtra("csection");
        String cinstructor = intent.getStringExtra("cinstructor");
        String cterm = intent.getStringExtra("cterm");
        ((TextView)findViewById(R.id.Title)).setText("Title: " + ctitle);
        ((TextView)findViewById(R.id.CCode)).setText("Course Code: " + ccode);
        ((TextView)findViewById(R.id.Professor)).setText("Instructor: " + cinstructor);
        ((TextView)findViewById(R.id.courseSection)).setText("Section: " + csection);
        ((TextView)findViewById(R.id.courseTerm)).setText("Term: " + cterm);
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
