package com.targeteducare;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Adapter.ExamAdapter;
import com.targeteducare.Adapter.MockTestAdapter;
import com.targeteducare.Adapter.PracticeTestAdapter;
import com.targeteducare.Classes.Exam;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewFragment extends Fragment{//} implements ExamListActivity.DataFromActivityToFragment, PracticeTestActivity.DataFromActivityToFragment, MockTestActivity.DataFromActivityToFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static NewFragment newFragment = null;
    LinearLayout layout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    TextView tv_test, tv_notest, tv_toolbar;
    String lang = "";
    private PracticeTestAdapter practiceTestAdapter;
    private MockTestAdapter mockTestAdapter;
    private ExamAdapter examAdapter;
    Context context;
    SearchView searchView;
    // TODO: Rename and change types of parameters
    private ArrayList<Exam> mParam1;
    private String mParam2;
    //  private OnFragmentInteractionListener mListener;

    public NewFragment() {
        // Required empty public constructor
    }

    public static NewFragment newInstance(Context context, ArrayList<Exam> param1, String param2) {
        // if (newFragment == null) {

        newFragment = new NewFragment();
        //}
        try {
            Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            newFragment.setArguments(args);
            newFragment.context = context;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                try {
                    // mParam1 = (ArrayList<Exam>) getArguments().getSerializable(ARG_PARAM1);
                    mParam1 = GlobalValues.mdataset_New;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        } catch (Exception e) {
            reporterror("Newfragment", e.toString());
        }
    }

    @Override
    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rec);
        } catch (Exception e) {
            reporterror("Newfragment", e.toString());
        }
        //   LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(responseRec);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new, container, false);
        try {
            Log.e("mparam2","mparam2 "+mParam2);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rec, new IntentFilter("updateddata"));
            tv_test = v.findViewById(R.id.typeoftestname);
            tv_notest = v.findViewById(R.id.notestavailable);
            lang = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            recyclerView = v.findViewById(R.id.recyclerviewfornew);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            searchView = v.findViewById(R.id.search);

        /*for (int i = 0; i < mParam1.size(); i++) {
            if (((int) Math.round(mParam1.get(i).getProgress())) == 0) {
                newFragmentData();
            } else {
                tv_notest.setVisibility(View.VISIBLE);
                //tv_notest.setText("No New Test available currently. We'll update you soon.\nKeep checking");
            }
        }*/

            /*if (mParam1.size() == 0) {
                tv_notest.setVisibility(View.VISIBLE);
                //tv_notest.setText("No Attempted Tests at the Moment");
            }*/

            newFragmentData();

            setHasOptionsMenu(true);
        } catch (Exception e) {
            reporterror("Newfragment", e.toString());
        }
        return v;
    }


    private BroadcastReceiver rec=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("received ","received in newfragment");
            try {

                if(newFragment!=null) {
                    if (newFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                        practiceTestAdapter = new PracticeTestAdapter(getContext(),  GlobalValues.mdataset_New, lang);
                        recyclerView.setAdapter(practiceTestAdapter);
                        practiceTestAdapter.notifyDataSetChanged();
                    } else if (newFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                        mockTestAdapter = new MockTestAdapter(getContext(),  GlobalValues.mdataset_New, lang,mParam2);
                        recyclerView.setAdapter(mockTestAdapter);
                        mockTestAdapter.notifyDataSetChanged();
                    } else {
                        examAdapter = new ExamAdapter(getContext(),  GlobalValues.mdataset_New, lang,mParam2);
                        recyclerView.setAdapter(examAdapter);
                        examAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* try {
            if (newFragment != null) {
                if (newFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    ((PracticeTestActivity) context).dataFromActivityToFragment = this;

                } else if (newFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    ((MockTestActivity) context).dataFromActivityToFragment = this;
                } else if (newFragment.context.getClass().getName().equals("com.targeteducare.ExamListActivity")){
                    ((ExamListActivity) context).dataFromActivityToFragment = this;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
     /*   try {
            if (newFragment != null) {
                if (newFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    ((PracticeTestActivity) context).dataFromActivityToFragment = this;

                } else if (newFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    ((MockTestActivity) context).dataFromActivityToFragment = this;
                }  else if (newFragment.context.getClass().getName().equals("com.targeteducare.ExamListActivity")) {
                    ((ExamListActivity) context).dataFromActivityToFragment = this;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
       /* try {
            if (newFragment != null) {
                if (newFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    ((PracticeTestActivity) context).dataFromActivityToFragment = this;

                } else if (newFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    ((MockTestActivity) context).dataFromActivityToFragment = this;
                }  else if (newFragment.context.getClass().getName().equals("com.targeteducare.ExamListActivity")) {
                    ((ExamListActivity) context).dataFromActivityToFragment = this;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    /*@Override
    public void sendData(ArrayList<Exam> data) {
        try {
            if (newFragment != null) {
                if (newFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    practiceTestAdapter = new PracticeTestAdapter(getActivity(), mParam1, lang);
                    recyclerView.setAdapter(practiceTestAdapter);
                    practiceTestAdapter.notifyDataSetChanged();
                } else if (newFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    mockTestAdapter = new MockTestAdapter(getActivity(), mParam1, lang);
                    recyclerView.setAdapter(mockTestAdapter);
                    mockTestAdapter.notifyDataSetChanged();
                } else {
                    examAdapter = new ExamAdapter(getActivity(), mParam1, lang);
                    recyclerView.setAdapter(examAdapter);
                    examAdapter.notifyDataSetChanged();
                }
            }
      *//*      if(mParam1.size() == 0){
                tv_notest.setVisibility(View.VISIBLE);
                tv_notest.setText(context.getResources().getString(R.string.nomissedtestsmessage));
            }else {
                tv_notest.setVisibility(View.GONE);
            }*//*
        } catch (Exception e) {
            reporterror("Newfragment", e.toString());
        }

    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mParam1 = (ArrayList<Exam>) intent.getSerializableExtra("datafromactivity");
            try {
                //adapter = new ExaAdapter(ExamListActivity.this, mdataset);
                for (int i = 0; i < mParam1.size(); i++) {
                    JSONArray array = DatabaseHelper.getInstance(newFragment.getContext()).getexamdetails(mParam1.get(i).getExamid(), mParam1.get(i).getExam_type());
                    if (array.length() > 0) {
                        JSONObject obj = array.getJSONObject(i);
                        mParam1.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                        mParam1.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                        mParam1.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                        mParam1.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                        mParam1.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                        mParam1.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                        mParam1.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                        //practiceTestModels.get(i).setTotal_questions(obj.getString(EBookDatabaseHelper.QUESTION));
                    }
                }
                mockTestAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                reporterror("Newfragment", e.toString());
            }
        }
    };


    public void newFragmentData() {
        try {
            if (newFragment != null) {
                if (newFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            try {
                                practiceTestAdapter.getFilter().filter(newText.trim());
                            } catch (Exception e) {
                                reporterror("Newfragment", e.toString());
                            }
                            return false;
                        }
                    });

                    practiceTestAdapter = new PracticeTestAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(practiceTestAdapter);
                    practiceTestAdapter.notifyDataSetChanged();

                } else if (newFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            try {
                                mockTestAdapter.getFilter().filter(newText.trim());
                            } catch (Exception e) {
                                reporterror("Newfragment", e.toString());
                            }
                            return false;
                        }
                    });

                    mockTestAdapter = new MockTestAdapter(getContext(), mParam1, lang,mParam2);
                    recyclerView.setAdapter(mockTestAdapter);
                    mockTestAdapter.notifyDataSetChanged();
                } else {

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            try {
                                examAdapter.getFilter().filter(newText.trim());
                            } catch (Exception e) {
                                reporterror("Newfragment", e.toString());
                            }
                            return false;
                        }
                    });

                    examAdapter = new ExamAdapter(getContext(), mParam1, lang,mParam2);
                    recyclerView.setAdapter(examAdapter);
                    examAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            reporterror("Newfragment", e.toString());
        }
    }


    public void newFragment_GetExamDetails(ArrayList<Exam> mParam1) {
        try {

            for (int i = 0; i < mParam1.size(); i++) {
                JSONArray array = DatabaseHelper.getInstance(newFragment.getContext()).getexamdetails(mParam1.get(i).getExamid(), mParam1.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    mParam1.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    mParam1.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    mParam1.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    mParam1.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    mParam1.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    mParam1.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    mParam1.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                }
            }

        } catch (Exception e) {
            reporterror("Newfragment", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
            if (newFragment != null) {
                if (newFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    try {
                        practiceTestAdapter = new PracticeTestAdapter(newFragment.getContext(), mParam1, lang);
                        newFragment_GetExamDetails(mParam1);
                        practiceTestAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        reporterror("Newfragment", e.toString());
                        e.printStackTrace();
                    }
                } else if (newFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    try {
                        mockTestAdapter = new MockTestAdapter(newFragment.getContext(), mParam1, lang,mParam2);
                        newFragment_GetExamDetails(mParam1);
                        mockTestAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        reporterror("Newfragment", e.toString());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        examAdapter = new ExamAdapter(newFragment.getContext(), mParam1, lang,mParam2);
                        newFragment_GetExamDetails(mParam1);
                        examAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        reporterror("Newfragment", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            reporterror("Newfragment", e.toString());
        }
        super.onResume();
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
}
