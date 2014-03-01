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
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            rootView = inflater.inflate(R.layout.fragment_courses, container, false);
            courseListView = (ListView)rootView.findViewById(R.id.course_list_view);


            NameValuePair ext = new BasicNameValuePair("ext", "json");
            NameValuePair count = new BasicNameValuePair("count", "20");
            new GetJSONArrayTask(this, "/api/course").execute(ext, count);
            return rootView;
        }


    @Override
    public void onTaskCompleted(Object obj) {
        jArray = (JSONArray)obj;
        List<Course> courseList = new ArrayList<Course>();
        try{
            for(int i = 0; i < jArray.length(); i++){
                courseList.add(new Course(
                jArray.getJSONObject(i).getJSONObject("data").getInt("id"),
                jArray.getJSONObject(i).getJSONObject("data").getString("title"),
                jArray.getJSONObject(i).getJSONObject("data").getString("code"),
                jArray.getJSONObject(i).getJSONObject("data").getString("section"),
                jArray.getJSONObject(i).getJSONObject("data").getInt("department_id"),
                jArray.getJSONObject(i).getJSONObject("data").getString("instructor"),
                jArray.getJSONObject(i).getJSONObject("data").getString("term")));
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
