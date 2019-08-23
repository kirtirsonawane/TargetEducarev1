package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.Exam;
import com.targeteducare.DateUtils;
import com.targeteducare.ExamListActivity;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ExamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context mContext;
    ArrayList<Exam> datasetFilter;
    ArrayList<Exam> mdataset = new ArrayList<>();
    String lang = "";
    String type="";
    int[] colorsforbackground;
    int set_color = 0;

    public ExamAdapter(Context mContext, ArrayList<Exam> mdataset, String lang,String type) {
        try {
            this.mContext = mContext;
            this.mdataset.addAll(mdataset);
            this.datasetFilter = mdataset;
            this.lang = lang;
            this.type=type;
        } catch (Exception e) {
            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
            e.printStackTrace();
        }
        // colorsforbackground = mContext.getResources().getIntArray(R.array.colorsforbackground);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            colorsforbackground = mContext.getResources().getIntArray(R.array.colorsforbackground);
        } catch (Exception e) {
            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
            e.printStackTrace();
        }

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == 1) {
            View v = inflater.inflate(R.layout.headerxml, parent, false);
            return new viewHolderHeader(v);
        } else {
            View v = inflater.inflate(R.layout.list_item_exam, parent, false);
            return new viewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mdataset.get(position).isIsheader())
            return 1;
        else return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder vholder, final int position) {
        try {
            if (getItemViewType(position) == 1) {
                viewHolderHeader header = (viewHolderHeader) vholder;
                if (lang.equalsIgnoreCase("mr"))
                    header.txt.setText(mdataset.get(position).getCoursename_inmarathi());
                else
                    header.txt.setText(mdataset.get(position).getCoursename());
            } else {
                viewHolder holder = (viewHolder) vholder;
                //holder.txt1.setTypeface(Fonter.getTypefaceregular(mContext));
                holder.txt2.setTypeface(Fonter.getTypefaceregular(mContext));
                if (lang.equalsIgnoreCase("mr"))
                    holder.txt1.setText(mdataset.get(position).getName_InMarathi());
                else
                    holder.txt1.setText(mdataset.get(position).getExamname());

                holder.txt3.setText(mdataset.get(position).getMarks() + " " + mContext.getResources().getString(R.string.exam_marks) + "\n" + mdataset.get(position).getDurationinMin() + " " + mContext.getResources().getString(R.string.exam_minutes));
                holder.txt2.setText(mdataset.get(position).getDurationinMin() + " " + mContext.getResources().getString(R.string.exam_minutes) + "\t  " + mdataset.get(position).getMarks() + " " + mContext.getResources().getString(R.string.exam_marks));
            /*holder.txt3.setText(mdataset.get(position).getStartdate() + " " + DateUtils.parseDate(mdataset.get(position).getExamstarttime(), "HH:mm:ss", "hh:mm a") + "\t " +
                    mContext.getResources().getString(R.string.exam_to) + "\t " + mdataset.get(position).getEnddate() + " " + DateUtils.parseDate(mdataset.get(position).getExamendtime(), "HH:mm:ss", "hh:mm a"));*/

                holder.mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ((ExamListActivity) mContext).maildata(mdataset.get(position));
                        } catch (Exception e) {
                            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                holder.tv_attempt_exam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ((Activitycommon) mContext).gotoexamActivity(mdataset.get(position),type);
                        } catch (Exception e) {
                            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                holder.buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ((Activitycommon) mContext).gotobuypackages();
                        } catch (Exception e) {
                            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                holder.answersheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ((Activitycommon) mContext).gotoanswersheet(mdataset.get(position), 0);

                        } catch (Exception e) {
                            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                holder.sync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ((ExamListActivity) mContext).syncdata(mdataset.get(position));
                        } catch (Exception e) {
                            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                holder.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ((ExamListActivity) mContext).downloaddata(mdataset.get(position));
                        } catch (Exception e) {
                            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                holder.tv_checkprogress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //   if (mdataset.get(position).getProgress() > 0) {
                            ((Activitycommon) mContext).gotoviewmarksheet("http://" + GlobalValues.IP + "/Home/StudentPerformanceReport?" + mdataset.get(position).getExamid() + "_" + GlobalValues.student.getId());
                          /*  } else {
                                Log.e("Nothing ", "to do");
                                //do nothing for now
                            }*/
                        } catch (Exception e) {
                            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                holder.tv_attempt_exam.setText(mContext.getResources().getString(R.string.take_test));
              /*  if (mdataset.get(position).getIspaid() == 0) {
                    holder.buy.setVisibility(View.GONE);
                    holder.attempt.setVisibility(View.GONE);
                    holder.tv_attempt_exam.setVisibility(View.VISIBLE);
                    holder.answersheet.setVisibility(View.GONE);
                    holder.lprogbar.setVisibility(View.GONE);
                    holder.tv_checkprogress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                } else*/ if ((mdataset.get(position).getExamstatus().equalsIgnoreCase("Missed"))&& mdataset.get(position).getProgress()<=0) {
                    holder.buy.setVisibility(View.GONE);
                    holder.attempt.setVisibility(View.GONE);
                    holder.tv_attempt_exam.setVisibility(View.VISIBLE);
                    holder.answersheet.setVisibility(View.GONE);
                    holder.lprogbar.setVisibility(View.GONE);
                    holder.tv_checkprogress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                } else if (mdataset.get(position).getExamstatus().equalsIgnoreCase("Attempted") || (((int) Math.round(mdataset.get(position).getProgress()) > 0))) {
                    holder.buy.setVisibility(View.GONE);
                    holder.attempt.setVisibility(View.GONE);
                    holder.tv_attempt_exam.setVisibility(View.GONE);
                    holder.lprogbar.setVisibility(View.VISIBLE);
                    /*holder.tv_checkprogress.setVisibility(View.VISIBLE);*/
                    try {
                        if(mdataset.get(position).getResultdate().equalsIgnoreCase("null") || mdataset.get(position).getResultdate().length()==0)
                        {
                            holder.tv_checkprogress.setVisibility(View.GONE);
                        }else {
                            Calendar cal1 = Calendar.getInstance();
                            Date date2 = DateUtils.parseDate(mdataset.get(position).getResultdate(), "dd MMM yyyy");
                            cal1.setTime(date2);
                            if (Calendar.getInstance().before(cal1)) {
                                holder.tv_checkprogress.setVisibility(View.GONE);
                            } else {
                                holder.tv_checkprogress.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.answersheet.setVisibility(View.VISIBLE);
                    holder.progressBar.setProgress((int) Math.round(mdataset.get(position).getProgress()));
                    int progress_percent = (int) Math.round(mdataset.get(position).getProgress());
                    holder.tv_percentcovered.setText(progress_percent + " % " + mContext.getResources().getString(R.string.covered));
                } else if ((int) Math.round(mdataset.get(position).getProgress()) == 0) {
                    holder.buy.setVisibility(View.GONE);
                    holder.attempt.setVisibility(View.GONE);
                    holder.tv_attempt_exam.setText(mContext.getResources().getString(R.string.take_test));
                    holder.tv_attempt_exam.setVisibility(View.VISIBLE);
                    holder.answersheet.setVisibility(View.GONE);
                    holder.lprogbar.setVisibility(View.GONE);
                    holder.tv_checkprogress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                }/* else {
                    holder.attempt.setText(mContext.getResources().getString(R.string.attempt));
                    holder.lprogbar.setVisibility(View.GONE);
                    holder.tv_checkprogress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                }*/



            /*if (mdataset.get(position).isIsexamgiven()) {
                holder.answersheet.setVisibility(View.VISIBLE);
                if (((int) Math.round(mdataset.get(position).getProgress()) > 0) && ((int) Math.round(mdataset.get(position).getProgress()) <= 100)) {
                    //holder.attempt.setText("Retry");
                    holder.attempt.setVisibility(View.GONE);
                    holder.lprogbar.setVisibility(View.VISIBLE);
                    holder.tv_checkprogress.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.progressBar.setProgress((int) Math.round(mdataset.get(position).getProgress()));
                    int progress_percent = (int) Math.round(mdataset.get(position).getProgress());
                    holder.tv_percentcovered.setText(progress_percent + " % " + mContext.getResources().getString(R.string.covered));
                } else if ((int) Math.round(mdataset.get(position).getProgress()) == 0) {
                    holder.attempt.setVisibility(View.GONE);
                    holder.answersheet.setVisibility(View.VISIBLE);
                    holder.lprogbar.setVisibility(View.GONE);
                    holder.tv_checkprogress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                } else {
                    holder.attempt.setText(mContext.getResources().getString(R.string.attempt));
                    holder.lprogbar.setVisibility(View.GONE);
                    holder.tv_checkprogress.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                }

            } else {
                holder.answersheet.setVisibility(View.GONE);
                holder.lprogbar.setVisibility(View.GONE);
                holder.tv_checkprogress.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
            }*/
                if (mdataset.get(position).isIssync()) {
                    holder.sync.setVisibility(View.VISIBLE);
                    holder.mail.setVisibility(View.VISIBLE);
                } else {
                    holder.sync.setVisibility(View.GONE);
                    holder.mail.setVisibility(View.GONE);
                }

                holder.imgview.setVisibility(View.GONE);
                holder.tv_chapterno.setVisibility(View.VISIBLE);
                holder.txt2.setVisibility(View.GONE);

                //Log.e("date is ",mdataset.get(position).getStartdate()+"");
                //holder.tv_chapterno.setText(DateUtils.parseDate(mdataset.get(position).getStartdate(), "dd MMMM yyyy", "dd MMM") + "");
                holder.tv_chapterno.setText(mdataset.get(position).getDayMonthNumber() + "");
                /*set_color = position;
                if (set_color < colorsforbackground.length) {
                    holder.linearlayout_color.setBackgroundColor(colorsforbackground[set_color]);
                } else {
                    set_color = 0;
                    holder.linearlayout_color.setBackgroundColor(mContext.getResources().getColor(R.color.default_color));
                }

                set_color++;*/

                if (position < colorsforbackground.length) {
                    holder.linearlayout_color.setBackgroundColor(colorsforbackground[position]);
                } else {
                    holder.linearlayout_color.setBackgroundColor(colorsforbackground[new Random().nextInt(colorsforbackground.length)]);
                }
            }
        } catch (Exception e) {
            Log.e("error ", "error " + e.toString());
            ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mdataset.size();
    }

    @Override
    public Filter getFilter() {
        return datasetFilterFull;
    }

    private Filter datasetFilterFull = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Exam> filteredList = new ArrayList<>();

            try {
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(datasetFilter);

                    //Log.e("called ","calle "+filteredList.size());

                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Exam item : datasetFilter) {
                        Log.e("check ", "" + constraint + " " + item.getExamname().toLowerCase());
                        if ((item.getExamname().toLowerCase().trim().contains(filterPattern))) {// || (item.getMarks().toLowerCase().contains(filterPattern))
                            filteredList.add(item);
                        }
                    }
                    Log.e("called1 ", "called1 " + filteredList.size() + " " + datasetFilter.size());
                }

            } catch (Exception e) {
                ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                e.printStackTrace();
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            try {
                mdataset.clear();
                mdataset.addAll((ArrayList) results.values);
                notifyDataSetChanged();

            } catch (Exception e) {
                ((Activitycommon) mContext).reporterror("ExamAdapter", e.toString());
                e.printStackTrace();
            }
        }
    };


    public class viewHolderHeader extends RecyclerView.ViewHolder {
        TextView txt;

        public viewHolderHeader(@NonNull View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.textview_1);
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txt1, txt2, download, sync, answersheet, txt3, attempt, buy, tv_percentcovered;
        ImageView mail;
        TextView tv_chapterno, tv_attempt_exam;
        ProgressBar progressBar;
        TextView tv_checkprogress;
        LinearLayout lprogbar;
        LinearLayout linearlayout_color;
        ImageView imgview;

        public viewHolder(View itemView) {
            super(itemView);
            txt1 = (TextView) itemView.findViewById(R.id.textview_1);
            txt2 = (TextView) itemView.findViewById(R.id.textview_2);
            txt3 = (TextView) itemView.findViewById(R.id.textview_3);
            download = (TextView) itemView.findViewById(R.id.download);
            attempt = (TextView) itemView.findViewById(R.id.attempt);
            buy = (TextView) itemView.findViewById(R.id.pay);

            sync = (TextView) itemView.findViewById(R.id.sync);
            answersheet = (TextView) itemView.findViewById(R.id.viewanswersheet);
            mail = (ImageView) itemView.findViewById(R.id.imageview_mail);

            tv_percentcovered = itemView.findViewById(R.id.tv_coveredpercentage);
            tv_checkprogress = itemView.findViewById(R.id.tv_checkprogress);
            progressBar = itemView.findViewById(R.id.progress_topiccovered);
            lprogbar = itemView.findViewById(R.id.linearlayoutprogresbar);

            imgview = itemView.findViewById(R.id.imageview_1);
            tv_chapterno = itemView.findViewById(R.id.chapternumber);
            linearlayout_color = itemView.findViewById(R.id.linearlayout_color);

            tv_attempt_exam = itemView.findViewById(R.id.text_view_attempt);

        }
    }
}
