package com.example.penalcode.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.penalcode.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DatabaseReference CrimeRef;
        CrimeRef = FirebaseDatabase.getInstance().getReference();
        CrimeRef.child("Crimes");

    }
    private void createref()
    {

    }
}
