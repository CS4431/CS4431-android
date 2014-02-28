package com.lakehead.textbookmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The adapter used to populate the list view for the courses fragment
 */
public class CourseArrayAdapter extends ArrayAdapter<Course> {
    private final Context context;
    private final Course[] courses;

    /**
     *
     * @param context Used to get the inflater for the object.
     * @param courses Array of Course objects to display.
     */
    public CourseArrayAdapter(Context context, Course[] courses) {
        super(context, R.layout.books_item_view,  courses);
        this.context = context;
        this.courses = courses;
    }

    /**
     *
     * @param position The Array position of the iterable list
     * @param convertView unused
     * @param parent The parent of the current View.
     * @return Returns the now-formulated row of data.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.courses_item_view, parent, false);

        TextView titleTextView = (TextView)rowView.findViewById(R.id.courseTitle);
        titleTextView.setText(courses[position].get_title());

        //commented out as we currently have no need for an Icon for the class?
        //ImageView iconImageView = (ImageView)rowView.findViewById(R.id.icon);
        //this is temp shit until we decide if we want an icon for courselist.
        //iconImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));

        TextView sectionTextView = (TextView)rowView.findViewById(R.id.courseInfo);
        sectionTextView.setText(courses[position].get_code()+ " " + courses[position].get_section());

        TextView instructorTextView = (TextView)rowView.findViewById(R.id.courseInstructor);
        instructorTextView.setText(courses[position].get_instructor());

        return rowView;


    }

}
