package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.targeteducare.ActivityStartChat;
import com.targeteducare.Classes.ChatQuestionsNAnswers;
import com.targeteducare.Classes.MessagesModel;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;
import java.util.ArrayList;

public class Adapter_Start_Chat extends RecyclerView.Adapter<Adapter_Start_Chat.Holder> {
    Context context;
    ArrayList<MessagesModel> list;
    ChatQuestionsNAnswers questionsNAnswers = new ChatQuestionsNAnswers();
    String token = "";
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;

    public Adapter_Start_Chat(ActivityStartChat activityStartChat, ArrayList<MessagesModel> list, String token) {
        this.context = activityStartChat;
        this.list = list;
        this.token = token;
//       Log.e("list in holder"," list "+list.get(0).getTokenId());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        //  Log.e("id  "," out of if "+list.get(i).getSenderId());
        if (i == LAYOUT_ONE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_start_chat, viewGroup, false);
            //GlobalValues.id=2;

            /* return new Adapter_Start_Chat.Holder( view);*/
        } else {
            //     Log.e("id  "," else "+list.get(i).getSenderId());
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_start_chat_2, viewGroup, false);
            //GlobalValues.id=1;

        }

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        ArrayList<ChatQuestionsNAnswers> answers = new ArrayList<>();
        //   ArrayList<MessagesModel>messagesModels=new ArrayList<>();

        try {
            if (list.get(i).getTokenId().equalsIgnoreCase(token)) {
                if (holder.getItemViewType() == LAYOUT_ONE) {
                    //if( list.get(0).getMessage().length()>0) {
                    //  for (int i1 = 0; i1 < list.get(0).getMessage().length(); i1++) {
                    ChatQuestionsNAnswers nAnswers = new ChatQuestionsNAnswers();
                    MessagesModel model = new MessagesModel();
                    if (list.get(i).getMessage() == null || list.get(i).getMessage().equalsIgnoreCase("")) {
                        Toast.makeText(context, "Possibly no chat.", Toast.LENGTH_SHORT);
                    } else {
                        holder.Showmsg.setText(list.get(i).getMessage());
                        model.setMessage(holder.Showmsg.getText().toString());

                        //  messagesModels.add(model);
                    }

                    //nAnswers.setToken(list.get(i).getToken());
                    //nAnswers.setMessagesModels(messagesModels);
                    //}



              /*  String token=nAnswers.getToken();
                list=new ArrayList<>();
                list.add(nAnswers);
                ((ActivityStartChat)context).update(list,token);*/
                    //}
                    holder.Showtime.setText(list.get(i).getTime());
                    //model.setSenderId(Holder.Showtime.getText().toString());
                    holder.Showtoken.setText("Sender Id :" + list.get(i).getSenderId());
                    //  model.setSenderId(Holder.Showtoken.getText().toString());

                } else {
                    if (list.get(i).getMessage().length() > 0) {
                        // for (int i1=0;i1<list.get(i).getMessage().length();i1++) {
                        holder.Showmsg.setText(list.get(i).getMessage());
                        //  model.setSenderId(Holder.Showtime.getText().toString());
                        // }
                    }
                    holder.Showtime.setText(list.get(i).getTime());
                    holder.Showtoken.setText("Sender Id :" + list.get(i).getSenderId());

                }


                if (list.get(i).getFileUrl() == null) {
//               Log.e("url3 ","null45jjj "+list.get(i).getFileUrl().toString());
                    holder.imageView.setVisibility(View.GONE);
                  //  holder.Showmsg.setVisibility(View.VISIBLE);
                } else if (list.get(i).getFileUrl().length() > 0) {


                   // holder.Showmsg.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(list.get(i).getFileUrl())
                            .placeholder(R.drawable.college)
                            .error(R.drawable.college)
                            //.networkPolicy(NetworkPolicy.NO_CACHE)
                            .skipMemoryCache()
                            .resize(100, 100)

                            //.fit()
                            //  .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(holder.imageView);


                } else {

                }

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ActivityStartChat) context).enlargeimage(i, list.get(i).getFileUrl());
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {


        if (list.get(position).getSenderId().equalsIgnoreCase(GlobalValues.student.getId()))
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView Showmsg;
        TextView Showtime;
        ImageView imageView;
        TextView Showtoken;


        public Holder(View view) {
            super(view);
            view.setTag(this);
            final int position = getAdapterPosition();
            Showmsg = (TextView) view.findViewById(R.id.chat_show_msg);
            Showtoken = (TextView) view.findViewById(R.id.chat_show_token);
            Showtime = (TextView) view.findViewById(R.id.chat_show_time);
            imageView = (ImageView) view.findViewById(R.id.chat_start_img);
            /*layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotonext(position);
                }
            });*/

        }

    }
}
