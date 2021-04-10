package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startscreen extends AppCompatActivity {

    Button button3;
    long startButtonTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        button3 = findViewById(R.id.button3);
        startButtonTime = System.currentTimeMillis();
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(startscreen.this,MainActivity.class);
                intent.putExtra("startButtonTime",startButtonTime);
                startActivity(intent);
            }
        });

    }

}

