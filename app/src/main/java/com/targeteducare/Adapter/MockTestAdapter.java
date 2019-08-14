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
import com.targeteducare.GlobalValues;
import com.targeteducare.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MockTestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, Serializable {
    Context context;
    ArrayList<Exam> datasetFilter;
    ArrayList<Exam> mockTestDataset = new ArrayList<>();
    String lang = "";
    String type="";
    int[] colorsforbackground;
    int set_color = 0;

    public MockTestAdapter(Context context, ArrayList<Exam> mockTestDataset, String lang,String type) {

        try {
            this.context = context;
            this.mockTestDataset.addAll(mockTestDataset);
            this.datasetFilter = mockTestDataset;
            this.lang = lang;
            this.type=type;
            //colorsforbackground = context.getResources().getIntArray(R.array.colorsforbackground);

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
            e.printStackTrace();
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (mockTestDataset.get(position).isIsheader())
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        try {
            colorsforbackground = context.getResources().getIntArray(R.array.colorsforbackground);

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (i == 1) {
            View v = inflater.inflate(R.layout.headerxml, viewGroup, false);
            return new viewHolderHeader(v);
        } else {
            View v = inflater.inflate(R.layout.list_item_exam, viewGroup, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        try {
            if (getItemViewType(i) == 1) {
                viewHolderHeader header = (viewHolderHeader) holder;
                if (lang.equalsIgnoreCase("mr"))
                    header.txt.setText(mockTestDataset.get(i).getCoursename_inmarathi());
                else
                    header.txt.setText(mockTestDataset.get(i).getCoursename());
            } else {
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.tv_chapterno.setVisibility(View.VISIBLE);

                int chap_no = i;
                if (chap_no >= 0 && chap_no < 10) {
                    myViewHolder.tv_chapterno.setText("0" + (chap_no + 1));
                } else {
                    myViewHolder.tv_chapterno.setText("" + (chap_no++));
                }

                myViewHolder.tv_topicname.setVisibility(View.VISIBLE);
                myViewHolder.tv_total_questions.setVisibility(View.VISIBLE);
                myViewHolder.tv_totalvideo.setVisibility(View.VISIBLE);
                myViewHolder.tv_totalconcepts.setVisibility(View.VISIBLE);
                myViewHolder.timemarks1.setVisibility(View.VISIBLE);
                myViewHolder.imgview.setVisibility(View.GONE);
                myViewHolder.time2.setVisibility(View.GONE);
                myViewHolder.timemarks1.setText(mockTestDataset.get(i).getMarks() + " " + context.getResources().getString(R.string.exam_marks));

                if (lang.equalsIgnoreCase("mr"))
                    myViewHolder.tv_topicname.setText(mockTestDataset.get(i).getName_InMarathi());
                else
                    myViewHolder.tv_topicname.setText(mockTestDataset.get(i).getExamname());

                //Log.e("name", "name " + mockTestDataset.get(i).getExamname());
                myViewHolder.tv_total_questions.setText(mockTestDataset.get(i).getTotal_questions() + " questions");
                myViewHolder.tv_totalvideo.setText(mockTestDataset.get(i).getTotal_videos() + " videos");
                myViewHolder.tv_totalconcepts.setText(mockTestDataset.get(i).getTotal_concepts() + " concepts");

                myViewHolder.lprogbar.setVisibility(View.VISIBLE);
                myViewHolder.answersheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Log.e("view answersheet ", "mock test adapter");
                            ((Activitycommon) context).gotoanswersheet(mockTestDataset.get(i), 0);
                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

          /*  if (mockTestDataset.get(i).isIsexamgiven()) {
                if (((int) Math.round(mockTestDataset.get(i).getProgress()) > 0) && ((int) Math.round(mockTestDataset.get(i).getProgress()) <= 100)) {
                    Log.e("in mock test ", "retry");
                    myViewHolder.tv_attempt.setText(context.getResources().getString(R.string.retry));
                    myViewHolder.answersheet.setVisibility(View.VISIBLE);
                    myViewHolder.lprogbar.setVisibility(View.VISIBLE);
                    myViewHolder.tv_checkprogress.setVisibility(View.VISIBLE);
                    myViewHolder.progressBar.setVisibility(View.VISIBLE);
                    myViewHolder.progressBar.setProgress((int) Math.round(mockTestDataset.get(i).getProgress()));
                    int progress_percent = (int) Math.round(mockTestDataset.get(i).getProgress());
                    myViewHolder.tv_percentcovered.setText(progress_percent + " % " + context.getResources().getString(R.string.covered));
                }
                else {
                    Log.e("in mock test ", "attempt");
                    //myViewHolder.answersheet.setVisibility(View.VISIBLE);
                    myViewHolder.tv_attempt.setText(context.getResources().getString(R.string.attempt));
                    myViewHolder.lprogbar.setVisibility(View.GONE);
                    myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    myViewHolder.progressBar.setVisibility(View.GONE);
                }
            } else {
                myViewHolder.answersheet.setVisibility(View.GONE);
                myViewHolder.lprogbar.setVisibility(View.GONE);
                myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                myViewHolder.progressBar.setVisibility(View.GONE);
            }
*/
                /*if (mockTestDataset.get(i).getIspaid() == 0) {
                    myViewHolder.buy.setVisibility(View.VISIBLE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_exam.setVisibility(View.GONE);
                    myViewHolder.lprogbar.setVisibility(View.GONE);
                    myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    myViewHolder.answersheet.setVisibility(View.GONE);
                } else*/ if ((mockTestDataset.get(i).getExamstatus().equalsIgnoreCase("Missed"))&& mockTestDataset.get(i).getProgress()<=0) {
                    myViewHolder.buy.setVisibility(View.GONE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_exam.setText(context.getResources().getString(R.string.take_test));
                    myViewHolder.tv_attempt_exam.setVisibility(View.VISIBLE);
                    myViewHolder.lprogbar.setVisibility(View.GONE);
                    myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    myViewHolder.answersheet.setVisibility(View.GONE);
                } else if (mockTestDataset.get(i).getExamstatus().equalsIgnoreCase("Attempted") || (((int) Math.round(mockTestDataset.get(i).getProgress()) > 0))) {
                    myViewHolder.buy.setVisibility(View.GONE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_exam.setVisibility(View.GONE);
                    myViewHolder.lprogbar.setVisibility(View.VISIBLE);
                    myViewHolder.answersheet.setVisibility(View.VISIBLE);
                    myViewHolder.progressBar.setVisibility(View.VISIBLE);
                    myViewHolder.progressBar.setProgress((int) Math.round(mockTestDataset.get(i).getProgress()));
                    int progress_percent = (int) Math.round(mockTestDataset.get(i).getProgress());
                    myViewHolder.tv_percentcovered.setText(progress_percent + " % " + context.getResources().getString(R.string.covered));
                    if(mockTestDataset.get(i).getResultdate().equalsIgnoreCase("null") || mockTestDataset.get(i).getResultdate().length()==0)
                    {
                        myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    }else {
                        Calendar cal1 = Calendar.getInstance();
                        Date date2 = DateUtils.parseDate(mockTestDataset
                                .get(i).getResultdate(), "dd MMM yyyy");
                        if (cal1 != null) {
                            if (date2 != null)
                                cal1.setTime(date2);

                            if (Calendar.getInstance().before(cal1)) {
                                myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                            } else {
                                myViewHolder.tv_checkprogress.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else if ((int) Math.round(mockTestDataset.get(i).getProgress()) == 0) {
                    myViewHolder.buy.setVisibility(View.GONE);
                    myViewHolder.tv_attempt.setVisibility(View.GONE);
                    myViewHolder.tv_attempt_exam.setText(context.getResources().getString(R.string.take_test));
                    myViewHolder.tv_attempt_exam.setVisibility(View.VISIBLE);
                    myViewHolder.lprogbar.setVisibility(View.GONE);
                    myViewHolder.tv_checkprogress.setVisibility(View.GONE);
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    myViewHolder.answersheet.setVisibility(View.GONE);
                }
                myViewHolder.buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ((Activitycommon) context).gotobuypackages();
                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
                            e.printStackTrace();
                        }

                    }
                });
                myViewHolder.tv_checkprogress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ((Activitycommon) context).gotoviewmarksheet("http://" + GlobalValues.IP + "/Home/StudentPerformanceReport?" + mockTestDataset.get(i).getExamid() + "_" + GlobalValues.student.getId());
                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                myViewHolder.tv_attempt_exam.setOnClickListener(new View.OnClickListener() {
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

                        try {
                            Log.e("Got into ", "attempt click mock test adapter");
                            ((Activitycommon) context).gotoexamActivity(mockTestDataset.get(i),type);

                        } catch (Exception e) {
                            ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
                            e.printStackTrace();
                        }
                    }
                });
    /*    if(mockTestDataset
   .get(i).getIsshowexam()==0 && !mockTestDataset
   .get(i).isIsexamgiven() )
        {
            try {
                Calendar cal1 = Calendar.getInstance();
                Date date2 = DateUtils.parseDate(mockTestDataset
               .get(i).getEnddate(), "dd MMM yyyy");
                Date date3 = DateUtils.parseDate(mockTestDataset
               .get(i).getExamendtime(), "HH:mm:ss");
                date2.setHours(date3.getHours());
                date2.setMinutes(date3.getMinutes());
                cal1.setTime(date2);

                Calendar cal = Calendar.getInstance();
                Date date = DateUtils.parseDate(mockTestDataset
               .get(i).getStartdate(), "dd MMM yyyy");
                Date date1 = DateUtils.parseDate(mockTestDataset
               .get(i).getExamstarttime(), "HH:mm:ss");
                date.setHours(date1.getHours());
                date.setMinutes(date1.getMinutes());
                cal.setTime(date);

                if (cal.before(Calendar.getInstance()) && cal1.after(Calendar.getInstance())) {
                    if(mockTestDataset
                   .get(i).isIsqdownloaded()) {
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
                myViewHolder.tv_percentcovered.setText(mockTestDataset
               .get(i).getCovered_percentage()+" %covered");
            }
        });

        myViewHolder.btn_checkprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PracticeTestActivity)context).gotopracticetest(i);
            }
        });*/


                myViewHolder.imgview.setVisibility(View.GONE);
                myViewHolder.tv_chapterno.setVisibility(View.VISIBLE);
                //   myViewHolder.txt2.setVisibility(View.GONE);

                myViewHolder.txt2.setText(mockTestDataset.get(i).getDurationinMin() + " " + context.getResources().getString(R.string.exam_minutes) + "\t\t" + mockTestDataset.get(i).getMarks() + " " + context.getResources().getString(R.string.exam_marks));
                //Log.e("date is ",mdataset.get(position).getStartdate()+"");
               // myViewHolder.tv_chapterno.setText(DateUtils.parseDate(mockTestDataset.get(i).getStartdate(), "dd MMMM yyyy", "dd MMM") + "");
                myViewHolder.tv_chapterno.setText(mockTestDataset.get(i).getDayMonthNumber());
               // Log.e("dateeee","dateee "+DateUtils.parseDate(mockTestDataset.get(i).getStartdate()));
                /*set_color = i;
                if (set_color < colorsforbackground.length) {
                    myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[set_color]);
                } else {
                    set_color = 0;
                    myViewHolder.linearlayout_color.setBackgroundColor(context.getResources().getColor(R.color.default_color));
                }

                set_color++;*/

                if (i < colorsforbackground.length) {
                    myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[i]);
                } else {
                    myViewHolder.linearlayout_color.setBackgroundColor(colorsforbackground[new Random().nextInt(colorsforbackground.length)]);
                }

            }

        } catch (Exception e) {
            Log.e("error ","error "+e.toString());
            ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mockTestDataset.size();
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
                ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
                e.printStackTrace();
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            try {
                mockTestDataset.clear();
                mockTestDataset.addAll((ArrayList) results.values);
                notifyDataSetChanged();

            } catch (Exception e) {
                ((Activitycommon) context).reporterror("MockTestAdapter", e.toString());
                e.printStackTrace();
            }
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_chapterno, tv_topicname, tv_totalvideo, tv_total_questions, tv_totalconcepts, tv_percentcovered, buy, txt2;
        //Button btn_startpractice, btn_checkprogress;
        ProgressBar progressBar;
        TextView tv_checkprogress, answersheet;
        TextView tv_attempt, tv_attempt_exam;

        ImageView imgview;
        TextView timemarks1, time2;

        LinearLayout lmocktest;
        LinearLayout lattemptexam;
        LinearLayout lprogbar;
        LinearLayout linearlayout_color;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt2 = (TextView) itemView.findViewById(R.id.textview_2);
            buy = (TextView) itemView.findViewById(R.id.pay);
            tv_chapterno = itemView.findViewById(R.id.chapternumber);
            tv_topicname = itemView.findViewById(R.id.textview_1);
            tv_totalvideo = itemView.findViewById(R.id.totalvideos);
            tv_total_questions = itemView.findViewById(R.id.totalquestions);
            tv_totalconcepts = itemView.findViewById(R.id.totalconcepts);
            tv_percentcovered = itemView.findViewById(R.id.tv_coveredpercentage);
            tv_checkprogress = itemView.findViewById(R.id.tv_checkprogress);
            //download = (TextView) itemView.findViewById(R.id.download);
            tv_attempt = (TextView) itemView.findViewById(R.id.attempt);
            //btn_startpractice = itemView.findViewById(R.id.button_startpractice);
            //btn_checkprogress = itemView.findViewById(R.id.button_checkprogress);
            progressBar = itemView.findViewById(R.id.progress_topiccovered);

            imgview = itemView.findViewById(R.id.imageview_1);
            timemarks1 = itemView.findViewById(R.id.textview_2);
            time2 = itemView.findViewById(R.id.textview_3);

            answersheet = (TextView) itemView.findViewById(R.id.viewanswersheet);

            lattemptexam = itemView.findViewById(R.id.linearlayouttoattemptexam);
            lmocktest = itemView.findViewById(R.id.linearlayoutforpracticetest);
            lprogbar = itemView.findViewById(R.id.linearlayoutprogresbar);

            imgview = itemView.findViewById(R.id.imageview_1);
            tv_chapterno = itemView.findViewById(R.id.chapternumber);
            linearlayout_color = itemView.findViewById(R.id.linearlayout_color);

            tv_attempt_exam = itemView.findViewById(R.id.text_view_attempt);
        }
    }
}
