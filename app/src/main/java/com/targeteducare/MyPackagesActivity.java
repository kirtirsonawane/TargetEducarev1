package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ebs.android.sdk.Config;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;
import com.targeteducare.Adapter.GetMyPackagesAdapter;
import com.targeteducare.Classes.MyPackages;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    boolean isloadingstarted = false;
    String lang = "";
    SearchView searchView;
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_my_packages);
            // setmaterialDesign();
            setmaterialDesign_my_package();
            back();
            setTitle(getResources().getString(R.string.package_title));
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            tag = this.getClass().getSimpleName();
            searchView = findViewById(R.id.search);
            Packages_TextView_Course_Name = (TextView) findViewById(R.id.package_textview_course_name);
            // Packages_TextView_Payment = (TextView) findViewById(R.id.package_textview_payment);
            Packages_CheckBox = (CheckBox) findViewById(R.id.package_checkbox_1);
            Packages_Textview_Toolbar_Payment = (TextView) findViewById(R.id.package_toolbar_textview_payment);
            Packages_Button_Toolbar_Button = (TextView) findViewById(R.id.package_toolbar_button_1);

            Packages_Button_Toolbar_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        isloadingstarted = false;
                        JSONObject obj = new JSONObject();
                        JSONArray array = new JSONArray();
                        for (int i = 0; i < myPackagesArrayList.size(); i++) {
                            if (myPackagesArrayList.get(i).isSelected()) {
                                JSONObject obj1 = new JSONObject();
                                obj1.put("Id", myPackagesArrayList.get(i).getId());
                                obj1.put("Name", myPackagesArrayList.get(i).getName());
                                obj1.put("Amount", myPackagesArrayList.get(i).getAmount());
                                obj1.put("NameAmount", myPackagesArrayList.get(i).getName() + " (" + myPackagesArrayList.get(i).getAmount() + ")");
                                array.put(obj1);
                            }
                        }

                        obj.put("PayMode", "");
                        obj.put("Transctionno", "");
                        obj.put("BankName", "");
                        obj.put("dueamount", "0");
                        obj.put("PayRemark", "");
                        obj.put("DDCheque", "");
                        Calendar cal = Calendar.getInstance();

                        String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        obj.put("Date", d);
                        obj.put("totalamount", Packages_Textview_Toolbar_Payment.getText().toString());
                        obj.put("status", "Pending");
                        obj.put("billdate", d);
                        obj.put("PackageDetails", array);
                        obj.put("Id", 0);
                        JSONObject rootobj = new JSONObject();
                        JSONObject subrootobj = new JSONObject();
                        JSONObject xmlobj = new JSONObject();
                        subrootobj.put("subroot", obj);
                        rootobj.put("root", subrootobj);
                        xmlobj.put("xml", rootobj.toString());
                        xmlobj.put("StudentId", GlobalValues.student.getId());
                        xmlobj.put("billid", 0);
                        xmlobj.put("flag", "new");
                        JSONObject mainobj = new JSONObject();
                        mainobj.put("FilterParameter", xmlobj.toString());
                        //Log.e("obj ", "obj " + xmlobj.toString());
                        // genloading("loading...");
                        ConnectionManager.getInstance(MyPackagesActivity.this).getbillid(mainobj.toString(), 1);
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            Packages_TextView_Amonut = (TextView) findViewById(R.id.package_textview_amount);
            // Packages_Button = (Button) findViewById(R.id.package_button_1);
            Packages_RecyclerView = (RecyclerView) findViewById(R.id.package_recyclerview);
            myPackagesArrayList = new ArrayList<MyPackages>();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyPackagesActivity.this);
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            getMyPackagesAdapter = new GetMyPackagesAdapter(MyPackagesActivity.this, myPackagesArrayList, 0, lang);

            Packages_RecyclerView.setLayoutManager(linearLayoutManager);
            Packages_RecyclerView.setItemAnimator(new DefaultItemAnimator());
            Packages_RecyclerView.setAdapter(getMyPackagesAdapter);
            // {"FilterParameter":"{\"PageNo\":\"1\",\"NoofRecords\":\"1000000\",\"CategoryId\":\"0\",\"SubCategoryId\":\"0\"}"}

            try {
                jsonObject.put("PageNo", Constants.Packages_page_no);
                jsonObject.put("NoofRecords", Constants.Packages_no_of_records);
                jsonObject.put("CategoryId", Constants.Packages_Category_id);
                jsonObject.put("SubCategoryId", Constants.Packages_Subcategory_id);
                jsonObject.put("ImagePath", URLS.packages_image_url());
                jsonObject1.put("FilterParameter", jsonObject.toString());
                //Log.e("parameters ::  :: ", jsonObject1.toString());
                ConnectionManager.getInstance(MyPackagesActivity.this).getcourse(jsonObject1.toString());
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }

            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            // searchView.setIconified(false);
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setIconified(false);
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    getMyPackagesAdapter.getFilter().filter(newText.trim());
                    return false;
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    protected void onResponsed(int statuscode, int accsesscode, String data) {
        super.onResponsed(statuscode, accsesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accsesscode == Connection.GETCOURSE.ordinal()) {
                    data = GlobalValues.TEMP_STR;
                    //Log.e("data  ::", data);
                    if (data != null) {
                        try {
                            JSONObject jsonObject2 = new JSONObject(data);
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("root");
                            myPackagesArrayList.addAll(MyPackages.getjson(jsonObject3));
                            //Log.e("Array     ", myPackagesArrayList.size() + " " + myPackagesArrayList.toString());
                            getMyPackagesAdapter.notifyDataSetChanged();
                            getMyPackagesAdapter.getFilter().filter("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (accsesscode == Connection.GetBillid.ordinal()) {
                    try {
                        dismissLoading();
                        //Log.e("received ", "received");
                        JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                        if (!isloadingstarted) {
                            isloadingstarted = true;
                            if (obj.has("orderid")) {
                                GlobalValues.bilid = obj.getString("orderid");
                                PaymentRequest.getInstance().setTransactionAmount(Packages_Textview_Toolbar_Payment.getText().toString());
                              //  PaymentRequest.getInstance().setTransactionAmount("2");
                                PaymentRequest.getInstance().setReferenceNo(obj.getString("orderid"));
                                PaymentRequest.getInstance().setBillingEmail("targeteducareapp@gmail.com");
                                PaymentRequest.getInstance().setBillingCity("Bhayander");
                                PaymentRequest.getInstance().setBillingCountry("IND");
                                PaymentRequest.getInstance().setBillingPostalCode("401101");
                                PaymentRequest.getInstance().setBillingPhone(GlobalValues.student.getMobile());
                                PaymentRequest.getInstance().setTransactionDescription("Paymet to get Ebooks of Android basic development ");
                                PaymentRequest.getInstance().setBillingName(GlobalValues.student.getMobile());
                                PaymentRequest.getInstance().setBillingAddress("Target Educare pvt ltd, 102, Bhakti Plaza Building, Bhayander West");
                                ArrayList<HashMap<String, String>> custom_post_parameters = new ArrayList<HashMap<String, String>>();
                                HashMap<String, String> hashpostvalues = new HashMap<String, String>();
                                hashpostvalues.put("account_details", "saving");
                                hashpostvalues.put("merchant_type", "gold");
                                custom_post_parameters.add(hashpostvalues);

                                PaymentRequest.getInstance()
                                        .setCustomPostValues(custom_post_parameters);

                                EBSPayment.getInstance().init(MyPackagesActivity.this, Constants.AccountId, Constants.SecretKey, Config.Mode.ENV_LIVE,
                                        Config.Encryption.ALGORITHM_MD5, Constants.AccountName);

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (accsesscode == Connection.GetBillidException.ordinal()) {

                } else if (accsesscode == Connection.GetReceiptid.ordinal()) {
                    finish();
                } else if (accsesscode == Connection.GetReceiptidException.ordinal()) {

                }

            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
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

        try {
            double s = (double) Double.parseDouble(String.valueOf(Packages_Textview_Toolbar_Payment.getText().toString()));
            //Log.e("amount  ", String.valueOf(s));
            //Log.e("add ", String.valueOf(sum));

            if (add == 1 && Packages_Textview_Toolbar_Payment.getText().toString() != null) {
                s += sum;

                //Log.e("total final amt ", String.valueOf(s));

                Packages_Textview_Toolbar_Payment.setText(String.valueOf(s));

                //  Log.e("Packages_Payment  :", Packages_TextView_Payment.getText().toString());

            } else {
                s -= sum;
                //Log.e("total final amt ", String.valueOf(s));

                Packages_Textview_Toolbar_Payment.setText(String.valueOf(s));

                //Log.e("Packages_Payment  :", Packages_TextView_Payment.getText().toString());
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
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

    public void opendialog(int position) {

       /* AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Title here");

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_layout, null);
        alert.setView(alertLayout);
        WebView wv = (WebView) alertLayout.findViewById(R.id.webview_dialog_discription);
        wv.loadUrl("http:\\www.google.com");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadData(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_layout, null);
        alert.setView(alertLayout);
        WebView dialog_discp = (WebView) alertLayout.findViewById(R.id.webview_dialog_discription);
        TextView dialog_heading = (TextView) alertLayout.findViewById((R.id.textview_dialog_heading));
        dialog_heading.setText(context.getResources().getString(R.string.dialog_details));
        TextView Textview_dialog_Amount = (TextView) alertLayout.findViewById(R.id.textview_dialog_amount);

        // holder.Packages_TextView_Amount.setTypeface(Fonter.getTypefacebold(context));

        Textview_dialog_Amount.setText("" + myPackagesArrayList.get(position).getAmount());
        TextView text1 = (TextView) alertLayout.findViewById(R.id.textview_course_name);

        if (!lang.equalsIgnoreCase("mr"))
            text1.setText(Html.fromHtml(myPackagesArrayList.get(position).getName()));
        else
            text1.setText(Html.fromHtml(myPackagesArrayList.get(position).getName_InMarathi()));


        if (lang.equalsIgnoreCase("mr")) {
            Log.e("marathi ", " " + myPackagesArrayList.get(position).getDescription_InMarathi());
            dialog_discp.loadData(myPackagesArrayList.get(position).getDescription_InMarathi().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
            //textView.setText("\" " + splashModels.get(position).getTitle_Marathi() + " \"");
        } else {
            Log.e("english ", " " + myPackagesArrayList.get(position).getDescription());

            dialog_discp.loadData(myPackagesArrayList.get(position).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
            // textView.setText("\" " + splashModels.get(position).getTitle() + " \"");
        }

        dialog_discp.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                view.loadData(url, "text/html", "utf-8");

                return true;
            }
        });
        alert.setCancelable(true);
        final AlertDialog dialog1 = alert.create();
        if (!((Activity) context).isFinishing()) {
            dialog1.show();
        }

        ImageView dialog_close = (ImageView) alertLayout.findViewById(R.id.dialog_close_button);

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
   /*     final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater inflater =getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_layout, null);
        alert.setView(alertLayout);

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
       // dialog.setContentView(R.layout.dialog_layout);

        WebView dialog_discp = (WebView) dialog.findViewById(R.id.webview_dialog_discription);

        dialog_discp.getSettings().setJavaScriptEnabled(true);
           TextView dialog_heading = (TextView) dialog.findViewById((R.id.textview_dialog_heading));
        dialog_heading.setText(context.getResources().getString(R.string.dialog_details));
        Log.e("data ", "data " + myPackagesArrayList.get(position).getDescription());

        //  dialog_discp.loadData(arrayList.get(position).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");




        TextView Textview_dialog_Amount = (TextView) dialog.findViewById(R.id.textview_dialog_amount);

       // holder.Packages_TextView_Amount.setTypeface(Fonter.getTypefacebold(context));

        Textview_dialog_Amount.setText("");

        TextView text1 = (TextView) dialog.findViewById(R.id.textview_course_name);

        *//* webView.loadData(splashModels.get(position).getDescription(),"text/html","uft-8");*//*
        if (lang.equalsIgnoreCase("mr")) {
            Log.e("marathi ", " " + myPackagesArrayList.get(position).getDescription_InMarathi());
            dialog_discp.loadData(myPackagesArrayList.get(position).getDescription_InMarathi().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
            //textView.setText("\" " + splashModels.get(position).getTitle_Marathi() + " \"");
        } else {
            dialog_discp.loadData( myPackagesArrayList.get(position).getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
            // textView.setText("\" " + splashModels.get(position).getTitle() + " \"");
        }

        //text1.setText(holder.Packages_TextView_Course_Name.getText());

        ImageView dialog_close = (ImageView) dialog.findViewById(R.id.dialog_close_button);

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }


}