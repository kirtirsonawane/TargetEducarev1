package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.ActivitySubCategory;
import com.targeteducare.Classes.SubCategoryModel;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;

import java.util.ArrayList;

public class AdapterSubCategory extends RecyclerView.Adapter<AdapterSubCategory.Holder> {
Context context;
    ArrayList<SubCategoryModel> list;

/*
    String style = "<style type=\"text/css\">\n" +
            "@font-face {\n" +
            "    font-family: Symbol;\n" +
            "    src:url(\"file:///android_asset/fonts/symbol.ttf\"), url(\"file:///android_asset/fonts/symbol_webfont.woff\"), url(\"file:///android_asset/fonts/symbol_webfont.woff2\");\n" +
            "}\n" +
            "@font-face {\n" +
            "    font-family: Calibri;\n" +
            "    src: url(\"file:///android_asset/fonts/calibri.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: AkrutiDevPriya;\n" +
            "    src: url(\"file:///android_asset/fonts/akrutidevpriyanormal.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: Times New Roman,serif;\n" +
            "    src: url(\"file:///android_asset/fonts/timesnewroman.ttf\")\n" +
            "}\n" +
            "\n" +
            "</style>";*/


    public AdapterSubCategory(Context context, ArrayList<SubCategoryModel> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_subcategory, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        try {


        if (GlobalValues.langs.equalsIgnoreCase("mr")){
            holder.SubSupport.setText(/*style+*/list.get(i).getNameInMarathi());
            holder.SubSupport.setTypeface(Fonter.getTypefacesemibold( context));
            holder.SubSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((ActivitySubCategory) context).gotonext(i);
                }
            });
        }
        else {
            holder.SubSupport.setText(/*style+*/list.get(i).getName());
            holder.SubSupport.setTypeface(Fonter.getTypefacesemibold( context));
            holder.SubSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((ActivitySubCategory) context).gotonext(i);
                }
            });
        }
    }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class Holder extends RecyclerView.ViewHolder {
        TextView SubSupport;


        public Holder(View view) {
            super(view);
            view.setTag(this);
            final int position = getAdapterPosition();
            SubSupport = (TextView) view.findViewById(R.id.subcategory_label);




        }



    }
}
