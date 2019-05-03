package com.targeteducare;

import android.util.Log;

public class URLS {
    //public static final String BASE_URL = "http://"+GlobalValues.IP+"/CommonRoute/";
    //public static final String BASE_URL1 = "http://"+GlobalValues.IP+"/WebRoute/";

    public static String getBaseUrl(){
        return  "http://"+GlobalValues.IP+"/CommonRoute/";
    }
    public static String getBaseUrl1(){
        return  "http://"+GlobalValues.IP+"/WebRoute/";
    }
    public static String getBaseUrv1(){
        return  "http://192.168.1.59:8097/CommonRoute/";
    }
    public static String getBaseUrlup(){ return "http://192.168.1.59:8097/ExamRoute/";}
    public static String getBaseUrlimg(){return "http://192.168.1.59:8097/images/";}

    public static String LOGIN() {
        Log.e("urldata",getBaseUrl() +"Login_Api");
        return getBaseUrl() + "Login_Api";
    }

    public static String GET_EXAMS() {
        Log.e("urldata",getBaseUrl1() +"getexams_api");
        //Return getBaseUrl1() + "getexams_api";
        //return getBaseUrl1() + "getexams_api";
        return getBaseUrl1()+"getexams_api";
        //return "http://192.168.1.59:8097/WebRoute/getexams_api";
    }
    public static String GET_QUESTIONS() {
        Log.e("urldata",getBaseUrl1() +"getquestions_api");
        return getBaseUrl1()+ "getquestions_api";
    }
    public static String SAVE_ANSWERSHEET() {
        Log.e("urldata",getBaseUrl1() +"saveanswersheet");
        return getBaseUrl1() + "saveanswersheet";
    }
    public static String QUESTION_URL() {
        Log.e("urldata",getBaseUrl1() +"getquestionsUrl_Api ");
        return getBaseUrl1() + "getquestionsUrl_Api";
    }
    public static String Updatedata() {
        return getBaseUrl1() + "saveexamstudentstatus_ques";
    }

    public static String loginv1() {
        return getBaseUrv1() + "Api_Student_Login";
    }

    public static String signup() {
        return getBaseUrv1() + "Api_Student_Signup";
    }
    public static String getcategory() {
        return getBaseUrv1() + "Api_Category_Get";
    }

    public static String editprofile() {
        return getBaseUrv1() + "Api_GetStates_ById";
    }

    public static String getcity() {
        return getBaseUrv1() + "Api_GetCity_ById";
    }

    public static String updateprofile() {
        return getBaseUrlup() + "Student_Update";
    }

    public static String sendotp(String msg,String otp) {
        return  "https://www.businesssms.co.in/SMS.aspx?ID=drpbansal%40gmail.com&Pwd=Targeteducare%401234&PhNo="+
                msg+"&Text=Your otp for TEPL CBT is "+otp;
    }

    public static String getcourse() {
        return getBaseUrv1() + "Api_Course_Get";
    }

    public static String getsamplepaper() {
        return getBaseUrv1() + "Api_Student_Ebook_Get";
    }

    public static String getmy_package_image() {
        return getBaseUrlimg() + "uploadimages/20190411181207_red-2014577_1280.png";
    }
}
