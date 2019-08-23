package com.targeteducare.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.MyPackages;
import com.targeteducare.Constants;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.MyPackagesActivity;
import com.targeteducare.R;

import java.util.ArrayList;
import java.util.List;

public class GetMyPackagesAdapter extends RecyclerView.Adapter<GetMyPackagesAdapter.Holder> implements Filterable {
    ArrayList<MyPackages> arrayList = new ArrayList<>();
    ArrayList<MyPackages> datasetFilter;
    MyPackages myPackages = new MyPackages();
    Context context;
    public ItemClickListener itemClickListener;
    int flag_ispaid = 0;
    String lang = "";

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

    public class Holder extends RecyclerView.ViewHolder {
        TextView Packages_TextView_Course_Name;
        TextView Packages_TextView_Amount;
        TextView Packages_TextView_Discription;
        TextView Packages_TextView_ViewMore;
        ImageView Packages_ImageView;
        CheckBox Packages_CheckBox;
        ImageView image;
        WebView webView;
        WebSettings webSettings;
        ArrayList<MyPackages> myPackagesArrayList;

        public Holder(View view) {
            super(view);
            view.setTag(this);
            Packages_TextView_Amount = (TextView) view.findViewById(R.id.package_textview_amount);
            // Packages_TextView_Discription = (TextView) view.findViewById(R.id.package_textview_discription);
            Packages_TextView_Course_Name = (TextView) view.findViewById(R.id.package_textview_course_name);
            Packages_ImageView = (ImageView) view.findViewById(R.id.package_imageview_1);
            Packages_TextView_Course_Name = (TextView) view.findViewById(R.id.package_textview_course_name);
            Packages_CheckBox = (CheckBox) view.findViewById(R.id.package_checkbox_1);
            Packages_TextView_ViewMore = (TextView) view.findViewById(R.id.package_textview_viewmore);

            myPackagesArrayList = new ArrayList<MyPackages>();
            //  Packages_TextView_ViewMore.setTypeface(Fonter.getTypefaceregular(context));

        }


      /*  public void getdiscriptiontext(int position) {
            final Dialog dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_layout);

            WebView dialog_discp = (WebView) dialog.findViewById(R.id.webview_dialog_discription);
            TextView dialog_heading = (TextView) dialog.findViewById((R.id.textview_dialog_heading));
            dialog_discp.getSettings().setJavaScriptEnabled(true);
            dialog_heading.setText(context.getResources().getString(R.string.dialog_details));
            Log.e("data ", "data " + arrayList.get(position).getDescription());

          //  dialog_discp.loadData(arrayList.get(position).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");


            Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));

            TextView Textview_dialog_Amount = (TextView) dialog.findViewById(R.id.textview_dialog_amount);

            Packages_TextView_Amount.setTypeface(Fonter.getTypefacebold(context));

            Textview_dialog_Amount.setText(Packages_TextView_Amount.getText());

            TextView text1 = (TextView) dialog.findViewById(R.id.textview_course_name);

            *//* webView.loadData(splashModels.get(position).getDescription(),"text/html","uft-8");*//*
            if (GlobalValues.langs.equalsIgnoreCase("mr")) {
                dialog_discp.loadData(arrayList.get(position).getDescription_InMarathi().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
                //textView.setText("\" " + splashModels.get(position).getTitle_Marathi() + " \"");
            } else {
                dialog_discp.loadData(arrayList.get(position).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
               // textView.setText("\" " + splashModels.get(position).getTitle() + " \"");
            }

            text1.setText(Packages_TextView_Course_Name.getText());

            ImageView dialog_close = (ImageView) dialog.findViewById(R.id.dialog_close_button);

            dialog_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
*/
    }


    public GetMyPackagesAdapter(Context context, ArrayList<MyPackages> qList, int flag_ispaid, String lang) {

        try {
            this.arrayList.addAll(qList);
            this.datasetFilter = qList;
            this.context = context;
            this.flag_ispaid = flag_ispaid;
            this.lang = lang;

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("GetMyPackagesAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @NonNull
    @Override

    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_package_adapter_xml_file, parent, false);


        return new Holder(view);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int i) {
        try {
            if (flag_ispaid == 1) {
                Log.e("size of arr in ispaid ", String.valueOf(arrayList.size()));
                holder.Packages_CheckBox.setVisibility(View.GONE);
                for (int j = 0; j < arrayList.size(); j++) {
                    //String urls = Constants.imagepath + arrayList.get(i).getImageurl();
                    String urls = arrayList.get(i).getImageurl();
                    Log.e("url", i + " " + urls);
                    Picasso.with(context)
                            .load(urls)
                            .placeholder(R.drawable.pkgdefault)
                            .error(R.drawable.pkgdefault)
                            .into(holder.Packages_ImageView);
                }
                holder.Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));
                if (!lang.equalsIgnoreCase("mr"))
                    holder.Packages_TextView_Course_Name.setText(Html.fromHtml(arrayList.get(i).getName()));
                else
                    holder.Packages_TextView_Course_Name.setText(Html.fromHtml(arrayList.get(i).getName_InMarathi()));
                holder.Packages_TextView_Amount.setText("₹ " + arrayList.get(i).getAmount());


            } else {
                Log.e("size of array ", String.valueOf(arrayList.size()));
                for (int j = 0; j < arrayList.size(); j++) {
                    //String urls = Constants.imagepath + arrayList.get(i).getImageurl();
                    String urls = arrayList.get(i).getImageurl();
                    Log.e("url", i + " " + urls);
                    Picasso.with(context)
                            .load(urls)
                            .placeholder(R.drawable.pkgdefault)
                            .error(R.drawable.pkgdefault)
                            .into(holder.Packages_ImageView);
                }

                holder.Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));
                if (!lang.equalsIgnoreCase("mr"))
                    holder.Packages_TextView_Course_Name.setText(Html.fromHtml(arrayList.get(i).getName()));
                else
                    holder.Packages_TextView_Course_Name.setText(Html.fromHtml(arrayList.get(i).getName_InMarathi()));
                holder.Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));
                holder.Packages_TextView_Amount.setText("₹ " + arrayList.get(i).getAmount());

                holder.Packages_CheckBox.setTypeface(Fonter.getTypefaceregular(context));


                holder.Packages_CheckBox.setOnCheckedChangeListener(null);
                holder.Packages_CheckBox.setChecked(arrayList.get(i).isSelected());

                holder.Packages_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            Log.e("is Checked", String.valueOf((isChecked)));
                            if (isChecked == true) {
                                Log.e("is Checked in if", String.valueOf((isChecked)));
                                holder.Packages_CheckBox.setText(context.getResources().getString(R.string.remove));
                                ((MyPackagesActivity) context).gotogetamount(arrayList.get(i).getAmount(), 1);
                                Log.e("value of amount :: ::", getAmount().toString());
                                Log.e("checked ", String.valueOf(arrayList.get(i).getAmount()));
                            } else if (isChecked == false) {
                                holder.Packages_CheckBox.setText(context.getResources().getString(R.string.add));
                                ((MyPackagesActivity) context).gotogetamount(arrayList.get(i).getAmount(), 0);
                            }
                            arrayList.get(i).setIsSelected(isChecked);
                            //  GetMyPackagesAdapter.this.notify();

                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("GetMyPackagesAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });
            }
            //  holder.Packages_TextView_ViewMore.setVisibility(View.GONE);
            holder.Packages_TextView_ViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activitycommon)context).opendialog(arrayList.get(i));
                    /* int position = getAdapterPosition();*/
                    //  String discription_data=arrayList.get(position).getDescription();
                    // ((MyPackagesActivity)context).gotocallfragment(discription_data);
                    /* getdiscriptiontext(i);*/
                  /* try {
                       final Dialog dialog = new Dialog(context);
                       dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                       dialog.setContentView(R.layout.dialog_layout);
                       WebView dialog_discp = (WebView) dialog.findViewById(R.id.webview_dialog_discription);
                       TextView dialog_heading = (TextView) dialog.findViewById((R.id.textview_dialog_heading));
                       dialog_discp.getSettings().setJavaScriptEnabled(true);
                       dialog_heading.setText(context.getResources().getString(R.string.dialog_details));
                       Log.e("data ", "data " + arrayList.get(i).getDescription());

                       //  dialog_discp.loadData(arrayList.get(position).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");


                       holder.Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));

                       TextView Textview_dialog_Amount = (TextView) dialog.findViewById(R.id.textview_dialog_amount);

                       holder.Packages_TextView_Amount.setTypeface(Fonter.getTypefacebold(context));

                       Textview_dialog_Amount.setText(holder.Packages_TextView_Amount.getText());

                       TextView text1 = (TextView) dialog.findViewById(R.id.textview_course_name);

                       *//* webView.loadData(splashModels.get(position).getDescription(),"text/html","uft-8");*//*
                       if (lang.equalsIgnoreCase("mr")) {
                           Log.e("marathi "," "+arrayList.get(i).getDescription_InMarathi());
                           dialog_discp.loadData(style+arrayList.get(i).getDescription_InMarathi().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
                           //textView.setText("\" " + splashModels.get(position).getTitle_Marathi() + " \"");
                       } else {
                           dialog_discp.loadData(style+arrayList.get(i).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
                           // textView.setText("\" " + splashModels.get(position).getTitle() + " \"");
                       }

                       text1.setText(holder.Packages_TextView_Course_Name.getText());

                       ImageView dialog_close = (ImageView) dialog.findViewById(R.id.dialog_close_button);

                       dialog_close.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               dialog.dismiss();
                           }
                       });

                       dialog.show();
                   }catch (Exception e)
                   {
                       e.printStackTrace();
                   }*/
                }
            });


        } catch (Exception e) {
            ((Activitycommon) context).reporterror("GetMyPackagesAdapter", e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public List<MyPackages> getAmount() {
        return arrayList;
    }

    @Override
    public Filter getFilter() {
        return datasetFilterFull;
    }

    private Filter datasetFilterFull = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MyPackages> filteredList = new ArrayList<>();


            try {
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(datasetFilter);

                    //Log.e("called ","calle "+filteredList.size());

                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (MyPackages item : datasetFilter) {
                        Log.e("check ", "" + constraint + " " + item.getName().toLowerCase());
                        if ((item.getName().toLowerCase().trim().contains(filterPattern)) || (item.getCategoryName().toLowerCase().split("").toString().contains(filterPattern))) {// || (item.getMarks().toLowerCase().contains(filterPattern))
                            filteredList.add(item);
                        }
                    }
                    Log.e("called1 ", "called1 " + filteredList.size() + " " + datasetFilter.size());
                }
            } catch (Exception e) {
                ((Activitycommon) context).reporterror("GetMyPackagesAdapter", e.toString());
                e.printStackTrace();
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            try {
                arrayList.clear();
                arrayList.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            } catch (Exception e) {
                ((Activitycommon) context).reporterror("GetMyPackagesAdapter", e.toString());
                e.printStackTrace();
            }
        }
    };
}
