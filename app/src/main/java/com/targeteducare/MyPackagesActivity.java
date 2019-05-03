package com.targeteducare;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.targeteducare.Adapter.GetMyPackagesAdapter;
import com.targeteducare.Classes.MyPackages;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyPackagesActivity extends Activitycommon {

    TextView Packages_TextView_Payment;
    TextView Packages_TextView_Course_Name;
    RecyclerView Packages_RecyclerView;
    CheckBox Packages_CheckBox;
    TextView Packages_TextView_Amonut;
    Button Packages_Button;
    ArrayList<MyPackages> myPackagesArrayList;
    GetMyPackagesAdapter getMyPackagesAdapter;
    TextView Packages_Textview_Toolbar_Payment;
    TextView Packages_Button_Toolbar_Button;
    double amt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_packages);
        // setmaterialDesign();
        setmaterialDesign_my_package();
        back();
        setTitle("Package");
        Packages_TextView_Course_Name = (TextView) findViewById(R.id.package_textview_course_name);
        // Packages_TextView_Payment = (TextView) findViewById(R.id.package_textview_payment);
        Packages_CheckBox = (CheckBox) findViewById(R.id.package_checkbox_1);
        Packages_Textview_Toolbar_Payment = (TextView) findViewById(R.id.package_toolbar_textview_payment);
        Packages_Button_Toolbar_Button = (TextView) findViewById(R.id.package_toolbar_button_1);
        Packages_TextView_Amonut = (TextView) findViewById(R.id.package_textview_amount);
        // Packages_Button = (Button) findViewById(R.id.package_button_1);
        Packages_RecyclerView = (RecyclerView) findViewById(R.id.package_recyclerview);
        myPackagesArrayList = new ArrayList<MyPackages>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyPackagesActivity.this);


        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        getMyPackagesAdapter = new GetMyPackagesAdapter(context, myPackagesArrayList);

        Packages_RecyclerView.setLayoutManager(linearLayoutManager);
        Packages_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        Packages_RecyclerView.setAdapter(getMyPackagesAdapter);

       // {"FilterParameter":"{\"PageNo\":\"1\",\"NoofRecords\":\"1000000\",\"CategoryId\":\"0\",\"SubCategoryId\":\"0\"}"}

        try {
            jsonObject.put("PageNo", Constants.Packages_page_no);
            jsonObject.put("NoofRecords", Constants.Packages_no_of_records);
            jsonObject.put("CategoryId", Constants.Packages_Category_id);
            jsonObject.put("SubCategoryId", Constants.Packages_Subcategory_id);
            jsonObject1.put("FilterParameter", jsonObject.toString());
            Log.e("parameters ::  :: ", jsonObject1.toString());
            ConnectionManager.getInstance(MyPackagesActivity.this).getcourse(jsonObject1.toString());
        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    protected void onResponsed(int statuscode, int accsesscode, String data) {
        super.onResponsed(statuscode, accsesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accsesscode == Connection.GETCOURSE.ordinal()) {
                data = GlobalValues.TEMP_STR;
                Log.e("data  ::", data);
                if (data != null) {
                    try {

                        JSONObject jsonObject2 = new JSONObject(data);
                        JSONObject jsonObject3 = jsonObject2.getJSONObject("root");

                        myPackagesArrayList.addAll(MyPackages.getjson(jsonObject3));
                        Log.e("Array     ", myPackagesArrayList.size() + " " + myPackagesArrayList.toString());
                        getMyPackagesAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

 /*    public void gotogetamount(double sum, int add) {


        double s = (double) Double.parseDouble(String.valueOf(Packages_TextView_Payment.getText().toString()));
        Log.e("amount  ", String.valueOf(s));
        Log.e("add ", String.valueOf(sum));

        if (add == 1 && Packages_TextView_Payment.getText().toString() != null) {
            s += sum;

            Log.e("total final amt ", String.valueOf(s));

            Packages_TextView_Payment.setText(String.valueOf(s));

            Log.e("Packages_Payment  :", Packages_TextView_Payment.getText().toString());

        } else {
            s -= sum;
            Log.e("total final amt ", String.valueOf(s));

            Packages_TextView_Payment.setText(String.valueOf(s));

            Log.e("Packages_Payment  :", Packages_TextView_Payment.getText().toString());
        }


    }*/


    public void gotogetamount(double sum, int add) {


        double s = (double) Double.parseDouble(String.valueOf(Packages_Textview_Toolbar_Payment.getText().toString()));
        Log.e("amount  ", String.valueOf(s));
        Log.e("add ", String.valueOf(sum));

        if (add == 1 && Packages_Textview_Toolbar_Payment.getText().toString() != null) {
            s += sum;

            Log.e("total final amt ", String.valueOf(s));

            Packages_Textview_Toolbar_Payment.setText(String.valueOf(s));

            //  Log.e("Packages_Payment  :", Packages_TextView_Payment.getText().toString());

        } else {
            s -= sum;
            Log.e("total final amt ", String.valueOf(s));

            Packages_Textview_Toolbar_Payment.setText(String.valueOf(s));

            //Log.e("Packages_Payment  :", Packages_TextView_Payment.getText().toString());
        }


    }


    public void gotocallfragment(String discription_data) {

        //Fragment fragment = new My_Package_fragment();
       /* Bundle bundle=new Bundle();
        bundle.putString("Discription_data",discription_data);
        My_PackageFragment fragment = new My_PackageFragment();
        Log.e("Bundel data :: ",bundle.toString());
        fragment.setArguments(bundle);
       // Log.e("fragment :: ",String.valueOf(fragment));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, new My_PackageFragment());
        //Log.e("transaction :: ",transaction.toString());
        transaction.commit();*/

    }


}