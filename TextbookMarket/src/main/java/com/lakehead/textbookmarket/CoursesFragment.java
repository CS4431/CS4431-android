package com.lakehead.textbookmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Master on 2/18/14.
 */
public class CoursesFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_courses, container, false);
            ListView courseListView = (ListView)rootView.findViewById(R.id.course_list_view);
            Course course1 = new Course(1,"Introduction to Programming","COMP-1141" ,"FA", 1,"Dr Jinan Fiaidhi","13F" );
            Course course2 = new Course(1,"Biology of Human Variation","ANTH-2110" ,"FA", 1,"Dr Tamara Varney","13F" );
            Course course3 = new Course(50,"Forest Pathology", "BIOL-3213","FA", 3,"Dr Leonard J. Hutchinson","13F" );
            Course course4 = new Course(76,"Endocrinology","BIOL-4830" ,"FA", 3,"Dr Robert J. Omeljanuk","13F" );
            Course course5 = new Course(150,"Principles of Entrepreneureship","BUSI-3215" ,"FA", 4,"Ken Hartviksen","13F" );
            Course course6 = new Course(200,"Website Design & Admin","BUSI-3273" ,"FA", 4,"Dr Alexander Serenko","13F" );
            Course course7 = new Course(250,"Roman History","CLAS-2203" ,"YA", 6,"Ms Catherine Hudson","13Y" );
            Course course8 = new Course(300,"Teachers Aboriginal Learners","EDUC-2130" ,"FA", 9,"Ms Shy-Anne L. Hovorka","13F" );
            Course course9 = new Course(350,"C&I Intermediate Mathematics","EDUC-4234" ,"FA", 9,"Miss Jennifer Holm","13F" );
            Course course10 = new Course(650,"Surveying","ENGI-1235" ,"FA", 10,"Ms Alison Parsons","13F" );
            Course[] courseList = new Course[] {course1, course2, course3, course4, course5, course6, course7, course8, course9, course10};

            final CourseArrayAdapter courseAdapter = new CourseArrayAdapter(this.getActivity(), courseList);
            courseListView.setAdapter(courseAdapter);
            courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), Book_Info.class);
                    startActivity(intent);
                }});

            return rootView;
        }

}
