package com.targeteducare.Adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.targeteducare.Classes.MyPackages;
import com.targeteducare.Constants;
import com.targeteducare.Fonter;
import com.targeteducare.MyPackagesActivity;
import com.targeteducare.R;

import java.util.ArrayList;
import java.util.List;

/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/

public class GetMyPackagesAdapter extends RecyclerView.Adapter<GetMyPackagesAdapter.Holder> {
    ArrayList<MyPackages> arrayList;
    MyPackages myPackages = new MyPackages();
    Context context;
    public ItemClickListener itemClickListener;

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
            Packages_TextView_ViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //  String discription_data=arrayList.get(position).getDescription();


                    // ((MyPackagesActivity)context).gotocallfragment(discription_data);


                    getdiscriptiontext(position);


                }
            });

        }


        private void getdiscriptiontext(int position) {

            final Dialog dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_layout);

            WebView dialog_discp = (WebView) dialog.findViewById(R.id.webview_dialog_discription);
            TextView dialog_heading = (TextView) dialog.findViewById((R.id.textview_dialog_heading));
            dialog_discp.getSettings().setJavaScriptEnabled(true);
            dialog_heading.setText("Details : ");
            Log.e("data ", "data " + arrayList.get(position).getDescription());

            dialog_discp.loadData(arrayList.get(position).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");

            Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));


            TextView Textview_dialog_Amount = (TextView) dialog.findViewById(R.id.textview_dialog_amount);

            Packages_TextView_Amount.setTypeface(Fonter.getTypefacebold(context));
            Textview_dialog_Amount.setText(Packages_TextView_Amount.getText());


            TextView text1 = (TextView) dialog.findViewById(R.id.textview_course_name);

            //  text.setText(Packages_TextView_Discription.getText());


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

    }


    public GetMyPackagesAdapter(Context context, ArrayList<MyPackages> qList) {
        this.arrayList = qList;
        this.context = context;
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


        Log.e("size of array ", String.valueOf(arrayList.size()));

        for (int j = 0; j < arrayList.size(); j++) {
            String urls = Constants.imagepath + arrayList.get(i).getImageurl();
            Log.e("url", i + " " + urls);

            //  String urls=URLS.getmy_package_image();
            //Log.e("URLS ::: ;;; ",arrayList.get(i).getImageurl());
            Picasso.with(context)
                    .load(urls)
                    .placeholder(R.drawable.ic_exam)
                    .error(R.drawable.ic_launcher)
                    .into(holder.Packages_ImageView);
            //Picasso.Builder builder = new Picasso.Builder(context);
            /*builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();

                }*/

        }
        ;
        /*    Glide.with(context)
                    .load(urls)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_exam)
                    .placeholder(R.drawable.ic_launcher)
                    .into(holder.Packages_ImageView);*/


        holder.Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));
        holder.Packages_TextView_Course_Name.setText(Html.fromHtml(arrayList.get(i).getName()));
         /*  holder.Packages_TextView_Discription.setText(Html.fromHtml( arrayList.get(i).getDescription()));
            Log.e("Discription::holder@ ::",arrayList.get(i).getDescription());*/
         holder.Packages_TextView_Course_Name.setTypeface(Fonter.getTypefacebold(context));
        holder.Packages_TextView_Amount.setText("₹ " + arrayList.get(i).getAmount());

        holder.Packages_CheckBox.setTypeface(Fonter.getTypefaceregular(context));
                holder.Packages_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.e("is Checked", String.valueOf((isChecked)));


                if (isChecked == true) {

                    Log.e("is Checked in if", String.valueOf((isChecked)));
                    holder.Packages_CheckBox.setText("Remove");
                    ((MyPackagesActivity) context).gotogetamount(arrayList.get(i).getAmount(), 1);

                    Log.e("value of amount :: ::", getAmount().toString());
                    Log.e("checked ", String.valueOf(arrayList.get(i).getAmount()));
                } else if (isChecked == false) {
                    holder.Packages_CheckBox.setText("Add");
                    ((MyPackagesActivity) context).gotogetamount(arrayList.get(i).getAmount(), 0);
                }


            }
        });


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public List<MyPackages> getAmount() {
        return arrayList;
    }

}
