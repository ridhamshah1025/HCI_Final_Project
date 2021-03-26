package com.example.hci_final_project;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String,List<String>> Contacts;
    private List<String> Characters;

    public MyExpandableListAdapter(Context context, List<String> Characters, Map<String, List<String>> Contacts) {
        this.context = context;
        this.Characters=Characters;
        this.Contacts=Contacts;

    }

    @Override
    public int getGroupCount() {
        return Contacts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Contacts.get(Characters.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Characters.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Contacts.get(Characters.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String character = getGroup(groupPosition).toString();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.characters,null);
        }
        TextView name = convertView.findViewById(R.id.character);
        name.setTypeface(null, Typeface.BOLD);
        name.setText(character);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String name = getChild(groupPosition,childPosition).toString();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.names,null);
        }
        TextView item = convertView.findViewById(R.id.names);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(name);
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
