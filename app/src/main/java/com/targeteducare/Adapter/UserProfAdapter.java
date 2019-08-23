package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.UserProfModel;
import com.targeteducare.R;
import com.targeteducare.UserProfileActivity;

import java.util.ArrayList;

public class UserProfAdapter extends RecyclerView.Adapter<UserProfAdapter.MyViewHolder> {

    Context context;
    private ArrayList<UserProfModel> userProfModels;

    public UserProfAdapter(Context context, ArrayList<UserProfModel> userProfModels) {

        try {
            this.context = context;
            this.userProfModels = userProfModels;

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("UserProfAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userprof_gridview, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        try {

            holder.tv_usersettings.setText(userProfModels.get(position).getUsersettings());
            holder.iv_usersettingsimage.setImageResource(userProfModels.get(position).getUsersettingsimage());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        ((UserProfileActivity) context).referto(userProfModels.get(position).getUsersettingsimage());
                    } catch (Exception e) {
                        ((Activitycommon) context).reporterror("UserProfAdapter", e.toString());
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("UserProfAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return userProfModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_usersettings;
        ImageView iv_usersettingsimage;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_usersettings = itemView.findViewById(R.id.textusersettings);
            iv_usersettingsimage = itemView.findViewById(R.id.imagetextusersettings);
        }
    }
}
