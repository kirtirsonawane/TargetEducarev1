package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.Student;
import com.targeteducare.Constants;
import com.targeteducare.DateUtils;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.HolderNotification> {
    ArrayList<String> list;
    Context mContext;
    DatabaseReference databaseReference, update;
    String key = "";

    public class HolderNotification extends RecyclerView.ViewHolder{
        public TextView txt;
        public TextView txt1;
        public TextView time;
        public ImageView img;
        public ImageView deleteimg;
        public ImageView syncimg;
        public CardView cardview_notification;

        public HolderNotification(Context context,View arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub
            txt1=(TextView)arg0.findViewById(R.id.textView3);
            txt=(TextView)arg0.findViewById(R.id.textView1);
            time=(TextView)arg0.findViewById(R.id.textView2);
            img=(ImageView)arg0.findViewById(R.id.imageview_1);
            syncimg=(ImageView)arg0.findViewById(R.id.imageview_2);
            deleteimg=(ImageView)arg0.findViewById(R.id.imageview_3);
            txt.setTypeface(Fonter.getTypefaceregular(context));
            time.setTypeface(Fonter.getTypefaceregular(context));
            cardview_notification = (CardView) arg0.findViewById(R.id.cardview_notification);
        }

    }

    public AdapterNotification(Context mContext, ArrayList<String> list) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        Collections.reverse(list);
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public void onBindViewHolder(HolderNotification holderNotification, int pos) {
        // TODO Auto-generated method stub
        try {

            JSONObject obj = new JSONObject(list.get(pos));
            try {

                if(obj.has("header") && obj.has("desc")){
                    if (!obj.getString("header").equalsIgnoreCase(obj.getString("desc"))) {
                        holderNotification.txt1.setVisibility(View.VISIBLE);
                        holderNotification.txt1.setText(obj.optString("header"));
                    } else {
                        holderNotification.txt1.setVisibility(View.GONE);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(obj.has("desc")){
                holderNotification.txt.setText(obj.optString("desc"));
            }

            if(obj.has("time")){
                holderNotification.time.setText(obj.optString("time"));
            }

            try {
                if (obj.has("issync")) {
                    if (obj.getInt("issync") == 1) {
                        holderNotification.syncimg.setVisibility(View.VISIBLE);
                        holderNotification.syncimg.setTag(pos);
                        holderNotification.syncimg.setImageResource(R.drawable.ic_refresh);
                        holderNotification.syncimg.setOnClickListener(clk);
                    } else if (obj.getInt("issync") == 2) {
                        holderNotification.syncimg.setVisibility(View.GONE);
                        holderNotification.syncimg.setTag(pos);
                       // arg0.syncimg.setImageResource(R.drawable.ic_update);
                        holderNotification.syncimg.setOnClickListener(clkgotoplaystore);
                    } else
                        holderNotification.syncimg.setVisibility(View.GONE);
                } else {
                    holderNotification.syncimg.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                holderNotification.syncimg.setVisibility(View.GONE);
                e.printStackTrace();
            }
            holderNotification.deleteimg.setTag(pos);
            holderNotification.deleteimg.setOnClickListener(clkdelete);

            if (obj.has("imageurl")) {
                holderNotification.img.setVisibility(View.VISIBLE);
                String imgurl = obj.getString("imageurl");
                if (!imgurl.equalsIgnoreCase("")) {
                    Picasso.with(mContext)
                            .load(imgurl)
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(holderNotification.img);
                }else {
                    holderNotification.img.setVisibility(View.GONE);
                }
            } else {
                holderNotification.img.setVisibility(View.GONE);
            }

            /*if(obj.has("isread")){
                String isread = obj.getString("isread");

                if(isread.equalsIgnoreCase("1")){
                    arg0.cardview_notification.setVisibility(View.GONE);
                }else{
                    arg0.cardview_notification.setVisibility(View.VISIBLE);
                }
            }*/

            if(obj.has("key")){
                key = obj.optString("key");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //   ((ActivityCommon) mContext).reporterror("AdapterNotification", e.toString());

        }

        catch (Exception e) {
            e.printStackTrace();
            //   ((ActivityCommon) mContext).reporterror("AdapterNotification", e.toString());
        }

    }

    View.OnClickListener clk = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int tag = (int) v.getTag();
            JSONObject obj;
            /*try {
                obj = new JSONObject(list.get(tag));
                Log.e("click ", "click ");
                ((ActivityCommon) mContext).genloading("Updating Data");
                ConnectionManager.getInstance(mContext).getOfficeBearerinbackground1();
                //DatabaseHelper.getInstance(mContext).deletebyid("table_notification", obj.getLong("id"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ((ActivityCommon) mContext).reporterror("AdapterNotification", e.toString());
            } catch (Exception e) {
                ((ActivityCommon) mContext).reporterror("AdapterNotification", e.toString());
            }*/
            //list.remove(tag);
            //notifyDataSetChanged();
        }
    };
    View.OnClickListener clkgotoplaystore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
         /*   try {
                ((Activitycommon) mContext).gotoplaystore();
            } catch (Exception e) {
                Toast.makeText(mContext, "Unable to Connect Try Again...",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }*/
        }
    };
    View.OnClickListener clkdelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int tag = (int) v.getTag();
            JSONObject obj;
            try {
                obj = new JSONObject(list.get(tag));
                if(obj.has("isread")){
                    //update_chat(obj);
                }

                DatabaseHelper.getInstance(mContext).deletebyid("table_notification", obj.getLong("id"));
                list.remove(tag);

                Map<String, String> driverdetails = new HashMap<>();
                driverdetails.put("data", "test");
                driverdetails.put("date", "12");
                driverdetails.put("isread", "1");
                update = databaseReference.child(GlobalValues.student.getMobile()).child("chat").child(key);
                update.setValue(driverdetails);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            notifyDataSetChanged();
        }
    };

    @Override
    public HolderNotification onCreateViewHolder(ViewGroup arg0, int pos) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_notification, arg0, false);
        HolderNotification vh = new HolderNotification(mContext, v);
        return vh;
    }

    /*public  void update_chat(final JSONObject obj){
        try{
            databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
            databaseReference.child(GlobalValues.student.getMobile()).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        *//*if(dataSnapshot!=null){
                            dataSnapshot.getKey();
                            Log.e("key ", dataSnapshot.getKey());
                        }

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("isread", 1);
                        databaseReference.child(GlobalValues.student.getMobile()).child("chat").updateChildren(childUpdates);*//*


                        Map<String, Object> childUpdates = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            childUpdates.put(snapshot.getKey(),snapshot.getValue());
                        }
                        childUpdates.put("data", obj.optString("desc"));
                        childUpdates.put("date", obj.optString("time"));
                        childUpdates.put("isread", 1);

                        databaseReference.child(GlobalValues.student.getMobile()).child("chat").updateChildren(childUpdates);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

}