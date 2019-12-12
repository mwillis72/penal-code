package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.TrafficActivity;
import com.example.penalcode.Activities.BookmarkActivity;
import com.example.penalcode.Activities.CrimeDetailsActivity;
import com.example.penalcode.Activities.MaintainCrimesActivity;
import com.example.penalcode.Activities.SearchCrimesActivity;
import com.example.penalcode.Model.Crimes;
import com.example.penalcode.Prevalent.Prevalent;
import com.example.penalcode.R;
import com.example.penalcode.ViewHolder.CrimeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class TrafficActivity extends AppCompatActivity {

    private DatabaseReference CrimeRef;
    private RecyclerView mRecycler_menu;
    private FloatingActionButton goToBookmarks;
    RecyclerView.LayoutManager layoutManager;
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        if(FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseApp.initializeApp(this);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        }
        CrimeRef = FirebaseDatabase.getInstance().getReference().child("Traffic Crimes");
        CrimeRef.keepSynced(true);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Traffic");

        setSupportActionBar(toolbar);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mRecycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);

        goToBookmarks = (FloatingActionButton)findViewById(R.id.go_to_favorites);
        goToBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrafficActivity.this, BookmarkActivity.class);
                startActivity(i);
            }
        });

        mRecycler_menu.setHasFixedSize(true);
        //
        layoutManager = new LinearLayoutManager(this);
        mRecycler_menu.setLayoutManager(layoutManager);

        Paper.init(this);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle != null)
        {
            type = getIntent().getExtras().get("Admins").toString();
        }

        /*if(!type.equals("Admins"))
        {
            usernametext.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImage);
        }*/

    }
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Crimes> options = new FirebaseRecyclerOptions.Builder<Crimes>()
                .setQuery(CrimeRef,Crimes.class).build();

        FirebaseRecyclerAdapter<Crimes, CrimeViewHolder> adapter = new FirebaseRecyclerAdapter<Crimes, CrimeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CrimeViewHolder crimeViewHolder, int i, @NonNull final Crimes crimes) {
                crimeViewHolder.txtCrimeNumber.setText(crimes.getNumber());
                crimeViewHolder.txtCrimeName.setText(crimes.getName());
                crimeViewHolder.txtCrimeDesc.setText(crimes.getDescription());


                crimeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("Admins"))
                        {
                            Intent i = new Intent(TrafficActivity.this, MaintainCrimesActivity.class);
                            i.putExtra("cid",crimes.getCid());
                            startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(TrafficActivity.this, CrimeDetailsActivity.class);
                            i.putExtra("cid",crimes.getCid());
                            startActivity(i);
                        }
                    }
                });


            }

            @NonNull
            @Override
            public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crimes_layout,parent,false);
                CrimeViewHolder holder = new CrimeViewHolder(view);
                return holder;
            }
        };
        mRecycler_menu.setAdapter(adapter);
        adapter.startListening();
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
   

}
