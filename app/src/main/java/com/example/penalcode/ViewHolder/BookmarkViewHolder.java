package com.example.penalcode.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penalcode.Interface.ItemClickListener;
import com.example.penalcode.R;


public class BookmarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtCrimeName, txtCrimeNumber, txtCrimeDesc;
    private ItemClickListener itemClickListener;

    public BookmarkViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCrimeName = itemView.findViewById(R.id.book_crime_name);
        txtCrimeNumber = itemView.findViewById(R.id.book_crime_number);
        txtCrimeDesc = itemView.findViewById(R.id.book_crime_desc);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
