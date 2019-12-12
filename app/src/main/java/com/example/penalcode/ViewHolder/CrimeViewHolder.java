package com.example.penalcode.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penalcode.Interface.ItemClickListener;
import com.example.penalcode.R;

public class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtCrimeNumber, txtCrimeDesc, txtCrimeName;

    public ItemClickListener listener;

    public CrimeViewHolder(@NonNull View itemView) {
        super(itemView);


        txtCrimeNumber=(TextView)itemView.findViewById(R.id.crime_number1);
        txtCrimeDesc=(TextView)itemView.findViewById(R.id.crime_desc1);
        txtCrimeName=(TextView)itemView.findViewById(R.id.crime_name1);
    }
    public void setItemClickListener(){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
