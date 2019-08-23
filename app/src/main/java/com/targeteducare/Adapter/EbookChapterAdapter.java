package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.targeteducare.Classes.EbookChapter;
import com.targeteducare.EBookContentDisplayActivity;
import com.targeteducare.EbookChapterSelectActivity;
import com.targeteducare.EbookSubjectActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class EbookChapterAdapter extends RecyclerView.Adapter<EbookChapterAdapter.MyViewHolder> {

    Context context;
    ArrayList<EbookChapter> ebookChapters = new ArrayList<>();
    String lang = "";

    public EbookChapterAdapter(Context context, ArrayList<EbookChapter> ebookChapters, String lang){
        this.context = context;
        this.ebookChapters = ebookChapters;
        this.lang = lang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ebook_chapter_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        try {
            myViewHolder.tv_unitname.setText(Html.fromHtml(ebookChapters.get(i).getUnitName()));

            myViewHolder.linearlayout_chapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        ((EbookChapterSelectActivity) context).gotochapterselect(ebookChapters.get(i).getEbookContentDetails());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return ebookChapters.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearlayout_chapter;
        ImageView iv_arrow;
        TextView tv_unitname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearlayout_chapter = itemView.findViewById(R.id.linearlayout_chapter);
            iv_arrow = itemView.findViewById(R.id.iv_arrow);
            tv_unitname = itemView.findViewById(R.id.tv_unitname);
        }
    }
}
