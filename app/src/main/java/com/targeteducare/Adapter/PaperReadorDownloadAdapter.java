package com.targeteducare.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.targeteducare.Classes.PaperModel;
import com.targeteducare.Constants;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;
import com.targeteducare.SamplePapersActivity;
import com.targeteducare.Sample_Paper_Video_Activity;
import com.targeteducare.Sample_Paper_Webview_Activity;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

public class PaperReadorDownloadAdapter extends RecyclerView.Adapter<PaperReadorDownloadAdapter.MyViewHolder> implements Filterable {
    Context context;
    private ArrayList<PaperModel> datasetFilter;
    private ArrayList<PaperModel> dataSet = new ArrayList<>();

    public PaperReadorDownloadAdapter(Context context, ArrayList<PaperModel> data) {
        datasetFilter = data;
        this.dataSet.addAll(data);
        /*     datasetFilter.addAll(data);*/
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.samplepaper_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PaperReadorDownloadAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        try {

            holder.Sample_Paper_View_More.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.sample_paper_dialog_box);


                    TextView dialog_heading = (TextView) dialog.findViewById((R.id.sample_papers_dialog_heading));

                    dialog_heading.setText("Details : ");
                    Log.e("Discription", dataSet.get(position).getDescription());
                    TextView text1 = (TextView) dialog.findViewById(R.id.sample_papers_dialog_discription);

                    //  text.setText(Packages_TextView_Discription.getText());

                    text1.setText(dataSet.get(position).getDescription());

                    ImageView dialog_close = (ImageView) dialog.findViewById(R.id.dialog_close_button);


                    dialog_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                    dialog.show();


                }
            });

            holder.Sample_Paper_Subject_Name.setTypeface(Fonter.getTypefacesemibold(context));
            holder.Sample_Paper_Subject_Name.setText("Subject Name : " + dataSet.get(position).getSubject());
            Log.e("dataset :: ", dataSet.toString());
            Log.e("Id :: ", String.valueOf(dataSet.get(position).getId()));
            //holder.papername.setImageResource(Integer.parseInt(dataSet.get(position).getSubject()));
            holder.Sample_Paper_Quiz_Name.setTypeface(Fonter.getTypefacebold(context));

            holder.Sample_Paper_Quiz_Name.setText("Name : " + dataSet.get(position).getName());

            holder.Sample_Paper_discription.setTypeface(Fonter.getTypefacesemibold(context));
            holder.Sample_Paper_discription.setText(dataSet.get(position).getDescription());
            //holder.Sample_Paper_Image.setText(dataSet.get(position).getName());

            String Type_of_document = "";

            Type_of_document = dataSet.get(position).getType();
            if (Type_of_document.equals("Video")) {
                holder.Sample_Paper_Button_View_or_Read.setText("View");
                holder.Sample_Paper_Button_View_or_Read.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            String Name_of_subject_1="";
                            Name_of_subject_1=dataSet.get(position).getSubject();

                            String Name_of_course="";
                            Name_of_course=dataSet.get(position).getName();

                            Intent intent = new Intent(context, Sample_Paper_Video_Activity.class);
                            String Url=dataSet.get(position).getVideoUrl();
                            Log.e("position :: ",String.valueOf(position));

                            intent.putExtra("Video_Url",Url);
                            intent.putExtra("Name_of_subject",Name_of_subject_1);
                            intent.putExtra("Name_of_course",Name_of_course);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            } else {
                holder.Sample_Paper_Button_View_or_Read.setText("Read");
                holder.Sample_Paper_Button_View_or_Read.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Name_of_Course="";
                        Name_of_Course=dataSet.get(position).getName();
                        Intent intent=new Intent(context, Sample_Paper_Webview_Activity.class);
                        GlobalValues.WEB_VIEW =dataSet.get(position).getDescription();

                        intent.putExtra("Title",Name_of_Course);
                        context.startActivity(intent);
                    }
                });
            }

            String Image_path = dataSet.get(position).getImagefile();
            Log.e("data image ", dataSet.get(position).getImagefile());
            Picasso.with(context)
                    .load(Image_path)
                    .placeholder(R.drawable.ic_exam)
                    .error(R.drawable.ic_launcher)
                    .into(holder.Sample_Paper_Image);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        public String s = "";

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PaperModel> filteredList = new ArrayList<>();
            Log.e("s ", "sval " + ((SamplePapersActivity) context).sdata);
            String sdata ="";
            sdata=((SamplePapersActivity) context).sdata;
            if ((constraint == null || constraint.length() == 0)&&(sdata==null||sdata.length()==0)) {

                filteredList.addAll(datasetFilter);
                Log.e("called ","constraint "+constraint);

                Log.e("called ","sdata "+sdata);
                Log.e("called ","calle "+filteredList.size());

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                String[] parts = filterPattern.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    for (PaperModel item : datasetFilter) {
                        Log.e("filter pattern:: ", filterPattern);
                        Log.e("type ", item.getType());
                        if ((item.getName().toLowerCase().contains(parts[i])) || (item.getSubject().toLowerCase().contains(parts[i])) ||
                                ((item.getType()).toLowerCase().contains(parts[i]))) {
                            ArrayList<PaperModel> filteredList1 = new ArrayList<>();
                            String[] sdataval=sdata.split(" ");
                            Log.e("valsize ","val "+sdataval.length);
                            for (int j=0;j<sdataval.length;j++) {
                                if (sdataval[j].equalsIgnoreCase(item.getType())) {
                                    Log.e("valcal ","val1 "+j+" "+sdataval[j]+" "+item.getType());
                                    //   filteredList.add(item);
                                    Log.e("valcalbefore","val "+j+" "+filteredList1.size());

                                    filteredList1.add(item);
                                    Log.e("valcalafter","val "+j+" "+filteredList1.size());
                                    // break;

                                }

                            }
                            filteredList.addAll(filteredList1);
                            //  break;
                        }

                    }
                }

            }


            Log.e("called1 ", "called1 " + filteredList.size());


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

        public void setdata(String s) {
            this.s = s;
        }
    };


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView Sample_Paper_Quiz_Name;
        ImageView Sample_Paper_Image;
        TextView Sample_Paper_Subject_Name;
        TextView Sample_Paper_discription;
        Button Sample_Paper_Button_View_or_Read;
        TextView Sample_Paper_View_More;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's


            Sample_Paper_View_More = itemView.findViewById(R.id.sample_papers_view_more);
            Sample_Paper_Quiz_Name = itemView.findViewById(R.id.sample_papers_name_of_quiz);
            Sample_Paper_Subject_Name = itemView.findViewById(R.id.sample_papers_name_of_subject);
            Sample_Paper_Image = itemView.findViewById(R.id.sample_papers_image);
            Sample_Paper_discription = itemView.findViewById(R.id.sample_papers_discription);
            Sample_Paper_Button_View_or_Read = itemView.findViewById(R.id.sample_papers_view_or_read_button);
        }

      /*  public static void gotochanged(int i) {
            if

        }*/
    }

}
