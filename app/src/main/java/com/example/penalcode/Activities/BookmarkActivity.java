package com.example.penalcode.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.penalcode.Model.Bookmarks;
import com.example.penalcode.Prevalent.Prevalent;
import com.example.penalcode.R;
import com.example.penalcode.ViewHolder.BookmarkViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView bookmarkRecyclerView;
    private TextView totalPriceAmount, message1;

    private  RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bookmarkRecyclerView = (RecyclerView)findViewById(R.id.bookmark_recyclerView);

        message1 = (TextView)findViewById(R.id.msg1);

        bookmarkRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        bookmarkRecyclerView.setLayoutManager(layoutManager);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(bookmarkRecyclerView!=null)
        {
            message1.setVisibility(View.GONE);
           // message1.getText();

        }
    }
    @Override
    protected void onStart() {
        super.onStart();

       // CheckOrderState();

        final DatabaseReference bookmarkListRef = FirebaseDatabase.getInstance().getReference().child("Bookmark List");
        bookmarkListRef.keepSynced(true);

        FirebaseRecyclerOptions<Bookmarks> options =
                new FirebaseRecyclerOptions.Builder<Bookmarks>()
                        .setQuery(bookmarkListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Crimes"),Bookmarks.class).build();
        final FirebaseRecyclerAdapter<Bookmarks, BookmarkViewHolder> adapter =
                new FirebaseRecyclerAdapter<Bookmarks, BookmarkViewHolder>(options) {

//                    @Override
//                    public int getItemCount(){
//                        return
//                    }


                    @Override
                    protected void onBindViewHolder(@NonNull BookmarkViewHolder bookmarkViewHolder, int i, @NonNull final Bookmarks bookmarks) {


                        bookmarkViewHolder.txtCrimeNumber.setText(bookmarks.getNumber());
                        bookmarkViewHolder.txtCrimeName.setText(bookmarks.getName());
                        bookmarkViewHolder.txtCrimeDesc.setText(bookmarks.getDescription());


                        bookmarkViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence[] options = new CharSequence[]
                                        {
                                                "Remove",
                                                "View"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                                builder.setTitle("Favorites Options:");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which==0){
                                            bookmarkListRef.child("User View")
                                                    .child(Prevalent.currentOnlineUser.getPhone())
                                                    .child("Crimes")
                                                    .child(bookmarks.getCid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(BookmarkActivity.this,"Item removed successfully",Toast.LENGTH_SHORT).show();
                                                                Intent i = new Intent(BookmarkActivity.this,HomeActivity.class);
                                                                startActivity(i);
                                                            }
                                                        }
                                                    });
                                        }
                                        if(which==1){
                                            Intent i = new Intent(BookmarkActivity.this,CrimeDetailsActivity.class);
                                            i.putExtra("cid",bookmarks.getCid());
                                            startActivity(i);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_items_list,parent,false);
                        BookmarkViewHolder holder = new BookmarkViewHolder(view);
                        return holder;

                    }
                };
        bookmarkRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
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
