package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Finish extends AppCompatActivity {

    TextView textView33;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_finish);

        textView33 = findViewById(R.id.textView33);
        Intent intent = getIntent();
        String str = intent.getStringExtra("fileLocation");
        System.out.println(str);
        textView33.setText(str);

    }
}