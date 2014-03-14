package com.lakehead.textbookmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The fragment used in the "Books" tab of MainActivity
 */
public class CoursesFragment extends Fragment implements OnTaskCompleted{
    JSONArray jArray;
    ListView courseListView;
    View rootView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {

            View rootView = inflater.inflate(R.layout.fragment_courses, container, false);
            courseListView = (ListView)rootView.findViewById(R.id.course_list_view);


            //rootView = inflater.inflate(R.layout.fragment_courses, container, false);
            //courseListView = (ListView)rootView.findViewById(R.id.course_list_view);


            NameValuePair ext = new BasicNameValuePair("ext", "json");
            NameValuePair count = new BasicNameValuePair("count", "100");
            new GetJSONArrayTask(this, "/api/course").execute(ext, count);
            return rootView;
        }


    @Override
    public void onTaskCompleted(Object obj) {
        jArray = (JSONArray)obj;
        int id;
        String title;
        String code;
        String section;
        int department_id;
        String instructor;
        String term;
        JSONObject courseDataNode;
        List<Course> courseList = new ArrayList<Course>();
        try{
            for(int i = 0; i < jArray.length(); i++){
                courseDataNode = jArray.getJSONObject(i).getJSONObject("data");

                try{
                    id = courseDataNode.getInt("id");
                }catch(Exception e){
                    id = 0;
                    Log.e("CoursesFragment", "OnTaskCompleted() -> Couldn't parse JSON for id: " + e.toString());
                    continue;
                }
                try{
                    title = courseDataNode.getString("title");
                }catch(Exception e){
                    Log.e("CoursesFragment", "OnTaskCompleted() -> Couldn't parse JSON for title: " + e.toString());
                    continue;
                }
                try{
                    code = courseDataNode.getString("code");
                }catch(Exception e){
                    code = "N/A";
                    Log.e("CoursesFragment", "OnTaskCompleted() -> Couldn't parse JSON for code: " + e.toString());
                }
                try{
                    section = courseDataNode.getString("section");
                }catch(Exception e){
                    section = "N/A";
                    Log.e("CoursesFragment", "OnTaskCompleted() -> Couldn't parse JSON for section: " + e.toString());
                }
                try{
                    department_id = courseDataNode.getInt("department_id");
                }catch(Exception e){
                    //maybe we should hold some uncategorized classes?
                    department_id = -1;
                    Log.e("CoursesFragment", "OnTaskCompleted() -> Couldn't parse JSON for department_id: " + e.toString());
                }
                try{
                    instructor = courseDataNode.getString("instructor");
                }catch(Exception e){
                    //maybe we should hold some uncategorized classes?
                    instructor = "N/A";
                    Log.e("CoursesFragment", "OnTaskCompleted() -> Couldn't parse JSON for instructor: " + e.toString());
                }
                try{
                    term = courseDataNode.getString("term");
                }catch(Exception e){
                    term = "N/A";
                    Log.e("CoursesFragment", "OnTaskCompleted() -> Couldn't parse JSON for term: " + e.toString());
                }

                courseList.add(new Course(id,title,code,section,department_id,instructor,term));
            }
        }
        catch(Exception e){
            Log.e("CoursesFragment", "onTaskCompleted() -> " + e.toString());
            e.printStackTrace();
        }

        final CourseArrayAdapter courseAdapter = new CourseArrayAdapter(this.getActivity(), courseList);


        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Course_Info.class);
                Bundle extras = new Bundle();

                int cid = courseAdapter.getItem(position).get_id();
                int departmentid = courseAdapter.getItem(position).get_department_id();
                String ctitle = courseAdapter.getItem(position).get_title();
                String ccode = courseAdapter.getItem(position).get_code();
                String csection = courseAdapter.getItem(position).get_section();
                String cinstructor = courseAdapter.getItem(position).get_instructor();
                String cterm = courseAdapter.getItem(position).get_term();
                Log.d("Debug", "Instructor is: " + cinstructor);
                extras.putInt("cid",cid);
                extras.putInt("departmentid",departmentid);
                extras.putString("ctitle", ctitle);
                extras.putString("ccode", ccode);
                extras.putString("csection", csection);
                extras.putString("cinstructor",cinstructor);
                extras.putString("cterm",cterm);
                intent.putExtras(extras);
                startActivity(intent);
            }});

        courseListView.setAdapter(courseAdapter);
    }
}
