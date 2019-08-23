package com.targeteducare;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.targeteducare.Classes.SpinnerCity;
import com.targeteducare.Classes.SpinnerCenter;
import com.targeteducare.Classes.SpinnerSchoolorCollege;
import com.targeteducare.Classes.SpinnerStandard;
import com.targeteducare.Classes.SpinnerState;
import com.targeteducare.Classes.SpinnerStream;
import com.targeteducare.Classes.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class EditProfileActivity extends Activitycommon {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    EditText dateofbirth;
    private RadioGroup radioGroup;
    ImageView profileimageupdate;
    EditText et_name, et_fathersname, et_surname, et_mobno, et_alternateno, et_emailid, et_district;
    String id = "", city_id = "";
    TextView tv_username;
    Button update;
    RadioButton rb_female, rb_male, rb;
    JSONArray subrootboard;
    View vstandard, vcity, v_center;
    TextView tv_standard, tv_city, tv_center;
    Bitmap selectedImage;
    String iscategoryupdate = "";
    String RollNumber = "";
    int flag_permission = 0;
    String name_board = "";
    String name_subboard = "";
    int empty_name_school_college = 0;
    int empty_name_of_center = 0;
    String name_school_or_college = "";
    String name_center = "";
    String center_id = "";
    String state_name = "";
    String city_name = "";
    String date = "";
    int radiobutton_gender;
    String rb_text = "";
    Spinner spin_stream, spin_standard, spin_state, spin_city, spin_school_or_college, spin_center;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<SpinnerState> spinnerStates;
    ArrayAdapter<SpinnerState> arrayAdapterstate;
    ArrayList<SpinnerCity> spinnerCities;
    ArrayAdapter<SpinnerCity> arrayAdaptercity;
    ArrayList<SpinnerStream> spinnerStreams;
    ArrayAdapter<SpinnerStream> arrayAdapterstream;
    ArrayList<SpinnerStandard> spinnerStandards;
    ArrayAdapter<SpinnerStandard> arrayAdapterstandard;
    ArrayList<SpinnerSchoolorCollege> spinnerSchoolorCollege;
    ArrayAdapter<SpinnerSchoolorCollege> arrayAdapterSchoolorCollege;
    ArrayList<SpinnerCenter> spinnerCenter;
    ArrayAdapter<SpinnerCenter> arrayAdapterCenter;
    String check_str, check_subboardstr;
    String citycheckvalue = "";
    String lang = "";
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_profile);
            setmaterialDesign();
            back();
            setTitle(Constants.TITLE);
            toolbar.setTitleMargin(30, 10, 10, 10);
            StructureClass.defineContext(EditProfileActivity.this);

           // Log.e("in profile ", "in profile");
            tag = this.getClass().getSimpleName();
            spin_stream = findViewById(R.id.spinner_stream);
            spin_standard = findViewById(R.id.spinner_standard);
            spin_state = findViewById(R.id.spinner_state);
            spin_city = findViewById(R.id.spinner_city);
            spin_school_or_college = findViewById(R.id.spinner_college);
            spin_center = findViewById(R.id.spinner_center);
            preferences = PreferenceManager.getDefaultSharedPreferences(EditProfileActivity.this);
            editor = preferences.edit();
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");

            Gson gson = new Gson();
            Type type = new TypeToken<Student>() {
            }.getType();
            GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);

            vstandard = findViewById(R.id.viewforstandard);
            vcity = findViewById(R.id.vcity);
            v_center = findViewById(R.id.viewforcenter);
            dateofbirth = findViewById(R.id.edittext_dob);
            profileimageupdate = findViewById(R.id.profileimage);
            rb_male = findViewById(R.id.gendermale);
            rb_female = findViewById(R.id.genderfemale);
            radioGroup = findViewById(R.id.radiogroup_gender);
            et_name = findViewById(R.id.edittext_firstname);
            et_fathersname = findViewById(R.id.edittext_fathersname);
            et_surname = findViewById(R.id.edittext_surname);
            et_mobno = findViewById(R.id.edittext_mobileno1);
            et_alternateno = findViewById(R.id.edittext_mobileno2);
            et_emailid = findViewById(R.id.edittext_email);
            et_district = findViewById(R.id.edittext_district);
            tv_username = findViewById(R.id.tv_username);
            tv_standard = findViewById(R.id.tv_standard);
            tv_city = findViewById(R.id.tv_city);
            tv_center = findViewById(R.id.tv_center);
            update = findViewById(R.id.button_updateprofile);
            Log.e("name in edit prof is ", GlobalValues.student.getName());
            et_name.setText(GlobalValues.student.getName());
            et_fathersname.setText(GlobalValues.student.getFatherName());
            et_surname.setText(GlobalValues.student.getSurname());

            if (GlobalValues.student.getFullname().equals("")) {
                tv_username.setText(GlobalValues.student.getName() + " " + GlobalValues.student.getFatherName());
            } else {
                tv_username.setText(GlobalValues.student.getFullname());
            }

            Log.e("sanstha editprof ", GlobalValues.student.getSanstha_id());
            Log.e("center editprof ", GlobalValues.student.getCenterId());

            Log.e("Mobile number is ", GlobalValues.student.getMobile() + "");
            et_mobno.setText(GlobalValues.student.getMobile());
            et_mobno.setEnabled(false);
            et_alternateno.setText(GlobalValues.student.getAltMobile());

            dateofbirth.setText(GlobalValues.student.getDOB());
            et_emailid.setText(GlobalValues.student.getEmail());
            et_district.setText(GlobalValues.student.getDistrict());


            Log.e("Getting gender ", GlobalValues.student.getGender());
            if (GlobalValues.student.getGender().equalsIgnoreCase(getResources().getString(R.string.female)) || GlobalValues.student.getGender().equalsIgnoreCase("Female")) {
                rb_female.setChecked(true);
            } else if (GlobalValues.student.getGender().equalsIgnoreCase(getResources().getString(R.string.male)) || GlobalValues.student.getGender().equalsIgnoreCase("Male")) {
                rb_male.setChecked(true);
            } else {
                rb_male.setChecked(false);
                rb_female.setChecked(false);
            }
        /*if (GlobalValues.student.getGender().equalsIgnoreCase("Female")) {
            rb_female.setChecked(true);
        } else if (GlobalValues.student.getGender().equalsIgnoreCase("Male")) {
            rb_male.setChecked(true);
        } else {
            rb_male.setChecked(false);
            rb_female.setChecked(false);
        }*/

            et_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    tv_username.setText(et_name.getText().toString() + " " + et_fathersname.getText().toString() + " " + et_surname.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_fathersname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    tv_username.setText(et_name.getText().toString() + " " + et_fathersname.getText().toString() + " " + et_surname.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_surname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    tv_username.setText(et_name.getText().toString() + " " + et_fathersname.getText().toString() + " " + et_surname.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radiobutton_gender = radioGroup.getCheckedRadioButtonId();
                    if (radiobutton_gender != -1) {
                        rb = group.findViewById(radiobutton_gender);
                        rb_text = rb.getText().toString();
                    }
                }
            });

            dateofbirth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        if (!((Activity) context).isFinishing()) {
                            DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this,
                                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                    onDateSetListener,
                                    year, month, day);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            if (!((EditProfileActivity.this)).isFinishing()) {
                                dialog.show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            profileimageupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.e("Permission Denied "," in onClick image");
                    try {
                        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                            flag_permission = 1;
                        } else {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    try {
                        date = day + "/" + (month + 1) + "/" + year;
                        dateofbirth.setText(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.e("On Update click", "this is what happens..");
                        JSONObject objupdate = new JSONObject();
                        JSONObject objsubroot = new JSONObject();
                        JSONObject objroot = new JSONObject();
                        JSONObject objxml = new JSONObject();
                        JSONObject mainobjupdate = new JSONObject();

                        try {
                            objupdate.put("TotalRecord", "");
                            objupdate.put("Id", GlobalValues.student.getId());
                            objupdate.put("Name", et_name.getText().toString());
                            objupdate.put("RollNumber", GlobalValues.student.getRollNumber());
                            objupdate.put("CenterId", GlobalValues.student.getCenterId());
                            Log.e("sending center id as ", GlobalValues.student.getCenterId() + "");
                            objupdate.put("FatherName", et_fathersname.getText().toString());
                            objupdate.put("MotherName", "");
                            objupdate.put("DOB", dateofbirth.getText().toString());
                            objupdate.put("Mobile", et_mobno.getText().toString());
                            objupdate.put("Email", et_emailid.getText().toString());
                            objupdate.put("QualificationId", "");
                            objupdate.put("CountryId", GlobalValues.student.getCountryId());
                            objupdate.put("StateId", (int) Double.parseDouble(spinnerStates.get(spin_state.getSelectedItemPosition()).getState_id()));
                            Log.e("State id is ", String.valueOf((int) Double.parseDouble(spinnerStates.get(spin_state.getSelectedItemPosition()).getState_id())));

                            if (spinnerCities != null) {
                                if (spin_city.getSelectedItemPosition() >= 0 && (spin_city.getSelectedItemPosition() < spinnerCities.size())) {
                                    Log.e("City id is ", String.valueOf((int) Double.parseDouble(spinnerCities.get(spin_city.getSelectedItemPosition()).getCity_id())));
                                    objupdate.put("CityId", (int) Double.parseDouble(spinnerCities.get(spin_city.getSelectedItemPosition()).getCity_id()));//check
                                } else {
                                    objupdate.put("CityId", "");
                                }
                            } else {
                                objupdate.put("CityId", "");
                            }

                            objupdate.put("Password", "a");
                            objupdate.put("Address", "");
                            objupdate.put("Qualification", "");
                            objupdate.put("Center", "");
                            objupdate.put("RegistrationDate", "");
                            objupdate.put("Adhar", "");
                            if (spin_stream.getSelectedItemPosition() > 0 && spin_stream.getSelectedItemPosition() < spinnerStreams.size())
                                objupdate.put("CategoryId", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                            else
                                objupdate.put("CategoryId", "0");
                        /*if(spinnerStreams!=null){
                            if(spinnerStreams.size()>spin_stream.getSelectedItemPosition()){
                                objupdate.put("CategoryId", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                            }
                            else{
                                objupdate.put("CategoryId",0);
                            }
                        }
                        else{
                            objupdate.put("CategoryId",0);
                        }*/

                            objupdate.put("CasteCategory", "");

                            Log.e("Gender is ", rb_text);
                            objupdate.put("Gender", rb_text);
                            objupdate.put("Nationality", "Indian");
                            objupdate.put("AltMobile", et_alternateno.getText().toString());
                            objupdate.put("AltEmail", "");
                            objupdate.put("IsActive", "");

                            if ((spinnerStandards != null) && spinnerStandards.size() > 0) {
                                if (spinnerStandards.size() > spin_standard.getSelectedItemPosition()) {
                                    objupdate.put("SubCategoryId", spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                                } else objupdate.put("SubCategoryId", 0);
                            } else {
                                objupdate.put("SubCategoryId", 0);
                            }
                            if (spinnerSchoolorCollege.size() > 0) {
                                if (spin_school_or_college.getSelectedItemPosition() > 0 && spin_school_or_college.getSelectedItemPosition() < spinnerSchoolorCollege.size())
                                    objupdate.put("sanstha_id", spinnerSchoolorCollege.get(spin_school_or_college.getSelectedItemPosition()).getSanstha_id());
                                else objupdate.put("sanstha_id", 0);
                            } else objupdate.put("sanstha_id", 0);
                            // Log.e("sending sanstha id as ", spinnerSchoolorCollege.get(spin_school_or_college.getSelectedItemPosition()).getSanstha_id() + "");

                            if (spinnerStreams.size() > 0)
                                if (spin_stream.getSelectedItemPosition() > 0 && spin_stream.getSelectedItemPosition() < spinnerStates.size())
                                    objupdate.put("CategoryName", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                                else objupdate.put("CategoryName", "");
                            else objupdate.put("CategoryName", "");
                            objupdate.put("Totalamt", "");
                            objupdate.put("Packageamount", "");
                            objupdate.put("Type", "");
                            objupdate.put("PracticeId", "");
                            objupdate.put("DepartmentId", "");
                            objupdate.put("PackageDetails", new JSONArray());
                            objupdate.put("UserId", "");
                            objsubroot.put("subroot", objupdate);
                            objroot.put("root", objsubroot);
                            objxml.put("xml", objroot.toString());
                            mainobjupdate.put("FilterParameter", objxml.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ConnectionManager.getInstance(EditProfileActivity.this).updateprofile(mainobjupdate.toString());
                        //Log.e("subroot value",objsubroot.toString());
                        //Log.e("root value",objroot.toString());
                        //Log.e("xml value",objxml.toString());

                        Log.e("update service called: ", mainobjupdate.toString());

                        if (iscategoryupdate.equals("1")) {
                            Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.updated_profile), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.category_update_notallowed), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        /*spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

            spin_school_or_college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject mainobj = new JSONObject();
                    try {
                        jsonObject.put("PageNo", "1");
                        jsonObject.put("NoofRecords", "1000000");

                        Log.e("sanstha id edit ", GlobalValues.student.getSanstha_id());
                        jsonObject.put("Sansthaid", spinnerSchoolorCollege.get(position).getSanstha_id());
                        Log.e("sanstha id ", "in service call " + spinnerSchoolorCollege.get(position).getSanstha_id());

                        mainobj.put("FilterParameter", jsonObject.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ConnectionManager.getInstance(EditProfileActivity.this).getcenter(mainobj.toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(context, "position selected is "+i, Toast.LENGTH_SHORT).show();

                    /*String name = adapterView.getItemAtPosition(i).toString();
                    Log.e("String is", name);*/
                    try {

                        JSONObject obj1 = new JSONObject();
                        JSONObject mainobj1 = new JSONObject();

                        try {
                            obj1.put("StateName", Double.parseDouble(spinnerStates.get(i).getState_id()));
                            mainobj1.put("FilterParameter", obj1.toString());
                            Log.e("dataobj1 ", mainobj1.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ConnectionManager.getInstance(EditProfileActivity.this).getcity(mainobj1.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            JSONObject obj2 = new JSONObject();
            JSONObject mainobj2 = new JSONObject();
            try {
                obj2.put("Type", "Category");
                mainobj2.put("FilterParameter", obj2.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ConnectionManager.getInstance(EditProfileActivity.this).getcategory(mainobj2.toString());


            spin_stream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    try {
                        JSONArray jsonsubcategory = subrootboard.getJSONObject(i).optJSONArray("SubCategory");
                        //Log.e("this is the mainarray: ",c.toString());

                        if (jsonsubcategory != null) {

                            tv_standard.setVisibility(View.VISIBLE);
                            vstandard.setVisibility(View.VISIBLE);
                            spin_standard.setVisibility(View.VISIBLE);

                            spinnerStandards = new ArrayList<>();
                            for (int j = 0; j < jsonsubcategory.length(); j++) {

                                JSONObject c1 = jsonsubcategory.getJSONObject(j);
                                Log.e("this is the sub array: ", c1.toString());

                                String id_subboard = c1.getString("Id");

                                if ((!c1.getString("Name").equals("null")) || !(c1.getString("Name_InMarathi").equals("null"))) {
                                    if ((lang.equals("mr")) && !(c1.getString("Name_InMarathi").equals("null"))) {
                                        name_subboard = c1.getString("Name_InMarathi");
                                    } else if ((lang.equals("mr")) && (c1.getString("Name_InMarathi").equals("null"))) {
                                        name_subboard = "-";
                                    } else if ((lang.equals("En")) && (c1.getString("Name").equals("null"))) {
                                        name_subboard = "-";
                                    } else {
                                        name_subboard = c1.getString("Name");
                                    }

                                } else {
                                    name_subboard = "-";
                                }
                                String deleted_subboard = c1.getString("Deleted");
                                String abbr_subboard = c1.getString("Abbr");
                                String description_subboard = c1.getString("Description");
                                String subjectid_subboard = c1.getString("SubjectId");
                                String unitid_subboard = c1.getString("UnitId");
                                String chapterid_subboard = c1.getString("ChapterId");
                                String parentid_subboard = c1.getString("ParentId");

                                Log.e("subboard name", name_subboard);

                                SpinnerStandard spinnerStandard = new SpinnerStandard(name_subboard, id_subboard);
                                spinnerStandards.add(spinnerStandard);
                                //Log.e("spinner standards ",spinnerStandards.toString());
                            }
                            arrayAdapterstandard = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                            arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_standard.setAdapter(arrayAdapterstandard);
                            arrayAdapterstandard.notifyDataSetChanged();

                            check_subboardstr = GlobalValues.student.getSubCategoryId();
                            Log.e("subboard in editprof is", check_subboardstr);

                            for (int k = 0; k < spinnerStandards.size(); k++) {
                                Log.e("spin subboard name is ", spinnerStandards.get(k).getId());
                                if (spinnerStandards.get(k).getId().equalsIgnoreCase(check_subboardstr)) {
                                    spin_standard.setSelection(k);
                                    break;
                                }
                            }


                        } else {
                            tv_standard.setVisibility(View.VISIBLE);
                            vstandard.setVisibility(View.VISIBLE);
                            spin_standard.setVisibility(View.VISIBLE);
                            JSONObject c1 = subrootboard.getJSONObject(i).optJSONObject("SubCategory");
                            spinnerStandards = new ArrayList<>();
                            if (c1 != null) {
                                Log.e("this is the sub array: ", c1.toString());
                                String id_subboard = c1.getString("Id");

                                if ((!c1.getString("Name").equals("null")) || !(c1.getString("Name_InMarathi").equals("null"))) {
                                    if ((lang.equals("mr")) && !(c1.getString("Name_InMarathi").equals("null"))) {
                                        name_subboard = c1.getString("Name_InMarathi");
                                    } else if ((lang.equals("mr")) && (c1.getString("Name_InMarathi").equals("null"))) {
                                        name_subboard = "-";
                                    } else if ((lang.equals("En")) && (c1.getString("Name").equals("null"))) {
                                        name_subboard = "-";
                                    } else {
                                        name_subboard = c1.getString("Name");
                                    }

                                } else {
                                    name_subboard = "-";
                                }

                                String deleted_subboard = c1.getString("Deleted");
                                String abbr_subboard = c1.getString("Abbr");
                                String description_subboard = c1.getString("Description");
                                String subjectid_subboard = c1.getString("SubjectId");
                                String unitid_subboard = c1.getString("UnitId");
                                String chapterid_subboard = c1.getString("ChapterId");
                                String parentid_subboard = c1.getString("ParentId");
                                Log.e("subboard name", name_subboard);
                                SpinnerStandard spinnerStandard = new SpinnerStandard(name_subboard, id_subboard);
                                spinnerStandards.add(spinnerStandard);
                                arrayAdapterstandard = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                                arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin_standard.setAdapter(arrayAdapterstandard);
                                arrayAdapterstandard.notifyDataSetChanged();
                                check_subboardstr = GlobalValues.student.getSubboard_name();
                            } else {
                                tv_standard.setVisibility(View.GONE);
                                vstandard.setVisibility(View.GONE);
                                spin_standard.setVisibility(View.GONE);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                if (resultCode == RESULT_OK) {
                    try {

                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);

                        try {
                            OutputStream fout = null;
                        /*File f = File.createTempFile(Constants.PROFILE_PIC, Constants.FILE_NAME_EXT,
                                new File(StructureClass.generate()));*/
                            File f2 = new File(StructureClass.generate(context.getResources().getString(R.string.storage_name)));

                            File f1 = new File(f2.getAbsolutePath() + "/" + GlobalValues.student.getId() + Constants.PROFILE_PIC + Constants.FILE_NAME_EXT);
                            if ((f1.exists()) && (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                                fout = new FileOutputStream(f1.getAbsoluteFile());
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fout);
                                profileimageupdate.setImageBitmap(selectedImage);
                            } else if (!f1.exists()) {
                                f1.createNewFile();
                                fout = new FileOutputStream(f1.getAbsoluteFile());
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fout);
                                profileimageupdate.setImageBitmap(selectedImage);

                                Log.e("File does not exist ", " created, in onRequestPermissionsResult");
                            } else {
                                Log.e("Permission Denied ", " in onRequestPermissionsResult");
                                profileimageupdate.setImageResource(R.mipmap.default_profile);
                            }

                            Log.e("File path ", f1.toString());

                            if (fout != null) {
                                fout.flush();
                                fout.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.e("selection status ", "no image is selected!!");
                    //Toast.makeText(EditProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);

        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.EDITPROFILE.ordinal()) {
                    Log.e("res ", "reseditprofile " + GlobalValues.TEMP_STR);

                    JSONArray jsonArray = new JSONArray(GlobalValues.TEMP_STR);

                    if (jsonArray != null) {
                        spinnerStates = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            id = Integer.toString((int) Double.parseDouble(c.optString("Id")));

                            if (lang.equals("mr")) {
                                state_name = c.optString("stateName_InMarathi");
                            } else {
                                state_name = c.optString("Name");
                            }

                            String country_id = c.optString("CountryId");

                            SpinnerState spinnerState = new SpinnerState(state_name, id);
                            spinnerStates.add(spinnerState);

                        }
                        arrayAdapterstate = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStates);
                        arrayAdapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_state.setAdapter(arrayAdapterstate);
                        arrayAdapterstate.notifyDataSetChanged();

                        String statecheckvalue = GlobalValues.student.getStateId();
                        for (int statecheck = 0; statecheck < spinnerStates.size(); statecheck++) {
                            //Log.e("spinstate ","spinstate "+spinnerStates.get(statecheck).getState_id()+" "+statecheckvalue);
                            if (spinnerStates.get(statecheck).getState_id().equalsIgnoreCase(statecheckvalue)) {
                                spin_state.setSelection(statecheck);
                                break;
                            }
                            /*if (spinnerStates.get(statecheck).getStatename().equalsIgnoreCase(statecheckvalue)) {
                                spin_state.setSelection(statecheck);
                                break;
                            }*/
                        }

                    }

                    JSONObject object = new JSONObject();
                    JSONObject mainobject = new JSONObject();
                    try {
                        object.put("PageNo", "1");
                        object.put("NoofRecords", "1000000");
                        mainobject.put("FilterParameter", object.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ConnectionManager.getInstance(EditProfileActivity.this).getsanstha(mainobject.toString());

                } else if (accesscode == Connection.EDITPROFILEEXCPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.GETCITY.ordinal()) {
                    Log.e("res ", "resgetcity " + GlobalValues.TEMP_STR);


                    JSONArray jsonArray1 = new JSONArray(GlobalValues.TEMP_STR);

                    if (jsonArray1 != null) {

                        if (jsonArray1.length() > 0) {
                            tv_city.setVisibility(View.VISIBLE);
                            vcity.setVisibility(View.VISIBLE);
                            spin_city.setVisibility(View.VISIBLE);

                            spinnerCities = new ArrayList<>();

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject c1 = jsonArray1.getJSONObject(i);

                                city_id = Integer.toString((int) Double.parseDouble(c1.optString("Id")));
                                if (lang.equals("mr")) {
                                    city_name = c1.optString("CityName_InMarathi");
                                } else {
                                    city_name = c1.optString("Name");
                                }

                                String id = c1.optString("StateID");

                                SpinnerCity spinnerCity = new SpinnerCity(city_name, city_id);
                                spinnerCities.add(spinnerCity);
                            }
                            arrayAdaptercity = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerCities);
                            arrayAdaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_city.setAdapter(arrayAdaptercity);
                            arrayAdaptercity.notifyDataSetChanged();

                            citycheckvalue = GlobalValues.student.getCityId();
                            Log.e("Value of citycheck is", citycheckvalue);


                            for (int citycheck = 0; citycheck < spinnerCities.size(); citycheck++) {

                                if (spinnerCities.get(citycheck).getCity_id().equalsIgnoreCase(citycheckvalue)) {
                                    spin_city.setSelection(citycheck);
                                    break;
                                }
                            /*if (spinnerCities.get(citycheck).getCity_name().equalsIgnoreCase(citycheckvalue)) {
                                spin_city.setSelection(citycheck);
                                break;
                            }*/
                            }
                        } else {
                            Log.e("city size is ", "null");
                            tv_city.setVisibility(View.GONE);
                            vcity.setVisibility(View.GONE);
                            spin_city.setVisibility(View.GONE);
                        }
                    }

                } else if (accesscode == Connection.GETCITYEXCPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.GETCATEGORY.ordinal()) {

                    Log.e("res", "res " + GlobalValues.TEMP_STR);

                    spinnerStreams = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    subrootboard = jsonObject.getJSONObject("root").optJSONArray("subroot");

                    if (subrootboard != null) {
                        //response - array

                        for (int i = 0; i < subrootboard.length(); i++) {
                            JSONObject c = subrootboard.getJSONObject(i);

                            String totalrecord_board = c.getString("TotalRecord");
                            String id_board = c.getString("Id");
                            if (lang.equals("mr")) {
                                name_board = c.getString("Name_InMarathi");
                            } else {
                                name_board = c.getString("Name");
                            }
                            String abbr_board = c.getString("Abbr");
                            String description_board = c.getString("Description");
                            String parentid_board = c.getString("ParentId");
                            String deleted_board = c.getString("Deleted");
                            String subjectid_board = c.getString("SubjectId");
                            String unitidrecord_board = c.getString("UnitId");
                            String chapterid_board = c.getString("ChapterId");

                            SpinnerStream spinnerStream = new SpinnerStream(name_board, id_board);
                            spinnerStreams.add(spinnerStream);
                        }
                        arrayAdapterstream = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStreams);
                        arrayAdapterstream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_stream.setAdapter(arrayAdapterstream);
                        arrayAdapterstream.notifyDataSetChanged();

                        check_str = GlobalValues.student.getCategoryId();
                        Log.e("board name is ", check_str);
                        for (int check = 0; check < spinnerStreams.size(); check++) {
                            Log.e("spinner board name is ", spinnerStreams.get(check).getID());

                            if (spinnerStreams.get(check).getID().equalsIgnoreCase(check_str)) {
                                spin_stream.setSelection(check);
                                break;
                            }
                            /*if (spinnerStreams.get(check).getBoard_name().equalsIgnoreCase(check_str)) {
                                spin_stream.setSelection(check);
                                break;
                            }*/
                        }

                    } else {
                        //response - object
                        subrootboard = new JSONArray();
                        JSONObject c = jsonObject.getJSONObject("root").optJSONObject("subroot");
                        subrootboard.put(c);

                        String totalrecord_board = c.getString("TotalRecord");
                        String id_board = c.getString("Id");

                        if (lang.equals("mr")) {
                            name_board = c.getString("Name_InMarathi");
                        } else {
                            name_board = c.getString("Name");
                        }
                        String abbr_board = c.getString("Abbr");
                        String description_board = c.getString("Description");
                        String parentid_board = c.getString("ParentId");
                        String deleted_board = c.getString("Deleted");
                        String subjectid_board = c.getString("SubjectId");
                        String unitidrecord_board = c.getString("UnitId");
                        String chapterid_board = c.getString("ChapterId");

                        SpinnerStream spinnerStream = new SpinnerStream(name_board, id_board);
                        spinnerStreams.add(spinnerStream);
                        arrayAdapterstream = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStreams);
                        arrayAdapterstream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_stream.setAdapter(arrayAdapterstream);
                        arrayAdapterstream.notifyDataSetChanged();
                    }

                    JSONObject obj = new JSONObject();
                    JSONObject mainobj = new JSONObject();

                    try {
                        obj.put("CountryName", 1);
                        mainobj.put("FilterParameter", obj.toString());
                        Log.e("dataobj ", mainobj.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ConnectionManager.getInstance(EditProfileActivity.this).editprofile(mainobj.toString());
                } else if (accesscode == Connection.GETCATEGORYEXCEPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.UPDATEPROFILE.ordinal()) {
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject getjsonsubroot = jsonObject.getJSONObject("root").getJSONObject("subroot");
                    //Log.e("subroot check val ",getjsonsubroot.toString());

                    try {

                        if (getjsonsubroot != null) {
                            String Id = getjsonsubroot.optString("Id");
                            String Name = getjsonsubroot.optString("Name");
                            RollNumber = getjsonsubroot.optString("RollNumber");
                            String MotherName = getjsonsubroot.optString("MotherName");
                            String CenterId = getjsonsubroot.optString("CenterId");
                            String FatherName = getjsonsubroot.optString("FatherName");
                            String DOB = getjsonsubroot.optString("DOB");
                            String Mobile = getjsonsubroot.optString("Mobile");
                            String Email = getjsonsubroot.optString("Email");
                            String QualificationId = getjsonsubroot.optString("QualificationId");
                            String CountryId = getjsonsubroot.optString("CountryId");
                            String StateId = getjsonsubroot.optString("StateId");
                            String CityId = getjsonsubroot.optString("CityId");
                            String Password = getjsonsubroot.optString("Password");
                            String Address = getjsonsubroot.optString("Address");
                            String RegistrationDate = getjsonsubroot.optString("RegistrationDate");
                            String CategoryId = getjsonsubroot.optString("CategoryId");
                            String SubCategoryId = getjsonsubroot.optString("SubCategoryId");
                            String CasteCategory = getjsonsubroot.optString("CasteCategory");
                            String Adhar = getjsonsubroot.optString("Adhar");
                            String IsActive = getjsonsubroot.optString("IsActive");
                            String Gender = getjsonsubroot.optString("Gender");
                            String Nationality = getjsonsubroot.optString("Nationality");
                            String AltMobile = getjsonsubroot.optString("AltMobile");
                            String AltEmail = getjsonsubroot.optString("AltEmail");
                            String Totalamt = getjsonsubroot.optString("Totalamt");
                            String dueAmt = getjsonsubroot.optString("dueAmt");
                            String sanstha_id = getjsonsubroot.optString("sanstha_id");
                            String ReferalMobile = getjsonsubroot.optString("ReferalMobile");
                            String RoleName = getjsonsubroot.optString("RoleName");
                            String UserTheme = getjsonsubroot.optString("UserTheme");
                            String InstituteName = getjsonsubroot.optString("InstituteName");
                            String Institutelogo = getjsonsubroot.optString("Institutelogo");
                            String Institutesign = getjsonsubroot.optString("Institutesign");
                            iscategoryupdate = getjsonsubroot.optString("iscategoryupdate");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Gson gson = new Gson();

                    GlobalValues.student.setRollNumber(RollNumber);
                    GlobalValues.student.setName(et_name.getText().toString().trim());
                    GlobalValues.student.setFatherName(et_fathersname.getText().toString().trim());
                    GlobalValues.student.setSurname(et_surname.getText().toString().trim());
                    GlobalValues.student.setFullname(et_name.getText().toString().trim() + " " + et_fathersname.getText().toString().trim() + " " +
                            et_surname.getText().toString().trim());

                    GlobalValues.student.setDOB(dateofbirth.getText().toString());

                    Log.e("Set Gender is ", rb_text);

                    if (rb_text.equals(getResources().getString(R.string.female))) {
                        GlobalValues.student.setGender("Female");
                    } else {
                        GlobalValues.student.setGender("Male");
                    }

                    GlobalValues.student.setMobile(et_mobno.getText().toString());
                    GlobalValues.student.setAltMobile(et_alternateno.getText().toString());

                    GlobalValues.student.setEmail(et_emailid.getText().toString().trim());

                    GlobalValues.student.setBoard_name(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());

                    try {
                        if (spin_standard.getSelectedItemPosition() >= 0 && (spin_standard.getSelectedItemPosition() < spinnerStandards.size())) {
                            GlobalValues.student.setSubboard_name(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard());
                        } else {
                            GlobalValues.student.setSubboard_name("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Log.e("roll no test: ", GlobalValues.student.getId());

                    //Log.e("spinner state value ",spinnerStates.get(spin_state.getSelectedItemPosition()).getStatename());

                    GlobalValues.student.setState(spinnerStates.get(spin_state.getSelectedItemPosition()).getStatename());

                    try {

                        if ((spin_city.getSelectedItemPosition() >= 0) && (spin_city.getSelectedItemPosition() < spinnerCities.size())) {
                            GlobalValues.student.setCity(spinnerCities.get(spin_city.getSelectedItemPosition()).getCity_name());
                        } else {
                            GlobalValues.student.setCity("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tv_username.setText(GlobalValues.student.getFullname());

                    GlobalValues.student.setStateId(Integer.toString((int) Double.parseDouble(spinnerStates.get(spin_state.getSelectedItemPosition()).getState_id())));

                    if (spinnerCities != null) {
                        if (spin_city.getSelectedItemPosition() >= 0 && (spin_city.getSelectedItemPosition() < spinnerCities.size())) {
                            GlobalValues.student.setCityId(Integer.toString((int) Double.parseDouble(spinnerCities.get(spin_city.getSelectedItemPosition()).getCity_id())));
                        } else {
                            GlobalValues.student.setCityId("0");
                        }
                    } else {
                        GlobalValues.student.setCityId("0");
                    }

                    GlobalValues.student.setSanstha_id(spinnerSchoolorCollege.get(spin_school_or_college.getSelectedItemPosition()).getSanstha_id());

                    if (spinnerCenter != null) {
                        if ((spin_center.getSelectedItemPosition() >= 0) && (spin_center.getSelectedItemPosition() < spinnerCenter.size())) {
                            GlobalValues.student.setCenterId(spinnerCenter.get(spin_center.getSelectedItemPosition()).getCenter_id());
                            Log.e("spinner center val ", spinnerCenter.get(spin_center.getSelectedItemPosition()).getCenter_id());
                        } else {
                            GlobalValues.student.setCenterId("0");
                        }
                    } else {
                        GlobalValues.student.setCenterId("0");
                    }

                    Log.e("spinner college val ", spinnerSchoolorCollege.get(spin_school_or_college.getSelectedItemPosition()).getSanstha_id());


                    Log.e("selected student ", "selected student " + GlobalValues.student.getStateId() + " " + GlobalValues.student.getCityId());

                    //profile update
                    Log.e("iscategoryupdate ", iscategoryupdate);

                    try {

                        if (iscategoryupdate.equals("1")) {
                            GlobalValues.student.setCategoryId(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());

                            if (spinnerStandards != null && spinnerStandards.size() > 0) {
                                if (spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard().equals("-")) {
                                    GlobalValues.student.setSubCategoryId("1");
                                } else {
                                    GlobalValues.student.setSubCategoryId(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                                }
                            } else {
                                GlobalValues.student.setSubCategoryId("0");
                            }
                        } else {
                            check_str = GlobalValues.student.getCategoryId();
                            Log.e("board name is ", check_str);
                            for (int check = 0; check < spinnerStreams.size(); check++) {
                                Log.e("spinner board name is ", spinnerStreams.get(check).getID());

                                if (spinnerStreams.get(check).getID().equalsIgnoreCase(check_str)) {
                                    spin_stream.setSelection(check);
                                    break;
                                }
                            }

                            Log.e("subcat id ", GlobalValues.student.getSubCategoryId() + "");
                            check_subboardstr = GlobalValues.student.getSubCategoryId();
                            Log.e("subboard in editprof is", check_subboardstr);

                            for (int k = 0; k < spinnerStandards.size(); k++) {
                                Log.e("spin subboard name is ", spinnerStandards.get(k).getId());
                                if (spinnerStandards.get(k).getId().equalsIgnoreCase(check_subboardstr)) {
                                    spin_standard.setSelection(k);
                                    break;
                                }
                            }

                            GlobalValues.student.setCategoryId(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                            if (spinnerStandards != null && spinnerStandards.size() > 0) {
                                if (spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard().equals("-")) {
                                    GlobalValues.student.setSubCategoryId("1");
                                } else {
                                    GlobalValues.student.setSubCategoryId(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                                }
                            } else {
                                GlobalValues.student.setSubCategoryId("0");
                            }
                        }

                    } catch (Exception e) {
                        Log.e("oops ", "update failed");
                        e.printStackTrace();
                    }


                    String jsonstudent = gson.toJson(GlobalValues.student);
                    editor.putString("studentdetails", jsonstudent);
                    editor.apply();

                } else if (accesscode == Connection.UPDATEPROFILEEXCEPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.GET_SANSTHA.ordinal()) {
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);

                    try {
                        JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                        JSONObject objsubroot = jsonObject.getJSONObject("root");
                        JSONArray arraysubroot = objsubroot.optJSONArray("subroot");
                        spinnerSchoolorCollege = new ArrayList<>();
                        if (arraysubroot != null) {
                            for (int i = 0; i < arraysubroot.length(); i++) {
                                JSONObject a = arraysubroot.getJSONObject(i);
                                String TotalRecord = a.optString("TotalRecord");
                                String sanstha_id = a.getString("Id");

                                if (lang.equals("mr")) {
                                    if (a.optString("Name_InMarathi").equals("")) {
                                        empty_name_school_college = 1;
                                    } else {
                                        empty_name_school_college = 0;
                                        name_school_or_college = a.optString("Name_InMarathi");
                                    }
                                } else {
                                    name_school_or_college = a.optString("Name");
                                }


                                String Code = a.optString("Code");
                                String Address = a.optString("Address");
                                String logo = a.optString("logo");
                                String Email = a.optString("Email");
                                String Mobile = a.optString("Mobile");
                                String Website = a.optString("Website");
                                String UserName = a.optString("UserName");
                                String Password = a.optString("Password");
                                String Email2 = a.optString("Email2");
                                String Telephone1 = a.optString("Telephone1");
                                String Telephone2 = a.optString("Telephone2");
                                String CountryId = a.optString("CountryId");
                                String StateId = a.optString("StateId");
                                String CityId = a.optString("CityId");
                                String sign = a.optString("sign");
                                String Deleted = a.optString("Deleted");
                                Log.e("Name of college is ", name_school_or_college);

                                if (empty_name_school_college != 1) {
                                    SpinnerSchoolorCollege spinnerSchoolorColleges = new SpinnerSchoolorCollege(sanstha_id, name_school_or_college);
                                    spinnerSchoolorCollege.add(spinnerSchoolorColleges);
                                } else {
                                }

                            }

                            arrayAdapterSchoolorCollege = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerSchoolorCollege);
                            arrayAdapterSchoolorCollege.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Log.e("adapter value ", arrayAdapterSchoolorCollege.toString());
                            spin_school_or_college.setAdapter(arrayAdapterSchoolorCollege);
                            arrayAdapterSchoolorCollege.notifyDataSetChanged();

                            String chk_sanstha = GlobalValues.student.getSanstha_id();
                            for (int i = 0; i < spinnerSchoolorCollege.size(); i++) {
                                if (spinnerSchoolorCollege.get(i).getSanstha_id().equals(chk_sanstha)) {
                                    spin_school_or_college.setSelection(i);
                                    break;
                                }
                            }

                            /*String City_id = GlobalValues.student.getCityId();
                            for(int i =0; i<spinnerSchoolorCollege.size(); i++){
                                if(spinnerSchoolorCollege.get(i).getCity_id().equals(City_id)){
                                    spin_school_or_college.setSelection(i);
                                    break;
                                }
                            }*/
                        } else {

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (accesscode == Connection.GET_SANSTHAEXCEPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.GETCENTER.ordinal()) {
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);

                    try {
                        JSONObject object = new JSONObject(GlobalValues.TEMP_STR);
                        JSONObject root = object.optJSONObject("root");

                        if (root != null) {
                            Log.e("in ", "not null");
                            JSONObject subroot = root.optJSONObject("subroot");

                            if (subroot != null) {
                                Log.e("in subroot ", "not null " + subroot.toString());
                                Log.e("in subroot ", "not null root " + root.toString());
                                String error = subroot.optString("error");
                                if (error.equals("")) {

                                    JSONArray arraycenter = root.optJSONArray("subroot");
                                    if (arraycenter != null) {
                                        Log.e("hello ", " from array");
                                        if (arraycenter.length() > 0) {
                                            v_center.setVisibility(View.VISIBLE);
                                            tv_center.setVisibility(View.VISIBLE);
                                            spin_center.setVisibility(View.VISIBLE);
                                            spinnerCenter = new ArrayList<>();

                                            for (int i = 0; i < arraycenter.length(); i++) {
                                                JSONObject a = arraycenter.getJSONObject(i);
                                                String TotalRecord = a.optString("TotalRecord");
                                                center_id = a.optString("Id");

                                                if (lang.equals("mr")) {
                                                    //name_center = a.optString("Name_InMarathi");

                                                    if (a.optString("Name_InMarathi").equals("")) {
                                                        empty_name_of_center = 1;
                                                    } else {
                                                        empty_name_of_center = 0;
                                                        name_center = a.optString("Name_InMarathi");
                                                    }

                                                } else {
                                                    name_center = a.optString("Name");
                                                }

                                                String Code = a.optString("Code");
                                                String Address = a.optString("Address");
                                                String logo = a.optString("logo");
                                                String Email = a.optString("Email");
                                                String Mobile = a.optString("Mobile");
                                                String Website = a.optString("Website");
                                                String UserName = a.optString("UserName");
                                                String Password = a.optString("Password");
                                                String Email2 = a.optString("Email2");
                                                String Telephone1 = a.optString("Telephone1");
                                                String Telephone2 = a.optString("Telephone2");
                                                String CountryId = a.optString("CountryId");
                                                String StateId = a.optString("StateId");
                                                String CityId = a.optString("CityId");
                                                String sign = a.optString("sign");
                                                String Deleted = a.optString("Deleted");
                                                String SansthaId = a.optString("SansthaId");
                                                String SansthaName = a.optString("SansthaName");

                                                if (empty_name_of_center != 1) {
                                                    SpinnerCenter spinnerSansthas = new SpinnerCenter(center_id, SansthaId, name_center);
                                                    spinnerCenter.add(spinnerSansthas);
                                                } else {
                                                }

                                            }
                                            arrayAdapterCenter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerCenter);
                                            arrayAdapterCenter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                            spin_center.setAdapter(arrayAdapterCenter);
                                            arrayAdapterCenter.notifyDataSetChanged();

                                            String chk_center = GlobalValues.student.getCenterId();
                                            for (int i = 0; i < spinnerCenter.size(); i++) {
                                                if (spinnerCenter.get(i).getCenter_id().equals(chk_center)) {
                                                    spin_center.setSelection(i);
                                                    break;
                                                }
                                            }

                                        } else {
                                            v_center.setVisibility(View.GONE);
                                            tv_center.setVisibility(View.GONE);
                                            spin_center.setVisibility(View.GONE);
                                        }


                                    } else {
                                        Log.e("hello ", " from object");
                                        v_center.setVisibility(View.VISIBLE);
                                        tv_center.setVisibility(View.VISIBLE);
                                        spin_center.setVisibility(View.VISIBLE);
                                        spinnerCenter = new ArrayList<>();
                                        JSONObject objcenter = root.optJSONObject("subroot");
                                        Log.e("subroot obj ", objcenter.toString());

                                        String TotalRecord = objcenter.optString("TotalRecord");
                                        center_id = objcenter.optString("Id");

                                        if (lang.equals("mr")) {
                                            //name_center = a.optString("Name_InMarathi");

                                            if (objcenter.optString("Name_InMarathi").equals("")) {
                                                empty_name_of_center = 1;
                                            } else {
                                                empty_name_of_center = 0;
                                                name_center = objcenter.optString("Name_InMarathi");
                                            }

                                        } else {
                                            name_center = objcenter.optString("Name");
                                        }

                                        String Code = objcenter.optString("Code");
                                        String Address = objcenter.optString("Address");
                                        String logo = objcenter.optString("logo");
                                        String Email = objcenter.optString("Email");
                                        String Mobile = objcenter.optString("Mobile");
                                        String Website = objcenter.optString("Website");
                                        String UserName = objcenter.optString("UserName");
                                        String Password = objcenter.optString("Password");
                                        String Email2 = objcenter.optString("Email2");
                                        String Telephone1 = objcenter.optString("Telephone1");
                                        String Telephone2 = objcenter.optString("Telephone2");
                                        String CountryId = objcenter.optString("CountryId");
                                        String StateId = objcenter.optString("StateId");
                                        String CityId = objcenter.optString("CityId");
                                        String sign = objcenter.optString("sign");
                                        String Deleted = objcenter.optString("Deleted");
                                        String SansthaId = objcenter.optString("SansthaId");
                                        String SansthaName = objcenter.optString("SansthaName");


                                        if (empty_name_of_center != 1) {
                                            SpinnerCenter spinnerSansthas = new SpinnerCenter(center_id, SansthaId, name_center);
                                            spinnerCenter.add(spinnerSansthas);
                                        } else {
                                        }

                                        arrayAdapterCenter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerCenter);
                                        arrayAdapterCenter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                        spin_center.setAdapter(arrayAdapterCenter);
                                        arrayAdapterCenter.notifyDataSetChanged();

                                        //newlyadded
                                        Log.e("sanstha id edit ", GlobalValues.student.getSanstha_id());
                                        Log.e("center id edit ", GlobalValues.student.getCenterId());
                                        String chk_center = GlobalValues.student.getCenterId();
                                        for (int i = 0; i < spinnerCenter.size(); i++) {
                                            if (spinnerCenter.get(i).getCenter_id().equals(chk_center)) {
                                                spin_center.setSelection(i);
                                                break;
                                            }
                                        }

                                    }
                                } else {
                                    Log.e("error value ", error);
                                    v_center.setVisibility(View.GONE);
                                    tv_center.setVisibility(View.GONE);
                                    spin_center.setVisibility(View.GONE);

                                }
                            } else {
                                Log.e("in subroot", " null");
                                JSONArray arraycenter = root.optJSONArray("subroot");
                                if (arraycenter != null) {
                                    Log.e("hello ", " from array");
                                    if (arraycenter.length() > 0) {
                                        v_center.setVisibility(View.VISIBLE);
                                        tv_center.setVisibility(View.VISIBLE);
                                        spin_center.setVisibility(View.VISIBLE);
                                        spinnerCenter = new ArrayList<>();

                                        for (int i = 0; i < arraycenter.length(); i++) {
                                            JSONObject a = arraycenter.getJSONObject(i);
                                            String TotalRecord = a.optString("TotalRecord");
                                            center_id = a.optString("Id");

                                            if (lang.equals("mr")) {
                                                //name_center = a.optString("Name_InMarathi");

                                                if (a.optString("Name_InMarathi").equals("")) {
                                                    empty_name_of_center = 1;
                                                } else {
                                                    empty_name_of_center = 0;
                                                    name_center = a.optString("Name_InMarathi");
                                                }

                                            } else {
                                                name_center = a.optString("Name");
                                            }

                                            String Code = a.optString("Code");
                                            String Address = a.optString("Address");
                                            String logo = a.optString("logo");
                                            String Email = a.optString("Email");
                                            String Mobile = a.optString("Mobile");
                                            String Website = a.optString("Website");
                                            String UserName = a.optString("UserName");
                                            String Password = a.optString("Password");
                                            String Email2 = a.optString("Email2");
                                            String Telephone1 = a.optString("Telephone1");
                                            String Telephone2 = a.optString("Telephone2");
                                            String CountryId = a.optString("CountryId");
                                            String StateId = a.optString("StateId");
                                            String CityId = a.optString("CityId");
                                            String sign = a.optString("sign");
                                            String Deleted = a.optString("Deleted");
                                            String SansthaId = a.optString("SansthaId");
                                            String SansthaName = a.optString("SansthaName");


                                            if (empty_name_of_center != 1) {
                                                SpinnerCenter spinnerSansthas = new SpinnerCenter(center_id, SansthaId, name_center);
                                                spinnerCenter.add(spinnerSansthas);
                                            } else {
                                            }

                                        }
                                        arrayAdapterCenter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerCenter);
                                        arrayAdapterCenter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                        spin_center.setAdapter(arrayAdapterCenter);
                                        arrayAdapterCenter.notifyDataSetChanged();

                                        String chk_center = GlobalValues.student.getCenterId();
                                        for (int i = 0; i < spinnerCenter.size(); i++) {
                                            if (spinnerCenter.get(i).getCenter_id().equals(chk_center)) {
                                                spin_center.setSelection(i);
                                                break;
                                            }
                                        }

                                    } else {
                                        v_center.setVisibility(View.GONE);
                                        tv_center.setVisibility(View.GONE);
                                        spin_center.setVisibility(View.GONE);
                                    }


                                } else {
                                    Log.e("hello ", " from object");
                                    v_center.setVisibility(View.VISIBLE);
                                    tv_center.setVisibility(View.VISIBLE);
                                    spin_center.setVisibility(View.VISIBLE);
                                    spinnerCenter = new ArrayList<>();
                                    JSONObject objcenter = root.optJSONObject("subroot");
                                    Log.e("subroot obj ", objcenter.toString());

                                    String TotalRecord = objcenter.optString("TotalRecord");
                                    center_id = objcenter.optString("Id");

                                    if (lang.equals("mr")) {
                                        //name_center = a.optString("Name_InMarathi");

                                        if (objcenter.optString("Name_InMarathi").equals("")) {
                                            empty_name_of_center = 1;
                                        } else {
                                            empty_name_of_center = 0;
                                            name_center = objcenter.optString("Name_InMarathi");
                                        }

                                    } else {
                                        name_center = objcenter.optString("Name");
                                    }

                                    String Code = objcenter.optString("Code");
                                    String Address = objcenter.optString("Address");
                                    String logo = objcenter.optString("logo");
                                    String Email = objcenter.optString("Email");
                                    String Mobile = objcenter.optString("Mobile");
                                    String Website = objcenter.optString("Website");
                                    String UserName = objcenter.optString("UserName");
                                    String Password = objcenter.optString("Password");
                                    String Email2 = objcenter.optString("Email2");
                                    String Telephone1 = objcenter.optString("Telephone1");
                                    String Telephone2 = objcenter.optString("Telephone2");
                                    String CountryId = objcenter.optString("CountryId");
                                    String StateId = objcenter.optString("StateId");
                                    String CityId = objcenter.optString("CityId");
                                    String sign = objcenter.optString("sign");
                                    String Deleted = objcenter.optString("Deleted");
                                    String SansthaId = objcenter.optString("SansthaId");
                                    String SansthaName = objcenter.optString("SansthaName");

                                    if (empty_name_of_center != 1) {
                                        SpinnerCenter spinnerSansthas = new SpinnerCenter(center_id, SansthaId, name_center);
                                        spinnerCenter.add(spinnerSansthas);
                                    } else {
                                    }

                                    arrayAdapterCenter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerCenter);
                                    arrayAdapterCenter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                    spin_center.setAdapter(arrayAdapterCenter);
                                    arrayAdapterCenter.notifyDataSetChanged();

                                }
                            }


                        } else {
                            Log.e("in ", "null");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (accesscode == Connection.GETCENTEREXCEPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                try {
                    if (flag_permission == 1) {
                        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_CONTACTS);
                            flag_permission = 1;
                        } else {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                        }
                    } else {

                        try {
                            OutputStream fout = null;
                        /*File f = File.createTempFile(Constants.PROFILE_PIC, Constants.FILE_NAME_EXT,
                                new File(StructureClass.generate()));*/
                            File f2 = new File(StructureClass.generate(context.getResources().getString(R.string.storage_name)));
                            File f1 = new File(f2.getAbsolutePath() + "/" + GlobalValues.student.getId() + Constants.PROFILE_PIC + Constants.FILE_NAME_EXT);
                            if ((f1.exists()) && (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                                fout = new FileOutputStream(f1.getAbsoluteFile());
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fout);
                                profileimageupdate.setImageBitmap(selectedImage);
                            } else if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                f1.createNewFile();
                                fout = new FileOutputStream(f1.getAbsoluteFile());
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fout);
                                profileimageupdate.setImageBitmap(selectedImage);

                                //profileimageupdate.setImageResource(R.mipmap.default_profile);
                            } else {
                                Log.e("Permission Denied ", " in onRequestPermissionsResult");
                                profileimageupdate.setImageResource(R.mipmap.default_profile);
                            }

                            Log.e("File path ", f1.toString());

                            fout.flush();
                            fout.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                }
        }
        return;
    }

    @Override
    protected void onResume() {
        //profileimageupdate.setImageResource(R.mipmap.default_profile);

        try {
            StructureClass.defineContext(EditProfileActivity.this);
            File f2 = new File(StructureClass.generate(context.getResources().getString(R.string.storage_name)));
            File f1 = new File(f2.getAbsolutePath() + "/" + GlobalValues.student.getId() + Constants.PROFILE_PIC + Constants.FILE_NAME_EXT);
            Log.e("profile image path ", f1.toString());

            if ((f1.exists()) && (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

                Bitmap myBitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());

                profileimageupdate.setImageBitmap(myBitmap);

            } else if (!f1.exists()) {
                profileimageupdate.setImageResource(R.mipmap.default_profile);
            } else {
                Log.e("Permission Denied ", " in onResume()");
            }
        } catch (OutOfMemoryError error){
            reporterror(tag, error.toString());
        }catch (Exception e) {
            reporterror(tag, e.toString());
        }
        super.onResume();
    }
}


