package com.targeteducare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.targeteducare.Classes.Exam;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgressReportActivity extends Activitycommon {

    TextView tv_time, tv_topicname, tv_percentcovered, tv_questions, tv_correct, tv_skipped, tv_wrong, tv_accuracy, tv_speed;
    Button btn_pracresumretry, btn_quit;

    ArrayList<Exam> examArrayList;
    Exam exam;
    int flag = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report);
        /*Bundle b = getIntent().getExtras();
        if(b!=null){
            if (b.containsKey("exam")) {
                this.exam = (Exam) b.getSerializable("exam");
                tv_topicname = findViewById(R.id.tv_topicnameselected);
                tv_topicname.setText(exam.getExamname());
            }
        }*/

        tv_time = findViewById(R.id.tv_timetaken);

        tv_percentcovered = findViewById(R.id.tv_percentcovered);
        tv_questions = findViewById(R.id.tv_totalquestions);
        tv_correct = findViewById(R.id.tv_correctanswers);
        tv_skipped = findViewById(R.id.tv_skippedquestions);
        tv_wrong = findViewById(R.id.tv_wronganswers);
        tv_accuracy = findViewById(R.id.tv_accuracypercent);
        tv_speed = findViewById(R.id.tv_questionsperhour);

        btn_pracresumretry = findViewById(R.id.button_startpractice);
        btn_quit = findViewById(R.id.button_quitpractice);

        examArrayList = (ArrayList<Exam>) getIntent().getSerializableExtra("progressreport");


        Bundle b1=getIntent().getExtras();
        if(b1!=null){
            if (b1.containsKey("flag")) {
                flag = b1.getInt("flag");
            }
        }


        //tv_questions.setText(GlobalValues.exam.getTotal_questions());
        //tv_time.setText(GlobalValues.exam.getDuration());

        if(flag==0){
            try{
                Log.e("i am in ",String.valueOf(flag));
                for (int i=0;i<examArrayList.size();i++) {
                    JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(examArrayList.get(i).getExamid(), examArrayList.get(i).getExam_type());
                    if (array.length() > 0) {
                        //Exam exam = new Exam();
                        JSONObject obj = array.getJSONObject(0);
                        //int questions = obj.getInt(DatabaseHelper.QUESTION);
                        //tv_questions.setText(questions+"");
                        btn_pracresumretry.setText("Start Again");
                        long timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                        tv_time.setText((timetaken/1000)+" s");
                        //tv_questions.setText(examArrayList.get(i).getTotal_questions());
                        int correct = obj.getInt(DatabaseHelper.CORRECT);
                        tv_correct.setText(correct+"");
                        //tv_correct.setText(exam.getTotal_correct());
                        int skipped = obj.getInt(DatabaseHelper.SKIPP);
                        tv_skipped.setText(skipped+"");
                        int wrong = obj.getInt(DatabaseHelper.WRONG);
                        tv_wrong.setText(wrong+"");
                        //tv_wrong.setText(exam.getTotal_wrong());
                        int percent_covered = (int)obj.getDouble(DatabaseHelper.PROGRESS);
                        tv_percentcovered.setText(percent_covered+" % covered");
                        int speed = (int)obj.getDouble(DatabaseHelper.SPEED);
                        tv_speed.setText(speed+" s/Q");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(flag == 1){
            try{
                Log.e("i am in ",String.valueOf(flag));
                for (int i=0;i<examArrayList.size();i++) {
                    JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(examArrayList.get(i).getExamid(), examArrayList.get(i).getExam_type());
                    if (array.length() > 0) {
                        //Exam exam = new Exam();
                        JSONObject obj = array.getJSONObject(0);
                        //int questions = obj.getInt(DatabaseHelper.QUESTION);
                        //tv_questions.setText(questions+"");
                        //btn_pracresumretry.setText("Resume Practice");
                        btn_pracresumretry.setVisibility(View.GONE);
                        long timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                        tv_time.setText((timetaken/1000)+" s");
                        //tv_questions.setText(examArrayList.get(i).getTotal_questions());
                        int correct = obj.getInt(DatabaseHelper.CORRECT);
                        tv_correct.setText(correct+"");
                        //tv_correct.setText(exam.getTotal_correct());
                        int skipped = obj.getInt(DatabaseHelper.SKIPP);
                        tv_skipped.setText(skipped+"");
                        int wrong = obj.getInt(DatabaseHelper.WRONG);
                        tv_wrong.setText(wrong+"");
                        //tv_wrong.setText(exam.getTotal_wrong());
                        int percent_covered = (int)obj.getDouble(DatabaseHelper.PROGRESS);
                        tv_percentcovered.setText(percent_covered+" % covered");
                        int speed = (int)obj.getDouble(DatabaseHelper.SPEED);
                        tv_speed.setText(speed+" s/Q");

                        /*btn_pracresumretry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                back();
                            }
                        });*/

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(flag == 2){
            try{
                Log.e("i am in ",String.valueOf(flag));
                for (int i=0;i<examArrayList.size();i++) {
                    JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(examArrayList.get(i).getExamid(), examArrayList.get(i).getExam_type());
                    if (array.length() > 0) {
                        //Exam exam = new Exam();
                        JSONObject obj = array.getJSONObject(0);
                        int questions = obj.getInt(DatabaseHelper.QUESTION);
                        tv_questions.setText(questions+"");
                        //btn_pracresumretry.setText("Resume Practice");
                        btn_pracresumretry.setVisibility(View.GONE);
                        long timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                        tv_time.setText((timetaken/1000)+" s");
                        //tv_questions.setText(examArrayList.get(i).getTotal_questions());
                        int correct = obj.getInt(DatabaseHelper.CORRECT);
                        tv_correct.setText(correct+"");
                        //tv_correct.setText(exam.getTotal_correct());
                        int skipped = obj.getInt(DatabaseHelper.SKIPP);
                        tv_skipped.setText(skipped+"");
                        int wrong = obj.getInt(DatabaseHelper.WRONG);
                        tv_wrong.setText(wrong+"");
                        //tv_wrong.setText(exam.getTotal_wrong());
                        int percent_covered = (int)obj.getDouble(DatabaseHelper.PROGRESS);
                        tv_percentcovered.setText(percent_covered+" % covered");
                        int speed = (int)obj.getDouble(DatabaseHelper.SPEED);
                        tv_speed.setText(speed+" s/Q");

                        /*btn_pracresumretry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                back();
                            }
                        });*/

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Log.e("value of flag ", ""+flag);
        }



        /*examArrayList = new ArrayList<Exam>();

        for(int i=0; i<examArrayList.size(); i++){
            Exam progressReportData = new Exam();
            examArrayList.add(progressReportData);
        }*/

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
