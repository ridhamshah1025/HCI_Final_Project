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

    ArrayList<Integer> falseTaskNumbers = new ArrayList<Integer>();
    ArrayList<String> falseTaskName = new ArrayList<String>();
    int totalCounter;


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

        totalCounter = intent.getExtras().getInt("totalCounter");
        falseTaskName = intent.getExtras().getStringArrayList("falseTaskName");
        falseTaskNumbers = intent.getExtras().getIntegerArrayList("falseTaskNumbers");


//        System.out.println("task Number"+taskNumber);
//        System.out.println("task list"+taskList+"size"+taskList.size());
//        System.out.println("task Time"+taskTime+"size"+taskTime.size());
//        System.out.println("task Numbers"+taskNumbers+"size"+taskNumbers.size());


//        int taskNoI = currentIndex+1;
//        String taskNo = String.valueOf(taskNoI);

        String screenTitle = "Task "+String.valueOf(showIndex)+"/20";
        this.setTitle(screenTitle);

        currentTime = System.currentTimeMillis();
//        System.out.println("button click time"+startButtonTime);
//        System.out.println("CurrentTime"+currentTime);
//        System.out.println("diff"+(currentTime-startButtonTime));
//        Toast.makeText(MainActivity.this,"diff"+(currentTime-startButtonTime),Toast.LENGTH_SHORT).show();

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

//        layoutInflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.list_header, expandableListView, false);
//        expandableListView.addHeaderView(header);

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
//                    expandableListView.setSelectionFromTop(groupPosition,0);
//                    expandableListView.top
                    expandableListView.setSelectedGroup(groupPosition);
                    lastExpandedPosition = groupPosition;
                }
            });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    childClickTime = System.currentTimeMillis()-currentTime;
                    Toast.makeText(MainActivity.this,String.valueOf(childClickTime),Toast.LENGTH_SHORT).show();
//                    System.out.println("childClickTime"+childClickTime);
                    String selected = expandableListAdapter.getChild(groupPosition,childPosition).toString();
                    System.out.println("group position "+groupPosition+" child "+childPosition+" secondCharacter "+ secondCharacter);

                    if (showIndex < 20)
                    {

                        if  (   (taskNumber==0 && secondCharacter==0 && groupPosition ==0 && childPosition == 3)||
                                (taskNumber==1 && secondCharacter==0 && groupPosition ==0 && childPosition == 156)||
                                (taskNumber==2 && secondCharacter==0 && groupPosition ==1 && childPosition == 92)||
                                (taskNumber==3 && secondCharacter==0 && groupPosition ==2 && childPosition == 35)||
                                (taskNumber==4 && secondCharacter==0 && groupPosition ==3 && childPosition == 25)||
                                (taskNumber==5 && secondCharacter==0 && groupPosition ==4 && childPosition == 39)||
                                (taskNumber==6 && secondCharacter==0 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==0 && groupPosition ==6 && childPosition == 2)||
                                (taskNumber==8 && secondCharacter==0 && groupPosition ==7 && childPosition == 9)||
                                (taskNumber==9 && secondCharacter==0 && groupPosition ==9 && childPosition == 216)||
                                (taskNumber==10 && secondCharacter==0 && groupPosition ==11 && childPosition == 1)||
                                (taskNumber==11 && secondCharacter==0 && groupPosition ==12 && childPosition == 77)||
                                (taskNumber==12 && secondCharacter==0 && groupPosition ==14 && childPosition == 1)||
                                (taskNumber==13 && secondCharacter==0 && groupPosition ==16 && childPosition == 1)||
                                (taskNumber==14 && secondCharacter==0 && groupPosition ==17 && childPosition == 15)||
                                (taskNumber==15 && secondCharacter==0 && groupPosition ==18 && childPosition == 390)||
                                (taskNumber==16 && secondCharacter==0 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==0 && groupPosition ==19 && childPosition == 2)||
                                (taskNumber==18 && secondCharacter==0 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==0 && groupPosition ==25 && childPosition == 2)||
                                (taskNumber==0 && secondCharacter==1 && groupPosition ==0 && childPosition == 3)||
                                (taskNumber==1 && secondCharacter==1 && groupPosition ==0 && childPosition == 156)||
                                (taskNumber==2 && secondCharacter==1 && groupPosition ==1 && childPosition == 92)||
                                (taskNumber==3 && secondCharacter==1 && groupPosition ==2 && childPosition == 35)||
                                (taskNumber==4 && secondCharacter==1 && groupPosition ==3 && childPosition == 25)||
                                (taskNumber==5 && secondCharacter==1 && groupPosition ==4 && childPosition == 39)||
                                (taskNumber==6 && secondCharacter==1 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==1 && groupPosition ==6 && childPosition == 2)||
                                (taskNumber==8 && secondCharacter==1 && groupPosition ==7 && childPosition == 9)||
                                (taskNumber==9 && secondCharacter==1 && groupPosition ==9 && childPosition == 216)||
                                (taskNumber==10 && secondCharacter==1 && groupPosition ==11 && childPosition == 1)||
                                (taskNumber==11 && secondCharacter==1 && groupPosition ==12 && childPosition == 77)||
                                (taskNumber==12 && secondCharacter==1 && groupPosition ==14 && childPosition == 1)||
                                (taskNumber==13 && secondCharacter==1 && groupPosition ==16 && childPosition == 1)||
                                (taskNumber==14 && secondCharacter==1 && groupPosition ==17 && childPosition == 15)||
                                (taskNumber==15 && secondCharacter==1 && groupPosition ==18 && childPosition == 390)||
                                (taskNumber==16 && secondCharacter==1 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==1 && groupPosition ==19 && childPosition == 2)||
                                (taskNumber==18 && secondCharacter==1 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==1 && groupPosition ==25 && childPosition == 2)||
                                (taskNumber==0 && secondCharacter==2 && groupPosition ==0 && childPosition == 3)||
                                (taskNumber==1 && secondCharacter==2 && groupPosition ==0 && childPosition == 18)||
                                (taskNumber==2 && secondCharacter==2 && groupPosition ==1 && childPosition == 0)||
                                (taskNumber==3 && secondCharacter==2 && groupPosition ==2 && childPosition == 15)||
                                (taskNumber==4 && secondCharacter==2 && groupPosition ==3 && childPosition == 12)||
                                (taskNumber==5 && secondCharacter==2 && groupPosition ==4 && childPosition == 27)||
                                (taskNumber==6 && secondCharacter==2 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==2 && groupPosition ==6 && childPosition == 0)||
                                (taskNumber==8 && secondCharacter==2 && groupPosition ==7 && childPosition == 0)||
                                (taskNumber==9 && secondCharacter==2 && groupPosition ==9 && childPosition == 36)||
                                (taskNumber==10 && secondCharacter==2 && groupPosition ==11 && childPosition == 1)||
                                (taskNumber==11 && secondCharacter==2 && groupPosition ==12 && childPosition == 1)||
                                (taskNumber==12 && secondCharacter==2 && groupPosition ==14 && childPosition == 0)||
                                (taskNumber==13 && secondCharacter==2 && groupPosition ==16 && childPosition == 0)||
                                (taskNumber==14 && secondCharacter==2 && groupPosition ==17 && childPosition == 3)||
                                (taskNumber==15 && secondCharacter==2 && groupPosition ==18 && childPosition == 2)||
                                (taskNumber==16 && secondCharacter==2 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==2 && groupPosition ==19 && childPosition == 0)||
                                (taskNumber==18 && secondCharacter==2 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==2 && groupPosition ==25 && childPosition == 0)||
                                (taskNumber==0 && secondCharacter==3 && groupPosition ==0 && childPosition == 1)||
                                (taskNumber==1 && secondCharacter==3 && groupPosition ==0 && childPosition == 0)||
                                (taskNumber==2 && secondCharacter==3 && groupPosition ==1 && childPosition == 0)||
                                (taskNumber==3 && secondCharacter==3 && groupPosition ==2 && childPosition == 1)||
                                (taskNumber==4 && secondCharacter==3 && groupPosition ==3 && childPosition == 0)||
                                (taskNumber==5 && secondCharacter==3 && groupPosition ==4 && childPosition == 2)||
                                (taskNumber==6 && secondCharacter==3 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==3 && groupPosition ==6 && childPosition == 0)||
                                (taskNumber==8 && secondCharacter==3 && groupPosition ==7 && childPosition == 0)||
                                (taskNumber==9 && secondCharacter==3 && groupPosition ==9 && childPosition == 6)||
                                (taskNumber==10 && secondCharacter==3 && groupPosition ==11 && childPosition == 0)||
                                (taskNumber==11 && secondCharacter==3 && groupPosition ==12 && childPosition == 0)||
                                (taskNumber==12 && secondCharacter==3 && groupPosition ==14 && childPosition == 0)||
                                (taskNumber==13 && secondCharacter==3 && groupPosition ==16 && childPosition == 0)||
                                (taskNumber==14 && secondCharacter==3 && groupPosition ==17 && childPosition == 0)||
                                (taskNumber==15 && secondCharacter==3 && groupPosition ==18 && childPosition == 0)||
                                (taskNumber==16 && secondCharacter==3 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==3 && groupPosition ==19 && childPosition == 0)||
                                (taskNumber==18 && secondCharacter==3 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==3 && groupPosition ==25 && childPosition == 0)

                        )
                        {
//                            System.out.println("chaddi1");
                            updateData(groupPosition,childPosition,childClickTime,taskList,taskTime,
                                    counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList,totalCounter, falseTaskName,falseTaskNumbers);
                        }
                        else
                            {
                                failUpdateData(groupPosition,childPosition,childClickTime,taskList,taskTime,
                                        counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList,totalCounter,falseTaskName,falseTaskNumbers);
//                                Toast.makeText(MainActivity.this,"Please Click on Correct Name",Toast.LENGTH_SHORT).show();
                            }


                    }

                    else if(showIndex==20)
                    {
                        if  (   (taskNumber==0 && secondCharacter==0 && groupPosition ==0 && childPosition == 3)||
                                (taskNumber==1 && secondCharacter==0 && groupPosition ==0 && childPosition == 156)||
                                (taskNumber==2 && secondCharacter==0 && groupPosition ==1 && childPosition == 92)||
                                (taskNumber==3 && secondCharacter==0 && groupPosition ==2 && childPosition == 35)||
                                (taskNumber==4 && secondCharacter==0 && groupPosition ==3 && childPosition == 25)||
                                (taskNumber==5 && secondCharacter==0 && groupPosition ==4 && childPosition == 39)||
                                (taskNumber==6 && secondCharacter==0 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==0 && groupPosition ==6 && childPosition == 2)||
                                (taskNumber==8 && secondCharacter==0 && groupPosition ==7 && childPosition == 9)||
                                (taskNumber==9 && secondCharacter==0 && groupPosition ==9 && childPosition == 216)||
                                (taskNumber==10 && secondCharacter==0 && groupPosition ==11 && childPosition == 1)||
                                (taskNumber==11 && secondCharacter==0 && groupPosition ==12 && childPosition == 77)||
                                (taskNumber==12 && secondCharacter==0 && groupPosition ==14 && childPosition == 1)||
                                (taskNumber==13 && secondCharacter==0 && groupPosition ==16 && childPosition == 1)||
                                (taskNumber==14 && secondCharacter==0 && groupPosition ==17 && childPosition == 15)||
                                (taskNumber==15 && secondCharacter==0 && groupPosition ==18 && childPosition == 390)||
                                (taskNumber==16 && secondCharacter==0 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==0 && groupPosition ==19 && childPosition == 2)||
                                (taskNumber==18 && secondCharacter==0 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==0 && groupPosition ==25 && childPosition == 2)||
                                (taskNumber==0 && secondCharacter==1 && groupPosition ==0 && childPosition == 3)||
                                (taskNumber==1 && secondCharacter==1 && groupPosition ==0 && childPosition == 156)||
                                (taskNumber==2 && secondCharacter==1 && groupPosition ==1 && childPosition == 92)||
                                (taskNumber==3 && secondCharacter==1 && groupPosition ==2 && childPosition == 35)||
                                (taskNumber==4 && secondCharacter==1 && groupPosition ==3 && childPosition == 25)||
                                (taskNumber==5 && secondCharacter==1 && groupPosition ==4 && childPosition == 39)||
                                (taskNumber==6 && secondCharacter==1 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==1 && groupPosition ==6 && childPosition == 2)||
                                (taskNumber==8 && secondCharacter==1 && groupPosition ==7 && childPosition == 9)||
                                (taskNumber==9 && secondCharacter==1 && groupPosition ==9 && childPosition == 216)||
                                (taskNumber==10 && secondCharacter==1 && groupPosition ==11 && childPosition == 1)||
                                (taskNumber==11 && secondCharacter==1 && groupPosition ==12 && childPosition == 77)||
                                (taskNumber==12 && secondCharacter==1 && groupPosition ==14 && childPosition == 1)||
                                (taskNumber==13 && secondCharacter==1 && groupPosition ==16 && childPosition == 1)||
                                (taskNumber==14 && secondCharacter==1 && groupPosition ==17 && childPosition == 15)||
                                (taskNumber==15 && secondCharacter==1 && groupPosition ==18 && childPosition == 390)||
                                (taskNumber==16 && secondCharacter==1 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==1 && groupPosition ==19 && childPosition == 2)||
                                (taskNumber==18 && secondCharacter==1 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==1 && groupPosition ==25 && childPosition == 2)||
                                (taskNumber==0 && secondCharacter==2 && groupPosition ==0 && childPosition == 3)||
                                (taskNumber==1 && secondCharacter==2 && groupPosition ==0 && childPosition == 18)||
                                (taskNumber==2 && secondCharacter==2 && groupPosition ==1 && childPosition == 0)||
                                (taskNumber==3 && secondCharacter==2 && groupPosition ==2 && childPosition == 15)||
                                (taskNumber==4 && secondCharacter==2 && groupPosition ==3 && childPosition == 12)||
                                (taskNumber==5 && secondCharacter==2 && groupPosition ==4 && childPosition == 27)||
                                (taskNumber==6 && secondCharacter==2 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==2 && groupPosition ==6 && childPosition == 0)||
                                (taskNumber==8 && secondCharacter==2 && groupPosition ==7 && childPosition == 0)||
                                (taskNumber==9 && secondCharacter==2 && groupPosition ==9 && childPosition == 36)||
                                (taskNumber==10 && secondCharacter==2 && groupPosition ==11 && childPosition == 1)||
                                (taskNumber==11 && secondCharacter==2 && groupPosition ==12 && childPosition == 1)||
                                (taskNumber==12 && secondCharacter==2 && groupPosition ==14 && childPosition == 0)||
                                (taskNumber==13 && secondCharacter==2 && groupPosition ==16 && childPosition == 0)||
                                (taskNumber==14 && secondCharacter==2 && groupPosition ==17 && childPosition == 3)||
                                (taskNumber==15 && secondCharacter==2 && groupPosition ==18 && childPosition == 2)||
                                (taskNumber==16 && secondCharacter==2 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==2 && groupPosition ==19 && childPosition == 0)||
                                (taskNumber==18 && secondCharacter==2 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==2 && groupPosition ==25 && childPosition == 0)||
                                (taskNumber==0 && secondCharacter==3 && groupPosition ==0 && childPosition == 1)||
                                (taskNumber==1 && secondCharacter==3 && groupPosition ==0 && childPosition == 0)||
                                (taskNumber==2 && secondCharacter==3 && groupPosition ==1 && childPosition == 0)||
                                (taskNumber==3 && secondCharacter==3 && groupPosition ==2 && childPosition == 1)||
                                (taskNumber==4 && secondCharacter==3 && groupPosition ==3 && childPosition == 0)||
                                (taskNumber==5 && secondCharacter==3 && groupPosition ==4 && childPosition == 2)||
                                (taskNumber==6 && secondCharacter==3 && groupPosition ==4 && childPosition == 0)||
                                (taskNumber==7 && secondCharacter==3 && groupPosition ==6 && childPosition == 0)||
                                (taskNumber==8 && secondCharacter==3 && groupPosition ==7 && childPosition == 0)||
                                (taskNumber==9 && secondCharacter==3 && groupPosition ==9 && childPosition == 6)||
                                (taskNumber==10 && secondCharacter==3 && groupPosition ==11 && childPosition == 0)||
                                (taskNumber==11 && secondCharacter==3 && groupPosition ==12 && childPosition == 0)||
                                (taskNumber==12 && secondCharacter==3 && groupPosition ==14 && childPosition == 0)||
                                (taskNumber==13 && secondCharacter==3 && groupPosition ==16 && childPosition == 0)||
                                (taskNumber==14 && secondCharacter==3 && groupPosition ==17 && childPosition == 0)||
                                (taskNumber==15 && secondCharacter==3 && groupPosition ==18 && childPosition == 0)||
                                (taskNumber==16 && secondCharacter==3 && groupPosition ==18 && childPosition == 3)||
                                (taskNumber==17 && secondCharacter==3 && groupPosition ==19 && childPosition == 0)||
                                (taskNumber==18 && secondCharacter==3 && groupPosition ==24 && childPosition == 0)||
                                (taskNumber==19 && secondCharacter==3 && groupPosition ==25 && childPosition == 0)

                        )
                        {
//                            System.out.println("33");
                            try {
                                taskTime.add(childClickTime);
                                taskDoneList.add(taskList.get(taskNumber));
                                falseTaskNumbers.remove(new Integer(taskNumber));
//                                System.out.println("task list"+taskList+"size"+taskList.size());
//                                System.out.println("task Time"+taskTime+"size"+taskTime.size());
//                                System.out.println("done");
                                showIndex+=1;
                                counter+=1;
                                currentIndex+=1;
                                totalCounter+=1;
//                                System.out.println("cindex " + currentIndex + " counter " + counter+ " showIndex " + showIndex);
                                data.append("Task No,Task Name,Movement Time");
//                                System.out.println("kkkkkkk");
                                for(int i = 0; i<20; i++)
                                {
                                    data.append("\n").append(String.valueOf(i+1)).append(",").append(taskDoneList.get(i)).append(",").append(taskTime.get(i));
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
                                intent2.putExtra("totalCounter",totalCounter);
                                intent2.putExtra("falseTaskName", falseTaskName);
                                intent2.putExtra("falseTaskNumbers", falseTaskNumbers);
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
                            failUpdateData(groupPosition,childPosition,childClickTime,taskList,taskTime,
                                    counter,currentIndex,showIndex,taskNumbers,taskNumber,taskDoneList,totalCounter,falseTaskName,falseTaskNumbers);
//                            Toast.makeText(MainActivity.this,"Please Click on Correct Name",Toast.LENGTH_SHORT).show();
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

    private void failUpdateData(int groupPosition, int childPosition, long childClickTime, ArrayList<String> taskList, ArrayList<Long> taskTime, int counter, int currentIndex, int showIndex, ArrayList<Integer> taskNumbers, int taskNumber, ArrayList<String> taskDoneList, int totalCounter, ArrayList<String> falseTaskName, ArrayList<Integer> falseTaskNumbers) {
//        taskTime.add(childClickTime);
        boolean ans = falseTaskNumbers.contains(taskNumber);
        if (ans){
            System.out.println("The list contains task Number");
        }
        else {
            falseTaskNumbers.add(taskNumber);
            System.out.println("The list does not contains taskNumber");
        }
//        taskDoneList.add(taskList.get(taskNumber));

//        System.out.println("task list" + taskList + "size" + taskList.size());
//        System.out.println("task Time" + taskTime + "size" + taskTime.size());
//        System.out.println("task Done List" + taskDoneList + "size" + taskDoneList.size());
//        System.out.println("done");
        counter += 1;
        currentIndex += 1;
//        showIndex += 1;
        totalCounter+=1;
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
        intent1.putExtra("totalCounter",totalCounter);
        intent1.putExtra("falseTaskName", falseTaskName);
        intent1.putExtra("falseTaskNumbers", falseTaskNumbers);
        startActivity(intent1);

    }

    private void updateFalseClickData(int groupPosition, int childPosition, long childClickTime, ArrayList<String> taskList, ArrayList<Long> taskTime, int counter, int currentIndex, int showIndex, ArrayList<Integer> taskNumbers, int taskNumber, ArrayList<String> taskDoneList) {
//        System.out.println("Wrong Click");
    }

    public void updateData(int groupPosition, int childPosition, long childClickTime, ArrayList<String> taskList, ArrayList<Long> taskTime, int counter, int currentIndex, int showIndex, ArrayList<Integer> taskNumbers, int taskNumber, ArrayList<String> taskDoneList, int totalCounter, ArrayList<String> falseTaskName, ArrayList<Integer> falseTaskNumbers)
    {

        taskTime.add(childClickTime);
        taskDoneList.add(taskList.get(taskNumber));
        falseTaskNumbers.remove(new Integer(taskNumber));
//        System.out.println("task list" + taskList + "size" + taskList.size());
//        System.out.println("task Time" + taskTime + "size" + taskTime.size());
//        System.out.println("task Done List" + taskDoneList + "size" + taskDoneList.size());
//        System.out.println("done");
        counter += 1;
        currentIndex += 1;
        showIndex += 1;
        totalCounter+=1;
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
        intent1.putExtra("totalCounter",totalCounter);
        intent1.putExtra("falseTaskName", falseTaskName);
        intent1.putExtra("falseTaskNumbers", falseTaskNumbers);
        startActivity(intent1);
    }

    @Override
    public void onBackPressed() {
    }

    private void CreateNamesList() {
        String[] contacts_a = {
                "Aaden",
                "Aaliyah",
                "Aarav",
                "Aaron",
                "Abhi",
                "Abagail",
                "Abb",
                "Abbey",
                "Abbie",
                "Abbigail",
                "Abbott",
                "Abby",
                "Abdiel",
                "Abdul",
                "Abdullah",
                "Abe",
                "Abel",
                "Abelardo",
                "Abie",
                "Abigail",
                "Abigale",
                "Abigayle",
                "Abner",
                "Abraham",
                "Abram",
                "Aedan",
                "Affie",
                "Afton",
                "Agatha",
                "Aggie",
                "Agnes",
                "Agness",
                "Agusta",
                "Alanna",
                "Alannah",
                "Alanzo",
                "Alayna",
                "Ara",
                "Arabella",
                "Araceli",
                "Aracely",
                "Arah",
                "Araminta",
                "Arba",
                "Arbie",
                "Arch",
                "Archer",
                "Archibald",
                "Archie",
                "Ardath",
                "Ardelia",
                "Ardell",
                "Ardella",
                "Ardelle",
                "Arden",
                "Ardeth",
                "Ardis",
                "Ardith",
                "Ardyce",
                "Areli",
                "Arely",
                "Aretha",
                "Argie",
                "Ari",
                "Aria",
                "Ariana",
                "Ariane",
                "Arianna",
                "Aric",
                "Arie",
                "Ariel",
                "Ariella",
                "Arielle",
                "Arietta",
                "Arizona",
                "Arjun",
                "Arkie",
                "Arla",
                "Arlan",
                "Arland",
                "Arleen",
                "Arlen",
                "Arlena",
                "Arlene",
                "Arleth",
                "Arletta",
                "Arley",
                "Arlie",
                "Arlin",
                "Arline",
                "Arlington",
                "Arlis",
                "Arlo",
                "Arly",
                "Arlyn",
                "Arlyne",
                "Arman",
                "Armand",
                "Armando",
                "Armani",
                "Armida",
                "Armin",
                "Arminda",
                "Arminta",
                "Armond",
                "Armstead",
                "Arnav",
                "Arne",
                "Arnett",
                "Arnetta",
                "Arnie",
                "Arno",
                "Arnold",
                "Arnoldo",
                "Arnulfo",
                "Aron",
                "Arra",
                "Arrie",
                "Arron",
                "Arsenio",
                "Art",
                "Arta",
                "Artelia",
                "Arther",
                "Arthor",
                "Arthur",
                "Artie",
                "Artis",
                "Arturo",
                "Arvel",
                "Arvid",
                "Arvil",
                "Arvilla",
                "Arvin",
                "Arvo",
                "Aryan",
                "Aryana",
                "Aryanna",
                "Asa",
                "Asberry",
                "Asbury",
                "Asha",
                "Ashanti",
                "Ashby",
                "Ashely",
                "Asher",
                "Ashlea",
                "Ashlee",
                "Ashleigh",
                "Ashley",
                "Ashli",
                "Ashlie",
                "Ashlyn",
                "Ashlynn",
                "Ashton",
                "Ashtyn",
                "Asia",
                "Ason",
                "Aspen",
                "Assunta",
                "Astrid",
                "Atha",
                "Athena",
                "Atlas",
                "Atticus",
                "Attie",
                "Attilio",
                "Aubra",
                "Aubree",
                "Aubrey",
                "Aubrie",
                "Audie",
                "Audley",
                "Audra",
                "Audrey",
                "Audriana",
                "Audrianna",
                "Audrina",
                "Audry",
                "Audy",
                "August",
                "Augusta",
                "Auguste",
                "Augustin",
                "Augustina",
                "Augustine",
                "Augustus",
                "Aura",
                "Aurelia",
                "Aurelio",
                "Aurilla",
                "Aurora",
                "Aurore",
                "Aurthur",
                "Austen",
                "Austin",
                "Auston",
                "Austyn",
                "Auther",
                "Author",
                "Authur",
                "Autry",
                "Autumn",
                "Ava",
                "Avah",
                "Averi",
                "Averie",
                "Avery",
                "Avie",
                "Avis",
                "Avon",
                "Axel",
                "Ayaan",
                "Ayana",
                "Ayanna",
                "Aydan",
                "Ayden",
                "Aydin",
                "Ayesha",
                "Ayla",
                "Aylin",
                "Azalee",
                "Azaria",
                "Azul",
                "Azzie"

        };
        String[] contacts_b = {
                "Bailey",
                "Baker",
                "Baldwin",
                "Ballard",
                "Bama",
                "Bambi",
                "Banks",
                "Barb",
                "Barbara",
                "Barbie",
                "Barbra",
                "Barnard",
                "Barnett",
                "Barney",
                "Barnie",
                "Baron",
                "Barrett",
                "Barrie",
                "Barron",
                "Barry",
                "Bart",
                "Bartholomew",
                "Bartley",
                "Barton",
                "Bascom",
                "Basil",
                "Baxter",
                "Bayard",
                "Baylee",
                "Baylie",
                "Bea",
                "Beadie",
                "Beatrice",
                "Beatrix",
                "Beatriz",
                "Beau",
                "Beaulah",
                "Bebe",
                "Beckett",
                "Beckham",
                "Beckie",
                "Becky",
                "Beda",
                "Bedford",
                "Bee",
                "Beecher",
                "Belen",
                "Belia",
                "Belinda",
                "Bell",
                "Bella",
                "Belle",
                "Belton",
                "Belva",
                "Ben",
                "Bena",
                "Benard",
                "Benedict",
                "Benita",
                "Benito",
                "Benjaman",
                "Benjamen",
                "Benjamin",
                "Benjamine",
                "Benji",
                "Benjiman",
                "Benjman",
                "Bennett",
                "Bennie",
                "Benny",
                "Benson",
                "Bentley",
                "Benton",
                "Berdie",
                "Berenice",
                "Berkley",
                "Berlin",
                "Bernadette",
                "Bernadine",
                "Bernard",
                "Bernardine",
                "Bernardo",
                "Berneice",
                "Bernetta",
                "Bernhard",
                "Bernice",
                "Bernie",
                "Berniece",
                "Bernita",
                "Berry",
                "Bert",
                "Berta",
                "Bill",
                "Billie",
                "Blain",
                "Blaine",
                "Blair",
                "Blaise",
                "Blake",
                "Blanca",
                "Blanch",
                "Blanchard",
                "Blanche",
                "Blanchie",
                "Blane",
                "Blas",
                "Blaze",
                "Bliss",
                "Blossom",
                "Bluford",
                "Bob",
                "Bobbi",
                "Bulah",
                "Buna",
                "Bunk",
                "Burdette",
                "Buren",
                "Burgess",
                "Burk",
                "Burke",
                "Burl",
                "Burleigh",
                "Burley",
                "Burnell",
                "Burnett",
                "Burney",
                "Burnice"
        };
        String[] contacts_c = {
                "Carey",
                "Cari",
                "Carie",
                "Carin",
                "Carole",
                "Carolee",
                "Carolina",
                "Carson",
                "Carter",
                "Cary",
                "Casimer",
                "Ceasar",
                "Cecelia",
                "Cecil",
                "Cecile",
                "Cecilia",
                "Cecily",
                "Cedric",
                "Cedrick",
                "Ceil",
                "Chace",
                "Chad",
                "Chadd",
                "Chadrick",
                "Chadwick",
                "Chaim",
                "Chaka",
                "Chalmer",
                "Charlize",
                "Charlotta",
                "Charlotte",
                "Charlottie",
                "Cherise",
                "Cheyenne",
                "Christie",
                "Christin",
                "Ciarra",
                "Cicely",
                "Cicero",
                "Clell",
                "Clella",
                "Clem",
                "Clemence",
                "Cleo",
                "Cleola",
                "Clovis",
                "Cloyd",
                "Clyda",
                "Clyde",
                "Coleton",
                "Coletta",
                "Colten",
                "Colter",
                "Colton",
                "Columbia",
                "Columbus",
                "Coretta",
                "Corey",
                "Cyntha",
                "Cynthia",
                "Cyril",
                "Cyrus"

        };
        String[] contacts_d = {
                "Daisey",
                "Daisha",
                "Daisie",
                "Daisy",
                "Daisye",
                "Damion",
                "Damon",
                "Dangelo",
                "Daniel",
                "Daniela",
                "Daniele",
                "Daniella",
                "Danielle",
                "Deacon",
                "Dean",
                "Deana",
                "Deandra",
                "Deandre",
                "Deane",
                "Deangelo",
                "Deann",
                "Deanna",
                "Deanne",
                "Deante",
                "Deasia",
                "Deb",
                "Debbi",
                "Debbie",
                "Debbra",
                "Debby",
                "Debera",
                "Debi",
                "Debora",
                "Deborah",
                "Deborrah",
                "Debra",
                "Denzell",
                "Denzil",
                "Deon",
                "Deondre",
                "Deonta",
                "Deontae",
                "Deonte",
                "Dequan",
                "Derald",
                "Dereck",
                "Derek",
                "Dereon",
                "Deric",
                "Derick",
                "Derik",
                "Desmond",
                "Dessa",
                "Dessie",
                "Destany",
                "Destin",
                "Destinee",
                "Destiney",
                "Destini",
                "Destiny",
                "Destry",
                "Devan",
                "Devante",
                "Devaughn",
                "Deven",
                "Devin",
                "Devon",
                "Devonta",
                "Devontae",
                "Devonte",
                "Devyn",
                "Deward",
                "Dewayne",
                "Dewey",
                "Dewitt",
                "Dexter",
                "Deyanira",
                "Dezzie",
                "Diallo",
                "Diamond",
                "Dian",
                "Diana",
                "Diandra",
                "Diane",
                "Diann",
                "Dianna",
                "Dianne",
                "Dicie",
                "Dick",
                "Dickie",
                "Dimitri",
                "Dimitrios",
                "Dimple",
                "Dina",
                "Dinah",
                "Dink",
                "Dino",
                "Dion",
                "Dione",
                "Doloris",
                "Dolph",
                "Dolphus",
                "Domenic",
                "Domenica",
                "Domenick",
                "Domenico",
                "Dominga",
                "Domingo",
                "Dominic",
                "Dominick",
                "Dominik",
                "Dove",
                "Dovie",
                "Dow",
                "Doyle",
                "Drusilla",
                "Duane",
                "Duard",
                "Dudley",
                "Duff",
                "Dustin",
                "Dusty",
                "Dwyane",
                "Dyan",
                "Dylan",
                "Dylon"

        };
        String[] contacts_e = {
                "Earl",
                "Earle",
                "Earlean",
                "Earlene",
                "Earnestine",
                "Eartha",
                "Edrie",
                "Edris",
                "Edythe",
                "Effa",
                "Effie",
                "Efrain",
                "Elda",
                "Elden",
                "Elder",
                "Eldon",
                "Eldora",
                "Eldred",
                "Eldridge",
                "Eleanor",
                "Eleanora",
                "Eleanore",
                "Elease",
                "Electa",
                "Elena",
                "Eleni",
                "Elenor",
                "Elenora",
                "Elenore",
                "Eleonora",
                "Eleonore",
                "Elex",
                "Elfie",
                "Elfreda",
                "Elfrieda",
                "Elgie",
                "Elgin",
                "Eli",
                "Elia",
                "Elian",
                "Emanuel",
                "Emelia",
                "Emelie",
                "Emeline",
                "Emmalee",
                "Emmaline",
                "Emmanuel",
                "Emmer",
                "Emmet",
                "Encarnacion",
                "Enid",
                "Ennis",
                "Enoch",
                "Enola",
                "Enos",
                "Eric",
                "Erica",
                "Erich",
                "Erick",
                "Erna",
                "Ernest",
                "Ernestina",
                "Ernestine",
                "Ernesto",
                "Ernie",
                "Esther",
                "Estie",
                "Estill",
                "Ether",
                "Ethie",
                "Ethyl",
                "Ethyle",
                "Etna",
                "Etta",
                "Eusebio",
                "Eustace",
                "Evelyne",
                "Ever",
                "Evie",
                "Evita",
                "Evon",
                "Evonne",
                "Ewald",
                "Ewart",
                "Ewell",
                "Ewin",
                "Ewing",
                "Exa",
                "Exie",
                "Ezekiel",
                "Ezell",
                "Ezequiel",
                "Ezra",
                "Ezzard"

        };
        String[] contacts_f = {
                "Fallon",
                "Ferris",
                "Fiona",
                "Flora",
                "Fount",
                "Fredy",
                "Furman"
        };
        String[] contacts_g = {
                "Garnet",
                "Germaine",
                "Gigi",
                "Gloria",
                "Glover",
                "Glynda",
                "Glynis",
                "Goldie",
                "Gorge",
                "Griffin",
                "Grover",
                "Gustavo",
                "Gwen"
        };
        String[] contacts_h = {
                "Hayley",
                "Haylie",
                "Hays",
                "Hayward",
                "Haywood",
                "Hazel",
                "Hazelle",
                "Hazen",
                "Hazle",
                "Heath",
                "Heather",
                "Heaven",
                "Heber",
                "Hebert",
                "Hector",
                "Hedwig",
                "Hedy",
                "Heidi",
                "Hillary",
                "Hosteen",
                "Hurley",
                "Hyman"
        };
        String[] contacts_i = {
                "Ian",
                "Ibrahim",
                "Immanuel",
                "Ina",
                "Iona",
                "Irene",
                "Issac",
                "Ivy",
                "Iyana",
                "Izora"


        };
        String[] contacts_j = {
                "Jack",
                "Jackeline",
                "Jacki",
                "Jackie",
                "Jacklyn",
                "Jackson",
                "Jacky",
                "Jaclyn",
                "Jacob",
                "Jacoby",
                "Jacque",
                "Jacquelin",
                "Jacqueline",
                "Jacquelyn",
                "Jacquelynn",
                "Jacques",
                "Jacquez",
                "Jacquline",
                "Jacqulyn",
                "Jada",
                "Jade",
                "Jaden",
                "Jadiel",
                "Jadon",
                "Jadyn",
                "Jaeda",
                "Jaeden",
                "Jaelyn",
                "Jaelynn",
                "Jagger",
                "Jaheem",
                "Jaheim",
                "Jahiem",
                "Jahir",
                "Jaida",
                "Jaiden",
                "Jaidyn",
                "Jailene",
                "Jailyn",
                "Jaime",
                "Jaimee",
                "Jaimie",
                "Jair",
                "Jairo",
                "Jajuan",
                "Jakayla",
                "Jake",
                "Jakob",
                "Jakobe",
                "Jaleel",
                "Jaleesa",
                "Jalen",
                "Jalisa",
                "Jalissa",
                "Jaliyah",
                "Jalon",
                "Jalyn",
                "Jalynn",
                "Jamaal",
                "Jamal",
                "Jamar",
                "Jamarcus",
                "Jamari",
                "Jamarion",
                "Jame",
                "Jameel",
                "Jamel",
                "James",
                "Jameson",
                "Jamey",
                "Jami",
                "Jamie",
                "Jamil",
                "Jamila",
                "Jamin",
                "Jamir",
                "Jamison",
                "Jamiya",
                "Jammie",
                "Jamya",
                "Jan",
                "Jana",
                "Janae",
                "Janay",
                "Jane",
                "Janeen",
                "Janel",
                "Janell",
                "Janelle",
                "Janene",
                "Janessa",
                "Janet",
                "Janette",
                "Janey",
                "Janiah",
                "Janice",
                "Janie",
                "Janine",
                "Janis",
                "Janiya",
                "Janiyah",
                "Jann",
                "Janna",
                "Jannette",
                "Jannie",
                "January",
                "Janyce",
                "Jaquan",
                "Jaquelin",
                "Jaqueline",
                "Jaquez",
                "Jarad",
                "Jared",
                "Jaren",
                "Jaret",
                "Jarett",
                "Jarod",
                "Jaron",
                "Jarrad",
                "Jarred",
                "Jarrell",
                "Jarret",
                "Jarrett",
                "Jarrod",
                "Jarvis",
                "Jase",
                "Jasen",
                "Jasiah",
                "Jaslene",
                "Jaslyn",
                "Jasmin",
                "Jasmine",
                "Jasmyn",
                "Jasmyne",
                "Jason",
                "Jasper",
                "Jaunita",
                "Javen",
                "Javier",
                "Javion",
                "Javon",
                "Javonte",
                "Jax",
                "Jaxon",
                "Jaxson",
                "Jay",
                "Jayce",
                "Jaycee",
                "Jaycie",
                "Jayda",
                "Jaydan",
                "Jayde",
                "Jayden",
                "Jaydin",
                "Jaydon",
                "Jaye",
                "Jayla",
                "Jaylah",
                "Jaylan",
                "Jaylee",
                "Jayleen",
                "Jaylen",
                "Jaylene",
                "Jaylin",
                "Jaylon",
                "Jaylyn",
                "Jaylynn",
                "Jayme",
                "Jaymes",
                "Jayne",
                "Jayson",
                "Jayvion",
                "Jayvon",
                "Jazlene",
                "Jazlyn",
                "Jazlynn",
                "Jazmin",
                "Jazmine",
                "Jazmyn",
                "Jazmyne",
                "Jenilee",
                "Jenna",
                "Jennette",
                "Jenni",
                "Jennie",
                "Jennifer",
                "Jenniffer",
                "Jennings",
                "Jenny",
                "Jens",
                "Jensen",
                "Jep",
                "Jeptha",
                "Jerad",
                "Jerald",
                "Jeraldine",
                "Jeramiah",
                "Jeramie",
                "Jeramy",
                "Jere",
                "Jered",
                "Jerel",
                "Jereme",
                "Jeremey",
                "Jeremiah",
                "Jeremie",
                "Jerrold",
                "Jerry",
                "Jerusha",
                "Jeryl",
                "Jesenia",
                "Jesica",
                "Jess",
                "Jesse",
                "Jessee",
                "Jessenia",
                "Jessi",
                "Jessica",
                "Jessie",
                "Jessika",
                "Jessy",
                "Joaquin",
                "Job",
                "Josefa",
                "Josefina",
                "Josefita",
                "Joselin",
                "Joseline",
                "Joseluis",
                "Jovita",
                "Joy",
                "Joyce",
                "Joycelyn",
                "Joye",
                "Juan",
                "Juana",
                "Junius",
                "Justen",
                "Justice",
                "Justin",
                "Justina",
                "Justine",
                "Juston",
                "Justus",
                "Justyn",
                "Juwan"

        };
        String[] contacts_k = {
                "Kaaren",
                "Kaylah",
                "Keyon",
                "Khalid",
                "Kinte",
                "Kole",
                "Krysta",
                "Kurt",
                "Kyson"

        };
        String[] contacts_l = {
                "Lacey",
                "Lauren",
                "Laurence",
                "Laurene",
                "Lauretta",
                "Laurette",
                "Lauri",
                "Lavern",
                "Laverna",
                "Laverne",
                "Lavonia",
                "Lavonne",
                "Lawanda",
                "Leah",
                "Leala",
                "Lenard",
                "Lenna",
                "Lennie",
                "Lennon",
                "Lenny",
                "Lenon",
                "Leonore",
                "Leontine",
                "Leopold",
                "Leopoldo",
                "Leora",
                "Letha",
                "Levina",
                "Levon",
                "Levy",
                "Lew",
                "Liane",
                "Libbie",
                "Libby",
                "Liberty",
                "Lida",
                "Liddie",
                "Lidia",
                "Lidie",
                "Lisle",
                "Lissa",
                "Lissette",
                "Lissie",
                "Liston",
                "Lita",
                "Litha",
                "Littie",
                "Loree",
                "Loreen",
                "Lossie",
                "Lota",
                "Lott",
                "Lotta",
                "Luana",
                "Lucky",
                "Lucretia",
                "Lucy",
                "Luda",
                "Ludie",
                "Ludwig",
                "Lue",
                "Luella",
                "Luetta",
                "Luisa",
                "Luka",
                "Lukas",
                "Luke",
                "Lula",
                "Lulah",
                "Lular",
                "Lulie",
                "Lupe",
                "Lura",
                "Lyda",
                "Lydell",
                "Lydia",
                "Lyla",
                "Lyric"
        };
        String[] contacts_m = {
                "Mabel",
                "Mabell",
                "Mabelle",
                "Mable",
                "Maia",
                "Maida",
                "Maira",
                "Maiya",
                "Major",
                "Makai",
                "Makaila",
                "Makala",
                "Makayla",
                "Makena",
                "Makenna",
                "Makenzie",
                "Makhi",
                "Malachi",
                "Malakai",
                "Malaki",
                "Malaya",
                "Malcolm",
                "Malcom",
                "Male",
                "Maleah",
                "Malia",
                "Malik",
                "Marcela",
                "Marcelina",
                "Marceline",
                "Mcarthur",
                "Mckayla",
                "Mckenna",
                "Mckenzie",
                "Meagan",
                "Meaghan",
                "Mearl",
                "Mechelle",
                "Meda",
                "Media",
                "Medora",
                "Meg",
                "Megan",
                "Meggan",
                "Meghan",
                "Meghann",
                "Mekhi",
                "Mel",
                "Melanie",
                "Melany",
                "Melba",
                "Melbourne",
                "Melina",
                "Melinda",
                "Melisa",
                "Melissa",
                "Melissia",
                "Mell",
                "Mellie",
                "Mellisa",
                "Mellissa",
                "Melodee",
                "Melodie",
                "Merrill",
                "Merrily",
                "Merritt",
                "Merry",
                "Mertie",
                "Mikala",
                "Minoru",
                "Mistie",
                "Misty",
                "Moesha",
                "Monte",
                "Montel",
                "Mozelle",
                "Muhammad",
                "Murphy",
                "Murray",
                "Murry",
                "Mustafa",
                "Mya",
                "Mykel",
                "Myla",
                "Mylee",
                "Myrtis",
                "Myrtle"

        };
        String[] contacts_n = {
                "Nancy",
                "Nathan",
                "Nena",
                "Nicolas",
                "Nolie",
                "Norris",
                "Nyla"
        };
        String[] contacts_o = {
                "Octave",
                "Odin",
                "Oliva",
                "Orlo",
                "Otha",
                "Ouida",
                "Ova",
                "Ozzie"
        };
        String[] contacts_p = {
                "Pablo",
                "Paula",
                "Peter",
                "Phillip",
                "Pinkey",
                "Porter",
                "Pranav",
                "Purl"

        };
        String[] contacts_q = {
                "Qiana",
                "Queen",
                "Queenie",
                "Quentin",
                "Quiana",
                "Quincy",
                "Quinn",
                "Quint",
                "Quinten",
                "Quintin",
                "Quinton"

        };
        String[] contacts_r = {
                "Rachael",
                "Reagan",
                "Rella",
                "Remington",
                "Rena",
                "Renada",
                "Renae",
                "Reynaldo",
                "Rich",
                "Richard",
                "Richelle",
                "Risa",
                "Rocio",
                "Rock",
                "Rollo",
                "Roma",
                "Romaine",
                "Roman",
                "Rome",
                "Roni",
                "Rosann",
                "Rosanna",
                "Rosanne",
                "Rosaria",
                "Rosina",
                "Roy",
                "Royal",
                "Royce",
                "Ruby",
                "Rubye",
                "Rudolf",
                "Rudolfo",
                "Rudolph",
                "Rudy",
                "Rueben",
                "Ruel",
                "Ruffin",
                "Ruffus",
                "Rufus",
                "Ruie",
                "Rupert",
                "Rush",
                "Rutha",
                "Ruthann",
                "Ruthanne",
                "Ruthe",
                "Rutherford",
                "Ruthie",
                "Ryan",
                "Ryann",
                "Ryder",
                "Ryker",
                "Rylan",
                "Ryland",
                "Rylee",
                "Ryleigh",
                "Ryley",
                "Rylie",
                "Ryne"

        };
        String[] contacts_s = {
                "Sabastian",
                "Sabina",
                "Sable",
                "Sabra",
                "Sabrina",
                "Sada",
                "Sade",
                "Sadie",
                "Sadye",
                "Sage",
                "Saige",
                "Saint",
                "Sal",
                "Salena",
                "Salina",
                "Sallie",
                "Sally",
                "Salma",
                "Salome",
                "Salomon",
                "Salvador",
                "Salvatore",
                "Sam",
                "Samantha",
                "Samara",
                "Samatha",
                "Samie",
                "Samir",
                "Samira",
                "Sammie",
                "Sammy",
                "Sampson",
                "Samson",
                "Samual",
                "Samuel",
                "Sanaa",
                "Sanai",
                "Sanders",
                "Sandi",
                "Sandie",
                "Sandra",
                "Sandy",
                "Sanford",
                "Saniya",
                "Saniyah",
                "Sanjuana",
                "Sanjuanita",
                "Sannie",
                "Santa",
                "Santana",
                "Santiago",
                "Santina",
                "Santino",
                "Santo",
                "Santos",
                "Sara",
                "Sarah",
                "Sarahi",
                "Sarai",
                "Sariah",
                "Sarina",
                "Sarita",
                "Sarrah",
                "Sasha",
                "Saul",
                "Saundra",
                "Savana",
                "Savanah",
                "Savanna",
                "Savannah",
                "Saverio",
                "Savilla",
                "Savion",
                "Savon",
                "Sawyer",
                "Scarlet",
                "Scarlett",
                "Schley",
                "Schuyler",
                "Scot",
                "Scott",
                "Scottie",
                "Scotty",
                "Seaborn",
                "Seamus",
                "Sean",
                "Sebastian",
                "Sebrina",
                "Sedrick",
                "Selah",
                "Seldon",
                "Selena",
                "Selene",
                "Selina",
                "Selma",
                "Selmer",
                "Semaj",
                "Sena",
                "Seneca",
                "Senora",
                "Serena",
                "Serenity",
                "Sergio",
                "Serina",
                "Seth",
                "Severo",
                "Severt",
                "Seward",
                "Seymour",
                "Shad",
                "Shade",
                "Shae",
                "Shafter",
                "Shaina",
                "Shakira",
                "Shalon",
                "Shalonda",
                "Shamar",
                "Shameka",
                "Shamika",
                "Shan",
                "Shana",
                "Shanae",
                "Shanda",
                "Shandra",
                "Shane",
                "Shaneka",
                "Shanell",
                "Shanelle",
                "Shanequa",
                "Shani",
                "Shania",
                "Shanice",
                "Shaniece",
                "Shanika",
                "Shaniqua",
                "Shanita",
                "Shaniya",
                "Shanna",
                "Shannan",
                "Shannen",
                "Shannon",
                "Shanon",
                "Shanta",
                "Shante",
                "Shantel",
                "Shantell",
                "Shaquan",
                "Shaquana",
                "Shaquille",
                "Shaquita",
                "Shara",
                "Shardae",
                "Sharday",
                "Sharde",
                "Sharee",
                "Sharen",
                "Shari",
                "Sharif",
                "Sharita",
                "Sharla",
                "Sharleen",
                "Sharlene",
                "Sharman",
                "Sharon",
                "Sharonda",
                "Sharron",
                "Sharyl",
                "Sharyn",
                "Shasta",
                "Shatara",
                "Shaun",
                "Shauna",
                "Shaunna",
                "Shavon",
                "Shavonne",
                "Shawanda",
                "Shawn",
                "Shawna",
                "Shawnda",
                "Shawnee",
                "Shawnna",
                "Shawnte",
                "Shay",
                "Shayla",
                "Shaylee",
                "Shayna",
                "Shayne",
                "Shea",
                "Shedrick",
                "Sheena",
                "Sheila",
                "Sheilah",
                "Shelba",
                "Shelbi",
                "Shelbie",
                "Shelby",
                "Sheldon",
                "Shelia",
                "Shelley",
                "Shelli",
                "Shellie",
                "Shelly",
                "Shelton",
                "Shelva",
                "Shelvia",
                "Shelvie",
                "Shemar",
                "Shena",
                "Shenna",
                "Shep",
                "Shepherd",
                "Sheree",
                "Sheri",
                "Sheridan",
                "Sherie",
                "Sherilyn",
                "Sherita",
                "Sherlyn",
                "Sherman",
                "Sheron",
                "Sherree",
                "Sherri",
                "Sherrie",
                "Sherrill",
                "Sherron",
                "Sherry",
                "Sherryl",
                "Sherwin",
                "Sherwood",
                "Sheryl",
                "Sheryll",
                "Sheyla",
                "Shianne",
                "Shiela",
                "Shiloh",
                "Shira",
                "Shirl",
                "Shirlee",
                "Shirleen",
                "Shirlene",
                "Shirley",
                "Shirleyann",
                "Shirlie",
                "Shoji",
                "Shon",
                "Shona",
                "Shonda",
                "Shonna",
                "Shreya",
                "Shyann",
                "Shyanne",
                "Shyheim",
                "Shyla",
                "Sibbie",
                "Sibyl",
                "Sid",
                "Siddie",
                "Sidney",
                "Sie",
                "Siena",
                "Sienna",
                "Sierra",
                "Sigmund",
                "Signa",
                "Signe",
                "Sigrid",
                "Sigurd",
                "Silas",
                "Silver",
                "Silvester",
                "Silvia",
                "Silvio",
                "Sim",
                "Simeon",
                "Simmie",
                "Simon",
                "Simona",
                "Simone",
                "Simpson",
                "Sina",
                "Sincere",
                "Sinda",
                "Sing",
                "Siobhan",
                "Sister",
                "Skip",
                "Sky",
                "Skye",
                "Skyla",
                "Skylar",
                "Skyler",
                "Slade",
                "Sloane",
                "Smith",
                "Socorro",
                "Sofia",
                "Sol",
                "Soledad",
                "Soloman",
                "Solomon",
                "Solon",
                "Somer",
                "Sommer",
                "Son",
                "Sondra",
                "Sonia",
                "Sonja",
                "Sonji",
                "Sonny",
                "Sonya",
                "Sophia",
                "Sophie",
                "Sophronia",
                "Soren",
                "Spencer",
                "Spenser",
                "Spring",
                "Spurgeon",
                "Squire",
                "Stacey",
                "Staci",
                "Stacia",
                "Stacie",
                "Stacy",
                "Stafford",
                "Stan",
                "Stanford",
                "Stanislaus",
                "Stanley",
                "Stanton",
                "Star",
                "Starla",
                "Starling",
                "Starr",
                "Stasia",
                "Stefan",
                "Stefani",
                "Stefanie",
                "Stella",
                "Stephaine",
                "Stephan",
                "Stephani",
                "Stephania",
                "Stephanie",
                "Stephany",
                "Stephen",
                "Stephenie",
                "Stephon",
                "Sterling",
                "Stetson",
                "Stevan",
                "Steve",
                "Steven",
                "Stevie",
                "Steward",
                "Stewart",
                "Stone",
                "Stonewall",
                "Stoney",
                "Storm",
                "Stormy",
                "Stuart",
                "Sudie",
                "Sue",
                "Suellen",
                "Sula",
                "Sullivan",
                "Summer",
                "Sumner",
                "Sunday",
                "Sunny",
                "Sunshine",
                "Susan",
                "Susana",
                "Susann",
                "Susanna",
                "Susannah",
                "Susanne",
                "Susie",
                "Sussie",
                "Suzan",
                "Suzann",
                "Suzanna",
                "Suzanne",
                "Suzette",
                "Suzie",
                "Suzy",
                "Sybil",
                "Syble",
                "Sydney",
                "Sydni",
                "Sydnie",
                "Syed",
                "Sylva",
                "Sylvan",
                "Sylvia",
                "Symone",
                "Syreeta"

        };
        String[] contacts_t = {
                "Tarik",
                "Terrence",
                "Thompson",
                "Titus",
                "Tory",
                "Troy",
                "Tula",
                "Twyla",
                "Tyson"
        };
        String[] contacts_u = {
                "Ula",
                "Ulises",
                "Ulysses",
                "Una"

        };
        String[] contacts_v = {
                "Valarie",
                "Vela",
                "Veva",
                "Viva",
                "Vlasta",
                "Vollie",
                "Vonda"

        };
        String[] contacts_w = {
                "Walt",
                "Westley",
                "Weston",
                "Winfred",
                "Wing",
                "Wong",
                "Wyman",
                "Wynona"

        };
        String[] contacts_x = {
                "Xander",
                "Xavier",
                "Xena",
                "Ximena",
                "Xiomara",
                "Xzavier"

        };
        String[] contacts_y = {
                "Yasmine",
                "Yessenia",
                "Yetta",
                "Yoel",
                "Young",
                "Yuliana",
                "Yvette",
                "Yvonne"

        };
        String[] contacts_z = {

                "Zayden",
                "Zeno",
                "Zina",
                "Zoa",
                "Zoie",
                "Zollie",
                "Zula"
        };




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
                if(secondCharacter==1 && characterTwo.equals(currentPredict))
                {
//                    System.out.println(names);
                    Names.add(names);
//                    if(characterTwo.equals("a") && currentPredict.equals("a"))
//                    if(characterTwo.equals(currentPredict))
//                    {
//                        System.out.println(names);
//                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
//                        Names.add(names);
//                    }
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
                else if(secondCharacter==2 && characterTwo.equals(previousCharacter) && characterThird.equals(currentPredict))
                {
//                        System.out.println(names);
//                        Toast.makeText(this,"second time a  ::"+secondCharacter,Toast.LENGTH_SHORT).show();
                        Names.add(names);

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
                Toast.makeText(MainActivity.this,predict,Toast.LENGTH_SHORT).show();
//                System.out.println("predict  in second"+predict);
                drawCharacterList.add(predict);
                System.out.println(drawCharacterList);
                if(secondCharacter==2)
                {
                   previousCharacter=drawCharacterList.get(1);
                    System.out.println("lastlist "+lastCharacterList+lastCharacterPosition+previousCharacter+predict);
                    System.out.println("list    "+drawCharacterList+"list    "+drawCharacterPosition);
                }

                System.out.println("lastlist "+lastCharacterList+lastCharacterPosition+predict);
                System.out.println("list    "+drawCharacterList+"list    "+drawCharacterPosition);

                lastCharacterList = drawCharacterList.get(0);
                lastCharacterPosition = drawCharacterPosition.get(0);
                currentPredict=predict;

                CreateCharacterList();
                CreateNamesList();

                expandableListAdapter = new MyExpandableListAdapter(this,Characters,Contacts);
                expandableListView.setAdapter(expandableListAdapter);
                secondCharacter +=1;

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
                else if(predict.equals("d"))
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
                Toast.makeText(MainActivity.this,predict,Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this,predict,Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(this,predict,Toast.LENGTH_SHORT).show();
            }

        }
    }
}