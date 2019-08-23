package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.BoardSubtypeSelection;
import com.targeteducare.Classes.GetCategorySubBoard;
import com.targeteducare.R;

import java.util.ArrayList;

public class GetCategorySubBoardAdapter extends RecyclerView.Adapter<GetCategorySubBoardAdapter.MyViewHolder> {

    Context context;
    ArrayList<GetCategorySubBoard> getCategorySubBoards;

    public GetCategorySubBoardAdapter(Context context, ArrayList<GetCategorySubBoard> getCategorySubBoards) {

        try {
            this.context = context;
            this.getCategorySubBoards = getCategorySubBoards;

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("GetCategorySubBoardAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_board_select, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_subboard.setText(getCategorySubBoards.get(position).getName_subboard());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ((BoardSubtypeSelection) context).referTo(position);

                } catch (Exception e) {
                    ((Activitycommon) context).reporterror("GetCategorySubBoardAdapter", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getCategorySubBoards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subboard;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_subboard = itemView.findViewById(R.id.textviewsubselectboard);
        }
    }
}
