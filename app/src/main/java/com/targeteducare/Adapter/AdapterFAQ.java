package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.targeteducare.Classes.Questions;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;

import java.util.ArrayList;

public class AdapterFAQ extends RecyclerView.Adapter<AdapterFAQ.Holder> {
    Context context;
    ArrayList<Questions> question;
    String lang="";

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
            "</style>";


    public AdapterFAQ(Context context, ArrayList<Questions> question) {
        this.context = context;
        this.question = question;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_faq, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        try {
        if (GlobalValues.langs.equalsIgnoreCase("mr")){
            holder.faq_questions.setText(question.get(i).getTitleInMarathi());
            holder.faq_answers.loadDataWithBaseURL("file:///",style+question.get(i).getExplanationInMarathi(),"text/html","utf-8",null);
            holder.faq_questions.setTypeface(Fonter.getTypefacesemibold( context));
           /* holder.faq_answers.setTypeface(Fonter.getTypefaceregular( context));*/
        }
        else{
            holder.faq_questions.setText(question.get(i).getTitle());
            holder.faq_answers.loadDataWithBaseURL("file:///",style+question.get(i).getExplanation(),"text/html","utf-8",null);
            holder.faq_questions.setTypeface(Fonter.getTypefacesemibold( context));
            //web.loadDataWithBaseURL("file:///", style + mParam1.getName(), "text/html", "utf-8", null);

        }

    }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return question.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView faq_questions;
        WebView faq_answers;



        public Holder(View view) {
            super(view);
            view.setTag(this);
            final int position = getAdapterPosition();
            faq_questions = (TextView) view.findViewById(R.id.faq_label);
          faq_answers = (WebView) view.findViewById(R.id.explanation_faq);



        }



    }

}