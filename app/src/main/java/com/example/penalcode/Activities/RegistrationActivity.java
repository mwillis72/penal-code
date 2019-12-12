package com.example.penalcode.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.penalcode.Model.Users;
import com.example.penalcode.Prevalent.Prevalent;
import com.example.penalcode.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class RegistrationActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPasssword;
    private TextView mSignIn_text;
    ProgressDialog mProgressDialog;
    private String parentNode = "users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        CreateAccountButton=(Button)findViewById(R.id.register);
        InputName=(EditText)findViewById(R.id.register_name);
        InputPasssword=(EditText)findViewById(R.id.register_password);
        InputPhoneNumber=(EditText)findViewById(R.id.register_phone_number);

        mSignIn_text = findViewById(R.id.signIn_text);
        mSignIn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        mProgressDialog = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        Paper.init(this);


    }


    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPasssword.getText().toString();
        if(TextUtils.isEmpty(name)){

            Toast.makeText(this, "Name field cannot be blank", Toast.LENGTH_LONG).show();

        }else if(TextUtils.isEmpty(phone)){


            Toast.makeText(this, "Phone Number field cannot be blank", Toast.LENGTH_LONG).show();



        }else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "Password field cannot be blank", Toast.LENGTH_LONG).show();
        }else if (phone.length() < 9 || phone.length() > 10) {
            Toast.makeText(this, "Please Enter Valid Phone number", Toast.LENGTH_LONG).show();

        }
        else
        {
            mProgressDialog.setTitle("Creating Account!");
            mProgressDialog.setMessage("Please Wait While We Are Checking Credentials...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            ValidatePhoneNumber(name, phone, password);
        }
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference rootRef,adminRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(phone).exists()) {
                    Toast.makeText(RegistrationActivity.this, "Phone Number " + phone + " Already Exists, Try Another One", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Phone", phone);
                    userdataMap.put("Name", name);
                    userdataMap.put("Password", password);

                    rootRef.child("users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegistrationActivity.this, "Account Creation Successful", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Network Error Occurred: Please Try Again Later!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
