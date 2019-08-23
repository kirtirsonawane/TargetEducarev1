package com.targeteducare.Adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.targeteducare.Classes.EbookVideoDetails;
import com.targeteducare.EBookContentDisplayActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class EBookVideoAdapter extends RecyclerView.Adapter<EBookVideoAdapter.MyViewHolder> {

    Context context;
    ArrayList<EbookVideoDetails> ebookVideoDetails = new ArrayList<>();
    String lang = "";
    boolean playWhenReady = true;

    public EBookVideoAdapter(Context context, ArrayList<EbookVideoDetails> ebookVideoDetails, String lang){
        this.context = context;
        this.ebookVideoDetails = ebookVideoDetails;
        this.lang = lang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ebook_video_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        try {
            myViewHolder.tv_videotitle.setText(Html.fromHtml(ebookVideoDetails.get(i).getVideotitle()));
            Picasso.with(context)
                    .load(ebookVideoDetails.get(i).getVideoimg())
                    .placeholder(R.drawable.pkgdefault)
                    .error(R.drawable.pkgdefault)
                    .into(myViewHolder.iv_video);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EBookContentDisplayActivity)context).gotoebookfragment(ebookVideoDetails.get(i));
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return ebookVideoDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_videotitle;
        ImageView iv_video;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_videotitle = itemView.findViewById(R.id.tv_videotitle);
            iv_video = itemView.findViewById(R.id.iv_video);
        }
    }
}
