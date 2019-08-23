package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.targeteducare.ActivityChatFaq;
import com.targeteducare.Classes.ChatQuestionsNAnswers;
import com.targeteducare.R;

import java.util.ArrayList;

public class AdapterChatFaq extends RecyclerView.Adapter<AdapterChatFaq.Holder> implements Filterable {
    Context context;
    ArrayList<ChatQuestionsNAnswers> list1=new ArrayList<>();

    ArrayList<ChatQuestionsNAnswers> questionsNAnswersArrayList;

    public AdapterChatFaq(Context context, ArrayList<ChatQuestionsNAnswers> list) {
        this.context = context;
        this.list1.addAll(list);
        this.questionsNAnswersArrayList=list;


    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_chat_faq, viewGroup, false);
        return new Holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {

        try {


            holder.Showtoken.setText("Token ID : " + list1.get(i).getToken());
            //   Log.e("Holder token ","view string : "+Holder.Showtoken.getText().toString());
            holder.Showtime.setText("Dated : " + list1.get(i).getDate());
//       if( list.get(i).getMessagesModels().size()>0) {
            //     if (list.get(i).getMessagesModels()==null||list.get(i).getMessagesModels().equals("")){
            //  Holder.Showmsg.setText("");
            //   }
            //  else//{
            int count=0;
           /* for (int j=0;j<list1.get(i).getMessagesModels().size();j++){
                    count++;
            }*/

            holder.Showchat.setText("Replys : "+list1.get(i).getCount());
            holder.Showstatus.setText("Status : " + list1.get(i).getStatus());
            holder.Showmsg.setText("Title : " + list1.get(i).getTitle());

            holder.Showupdateddate.setText("Update Date : "+list1.get(i).getUpdatedDate());

            //  }
            if (list1.get(i).getStatus().equalsIgnoreCase("Resolved")) {
                holder.layout.setBackgroundResource(R.drawable.border_for_list_green);
                holder.Showstatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_green1, 0);
                holder.Showstatus.setCompoundDrawablePadding(5);
            }
            else if (list1.get(i).getStatus().equalsIgnoreCase("Pending")) {
                holder.layout.setBackgroundResource(R.drawable.border_for_list_yellow);
                holder.Showstatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_yellow1, 0);
                holder.Showstatus.setCompoundDrawablePadding(5);
            }
             else if (list1.get(i).getStatus().equalsIgnoreCase("Cancelled")) {
                holder.layout.setBackgroundResource(R.drawable.border_for_list_red);
                holder.Showstatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_red1, 0);
                holder.Showstatus.setCompoundDrawablePadding(5);
            }
//                Log.e("url ","length "+list1.get(i).getFileUrl().length());
             if (list1.get(i).getFileUrl().length()>0){

                 holder.imageView.setVisibility(View.VISIBLE);
                // holder.Showmsg.setVisibility(View.INVISIBLE);
                 Picasso.with(context)
                         .load(list1.get(i).getFileUrl())
                         .placeholder(R.drawable.college)
                         .error(R.drawable.college)
                         /*.networkPolicy(NetworkPolicy.NO_CACHE)
                         .skipMemoryCache()*/
                         .resize(100, 100)

                         //.fit()
                         /*.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)*/
                         .into(holder.imageView);
             }
             else/* if (list1.get(i).getFileUrl()==null)*/{

                 holder.imageView.setVisibility(View.GONE);
               //  holder.Showmsg.setVisibility(View.VISIBLE);
             }


            //     }

        /*Holder.Showtoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityChatFaq) context).gotonext(i);

            }
        });*/

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityChatFaq) context).enlargeimage(i,list1.get(i).getFileUrl());
                }
            });
            holder.linearclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityChatFaq) context).gotonext(i,list1);
                }
            });
           /* holder.Showmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityChatFaq) context).gotonext(i,list1);
                }
            });*/

       /* Holder.Showtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityChatFaq) context).gotonext(i);
            }
        });*/
       /* Holder.Showmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityChatFaq) context).gotonext(i);
            }
        });*/
       /* Holder.Showtoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityChatFaq) context).gotonext(i);

            }
        });
*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        // Log.e("list ", "count " + list.size());
        return list1.size();
    }

    @Override
    public Filter getFilter() {
        return datasetFilterFull;
    }


    protected Filter datasetFilterFull = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ChatQuestionsNAnswers> filteredlist1 = new ArrayList<>();
            try {


                String Adata = "";

                String s="";
                Adata = ((ActivityChatFaq) context).sdata;
               // String[] parts = Adata.split("");

//                Log.e("cnstraint "," "+constraint.length() +" "+constraint.toString());
               // Log.e("parts "," "+parts.length +" "+parts.toString());



                try{
                if (Adata.contains("Pending")||Adata.contains("Resolved")||Adata.contains("Cancelled")){


                }
                else {
                    Adata=s;


                }}catch (Exception e){
                    e.printStackTrace();
                }





                if (Adata.length()<=6){

                    Adata=null;

                    /*Log.e("Adata "," " +" "+Adata.toString()+2);*/
                }
//                Log.e("adata ", "empty " + Adata.isEmpty());
                if (/*((Adata.length() == 0 )*//*||( Adata.isEmpty())*/Adata==null/*||constraint == null || constraint.length() == 0*/){
                    /*(constraint == null || constraint.length() == 0)&&(sdata==null||sdata.length()==0){*/
                  //  Log.e("cnstraint "," "+constraint.length() +" "+constraint.toString());

                    filteredlist1.addAll(questionsNAnswersArrayList);



                } else {
                    ArrayList<ChatQuestionsNAnswers> filteredlist = new ArrayList<>();

                    String[] sdata = Adata.split(" ");


                    for (int i1 = 1; i1 <sdata.length; i1++) {
                        int j;
                        for (j = 0; j < i1; ++j) {
                            if ((sdata[i1].equalsIgnoreCase(sdata[j]))) {
                                sdata[i1] = " ";
                            }
                        }

                    }



                    for (int i = 0; i < sdata.length; i++) {

                        for (ChatQuestionsNAnswers answers : questionsNAnswersArrayList) {

                            if ((answers.getStatus()).toLowerCase().contains(sdata[i].toLowerCase())) {

                                if ((answers.getStatus().toLowerCase()).equalsIgnoreCase(sdata[i].toLowerCase())) {

                                    filteredlist.add(answers);

                                }
                            }


                        }

                       // filteredlist1.addAll(filteredlist);


                    }
                   filteredlist1.addAll(filteredlist);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }




            FilterResults results = new FilterResults();
            results.values =filteredlist1 ;


//            notifyDataSetChanged();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                notifyDataSetChanged();
                list1.clear();

                list1=((ArrayList<ChatQuestionsNAnswers>) results.values);

                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }



        }
    };












    public class Holder extends RecyclerView.ViewHolder {
        TextView Showmsg;
        TextView Showtime;
        TextView Showtoken;
        TextView Showstatus;
        LinearLayout layout;
        LinearLayout linearclick;
        TextView Showchat;
        TextView Showupdateddate;
        ImageView imageView;

        public Holder(View view) {
            super(view);
            view.setTag(this);
            Showmsg = (TextView) view.findViewById(R.id.chat_show_msg);
            linearclick = (LinearLayout) view.findViewById(R.id.linearclick);
            Showtoken = (TextView) view.findViewById(R.id.chat_show_token1);
            Showstatus = (TextView) view.findViewById(R.id.chat_show_status);
            Showtime = (TextView) view.findViewById(R.id.chat_show_date);
            /*Showtime = (TextView) view.findViewById(R.id.chat_show_time);*/
            layout = (LinearLayout) view.findViewById(R.id.linear_layout_chat_status);
            Showchat=(TextView)view.findViewById(R.id.chat_show_chats);
            Showupdateddate=(TextView)view.findViewById(R.id.chat_show_updateddate);
            imageView=(ImageView)view.findViewById(R.id.chat_img);

        }


    }


}


