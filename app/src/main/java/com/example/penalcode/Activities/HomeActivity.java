package com.example.penalcode.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TrafficActivity;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference CrimeRef;
    private RecyclerView mRecycler_menu;
    private FloatingActionButton goToBookmarks;
    RecyclerView.LayoutManager layoutManager;
    private String type = "";
    private  boolean hasSignedIn = true;
    private ProgressBar mProgress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        CrimeRef = FirebaseDatabase.getInstance().getReference().child("Crimes");
        CrimeRef.keepSynced(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView usernametext = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImage = headerView.findViewById(R.id.profile_image);

        mRecycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);

        goToBookmarks = (FloatingActionButton)findViewById(R.id.go_to_favorites);
        goToBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,BookmarkActivity.class);
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

        if(!type.equals("Admins"))
        {
            usernametext.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImage);
        }

        mProgress_bar = findViewById(R.id.home_progress_bar);
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
                            Intent i = new Intent(HomeActivity.this, MaintainCrimesActivity.class);
                            i.putExtra("cid",crimes.getCid());
                            startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(HomeActivity.this, CrimeDetailsActivity.class);
                            i.putExtra("cid",crimes.getCid());
                            startActivity(i);
                        }
                    }
                });


            }

            @Override
            public void onDataChanged(){
                if(mProgress_bar!=null){
                    mProgress_bar.setVisibility(View.INVISIBLE);
                }
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }
        else if(id == R.id.action_sendFeedBack)
        {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","developersam6@gmail.com", null));

            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello...");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
       }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_bookmarks)
        {
            Intent i = new Intent(HomeActivity.this,BookmarkActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_search)
        {
            Intent i = new Intent(HomeActivity.this, SearchCrimesActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_traffic)
        {
            Intent i = new Intent(HomeActivity.this, TrafficActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_settings)
        {
            Intent i = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent i = new Intent(HomeActivity.this,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
