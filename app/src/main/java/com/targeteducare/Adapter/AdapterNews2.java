package com.targeteducare.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.ActivityChatFaq;
import com.targeteducare.ActivityNews;
import com.targeteducare.Classes.ChatQuestionsNAnswers;
import com.targeteducare.Classes.SplashModel;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class AdapterNews2 extends RecyclerView.Adapter<AdapterNews2.Holder> implements Filterable {
    Context context;
    ArrayList<SplashModel> splashModelArrayList = new ArrayList<>();

    ArrayList<SplashModel> list1;

    public AdapterNews2(Context context, ArrayList<SplashModel> question) {
       try {


        this.context = context;
        this.splashModelArrayList.addAll(question);
        this.list1 = (question);

    }catch (Exception e){
           e.printStackTrace();
       }
    }

    public AdapterNews2() {
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_news, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        try {

            // holder.news_title.setText(splashModelArrayList.get(i).getTitle());


            if (GlobalValues.langs.equalsIgnoreCase("mr")) {
                holder.news_title.setText(splashModelArrayList.get(i).getTitle_Marathi());
            } else {
                holder.news_title.setText(splashModelArrayList.get(i).getTitle());
            }


            holder.cardView_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalValues.langs.equalsIgnoreCase("mr")) {
                        ((ActivityNews) context).gotoDescription(splashModelArrayList.get(i).getTitle_Marathi(), splashModelArrayList.get(i).getDescription_Marathi(), splashModelArrayList.get(i).getId());
                    } else {
                        ((ActivityNews) context).gotoDescription(splashModelArrayList.get(i).getTitle(), splashModelArrayList.get(i).getDescription(), splashModelArrayList.get(i).getId());
                    }


                }
            });
            holder.color_textview.setBackgroundColor(getRandomColor());
            holder.description_cardviewdate.setText((context).getResources().getString(R.string.date)+" : "+splashModelArrayList.get(i).getFromDate());

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalValues.langs.equalsIgnoreCase("mr")) {
                        ((ActivityNews) context).gotoshare(splashModelArrayList.get(i).getTitle_Marathi(), splashModelArrayList.get(i).getDescription_Marathi());
                    } else {
                        ((ActivityNews) context).gotoshare(splashModelArrayList.get(i).getTitle(), splashModelArrayList.get(i).getDescription());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(250, rnd.nextInt(254), rnd.nextInt(253), rnd.nextInt(254));
    }

    @Override
    public int getItemCount() {
        return splashModelArrayList.size();
    }


    @Override
    public Filter getFilter() {
        return datasetFilterFull;
    }


    protected Filter datasetFilterFull = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SplashModel> filteredlist1 = new ArrayList<>();
            splashModelArrayList.addAll(list1);
            Log.e(" id ", " " + constraint);
            try {

                String s=((ActivityNews)context).promoid;

                Log.e("filter size", " ");
                if (s.length() > 0) {

                    String Adata=constraint.toString();

                    Log.e("adata "," "+Adata.length());
                    String[] sdata = Adata.split(" ");

                    for (int i = 0; i < sdata.length; i++) {



                        for (SplashModel answers : list1) {

                            if ((answers.getId()).toLowerCase().contains(sdata[i].toLowerCase())) {

                                if ((answers.getId().toLowerCase()).equalsIgnoreCase(sdata[i].toLowerCase())) {

                                    filteredlist1.add(answers);

                                }
                            }


                        }




                        /*if ((constraint).toString().equalsIgnoreCase(list1.get(i).getId())) {
                            SplashModel model = new SplashModel();
                            model.setId(splashModelArrayList.get(i).getId());
                            model.setType(splashModelArrayList.get(i).getType());
                            model.setCreatedDate(splashModelArrayList.get(i).getCreatedDate());
                            model.setDescription(splashModelArrayList.get(i).getDescription());
                            model.setTitle(splashModelArrayList.get(i).getTitle());

                            model.setToDate(splashModelArrayList.get(i).getToDate());
                            filteredlist1.add(model);
                            Log.e(" id ", " " + model.getId());
                        }*/


                        Log.e("filter size1", " " + filteredlist1.size());
                    }


                } else if (constraint == null) {
                    filteredlist1.addAll(list1);
                    Log.e("filter size1", " else " + filteredlist1.size());
                }
                else {
                    filteredlist1.addAll(list1);
                    Log.e("filter size12", " else " + filteredlist1.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

       /*     for (int i = 0; i < filteredlist1.size(); i++) {
                Log.e("for filter ", " " + filteredlist1.get(i).getTitle());
            }*/


            FilterResults results = new FilterResults();
            results.values = filteredlist1;
          /*  for (int i = 0; i < filteredlist1.size(); i++) {
                Log.e("for filter 1", " " + filteredlist1.get(i).getTitle());
            }
*/

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                //  notifyDataSetChanged();
                /*for (int i = 0; i < filteredlist1.size(); i++) {
                    Log.e(" layouts1 ", " " + results.values);
                }*/



                splashModelArrayList.clear();

                splashModelArrayList= (ArrayList<SplashModel>) results.values;
                Log.e("splashModelArrayList", " " + splashModelArrayList.size());
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };


    public class Holder extends RecyclerView.ViewHolder {
        View color_textview;
        TextView news_title, description_cardviewdate;//, enddate;
        CardView cardView_description;
        ImageView share;

        public Holder(View view) {
            super(view);
            view.setTag(this);
            final int position = getAdapterPosition();

            color_textview = (View) view.findViewById(R.id.textview_color);
            news_title = (TextView) view.findViewById(R.id.title_news);
            cardView_description = (CardView) view.findViewById(R.id.description_cardview);
            description_cardviewdate = (TextView) view.findViewById(R.id.description_cardviewdate);
          //  enddate = (TextView) view.findViewById(R.id.enddate);

            share=(ImageView)view.findViewById(R.id.share);

        }


    }


}
