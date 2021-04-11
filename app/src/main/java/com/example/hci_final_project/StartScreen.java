package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class StartScreen extends AppCompatActivity {

    Button button3;
    ArrayList<String> taskList = new ArrayList<String>();
    ArrayList<Long> taskTime = new ArrayList<Long>();
    long startButtonTime;
    int startIndex=-1;
    int currentIndex=0;
    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        button3 = findViewById(R.id.button3);

        intent1 = getIntent();
        Bundle bundle = intent1.getExtras();

        if( intent1.getExtras() == null)
        {
            System.out.println("intent************");
            if (startIndex == -1)
            {
                for(int j = 0; j<10; j++)
                {
                    String k = "Task "+String.valueOf(j);
                    taskList.add(k);
                }
                System.out.println("cindex "+currentIndex+" startindex "+startIndex);
            }

        }
        else
        {
            System.out.println("************");
            taskList= intent1.getExtras().getStringArrayList("taskList");
            startIndex = intent1.getExtras().getInt("startIndex");
            currentIndex = intent1.getExtras().getInt("currentIndex");
            taskTime= (ArrayList<Long>) intent1.getSerializableExtra("taskTime");
            System.out.println("cindex "+currentIndex+" startindex "+startIndex);
            System.out.println("task list"+taskList+"size"+taskList.size());
            System.out.println("task Time"+taskTime+"size"+taskTime.size());
        }

        TextView text3 = findViewById(R.id.text3);
        TextView text4 = findViewById(R.id.text4);
        currentIndex = startIndex+1;
        text3.setText(taskList.get(currentIndex));
        text4.setText(taskList.get(currentIndex));
        System.out.println(text3);
        System.out.println(text4);
        System.out.println("task list"+taskList);

        int taskNoI = currentIndex+1;
        String taskNo = String.valueOf(taskNoI);
        String screenTitle = "Task "+taskNo+"/10";
        this.setTitle(screenTitle);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonTime = System.currentTimeMillis();
                Intent intent=new Intent(StartScreen.this,MainActivity.class);
                intent.putExtra("startButtonTime",startButtonTime);
                intent.putExtra("taskList",taskList);
                intent.putExtra("startIndex",startIndex);
                intent.putExtra("currentIndex",currentIndex);
                intent.putExtra("taskTime",taskTime);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
    }

}

