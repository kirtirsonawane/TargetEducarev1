package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.targeteducare.Classes.Menu;
import com.targeteducare.GridMainActivity;
import com.targeteducare.R;
import java.util.ArrayList;

public class CustomAdapterforGridMain extends RecyclerView.Adapter<CustomAdapterforGridMain.MyViewHolder> {

    Context context;
    private ArrayList<Menu> dataSet;

    public CustomAdapterforGridMain(Context context, ArrayList<Menu> data) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterforGridMain.MyViewHolder holder, final int position) {
        // set the data in items
        holder.name.setText(dataSet.get(position).getName());
        holder.image.setImageResource(dataSet.get(position).getIcon());

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                ((GridMainActivity)context).referto(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.icon);
        }
    }
}
