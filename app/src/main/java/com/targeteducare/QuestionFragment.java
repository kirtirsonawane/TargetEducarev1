package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Classes.Options;
import com.targeteducare.Classes.Question;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class
QuestionFragment extends Fragment implements Html.ImageGetter {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private Question mParam1;
    private String mParam2;
    String lang = "";
    int flag_setback = 0;
    int pos = 0;
    int flag_reviewcheck = 0;
    LinearLayout layoutv1;
    ArrayList<Question> question = new ArrayList<>();
    TextView txt, txtsub;
    LinearLayout layout;
    RadioGroup rg;
    View view;
    TextView tv_serialNo;
    long MAX_TOUCH_DURATION = 150;
    long m_DownTime = 0;
    TextView report_error;
    Switch switchlang;
    String style = "<style type=\"text/css\">\n" +
            "@font-face {\n" +
            "    font-family: Symbol;\n" +
            "    src: url(\"file:///android_asset/fonts/hinted_symbolmt.ttf\")\n" +
            "}\n" +
            "@font-face {\n" +
            "    font-family: Calibri;\n" +
            "    src: url(\"file:///android_asset/fonts/calibri.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: AkrutiDevPriya;\n" +
            "    src: url(\"file:///android_asset/fonts/akrutidevpriyanormal.ttf\")\n" +
            "}\n" +
            "\n" +
            "</style>";

    TextView question_unclear, wrong_answer, wrong_question, unclear_image;
    EditText et_issueindetail;
    ImageView cancel_dialog;
    Button submit_issue;
    int examid = 0;
    int pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
    StringBuilder issue = new StringBuilder();

    public QuestionFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(Question param1, String param2) {

        QuestionFragment fragment = new QuestionFragment();
        try {
            Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            fragment.question.add(param1);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                mParam1 = (Question) getArguments().getSerializable(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rec);
        } catch (Exception e) {
            reporterror("QuestionFragment", e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question, container, false);
        try {
            Log.e("layout ", "QuestionFragment");
            layout = (LinearLayout) view.findViewById(R.id.layout);


            report_error = (TextView) view.findViewById(R.id.iv_reporterror);
            report_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                        View alertView = layoutInflater.inflate(R.layout.report_error_layout, null);

                        question_unclear = alertView.findViewById(R.id.question_unclear);
                        wrong_answer = alertView.findViewById(R.id.wrong_answer);
                        wrong_question = alertView.findViewById(R.id.wrong_question);
                        unclear_image = alertView.findViewById(R.id.unclear_image);
                        et_issueindetail = alertView.findViewById(R.id.et_issueindetail);
                        submit_issue = alertView.findViewById(R.id.button_submit_issue);
                        cancel_dialog = alertView.findViewById(R.id.cancel_dialog);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setView(alertView);

                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        if (!((Activity) getActivity()).isFinishing()) {
                            alertDialog.show();
                        }

                        question_unclear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    question_unclear.setSelected(false);
                                    if (pos1 == 0) {
                                        question_unclear.setBackgroundResource(R.drawable.textview_onclick);
                                        question_unclear.setTextColor(getResources().getColor(R.color.white));
                                        question_unclear.setSelected(true);
                                        pos1 = 1;
                                    } else {
                                        question_unclear.setBackgroundResource(R.drawable.rounded_textviewforsubject);
                                        question_unclear.setTextColor(getResources().getColor(R.color.blue1));
                                        question_unclear.setSelected(false);
                                        pos1 = 0;
                                    }
                                } catch (Exception e) {
                                    reporterror("QuestionPracticeFragment", e.toString());
                                    e.printStackTrace();
                                }

                            }
                        });

                        wrong_answer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    wrong_answer.setSelected(false);
                                    if (pos2 == 0) {
                                        wrong_answer.setBackgroundResource(R.drawable.textview_onclick);
                                        wrong_answer.setTextColor(getResources().getColor(R.color.white));
                                        wrong_answer.setSelected(true);
                                        pos2 = 1;
                                    } else {
                                        wrong_answer.setBackgroundResource(R.drawable.rounded_textviewforsubject);
                                        wrong_answer.setTextColor(getResources().getColor(R.color.blue1));
                                        wrong_answer.setSelected(false);
                                        pos2 = 0;
                                    }
                                } catch (Exception e) {
                                    reporterror("QuestionPracticeFragment", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });

                        wrong_question.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {

                                    wrong_question.setSelected(false);
                                    if (pos3 == 0) {
                                        wrong_question.setBackgroundResource(R.drawable.textview_onclick);
                                        wrong_question.setTextColor(getResources().getColor(R.color.white));
                                        wrong_question.setSelected(true);
                                        pos3 = 1;
                                    } else {
                                        wrong_question.setBackgroundResource(R.drawable.rounded_textviewforsubject);
                                        wrong_question.setTextColor(getResources().getColor(R.color.blue1));
                                        wrong_question.setSelected(false);
                                        pos3 = 0;
                                    }

                                } catch (Exception e) {
                                    reporterror("QuestionPracticeFragment", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });

                        unclear_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {

                                    unclear_image.setSelected(false);
                                    if (pos4 == 0) {
                                        unclear_image.setBackgroundResource(R.drawable.textview_onclick);
                                        unclear_image.setTextColor(getResources().getColor(R.color.white));
                                        unclear_image.setSelected(true);
                                        pos4 = 1;
                                    } else {
                                        unclear_image.setBackgroundResource(R.drawable.rounded_textviewforsubject);
                                        unclear_image.setTextColor(getResources().getColor(R.color.blue1));
                                        unclear_image.setSelected(false);
                                        pos4 = 0;
                                    }

                                } catch (Exception e) {
                                    reporterror("QuestionPracticeFragment", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });

                        submit_issue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //bitmap = CaptureScreenshot.getInstance().takeScreenshotForScreen(getActivity());
                                //new uploadpan().execute();

                                try {
                                    issue.delete(0, issue.length());

                                    if (question_unclear.isSelected()) {
                                        issue.append(getResources().getString(R.string.report_error_question_unclear) + " / ");
                                    }
                                    if (wrong_answer.isSelected()) {
                                        issue.append(getResources().getString(R.string.report_error_wrong_answer) + " / ");
                                    }
                                    if (wrong_question.isSelected()) {
                                        issue.append(getResources().getString(R.string.report_error_wrong_question) + " / ");
                                    }
                                    if (unclear_image.isSelected()) {
                                        issue.append(getResources().getString(R.string.report_error_unclear_image) + " / ");
                                    }

                                    //Log.e("issue for ", GlobalValues.student.getId() + " " + examid);

                                    issue.append(et_issueindetail.getText().toString());

                                    //Log.e("issues ", String.valueOf(issue));
                                    JSONObject object = new JSONObject();
                                    JSONObject mainobj = new JSONObject();


                                    try {
                                        object.put("studentid", GlobalValues.student.getId());
                                        object.put("examid", examid);
                                        object.put("questionid", mParam1.getId());
                                        object.put("Error", issue);
                                        mainobj.put("FilterParameter", object.toString());

                                        //Log.e("obj report ", mainobj.toString());

                                        ConnectionManager.getInstance(getActivity()).report_error_forquestions(mainobj.toString());

                                    } catch (Exception e) {
                                        reporterror("QuestionPracticeFragment", e.toString());
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getActivity(), getResources().getString(R.string.issuesubmitted), Toast.LENGTH_SHORT).show();
                                    if (alertDialog != null)
                                        alertDialog.cancel();

                                } catch (Exception e) {
                                    reporterror("QuestionPracticeFragment", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });

                        cancel_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    if (alertDialog != null)
                                        alertDialog.cancel();
                                } catch (Exception e) {
                                    reporterror("QuestionPracticeFragment", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        reporterror("QuestionPracticeFragment", e.toString());
                        e.printStackTrace();
                    }

                }
            });


            switchlang = (Switch) view.findViewById(R.id.switchlang);
            switchlang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.e("is marathi ", "is marathi " + b);
                    boolean ischange=true;
                    if (b) {
                        if(lang.equalsIgnoreCase("mr"))
                            ischange=false;
                        lang = "mr";
                    } else {
                        if(lang.equalsIgnoreCase("en"))
                            ischange=false;

                        lang = "en";
                    }
                    if(ischange)
                        changetxt();
                }
            });

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rec, new IntentFilter("QuestionUpdated"));
            layoutv1 = (LinearLayout) view.findViewById(R.id.layoutv1);
            lang = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            /*if(GlobalValues.LanguageChange_forQuestion == 1){
            if(lang.equalsIgnoreCase("mr"))
                lang="En";
            else lang="mr";
            }*/

            final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(mParam1.getId(), "que");
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        String originaldata = obj.getString(DatabaseHelper.IMAGESOURCE);
                        String offlinepath = obj.getString(DatabaseHelper.OFFLINEPATH);
                     //   Log.e("original ", " " + originaldata + " " + offlinepath);

                        File f=new File(offlinepath);
                        if(f.exists()) {
                            if(offlinepath!=null) {
                                if (offlinepath.length() > 0) {
                                    if (originaldata != null) {
                                        if (originaldata.length() > 0) {
                                            if (mParam1.getName() != null)
                                                mParam1.setName(mParam1.getName().replaceAll(originaldata, offlinepath));

                                            if (mParam1.getNameInMarathi() != null)
                                                mParam1.setNameInMarathi(mParam1.getNameInMarathi().replaceAll(originaldata, offlinepath));
                                        }
                                    }
                                }
                            }
                        }else {
                            if(offlinepath!=null) {
                                if (offlinepath.length() > 0) {
                                    if (originaldata != null) {
                                        if (originaldata.length() > 0) {
                                            if (mParam1.getName() != null)
                                                mParam1.setName(mParam1.getName().replaceAll(offlinepath, originaldata));

                                            if (mParam1.getNameInMarathi() != null)
                                                mParam1.setNameInMarathi(mParam1.getNameInMarathi().replaceAll(offlinepath, originaldata));
                                        }
                                    }
                                }
                            }

                        }

                      /*  if (lang.equals("mr")) {
                            mParam1.setNameInMarathi(mParam1.getNameInMarathi().replaceAll(originaldata, offlinepath));
                        } else {
                            mParam1.setName(mParam1.getName().replaceAll(originaldata, offlinepath));
                        }*/


                    } catch (Exception e) {
                        reporterror("QuestionFragment", e.toString());
                        e.printStackTrace();
                    }
                }


            }

            txt = (TextView) view.findViewById(R.id.textview_1);
            txtsub = (TextView) view.findViewById(R.id.textview_sub);
            tv_serialNo = (TextView) view.findViewById(R.id.tv_serialNo);

            //txtsub.setText("Q."+mParam1.getSrno()+" / "+mParam1.getSubjectname());
            txt.setTypeface(Fonter.getTypefaceregular(getActivity()));
            tv_serialNo.setTypeface(Fonter.getTypefaceregular(getActivity()));

            //Spanned spanned = Html.fromHtml(mParam1.getSrno() + "." + mParam1.getName(), this, null);
            Spanned spannedserialno = Html.fromHtml(mParam1.getSrno() + ".");
            Spanned spanned;

            if (lang.equals("mr")) {
                spanned = Html.fromHtml(style + mParam1.getNameInMarathi().trim(), this, null);

            } else {
                spanned = Html.fromHtml(style + mParam1.getName().trim(), this, null);

            }

            tv_serialNo.setText(spannedserialno);
            tv_serialNo.setPadding(10, 10, 2, 10);
            tv_serialNo.setTextColor(getResources().getColor(R.color.textcolor));

            txt.setText(spanned);
            txt.setPadding(2, 10, 10, 10);
            txt.setTextColor(getResources().getColor(R.color.textcolor));

            //if (mParam1.getIsTabletag().equalsIgnoreCase("1")) {
            txt.setVisibility(View.GONE);
            final WebView web = new WebView(getActivity());
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setAllowFileAccess(true);
            //web.loadDataWithBaseURL("file:///", style+mParam1.getSrno() + "." + mParam1.getName(), "text/html", "utf-8", null);
            web.setId(R.id.qname);
            if (lang.equals("mr")) {
                web.loadDataWithBaseURL("file:///", style + mParam1.getNameInMarathi(), "text/html", "utf-8", null);
            } else {
                web.loadDataWithBaseURL("file:///", style + mParam1.getName(), "text/html", "utf-8", null);
            }

            //parent.addView(web);

            RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            web.setLayoutParams(webViewParams);
            layoutv1.addView(web);
            //}
            Matcher matcher;
            Pattern pattern = Pattern.compile("src=([^>]*)>");
            if (lang.equals("mr")) {
                matcher = pattern.matcher(mParam1.getNameInMarathi());
            } else {
                matcher = pattern.matcher(mParam1.getName());
            }

            final ArrayList<QuestionURL> qdata = new ArrayList<>();
            while (matcher.find()) {
                qdata.add(new QuestionURL(matcher.group(1)));
            }

            if (qdata.size() > 0) {
                TextView txt = new TextView(getActivity());
                txt.setText(getResources().getString(R.string.enlarge_image));
                txt.setGravity(Gravity.RIGHT);
                txt.setTextColor(getResources().getColor(R.color.colorAccent));
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        opendialog(qdata);
                    }
                });
                layout.addView(txt);
            }

            if (Integer.parseInt(mParam1.getQuestionTypeId()) == 1) {
                addradioGroup(mParam1.getOptions());
            } else {
                for (int i = 0; i < mParam1.getOptions().size(); i++) {
                    Options t = mParam1.getOptions().get(i);
                    addCheckBox(t);
                }
            }


            if (lang.equalsIgnoreCase("mr"))
                switchlang.setChecked(true);
            else
                switchlang.setChecked(false);

        } catch (Exception e) {
            reporterror("QuestionFragment", e.toString());
            e.printStackTrace();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    private void addradioGroup(final ArrayList<Options> dataset) {
        try {
            String ans = "";
            rg = new RadioGroup(getActivity());
            final ArrayList<QuestionURL> qurldata = new ArrayList<>();
            rg.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < dataset.size(); i++) {
                final JSONArray arrayopt = DatabaseHelper.getInstance(getActivity()).getquestionurl(dataset.get(i).getId(), "ans");
                if (arrayopt.length() > 0) {
                    for (int j = 0; j < arrayopt.length(); j++) {
                        try {

                            JSONObject obj = arrayopt.getJSONObject(j);
                            String originaldata = obj.getString(DatabaseHelper.IMAGESOURCE);
                            String offlinepath = obj.getString(DatabaseHelper.OFFLINEPATH);

                           if(offlinepath==null)
                                offlinepath="";

                            if(originaldata==null)
                                originaldata="";




                         /*   if (lang.equals("mr")) {
                                dataset.get(i).setNameInMarathi(dataset.get(i).getNameInMarathi().replaceAll(originaldata, offlinepath));
                            } else {
                                dataset.get(i).setName(dataset.get(i).getName().replaceAll(originaldata, offlinepath));
                            }
*/
                            File f=new File(offlinepath);
                            if(f.exists()) {
                                if(offlinepath!=null) {
                                    if (offlinepath.length() > 0) {
                                        if (originaldata != null) {
                                            if (originaldata.length() > 0) {
                                                if (dataset.get(i).getName() != null)
                                                    dataset.get(i).setName(dataset.get(i).getName().replaceAll(originaldata, offlinepath));
                                            }
                                        }
                                    }
                                }

                                if(dataset.get(i).getNameInMarathi()!=null)
                                    dataset.get(i).setNameInMarathi(dataset.get(i).getNameInMarathi().replaceAll(originaldata, offlinepath));

                            }else {
                                if(offlinepath!=null) {
                                    if (offlinepath.length() > 0) {
                                        if (originaldata != null) {
                                            if (originaldata.length() > 0) {
                                                if (dataset.get(i).getName() != null)
                                                    dataset.get(i).setName(dataset.get(i).getName().replaceAll(offlinepath, originaldata));

                                                if(dataset.get(i).getNameInMarathi()!=null)
                                                    dataset.get(i).setNameInMarathi(dataset.get(i).getNameInMarathi().replaceAll(offlinepath, originaldata));
                                            }
                                        }
                                    }
                                }


                            }

                        }catch (OutOfMemoryError error){
                            reporterror("QuestionFragment", error.toString());
                        }catch (Exception e) {
                            reporterror("QuestionFragment", e.toString());
                            e.printStackTrace();
                        }
                    }



                }

                final Options opt = dataset.get(i);
                dataset.get(i).setIsTabletag(1);
                if (dataset.get(i).getIsTabletag() == 1) {
                    final LinearLayout parent = new LinearLayout(getActivity());
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    parent.setOrientation(LinearLayout.HORIZONTAL);
                    parent.setGravity(Gravity.CENTER);
                    parent.setBackgroundResource(R.drawable.card_radiobutton);

                    final WebView web = new WebView(getActivity());
                    web.getSettings().setJavaScriptEnabled(true);
                    // web.loadData(dataset.get(i).getName(), null, null);
                    web.getSettings().setAllowFileAccess(true);

                    if (lang.equals("mr")) {
                        web.loadDataWithBaseURL("file:///", style + dataset.get(i).getNameInMarathi(), "text/html", "utf-8", null);
                    } else {
                        web.loadDataWithBaseURL("file:///", style + dataset.get(i).getName(), "text/html", "utf-8", null);
                    }

                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    web.setLayoutParams(webViewParams);

                    web.setId(i + 100);
                    parent.setId(i);

                    if (flag_reviewcheck == 1) {
                        if (dataset.get(i).isSelected()) {
                            parent.setBackgroundResource(R.drawable.card_radiobutton);
                            //web.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            web.setBackgroundColor(getResources().getColor(R.color.white));
                            parent.setSelected(true);
                            web.setSelected(true);
                        }
                    } else {
                        if (dataset.get(i).isSelected()) {
                            parent.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                            //web.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            web.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            parent.setSelected(true);
                            web.setSelected(true);
                        }
                    }

                    parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                parent.setSelected(!parent.isSelected());
                                for (int h = 0; h < mParam1.getOptions().size(); h++) {

                                    dataset.get(h).setSelected(false);
                                    try {

                                        LinearLayout l = (LinearLayout) view.findViewById(h);
                                        l.setBackgroundResource(R.drawable.card_radiobutton);
                                        WebView w = (WebView) view.findViewById(h + 100);
                                        w.setBackgroundColor(getResources().getColor(R.color.white));

                                    } catch (Exception e) {
                                        reporterror("QuestionFragment", e.toString());
                                        e.printStackTrace();
                                    }
                                }
                                parent.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                                //web.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                web.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                opt.setSelected(parent.isSelected());
                                if (!mParam1.isIsanswered())
                                    ((ExamActivity) getActivity()).notifyqdatachanged(mParam1);

                                mParam1.setIsanswered(true);
                            } catch (Exception e) {
                                reporterror("QuestionFragment", e.toString());
                                e.printStackTrace();
                            }
                        }
                    });

                    web.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view1, MotionEvent motionEvent) {
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    try {
                                        m_DownTime = motionEvent.getEventTime(); //init time
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case MotionEvent.ACTION_UP:
                                    try{
                                    if (motionEvent.getEventTime() - m_DownTime <= MAX_TOUCH_DURATION) {
                                        for (int h = 0; h < mParam1.getOptions().size(); h++) {
                                            LinearLayout l = (LinearLayout) view.findViewById(h);
                                            WebView w = (WebView) view.findViewById(h + 100);
                                            l.setBackgroundResource(R.drawable.card_radiobutton);
                                            w.setBackgroundColor(getResources().getColor(R.color.white));
                                            dataset.get(h).setSelected(false);
                                        }
                                        opt.setSelected(!opt.isSelected());
                                        web.setSelected(opt.isSelected());
                                        //if (flag_setback == 1) {
                                        // LinearLayout l = (LinearLayout) view.findViewById(pos);
                                        parent.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                                        web.setBackgroundColor(getResources().getColor(R.color.selectedcolor));
                                        //}

                                        if (!mParam1.isIsanswered())
                                            ((ExamActivity) getActivity()).notifyqdatachanged(mParam1);

                                        mParam1.setIsanswered(true);
                                    }
                                    } catch (Exception e) {
                                        reporterror("QuestionFragment", e.toString());
                                        e.printStackTrace();
                                    }
                                    //On click action
                                    break;
                                default:
                                    break; //No-Op
                            }
                            return false;
                        }
                    });

                    /*web.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            web.setSelected(!web.isSelected());

                            for (int h = 0; h < mParam1.getOptions().size(); h++) {
                                LinearLayout l = (LinearLayout) view.findViewById(h);
                                WebView w = (WebView) view.findViewById(h + 100);
                                l.setBackgroundResource(R.drawable.card_radiobutton);
                                w.setBackgroundColor(getResources().getColor(R.color.white));
                                dataset.get(h).setSelected(false);
                                try {
                                    if ((v.getId() == w.getId()) && (event.getAction() == MotionEvent.ACTION_DOWN)) {
                                        pos = h;
                                        flag_setback = 1;

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (flag_setback == 1) {
                                LinearLayout l = (LinearLayout) view.findViewById(pos);
                                l.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                                web.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            }
                            opt.setSelected(web.isSelected());
                            if (!mParam1.isIsanswered())
                                ((ExamActivity) getActivity()).notifyqdatachanged(mParam1);

                            mParam1.setIsanswered(true);

                            return false;
                        }
                    });*/
                    TextView txt = new TextView(getActivity());
                    txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedtxt1));
                    txt.setTextColor(getResources().getColor(R.color.White));
                    txt.setTypeface(Fonter.getTypefacebold(getActivity()));
                    txt.setGravity(Gravity.CENTER_VERTICAL);
                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    param1.gravity = Gravity.CENTER;
                    txt.setPadding(7, 7, 0, 0);
                    txt.setLayoutParams(param1);
                    txt.setGravity(Gravity.CENTER);
                    txt.setText("" + dataset.get(i).getOptNumber());
                    parent.addView(txt);
                    parent.addView(web);
                    // parent.addView(web);
                    layout.addView(parent);
                } else {
                    final RadioButton rdbtn = new RadioButton(getActivity());
                    rdbtn.setPadding(30, 10, 15, 10);
                    rdbtn.setBackgroundResource(R.drawable.card_radiobutton);
                    rdbtn.setButtonDrawable(null);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rdbtn.setLayoutParams(params);

                    if (dataset.get(i).isSelected()) {
                        rdbtn.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                        rdbtn.setChecked(true);
                    }

                    rdbtn.setId(i);
                    Html.ImageGetter imggetter = new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                           /* LevelListDrawable d = new LevelListDrawable();
                            Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                            d.addLevel(0, 0, empty);
                            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                            new LoadImage(rdbtn, 1).execute(s, d);
                            return d;*/
                            LevelListDrawable d = new LevelListDrawable();
                            try {

                                Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                                d.addLevel(0, 0, empty);
                                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                                //  new LoadImage(txt, 0).execute(source, d);
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                Bitmap bitmap = BitmapFactory.decodeFile(source, bmOptions);
                                if (bitmap != null) {
                                    BitmapDrawable d1 = new BitmapDrawable(bitmap);
                                    d.addLevel(1, 1, d1);

                                    d.setBounds(0, 0, pxToDp(bitmap.getWidth()), pxToDp(bitmap.getHeight()));
                                    CharSequence t = ((RadioButton) rdbtn).getText();
                                    ((RadioButton) rdbtn).setText(t);
                                    d.setLevel(1);
                                }
                            } catch (Exception e) {
                                reporterror("QuestionFragment", e.toString());
                                e.printStackTrace();
                            }
                            // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                            return d;
                        }
                    };
                    JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(opt.getId(), "que");
                    if (array.length() > 0) {
                        ImageView img = new ImageView(getActivity());
                        img.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                    }

                    if (lang.equals("mr")) {

                        rdbtn.setText(Html.fromHtml(dataset.get(i).getNameInMarathi(), imggetter, null));
                    } else {
                        rdbtn.setText(Html.fromHtml(dataset.get(i).getName(), imggetter, null));
                    }

                    rg.addView(rdbtn);
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                                int pos = rg.getCheckedRadioButtonId();

                                for (int i = 0; i < dataset.size(); i++) {
                                    dataset.get(i).setSelected(false);

                                    rg.getChildAt(i).setBackgroundResource(R.drawable.card_radiobutton);
                                    if (pos == i) {
                                        rg.getChildAt(pos).setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                                        dataset.get(pos).isSelected();
                                    }
                                }

                                opt.setSelected(((RadioButton) view).isChecked());
                                if (!mParam1.isIsanswered())
                                    ((ExamActivity) getActivity()).notifyqdatachanged(mParam1);


                                mParam1.setIsanswered(true);
                            } catch (Exception e) {
                                reporterror("QuestionFragment", e.toString());
                                e.printStackTrace();
                            }
                        }
                    });


                   /* final  int selected=i;
                        Log.e("array ","array "+array1.length()+" "+array1.toString());
                        ImageView img=new ImageView(getActivity());
                        img.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("id ","id "+dataset.get(selected).getId());

                                if(qdata!=null)
                                    opendialog(qdata);
                            }
                        });
                        layout.addView(img);*/
                    //}
                }
               /* final JSONArray array1 = DatabaseHelper.getInstance(getActivity()).getquestionurl(dataset.get(i).getId(), "ans");
                if (array1.length() > 0) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<QuestionURL>>() {
                    }.getType();
                    ArrayList<QuestionURL> qdata = gson.fromJson(array1.toString(), type);
                    qurldata.addAll(qdata);
                }*/

                Pattern pattern = Pattern.compile("src=([^>]*)>");

                Matcher matcher;

                if (lang.equals("mr")) {
                    matcher = pattern.matcher(dataset.get(i).getNameInMarathi());
                } else {
                    matcher = pattern.matcher(dataset.get(i).getName());
                }

                while (matcher.find()) {
                    qurldata.add(new QuestionURL(matcher.group(1)));
                }
            }
            layout.addView(rg);
            if (qurldata.size() > 0) {
                TextView txt = new TextView(getActivity());
                txt.setText(getResources().getString(R.string.enlarge_image));
                txt.setGravity(Gravity.RIGHT);
                txt.setTextColor(getResources().getColor(R.color.colorAccent));
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        opendialog(qurldata);
                    }
                });
                layout.addView(txt);
            }
        } catch (Exception e) {
            reporterror("QuestionFragment", e.toString());
            e.printStackTrace();
        }
    }

    private void addCheckBox(final Options qopt) {
        try {
            final CheckBox cb = new CheckBox(getActivity());
            cb.setChecked(qopt.isSelected());
            Html.ImageGetter imggetter = new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                  /*  LevelListDrawable d = new LevelListDrawable();
                    Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                    d.addLevel(0, 0, empty);
                    d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                    new LoadImage(cb, 1).execute(s, d);
                    return d;*/

                    LevelListDrawable d = new LevelListDrawable();
                    try {

                        Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                        //  new LoadImage(txt, 0).execute(source, d);
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(source, bmOptions);
                        if (bitmap != null) {
                            BitmapDrawable d1 = new BitmapDrawable(bitmap);
                            d.addLevel(1, 1, d1);
                            d.setBounds(0, 0, pxToDp(bitmap.getWidth()), pxToDp(bitmap.getHeight()));
                            CharSequence t = ((CheckBox) cb).getText();
                            ((CheckBox) cb).setText(t);
                            d.setLevel(1);
                        }
                    } catch (Exception e) {
                        reporterror("QuestionFragment", e.toString());
                        e.printStackTrace();
                    }
                    // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                    return d;
                }
            };

            if (lang.equals("mr")) {
                cb.setText(Html.fromHtml(qopt.getNameInMarathi(), imggetter, null));
            } else {
                cb.setText(Html.fromHtml(qopt.getName(), imggetter, null));
            }

            cb.setTag(qopt);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    qopt.setSelected(b);
                }
            });
            cb.setTextColor(getResources().getColor(R.color.Black));
            layout.addView(cb);
        } catch (Exception e) {
            reporterror("QuestionFragment", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        try {
            Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
            //  new LoadImage(txt, 0).execute(source, d);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(source, bmOptions);
            if (bitmap != null) {
                BitmapDrawable d1 = new BitmapDrawable(bitmap);
                d.addLevel(1, 1, d1);
                d.setBounds(0, 0, pxToDp(bitmap.getWidth()), pxToDp(bitmap.getHeight()));
                CharSequence t = ((TextView) txt).getText();
                ((TextView) txt).setText(t);
                d.setLevel(1);
            }
        } catch (Exception e) {
            reporterror("QuestionFragment", e.toString());
            e.printStackTrace();
        }
        // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;
        View v;
        int type;

        public LoadImage(View v, int type) {
            super();
            this.v = v;
            this.type = type;
            // do stuff
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                reporterror("QuestionFragment", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try{
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, pxToDp(bitmap.getWidth()), pxToDp(bitmap.getHeight()));
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                switch (type) {
                    case 0:
                        CharSequence t = ((TextView) v).getText();
                        ((TextView) v).setText(t);
                        break;
                    case 1:
                        CharSequence t1 = ((RadioButton) v).getText();
                        ((RadioButton) v).setText(t1);
                        break;
                    case 2:
                        CharSequence t2 = ((CheckBox) v).getText();
                        ((CheckBox) v).setText(t2);
                        break;
                }

            }
            } catch (Exception e) {
                reporterror("QuestionFragment", e.toString());
                e.printStackTrace();
            }
        }
    }

    public static int pxToDp(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        // return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void opendialog(final ArrayList<QuestionURL> dataset) {
        /*if (!((Activity) getActivity()).isFinishing()) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.list_item_questionurl, null);
            alert.setView(alertLayout);
            RecyclerView mRecyclerview = alertLayout.findViewById(R.id.recyclerview_1);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerview.setLayoutManager(linearLayoutManager);
            QuestionURLDataAdapter adapter = new QuestionURLDataAdapter(getActivity(), dataset);
            mRecyclerview.setAdapter(adapter);

            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
        } else {
            Log.e("no activity ", "no activity dialog");
        }*/

        try{
        ((Activitycommon) getActivity()).gotoload_image(dataset, mParam1.getSrno());
        } catch (Exception e) {
            reporterror("QuestionFragment", e.toString());
            e.printStackTrace();
        }
    }

    BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (mParam1.getId() == intent.getIntExtra("Qid", 0)) {
                    if (rg != null) {
                        int radioButtonID = rg.getCheckedRadioButtonId();
                        RadioButton radioButton = rg.findViewById(radioButtonID);
                        if (radioButton != null)
                            radioButton.setChecked(false);
                    }

                    //  for (int k = 0; k < question.size(); k++) {

                    if (mParam1.isIsreview()) {
                        flag_reviewcheck = 1;
                        for (int c = 0; c < mParam1.getOptions().size(); c++) {
                            LinearLayout l = view.findViewById(c);
                            WebView w = view.findViewById(c + 100);
                            // if (mParam1.getOptions().get(c).isSelected()) {
                            l.setBackgroundResource(R.drawable.card_radiobutton);
                            w.setBackgroundColor(getResources().getColor(R.color.white));
                            mParam1.getOptions().get(c).setSelected(false);
                            //  }
                        }
                    }
                    //}
                }
            } catch (Exception e) {
                reporterror("QuestionFragment", e.toString());
                e.printStackTrace();
            }
        }
    };

    public int getos() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public String getmodel() {
        return Build.MODEL + " " + Build.BRAND;
    }


    public void reporterror(String classname, String error) {
        try {
            JSONObject obj = new JSONObject();
            if (GlobalValues.student != null)
                obj.put("email", GlobalValues.student.getMobile());
            else obj.put("email", "");
            obj.put("osversion", getmodel() + " " + getos());
            obj.put("errordetail", error.replaceAll("'", ""));
            obj.put("appname", "Target Educare Peak");
            obj.put("activityname", classname);
            ConnectionManager.getInstance(getActivity()).reporterror(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changetxt() {
        WebView web = view.findViewById(R.id.qname);
        if (web != null) {
            if (!lang.equals("mr")) {
                if(mParam1.getName().equalsIgnoreCase("null"))
                {
                    switchlang.setChecked(true);
                    lang="mr";
                    Toast.makeText(getActivity(),R.string.nodata,Toast.LENGTH_LONG).show();
                    return;
                }else
                web.loadDataWithBaseURL("file:///", style + mParam1.getName(), "text/html", "utf-8", null);
            } else {
                if(mParam1.getNameInMarathi().equalsIgnoreCase("null"))
                {

                    lang="en";
                    switchlang.setChecked(false);
                    Toast.makeText(getActivity(),R.string.nodata,Toast.LENGTH_LONG).show();
                    return;
                }else
                web.loadDataWithBaseURL("file:///", style + mParam1.getNameInMarathi(), "text/html", "utf-8", null);
            }
        }

        for (int i = 0; i < mParam1.getOptions().size(); i++) {
            // LinearLayout l = (LinearLayout) view.findViewById(i);
            WebView w = (WebView) view.findViewById(i + 100);
            if (!lang.equals("mr")) {
                w.loadDataWithBaseURL("file:///", style + mParam1.getOptions().get(i).getName(), "text/html", "utf-8", null);
            } else
                w.loadDataWithBaseURL("file:///", style + mParam1.getOptions().get(i).getNameInMarathi(), "text/html", "utf-8", null);

        }
        mParam1.setIsTabletagexplanation("1");

        try {
            WebView expweb = view.findViewById(R.id.webxml);

            if (expweb != null) {
                if (!lang.equalsIgnoreCase("mr")) {
                    Log.e("view is null ", "view is marathi " + mParam1.getExplanation());
                    expweb.loadDataWithBaseURL("file:///", style + mParam1.getExplanation(), "text/html", "utf-8", null);
                } else {
                    Log.e("view is null ", "view is english " + mParam1.getExplInMarathi());
                    expweb.loadDataWithBaseURL("file:///", style + mParam1.getExplInMarathi(), "text/html", "utf-8", null);
                }
            } else {
                Log.e("view is null ", "view is null ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
