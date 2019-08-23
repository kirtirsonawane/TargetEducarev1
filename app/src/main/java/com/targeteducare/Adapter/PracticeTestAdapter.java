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
import com.targeteducare.PracticeTestActivity;
import com.targeteducare.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PracticeTestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, Serializable {
    Context context;
    ArrayList<Exam> datasetFilter;
    ArrayList<Exam> practiceTestModels = new ArrayList<>();
    String lang = "";
    int[] colorsforbackground;
    int set_color = 0;

    public PracticeTestAdapter(Context context, ArrayList<Exam> practiceTestModelsdata, String lang) {
        try {
            this.practiceTestModels.addAll(practiceTestModelsdata);
            //this.practiceTestModels.addAll(practiceTestModelsdata);
            this.context = context;
            this.datasetFilter = practiceTestModelsdata;
            this.lang = lang;
            //colorsforbackground = context.getResources().getIntArray(R.array.colorsforbackground);
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        try {
            colorsforbackground = context.getResources().getIntArray(R.array.colorsforbackground);
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
            e.printStackTrace();
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (i == 1) {
            View v = inflater.inflate(R.layout.headerxml, viewGroup, false);
            return new PracticeTestAdapter.viewHolderHeader(v);
        } else {
            View v = inflater.inflate(R.layout.list_item_exam, viewGroup, false);
            return new PracticeTestAdapter.MyViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (practiceTestModels.get(position).isIsheader())
            return 1;
        else return 0;
    }

    public class viewHolderHeader extends RecyclerView.ViewHolder {
        TextView txt;

        public viewHolderHeader(@NonNull View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.textview_1);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        try {
            if (getItemViewType(i) == 1) {
                viewHolderHeader header = (viewHolderHeader) holder;
                if (lang.equalsIgnoreCase("mr"))
                    header.txt.setText(practiceTestModels.get(i).getCoursename_inmarathi());
                else
                    header.txt.setText(practiceTestModels.get(i).getCoursename());
            } else {
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.tv_chapterno.setVisibility(View.VISIBLE);
          /*  int chap_no = i;
            if (chap_no >= 0 && chap_no < 10) {
                myViewHolder.tv_chapterno.setText("0" + (chap_no + 1));
            } else {
                myViewHolder.tv_chapterno.setText("" + (chap_no++));
            }*/
           /* int chap_no = i + 1;
            set_color++;
            if (chap_no >= 0 && chap_no < 10) {
                myViewHolder.tv_chapterno.setText("0" + chap_no);
                myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[chap_no]);
            } else {
                myViewHolder.tv_chapterno.setText("" + chap_no);
                if (set_color < colorsforbackground.length) {
                    myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[set_color]);
                } else {
                    set_color = 0;
                    myViewHolder.linearlayout_color.setBackgroundColor(context.getResources().getColor(R.color.default_color));
                }
            }*/
                myViewHolder.tv_chapterno.setVisibility(View.VISIBLE);
                myViewHolder.tv_chapterno.setText(DateUtils.parseDate(practiceTestModels.get(i).getStartdate(), "dd MMMM yyyy", "dd MMM") + "");

                try {
                    if(i<colorsforbackground.length){
                        myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[i]);
                    }else{
                        myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[new Random().nextInt(colorsforbackground.length)]);
                    }

                    /*set_color = i;
                    if (set_color < colorsforbackground.length) {
                        myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[set_color]);
                    } else {
                        set_color = 0;
                        myViewHolder.linearlayout_color.setBackgroundColor(context.getResources().getColor(R.color.default_color));
                    }
                    set_color++;*/
                } catch (Exception e) {
                    ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                    e.printStackTrace();
                }

                myViewHolder.tv_topicname.setVisibility(View.VISIBLE);
                myViewHolder.tv_total_questions.setVisibility(View.VISIBLE);
                myViewHolder.tv_totalvideo.setVisibility(View.VISIBLE);
                myViewHolder.tv_totalconcepts.setVisibility(View.VISIBLE);
                myViewHolder.timemarks1.setVisibility(View.VISIBLE);
                myViewHolder.imgview.setVisibility(View.GONE);
                myViewHolder.time2.setVisibility(View.GONE);
                //myViewHolder.tv_chapterno.setText(practiceTestModels.get(i).getTopic_no());
                myViewHolder.timemarks1.setText("Total marks for this exam");
                myViewHolder.timemarks1.setVisibility(View.GONE);
                if (lang.equalsIgnoreCase("mr"))
                    myViewHolder.tv_topicname.setText(practiceTestModels.get(i).getName_InMarathi());
                else
                    myViewHolder.tv_topicname.setText(practiceTestModels.get(i).getExamname());

              //  Log.e("name", "name " + practiceTestModels.get(i).getExamname());
                myViewHolder.tv_total_questions.setText(practiceTestModels.get(i).getTotal_questions() + " questions");
                myViewHolder.tv_totalvideo.setText(practiceTestModels.get(i).getTotal_videos() + " videos");
                myViewHolder.tv_totalconcepts.setText(practiceTestModels.get(i).getTotal_concepts() + " concepts");
                myViewHolder.tv_checkprogress.setText(context.getResources().getString(R.string.check_progress));
                //myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                //myViewHolder.progressBar.setVisibility(View.VISIBLE);
                /*myViewHolder.tv_attempt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Log.e("examidddd1", "examiddd " + practiceTestModels.get(i).getExamid());
                            ((Activitycommon) context).gotopracticeActivitytoAttempt(practiceTestModels.get(i));

                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });*/


                myViewHolder.tv_attempt_practice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                           // Log.e("examidddd1", "examiddd " + practiceTestModels.get(i).getExamid());
                            ((Activitycommon) context).gotopracticeActivitytoAttempt(practiceTestModels.get(i));

                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                myViewHolder.lprogbar.setVisibility(View.VISIBLE);

              /*  if (practiceTestModels.get(i).getIspaid() == 0) {
                    myViewHolder.buy.setVisibility(View.VISIBLE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_practice.setVisibility(View.GONE);
                    myViewHolder.lprogbar.setVisibility(View.GONE);
                    myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    myViewHolder.answersheet.setVisibility(View.GONE);
                } else*/ if (practiceTestModels.get(i).getExamstatus().equalsIgnoreCase("Missed")) {
                    myViewHolder.buy.setVisibility(View.GONE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_practice.setVisibility(View.VISIBLE);
                    myViewHolder.lprogbar.setVisibility(View.GONE);
                    myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    myViewHolder.answersheet.setVisibility(View.GONE);
                } else if (practiceTestModels.get(i).getExamstatus().equalsIgnoreCase("Attempted") || (((int) Math.round(practiceTestModels.get(i).getProgress()) > 0))) {
                    myViewHolder.buy.setVisibility(View.GONE);
                    if (practiceTestModels.get(i).getExamstatus().equalsIgnoreCase("Attempted"))
                        myViewHolder.answersheet.setVisibility(View.VISIBLE);
                    else myViewHolder.answersheet.setVisibility(View.GONE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_practice.setVisibility(View.VISIBLE);
                    myViewHolder.lprogbar.setVisibility(View.VISIBLE);
                    myViewHolder.tv_checkprogress.setVisibility(View.VISIBLE);
                    myViewHolder.progressBar.setVisibility(View.VISIBLE);
                    myViewHolder.progressBar.setProgress((int) Math.round(practiceTestModels.get(i).getProgress()));
                    int progress_percent = (int) Math.round(practiceTestModels.get(i).getProgress());
                    myViewHolder.tv_percentcovered.setText(progress_percent + " % " + context.getResources().getString(R.string.covered));
                } else if ((int) Math.round(practiceTestModels.get(i).getProgress()) == 0) {
                    myViewHolder.buy.setVisibility(View.GONE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_practice.setVisibility(View.VISIBLE);
                    myViewHolder.lprogbar.setVisibility(View.GONE);
                    myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    myViewHolder.answersheet.setVisibility(View.GONE);
                }

                myViewHolder.tv_checkprogress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                            if (practiceTestModels.get(i).getProgress() > 0) {
                                ((PracticeTestActivity) context).gotoAction(practiceTestModels.get(i));
                            } else {
                                ((PracticeTestActivity) context).gotoAction(practiceTestModels.get(i));
                            }

                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                myViewHolder.buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ((Activitycommon) context).gotobuypackages();
                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                            e.printStackTrace();
                        }

                    }
                });
                myViewHolder.answersheet.setVisibility(View.GONE);
                myViewHolder.tv_chapterno.setText(practiceTestModels.get(i).getDayMonthNumber() + "");
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
             /*   ((PracticeTestActivity)context).gotoprogreport(i);
                myViewHolder.progressBar.setVisibility(View.VISIBLE);
                myViewHolder.lprogbar.setVisibility(View.VISIBLE);
                myViewHolder.tv_checkprogress.setVisibility(View.VISIBLE);

                myViewHolder.tv_checkprogress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((PracticeTestActivity)context).gotoAction(i);
                    }
                });*/
                        //((Activitycommon)context).gotopracticeActivity(practiceTestModels.get(i));
                    }
                });

                myViewHolder.answersheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ((Activitycommon) context).gotoanswersheet(practiceTestModels.get(i), 1);

                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });
    /*    if(practiceTestModels.get(i).getIsshowexam()==0 && !practiceTestModels.get(i).isIsexamgiven() )
        {
            try {
                Calendar cal1 = Calendar.getInstance();
                Date date2 = DateUtils.parseDate(practiceTestModels.get(i).getEnddate(), "dd MMM yyyy");
                Date date3 = DateUtils.parseDate(practiceTestModels.get(i).getExamendtime(), "HH:mm:ss");
                date2.setHours(date3.getHours());
                date2.setMinutes(date3.getMinutes());
                cal1.setTime(date2);

                Calendar cal = Calendar.getInstance();
                Date date = DateUtils.parseDate(practiceTestModels.get(i).getStartdate(), "dd MMM yyyy");
                Date date1 = DateUtils.parseDate(practiceTestModels.get(i).getExamstarttime(), "HH:mm:ss");
                date.setHours(date1.getHours());
                date.setMinutes(date1.getMinutes());
                cal.setTime(date);

                if (cal.before(Calendar.getInstance()) && cal1.after(Calendar.getInstance())) {
                    if(practiceTestModels.get(i).isIsqdownloaded()) {
                        myViewHolder.attempt.setVisibility(View.VISIBLE);
                        myViewHolder.download.setVisibility(View.GONE);
                    }else {
                        myViewHolder.attempt.setVisibility(View.GONE);
                        myViewHolder.download.setVisibility(View.VISIBLE);
                    }
                } else {
                    myViewHolder.attempt.setVisibility(View.GONE);
                    myViewHolder.download.setVisibility(View.GONE);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }else {
            myViewHolder.attempt.setVisibility(View.GONE);
            myViewHolder.download.setVisibility(View.GONE);
        }*/
        /*myViewHolder.btn_startpractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PracticeTestActivity)context).incrementprogress(i);
                myViewHolder.progressBar.setMax(max);
                p = pt.incrementprogress(20);
                myViewHolder.progressBar.setProgress(p);
                myViewHolder.tv_percentcovered.setText(practiceTestModels.get(i).getCovered_percentage()+" %covered");
            }
        });

        myViewHolder.btn_checkprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PracticeTestActivity)context).gotopracticetest(i);
            }
        });*/


            }

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return practiceTestModels.size();
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
                        if ((item.getExamname().toLowerCase().trim().contains(filterPattern)) || (item.getExamname().toLowerCase().split("").toString().contains(filterPattern))) {// || (item.getMarks().toLowerCase().contains(filterPattern))
                            filteredList.add(item);
                        }
                    }
                    Log.e("called1 ", "called1 " + filteredList.size() + " " + datasetFilter.size());
                }

            } catch (Exception e) {
                ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                e.printStackTrace();
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            try {
                practiceTestModels.clear();
                practiceTestModels.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            } catch (Exception e) {
                ((Activitycommon) context).reporterror("PracticeTestAdapter", e.toString());
                e.printStackTrace();
            }
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_chapterno, tv_topicname, tv_totalvideo, tv_total_questions, tv_totalconcepts, tv_percentcovered, buy, answersheet;
        //Button btn_startpractice, btn_checkprogress;
        ProgressBar progressBar;
        TextView tv_checkprogress;
        TextView tv_attempt, tv_attempt_practice;

        ImageView imgview;
        TextView timemarks1, time2;

        LinearLayout lpracticetest;
        LinearLayout lattemptexam;
        LinearLayout lprogbar;
        LinearLayout linearlayout_color;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_chapterno = itemView.findViewById(R.id.chapternumber);
            answersheet = itemView.findViewById(R.id.viewanswersheet);

            tv_topicname = itemView.findViewById(R.id.textview_1);
            tv_totalvideo = itemView.findViewById(R.id.totalvideos);
            tv_total_questions = itemView.findViewById(R.id.totalquestions);
            tv_totalconcepts = itemView.findViewById(R.id.totalconcepts);
            tv_percentcovered = itemView.findViewById(R.id.tv_coveredpercentage);
            tv_checkprogress = itemView.findViewById(R.id.tv_checkprogress);
            //download = (TextView) itemView.findViewById(R.id.download);
            tv_attempt = (TextView) itemView.findViewById(R.id.attempt);
            buy = (TextView) itemView.findViewById(R.id.pay);
            //btn_startpractice = itemView.findViewById(R.id.button_startpractice);
            //btn_checkprogress = itemView.findViewById(R.id.button_checkprogress);
            progressBar = itemView.findViewById(R.id.progress_topiccovered);

            imgview = itemView.findViewById(R.id.imageview_1);
            timemarks1 = itemView.findViewById(R.id.textview_2);
            time2 = itemView.findViewById(R.id.textview_3);

            lattemptexam = itemView.findViewById(R.id.linearlayouttoattemptexam);
            lpracticetest = itemView.findViewById(R.id.linearlayoutforpracticetest);
            lprogbar = itemView.findViewById(R.id.linearlayoutprogresbar);
            linearlayout_color = itemView.findViewById(R.id.linearlayout_color);

            tv_attempt_practice = itemView.findViewById(R.id.text_view_attempt);
        }
    }
}
