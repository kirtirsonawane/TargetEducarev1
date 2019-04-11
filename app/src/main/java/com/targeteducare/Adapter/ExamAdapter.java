package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.Exam;
import com.targeteducare.DateUtils;
import com.targeteducare.ExamListActivity;
import com.targeteducare.Fonter;
import com.targeteducare.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.viewHolder> {
    Context mContext;
    ArrayList<Exam> mdataset;

    public ExamAdapter(Context mContext, ArrayList<Exam> mdataset) {
        this.mContext = mContext;
        this.mdataset = mdataset;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_item_exam, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        holder.txt1.setTypeface(Fonter.getTypefaceregular(mContext));
        holder.txt2.setTypeface(Fonter.getTypefaceregular(mContext));
        holder.txt1.setText(mdataset.get(position).getExamname());
        holder.txt2.setText(mdataset.get(position).getDurationinMin() + " Mins" + " " + mdataset.get(position).getMarks() + " Marks");
        holder.txt3.setText(mdataset.get(position).getStartdate() + " " + DateUtils.parseDate(mdataset.get(position).getExamstarttime(), "HH:mm:ss", "hh:mm a") + " To " + mdataset.get(position).getEnddate() + " " + DateUtils.parseDate(mdataset.get(position).getExamendtime(), "HH:mm:ss", "hh:mm a"));

        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExamListActivity)mContext).maildata(position);
            }
        });

        holder.attempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ((Activitycommon) mContext).gotoexamActivity(mdataset.get(position));
            }
        });
        holder.answersheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activitycommon) mContext).gotoanswersheet( mdataset.get(position));
            }
        });
        holder.sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExamListActivity) mContext).syncdata(position);
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ((ExamListActivity) mContext).downloaddata(position);
            }
        });

        /*if(mdataset.get(position).isIsqdownloaded()) {
            holder.attempt.setVisibility(View.VISIBLE);
            holder.download.setVisibility(View.GONE);
        }else {
            holder.attempt.setVisibility(View.GONE);
            holder.download.setVisibility(View.VISIBLE);
        }*/

        if(mdataset.get(position).getIsshowexam()==0 && !mdataset.get(position).isIsexamgiven() )
        {
            try {
                Calendar cal1 = Calendar.getInstance();
                Date date2 = DateUtils.parseDate(mdataset.get(position).getEnddate(), "dd MMM yyyy");
                Date date3 = DateUtils.parseDate(mdataset.get(position).getExamendtime(), "HH:mm:ss");
                date2.setHours(date3.getHours());
                date2.setMinutes(date3.getMinutes());
                cal1.setTime(date2);

                Calendar cal = Calendar.getInstance();
                Date date = DateUtils.parseDate(mdataset.get(position).getStartdate(), "dd MMM yyyy");
                Date date1 = DateUtils.parseDate(mdataset.get(position).getExamstarttime(), "HH:mm:ss");
                date.setHours(date1.getHours());
                date.setMinutes(date1.getMinutes());
                cal.setTime(date);

                if (cal.before(Calendar.getInstance()) && cal1.after(Calendar.getInstance())) {
                    if(mdataset.get(position).isIsqdownloaded()) {
                        holder.attempt.setVisibility(View.VISIBLE);
                        holder.download.setVisibility(View.GONE);
                    }else {
                        holder.attempt.setVisibility(View.GONE);
                        holder.download.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.attempt.setVisibility(View.GONE);
                    holder.download.setVisibility(View.GONE);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }else {
            holder.attempt.setVisibility(View.GONE);
            holder.download.setVisibility(View.GONE);
        }
        if(mdataset.get(position).isIsexamgiven())
        {
            holder.answersheet.setVisibility(View.VISIBLE);
        }else {
            holder.answersheet.setVisibility(View.GONE);
        }
        if(mdataset.get(position).isIssync())
        {
            holder.sync.setVisibility(View.VISIBLE);
            holder.mail.setVisibility(View.VISIBLE);
        }else {
            holder.sync.setVisibility(View.GONE);
            holder.mail.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mdataset.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txt1, txt2, download, sync,answersheet, txt3,attempt;
        ImageView mail;
        public viewHolder(View itemView) {
            super(itemView);
            txt1 = (TextView) itemView.findViewById(R.id.textview_1);
            txt2 = (TextView) itemView.findViewById(R.id.textview_2);
            txt3 = (TextView) itemView.findViewById(R.id.textview_3);
            download = (TextView) itemView.findViewById(R.id.download);
            attempt = (TextView) itemView.findViewById(R.id.attempt);
            sync = (TextView) itemView.findViewById(R.id.sync);
            answersheet = (TextView) itemView.findViewById(R.id.viewanswersheet);
            mail=(ImageView)itemView.findViewById(R.id.imageview_mail);
        }
    }
}
