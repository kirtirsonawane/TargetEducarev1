package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.GetCategoryBoard;
import com.targeteducare.R;
import com.targeteducare.SelectBoardActivity;

import java.util.ArrayList;

public class GetCategoryBoardAdapter extends RecyclerView.Adapter<GetCategoryBoardAdapter.MyViewHolder> {

    String boardvalue = "";
    String name_board;
    Context context;

    private ArrayList<GetCategoryBoard> getCategoryBoards;

    public GetCategoryBoardAdapter(Context context, ArrayList<GetCategoryBoard> getCategoryBoardsdata) {

        try {
            this.context = context;
            this.getCategoryBoards = getCategoryBoardsdata;
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("GetCategoryBoardAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_select, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_board.setText(getCategoryBoards.get(position).getName_board());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    boardvalue = getCategoryBoards.get(position).getName_board();
                    ((SelectBoardActivity) context).referTo(position, boardvalue);

                } catch (Exception e) {
                    ((Activitycommon) context).reporterror("GetCategoryBoardAdapter", e.toString());
                    e.printStackTrace();
                }
                //Toast.makeText(context, boardvalue+" "+Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getCategoryBoards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_board;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_board = itemView.findViewById(R.id.textviewselectboard);
        }
    }
}
