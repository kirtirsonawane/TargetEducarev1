package com.targeteducare;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class PaymentSuccessActivity extends Activitycommon {
    // TextView txt;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_payment_success);
            setmaterialDesign();
            back();

            registerreceiver();
            Intent intent = getIntent();
            String payment_id = intent.getStringExtra("payment_id");
            Log.e("payment_id ", "payment_id " + payment_id);
            JSONObject obj = new JSONObject(payment_id);
            String PaymentId = "";
            if (obj.has("PaymentId"))
                PaymentId = obj.getString("PaymentId");

            String PaymentMode = "";
            if (obj.has("PaymentMode"))
                PaymentMode = obj.getString("PaymentMode");

            String PaymentStatus = "";
            if (obj.has("PaymentStatus"))
                PaymentStatus = obj.getString("PaymentStatus");

            if (!PaymentStatus.equalsIgnoreCase("Authorized"))
                PaymentStatus = "Failed";
            else
                PaymentStatus = "Confirm";

            String TransactionId = "";
            if (obj.has("TransactionId"))
                TransactionId = obj.getString("TransactionId");

            String Amount = "";
            if (obj.has("Amount"))
                Amount = obj.getString("Amount");


            obj.put("PayMode", PaymentMode);
            obj.put("Transctionno", TransactionId);
            obj.put("BankName", "");
            obj.put("dueamount", "0");
            obj.put("PayRemark", "");
            obj.put("DDCheque", "");
            Calendar cal = Calendar.getInstance();
            String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            obj.put("Date", d);
            obj.put("totalamount", Amount);
            obj.put("status", PaymentStatus);
            obj.put("billdate", d);
            obj.put("PackageDetails", new JSONArray());
            obj.put("Id", 0);
            JSONObject rootobj = new JSONObject();
            JSONObject subrootobj = new JSONObject();
            JSONObject xmlobj = new JSONObject();
            subrootobj.put("subroot", obj);
            rootobj.put("root", subrootobj);
            xmlobj.put("xml", rootobj.toString());
            xmlobj.put("StudentId", GlobalValues.student.getId());
            xmlobj.put("billid", GlobalValues.bilid);
            xmlobj.put("flag", "new");
            JSONObject mainobj = new JSONObject();
            mainobj.put("FilterParameter", xmlobj.toString());
            Log.e("obj ", "obj " + xmlobj.toString());
            img = (ImageView) findViewById(R.id.imageview_1);
            genloading("loading...");
            ConnectionManager.getInstance(PaymentSuccessActivity.this).getbillid(mainobj.toString(), 0);

            Log.e("payment ", "payment " + payment_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GetReceiptid.ordinal()) {
                try {
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    Log.e("obj", "ohbj" + obj.toString());
                    if (obj.has("ReceiptId")) {
                        if (obj.getInt("ReceiptId") < 0) {
                            img.setImageDrawable(getResources().getDrawable(R.drawable.def_failed));

                        } else {
                            Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_LONG);
                            img.setImageDrawable(getResources().getDrawable(R.drawable.def_success));
                        }
                    } else {
                        img.setImageDrawable(getResources().getDrawable(R.drawable.def_failed));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
