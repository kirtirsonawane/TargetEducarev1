package com.targeteducare.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Classes.CategoryModel;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;
import com.targeteducare.SupportActivity;

import java.util.ArrayList;

public class AdapterSupportListview extends RecyclerView.Adapter<AdapterSupportListview.Holder> {


ArrayList<CategoryModel> list =new ArrayList<CategoryModel>();
Context context;
 /*   String style = "<style type=\"text/css\">\n" +
            "@font-face {\n" +
            "    font-family: Symbol;\n" +
            "    src: url(\"file:///android_asset/fonts/hinted_symbolmt.ttf\")\n" +
            "}\n" +
            "@font-face {\n" +
            "    font-family: Calibri;\n" +
            "    src: url(\"file:///android_asset/fonts/calibri.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: AkrutiDevPriya;\n" +
            "    src: url(\"file:///android_asset/fonts/akrutidevpriyanormal.ttf\")\n" +
            "}\n" +
            "\n" +
            "</style>";*/


    public AdapterSupportListview( Context context, ArrayList<CategoryModel> list) {

        this.context=context;
        this.list=list;
    }



    @Override
    public Holder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout_category, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder( Holder holder, final int i) {
        try {
        if(GlobalValues.langs.equalsIgnoreCase("mr")) {

                holder.Support.setText(list.get(i).getNameInMarathi());
                holder.Support.setTypeface(Fonter.getTypefacesemibold(context));
                holder.Support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ((SupportActivity) context).gotonext(i);
                    }
                });
            }
        else{


                holder.Support.setTypeface(Fonter.getTypefacesemibold(context));
                holder.Support.setText(list.get(i).getName());

                holder.Support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((SupportActivity) context).gotonext(i);
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
        TextView Support;


        public Holder(View view) {
            super(view);
            view.setTag(this);
            final int position = getAdapterPosition();
            Support = (TextView) view.findViewById(R.id.label);

        }



    }
}

