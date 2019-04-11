package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Classes.SelectBoardModel;
import com.targeteducare.R;
import com.targeteducare.SelectBoard;

import java.util.ArrayList;

public class SelectBoardAdapter extends RecyclerView.Adapter<SelectBoardAdapter.MyViewHolder>{

    String boardvalue;
    Context context;
    private ArrayList<SelectBoardModel> board;
    public SelectBoardAdapter(Context context, ArrayList<SelectBoardModel> board){
        this.context = context;
        this.board = board;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_select,parent,false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_board.setText(board.get(position).getBoard());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boardvalue = board.get(position).getBoard();
                ((SelectBoard)context).referTo(position,boardvalue);
                //Toast.makeText(context, boardvalue+" "+Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return board.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_board;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_board = itemView.findViewById(R.id.textviewselectboard);
        }
    }
}
