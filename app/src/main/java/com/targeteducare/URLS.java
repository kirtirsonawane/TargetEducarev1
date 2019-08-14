package com.targeteducare;

import android.util.Log;
public class URLS {
    //public static final String BASE_URL = "http://"+GlobalValues.IP+"/CommonRoute/";
    //public static final String BASE_URL1 = "http://"+GlobalValues.IP+"/WebRoute/";
    public static String getBaseUrl() {
        return "http://" + GlobalValues.IP + "/CommonRoute/";
    }

    public static String getBaseUrl1() {
        return "http://" + GlobalValues.IP + "/WebRoute/";
    }

    public static String getBaseUrv1() {
        return "http://" + GlobalValues.IP + "/CommonRoute/";
    }

    public static String getBaseUrlup() {
        return "http://" + GlobalValues.IP + "/ExamRoute/";
    }
   /* public static String getBaseUrl(){
        return  "http://"+"exam.targeteducare.com"+"/CommonRoute/";
    }
    public static String getBaseUrl1(){
        return  "http://"+"exam.targeteducare.com"+"/WebRoute/";
    }
    public static String getBaseUrv1(){
        return  "http://"+"exam.targeteducare.com"+"/CommonRoute/";
    }
    public static String getBaseUrlup(){ return "http://exam.targeteducare.com/ExamRoute/";}*/


    public static String getBaseUrlimg() {
        return "http://" + GlobalValues.IP + "/images/";
    }

    public static String LOGIN() {
        Log.e("urldata", getBaseUrl() + "Login_Api");
        return getBaseUrl1() + "Login_Api";
    }

    public static String GET_EXAMS() {
        Log.e("urldata", getBaseUrl1() + "getexams_api");
        //Return getBaseUrl1() + "getexams_api";
        //return getBaseUrl1() + "getexams_api";
        return getBaseUrl1() + "getexams_api";
        //return "http://192.168.1.59:8097/WebRoute/getexams_api";
    }

    public static String GET_QUESTIONS() {
        Log.e("urldata", getBaseUrl1() + "getquestions_api");
        return getBaseUrl1() + "getquestions_api";
    }

    public static String GET_Ans() {
        Log.e("urldata", getBaseUrl1() + "getanswersheet_api");
        return getBaseUrl1() + "getanswersheet_api";
    }

    public static String SAVE_ANSWERSHEET() {
        Log.e("urldata", getBaseUrl1() + "saveanswersheet");
        return getBaseUrl1() + "saveanswersheet";
    }

    public static String QUESTION_URL() {
        Log.e("urldata", getBaseUrl1() + "getquestionsUrl_Api ");
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


    public static String sendotp(String msg, String otp) {
        return "https://www.businesssms.co.in/SMS.aspx?ID=info%40targeteducare.com&Pwd=bansals&PhNo="+ msg +"&Text=" + otp;
    }

   /* public static String sendotp(String msg, String otp) {
        return "https://www.businesssms.co.in/SMS.aspx?ID=drpbansal%40gmail.com&Pwd=Targeteducare%401234&PhNo="+ msg +"&Text=Your otp for TEPL PEAK is " + otp;
    }
*/
    public static String getsanstha() {
        return getBaseUrv1() + "Api_Sanstha_Get";
    }

    public static String getcenter() {
        return getBaseUrv1() + "Api_Center_Get";
    }

    public static String getcourse() {
        return getBaseUrv1() + "Api_Course_Get";
    }

    public static String getsamplepaper() {
        return getBaseUrv1() + "Api_Student_Ebook_Get";
    }

    public static String savepracticetest() {
        return getBaseUrlup() + "Api_Exam_SaveResult_PracticeTest";
    }

    public static String getbillid() {
        return getBaseUrlup() + "Student_SavePackage";
    }

    public static String getmy_package_image() {
        return getBaseUrlimg() + "uploadimages/20190411181207_red-2014577_1280.png";
    }

    public static String getresultremark() {
        return getBaseUrlup() + "Api_GetResultRemark";
    }

    public static String url_chatbot() {
        return "https://tawk.to/chat/5ce4f8ae2846b90c57afca85/default";
    }

    public static String getstudentpackage() {
        return getBaseUrlup() + "Student_GetPackage_Api";
    }

    public static String packages_image_url() {
        return "http://exam.targeteducare.com/images/uploadimages/";
    }

    public static String syllabus_url() {
        return "http://" + GlobalValues.IP + "/Home/syllabus";
    }


    public static String report_error() {
        return getBaseUrlup() + "saveerrordetail";
    }

    public static String report_error_forquestion() {
        return "http://" + GlobalValues.IP + "/CommonRoute/Api_Student_ReportError";
    }

    public static String reward_points() {
        return "http://" + GlobalValues.IP + "/ExamRoute/ReferralRewardPoint_Get";
    }

    public static String getevent_feedback() {
        return "http://" + GlobalValues.IP + "/ExamRoute/Api_EventGet";
    }

    public static String getfill_feedback() {
        return "http://" + GlobalValues.IP + "/ExamRoute/Api_Event_GetQuestion";
    }

    public static String save_questionFeedback() {
        return "http://" + GlobalValues.IP + "/ExamRoute/Api_SavequestionFeedback";
    }


    public static String getcategoryurl() {
        return getBaseUrlup() + "Api_SupportCategory_Get";
    }

    public static String getchatmessages() {
        return getBaseUrlup() + "Api_GetSupportTokenDetails";
    }

    public static String savechatmessages() {
        return getBaseUrlup() + "Api_SaveSupportToken";
    }

    public static String tokendetailschatmessages() {
        return getBaseUrlup() + "Api_SaveSupportTokenDetails";
    }


    ///----------Splash Screen---------///
    public static String splash_screen_images() {
        return getBaseUrlup() + "Api_PromotionDetails_Get";
    }


}
