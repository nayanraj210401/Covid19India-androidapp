package com.example.covid19;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<String> states, values;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> states, ArrayList<String> values) {
        this.states = states;
        this.values = values;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activity, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("loc","Val"+states);
        holder.state.setText(states.get(position));
        holder.values.setText(values.get(position));
        int val = Integer.parseInt(values.get(position));
        if(val > 5000){
            holder.state.setTextColor(Color.rgb(255,0,0));

        }else if(val < 5000 && val > 1000){
            holder.state.setTextColor(Color.rgb(255,255,0));
        }else{
            holder.state.setTextColor(Color.rgb(0,120,0));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView state, values;

        public MyViewHolder(View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.stateName);
            values = itemView.findViewById(R.id.stateCases);

        }

    }

}