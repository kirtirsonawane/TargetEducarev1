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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.targeteducare.Adapter.QuestionURLDataAdapter;
import com.targeteducare.Classes.Options;
import com.targeteducare.Classes.Question;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
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
 * Use the {@link AnswerPracticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerPracticeFragment extends Fragment implements Html.ImageGetter {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private Question mParam1;
    private String mParam2;
    TextView txt;
    LinearLayout layout;

    String style="<style type=\"text/css\">\n" +
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

    public AnswerPracticeFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static AnswerPracticeFragment newInstance(Question param1, String param2) {
        AnswerPracticeFragment fragment = new AnswerPracticeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("I am in ", "AnswerPracticeFragment");
        try {
            if (getArguments() != null) {
                mParam1 = (Question) getArguments().getSerializable(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }catch (Exception e){
            reporterror("Answerfragment",e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_practice, container, false);
        try {
            final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(mParam1.getId(), "que");
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        String originaldata = obj.getString(DatabaseHelper.IMAGESOURCE);
                        String offlinepath = obj.getString(DatabaseHelper.OFFLINEPATH);
                        Log.e("original ", " " + originaldata + " " + offlinepath);
                        mParam1.setName(mParam1.getName().replaceAll(originaldata, offlinepath));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.e("qname ", " qname " + mParam1.getName());
            }

            layout = (LinearLayout) v.findViewById(R.id.layout);
            txt = (TextView) v.findViewById(R.id.textview_1);
            txt.setTypeface(Fonter.getTypefaceregular(getActivity()));
            Spanned spanned = Html.fromHtml(mParam1.getSrno() + "." + mParam1.getName(), this, null);
            txt.setText(spanned);
            txt.setPadding(10, 10, 10, 10);
            //   mParam1.setIsTabletag("1");
            if (mParam1.getIsTabletag().equalsIgnoreCase("1")) {
                txt.setVisibility(View.GONE);
                final WebView web = new WebView(getActivity());
                web.getSettings().setJavaScriptEnabled(true);
                web.getSettings().setAllowFileAccess(true);
                web.loadDataWithBaseURL("file:///", style + mParam1.getSrno() + "." + mParam1.getName(), "text/html", "utf-8", null);
                //parent.addView(web);
                layout.addView(web);
                RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
                webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            //final ArrayList<QuestionURL> qdata = checkdata();
            addenlargedata(checkdata(mParam1.getName()));

      /*  if (qdata.size() > 0) {
            TextView txt = new TextView(getActivity());
            txt.setText("enlarge Image");
            txt.setGravity(Gravity.RIGHT);
            txt.setTextColor(getResources().getColor(R.color.colorAccent));
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    opendialog(qdata);
                }
            });
            layout.addView(txt);
        }*/

        /*final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl1(mParam1.getId(), "que");
        if (array.length() > 0) {
            Log.e("array ", "array " + array.length() + " " + array.toString());
            TextView txt=new TextView(getActivity());
            txt.setText("enlarge Image");
            txt.setGravity(Gravity.RIGHT);
            txt.setTextColor(getResources().getColor(R.color.colorAccent));
            txt.setOnClickListener(new View.OnClickListener() {
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
            layout.addView(txt);
        }*/
            if (Integer.parseInt(mParam1.getQuestionTypeId()) == 1) {
                addradioGroup(mParam1.getOptions());
            } else {
                for (int i = 0; i < mParam1.getOptions().size(); i++) {
                    Options t = mParam1.getOptions().get(i);
                    addCheckBox(t);
                }
            }
        }catch (Exception e)
        {
            reporterror("Anserfrag",e.toString());
        }
        return v;
    }

    public void addenlargedata(final ArrayList<QuestionURL> qdata){
        try{
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
        }catch (Exception e)
        {
            reporterror("Anserfrag",e.toString());
        }
    }

    public ArrayList<QuestionURL> checkdata(String sdata){
        ArrayList<QuestionURL> qdata=new ArrayList<>();
        try{
        Pattern pattern = Pattern.compile("src=([^>]*)>");
        Matcher matcher = pattern.matcher(sdata);
        while (matcher.find()) {
            qdata.add(new QuestionURL(matcher.group(1)));
        }
        }catch (Exception e)
        {
            reporterror("Anserfrag",e.toString());
        }
        return  qdata;
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
                    for (int j=0;j<arrayopt.length();j++)
                    {
                        try {
                            JSONObject obj = arrayopt.getJSONObject(j);
                            String originaldata=obj.getString(DatabaseHelper.IMAGESOURCE);
                            String offlinepath=obj.getString(DatabaseHelper.OFFLINEPATH);
                            Log.e("original "," "+originaldata+" "+offlinepath);
                            dataset.get(i).setName(dataset.get(i).getName().replaceAll(originaldata,offlinepath));
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    Log.e("qname "," qname "+mParam1.getName());
                }
                // dataset.get(i).setIsTabletag(1);
                if (dataset.get(i).getIsTabletag() == 1) {
                    LinearLayout parent = new LinearLayout(getActivity());
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.HORIZONTAL);
                    final RadioButton rdbtn = new RadioButton(new ContextThemeWrapper(getActivity(), R.style.radionbutton), null, 0);
                    rdbtn.setPadding(10, 10, 10, 10);
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
                    web.loadDataWithBaseURL("file:///",style+dataset.get(i).getName(), "text/html", "utf-8", null);
                    //   web.loadData(dataset.get(i).getName(), null, null);
                    parent.addView(web);
                    layout.addView(parent);
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(
                            300, 100);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
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
                    rdbtn.setPadding(10, 10, 10, 10);
                    final Options opt = dataset.get(i);
                    rdbtn.setId(i);
                    //  rdbtn.setTextSize(14);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    Log.e("isanswerdata ", "isanswer " + dataset.get(i).isSelected() + " " + dataset.get(i).isAnswer());
                    if (dataset.get(i).isSelected()) {
                        rdbtn.setChecked(true);
                    }

                    if (dataset.get(i).isAnswer()) {
                        if (exam.isInstantExamResultWithAns()) {
                            rdbtn.setBackgroundColor(getContext().getResources().getColor(R.color.answered_questionlight));
                        }
                    }

                    if (!dataset.get(i).isAnswer() && dataset.get(i).isSelected()) {
                        isanswer = 2;
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
                            new AnswerPracticeFragment.LoadImage(rdbtn, 1).execute(s, d);
                            return d;*/

                            LevelListDrawable d = new LevelListDrawable();
                            try {
                                Log.e("source ",source);
                                Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                                d.addLevel(0, 0, empty);
                                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                                //  new LoadImage(txt, 0).execute(source, d);
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                Bitmap bitmap = BitmapFactory.decodeFile(source, bmOptions);
                                if (bitmap != null) {
                                    BitmapDrawable d1 = new BitmapDrawable(bitmap);
                                    d.addLevel(1, 1, d1);
                                    Log.e("width ", " " + bitmap.getWidth() + "  " + bitmap.getHeight());
                                    d.setBounds(0, 0, pxToDp(bitmap.getWidth()), pxToDp(bitmap.getHeight()));
                                    CharSequence t = ((RadioButton) rdbtn).getText();
                                    ((RadioButton) rdbtn).setText(t);
                                    d.setLevel(1);
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                            return d;

                        }
                    };
                    rdbtn.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    rdbtn.setText(Html.fromHtml(dataset.get(i).getName(), imggetter, null));
                    rg.addView(rdbtn);
                }
                qurldata.addAll(checkdata(dataset.get(i).getName()));
                /*Pattern pattern = Pattern.compile("src=([^>]*)>");
                Matcher matcher = pattern.matcher(dataset.get(i).getName());
                while (matcher.find()) {
                    qurldata.add(new QuestionURL(matcher.group(1)));
                }*/
              /*  final JSONArray array1 = DatabaseHelper.getInstance(getActivity()).getquestionurl1(dataset.get(i).getId(), "ans");
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

            switch (isanswer) {
                case 0:
                    txt.setText(getResources().getString(R.string.not_answered));
                    txt.setBackgroundColor(getResources().getColor(R.color.notanswered_questionlight));
                    break;
                case 1:
                    txt.setText(getResources().getString(R.string.correct_answer));
                    txt.setBackgroundColor(getResources().getColor(R.color.answered_questionlight));
                    break;
                case 2:
                    txt.setText(getResources().getString(R.string.wrong_answer));
                    txt.setBackgroundColor(getResources().getColor(R.color.wrongswered_questionlight));
                    break;
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            txt.setPadding(10, 10, 10, 10);
            txt.setLayoutParams(params);
            layout.addView(txt);

            if (exam.isInstantExamResultWithAns()) {
                final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(mParam1.getId(), "explanation");
                if (array.length() > 0) {
                    for (int i=0;i<array.length();i++)
                    {
                        try {
                            JSONObject obj = array.getJSONObject(i);
                            String originaldata=obj.getString(DatabaseHelper.IMAGESOURCE);
                            String offlinepath=obj.getString(DatabaseHelper.OFFLINEPATH);
                            Log.e("original "," "+originaldata+" "+offlinepath);
                            mParam1.setExplanation(mParam1.getExplanation().replaceAll(originaldata,offlinepath));

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    Log.e("qname "," qname "+mParam1.getName());
                }

                if (mParam1.getIsTabletagexplanation().equalsIgnoreCase("1")) {
                    //   txt.setVisibility(View.GONE);
                    final WebView web = new WebView(getActivity());
                    web.getSettings().setJavaScriptEnabled(true);
                    web.getSettings().setAllowFileAccess(true);
                    web.loadDataWithBaseURL("file:///",style+mParam1.getExplanation(), "text/html", "utf-8", null);
                    layout.addView(web);
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }else {
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
                        new AnswerPracticeFragment.LoadImage(txtexplanation, 0).execute(s, d);
                        return d;*/

                            LevelListDrawable d = new LevelListDrawable();
                            try {
                                Log.e("source ", source);
                                Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                                d.addLevel(0, 0, empty);
                                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                                //  new LoadImage(txt, 0).execute(source, d);
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                Bitmap bitmap = BitmapFactory.decodeFile(source, bmOptions);
                                if (bitmap != null) {
                                    BitmapDrawable d1 = new BitmapDrawable(bitmap);
                                    d.addLevel(1, 1, d1);
                                    Log.e("width ", " " + bitmap.getWidth() + "  " + bitmap.getHeight());
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
                    layout.addView(txtexplanation);
                }
                addenlargedata(checkdata(mParam1.getExplanation()));

               /* final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl1(mParam1.getId(), "elplanation");
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
            }


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
                            new AnswerPracticeFragment.LoadImage(rdbtn, 1).execute(s, d);
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
                        new AnswerPracticeFragment.LoadImage(txtexplanation, 0).execute(s, d);
                        return d;
                    }
                };
                txtexplanation.setText(Html.fromHtml(mParam1.getExplanation(), imggetter, null));
                layout.addView(txtexplanation);

                final JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl1(mParam1.getId(), "elplanation");
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
                final JSONArray array1 = DatabaseHelper.getInstance(getActivity()).getquestionurl1(dataset.get(i).getId(), "ans");
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


        }catch (Exception e)
        {
            reporterror("Anserfrag",e.toString());
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
        }catch (Exception e)
        {
            reporterror("Anserfrag",e.toString());
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
        new AnswerPracticeFragment.LoadImage(txt, 0).execute(source, d);
        return d;*/

        LevelListDrawable d = new LevelListDrawable();
        try {
            Log.e("source ",source);
            Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
            //  new LoadImage(txt, 0).execute(source, d);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(source, bmOptions);
            if (bitmap != null) {
                BitmapDrawable d1 = new BitmapDrawable(bitmap);
                d.addLevel(1, 1, d1);
                Log.e("width ", " " + bitmap.getWidth() + "  " + bitmap.getHeight());
                d.setBounds(0, 0, pxToDp(bitmap.getWidth()), pxToDp(bitmap.getHeight()));
                CharSequence t = ((TextView) txt).getText();
                ((TextView) txt).setText(t);
                d.setLevel(1);
            }
        }catch (Exception e)
        {
            reporterror("Anserfrag",e.toString());
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
            Log.d("tag", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                reporterror("Anserfrag",e.toString());
                e.printStackTrace();
            } catch (MalformedURLException e) {
                reporterror("Anserfrag",e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                reporterror("Anserfrag",e.toString());
                e.printStackTrace();
            }catch (Exception e)
            {
                reporterror("Anserfrag",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try{
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
            }catch (Exception e)
            {
                reporterror("Anserfrag",e.toString());
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
        try{
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
            if (!((Activity) getActivity()).isFinishing()) {
                final AlertDialog dialog = alert.create();
                dialog.show();
            }
        }catch (Exception e)
        {
            reporterror("Anserfrag",e.toString());
        }
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
            if(GlobalValues.student!=null)
                obj.put("email",GlobalValues.student.getMobile());
            else obj.put("email","");
            obj.put("osversion", getmodel() + " " + getos());
            obj.put("errordetail", error.replaceAll("'", ""));
            obj.put("appname", "Target Educare Peak");
            obj.put("activityname", classname);
            ConnectionManager.getInstance(getActivity()).reporterror(obj.toString());
        } catch (Exception e) {
            Log.e("error", "error " + e);
            e.printStackTrace();
        }
    }
}
