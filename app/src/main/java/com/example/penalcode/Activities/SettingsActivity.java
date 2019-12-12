package com.example.penalcode.Activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.penalcode.Prevalent.Prevalent;
import com.example.penalcode.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settings_profile_image;
    private EditText settings_phone, settings_fullname;
    private TextView changePicText, closeText, updateText;

    private Uri imageUri;
    private  String myUri= "";
    private StorageReference profilePicRef;
    private StorageTask uploadTask;
    private String checker = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings_profile_image=(CircleImageView)findViewById(R.id.settings_prof_pic);
        changePicText=(TextView)findViewById(R.id.change_photo_settings);
        closeText=(TextView)findViewById(R.id.settings_close);
        updateText=(TextView)findViewById(R.id.update_settings);

        settings_fullname=(EditText)findViewById(R.id.settings_inputName);
        settings_phone=(EditText)findViewById(R.id.settings_inputPhone);

        userInfoDisplay(settings_profile_image,settings_fullname,settings_phone);

        profilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Picture");

        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }
                else
                {
                    updateUserInfoOnly();
                }
            }
        });

        try {
            changePicText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checker = "clicked";
                    CropImage.activity(imageUri)
                            .setAspectRatio(1,1)
                            .start(SettingsActivity.this);

                }
            });
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageUri = result.getUri();

            settings_profile_image.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error: Try Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent( SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void updateUserInfoOnly() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users");
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("Name", settings_fullname.getText().toString());

        userMap.put("Phone", settings_phone.getText().toString());

        reference.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(settings_fullname.getText().toString())){
            Toast.makeText(this,"Name is mandatory",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(settings_phone.getText().toString())){
            Toast.makeText(this,"Phone is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please Wait While We Upload Image");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = profilePicRef
                    .child(Prevalent.currentOnlineUser.getPhone()+".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();

                        myUri = downloadUrl.toString();

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users");
                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("Name", settings_fullname.getText().toString());

                        userMap.put("Phone", settings_phone.getText().toString());
                        userMap.put("image",myUri);
                        reference.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this,"Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this,"Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(SettingsActivity.this,"Image Not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView settings_profile_image, final EditText settings_fullname, final EditText settings_phone)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image =dataSnapshot.child("image").getValue().toString();
                        String name =dataSnapshot.child("Name").getValue().toString();
                        String phone =dataSnapshot.child("Phone").getValue().toString();


                        Picasso.get().load(image).into(settings_profile_image);
                        settings_fullname.setText(name);

                        settings_phone.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
