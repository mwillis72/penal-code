package com.example.penalcode.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.penalcode.Model.Crimes;
import com.example.penalcode.Prevalent.Prevalent;
import com.example.penalcode.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CrimeDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addToBookmarks;

    private TextView crimeDesc, crimeName, crimeNum;
    private String CrimeID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_details);

        CrimeID = getIntent().getStringExtra("cid");

        addToBookmarks = (FloatingActionButton)findViewById(R.id.add_to_favorites);

        crimeDesc = (TextView)findViewById(R.id.crime_detail_desc);
        crimeName = (TextView)findViewById(R.id.crime_detail_name);
        crimeNum = (TextView)findViewById(R.id.crime_detail_number);

        addToBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(state.equals("Added To Bookmarks"))
                {
                    Toast.makeText(CrimeDetailsActivity.this,"You Can Add More To Favorites",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addingToBookmarkList();
                }
            }
        });

        getCrimeDetails(CrimeID);
    }

    @Override
    protected void onStart() {
        super.onStart();

       // CheckOrderState();
    }

    private void addingToBookmarkList()
    {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference bookmarkListRef = FirebaseDatabase.getInstance().getReference().child("Bookmark List");
        bookmarkListRef.keepSynced(true);

        final HashMap<String,Object> bookmarkMap = new HashMap<>();
        bookmarkMap.put("cid", CrimeID);
        bookmarkMap.put("name", crimeName.getText().toString());
        bookmarkMap.put("number", crimeNum.getText().toString());
        bookmarkMap.put("date", saveCurrentDate);
        bookmarkMap.put("time", saveCurrentTime);
        bookmarkMap.put("desc", crimeDesc.getText().toString());


        bookmarkListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Crimes").child(CrimeID)
                 .updateChildren(bookmarkMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            bookmarkListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Crimes").child(CrimeID)
                                    .updateChildren(bookmarkMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CrimeDetailsActivity.this,"Added To Bookmarks",Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(CrimeDetailsActivity.this,HomeActivity.class);
                                                startActivity(i);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
    private void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName= dataSnapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped")){
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCrimeDetails(final String productID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Crimes");
        reference.child(CrimeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Crimes crimes = dataSnapshot.getValue(Crimes.class);

                    crimeDesc.setText(crimes.getDescription());
                    crimeName.setText(crimes.getName());
                    crimeNum.setText(crimes.getNumber());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
