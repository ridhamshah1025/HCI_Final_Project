package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    List<String> Characters;
    List<String> Names;
    Map<String, List<String>> Contacts;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateCharacterList();
        CreateNamesList();
        expandableListView = findViewById(R.id.e_list);
        expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition!=-1 && groupPosition != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selected = expandableListAdapter.getChild(groupPosition,childPosition).toString();
                Toast.makeText(MainActivity.this,selected,Toast.LENGTH_LONG).show();
                return true;
            }
        });
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                String selected = expandableListAdapter.getGroup(groupPosition).toString();
//                Toast.makeText(MainActivity.this,selected,Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });
    }

    private void CreateNamesList() {
        String[] contacts_a = {"contact 1","contact2"};
        String[] contacts_b = {"contact 1","contact2"};
        String[] contacts_c = {"contact 1","contact2"};
        String[] contacts_d = {"contact 1","contact2"};
        String[] contacts_e = {"contact 1","contact2"};
        String[] contacts_f = {"contact 1","contact2"};
        String[] contacts_g = {"contact 1","contact2"};
        String[] contacts_h = {"contact 1","contact2"};
        String[] contacts_i = {"contact 1","contact2"};
        String[] contacts_j = {"contact 1","contact2"};
        String[] contacts_k = {"contact 1","contact2"};
        String[] contacts_l = {"contact 1","contact2"};
        String[] contacts_m = {"contact 1","contact2"};
        String[] contacts_n = {"contact 1","contact2"};
        String[] contacts_o = {"contact 1","contact2"};
        String[] contacts_p = {"contact 1","contact2"};



        Contacts = new HashMap<String, List<String>>();
        for(String characters : Characters){
            if(characters.equals("A")){
                LoadNames(contacts_a);
            }
            else if(characters.equals("B")){
                LoadNames(contacts_b);
            }
            else if(characters.equals("C")){
                LoadNames(contacts_c);
            }
            else if(characters.equals("D")){
                LoadNames(contacts_d);
            }
            else if(characters.equals("E")){
                LoadNames(contacts_e);
            }
            else if(characters.equals("F")){
                LoadNames(contacts_f);
            }
            else if(characters.equals("G")){
                LoadNames(contacts_g);
            }
            else if(characters.equals("H")){
                LoadNames(contacts_h);
            }
            else if(characters.equals("I")){
                LoadNames(contacts_i);
            }
            else if(characters.equals("J")){
                LoadNames(contacts_j);
            }
            else if(characters.equals("K")){
                LoadNames(contacts_k);
            }
            else if(characters.equals("L")){
                LoadNames(contacts_l);
            }
            else if(characters.equals("M")){
                LoadNames(contacts_m);
            }
            else if(characters.equals("N")){
                LoadNames(contacts_n);
            }
            else if(characters.equals("O")){
                LoadNames(contacts_o);
            }
            else if(characters.equals("P")){
                LoadNames(contacts_p);
            }
            Contacts.put(characters,Names);

        }

    }

    private void LoadNames(String[] character_contacts) {
        Names = new ArrayList<>();
        for(String names: character_contacts){
            Names.add(names);
        }
    }

    private void CreateCharacterList() {
        Characters = new ArrayList<>();
        Characters.add("A");
        Characters.add("B");
        Characters.add("C");
        Characters.add("D");
        Characters.add("E");
        Characters.add("F");
        Characters.add("G");
        Characters.add("H");
        Characters.add("I");
        Characters.add("J");
        Characters.add("K");
        Characters.add("L");
        Characters.add("M");
        Characters.add("N");
        Characters.add("O");
        Characters.add("P");
    }

}