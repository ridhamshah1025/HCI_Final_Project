package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RadioButtonActivity extends AppCompatActivity {
    TextView textView25;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnSubmit;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_radio_button);
        addListenerButton();
    }

    private void addListenerButton() {
        radioGroup = findViewById(R.id.radioSelectChoice);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);
                String radioText = radioButton.getText().toString();
                Toast.makeText(RadioButtonActivity.this, radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
                if("Practice for Drawing Character".equals(radioText))
                {
                    Intent intent = new Intent(RadioButtonActivity.this, DrawingCharacter.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(RadioButtonActivity.this, StartScreen.class);
                    startActivity(intent);
                }

            }
        });
    }
}