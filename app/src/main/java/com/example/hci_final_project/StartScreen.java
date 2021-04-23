package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class StartScreen extends AppCompatActivity {

    Button button3;
    ArrayList<String> taskList = new ArrayList<String>();
    ArrayList<String> taskDoneList = new ArrayList<String>();
    ArrayList<Long> taskTime = new ArrayList<Long>();
    ArrayList<Integer> taskNumbers = new ArrayList<Integer>();
    long startButtonTime;
    int counter=0;
    int currentIndex=0;
    int showIndex=1;
    Intent intent1;
    int taskNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);

        button3 = findViewById(R.id.button3);

        intent1 = getIntent();
        Bundle bundle = intent1.getExtras();
//        System.out.println("bundle"+bundle);

        if( bundle == null)
        {
//            System.out.println("intent************");
            if (counter == 0)
            {
//                for(int j = 0; j<10; j++)
//                {

//                }
                String k = "Find Avon from A ";//1
                taskList.add(k);
                k = "Find Bill from B ";//2
                taskList.add(k);
                k = "Find Christian from C ";//3
                taskList.add(k);
                k = "Find Deb from D ";//4
                taskList.add(k);
                k = "Find Eric from E ";//5
                taskList.add(k);
                k = "Find Jessie from J ";//6
                taskList.add(k);
                k = "Find Lucy from L ";//7
                taskList.add(k);
                k = "Find Murphy from M ";//8
                taskList.add(k);
                k = "Find Roma from R "; //9
                taskList.add(k);
                k = "Find Sydney from S "; //10
                taskList.add(k);

                k = "Find Aaron from A ";//11
                taskList.add(k);
                k = "Find Earl from E ";//12
                taskList.add(k);
                k = "Find Gigi from G ";//13
                taskList.add(k);
                k = "Find Lauren from L ";//14
                taskList.add(k);
                k = "Find Odin from O ";//15
                taskList.add(k);
                k = "Find Queen from Q ";//16
                taskList.add(k);
                k = "Find Thompson from T ";//17
                taskList.add(k);
                k = "Find Viva from V ";//18
                taskList.add(k);
                k = "Find Yasmine from Y ";//19
                taskList.add(k);
                k = "Find Zina from Z ";//20
                taskList.add(k);


                for(int i = 0; i < 20; i++)
                {
                    taskNumbers.add(i);
                }
                Collections.shuffle(taskNumbers);
//                System.out.println(taskNumbers);
//                System.out.println("cindex "+currentIndex+" counter "+counter+" showindex "+showIndex);
            }

        }
        else
        {
//            System.out.println("************");
            taskList= intent1.getExtras().getStringArrayList("taskList");
            counter = intent1.getExtras().getInt("counter");
            currentIndex = intent1.getExtras().getInt("currentIndex");
            showIndex = intent1.getExtras().getInt("showIndex");
            taskNumbers = intent1.getExtras().getIntegerArrayList("taskNumbers");
            taskNumber = intent1.getExtras().getInt("taskNumber");
            taskDoneList= intent1.getExtras().getStringArrayList("taskDoneList");
            taskTime= (ArrayList<Long>) intent1.getSerializableExtra("taskTime");
//            System.out.println("cindex "+currentIndex+" counter "+counter+" showindex "+showIndex);
//            System.out.println("task list"+taskList+"size"+taskList.size());
//            System.out.println("task Time"+taskTime+"size"+taskTime.size());
//            System.out.println("task Numbers"+taskNumbers+"size"+taskNumbers.size());

        }

        TextView text3 = findViewById(R.id.text3);
        TextView text4 = findViewById(R.id.text4);
//        showIndex = currentIndex+1;
        String setText4 ="Task "+String.valueOf(showIndex);
        text3.setText(setText4);
        taskNumber = taskNumbers.get(currentIndex);
        text4.setText(taskList.get(taskNumber));
//        System.out.println(text3);
//        System.out.println(text4);
//        System.out.println("task list"+taskList);
//        System.out.println("task Numbers"+taskNumbers);
//        System.out.println("task Number"+taskNumber);
//        int taskNoI = currentIndex+1;
//        String taskNo = ;
        String screenTitle = "Task "+String.valueOf(showIndex)+"/20";
        this.setTitle(screenTitle);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonTime = System.currentTimeMillis();
                Intent intent=new Intent(StartScreen.this,MainActivity.class);
                intent.putExtra("startButtonTime",startButtonTime);
                intent.putExtra("taskList",taskList);
                intent.putExtra("taskDoneList",taskDoneList);
                intent.putExtra("counter",counter);
                intent.putExtra("showIndex",showIndex);
                intent.putExtra("currentIndex",currentIndex);
                intent.putExtra("taskTime",taskTime);
                intent.putExtra("taskNumbers",taskNumbers);
                intent.putExtra("taskNumber",taskNumber);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
    }

}

