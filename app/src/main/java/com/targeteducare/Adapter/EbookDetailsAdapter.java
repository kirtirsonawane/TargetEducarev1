package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.targeteducare.Classes.EbookDetails;
import com.targeteducare.EbookChapterSelectActivity;
import com.targeteducare.EbookSubjectActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class EbookDetailsAdapter extends RecyclerView.Adapter<EbookDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<EbookDetails> ebookDetails = new ArrayList<>();
    String lang = "";

    public EbookDetailsAdapter(Context context, ArrayList<EbookDetails> ebookDetails, String lang) {
        this.context = context;
        this.ebookDetails = ebookDetails;
        this.lang = lang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ebook_details_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.chapterno.setText(""+(i+1));
        myViewHolder.tv_name.setText(ebookDetails.get(i).getName());
        myViewHolder.tv_description.setText(ebookDetails.get(i).getDescription());
        Picasso.with(context)
                .load(ebookDetails.get(i).getImagefile())
                .placeholder(R.drawable.pkgdefault)
                .error(R.drawable.pkgdefault)
                .into(myViewHolder.iv_subject);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    ((EbookSubjectActivity) context).gotogetbeookcontent(ebookDetails.get(i));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return ebookDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_subject;
        TextView chapterno, tv_name, tv_description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            chapterno = itemView.findViewById(R.id.chapterno);
            iv_subject = itemView.findViewById(R.id.iv_subject);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }
}
