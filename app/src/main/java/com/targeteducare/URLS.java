package com.targeteducare;

import android.util.Log;

public class URLS {
    public static final String BASE_URL = "http://"+GlobalValues.IP+"/CommonRoute/";
    public static final String BASE_URL1 = "http://"+GlobalValues.IP+"/WebRoute/";

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

    public static String LOGIN() {
        Log.e("urldata",getBaseUrl() +"Login_Api");
        return getBaseUrl() + "Login_Api";
    }

    public static String GET_EXAMS() {
        Log.e("urldata",getBaseUrl1() +"getexams_api");
        return getBaseUrl1() + "getexams_api";
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
}