package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.gesture.GestureLibrary;
import android.gesture.GestureLibraries;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;

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


//    RelativeLayout layout;
//    public boolean check_con=true;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateCharacterList();
        CreateNamesList();

        expandableListView = findViewById(R.id.e_list);


//        layout = findViewById(R.id.layout1);



        expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
        expandableListView.setAdapter(expandableListAdapter);

//        System.out.println("1"+check_con);

//        expandableListView.setOnTouchListener(new View.OnTouchListener() {
//            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener(){
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    check_con = false;
//                    Toast.makeText(getApplicationContext(),"Long Press",Toast.LENGTH_SHORT).show();
//                    super.onLongPress(e);
//
//                }
//
//                @Override
//                public boolean onDoubleTap(MotionEvent e) {
//                    check_con = false;
//                    Toast.makeText(getApplicationContext(),"Double Tap",Toast.LENGTH_SHORT).show();
//                    return super.onDoubleTap(e);
//                }
//            });
//
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                gestureDetector.onTouchEvent(event);
//                System.out.println("2"+check_con);
//                if(!check_con)
//                {
//                    System.out.println("3"+check_con);
//                    check_con=true;
//                    System.out.println("4"+check_con);
//                    return true;
//                }
//                System.out.println("5"+check_con);
//                return false;
//
//            }
//        });



        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int lastExpandedPosition = -1;
                @Override

                public void onGroupExpand(int groupPosition) {
                    String selected = expandableListAdapter.getGroup(groupPosition).toString();

                    Toast.makeText(MainActivity.this,selected,Toast.LENGTH_SHORT).show();
//                    System.out.println("6"+check_con);
//                    if(check_con){
//                        System.out.println("7"+check_con);
//
//                    }

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

                    Toast.makeText(MainActivity.this,selected,Toast.LENGTH_SHORT).show();
                    return true;
//                    System.out.println("8"+check_con);
//                    if(check_con){
//                        Toast.makeText(MainActivity.this,selected,Toast.LENGTH_SHORT).show();
//                        System.out.println("9"+check_con);
//                        return true;
//                    }
//                    System.out.println("10"+check_con);
//                    return false;
                }
            });

//        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Toast.makeText(MainActivity.this,"long item",Toast.LENGTH_SHORT).show();
//
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
        String[] contacts_q = {"contact 1","contact2"};
        String[] contacts_r = {"contact 1","contact2"};
        String[] contacts_s = {"contact 1","contact2"};
        String[] contacts_t = {"contact 1","contact2"};
        String[] contacts_u = {"contact 1","contact2"};
        String[] contacts_v = {"contact 1","contact2"};
        String[] contacts_w = {"contact 1","contact2"};
        String[] contacts_x = {"contact 1","contact2"};
        String[] contacts_y = {"contact 1","contact2"};
        String[] contacts_z = {"contact 1","contact2"};




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
            else if(characters.equals("Q")){
                LoadNames(contacts_q);
            }
            else if(characters.equals("R")){
                LoadNames(contacts_r);
            }
            else if(characters.equals("S")){
                LoadNames(contacts_s);
            }
            else if(characters.equals("T")){
                LoadNames(contacts_t);
            }
            else if(characters.equals("U")){
                LoadNames(contacts_u);
            }
            else if(characters.equals("V")){
                LoadNames(contacts_v);
            }
            else if(characters.equals("W")){
                LoadNames(contacts_w);
            }
            else if(characters.equals("X")){
                LoadNames(contacts_x);
            }
            else if(characters.equals("Y")){
                LoadNames(contacts_y);
            }
            else if(characters.equals("Z")){
                LoadNames(contacts_z);
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
        Characters.add("Q");
        Characters.add("R");
        Characters.add("S");
        Characters.add("T");
        Characters.add("U");
        Characters.add("V");
        Characters.add("W");
        Characters.add("X");
        Characters.add("Y");
        Characters.add("Z");
    }



}