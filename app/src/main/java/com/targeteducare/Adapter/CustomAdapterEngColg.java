package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.Classes.EngColgDataModel;
import com.targeteducare.EngineeringCollegesActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class CustomAdapterEngColg extends RecyclerView.Adapter<CustomAdapterEngColg.MyViewHolder> implements Filterable {
    ArrayList<EngColgDataModel> datasetFilter = new ArrayList<>();
    ArrayList<EngColgDataModel> dataSet;
    Context context;

    public CustomAdapterEngColg(Context context, ArrayList<EngColgDataModel> data) {
        try {
            this.datasetFilter.addAll(data);
            this.dataSet = data;
            this.context = context;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        //view.setOnClickListener(EngineeringCollegesActivity.myOnClickListener);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull  CustomAdapterEngColg.MyViewHolder holder, final int position) {


       holder.icon.setImageResource(dataSet.get(position).getLogo_img());
        holder.ratings.setText(dataSet.get(position).getRating()+"/5");
        holder.reviews.setText(dataSet.get(position).getReviews()+" reviews");
        holder.collegename.setText(dataSet.get(position).getCollege_name());
        holder.establishedyear.setText(""+dataSet.get(position).getEstablished_year());
        holder.noofcourses.setText(""+dataSet.get(position).getCourses());
        holder.institutetype.setText(dataSet.get(position).getInstitute_type());
        holder.examtype.setText(dataSet.get(position).getExam_name());


        holder.cb_compare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.e("is Checked", String.valueOf((isChecked)));


                if (isChecked == true) {
                    Log.e("is Checked in if", String.valueOf((isChecked)));
                    ((EngineeringCollegesActivity) context).gotocompare_function( 1,position);


                } else if (isChecked == false) {
                    ((EngineeringCollegesActivity) context).gotocompare_function( 0,position);

                }

            }
        });

    }





    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public Filter getFilter() {
        return datasetFilterFull;
    }
    private Filter datasetFilterFull = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<EngColgDataModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(datasetFilter);

                //Log.e("called ","calle "+filteredList.size());

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (EngColgDataModel item : datasetFilter) {


                    if ((item.getExam_name().toLowerCase().contains(filterPattern)) || (item.getCollege_name().toLowerCase().contains(filterPattern)) ||
                            (Integer.toString(item.getCourses()).toLowerCase().contains(filterPattern)))
                    {
                        filteredList.add(item);
                    }
                }
                Log.e("called1 ","called1 "+filteredList.size());
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSet.clear();
            dataSet.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView ratings;
        TextView reviews;
        TextView collegename;
        TextView establishedyear;
        TextView noofcourses;
        TextView institutetype;
        TextView examtype;
        Button btn_getcontact;
        CheckBox cb_compare;
        Button college_compare_button;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.logoimageview);
            ratings = itemView.findViewById(R.id.ratingview);
            reviews = itemView.findViewById(R.id.reviewview);
            collegename = itemView.findViewById(R.id.collegename);
            establishedyear = itemView.findViewById(R.id.estdview);
            noofcourses = itemView.findViewById(R.id.courseview);
            institutetype = itemView.findViewById(R.id.typeofinstview);
            examtype = itemView.findViewById(R.id.examtogiveview);
            btn_getcontact = itemView.findViewById(R.id.btn_getcontact);
            cb_compare = itemView.findViewById(R.id.cb_compare);
            college_compare_button=itemView.findViewById(R.id.college_compare_button);
        }



    }

}
