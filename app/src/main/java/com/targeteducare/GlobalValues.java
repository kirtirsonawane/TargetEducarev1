package com.targeteducare;

import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.Student;
import com.targeteducare.Classes.StudentProfile;
import java.util.ArrayList;

public class GlobalValues {
    public static String TEMP_STR = "";
    public static Student student = new Student();
    public static int width = 500;
    public static String IP = "exam.targeteducare.com";
  // public static String IP = "192.168.1.59:8097";
   // public static String IP = "13.232.35.201";
    public static String bilid = "0";
    public static String selectedtesttype = "";
    public static StudentProfile studentProfile = new StudentProfile();
    public static String WEB_VIEW = "";
    public static ArrayList<Exam> mdataset_New = new ArrayList<>();
    public static ArrayList<Exam> mdataset_Missed = new ArrayList<>();
    public static ArrayList<Exam> mdataset_Attempted = new ArrayList<>();
    public static String langs = "mr";
    public static String version = "1.8";
    public static boolean isopen = false;
    public static long currentmillies = 0;
}
