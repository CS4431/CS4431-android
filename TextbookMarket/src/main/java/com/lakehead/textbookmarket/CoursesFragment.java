package com.lakehead.textbookmarket;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    ArrayList<Course> courseList;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            rootView = inflater.inflate(R.layout.fragment_courses, container, false);
            courseListView = (ListView)rootView.findViewById(R.id.course_list_view);

            if(savedInstanceState != null)
            {
                Log.d("CoursesFragment", "onCreateView() -> " + "Found saved instance state. Loading Course list from it...");
                courseList = savedInstanceState.getParcelableArrayList("courseList");
                final CourseArrayAdapter courseAdapter = new CourseArrayAdapter(this.getActivity(), courseList);
                courseListView.setAdapter(courseAdapter);
            }
            else
            {
            Log.d("CoursesFragment", "onCreateView() -> " + "No Saved Instance state. Loading Course list from API...");
            NameValuePair ext = new BasicNameValuePair("ext", "json");
            NameValuePair count = new BasicNameValuePair("count", "100");
            new GetJSONArrayTask(this, "/api/course").execute(ext, count);
            }
            return rootView;
        }


    @Override
    public void onPause()
    {
        Log.d("CoursesFragment", "onPause() -> " + "paused fragment.");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.d("CoursesFragment", "onResume() -> " + "resumed fragment.");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d("CoursesFragment", "onSaveInstanceState() -> " + "state saved for fragment.");
        outState.putParcelableArrayList("courseList", courseList);
        super.onSaveInstanceState(outState);

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
        courseList = new ArrayList<Course>();
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
        courseListView.setAdapter(courseAdapter);
    }
}
