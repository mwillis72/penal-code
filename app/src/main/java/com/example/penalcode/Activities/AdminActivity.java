package com.example.penalcode.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.penalcode.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private String Description,Crime_name,Crime_number,saveCurrentDate,saveCurrentTime;
    private Button mAdd_Crime;
    private EditText mCrime_Name, mCrime_desc, mCrime_Number;
    private static final int r =1;
    private String crimeRandomKey;
    private StorageReference ProductImageRef;
    private DatabaseReference crime_ref;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAdd_Crime=(Button) findViewById(R.id.add_crime);

        mCrime_Name=(EditText)findViewById(R.id.crime_name);
        mCrime_desc=(EditText)findViewById(R.id.crime_desc);
        mCrime_Number=(EditText)findViewById(R.id.crime_number);

        crime_ref = FirebaseDatabase.getInstance().getReference().child("Crimes");

        //Toast.makeText(this,categoryName,Toast.LENGTH_LONG).show();
        mAdd_Crime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateCrimeData();
            }
        });
        
        mProgressDialog = new ProgressDialog(this);
    }

    private void ValidateCrimeData() {
        Description= mCrime_desc.getText().toString();
        Crime_name= mCrime_Name.getText().toString();
        Crime_number=mCrime_Number.getText().toString();
        //mProduct_Image.setImageURI(ImageUri);

        if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Crime Description can't be empty",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Crime_name))
        {
            Toast.makeText(this,"Crime Name can't be empty",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Crime_number))
        {
            Toast.makeText(this,"Crime Number can't be empty",Toast.LENGTH_SHORT).show();
        }else {
            SaveCrimeInfoToDb();
        }

    }
    private void SaveCrimeInfoToDb(){

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());


        crimeRandomKey = saveCurrentDate + saveCurrentTime;

        HashMap<String,Object> crimeMap = new HashMap<>();
        crimeMap.put("cid",crimeRandomKey);

        crimeMap.put("description", Description);

        crimeMap.put("number",Crime_number);
        crimeMap.put("name",Crime_name);

        crime_ref.child(crimeRandomKey).updateChildren(crimeMap )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                            mProgressDialog.dismiss();
                            Toast.makeText(AdminActivity.this,"Crime is Added Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String message = task.getException().toString();
                            Toast.makeText(AdminActivity.this," Error: "+message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
