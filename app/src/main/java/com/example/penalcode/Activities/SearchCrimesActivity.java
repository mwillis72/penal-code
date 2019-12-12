package com.example.penalcode.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penalcode.Model.Crimes;
import com.example.penalcode.Prevalent.Prevalent;
import com.example.penalcode.R;
import com.example.penalcode.ViewHolder.CrimeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchCrimesActivity extends AppCompatActivity {

    private EditText searchCrime;
    private Button serachCrimeBtn;
    private RecyclerView searchRecyclerView;
    private  String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_crimes);
        searchCrime = findViewById(R.id.search_crime);
        serachCrimeBtn = findViewById(R.id.btn_search_crime);
        searchRecyclerView = findViewById(R.id.search_recyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        serachCrimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = searchCrime.getText().toString();

                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Crimes");

        String query = searchCrime.getText().toString().toLowerCase();
       // Query firebaseSearchQuery = reference.orderByChild(("name").start

        FirebaseRecyclerOptions<Crimes> options = new FirebaseRecyclerOptions.Builder<Crimes>()
               .setQuery(reference.orderByChild("search").startAt(query).endAt(query+ "\uf8ff"), Crimes.class)

                //.setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Crimes, CrimeViewHolder> adapter
                = new FirebaseRecyclerAdapter<Crimes, CrimeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CrimeViewHolder crimeViewHolder, int i, @NonNull final Crimes crimes) {
                crimeViewHolder.txtCrimeNumber.setText(crimes.getNumber());
                crimeViewHolder.txtCrimeName.setText(crimes.getName());
                crimeViewHolder.txtCrimeDesc.setText(crimes.getDescription());


                crimeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SearchCrimesActivity.this, CrimeDetailsActivity.class);
                        i.putExtra("cid",crimes.getCid());
                        startActivity(i);
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
        searchRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
