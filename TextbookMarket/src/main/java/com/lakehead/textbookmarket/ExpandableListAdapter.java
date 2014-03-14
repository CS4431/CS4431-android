package com.lakehead.textbookmarket;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Course>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Course>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {



        String deptTitle = _listDataHeader.get(groupPosition);

        Course course = _listDataChild.get(deptTitle).get(childPosition);

        LayoutInflater inflater = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.courses_item, parent, false);

        TextView titleTextView = (TextView)rowView.findViewById(R.id.courseTitle);
        titleTextView.setText(course.get_title());

        //commented out as we currently have no need for an Icon for the class?
        //ImageView iconImageView = (ImageView)rowView.findViewById(R.id.icon);
        //this is temp shit until we decide if we want an icon for courselist.
        //iconImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));

        TextView sectionTextView = (TextView)rowView.findViewById(R.id.courseInfo);
        sectionTextView.setText(course.get_code()+ " " + course.get_section());

        TextView instructorTextView = (TextView)rowView.findViewById(R.id.courseInstructor);
        instructorTextView.setText(course.get_instructor());

        return rowView;


        /*
        // below is old crap

        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.courses_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.courseTitle);

        //txtListChild.setText(childText);
        */
        //return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.courses_item_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.courses_department_textview);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}