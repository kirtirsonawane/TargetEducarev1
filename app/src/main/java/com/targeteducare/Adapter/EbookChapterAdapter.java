package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.targeteducare.Classes.EbookChapterContentDetails;
import com.targeteducare.EbookChapterSelectActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class EbookChapterAdapter extends RecyclerView.Adapter<EbookChapterAdapter.MyViewHolder> {

    Context context;
    ArrayList<EbookChapterContentDetails> ebookContentDetails = new ArrayList<>();
    String lang = "";

    int progress = 0;

    public EbookChapterAdapter(Context context, ArrayList<EbookChapterContentDetails> ebookContentDetails, String lang){
        this.context = context;
        this.ebookContentDetails = ebookContentDetails;
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
            myViewHolder.tv_unitname.setText(Html.fromHtml(ebookContentDetails.get(i).getChapterName()));

            //Log.e("type video ", ebookContentDetails.get(i).getType());
            if(ebookContentDetails.get(i).getType().equalsIgnoreCase("Video")){
                myViewHolder.progress_chapter.setVisibility(View.GONE);
                myViewHolder.tv_progress.setVisibility(View.GONE);
            }

            if(ebookContentDetails.get(i).getNoofpages()==0){
                myViewHolder.progress_chapter.setVisibility(View.GONE);
                myViewHolder.tv_progress.setVisibility(View.GONE);
                myViewHolder.linearlayout_chapter.setVisibility(View.GONE);
            }

            /*if(ebookContentDetails.get(i).getNoofpages()>=1 && ebookContentDetails.get(i).getLastvisitedpage()==0){
                progress = ((ebookContentDetails.get(i).getProgress())*100)/ebookContentDetails.get(i).getNoofpages();
            }
            else if(ebookContentDetails.get(i).getNoofpages()==1 || ebookContentDetails.get(i).getLastvisitedpage()==1){
                progress = ((ebookContentDetails.get(i).getProgress())*100)/ebookContentDetails.get(i).getNoofpages();
            }else{
                progress = ((ebookContentDetails.get(i).getProgress()+1)*100)/ebookContentDetails.get(i).getNoofpages();
            }*/

            if((ebookContentDetails.get(i).getNoofpages()>0) && (ebookContentDetails.get(i).getType().equalsIgnoreCase("Ebook"))){
                myViewHolder.progress_chapter.setVisibility(View.VISIBLE);
                myViewHolder.tv_progress.setVisibility(View.VISIBLE);
                progress = ((ebookContentDetails.get(i).getProgress()+1)*100)/ebookContentDetails.get(i).getNoofpages();

                myViewHolder.progress_chapter.setProgress(progress);
                myViewHolder.tv_progress.setText(progress + " % covered");
            }

            myViewHolder.linearlayout_chapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        ((EbookChapterSelectActivity) context).gotochapterselect(ebookContentDetails.get(i));

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
        return ebookContentDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearlayout_chapter;
        ImageView iv_arrow;
        TextView tv_unitname, tv_progress;
        ProgressBar progress_chapter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearlayout_chapter = itemView.findViewById(R.id.linearlayout_chapter);
            iv_arrow = itemView.findViewById(R.id.iv_arrow);
            tv_unitname = itemView.findViewById(R.id.tv_unitname);
            progress_chapter = itemView.findViewById(R.id.progress_chapter);
            tv_progress = itemView.findViewById(R.id.tv_progress);
        }
    }
}
