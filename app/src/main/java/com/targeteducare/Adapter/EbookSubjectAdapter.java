package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.targeteducare.Classes.EbookSubjects;
import com.targeteducare.EbookSubjectActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class EbookSubjectAdapter extends RecyclerView.Adapter<EbookSubjectAdapter.MyViewHolder> {

    Context context;
    ArrayList<EbookSubjects> ebookSubjects = new ArrayList<>();
    String lang = "";
    int mSelectedItem = 0;
    int video;


    public EbookSubjectAdapter(Context context, ArrayList<EbookSubjects> ebookSubjects, int video, String lang) {
        this.context = context;
        this.ebookSubjects = ebookSubjects;
        this.lang = lang;
        this.video = video;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.ebook_subject_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        try {

            myViewHolder.tv_select.setSelected(i == mSelectedItem);

            if (myViewHolder.tv_select.isSelected()) {
                myViewHolder.tv_select.setBackgroundResource(R.drawable.textview_onclick);
                ((EbookSubjectActivity) context).goto_ontextviewselected(ebookSubjects.get(i).getEbookDetails());
            } else {
                myViewHolder.tv_select.setBackgroundResource(R.drawable.textview_default);
            }

            if(video == 0){
                myViewHolder.tv_select.setText(Html.fromHtml(ebookSubjects.get(i).getSubjctname()));

            }else{

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ebookSubjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_select;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_select = itemView.findViewById(R.id.tv_select);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                }
            };
            itemView.setOnClickListener(clickListener);
            tv_select.setOnClickListener(clickListener);


        }
    }

    /*public void setSelectedPosition(int position) {
        for (int i = 0; i < ebookSubjects.size(); i++) {
            ebookSubjects.get(i).setSelected(i == position);
        }
        notifyDataSetChanged();
    }

    public void clearSelection() {
        for (int i = 0; i < ebookSubjects.size(); i++) {
            ebookSubjects.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }*/
}

