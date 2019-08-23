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
    int initial = 0;


    public EbookSubjectAdapter(Context context, ArrayList<EbookSubjects> ebookSubjects, String lang) {
        this.context = context;
        this.ebookSubjects = ebookSubjects;
        this.lang = lang;
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

        myViewHolder.tv_select.setText(Html.fromHtml(ebookSubjects.get(i).getSubjctname()));

        if (initial == 0) {
            initial = 1;
            ((EbookSubjectActivity) context).goto_ontextviewselected(ebookSubjects.get(0).getEbookDetails());
            myViewHolder.tv_select.setBackgroundResource(R.drawable.textview_onclick);
            ebookSubjects.get(0).setSelected(true);
        }

        myViewHolder.tv_select.setSelected(ebookSubjects.get(i).isSelected());

        myViewHolder.tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ebookSubjects.get(i).isSelected()){
                    myViewHolder.tv_select.setBackgroundResource(R.drawable.textview_onclick);
                    ((EbookSubjectActivity) context).goto_ontextviewselected(ebookSubjects.get(i).getEbookDetails());
                    ebookSubjects.get(i).setSelected(false);
                }
                else{
                    myViewHolder.tv_select.setBackgroundResource(R.drawable.textview_default);
                    ebookSubjects.get(i).setSelected(true);
                }

            }
        });

        /*if (ebookSubjects.get(i).isSelected()) {
            myViewHolder.tv_select.setBackgroundResource(R.drawable.textview_onclick);
            ebookSubjects.get(i).setSelected(false);
        } else {
            myViewHolder.tv_select.setBackgroundResource(R.drawable.textview_default);
            ebookSubjects.get(i).setSelected(true);
        }*/
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

