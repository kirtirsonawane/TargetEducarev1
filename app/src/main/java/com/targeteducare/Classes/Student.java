package com.targeteducare.Classes;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {
    String Id = "";
    String Name = "";
    String RollNumber = "";
    String CenterId = "";
    String FatherName = "";
    String MotherName = "";
    String DOB = "";
    String Mobile = "";
    String Email = "";
    String QualificationId = "";
    String CountryId = "";
    String StateId = "";
    String CityId = "";
    String Password = "";
    String Address = "";
    String RegistrationDate = "";
    String CategoryId = "";
    String CasteCategory = "";
    String Adhar = "";
    String IsActive = "";
    String Deleted = "";
    String CreatedDate = "";
    String RoleName = "";
    String UserTheme = "";
    String InstituteName = "";
    String Institutelogo = "";
    String SubCategoryId = "";
    String Gender = "";
    String Nationality = "";
    String AltMobile = "";
    String AltEmail = "";
    String Totalamt = "";
    String dueAmt = "";
    String sanstha_id = "";
    String ReferalMobile = "";
    String Institutesign = "";
    String surname = "";
    String fullname = "";
    String board_name = "";
    String subboard_name = "";
    String state = "";
    String city = "";
    String district = "";
    String IsFaculty = "";
    int isomr = 0;
    long timetaken = 0;
    long lasttimetaken = 0;
    String lastvisiteddate = "";
    int useractive = 0;
    String IEMIno = "";
    int islogin = 0;
    ArrayList<Examfirebasedb> examdata = new ArrayList<>();

    public String getIEMIno() {
        return IEMIno;
    }

    public void setIEMIno(String IEMIno) {
        this.IEMIno = IEMIno;
    }

    public int getUseractive() {
        return useractive;
    }

    public void setUseractive(int useractive) {
        this.useractive = useractive;
    }

    public long getTimetaken() {
        return timetaken;
    }

    public void setTimetaken(long timetaken) {
        this.timetaken = timetaken;
    }

    public String getLastvisiteddate() {
        return lastvisiteddate;
    }

    public void setLastvisiteddate(String lastvisiteddate) {
        this.lastvisiteddate = lastvisiteddate;
    }

    public String getIsFaculty() {
        return IsFaculty;
    }

    public void setIsFaculty(String isFaculty) {
        IsFaculty = isFaculty;
    }

    public int getIsomr() {
        return isomr;
    }

    public void setIsomr(int isomr) {
        this.isomr = isomr;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    public String getSubboard_name() {
        return subboard_name;
    }

    public void setSubboard_name(String subboard_name) {
        this.subboard_name = subboard_name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getAltMobile() {
        return AltMobile;
    }

    public void setAltMobile(String altMobile) {
        AltMobile = altMobile;
    }

    public String getAltEmail() {
        return AltEmail;
    }

    public void setAltEmail(String altEmail) {
        AltEmail = altEmail;
    }

    public String getTotalamt() {
        return Totalamt;
    }

    public void setTotalamt(String totalamt) {
        Totalamt = totalamt;
    }

    public String getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getSanstha_id() {
        return sanstha_id;
    }

    public void setSanstha_id(String sanstha_id) {
        this.sanstha_id = sanstha_id;
    }

    public String getReferalMobile() {
        return ReferalMobile;
    }

    public void setReferalMobile(String referalMobile) {
        ReferalMobile = referalMobile;
    }

    public String getInstitutesign() {
        return Institutesign;
    }

    public void setInstitutesign(String institutesign) {
        Institutesign = institutesign;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRollNumber() {
        return RollNumber;
    }

    public void setRollNumber(String rollNumber) {
        RollNumber = rollNumber;
    }

    public String getCenterId() {
        return CenterId;
    }

    public void setCenterId(String centerId) {
        CenterId = centerId;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getQualificationId() {
        return QualificationId;
    }

    public void setQualificationId(String qualificationId) {
        QualificationId = qualificationId;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }

    public String getStateId() {
        return StateId;
    }

    public void setStateId(String stateId) {
        StateId = stateId;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        RegistrationDate = registrationDate;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCasteCategory() {
        return CasteCategory;
    }

    public void setCasteCategory(String casteCategory) {
        CasteCategory = casteCategory;
    }

    public String getAdhar() {
        return Adhar;
    }

    public void setAdhar(String adhar) {
        Adhar = adhar;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getDeleted() {
        return Deleted;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getUserTheme() {
        return UserTheme;
    }

    public void setUserTheme(String userTheme) {
        UserTheme = userTheme;
    }

    public String getInstituteName() {
        return InstituteName;
    }

    public void setInstituteName(String instituteName) {
        InstituteName = instituteName;
    }

    public String getInstitutelogo() {
        return Institutelogo;
    }

    public void setInstitutelogo(String institutelogo) {
        Institutelogo = institutelogo;
    }

    public long getLasttimetaken() {
        return lasttimetaken;
    }

    public void setLasttimetaken(long lasttimetaken) {
        this.lasttimetaken = lasttimetaken;
    }

    public int getIslogin() {
        return islogin;
    }

    public void setIslogin(int islogin) {
        this.islogin = islogin;
    }

    public ArrayList<Examfirebasedb> getExamdata() {
        return examdata;
    }

    public void setExamdata(ArrayList<Examfirebasedb> examdata) {
        this.examdata = examdata;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Id", Id);
        result.put("Name", Name);
        result.put("RollNumber", RollNumber);
        result.put("Mobile", Mobile);
        result.put("timetaken", timetaken);
        result.put("lastvisiteddate", lastvisiteddate);
        result.put("lasttimetaken", lasttimetaken);
        result.put("useractive", useractive);
        result.put("IEMIno", IEMIno);
        result.put("islogin", islogin);
        result.put("exam",examdata);
        Log.e("useractive ", "useractive " + useractive);
        return result;
    }
}
