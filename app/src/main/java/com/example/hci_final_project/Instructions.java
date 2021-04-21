package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class Instructions extends AppCompatActivity {
Button button44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_instructions);

        button44 = findViewById(R.id.button44);
        button44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Instructions.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(Instructions.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // this will request for permission when user has not granted permission for the app
                    ActivityCompat.requestPermissions(Instructions.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);

                    if (ActivityCompat.checkSelfPermission(Instructions.this, Manifest.permission.READ_EXTERNAL_STORAGE) == -1) {
                        Toast.makeText(Instructions.this, "You must grant storage access.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(Instructions.this, RadioButtonActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
}