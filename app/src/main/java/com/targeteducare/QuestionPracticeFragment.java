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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.targeteducare.Adapter.QuestionURLDataAdapter;
import com.targeteducare.Classes.Options;
import com.targeteducare.Classes.Question;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.database.DatabaseHelper;


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
 * {@link QuestionPracticeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionPracticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class QuestionPracticeFragment extends Fragment implements Html.ImageGetter {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private Question mParam1;
    private String mParam2;

    private ArrayList<Options> options = new ArrayList<>();

    TextView txt, txtsub;
    TextView tv_skip;
    LinearLayout layout;
    RadioGroup rg;
    CardView cv;
    int skipped = 0;

    public QuestionPracticeFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static QuestionPracticeFragment newInstance(Question param1, String param2) {
        Log.e("fragment ", "QuestionPracticeFragment ");
        QuestionPracticeFragment fragment = new QuestionPracticeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("I am in ", "QuestionPracticeFragment");

        if (getArguments() != null) {
            mParam1 = (Question) getArguments().getSerializable(ARG_PARAM1);
            options = mParam1.getOptions();
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_practice, container, false);
        layout = (LinearLayout) v.findViewById(R.id.layout);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rec, new IntentFilter("QuestionUpdated"));

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

        txt = (TextView) v.findViewById(R.id.textview_1);
        txtsub = (TextView) v.findViewById(R.id.textview_sub);
        tv_skip = (TextView) v.findViewById(R.id.tv_skipquestion);

        txt.setTypeface(Fonter.getTypefaceregular(getActivity()));
        Spanned spanned = Html.fromHtml(mParam1.getSrno() + "." + mParam1.getName(), this, null);
        Log.e("optname ", "qname " + mParam1.getName());
        txt.setText(spanned);
        txt.setPadding(10, 10, 10, 10);
        txt.setTextColor(getResources().getColor(R.color.textcolor));

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipped += 1;
                addisskipped();
                mParam1.setIsskipped(true);
            }
        });

        if (mParam1.getIsTabletag().equalsIgnoreCase("1")) {
            txt.setVisibility(View.GONE);
            final WebView web = new WebView(getActivity());
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setAllowFileAccess(true);
            web.loadDataWithBaseURL("file:///", mParam1.getSrno() + "." + mParam1.getName(), "text/html", "utf-8", null);
            //parent.addView(web);
            layout.addView(web);
            RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
            webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }

        Pattern pattern = Pattern.compile("src=([^>]*)>");
        Matcher matcher = pattern.matcher(mParam1.getName());
        final ArrayList<QuestionURL> qdata = new ArrayList<>();
        while (matcher.find()) {
            qdata.add(new QuestionURL(matcher.group(1)));
        }

        if (qdata.size() > 0) {
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
        }

        /*if (Integer.parseInt(mParam1.getQuestionTypeId()) == 1) {
            addradioGroup(mParam1.getOptions());
        } else {
            for (int i = 0; i < mParam1.getOptions().size(); i++) {
                Options t = mParam1.getOptions().get(i);
                addCheckBox(t);
            }
        }*/
        if (Integer.parseInt(mParam1.getQuestionTypeId()) == 1) {
            addradioGroup(mParam1.getOptions());
        } else {
            for (int i = 0; i < mParam1.getOptions().size(); i++) {
                Options t = mParam1.getOptions().get(i);
                addCheckBox(t);
            }
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    private void addradioGroup(final ArrayList<Options> dataset) {
        try {
            Log.e("Value of IsSkipped is ", String.valueOf(mParam1.isIsskipped()));
            Log.e("Value of IsSkipped is ", String.valueOf(mParam1.getSrno()));

            String ans = "";
            rg = new RadioGroup(getActivity());
            Log.e("I am ", "Radio group");
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
                            Log.e("original ", " " + originaldata + " " + offlinepath);
                            dataset.get(i).setName(dataset.get(i).getName().replaceAll(originaldata, offlinepath));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("qname ", " qname " + mParam1.getName());
                }

                final Options opt = dataset.get(i);
                if (dataset.get(i).getIsTabletag() == 1) {
                    final LinearLayout parent = new LinearLayout(getActivity());
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.HORIZONTAL);
                    final RadioButton rdbtn = new RadioButton(getActivity());
                    rdbtn.setTag(options.get(i).getId());
                    rdbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    rdbtn.setPadding(30,10,15,10);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    rdbtn.setBackgroundResource(R.drawable.card_radiobutton);
                    if (dataset.get(i).isSelected()) {
                        rdbtn.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                        rdbtn.setChecked(true);
                    }
                    rdbtn.setButtonDrawable(null);

                    parent.addView(rdbtn);
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.e("You have selected ", String.valueOf(rg.getCheckedRadioButtonId()));

                            int pos = rg.getCheckedRadioButtonId();

                            try {
                                for (int i = 0; i < dataset.size(); i++) {
                                    //Log.e("I am into the ", "loop");
                                    dataset.get(i).setSelected(false);
                                    rg.getChildAt(i).setBackgroundResource(R.drawable.card_radiobutton);

                                    if (pos == i) {
                                        rg.getChildAt(pos).setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                                        dataset.get(pos).isSelected();
                                    }
                                }

                                opt.setSelected(((RadioButton) view).isChecked());

                                if (!mParam1.isIsanswered()) {
                                    ((PracticeActivity) getActivity()).notifyqdatachanged(mParam1);
                                }

                                mParam1.setIsanswered(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    final WebView web = new WebView(getActivity());

                    web.getSettings().setJavaScriptEnabled(true);
                    // web.loadData(dataset.get(i).getName(), null, null);
                    web.getSettings().setAllowFileAccess(true);
                    web.loadDataWithBaseURL("file:///", dataset.get(i).getName(), "text/html", "utf-8", null);
                    parent.addView(web);
                    layout.addView(parent);
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                } else {
                    final RadioButton rdbtn = new RadioButton(getActivity());
                    rdbtn.setTag(mParam1.getOptions().get(i).getId());
                    rdbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    rdbtn.setPadding(30,10,15,10);
                    rdbtn.setBackgroundResource(R.drawable.card_radiobutton);
                    rdbtn.setButtonDrawable(null);
                    rdbtn.setTextColor(getResources().getColor(R.color.textcolor));
                    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    rdbtn.setLayoutParams(params);
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
                    JSONArray array = DatabaseHelper.getInstance(getActivity()).getquestionurl(opt.getId(), "que");
                    if (array.length() > 0) {
                        ImageView img = new ImageView(getActivity());
                        img.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                    }
                    Log.e("optname ", "optname " + dataset.get(i).getName());
                    rdbtn.setText(Html.fromHtml(dataset.get(i).getName(), imggetter, null));
                    if (dataset.get(i).isSelected()) {
                        rdbtn.setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                        rdbtn.setChecked(true);
                    }
                    rg.addView(rdbtn);
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int pos = rg.getCheckedRadioButtonId();
                            Log.e("You have selected ", String.valueOf(rg.getCheckedRadioButtonId()));

                            try {
                                for (int i = 0; i < dataset.size(); i++) {
                                    //Log.e("I am into the ", "loop");
                                    dataset.get(i).setSelected(false);
                                    rg.getChildAt(i).setBackgroundResource(R.drawable.card_radiobutton);

                                    if (pos == i) {
                                        rg.getChildAt(pos).setBackgroundResource(R.drawable.card_radiobutton_onclicked);
                                        dataset.get(pos).isSelected();
                                    }
                                }

                                opt.setSelected(((RadioButton) view).isChecked());

                                if (!mParam1.isIsanswered()) {
                                    ((PracticeActivity) getActivity()).notifyqdatachanged(mParam1);
                                }

                                mParam1.setIsanswered(true);

                            } catch (Exception e) {
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
                Matcher matcher = pattern.matcher(dataset.get(i).getName());
                while (matcher.find()) {
                    qurldata.add(new QuestionURL(matcher.group(1)));
                }
            }
            layout.addView(rg);
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

            if (qurldata.size() > 0) {
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
            }

            if(mParam1.isIsskipped()){
                addisskipped();
            }

        } catch (Exception e) {
            Log.e("error ","err"+e.toString());
            e.printStackTrace();
        }
    }

    private void addisskipped() {
        for (int i = 0; i < mParam1.getOptions().size(); i++) {

            if (options.get(i).isAnswer()) {
                Log.e("I am here ", " to check radio button " + String.valueOf(rg.getChildCount()));
                for (int j = 0; j < rg.getChildCount(); j++) {

                    rg.getChildAt(j).setEnabled(false);
                    rg.getChildAt(j).setBackgroundResource(R.drawable.card_radiobutton);
                    if (options.get(i).getId() == (int) rg.getChildAt(j).getTag()) {
                        //color change
                        rg.getChildAt(j).setBackgroundResource(R.drawable.card_radiobutton_onskipped);
                    }
                }
            }

            if(mParam1.isIsskipped()){
                tv_skip.setVisibility(View.GONE);
            }
        }

        if (mParam1.getIsTabletag().equalsIgnoreCase("1")) {
            //   txt.setVisibility(View.GONE);
            final WebView web = new WebView(getActivity());
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setAllowFileAccess(true);
            web.loadDataWithBaseURL("file:///", mParam1.getExplanation(), "text/html", "utf-8", null);
            layout.addView(web);
            RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
            webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            final TextView txtexplanation = new TextView(getActivity());
            txtexplanation.setTextColor(getResources().getColor(R.color.textcolor));
            txtexplanation.setLayoutParams(params);
            txtexplanation.setPadding(10, 10, 10, 10);
            Html.ImageGetter imggetter = new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {

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
    }


    /*private void addradioGroup(final ArrayList<Options> dataset) {
        try {
            String ans = "";
            cv = new CardView(getActivity());
            final ArrayList<QuestionURL> qurldata = new ArrayList<>();

            CardView.LayoutParams cardViewParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            cardViewParams.gravity = Gravity.CENTER_VERTICAL;
            cv.setLayoutParams(cardViewParams);

            for (int i = 0; i < dataset.size(); i++) {
                final JSONArray arrayopt = PracticeDatabaseHelper.getInstance(getActivity()).getquestionurl(dataset.get(i).getId(), "ans");
                if (arrayopt.length() > 0) {
                    for (int j = 0; j < arrayopt.length(); j++) {
                        try {
                            JSONObject obj = arrayopt.getJSONObject(j);
                            String originaldata = obj.getString(PracticeDatabaseHelper.PRACTICE_IMAGESOURCE);
                            String offlinepath = obj.getString(PracticeDatabaseHelper.PRACTICE_OFFLINEPATH);
                            Log.e("original ", " " + originaldata + " " + offlinepath);
                            dataset.get(i).setName(dataset.get(i).getName().replaceAll(originaldata, offlinepath));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("qname ", " qname " + mParam1.getName());
                }

                final Options opt = dataset.get(i);
                if (dataset.get(i).getIsTabletag() == 1) {
                    LinearLayout parent = new LinearLayout(getActivity());
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.HORIZONTAL);

                    final CardView cv = new CardView(getActivity());
                    cv.setRadius(8);
                    cv.setCardElevation(5);
                    cv.setPadding(10, 10, 10, 10);
                    CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, 100);
                    layoutParams.setMargins(2, 10, 2, 10);
                    layoutParams.gravity = Gravity.CENTER;
                    cv.setLayoutParams(layoutParams);


                    final TextView textView = new TextView(getActivity());

                    textView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView.setPadding(5, 5, 5, 5);
                    cv.addView(textView);
                    parent.addView(cv);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (((TextView) view).isSelected()) {
                                    textView.setClickable(false);
                                    cv.setClickable(false);
                                } else {
                                    for (int i = 0; i < dataset.size(); i++) {
                                        dataset.get(i).setSelected(false);
                                    }

                                    opt.setSelected(((TextView) view).isSelected());

                                    //opt.setSelected(((RadioButton) view).isChecked());
                                    if (!mParam1.isIsanswered())
                                        ((PracticeActivity) getActivity()).notifyqdatachanged(mParam1);

                                    cv.setCardBackgroundColor(getResources().getColor(R.color.lightgray));
                                    mParam1.setIsanswered(true);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    final WebView web = new WebView(getActivity());

                    web.getSettings().setJavaScriptEnabled(true);
                    // web.loadData(dataset.get(i).getName(), null, null);
                    web.getSettings().setAllowFileAccess(true);
                    web.loadDataWithBaseURL("file:///", dataset.get(i).getName(), "text/html", "utf-8", null);
                    parent.addView(web);
                    layout.addView(parent);
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(300, 100);
                    webViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                } else {
                    final CardView cv = new CardView(getActivity());
                    cv.setRadius(8);
                    cv.setCardElevation(5);
                    cv.setPadding(10, 10, 10, 10);
                    CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, 100);
                    layoutParams.setMargins(2, 10, 2, 10);
                    layoutParams.gravity = Gravity.CENTER;
                    cv.setLayoutParams(layoutParams);


                    final TextView textView = new TextView(getActivity());

                    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(params);
                    textView.setPadding(5, 5, 5, 5);
                    textView.setId(i);
                    Html.ImageGetter imggetter = new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {

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
                                    CharSequence t = ((TextView) textView).getText();
                                    ((TextView) textView).setText(t);
                                    d.setLevel(1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return d;
                        }
                    };
                    JSONArray array = PracticeDatabaseHelper.getInstance(getActivity()).getquestionurl(opt.getId(), "que");
                    if (array.length() > 0) {
                        ImageView img = new ImageView(getActivity());
                        img.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                    }
                    Log.e("optname ", "optname " + dataset.get(i).getName());
                    textView.setText(Html.fromHtml(dataset.get(i).getName(), imggetter, null));
                    textView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView.setPadding(5, 5, 5, 5);
                    cv.addView(textView);
                    layout.addView(cv);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                                if (((TextView) view).isSelected()) {
                                    textView.setClickable(false);
                                    cv.setClickable(false);
                                } else {
                                    for (int i = 0; i < dataset.size(); i++) {
                                        dataset.get(i).setSelected(false);
                                    }
                                    opt.setSelected(((TextView) view).isSelected());

                                    //opt.setSelected(((RadioButton) view).isChecked());
                                    if (!mParam1.isIsanswered())
                                        ((PracticeActivity) getActivity()).notifyqdatachanged(mParam1);

                                    cv.setCardBackgroundColor(getResources().getColor(R.color.lightgray));
                                    mParam1.setIsanswered(true);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }


                Pattern pattern = Pattern.compile("src=([^>]*)>");
                Matcher matcher = pattern.matcher(dataset.get(i).getName());
                while (matcher.find()) {
                    qurldata.add(new QuestionURL(matcher.group(1)));
                }
            }
            layout.addView(cv);
            if (qurldata.size() > 0) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


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
                            CharSequence t = ((CheckBox) cb).getText();
                            ((CheckBox) cb).setText(t);
                            d.setLevel(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                    return d;
                }
            };

            cb.setButtonDrawable(null);
            cb.setText(Html.fromHtml(qopt.getName(), imggetter, null));
            cb.setTag(qopt);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    qopt.setSelected(b);
                }
            });
            cb.setTextColor(getResources().getColor(R.color.textcolor));
            layout.addView(cb);
        } catch (Exception e) {
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
                CharSequence t = ((TextView) txt).getText();
                ((TextView) txt).setText(t);
                d.setLevel(1);
            }
        } catch (Exception e) {
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
            Log.d("tag", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                Log.e("width ", " " + bitmap.getWidth() + "  " + bitmap.getHeight());
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
        if (!((Activity) getActivity()).isFinishing()) {
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
        }
    }

    /*BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mParam1.getId() == intent.getIntExtra("Qid", 0)) {
                if (cv != null) {

                    int textid = cv.getId();
                    TextView textView = cv.findViewById(textid);


                    if (textView != null) {
                        cv.setEnabled(false);
                        textView.setClickable(false);
                    }
                }
            }
        }
    };*/

    public void addenlargedata(final ArrayList<QuestionURL> qdata) {
        if (qdata.size() > 0) {
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
        }
    }

    public ArrayList<QuestionURL> checkdata(String sdata) {
        ArrayList<QuestionURL> qdata = new ArrayList<>();
        Pattern pattern = Pattern.compile("src=([^>]*)>");
        Matcher matcher = pattern.matcher(sdata);
        while (matcher.find()) {
            qdata.add(new QuestionURL(matcher.group(1)));
        }
        return qdata;
    }

    BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mParam1.getId() == intent.getIntExtra("Qid", 0)) {
                if (rg != null) {
                    int radioButtonID = rg.getCheckedRadioButtonId();

                    RadioButton radioButton = rg.findViewById(radioButtonID);
                    if (radioButton != null)
                        radioButton.setChecked(false);
                }
            }
        }
    };
}