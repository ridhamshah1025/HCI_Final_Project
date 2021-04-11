package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.gesture.GestureLibrary;
import android.gesture.GestureLibraries;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.gesture.Prediction;
import android.gesture.Gesture;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnGesturePerformedListener {
    List<String> Characters;
    List<String> Names;
    Map<String, List<String>> Contacts;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    Button button12;
    LayoutInflater layoutInflater;
    int lastExpandedPosition = -1;
    private GestureLibrary GesLib;
    String predict;
    long currentTime;
    long startButtonTime;
    long childClickTime;
    ArrayList<String> taskList = new ArrayList<String>();
    ArrayList<Long> taskTime = new ArrayList<Long>();
    int startIndex;
    int currentIndex;


    Object selectedChildItem;
    long getSelectedId;
    long getSelectedPosition;
    Object getSelectedItem;
    long getSelectedItemId;
    int getSelectedItemPosition;
    StringBuilder data = new StringBuilder();


//    RelativeLayout layout;
//    public boolean check_con=true;


    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTime = System.currentTimeMillis();
        Intent intent = getIntent();
        startButtonTime = intent.getExtras().getLong("startButtonTime");
        taskList= intent.getExtras().getStringArrayList("taskList");
        startIndex = intent.getExtras().getInt("startIndex");
        currentIndex = intent.getExtras().getInt("currentIndex");
        taskTime= (ArrayList<Long>) intent.getSerializableExtra("taskTime");
        System.out.println("task list"+taskList+"size"+taskList.size());
        System.out.println("task Time"+taskTime+"size"+taskTime.size());

        currentTime = System.currentTimeMillis();
        System.out.println("button click time"+startButtonTime);
        System.out.println("CurrentTime"+currentTime);
        System.out.println("diff"+(currentTime-startButtonTime));
        Toast.makeText(MainActivity.this,"diff"+(currentTime-startButtonTime),Toast.LENGTH_SHORT).show();

        CreateCharacterList();
        CreateNamesList();

        GesLib = GestureLibraries.fromRawResource(this,R.raw.gesture);
        if(!GesLib.load())
        {
            System.out.println("Not LOADED");
            finish();
        }
        GestureOverlayView GesOver = (GestureOverlayView) findViewById(R.id.ges1);
        GesOver.addOnGesturePerformedListener(this);

        expandableListView = findViewById(R.id.e_list);
        button12 = findViewById(R.id.button12);

        expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
        expandableListView.setAdapter(expandableListAdapter);

        layoutInflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) layoutInflater.inflate(R.layout.footer_layout, expandableListView, false);
        expandableListView.addFooterView(footer);

        layoutInflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.list_header, expandableListView, false);
        expandableListView.addHeaderView(header);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    lastExpandedPosition = -1;
                    System.out.println("group position "+groupPosition);
                    String selected = expandableListAdapter.getGroup(groupPosition).toString();
                    Toast.makeText(MainActivity.this,selected,Toast.LENGTH_SHORT).show();
                    if(lastExpandedPosition!=-1 && groupPosition != lastExpandedPosition){
                        expandableListView.collapseGroup(lastExpandedPosition);
                    }
                    expandableListView.setSelectionFromTop(groupPosition,0);
//                    expandableListView.setSelectedGroup(groupPosition);
                    lastExpandedPosition = groupPosition;
                }
            });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    childClickTime = System.currentTimeMillis()-currentTime;
                    System.out.println("childClickTime"+childClickTime);
                    String selected = expandableListAdapter.getChild(groupPosition,childPosition).toString();
                    System.out.println("group position "+groupPosition+" child "+childPosition);
                    if(currentIndex==0)
                    {
                        System.out.println("44");
                        if(groupPosition==0 && childPosition==0)
                        {
                            System.out.println("55");
                            if(taskTime.size()==0)
                            {
                                System.out.println("66");
                                taskTime.add(0,childClickTime);
                                System.out.println("task list"+taskList+"size"+taskList.size());
                                System.out.println("task Time"+taskTime+"size"+taskTime.size());
                                System.out.println("done");
                                startIndex+=1;
                                currentIndex+=1;
                                System.out.println("cindex "+currentIndex+" startindex "+startIndex);
                                Intent intent1=new Intent(MainActivity.this,StartScreen.class);
                                intent1.putExtra("taskList",taskList);
                                intent1.putExtra("startIndex",startIndex);
                                intent1.putExtra("currentIndex",currentIndex);
                                intent1.putExtra("taskTime",taskTime);
                                startActivity(intent1);
                            }
                        }
                    }
                    else if(currentIndex==1)
                    {
                        System.out.println("11");
                        if(groupPosition==0 && childPosition==1)
                        {
                            System.out.println("22");
                            if(taskTime.size()==1)
                            {
                                System.out.println("33");
                                try {
                                    taskTime.add(1,childClickTime);
                                    System.out.println("task list"+taskList+"size"+taskList.size());
                                    System.out.println("task Time"+taskTime+"size"+taskTime.size());
                                    System.out.println("done");
                                    startIndex+=1;
                                    currentIndex+=1;
                                    System.out.println("kkkkks"+startIndex);
                                    System.out.println("kkkkkc"+currentIndex);
                                    data.append("taskNo,time");
                                    System.out.println("kkkkkkk");
                                    for(int i = 0; i<2; i++)
                                    {
                                        data.append("\n").append(taskList.get(i)).append(",").append(taskTime.get(i));
                                    }
                                    System.out.println("kkkkkk"+data);
                                    Intent intent2=new Intent(MainActivity.this,Finish.class);
                                    intent2.putExtra("taskList",taskList);
                                    intent2.putExtra("startIndex",startIndex);
                                    intent2.putExtra("currentIndex",currentIndex);
                                    intent2.putExtra("taskTime",taskTime);
                                    String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZ", Locale.getDefault()).format(new Date());
                                    String filename = date + ".csv";
                                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
                                    System.out.println(file);
                                    FileOutputStream out = null;
                                    out = new FileOutputStream(file);
                                    out.write((data.toString()).getBytes());
                                    out.close();
                                    System.out.println("789");
                                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                    intent2.putExtra("fileLocation", file.getAbsolutePath());
                                    startActivity(intent2);
                                } catch (FileNotFoundException e) {
                                    System.out.println("123");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    System.out.println("456");
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    return true;
                }
            });

        button12.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Toast.makeText(MainActivity.this,"Double tap",Toast.LENGTH_SHORT).show();
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if (lastExpandedPosition!=-1){
                        for(int i=0;i<26;i++)
                        {
                            System.out.println(i);
                            expandableListView.collapseGroup(i);
                        }
                        expandableListView.collapseGroup(lastExpandedPosition);
                        Toast.makeText(MainActivity.this,"Collapsed",Toast.LENGTH_SHORT).show();
                    }
                    return super.onSingleTapConfirmed(e);
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
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
        String[] contacts_e = {"contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2"};
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
        String[] contacts_x = {"contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2"};
        String[] contacts_y = {"contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2"};
        String[] contacts_z = {"contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2","contact 1","contact2"};




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
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        ArrayList<Prediction> ObjPrediction = GesLib.recognize(gesture);
        if(ObjPrediction.size()>0 && ObjPrediction.get(0).score>1)
        {
            for(int i=0;i<26;i++)
            {
                System.out.println(i);
                expandableListView.collapseGroup(i);
            }
            System.out.println(ObjPrediction);
            predict = ObjPrediction.get(0).name;
            predict = predict.toString().toLowerCase();
            System.out.println("predict  "+predict);

            if(predict.equals("a"))
            {
                expandableListView.expandGroup(0);
            }
            else if(predict.equals("b"))
            {
                expandableListView.expandGroup(1);
            }
            else if(predict.equals("c"))
            {
                expandableListView.expandGroup(2);
            }
            else if(predict.equals("d"))
            {
                expandableListView.expandGroup(3);
            }
            else if(predict.equals("e"))
            {
                expandableListView.expandGroup(4);
            }
            else if(predict.equals("f"))
            {
                expandableListView.expandGroup(5);
            }
            else if(predict.equals("g"))
            {
                expandableListView.expandGroup(6);
            }
            else if(predict.equals("h"))
            {
                expandableListView.expandGroup(7);
            }
            else if(predict.equals("i"))
            {
                expandableListView.expandGroup(8);
            }
            else if(predict.equals("j"))
            {
                expandableListView.expandGroup(9);
            }
            else if(predict.equals("k"))
            {
                expandableListView.expandGroup(10);
            }
            else if(predict.equals("l"))
            {
                expandableListView.expandGroup(11);
            }
            else if(predict.equals("m"))
            {
                expandableListView.expandGroup(12);
            }
            else if(predict.equals("n"))
            {
                expandableListView.expandGroup(13);
            }
            else if(predict.equals("o"))
            {
                expandableListView.expandGroup(14);
            }
            else if(predict.equals("p"))
            {
                expandableListView.expandGroup(15);
            }
            else if(predict.equals("q"))
            {
                expandableListView.expandGroup(16);
            }
            else if(predict.equals("r"))
            {
                expandableListView.expandGroup(17);
            }
            else if(predict.equals("s"))
            {
                expandableListView.expandGroup(18);
            }
            else if(predict.equals("t"))
            {
                expandableListView.expandGroup(19);
            }
            else if(predict.equals("u"))
            {
                expandableListView.expandGroup(20);
            }
            else if(predict.equals("v"))
            {
                expandableListView.expandGroup(21);
            }
            else if(predict.equals("w"))
            {
                expandableListView.expandGroup(22);
            }
            else if(predict.equals("x"))
            {
                expandableListView.expandGroup(23);
            }
            else if(predict.equals("y"))
            {
                expandableListView.expandGroup(24);
            }
            else if(predict.equals("z"))
            {
                expandableListView.expandGroup(25);
            }
            Toast.makeText(this,predict,Toast.LENGTH_SHORT).show();
        }
    }
}