package com.targeteducare.Classes;

import java.io.Serializable;

public class EngColgDataModel implements Serializable {
     int logo_img=0;
     float rating;
     int reviews;
     String college_name;
     int established_year;
     int courses;
     String institute_type;
     String exam_name;



    public  int getLogo_img() {
        return logo_img;
    }

    public  void setLogo_img(int logo_img) {
        this.logo_img = logo_img;
    }

    public  float getRating() {
        return rating;
    }

    public  void setRating(float rating) {
        this.rating = rating;
    }

    public  int getReviews() {
        return reviews;
    }

    public  void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getCollege_name() {
        return college_name;
    }

    public  void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public  int getEstablished_year() {
        return established_year;
    }

    public  void setEstablished_year(int established_year) {
        this.established_year = established_year;
    }

    public  int getCourses() {
        return courses;
    }

    public  void setCourses(int courses) {
        this.courses = courses;
    }

    public String getInstitute_type() {
        return institute_type;
    }

    public  void setInstitute_type(String institute_type) {
        this.institute_type = institute_type;
    }

    public String getExam_name() {
        return exam_name;
    }

    public  void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public EngColgDataModel(int logo_img, float rating, int reviews, String college_name, int established_year ,
                            int courses, String institute_type, String exam_name) {
        this.logo_img = logo_img;
        this.rating = rating;
        this.reviews = reviews;
        this.college_name = college_name;
        this.established_year = established_year;
        this.courses = courses;
        this.institute_type = institute_type;
        this.exam_name = exam_name;
    }


}
