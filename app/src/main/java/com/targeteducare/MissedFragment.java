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

import com.targeteducare.Adapter.ExamAdapter;
import com.targeteducare.Adapter.MockTestAdapter;
import com.targeteducare.Adapter.PracticeTestAdapter;
import com.targeteducare.Classes.Exam;

import org.json.JSONObject;

import java.util.ArrayList;

public class MissedFragment extends Fragment {//implements ExamListActivity.DataFromActivityToFragment, PracticeTestActivity.DataFromActivityToFragment, MockTestActivity.DataFromActivityToFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static MissedFragment missedFragment = null;
    LinearLayout layout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    TextView tv_test, tv_notest, tv_toolbar;
    private ArrayList<Exam> practiceTestModels = new ArrayList<>();
    private PracticeTestAdapter practiceTestAdapter;
    private MockTestAdapter mockTestAdapter;
    private ExamAdapter examAdapter;
    String lang = "";
    Context context;

    SearchView searchView;

    // TODO: Rename and change types of parameters
    private ArrayList<Exam> mParam1=new ArrayList<>();
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MissedFragment() {
        // Required empty public constructor
    }

    public static MissedFragment newInstance(Context context, ArrayList<Exam> param1, String param2) {
        try {
            //
            //if (missedFragment == null) {
                missedFragment = new MissedFragment();
          //  }
            Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            missedFragment.setArguments(args);
            missedFragment.context = context;
        }catch (Exception e)
        {

        }
        return missedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                // mParam1 = (ArrayList<Exam>) getArguments().getSerializable(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
         LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rec);
        super.onDestroy();

    }

    BroadcastReceiver rec=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.e("received ","received in missed");
                if (missedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    practiceTestAdapter = new PracticeTestAdapter(getContext(), mParam1,lang);
                    recyclerView.setAdapter(practiceTestAdapter);
                    practiceTestAdapter.notifyDataSetChanged();
                } else if (missedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    Log.e("Got into ", "missed fragment mock test adapter");
                    mockTestAdapter = new MockTestAdapter(getContext(), mParam1,lang,mParam2);
                    recyclerView.setAdapter(mockTestAdapter);
                    mockTestAdapter.notifyDataSetChanged();
                } else {
                    examAdapter = new ExamAdapter(getContext(), mParam1, lang,mParam2);
                    recyclerView.setAdapter(examAdapter);
                    examAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_missed, container, false);

        try {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rec,new IntentFilter("updateddata"));
            lang = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");

            if(GlobalValues.mdataset_Missed!=null)
            mParam1 = GlobalValues.mdataset_Missed;

            tv_test = v.findViewById(R.id.typeoftestname);
            tv_notest = v.findViewById(R.id.notestavailable);

            recyclerView = v.findViewById(R.id.recyclerviewformissed);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            searchView = v.findViewById(R.id.search);


            /*if (mParam1.size() == 0) {
                tv_notest.setVisibility(View.VISIBLE);
            }

            missedFragmentData();*/

   /*     tv_notest.setVisibility(View.GONE);
        tv_notest.setText("Yippee!! You have No Missed Test");*/
        /*for(int i = 0; i<mParam1.size(); i++){

            if(((int) Math.round(mParam1.get(i).getProgress()))==0){
                missedFragmentData();
            }
            else{
                tv_notest.setVisibility(View.VISIBLE);
                tv_notest.setText("Yippee!! You have No Missed Test");
            }
        }*/

            setHasOptionsMenu(true);
        } catch (Exception e) {
            reporterror("missedfragment", e.toString());
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
        /*    if(missedFragment!=null) {
                if (missedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    ((PracticeTestActivity) context).dataFromActivityToFragmentmissed = this;

                } else if (missedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    ((MockTestActivity) context).dataFromActivityToFragmentmissed = this;
                }  else if (missedFragment.context.getClass().getName().equals("com.targeteducare.ExamListActivity")){
                    ((ExamListActivity) context).dataFromActivityToFragmentmissed = this;
                }
            }*/
        } catch (Exception e) {
            reporterror("missedfragment", e.toString());
            e.printStackTrace();
        }
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }


    @Override
    public void onDetach() {
        try {
            super.onDetach();
            mListener = null;
        } catch (Exception e) {
            reporterror("missedfragment", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
     /*   try {
            if(missedFragment!=null) {
                if (missedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    ((PracticeTestActivity) context).dataFromActivityToFragmentmissed = this;
                } else if (missedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    ((MockTestActivity) context).dataFromActivityToFragmentmissed = this;
                }  else if (missedFragment.context.getClass().getName().equals("com.targeteducare.ExamListActivity")) {
                    ((ExamListActivity) context).dataFromActivityToFragmentmissed = this;
                }
            }
        } catch (Exception e) {
            reporterror("missedfragment", e.toString());
            e.printStackTrace();
        }*/
    }

/*    @Override
    public void sendData(ArrayList<Exam> data) {
        try {
            if(missedFragment!=null) {
                if (missedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {

                    practiceTestAdapter = new PracticeTestAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(practiceTestAdapter);
                    practiceTestAdapter.notifyDataSetChanged();

                } else if (missedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    mockTestAdapter = new MockTestAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(mockTestAdapter);
                    mockTestAdapter.notifyDataSetChanged();
                }  else if (missedFragment.context.getClass().getName().equals("com.targeteducare.ExamListActivity")) {
                    examAdapter = new ExamAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(examAdapter);
                    examAdapter.notifyDataSetChanged();
                }

            *//*if (mParam1.size() == 0) {
                tv_notest.setVisibility(View.VISIBLE);
                tv_notest.setText(context.getResources().getString(R.string.nomissedtestsmessage));
            } else {
                tv_notest.setVisibility(View.VISIBLE);
            }*//*
            }
        } catch (Exception e) {
            reporterror("missedfragment", e.toString());
            e.printStackTrace();
        }
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void missedFragmentData() {
        try {
            if(missedFragment!=null) {
                if (missedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            practiceTestAdapter.getFilter().filter(newText.trim());
                            return false;
                        }
                    });

                    practiceTestAdapter = new PracticeTestAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(practiceTestAdapter);
                    practiceTestAdapter.notifyDataSetChanged();

                } else if (missedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            mockTestAdapter.getFilter().filter(newText.trim());
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
                            examAdapter.getFilter().filter(newText.trim());
                            return false;
                        }
                    });

                    examAdapter = new ExamAdapter(getContext(), mParam1, lang,mParam2);
                    recyclerView.setAdapter(examAdapter);
                    examAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            reporterror("missedfragment", e.toString());
            e.printStackTrace();
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
