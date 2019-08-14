package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.airbnb.lottie.L;
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

import static com.targeteducare.AnswersheetActivity.exam;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerFragment extends Fragment implements Html.ImageGetter {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private Question mParam1;
    private String mParam2;
    TextView txt;
    LinearLayout layout;
    TextView tv_serialNo;
    LinearLayout layoutv1;
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
    String lang = "";
    TextView report_error;
    Switch switchlang;
    TextView question_unclear, wrong_answer, wrong_question, unclear_image;
    EditText et_issueindetail;
    ImageView cancel_dialog;
    Button submit_issue;
    int examid = 0;
    int pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
    StringBuilder issue = new StringBuilder();

    public AnswerFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static AnswerFragment newInstance(Question param1, String param2) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Question) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_question, container, false);
        try {
            Log.e("layout ", "AnswerFragment");
            final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(mParam1.getId(), "que");

            lang = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");

            report_error = (TextView) v.findViewById(R.id.iv_reporterror);

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
                                    reporterror("AnswerFragment", e.toString());
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
                                    reporterror("AnswerFragment", e.toString());
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
                                    reporterror("AnswerFragment", e.toString());
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
                                    reporterror("AnswerFragment", e.toString());
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
                                        reporterror("AnswerFragment", e.toString());
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getActivity(), getResources().getString(R.string.issuesubmitted), Toast.LENGTH_SHORT).show();
                                    if (alertDialog != null)
                                        alertDialog.cancel();

                                } catch (Exception e) {
                                    reporterror("AnswerFragment", e.toString());
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
                                    reporterror("AnswerFragment", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        reporterror("AnswerFragment", e.toString());
                        e.printStackTrace();
                    }

                }
            });



            switchlang = (Switch) v.findViewById(R.id.switchlang);
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


            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        String originaldata = obj.getString(DatabaseHelper.IMAGESOURCE);
                        String offlinepath = obj.getString(DatabaseHelper.OFFLINEPATH);
                        File f = new File(offlinepath);
                        if (f.exists()) {
                            if (offlinepath != null) {
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
                        } else {
                            if (offlinepath != null) {
                                if (offlinepath.length() > 0) {
                                    if (originaldata != null) {
                                        if (originaldata.length() > 0) {
                                            if(mParam1.getName()!=null)
                                            mParam1.setName(mParam1.getName().replaceAll(offlinepath, originaldata));

                                            if(mParam1.getNameInMarathi()!=null)
                                            mParam1.setNameInMarathi(mParam1.getNameInMarathi().replaceAll(offlinepath, originaldata));
                                        }
                                    }
                                }
                            }
                        }
                    /*    mParam1.setName(mParam1.getName().replaceAll(originaldata, offlinepath));
                        mParam1.setNameInMarathi(mParam1.getNameInMarathi().replaceAll(originaldata, offlinepath));*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            layout = (LinearLayout) v.findViewById(R.id.layout);
            txt = (TextView) v.findViewById(R.id.textview_1);
            tv_serialNo = (TextView) v.findViewById(R.id.tv_serialNo);
            layoutv1 = (LinearLayout) v.findViewById(R.id.layoutv1);
            txt.setTypeface(Fonter.getTypefaceregular(getActivity()));
            tv_serialNo.setTypeface(Fonter.getTypefaceregular(getActivity()));

            Spanned spannedserialno = Html.fromHtml(mParam1.getSrno() + ".");
            Spanned spanned = Html.fromHtml(style + mParam1.getName(), this, null);
            final LinearLayout parent = new LinearLayout(getActivity());
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);
            tv_serialNo.setText(spannedserialno);
            tv_serialNo.setPadding(10, 10, 10, 10);
            tv_serialNo.setTextColor(getResources().getColor(R.color.textcolor));
            txt.setText(spanned);
            txt.setPadding(10, 10, 10, 10);
            //   mParam1.setIsTabletag("1");
            //if (mParam1.getIsTabletag().equalsIgnoreCase("1")) {
            txt.setVisibility(View.GONE);
            final WebView web = new WebView(getActivity());
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setAllowFileAccess(true);
            web.setId(R.id.qname);
            if (!lang.equalsIgnoreCase("mr"))
                web.loadDataWithBaseURL("file:///", style + mParam1.getName(), "text/html", "utf-8", null);
            else
                web.loadDataWithBaseURL("file:///", style + mParam1.getNameInMarathi(), "text/html", "utf-8", null);
            //parent.addView(web);
            layoutv1.addView(web);
            RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
            webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //}
            //final ArrayList<QuestionURL> qdata = checkdata();
            addenlargedata(checkdata(mParam1.getName()));
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
            reporterror("AnswerFragment", e.toString());
        }
        return v;
    }

    public void addenlargedata(final ArrayList<QuestionURL> qdata) {
        try {
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
        } catch (Exception e) {
            reporterror("AnswerFragment", e.toString());
        }
    }

    public ArrayList<QuestionURL> checkdata(String sdata) {
        ArrayList<QuestionURL> qdata = new ArrayList<>();
        try {
            Pattern pattern = Pattern.compile("src=([^>]*)>");
            Matcher matcher = pattern.matcher(sdata);
            while (matcher.find()) {
                qdata.add(new QuestionURL(matcher.group(1)));
            }
        } catch (Exception e) {
            reporterror("AnswerFragment", e.toString());
        }
        return qdata;
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
            int isanswer = 0;//0 not answered, 1 correct,2 wrong
            final ArrayList<QuestionURL> qurldata = new ArrayList<>();
            RadioGroup rg = new RadioGroup(getActivity());
            rg.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < dataset.size(); i++) {
                final JSONArray arrayopt = DatabaseHelper.getInstance(getActivity()).getquestionurl(dataset.get(i).getId(), "ans");
                if (arrayopt.length() > 0) {
                    for (int j = 0; j < arrayopt.length(); j++) {
                        try {
                            JSONObject obj = arrayopt.getJSONObject(j);
                            String originaldata = obj.getString(DatabaseHelper.IMAGESOURCE);
                            String offlinepath = obj.getString(DatabaseHelper.OFFLINEPATH);
                           /* dataset.get(i).setName(dataset.get(i).getName().replaceAll(originaldata, offlinepath));
                            dataset.get(i).setNameInMarathi(dataset.get(i).getNameInMarathi().replaceAll(originaldata, offlinepath));*/

                            File f = new File(offlinepath);
                            if (f.exists()) {
                                if (offlinepath != null) {
                                    if (offlinepath.length() > 0) {
                                        if (originaldata != null) {
                                            if (originaldata.length() > 0) {
                                                if(dataset.get(i).getName()!=null)
                                                    dataset.get(i).setName(dataset.get(i).getName().replaceAll(originaldata, offlinepath));

                                                if(dataset.get(i).getNameInMarathi()!=null)
                                                    dataset.get(i).setNameInMarathi(dataset.get(i).getNameInMarathi().replaceAll(originaldata, offlinepath));

                                            }
                                        }
                                    }
                                }
                            } else {
                                if (offlinepath != null) {
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


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                dataset.get(i).setIsTabletag(1);
                if (dataset.get(i).getIsTabletag() == 1) {
                    /*LinearLayout parent = new LinearLayout(getActivity());
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.HORIZONTAL);
                    final RadioButton rdbtn = new RadioButton(new ContextThemeWrapper(getActivity(), R.style.radionbutton), null, 0);
                    rdbtn.setPadding(30, 10, 15, 10);
                    rdbtn.setBackgroundResource(R.drawable.card_radiobutton);
                    rdbtn.setButtonDrawable(null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(0, 0, 0, 10);
                    rdbtn.setLayoutParams(params);
                    if (dataset.get(i).isSelected()) {
                        rdbtn.setChecked(true);
                    }

                    if (dataset.get(i).isAnswer()) {
                        rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.answered_questionlight));
                    }

                    if (!dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 2;
                        //  rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.notanswered_questionlight));
                    } else if (dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 1;
                    }
                    parent.addView(rdbtn);
                    final WebView web = new WebView(getActivity());

                    web.getSettings().setJavaScriptEnabled(true);
                    web.getSettings().setAllowFileAccess(true);
                    web.loadDataWithBaseURL("file:///", style + dataset.get(i).getName(), "text/html", "utf-8", null);
                    //   web.loadData(dataset.get(i).getName(), null, null);
                    parent.addView(web);
                    layout.addView(parent);
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(
                            300, 100);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);*/

                    final LinearLayout parent = new LinearLayout(getActivity());
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.HORIZONTAL);
                    parent.setBackgroundResource(R.drawable.card_radiobutton);

                    final WebView web = new WebView(getActivity());
                    web.getSettings().setJavaScriptEnabled(true);
                    // web.loadData(dataset.get(i).getName(), null, null);
                    web.getSettings().setAllowFileAccess(true);
                    if (lang.equals("mr")) {
                        web.loadDataWithBaseURL("file:///", style + dataset.get(i).getNameInMarathi(), "text/html", "utf-8", null);
                    } else
                        web.loadDataWithBaseURL("file:///", style + dataset.get(i).getName(), "text/html", "utf-8", null);
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    web.setLayoutParams(webViewParams);

                    web.setId(i + 100);
                    parent.setId(i);

                    /*if (dataset.get(i).isSelected()) {
                        parent.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                        web.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        parent.setSelected(true);
                    }*/

                    if (dataset.get(i).isAnswer()) {
                        parent.setBackgroundResource(R.drawable.card_webview_onclicked);
                        web.setBackgroundColor(getContext().getResources().getColor(R.color.answered_questionlight));
                    }

                    if (!dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 2;
                        parent.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                        web.setBackgroundColor(getContext().getResources().getColor(R.color.lightgray));
                        //  rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.notanswered_questionlight));
                    } else if (dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 1;
                    }
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
                    layout.addView(parent);
                } else {
                    //  final RadioButton rdbtn = new RadioButton(getActivity());
                    final RadioButton rdbtn = new RadioButton(new ContextThemeWrapper(getActivity(), R.style.radionbutton), null, 0);
                    ;
                    //  rdbtn.setPadding(10,10,10,10);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(10, 10, 10, 10);
                    rdbtn.setLayoutParams(params);
                    rdbtn.setEnabled(false);
                    rdbtn.setPadding(30, 10, 15, 10);
                    rdbtn.setBackgroundResource(R.drawable.card_radiobutton);
                    rdbtn.setButtonDrawable(null);
                    final Options opt = dataset.get(i);
                    rdbtn.setId(i);
                    //  rdbtn.setTextSize(14);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    if (dataset.get(i).isSelected()) {
                        rdbtn.setChecked(true);
                        rdbtn.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                    }

                    if (dataset.get(i).isAnswer()) {
                        if (exam.isInstantExamResultWithAns()) {
                            //rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.answered_questionlight));
                            rdbtn.setBackgroundResource(R.drawable.card_radiobutton_onskipped);
                        }
                    }

                    if (!dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 2;
                        rdbtn.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                        txt.setBackgroundColor(getContext().getResources().getColor(R.color.lightgray));
                        //  rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.notanswered_questionlight));
                    } else if (dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 1;
                    }

                    Html.ImageGetter imggetter = new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                        /*    LevelListDrawable d = new LevelListDrawable();
                            Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                            d.addLevel(0, 0, empty);
                            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                            new AnswerFragment.LoadImage(rdbtn, 1).execute(s, d);
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
                                e.printStackTrace();
                            }
                            // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                            return d;

                        }
                    };
                    rdbtn.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    if (lang.equalsIgnoreCase("mr"))
                        rdbtn.setText(Html.fromHtml(dataset.get(i).getNameInMarathi(), imggetter, null));
                    else rdbtn.setText(Html.fromHtml(dataset.get(i).getName(), imggetter, null));

                    rg.addView(rdbtn);
                }
                qurldata.addAll(checkdata(dataset.get(i).getName()));
                /*Pattern pattern = Pattern.compile("src=([^>]*)>");
                Matcher matcher = pattern.matcher(dataset.get(i).getName());
                while (matcher.find()) {
                    qurldata.add(new QuestionURL(matcher.group(1)));
                }*/
              /*  final JSONArray array1 = DatabaseHelper.getInstance(getActivity()).getquestionurl(dataset.get(i).getId(), "ans");
                if (array1.length() > 0) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<QuestionURL>>() {
                    }.getType();
                    ArrayList<QuestionURL> qdata = gson.fromJson(array1.toString(), type);
                    qurldata.addAll(qdata);
                }*/

            }
            layout.addView(rg);
            addenlargedata(qurldata);
          /*  if (qurldata.size() > 0) {
                TextView txt = new TextView(getActivity());
                txt.setText("enlarge Image");
                txt.setGravity(Gravity.RIGHT);
                txt.setTextColor(getResources().getColor(R.color.colorAccent));
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        opendialog(qurldata);
                    }
                });
                layout.addView(txt);
            }*/

            TextView txt = new TextView(getActivity());
            txt.setTypeface(Fonter.getTypefaceregular(getActivity()));
            txt.setTextColor(getResources().getColor(R.color.textcolor));
            txt.setPadding(20, 20, 20, 20);

            switch (isanswer) {
                case 0:
                    txt.setText(getResources().getString(R.string.not_answered));
                    txt.setTextColor(getContext().getResources().getColor(R.color.white));
                    //txt.setBackgroundColor(getResources().getColor(R.color.notanswered_questionlight));
                    txt.setBackgroundResource(R.drawable.card_radiobutton_notattempted);
                    break;
                case 1:
                    txt.setText(getResources().getString(R.string.correct_answer));
                    //txt.setBackgroundColor(getResources().getColor(R.color.answered_questionlight));
                    txt.setBackgroundResource(R.drawable.card_radiobutton_onskipped);
                    break;
                case 2:
                    txt.setText(getResources().getString(R.string.wrong_answer));
                    //txt.setBackgroundColor(getResources().getColor(R.color.wrongswered_questionlight));
                    txt.setBackgroundResource(R.drawable.card_radio_button_wronganswer);
                    break;
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            // txt.setPadding(40, 40, 40, 40);
            txt.setLayoutParams(params);
            layout.addView(txt);

            //uncomment this or put a new flag for showing explanantion
            // if (exam.isInstantExamResultWithAns()) {
            final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(mParam1.getId(), "explanation");
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        String originaldata = obj.getString(DatabaseHelper.IMAGESOURCE);
                        String offlinepath = obj.getString(DatabaseHelper.OFFLINEPATH);
                          /*  if (lang.equalsIgnoreCase("mr"))
                                mParam1.setExplanation(mParam1.getExplInMarathi().replaceAll(originaldata, offlinepath));
                            else
                                mParam1.setExplanation(mParam1.getExplanation().replaceAll(originaldata, offlinepath));*/

                        File f = new File(offlinepath);

                        if (f.exists()) {

                            if (offlinepath != null) {
                                if (offlinepath.length() > 0) {
                                    if (originaldata != null) {
                                        if (originaldata.length() > 0) {


                                            if (!lang.equals("mr")) {
                                                mParam1.setExplanation(mParam1.getExplanation().replaceAll(originaldata, offlinepath));
                                                mParam1.setExplInMarathi(mParam1.getExplInMarathi().replaceAll(originaldata, offlinepath));
                                            }
                                            else {
                                                mParam1.setExplanation(mParam1.getExplanation().replaceAll(originaldata, offlinepath));
                                                mParam1.setExplInMarathi(mParam1.getExplInMarathi().replaceAll(originaldata, offlinepath));
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (offlinepath != null) {
                                if (offlinepath.length() > 0) {
                                    if (originaldata != null) {
                                        if (originaldata.length() > 0) {
                                          if(mParam1.getExplanation()!=null)
                                                mParam1.setExplanation(mParam1.getExplanation().replaceAll(offlinepath, originaldata));

                                          if(mParam1.getExplInMarathi()!=null)
                                                mParam1.setExplInMarathi(mParam1.getExplInMarathi().replaceAll(offlinepath, originaldata));

                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mParam1.setIsTabletagexplanation("1");
            if (mParam1.getIsTabletagexplanation().equalsIgnoreCase("1")) {
                //   txt.setVisibility(View.GONE);
                final WebView web = new WebView(getActivity());
                web.getSettings().setJavaScriptEnabled(true);
                web.getSettings().setAllowFileAccess(true);
                web.setId(R.id.webxml);
                if (lang.equalsIgnoreCase("mr"))
                    web.loadDataWithBaseURL("file:///", style + mParam1.getExplInMarathi(), "text/html", "utf-8", null);
                else
                    web.loadDataWithBaseURL("file:///", style + mParam1.getExplanation(), "text/html", "utf-8", null);
                layout.addView(web);
                RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
                webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else {
                final TextView txtexplanation = new TextView(getActivity());
                txtexplanation.setLayoutParams(params);
                txtexplanation.setPadding(10, 10, 10, 10);
                Html.ImageGetter imggetter = new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                     /*   LevelListDrawable d = new LevelListDrawable();
                        Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                        new AnswerFragment.LoadImage(txtexplanation, 0).execute(s, d);
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
                                CharSequence t = ((TextView) txtexplanation).getText();
                                ((TextView) txtexplanation).setText(t);
                                d.setLevel(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                        return d;

                    }
                };
                txtexplanation.setText(Html.fromHtml(mParam1.getExplanation(), imggetter, null));
                Log.e("expl ", ""+Html.fromHtml(mParam1.getExplanation(), imggetter, null));
                layout.addView(txtexplanation);
            }
            addenlargedata(checkdata(mParam1.getExplanation()));

               /* final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(mParam1.getId(), "elplanation");
                if (array.length() > 0) {
                    Log.e("array ", "array " + array.length() + " " + array.toString());
                    TextView txt1 = new TextView(getActivity());
                    txt1.setText("enlarge Image");
                    txt1.setGravity(Gravity.RIGHT);
                    txt1.setTextColor(getResources().getColor(R.color.colorAccent));
                    txt1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<QuestionURL>>() {
                            }.getType();
                            ArrayList<QuestionURL> qdata = gson.fromJson(array.toString(), type);
                            if (qdata != null)
                                opendialog(qdata);
                        }
                    });
                    layout.addView(txt1);
                }*/
            // }


            /*for (int i = 0; i < dataset.size(); i++) {
                if (dataset.get(i).getIsTabletag() == 1) {
                    LinearLayout parent = new LinearLayout(getActivity());
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.HORIZONTAL);
                    final RadioButton rdbtn = new RadioButton(getActivity());
                    if (dataset.get(i).isSelected()) {
                        rdbtn.setChecked(true);
                    }

                    if (dataset.get(i).isAnswer()) {
                        rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.answered_questionlight));
                    }

                    if (!dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 2;
                        //  rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.notanswered_questionlight));
                    } else if (dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 1;
                    }
                    parent.addView(rdbtn);
                    final WebView web = new WebView(getActivity());

                    web.getSettings().setJavaScriptEnabled(true);
                    web.loadData(dataset.get(i).getName()
                            ,
                            null, null);
                    parent.addView(web);
                    layout.addView(parent);
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(
                            300, 100);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }else {
                    final RadioButton rdbtn = new RadioButton(getActivity());
                    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rdbtn.setLayoutParams(params);
                    rdbtn.setEnabled(false);
                    final Options opt = dataset.get(i);
                    rdbtn.setId(i);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    Log.e("isanswerdata ", "isanswer " + dataset.get(i).isSelected() + " " + dataset.get(i).isAnswer());
                    if (dataset.get(i).isSelected()) {
                        rdbtn.setChecked(true);
                    }

                    if (dataset.get(i).isAnswer()) {
                        rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.answered_questionlight));
                    }

                    if (!dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 2;
                        //  rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.notanswered_questionlight));
                    } else if (dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 1;
                    }

                    Html.ImageGetter imggetter = new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String s) {
                            LevelListDrawable d = new LevelListDrawable();
                            Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                            d.addLevel(0, 0, empty);
                            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                            new AnswerFragment.LoadImage(rdbtn, 1).execute(s, d);
                            return d;
                        }
                    };
                    rdbtn.setText(Html.fromHtml(dataset.get(i).getName(), imggetter, null));
                    rg.addView(rdbtn);
                }
                TextView txt=new TextView(getActivity());
                txt.setTypeface(Fonter.getTypefaceregular(getActivity()));
                txt.setTextColor(getResources().getColor(R.color.textcolor));

                switch (isanswer)
                {
                    case 0:
                        txt.setText("Not answer");
                        txt.setBackgroundColor(getResources().getColor(R.color.notanswered_questionlight));
                        break;
                    case 1:
                        txt.setText("Your answer is correct");
                        txt.setBackgroundColor(getResources().getColor(R.color.answered_questionlight));
                        break;
                    case 2:
                        txt.setText("Your answer is wrong");
                        txt.setBackgroundColor(getResources().getColor(R.color.wrongswered_questionlight));
                        break;
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin=10;
                txt.setPadding(10,10,10,10);
                txt.setLayoutParams(params);
                layout.addView(txt);

                final TextView txtexplanation=new TextView(getActivity());
                txtexplanation.setLayoutParams(params);
                txtexplanation.setPadding(10,10,10,10);
                Html.ImageGetter imggetter = new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String s) {
                        LevelListDrawable d = new LevelListDrawable();
                        Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                        new AnswerFragment.LoadImage(txtexplanation, 0).execute(s, d);
                        return d;
                    }
                };
                txtexplanation.setText(Html.fromHtml(mParam1.getExplanation(), imggetter, null));
                layout.addView(txtexplanation);

                final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(mParam1.getId(), "elplanation");
                if (array.length() > 0) {
                    Log.e("array ", "array " + array.length() + " " + array.toString());
                    TextView txt1=new TextView(getActivity());
                    txt1.setText("enlarge Image");
                    txt1.setGravity(Gravity.RIGHT);
                    txt1.setTextColor(getResources().getColor(R.color.colorAccent));
                    txt1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<QuestionURL>>() {
                            }.getType();
                            ArrayList<QuestionURL> qdata = gson.fromJson(array.toString(), type);
                            if (qdata != null)
                                opendialog(qdata);
                        }
                    });
                    layout.addView(txt1);
                }
                final JSONArray array1 = DatabaseHelper.getInstance(getActivity()).getquestionurl(dataset.get(i).getId(), "ans");
                if (array1.length() > 0) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<QuestionURL>>() {
                    }.getType();
                    ArrayList<QuestionURL> qdata = gson.fromJson(array1.toString(), type);
                    qurldata.addAll(qdata);
                }
            }*/


            //layout.addView(rg);

          /*  if (qurldata.size() > 0) {
                TextView txt=new TextView(getActivity());
                txt.setText("enlarge Image");
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

            if (qurldata.size() > 0) {
                TextView txt=new TextView(getActivity());
                txt.setText("enlarge Image");
                txt.setGravity(Gravity.RIGHT);
                txt.setTextColor(getResources().getColor(R.color.colorAccent));
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        opendialog(qurldata);
                    }
                });
                layout.addView(txt);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCheckBox(final Options qopt) {
        try {
            final CheckBox cb = new CheckBox(getActivity());
            cb.setChecked(qopt.isSelected());
            Html.ImageGetter imggetter = new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String s) {
                    LevelListDrawable d = new LevelListDrawable();
                    Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                    d.addLevel(0, 0, empty);
                    d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                    new LoadImage(cb, 1).execute(s, d);
                    return d;
                }
            };
            cb.setText(Html.fromHtml(qopt.getName(), imggetter, null));
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
            reporterror("Answerfragment", e.toString());
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
      /*  LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new AnswerFragment.LoadImage(txt, 0).execute(source, d);
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
                CharSequence t = ((TextView) txt).getText();
                ((TextView) txt).setText(t);
                d.setLevel(1);
            }
        } catch (Exception e) {
            reporterror("Answerfragment", e.toString());
            e.printStackTrace();
        }
        // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
        return d;
    }

    public static int pxToDp(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        // return (int) (px / Resources.getSystem().getDisplayMetrics().density);
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
                reporterror("Answerfragment", e.toString());
                e.printStackTrace();
            } catch (MalformedURLException e) {
                reporterror("Answerfragment", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                reporterror("Answerfragment", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                if (bitmap != null) {
                    BitmapDrawable d = new BitmapDrawable(bitmap);
                    mDrawable.addLevel(1, 1, d);
                    mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
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
                reporterror("Answerfragment", e.toString());
            }
        }
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
        /*final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
        dialog.show();*/
        ((Activitycommon) getActivity()).gotoload_image(dataset, mParam1.getSrno());
    }

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
        WebView web = v.findViewById(R.id.qname);
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
            } else
            if(mParam1.getNameInMarathi().equalsIgnoreCase("null"))
            {
                lang="en";
                switchlang.setChecked(false);
                Toast.makeText(getActivity(),R.string.nodata,Toast.LENGTH_LONG).show();
                return;
            }else
                web.loadDataWithBaseURL("file:///", style + mParam1.getNameInMarathi(), "text/html", "utf-8", null);
        }

        for (int i = 0; i < mParam1.getOptions().size(); i++) {
            // LinearLayout l = (LinearLayout) view.findViewById(i);
            WebView w = (WebView) v.findViewById(i + 100);
            if (!lang.equals("mr")) {
                w.loadDataWithBaseURL("file:///", style + mParam1.getOptions().get(i).getName(), "text/html", "utf-8", null);
            } else
                w.loadDataWithBaseURL("file:///", style + mParam1.getOptions().get(i).getNameInMarathi(), "text/html", "utf-8", null);

        }
        mParam1.setIsTabletagexplanation("1");

        try {
            WebView expweb = v.findViewById(R.id.webxml);

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
