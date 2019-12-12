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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber, InputPasssword;
    private Button LoginAccountButton;
    ProgressDialog mProgressDialog;
    private String parentNode = "users";
    private TextView AdminLink;
    private TextView notAdminLink,mSignUp_text;
    private  boolean hasSignedIn = false;
    private CheckBox mRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginAccountButton = (Button) findViewById(R.id.login);
        InputPasssword = (EditText) findViewById(R.id.login_password);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number);
        mProgressDialog = new ProgressDialog(this);
        notAdminLink =findViewById(R.id.not_admin_link);

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccountButton.setText(getText((R.string.sign_in)));
                notAdminLink.setVisibility(View.INVISIBLE);
                AdminLink.setVisibility(View.VISIBLE);
                parentNode = "users";
            }
        });


        AdminLink = (TextView) findViewById(R.id.admin_link);
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccountButton.setText(getText(R.string.admin_sign_in));
                notAdminLink.setVisibility(View.VISIBLE);
                AdminLink.setVisibility(View.INVISIBLE);
                parentNode = "Admins";
            }
        });

        LoginAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccount();
            }
        });

        mSignUp_text = findViewById(R.id.signUp_text);
        mSignUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });

        mRememberMe = (CheckBox)findViewById(R.id.remember_me);
        Paper.init(this);

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey!=null && UserPasswordKey!=null){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);
                mProgressDialog.setTitle("Already Logged In");
                mProgressDialog.setMessage("Please Wait...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

            }
        }
    }

    private void LoginAccount() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPasssword.getText().toString();
        if (TextUtils.isEmpty(phone)) {

            Toast.makeText(this, "Phone Number field cannot be Empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Password field cannot be blank", Toast.LENGTH_LONG).show();
        } else {
            mProgressDialog.setTitle(" Account Login!");
            mProgressDialog.setMessage("Please Wait While We Are Checking Credentials...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            AllowAccountAccess(phone,password);
        }
    }
    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentNode).child(phone).exists()) {

                    Users usersData = dataSnapshot.child(parentNode).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            // Toast.makeText(MainActivity.this, "You Are Already Logged In", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Please Register First If You Have Not Yet!", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void AllowAccountAccess(final String phone, final String password) {

        if(mRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);

        }

        final DatabaseReference rootRef,adminRef;
        adminRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentNode).child(phone).exists()) {

                    Users usersData = dataSnapshot.child(parentNode).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            if (parentNode.equals("Admins")) {

                                if (!dataSnapshot.child("Admins").child(phone).exists()) {
                                    Toast.makeText(LoginActivity.this, "You Are Not Admin, Login As A User!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Admin Login Successful!", Toast.LENGTH_LONG).show();
                                    mProgressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                }
                            } else if (parentNode.equals("users")) {
                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
                                mProgressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Account With Phone Number " + phone + " Does Not Exist, First Create Account To Login!", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
//                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                    startActivity(intent);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
