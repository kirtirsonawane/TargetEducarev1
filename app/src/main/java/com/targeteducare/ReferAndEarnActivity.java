package com.targeteducare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.Classes.Student;

import java.lang.reflect.Type;

public class ReferAndEarnActivity extends Activitycommon {
    TextView tv_share, tv_copy, tv_refer_whatsapp, tv_view_earnings;
    ImageView iv_copy;
    CardView cv_refer_whatsapp;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    boolean isAppInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_refer_and_earn);

            tag = this.getClass().getSimpleName();
            setmaterialDesign();
            back();
            setTitle(getResources().getString(R.string.refer_earn));
            toolbar.setTitleMargin(30, 10, 10, 10);

            tv_share = findViewById(R.id.tv_share);
            tv_copy = findViewById(R.id.tv_copy);
            iv_copy = findViewById(R.id.iv_copy);
            tv_refer_whatsapp = findViewById(R.id.tv_refer_whatsapp);
            tv_view_earnings = findViewById(R.id.tv_view_earnings);
            cv_refer_whatsapp = findViewById(R.id.cv_refer_whatsapp);

            preferences = PreferenceManager.getDefaultSharedPreferences(ReferAndEarnActivity.this);
            edit = preferences.edit();

            Gson gson = new Gson();
            Type type = new TypeToken<Student>() {
            }.getType();
            GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
            preferences.getString("studentdetails", "");

            if (GlobalValues.student != null) {
                if (GlobalValues.student.getMobile() != null) {

                    String s = getResources().getString(R.string.copyreferralcode) + ": " + "<b>" + GlobalValues.student.getMobile() + "</b>";
                    tv_copy.setText(Html.fromHtml(s));
                }
            }

            tv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (GlobalValues.student != null) {
                            if (GlobalValues.student.getId() != null) {
                                //String msg = "Hello,Now  Tests are available in Marathi and English. I am using it. Just download the app Target Educare PEAK  https://play.google.com/store/apps/details?id=com.targeteducare \n use my refernce code TEPLPEAK" + GlobalValues.student.getId() + "\n " + "via  Target Educare PEAK";

                                if (lang.equalsIgnoreCase("mr")) {

                                    String msg="विविध स्पर्धा / प्रवेश / सरकारी नोकर्यांमध्ये चांगल्या कामगिरीसाठी आणि आपल्या मुलांच्या मानसिकतेचे समग्र विकास\n" +
                                            "TARGET  EDUCARE PEAK अॅप डाउनलोड करा."+ "http://exam.targeteducare.com/ExamAdmin/Referral/ReferralApp?mobile=" + GlobalValues.student.getMobile() +"वर क्लिक करा. कृपया रेफरल म्हणून "+GlobalValues.student.getMobile()+" वापरा.";
                                  /*  String msg = "आपल्या भविष्यातील चांगलेपणासाठी मित्रांनो, TARGET EDUCARE PEAK ऍप मध्ये सामील व्हा\n" +
                                            "मित्रांना निमंत्रण द्या - माझे रेफरल कोड वापरा " + GlobalValues.student.getMobile() + "\n" +
                                            "http://exam.targeteducare.com/ExamAdmin/Referral/ReferralApp?mobile=" + GlobalValues.student.getMobile();*/
                                    sharelinkusingintent(msg);
                                } else {/*Friends, join TARGET EDUCARE PEAK App for betterment of your future...\n" +
                                    "Invite friends - Use My Referral Code " + GlobalValues.student.getMobile() + "\n" +*/
                                    String msg ="For better performance in various competition / entrance / Govt jobs exams and overall development of your\n" +
                                            "kids mindset download TARGET EDUCARE PEAK app. Click on "+
                                            " http://exam.targeteducare.com/ExamAdmin/Referral/ReferralApp?mobile=" + GlobalValues.student.getMobile()+". Please use "+GlobalValues.student.getMobile()+" as referal";
                                    sharelinkusingintent(msg);
                                }

                            }
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });

            tv_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (GlobalValues.student != null) {
                            if (GlobalValues.student.getMobile() != null) {
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("copytoclipboard", GlobalValues.student.getMobile());
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(context, getResources().getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });

            iv_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (GlobalValues.student != null) {
                            if (GlobalValues.student.getMobile() != null) {
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("copytoclipboard", GlobalValues.student.getMobile());
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(context, getResources().getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });

            cv_refer_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        isAppInstalled = appInstalledOrNot("com.whatsapp");
                        if (isAppInstalled) {

                            if (GlobalValues.student != null) {
                                if (GlobalValues.student.getMobile() != null) {

                                    if (lang.equalsIgnoreCase("mr")) {
                                 /*       String text = "आपल्या भविष्यातील चांगलेपणासाठी मित्रांनो, TARGET EDUCARE PEAK ऍप मध्ये सामील व्हा\n" +
                                                "मित्रांना निमंत्रण द्या - माझे रेफरल कोड वापरा " + GlobalValues.student.getMobile() + "\n" +
                                                "http://exam.targeteducare.com/ExamAdmin/Referral/ReferralApp?mobile=" + GlobalValues.student.getMobile();*/
                                        String text="विविध स्पर्धा / प्रवेश / सरकारी नोकर्यांमध्ये चांगल्या कामगिरीसाठी आणि आपल्या मुलांच्या मानसिकतेचे समग्र विकास\n" +
                                                "TARGET  EDUCARE PEAK अॅप डाउनलोड करा."+ " http://exam.targeteducare.com/ExamAdmin/Referral/ReferralApp?mobile=" + GlobalValues.student.getMobile() +"वर क्लिक करा. कृपया रेफरल म्हणून "+GlobalValues.student.getMobile()+" वापरा.";
                                        shareviawhatsapp(text);
                                    } else {
                                 /*       String text = "Friends, join TARGET EDUCARE PEAK App for betterment of your future...\n" +
                                                "Invite friends - Use My Referral Code " + GlobalValues.student.getMobile() + "\n" +
                                                "http://exam.targeteducare.com/ExamAdmin/Referral/ReferralApp?mobile=" + GlobalValues.student.getMobile();*/
                                        String text ="For better performance in various competition / entrance / Govt jobs exams and overall development of your\n" +
                                                "kids mindset download TARGET EDUCARE PEAK app. Click on "+
                                                "http://exam.targeteducare.com/ExamAdmin/Referral/ReferralApp?mobile=" + GlobalValues.student.getMobile()+". Please use "+GlobalValues.student.getMobile()+" as referal";

                                        shareviawhatsapp(text);
                                    }

                                }
                            }
                        } else {
                            Toast.makeText(context, "Whatsapp is not installed on this device!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });

            tv_view_earnings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(ReferAndEarnActivity.this, RewardPointsActivity.class);
                        startActivity(i);
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }
}
