package com.targeteducare.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;
import com.targeteducare.TouchImageView;

import java.util.ArrayList;

public class QuestionURLDataAdapter extends RecyclerView.Adapter<QuestionURLDataAdapter.viewHolder> {
    Context mContext;
    ArrayList<QuestionURL> mdataset;

    public QuestionURLDataAdapter(Context mContext, ArrayList<QuestionURL> mdataset) {
        this.mContext = mContext;
        this.mdataset = mdataset;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_item_image, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        try {
            Log.e("data ", "data " + mdataset.get(0).getImagemainsource() + " " + mdataset.get(0).getId());
/*
            Picasso.with(mContext).load(mdataset.get(position).getImagemainsource())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(holder.imageView1);*/

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(mdataset.get(position).getImagemainsource(), bmOptions);
            int height = holder.imageView1.getHeight() + 200;
            holder.imageView1.getLayoutParams().height = height;
            holder.imageView1.getLayoutParams().width = (GlobalValues.width - 90);
            holder.imageView1.setImageBitmap(bitmap);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)  holder.imageView1.getLayoutParams();
            params.width = (GlobalValues.width - 90);
            params.height = pxToDp(bitmap.getHeight());
            holder.imageView1.setLayoutParams(params);
        /*    holder.mLayout.getLayoutParams().height = height;
            holder.mLayout.getLayoutParams().width = (GlobalValues.width - 90);*/
           // holder.imageView1.setVisibility(View.GONE);
           /* holder.web.getSettings().setJavaScriptEnabled(true);
            holder.web.getSettings().setAllowFileAccess(true);
            holder.web.getSettings().setBuiltInZoomControls(true);
            holder.web.getSettings().setSupportZoom(true);
            holder.web.loadUrl("file:///"+ mdataset.get(position).getImagemainsource());*/
        //    holder.web.loadDataWithBaseURL("file:///", mdataset.get(position).getImagemainsource(), "text/html", "utf-8", null);
        } catch (Exception e) {
            Log.e("error ", "error " + e);
            e.printStackTrace();
            //  ((ActivityCommon) context).reporterror("ImagePagerAdapter", e.toString());

        }
    }
    public static int pxToDp(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        // return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
    @Override
    public int getItemCount() {
        return mdataset.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TouchImageView imageView1;
        LinearLayout mLayout;
       // WebView web;

        public viewHolder(View itemView) {
            super(itemView);
            imageView1 = (TouchImageView) itemView.findViewById(R.id.viewPagerItem_image2);
            mLayout=(LinearLayout)itemView.findViewById(R.id.layout_1);
           // web = (WebView) itemView.findViewById(R.id.webview_1);
        }

    }


}