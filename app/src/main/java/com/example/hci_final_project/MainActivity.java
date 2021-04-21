package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
    ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
    ArrayList<String> taskDoneList = new ArrayList<String>();
    ArrayList<String> drawCharacterList = new ArrayList<String>();
    ArrayList<Integer> drawCharacterPosition = new ArrayList<Integer>();
    String lastCharacterList =null;  // first character
    String previousCharacter = null; //second character
    int lastCharacterPosition;
    String currentPredict =null;
    int taskNumber;
    int counter;
    int currentIndex;
    int showIndex;

    int secondCharacter=0;


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


        Intent intent = getIntent();
        startButtonTime = intent.getExtras().getLong("startButtonTime");
        taskList= intent.getExtras().getStringArrayList("taskList");

        taskDoneList= intent.getExtras().getStringArrayList("taskDoneList");
        counter = intent.getExtras().getInt("counter");
        currentIndex = intent.getExtras().getInt("currentIndex");
        taskTime= (ArrayList<Long>) intent.getSerializableExtra("taskTime");

        showIndex = intent.getExtras().getInt("showIndex");

        taskNumber = intent.getExtras().getInt("taskNumber");

        taskNumbers= intent.getExtras().getIntegerArrayList("taskNumbers");


//        System.out.println("task Number"+taskNumber);
//        System.out.println("task list"+taskList+"size"+taskList.size());
//        System.out.println("task Time"+taskTime+"size"+taskTime.size());
//        System.out.println("task Numbers"+taskNumbers+"size"+taskNumbers.size());


//        int taskNoI = currentIndex+1;
//        String taskNo = String.valueOf(taskNoI);

        String screenTitle = "Task "+String.valueOf(showIndex)+"/5 :";
        this.setTitle(screenTitle);

        currentTime = System.currentTimeMillis();
//        System.out.println("button click time"+startButtonTime);
//        System.out.println("CurrentTime"+currentTime);
//        System.out.println("diff"+(currentTime-startButtonTime));
        Toast.makeText(MainActivity.this,"diff"+(currentTime-startButtonTime),Toast.LENGTH_SHORT).show();

        CreateCharacterList();
        CreateNamesList();

        GesLib = GestureLibraries.fromRawResource(this,R.raw.gesture);
        if(!GesLib.load())
        {
//            System.out.println("Not LOADED");
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
//                    System.out.println("group position "+groupPosition);
                    String selected = expandableListAdapter.getGroup(groupPosition).toString();
//                    Toast.makeText(MainActivity.this,selected,Toast.LENGTH_SHORT).show();
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
//                    System.out.println("childClickTime"+childClickTime);
                    String selected = expandableListAdapter.getChild(groupPosition,childPosition).toString();
//                    System.out.println("group position "+groupPosition+" child "+childPosition);

                    if (showIndex < 2)
                    {
//                        System.out.println("chaddi");
//                        if (groupPosition < 3 && childPosition < 2)
//                        {
//                            System.out.println("chaddi1");
//                            updateData(groupPosition,childPosition,childClickTime,taskList,taskTime,
//                                    counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList);
//                        }

                        if  (   (taskNumber==0 && groupPosition ==0 && childPosition == 0)||
                                (taskNumber==1 && groupPosition ==0 && childPosition == 1)||
                                (taskNumber==2 && groupPosition ==1 && childPosition == 0)||
                                (taskNumber==3 && groupPosition ==1 && childPosition == 1)||
                                (taskNumber==4 && groupPosition ==2 && childPosition == 0)
                            )
                        {
//                            System.out.println("chaddi1");
                            updateData(groupPosition,childPosition,childClickTime,taskList,taskTime,
                                    counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList);
                        }
                        else
                            {
                                Toast.makeText(MainActivity.this,"False Attempt",Toast.LENGTH_SHORT).show();
                                updateFalseClickData(groupPosition,childPosition,childClickTime,taskList,taskTime,
                                        counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList);
                            }


                    }

                    else if(showIndex==2)
                    {
                        if  (   (taskNumber==0 && groupPosition == 0 && childPosition == 0)||
                                (taskNumber==1 && groupPosition == 0 && childPosition == 1)||
                                (taskNumber==2 && groupPosition == 1 && childPosition == 0)||
                                (taskNumber==3 && groupPosition == 1 && childPosition == 1)||
                                (taskNumber==4 && groupPosition == 2 && childPosition == 0)
                        )
                        {
//                            System.out.println("33");
                            try {
                                taskTime.add(childClickTime);
                                taskDoneList.add(taskList.get(taskNumber));
//                                System.out.println("task list"+taskList+"size"+taskList.size());
//                                System.out.println("task Time"+taskTime+"size"+taskTime.size());
//                                System.out.println("done");
                                showIndex+=1;
                                counter+=1;
                                currentIndex+=1;
//                                System.out.println("cindex " + currentIndex + " counter " + counter+ " showIndex " + showIndex);
                                data.append("taskNo,time");
//                                System.out.println("kkkkkkk");
                                for(int i = 0; i<2; i++)
                                {
                                    data.append("\n").append(taskDoneList.get(i)).append(",").append(taskTime.get(i));
                                }
//                                System.out.println("kkkkkk"+data);
                                Intent intent2=new Intent(MainActivity.this,Finish.class);
                                intent2.putExtra("taskList",taskList);
                                intent2.putExtra("taskDoneList",taskDoneList);
                                intent2.putExtra("taskNumber",taskNumber);
                                intent2.putExtra("showIndex",showIndex);
                                intent2.putExtra("counter",counter);
                                intent2.putExtra("currentIndex",currentIndex);
                                intent2.putExtra("taskTime",taskTime);
                                intent2.putExtra("taskNumbers",taskNumbers);
                                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                                String filename = date + ".csv";
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
//                            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), filename);
//                            File file = new File(getApplicationContext().getFileStreamPath(filename).getPath());
//                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+filename);
//                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+filename);
//                                System.out.println(file);
                                FileOutputStream out = new FileOutputStream(file);
                                out.write((data.toString()).getBytes());
                                out.close();
//                                System.out.println("789");
                                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                intent2.putExtra("fileLocation", file.getAbsolutePath());
                                startActivity(intent2);
                            } catch (FileNotFoundException e) {
//                                System.out.println("123");
                                e.printStackTrace();
                            } catch (IOException e) {
//                                System.out.println("456");
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"False Attempt",Toast.LENGTH_SHORT).show();
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
//                            System.out.println(i);
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

    private void updateFalseClickData(int groupPosition, int childPosition, long childClickTime, ArrayList<String> taskList, ArrayList<Long> taskTime, int counter, int currentIndex, int showIndex, ArrayList<Integer> taskNumbers, int taskNumber, ArrayList<String> taskDoneList) {
//        System.out.println("Wrong Click");
    }

    public void updateData(int groupPosition, int childPosition, long childClickTime, ArrayList<String> taskList, ArrayList<Long> taskTime, int counter, int currentIndex, int showIndex, ArrayList<Integer> taskNumbers, int taskNumber, ArrayList<String> taskDoneList)
    {

        taskTime.add(childClickTime);
        taskDoneList.add(taskList.get(taskNumber));
//        System.out.println("task list" + taskList + "size" + taskList.size());
//        System.out.println("task Time" + taskTime + "size" + taskTime.size());
//        System.out.println("task Done List" + taskDoneList + "size" + taskDoneList.size());
//        System.out.println("done");
        counter += 1;
        currentIndex += 1;
        showIndex += 1;
//        System.out.println("cindex " + currentIndex + " counter " + counter+ " showIndex " + showIndex);
        Intent intent1 = new Intent(MainActivity.this, StartScreen.class);
        intent1.putExtra("taskList", taskList);
        intent1.putExtra("counter", counter);
        intent1.putExtra("showIndex", showIndex);
        intent1.putExtra("currentIndex", currentIndex);
        intent1.putExtra("taskTime", taskTime);
        intent1.putExtra("taskNumbers", taskNumbers);
        intent1.putExtra("taskNumber", taskNumber);
        intent1.putExtra("taskDoneList", taskDoneList);
        startActivity(intent1);
    }

    @Override
    public void onBackPressed() {
    }

    private void CreateNamesList() {
        String[] contacts_a = {"aaa","abb","acc","add"};
        String[] contacts_b = {"b1b","b2b","bab","bcb","bbc"};
        String[] contacts_c = {"caa","cbb","ccc","cdd"};
        String[] contacts_d = {"drtry","dqwqrt","dhhqe","dwebb","dopicc","dpxbmfdd"};
        String[] contacts_e = {"eaaa","eabb","eacghc","eadd"};
        String[] contacts_f = {"faaa","fabb","facc","fadd"};
        String[] contacts_g = {"gaaa","gabb","gacc","gadd"};
        String[] contacts_h = {"haaa","habb","hacc","hadd"};
        String[] contacts_i = {"iaaai","ibbb","iacc","iadd"};
        String[] contacts_j = {"jaaa","jabb","jacc","jadd"};
        String[] contacts_k = {"kaaa","kabb","kacc","kadd"};
        String[] contacts_l = {"laaa","labb","lacc","ladd"};
        String[] contacts_m = {"maaa","mabb","macc","madd"};
        String[] contacts_n = {"naaa","nabb","nacc","nadd"};
        String[] contacts_o = {"ooooaaa","oooabb","oacc","ooadd"};
        String[] contacts_p = {"paaa","pppabb","pppacc","ppadd"};
        String[] contacts_q = {"qqaaa","qqqqqqabb","qqacc","qqadd"};
        String[] contacts_r = {"raaa","rrrabb","rrracc","rraasfadd"};
        String[] contacts_s = {"ssssscvbcfaaa","sssggabb","sgvacc","sfadd"};
        String[] contacts_t = {"ttaaa","tabb","ttacc","tscvssadd"};
        String[] contacts_u = {"uuaaa","uabb","uuudvacc","uxcbdadd"};
        String[] contacts_v = {"vaaa","vdsdvabb","vdsssacc","vSDadd"};
        String[] contacts_w = {"waaa","wabb","wwacc","wadd"};
        String[] contacts_x = {"xaaa","xabb","xacc","xadd"};
        String[] contacts_y = {"yaaa","yzdsvcabb","yxsacc","yewadd"};
        String[] contacts_z = {"zmaaa","zdabb","zlasacc","ziadd"};




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
            String characterThird = String.valueOf(names.charAt(2)).toLowerCase();
            String characterTwo = String.valueOf(names.charAt(1)).toLowerCase();
            String characterFirst = String.valueOf(names.charAt(0)).toLowerCase();
//            System.out.println(characterFirst+"   "+characterTwo+ "  "+characterThird);
            if(secondCharacter>=1 && (lastCharacterList.equals("a")
                    || lastCharacterList.equals("b")
                    || lastCharacterList.equals("c")
                    || lastCharacterList.equals("d")
                    || lastCharacterList.equals("e")
                    || lastCharacterList.equals("f")
                    || lastCharacterList.equals("g")
                    || lastCharacterList.equals("h")
                    || lastCharacterList.equals("i")
                    || lastCharacterList.equals("j")
                    || lastCharacterList.equals("k")
                    || lastCharacterList.equals("l")
                    || lastCharacterList.equals("m")
                    || lastCharacterList.equals("n")
                    || lastCharacterList.equals("o")
                    || lastCharacterList.equals("p")
                    || lastCharacterList.equals("q")
                    || lastCharacterList.equals("r")
                    || lastCharacterList.equals("s")
                    || lastCharacterList.equals("t")
                    || lastCharacterList.equals("u")
                    || lastCharacterList.equals("v")
                    || lastCharacterList.equals("w")
                    || lastCharacterList.equals("x")
                    || lastCharacterList.equals("y")
                    || lastCharacterList.equals("z")

            ))
            {
                if(secondCharacter==1)
                {
//                    if(characterTwo.equals("a") && currentPredict.equals("a"))
                    if(characterTwo.equals(currentPredict))
                    {
                        System.out.println(names);
                        System.out.println("&");
//                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
                        Names.add(names);
                    }
//                    else if(characterTwo.equals("b") && currentPredict.equals("b"))
//                    {
//                        System.out.println("&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterTwo.equals("c") && currentPredict.equals("c"))
//                    {
//                        System.out.println("&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterTwo.equals("d") && currentPredict.equals("d"))
//                    {
//                        System.out.println("&&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
                }
                else if(secondCharacter==2 && characterTwo.equals(previousCharacter))
                {
                    System.out.println(names);
                    if(characterThird.equals(currentPredict))
                    {
                        System.out.println("&");
//                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
                        Names.add(names);
                    }
//                    if(characterThird.equals("a") && currentPredict.equals("a") )
//                    {
//                        System.out.println("&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterThird.equals("b") && currentPredict.equals("b"))
//                    {
//                        System.out.println("&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterThird.equals("c") && currentPredict.equals("c"))
//                    {
//                        System.out.println("&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
//                    else if(characterThird.equals("d") && currentPredict.equals("d"))
//                    {
//                        System.out.println("&&&&");
////                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
                }
            }

            else
            {
                Names.add(names);
            }
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
//                System.out.println(i);
                expandableListView.collapseGroup(i);
            }

            if(secondCharacter>=1 && secondCharacter<3)
            {
//                System.out.println("");
//                System.out.println(ObjPrediction);
                predict = ObjPrediction.get(0).name;
                predict = predict.toString().toLowerCase();
//                System.out.println("predict  in second"+predict);
                drawCharacterList.add(predict);
                System.out.println(drawCharacterList);
                if(secondCharacter==2)
                {
                   previousCharacter=drawCharacterList.get(1);
//                    System.out.println("lastlist "+lastCharacterList+lastCharacterPosition+previousCharacter+predict);
                }

                lastCharacterList = drawCharacterList.get(0);
                lastCharacterPosition = drawCharacterPosition.get(0);
                currentPredict=predict;

                CreateCharacterList();
                CreateNamesList();
                secondCharacter +=1;
                expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
                expandableListView.setAdapter(expandableListAdapter);

                if(predict.equals("a"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("b"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("c"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("e"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("f"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("g"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("h"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("i"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("j"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("k"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("l"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("m"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("n"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("o"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("p"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("q"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("r"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("s"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("t"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("u"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("v"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("w"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("x"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("y"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }
                else if(predict.equals("z"))
                {
                    expandableListView.expandGroup(lastCharacterPosition);
                }

            }
            else if(secondCharacter>=3)
            {
                secondCharacter=0;
                drawCharacterPosition.clear();
                drawCharacterList.clear();
                lastCharacterList=null;
                predict = ObjPrediction.get(0).name;
                predict = predict.toString().toLowerCase();
                drawCharacterList.add(predict);
//                secondCharacter+=1;
                currentPredict=predict;
                CreateCharacterList();
                CreateNamesList();
                secondCharacter +=1;
                expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
                expandableListView.setAdapter(expandableListAdapter);

                if(predict.equals("a"))
                {
                    drawCharacterPosition.add(0);
                    expandableListView.expandGroup(0);
                }
                else if(predict.equals("b"))
                {
                    drawCharacterPosition.add(1);
                    expandableListView.expandGroup(1);
                }
                else if(predict.equals("c"))
                {
                    drawCharacterPosition.add(2);
                    expandableListView.expandGroup(2);
                }
                else if(predict.equals("d"))
                {
                    drawCharacterPosition.add(3);
                    expandableListView.expandGroup(3);
                }
                else if(predict.equals("e"))
                {
                    drawCharacterPosition.add(4);
                    expandableListView.expandGroup(4);
                }
                else if(predict.equals("f"))
                {
                    drawCharacterPosition.add(5);
                    expandableListView.expandGroup(5);
                }
                else if(predict.equals("g"))
                {
                    drawCharacterPosition.add(6);
                    expandableListView.expandGroup(6);
                }
                else if(predict.equals("h"))
                {
                    drawCharacterPosition.add(7);
                    expandableListView.expandGroup(7);
                }
                else if(predict.equals("i"))
                {
                    drawCharacterPosition.add(8);
                    expandableListView.expandGroup(8);
                }
                else if(predict.equals("j"))
                {
                    drawCharacterPosition.add(9);
                    expandableListView.expandGroup(9);
                }
                else if(predict.equals("k"))
                {
                    drawCharacterPosition.add(10);
                    expandableListView.expandGroup(10);
                }
                else if(predict.equals("l"))
                {
                    drawCharacterPosition.add(11);
                    expandableListView.expandGroup(11);
                }
                else if(predict.equals("m"))
                {
                    drawCharacterPosition.add(12);
                    expandableListView.expandGroup(12);
                }
                else if(predict.equals("n"))
                {
                    drawCharacterPosition.add(13);
                    expandableListView.expandGroup(13);
                }
                else if(predict.equals("o"))
                {
                    drawCharacterPosition.add(14);
                    expandableListView.expandGroup(14);
                }
                else if(predict.equals("p"))
                {
                    drawCharacterPosition.add(15);
                    expandableListView.expandGroup(15);
                }
                else if(predict.equals("q"))
                {
                    drawCharacterPosition.add(16);
                    expandableListView.expandGroup(16);
                }
                else if(predict.equals("r"))
                {
                    drawCharacterPosition.add(17);
                    expandableListView.expandGroup(17);
                }
                else if(predict.equals("s"))
                {
                    drawCharacterPosition.add(18);
                    expandableListView.expandGroup(18);
                }
                else if(predict.equals("t"))
                {
                    drawCharacterPosition.add(19);
                    expandableListView.expandGroup(19);
                }
                else if(predict.equals("u"))
                {
                    drawCharacterPosition.add(20);
                    expandableListView.expandGroup(20);
                }
                else if(predict.equals("v"))
                {
                    drawCharacterPosition.add(21);
                    expandableListView.expandGroup(21);
                }
                else if(predict.equals("w"))
                {
                    drawCharacterPosition.add(22);
                    expandableListView.expandGroup(22);
                }
                else if(predict.equals("x"))
                {
                    drawCharacterPosition.add(23);
                    expandableListView.expandGroup(23);
                }
                else if(predict.equals("y"))
                {
                    drawCharacterPosition.add(24);
                    expandableListView.expandGroup(24);
                }
                else if(predict.equals("z"))
                {
                    drawCharacterPosition.add(25);
                    expandableListView.expandGroup(25);
                }


            }
            else
            {

//                System.out.println(ObjPrediction);
                predict = ObjPrediction.get(0).name;
                predict = predict.toString().toLowerCase();
                drawCharacterList.add(predict);
                secondCharacter+=1;
                currentPredict=predict;
                if(predict.equals("a"))
                {
                    drawCharacterPosition.add(0);
                    expandableListView.expandGroup(0);
                }
                else if(predict.equals("b"))
                {
                    drawCharacterPosition.add(1);
                    expandableListView.expandGroup(1);
                }
                else if(predict.equals("c"))
                {
                    drawCharacterPosition.add(2);
                    expandableListView.expandGroup(2);
                }
                else if(predict.equals("d"))
                {
                    drawCharacterPosition.add(3);
                    expandableListView.expandGroup(3);
                }
                else if(predict.equals("e"))
                {
                    drawCharacterPosition.add(4);
                    expandableListView.expandGroup(4);
                }
                else if(predict.equals("f"))
                {
                    drawCharacterPosition.add(5);
                    expandableListView.expandGroup(5);
                }
                else if(predict.equals("g"))
                {
                    drawCharacterPosition.add(6);
                    expandableListView.expandGroup(6);
                }
                else if(predict.equals("h"))
                {
                    drawCharacterPosition.add(7);
                    expandableListView.expandGroup(7);
                }
                else if(predict.equals("i"))
                {
                    drawCharacterPosition.add(8);
                    expandableListView.expandGroup(8);
                }
                else if(predict.equals("j"))
                {
                    drawCharacterPosition.add(9);
                    expandableListView.expandGroup(9);
                }
                else if(predict.equals("k"))
                {
                    drawCharacterPosition.add(10);
                    expandableListView.expandGroup(10);
                }
                else if(predict.equals("l"))
                {
                    drawCharacterPosition.add(11);
                    expandableListView.expandGroup(11);
                }
                else if(predict.equals("m"))
                {
                    drawCharacterPosition.add(12);
                    expandableListView.expandGroup(12);
                }
                else if(predict.equals("n"))
                {
                    drawCharacterPosition.add(13);
                    expandableListView.expandGroup(13);
                }
                else if(predict.equals("o"))
                {
                    drawCharacterPosition.add(14);
                    expandableListView.expandGroup(14);
                }
                else if(predict.equals("p"))
                {
                    drawCharacterPosition.add(15);
                    expandableListView.expandGroup(15);
                }
                else if(predict.equals("q"))
                {
                    drawCharacterPosition.add(16);
                    expandableListView.expandGroup(16);
                }
                else if(predict.equals("r"))
                {
                    drawCharacterPosition.add(17);
                    expandableListView.expandGroup(17);
                }
                else if(predict.equals("s"))
                {
                    drawCharacterPosition.add(18);
                    expandableListView.expandGroup(18);
                }
                else if(predict.equals("t"))
                {
                    drawCharacterPosition.add(19);
                    expandableListView.expandGroup(19);
                }
                else if(predict.equals("u"))
                {
                    drawCharacterPosition.add(20);
                    expandableListView.expandGroup(20);
                }
                else if(predict.equals("v"))
                {
                    drawCharacterPosition.add(21);
                    expandableListView.expandGroup(21);
                }
                else if(predict.equals("w"))
                {
                    drawCharacterPosition.add(22);
                    expandableListView.expandGroup(22);
                }
                else if(predict.equals("x"))
                {
                    drawCharacterPosition.add(23);
                    expandableListView.expandGroup(23);
                }
                else if(predict.equals("y"))
                {
                    drawCharacterPosition.add(24);
                    expandableListView.expandGroup(24);
                }
                else if(predict.equals("z"))
                {
                    drawCharacterPosition.add(25);
                    expandableListView.expandGroup(25);
                }
                Toast.makeText(this,predict,Toast.LENGTH_SHORT).show();
            }

        }
    }
}