package com.example.hci_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawingCharacter extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {
    private GestureLibrary GesLib;
    String predict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_drawing_character);

        GesLib = GestureLibraries.fromRawResource(this,R.raw.gesture);
        if(!GesLib.load())
        {
//            System.out.println("Not LOADED");
            finish();
        }
        GestureOverlayView GesOver = (GestureOverlayView) findViewById(R.id.ges2);
        GesOver.addOnGesturePerformedListener(this);

    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture)
    {
        ArrayList<Prediction> ObjPrediction = GesLib.recognize(gesture);
        if(ObjPrediction.size()>0 && ObjPrediction.get(0).score>1)
            {
                predict = ObjPrediction.get(0).name;
                predict = predict.toString().toLowerCase();
                Toast.makeText(this,predict,Toast.LENGTH_SHORT).show();
            }
    }
}