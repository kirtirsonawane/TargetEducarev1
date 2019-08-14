package com.targeteducare;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.targeteducare.Classes.EngColgDataModel;

import java.util.ArrayList;

public class CollegeComparisionActivity extends Activitycommon {
    int count = 0;
    LinearLayout layout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college__comparision);
        try {
        setmaterialDesign();
        setTitle(getResources().getString(R.string.title_collegecompare));
        back();


            Bundle bundle = getIntent().getExtras();
            ArrayList<EngColgDataModel> Received_data = (ArrayList<EngColgDataModel>) bundle.getSerializable("collegedata");
            Log.e("Recieved data : ", Received_data.get(0).getExam_name() + " " + Received_data.size());

            layout = (LinearLayout) findViewById(R.id.layout);


            final ScrollView scrollView1 = (ScrollView) findViewById(R.id.fixed_scrollview);
            final ScrollView scrollView2 = (ScrollView) findViewById(R.id.sv);
            scrollView1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    scrollView2.scrollTo(scrollX, scrollY);
                }
            });

            scrollView2.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    scrollView1.scrollTo(scrollX, scrollY);
                }
            });


            TableLayout ll = (TableLayout) findViewById(R.id.table);





  /*  TableRow tr_head = new TableRow(this);
    tr_head.setBackgroundColor(Color.GRAY);
    tr_head.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.FILL_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT));
    TextView tv_head = new TextView(this);
    tv_head.setText("Compare ");
    ll.addView(tr_head);*/


            for (int i = 0; i < 6; i++) {


                TableRow tr = new TableRow(this);

                tr.setId(100 + count);

                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT, 130));

                switch (i) {


                    case 0:
                        for (int j = 0; j < Received_data.size(); j++) {
                            TextView tv1 = new TextView(this);
                            tv1.setId(200 + count);
                            tv1.setHeight(53);
                            tv1.setText(Received_data.get(j).getCollege_name());
                            tv1.setPadding(5, 5, 5, 5);
                            tv1.setTextColor(Color.BLACK);
                            tv1.setBackgroundResource(R.drawable.comparision_table_shape_xml);
                    /*ShapeDrawable border = new ShapeDrawable(new RectShape());
                    border.getPaint().setStyle(Paint.Style.STROKE);
                    border.getPaint().setColor(Color.BLACK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv1.setBackground(border);

                    }*/
                            tr.addView(tv1);
                        }
                        break;
                    case 1:
                        for (int j = 0; j < Received_data.size(); j++) {
                            TextView tv2 = new TextView(this);
                            tv2.setId(200 + count);
                            tv2.setHeight(53);
                            tv2.setText(String.valueOf(Received_data.get(j).getCourses()));
                            tv2.setPadding(5, 5, 5, 5);
                            tv2.setTextColor(Color.BLACK);
                            tv2.setBackgroundResource(R.drawable.comparision_table_shape_xml);
                /*ShapeDrawable border = new ShapeDrawable(new RectShape());
                border.getPaint().setStyle(Paint.Style.STROKE);
                border.getPaint().setColor(Color.BLACK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv2.setBackground(border);

                }*/
                            tr.addView(tv2);
                        }

                        break;
                    case 2:
                        for (int j = 0; j < Received_data.size(); j++) {
                            TextView tv3 = new TextView(this);
                            tv3.setId(200 + count);
                            tv3.setHeight(53);
                            tv3.setText(Received_data.get(j).getInstitute_type());
                            tv3.setPadding(5, 5, 5, 5);
                            tv3.setTextColor(Color.BLACK);
                            tv3.setBackgroundResource(R.drawable.comparision_table_shape_xml);
                   /* ShapeDrawable border = new ShapeDrawable(new RectShape());
                    border.getPaint().setStyle(Paint.Style.STROKE);
                    border.getPaint().setColor(Color.BLACK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv3.setBackground(border);
                    }*/
                            tr.addView(tv3);
                        }
                        break;
                    case 3:

                        for (int j = 0; j < Received_data.size(); j++) {
                            TextView tv4 = new TextView(this);
                            tv4.setId(200 + count);
                            tv4.setHeight(53);
                            tv4.setText(Received_data.get(j).getExam_name());
                            tv4.setPadding(5, 5, 5, 5);
                            tv4.setTextColor(Color.BLACK);
                            tv4.setBackgroundResource(R.drawable.comparision_table_shape_xml);
                    /*ShapeDrawable border = new ShapeDrawable(new RectShape());
                    border.getPaint().setStyle(Paint.Style.STROKE);
                    border.getPaint().setColor(Color.BLACK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv4.setBackground(border);
                    }*/
                            tr.addView(tv4);
                        }
                        break;
                    case 4:
                        for (int j = 0; j < Received_data.size(); j++) {
                            TextView tv5 = new TextView(this);
                            tv5.setId(200 + count);
                            tv5.setHeight(53);
                            tv5.setText(String.valueOf(Received_data.get(j).getReviews()));
                            tv5.setPadding(5, 5, 5, 5);
                            tv5.setTextColor(Color.BLACK);
                            tv5.setBackgroundResource(R.drawable.comparision_table_shape_xml);
                    /*ShapeDrawable border = new ShapeDrawable(new RectShape());
                    border.getPaint().setStyle(Paint.Style.STROKE);
                    border.getPaint().setColor(Color.BLACK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv5.setBackground(border);
                    }*/
                            tr.addView(tv5);
                        }
                        break;
                    case 5:
                        for (int j = 0; j < Received_data.size(); j++) {
                            TextView tv6 = new TextView(this);
                            tv6.setId(200 + count);
                            tv6.setHeight(53);
                            tv6.setText(String.valueOf(Received_data.get(j).getRating()));
                            tv6.setPadding(5, 5, 5, 5);
                            tv6.setTextColor(Color.BLACK);
                            tv6.setBackgroundResource(R.drawable.comparision_table_shape_xml);
                  /*  ShapeDrawable border = new ShapeDrawable(new RectShape());
                    border.getPaint().setStyle(Paint.Style.STROKE);
                    border.getPaint().setColor(Color.BLACK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv6.setBackground(border);
                    }*/
                            tr.addView(tv6);
                        }

                        break;

                }

                ll.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                count++;
            }


////////////////////////-----------------------------half code-------------------***********************************************---------------------------////////////////////////////
   /*  TableLayout ll = (TableLayout)findViewById(R.id.table);



    TableRow tr_head = new TableRow(this);

    tr_head.setBackgroundColor(Color.GRAY);
    tr_head.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.FILL_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT));


    TextView label_CollegeName = new TextView(this);
    label_CollegeName.setId(count + 1);
    label_CollegeName.setText("College Name" + "\n");
    label_CollegeName.setTextColor(Color.BLACK);
    label_CollegeName.setPadding(5, 5, 5, 5);
    tr_head.addView(label_CollegeName);// add the column to the table row here

    TextView label_Course = new TextView(this);
    label_Course.setId(count + 1);// define id that must be unique
    label_Course.setText("Course"); // set the text for the header
    label_Course.setTextColor(Color.BLACK); // set the color
    label_Course.setPadding(5, 5, 5, 5); // set the padding (if required)
    tr_head.addView(label_Course); // add the column to the table row here


    TextView label_Institute_type = new TextView(this);
    label_Institute_type.setId(count + 1);// define id that must be unique
    label_Institute_type.setText("Institue_type"); // set the text for the header
    label_Institute_type.setTextColor(Color.BLACK); // set the color
    label_Institute_type.setPadding(5, 5, 5, 5); // set the padding (if required)
    tr_head.addView(label_Institute_type); // add the column to the table row here


    TextView label_Exam_Name = new TextView(this);
    label_Exam_Name.setId(count + 1);// define id that must be unique
    label_Exam_Name.setText("Exam Name"); // set the text for the header
    label_Exam_Name.setTextColor(Color.BLACK); // set the color
    label_Exam_Name.setPadding(5, 5, 5, 5); // set the padding (if required)
    tr_head.addView(label_Exam_Name); // add the column to the table row here


    TextView label_Ratings = new TextView(this);
    label_Ratings.setId(count + 1);// define id that must be unique
    label_Ratings.setText("Ratings"); // set the text for the header
    label_Ratings.setTextColor(Color.BLACK); // set the color
    label_Ratings.setPadding(5, 5, 5, 5); // set the padding (if required)
    tr_head.addView(label_Ratings); // add the column to the table row here


    TextView label_Review = new TextView(this);
    label_Review.setId(count + 1);// define id that must be unique
    label_Review.setText("Reviews"); // set the text for the header
    label_Review.setTextColor(Color.BLACK); // set the color
    label_Review.setPadding(5, 5, 5, 5); // set the padding (if required)
    tr_head.addView(label_Review); // add the column to the table row here


    ll.addView(tr_head, new TableLayout.LayoutParams(
            TableLayout.LayoutParams.FILL_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT));

        for(int i=0;i<Received_data.size();i++) {
            // tbrow=new TableRow(this);
            TableRow tr = new TableRow(this);
            if (count % 2 != 0) tr.setBackgroundColor(Color.GRAY);
            tr.setId(100 + count);

            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            try {
                TextView tv1 = new TextView(this);
                TextView tv3 = new TextView(this);
                TextView tv2 = new TextView(this);
                TextView tv4 = new TextView(this);
                TextView tv5 = new TextView(this);
                TextView tv6 = new TextView(this);

                tv1.setId(200 + count);
                tv1.setText(" || " + Received_data.get(i).getCollege_name());
                tv1.setPadding(2, 0, 5, 0);
                tv1.setTextColor(Color.BLACK);
                tr.addView(tv1);


                tv2.setId(200 + count);
                tv2.setText(" || " + Received_data.get(i).getCourses());
                tv2.setPadding(2, 0, 5, 0);
                tv2.setTextColor(Color.BLACK);
                tr.addView(tv2);

                tv3.setId(200 + count);
                tv3.setText(" || " + Received_data.get(i).getInstitute_type());
                tv3.setPadding(2, 0, 5, 0);
                tv3.setTextColor(Color.BLACK);
                tr.addView(tv3);

                tv4.setId(200 + count);
                tv4.setText(" || " + Received_data.get(i).getExam_name());
                tv4.setPadding(2, 0, 5, 0);
                tv4.setTextColor(Color.BLACK);
                tr.addView(tv4);

                tv5.setId(200 + count);
                tv5.setText(" || " + Received_data.get(i).getReviews());
                tv5.setPadding(2, 0, 5, 0);
                tv5.setTextColor(Color.BLACK);
                tr.addView(tv5);

                tv6.setId(200 + count);
                tv6.setText(" || " + Received_data.get(i).getRating());
                tv6.setPadding(2, 0, 5, 0);
                tv6.setTextColor(Color.BLACK);
                tr.addView(tv6);


            } catch (java.lang.IndexOutOfBoundsException e) {
                e.printStackTrace();

            }


            ll.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            count++;
        }*/

        } catch (Exception e) {
            reporterror("CollegeComparisionActivity",e.toString());
            e.printStackTrace();
        }
    }
}

