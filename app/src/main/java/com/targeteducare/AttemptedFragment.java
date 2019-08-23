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
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class AttemptedFragment extends Fragment{//} implements ExamListActivity.DataFromActivityToFragment, PracticeTestActivity.DataFromActivityToFragment, MockTestActivity.DataFromActivityToFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static AttemptedFragment attemptedFragment = null;
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
    private ArrayList<Exam> mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AttemptedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      /*  try {
            if(attemptedFragment!=null) {
                if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    ((PracticeTestActivity) context).dataFromActivityToFragmentattempted = this;
                } else if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    ((MockTestActivity) context).dataFromActivityToFragmentattempted = this;
                } else {
                    ((ExamListActivity) context).dataFromActivityToFragmentattempted = this;
                }
            }
        }catch (Exception e)
        {
            reporterror("Attemptedfrag",e.toString());
            e.printStackTrace();
        }*/
    }

    public static AttemptedFragment newInstance(Context context, ArrayList<Exam> param1, String param2) {
        try {
            if (attemptedFragment == null) {
                attemptedFragment = new AttemptedFragment();
            }

            Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            attemptedFragment.setArguments(args);
            attemptedFragment.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attemptedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                // mParam1 = (ArrayList<Exam>) getArguments().getSerializable(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rec);
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
        }
        super.onDestroy();

    }

    BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
            //    Log.e("received ","received in attempted");
                mParam1 = GlobalValues.mdataset_Attempted;
                if(attemptedFragment!=null) {
                    if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                        practiceTestAdapter = new PracticeTestAdapter(getContext(), mParam1, lang);
                        recyclerView.setAdapter(practiceTestAdapter);
                        practiceTestAdapter.notifyDataSetChanged();
                    } else if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                        mockTestAdapter = new MockTestAdapter(getContext(), mParam1, lang,mParam2);
                        recyclerView.setAdapter(mockTestAdapter);
                        mockTestAdapter.notifyDataSetChanged();
                    } else {
                        examAdapter = new ExamAdapter(getContext(), mParam1, lang,mParam2);
                        recyclerView.setAdapter(examAdapter);
                        examAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                reporterror("Attemptedfrag", e.toString());
                e.printStackTrace();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attempted, container, false);
        try {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rec, new IntentFilter("updateddata"));
            mParam1 = GlobalValues.mdataset_Attempted;
            tv_test = v.findViewById(R.id.typeoftestname);
            tv_notest = v.findViewById(R.id.notestavailable);
            lang = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            recyclerView = v.findViewById(R.id.recyclerviewforattempted);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            searchView = v.findViewById(R.id.search);
            if (mParam1.size() == 0) {
                tv_notest.setVisibility(View.VISIBLE);
                //tv_notest.setText("No Attempted Tests at the Moment");
            }
      /*  for (int i = 0; i < mParam1.size(); i++) {
            if (((int) Math.round(mParam1.get(i).getProgress())) > 0) {
                attemptedFragmentData();
            } else {
                tv_notest.setVisibility(View.VISIBLE);
                tv_notest.setText("No Attempted Tests at the Moment");
            }
        }*/
            attemptedFragmentData();
            setHasOptionsMenu(true);
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  try {
            if(attemptedFragment!=null) {
                if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    ((PracticeTestActivity) context).dataFromActivityToFragmentattempted = this;
                } else if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    ((MockTestActivity) context).dataFromActivityToFragmentattempted = this;
                } else if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.ExamListActivity")) {
                    ((ExamListActivity) context).dataFromActivityToFragmentattempted = this;
                }
            }
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
        }*/
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        try {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

  /*  @Override
    public void sendData(ArrayList<Exam> data) {
        try {
            if(attemptedFragment!=null) {
                if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    practiceTestAdapter = new PracticeTestAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(practiceTestAdapter);
                    practiceTestAdapter.notifyDataSetChanged();
                } else if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    mockTestAdapter = new MockTestAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(mockTestAdapter);
                    mockTestAdapter.notifyDataSetChanged();
                } else {
                    examAdapter = new ExamAdapter(getContext(), mParam1, lang);
                    recyclerView.setAdapter(examAdapter);
                    examAdapter.notifyDataSetChanged();
                }

                if (mParam1.size() == 0) {
                    tv_notest.setVisibility(View.VISIBLE);
                    tv_notest.setText("No Attempted Tests at the Moment");
                } else tv_notest.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
        }
    }
*/
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void attemptedFragmentData() {
        try {
            if(attemptedFragment!=null) {
                if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
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

                } else if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
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

                    examAdapter = new ExamAdapter(getContext(), GlobalValues.mdataset_Attempted, lang,mParam2);
                    recyclerView.setAdapter(examAdapter);
                    examAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
            if(attemptedFragment!=null) {
                if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.PracticeTestActivity")) {
                    try {
                        practiceTestAdapter = new PracticeTestAdapter(attemptedFragment.getContext(), mParam1, lang);
                        for (int i = 0; i < mParam1.size(); i++) {
                            JSONArray array = DatabaseHelper.getInstance(attemptedFragment.getContext()).getexamdetails(mParam1.get(i).getExamid(), mParam1.get(i).getExam_type());
                            if (array.length() > 0) {
                                JSONObject obj = array.getJSONObject(0);
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
                        practiceTestAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (attemptedFragment.context.getClass().getName().equals("com.targeteducare.MockTestActivity")) {
                    try {
                        mockTestAdapter = new MockTestAdapter(attemptedFragment.getContext(), mParam1, lang,mParam2);
                        for (int i = 0; i < mParam1.size(); i++) {
                            JSONArray array = DatabaseHelper.getInstance(attemptedFragment.getContext()).getexamdetails(mParam1.get(i).getExamid(), mParam1.get(i).getExam_type());
                            if (array.length() > 0) {
                                JSONObject obj = array.getJSONObject(0);
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
                        e.printStackTrace();
                    }
                } else {
                    try {
                        examAdapter = new ExamAdapter(attemptedFragment.getContext(), mParam1, lang,mParam2);
                        for (int i = 0; i < mParam1.size(); i++) {
                            JSONArray array = DatabaseHelper.getInstance(attemptedFragment.getContext()).getexamdetails(mParam1.get(i).getExamid(), mParam1.get(i).getExam_type());
                            if (array.length() > 0) {
                                JSONObject obj = array.getJSONObject(0);
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
                        examAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
            e.printStackTrace();
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
