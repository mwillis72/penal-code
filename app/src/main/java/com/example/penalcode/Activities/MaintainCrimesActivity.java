package com.example.penalcode.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.penalcode.R;

public class MaintainCrimesActivity extends AppCompatActivity {

    private EditText maintainNumber, maintainDesc, maintainName;
    private Button applyChanges;
    private ImageView maintainImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_crimes);

        maintainDesc = findViewById(R.id.maintain_crime_desc);

        maintainName = findViewById(R.id.maintain_crime_name);
        maintainNumber = findViewById(R.id.maintain_input_crime_num);
        applyChanges = findViewById(R.id.btn_apply_changes);
    }
}
