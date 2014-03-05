package com.lakehead.textbookmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The fragment used in the "Books" tab of MainActivity
 */
public class CoursesFragmentExpand extends Fragment implements OnTaskCompleted{
    JSONArray jArray;
    ListView courseListView;
    View rootView;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            rootView = inflater.inflate(R.layout.fragment_courses_expand, container, false);
            //courseListView = (ListView)rootView.findViewById(R.id.course_list_view);


            //NameValuePair ext = new BasicNameValuePair("ext", "json");
            //NameValuePair count = new BasicNameValuePair("count", "100");
            //new GetJSONArrayTask(this, "/api/course").execute(ext, count);


            expListView = (ExpandableListView) rootView.findViewById(R.id.courses_expand_lv);

            prepareListData();

            listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);



            return rootView;
        }



    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
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
        courseListView.setAdapter(courseAdapter);
    }
}
