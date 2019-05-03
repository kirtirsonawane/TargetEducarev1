package com.targeteducare;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class EditProfileActivity extends Activitycommon {

    private static final int PICK_IMAGE_REQUEST = 1;
    /*private static String[] stream = {"Engineering", "Medical"};
    private static String[] std = {"B.E./B.Tech", "M.E./M.Tech", "BSc", "Msc", "BCA", "MCA"};
    private static String[] college = {"College", "School"};*/


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
    View vstandard, vcity;
    TextView tv_standard, tv_city;

    int flag_permission = 0;

    Bitmap selectedImage;

    String date = "";

    int radiobutton_gender;

    String rb_text = "";

    Spinner spin_stream, spin_standard, spin_state, spin_city;

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

    String check_str, check_subboardstr;
    String citycheckvalue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setmaterialDesign();
        back();

        StructureClass.defineContext(EditProfileActivity.this);
        try {
            Log.e("in profile ", "in profile");

            spin_state = findViewById(R.id.spinner_state);
            spin_city = findViewById(R.id.spinner_city);
            spin_stream = findViewById(R.id.spinner_stream);
            spin_standard = findViewById(R.id.spinner_standard);

            preferences = PreferenceManager.getDefaultSharedPreferences(EditProfileActivity.this);
            editor = preferences.edit();


            Gson gson = new Gson();
            Type type = new TypeToken<Student>() {
            }.getType();
            GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
            Log.e("selected board ", "" + GlobalValues.student.getBoard_name());
            Log.e("value subboard editprof", " " + GlobalValues.student.getSubboard_name());
            Log.e("selected student1 ", "selected student1 " + GlobalValues.student.getStateId() + " " + GlobalValues.student.getCityId());

            vstandard = findViewById(R.id.viewforstandard);
            vcity = findViewById(R.id.vcity);

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

            update = findViewById(R.id.button_updateprofile);

            et_name.setText(GlobalValues.student.getName());
            et_fathersname.setText(GlobalValues.student.getFatherName());
            et_surname.setText(GlobalValues.student.getSurname());
            tv_username.setText(GlobalValues.student.getFullname());

            Log.e("Mobile number is ", GlobalValues.student.getMobile() + "");
            et_mobno.setText(GlobalValues.student.getMobile());
            et_alternateno.setText(GlobalValues.student.getAltMobile());

            dateofbirth.setText(GlobalValues.student.getDOB());
            et_emailid.setText(GlobalValues.student.getEmail());
            et_district.setText(GlobalValues.student.getDistrict());

            Log.e("Getting gender ", GlobalValues.student.getGender());
            if (GlobalValues.student.getGender().equalsIgnoreCase("Female")) {
                rb_female.setChecked(true);
            } else if (GlobalValues.student.getGender().equalsIgnoreCase("Male")) {
                rb_male.setChecked(true);
            } else {
                rb_male.setChecked(false);
                rb_female.setChecked(false);
            }

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
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            onDateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            profileimageupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Permission Denied "," in onClick image");
                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                        flag_permission = 1;
                    }
                    else {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                    }
                }
            });

            onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    date = day + "/" + (month + 1) + "/" + year;
                    dateofbirth.setText(date);
                }
            };

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                        objupdate.put("CenterId", "");
                        objupdate.put("FatherName", et_fathersname.getText().toString());
                        objupdate.put("MotherName", "");
                        objupdate.put("DOB", dateofbirth.getText().toString());
                        objupdate.put("Mobile", et_mobno.getText().toString());
                        objupdate.put("Email", et_emailid.getText().toString());
                        objupdate.put("QualificationId", "");
                        objupdate.put("CountryId", "");
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
                        objupdate.put("CategoryId", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());

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

                        if (spinnerStandards != null) {
                            if (spinnerStandards.size() > spin_standard.getSelectedItemPosition()) {
                                objupdate.put("SubCategoryId", spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                            } else objupdate.put("SubCategoryId", 0);
                        } else {
                            objupdate.put("SubCategoryId", 0);
                        }

                        objupdate.put("sanstha_id", "");
                        objupdate.put("CategoryName", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
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

                    Toast.makeText(EditProfileActivity.this, "Your profile has been updated!!", Toast.LENGTH_SHORT).show();
                }
            });

            spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(context, "position selected is "+i, Toast.LENGTH_SHORT).show();

                    /*String name = adapterView.getItemAtPosition(i).toString();
                    Log.e("String is", name);*/


                    JSONObject obj1 = new JSONObject();
                    JSONObject mainobj1 = new JSONObject();

                    try {
                        obj1.put("StateName", spinnerStates.get(i).getState_id());
                        mainobj1.put("FilterParameter", obj1.toString());
                        Log.e("dataobj1 ", mainobj1.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ConnectionManager.getInstance(EditProfileActivity.this).getcity(mainobj1.toString());

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
                                String name_subboard = c1.getString("Name");
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

                            check_subboardstr = GlobalValues.student.getId();
                            Log.e("subboard in editprof is", check_subboardstr);

                            for (int k = 0; k < spinnerStandards.size(); k++) {
                                if (spinnerStandards.get(k).getId().equalsIgnoreCase(check_subboardstr)) {
                                    spin_standard.setSelection(k);
                                    break;
                                }
                            }

                        } else {

                            tv_standard.setVisibility(View.GONE);
                            vstandard.setVisibility(View.GONE);
                            spin_standard.setVisibility(View.GONE);

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
            Log.e("error ", "error " + e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);

                    OutputStream fout = null;
                    /*File f = File.createTempFile(Constants.PROFILE_PIC, Constants.FILE_NAME_EXT,
                            new File(StructureClass.generate()));*/
                    Log.e("Permission Denied "," in onActivityResult");
                    File f2= new File(StructureClass.generate());
                    File f1=new File(f2.getAbsolutePath()+"/"+GlobalValues.student.getId()+Constants.PROFILE_PIC+Constants.FILE_NAME_EXT);
                    if((f1.exists()) && (ActivityCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){

                        fout = new FileOutputStream(f1.getAbsoluteFile());
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fout);
                        profileimageupdate.setImageBitmap(selectedImage);

                        Log.e("File path ",f1.toString());
                        fout.flush();
                        fout.close();

                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //Toast.makeText(EditProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
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
                            String state_name = c.optString("Name");
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
                                String city_name = c1.optString("Name");
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
                            String name_board = c.getString("Name");
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
                        String name_board = c.getString("Name");
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
                } else if (accesscode == Connection.UPDATEPROFILE.ordinal()) {
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject getjsonsubroot = jsonObject.getJSONObject("root").getJSONObject("subroot");
                    //Log.e("subroot check val ",getjsonsubroot.toString());

                    try {

                        if (getjsonsubroot != null) {
                            String roll_no = getjsonsubroot.optString("CatId");
                            GlobalValues.student.setRollNumber(roll_no);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Gson gson = new Gson();

                    GlobalValues.student.setName(et_name.getText().toString().trim());
                    GlobalValues.student.setFatherName(et_fathersname.getText().toString().trim());
                    GlobalValues.student.setSurname(et_surname.getText().toString().trim());
                    GlobalValues.student.setFullname(et_name.getText().toString().trim() + " " + et_fathersname.getText().toString().trim() + " " +
                            et_surname.getText().toString().trim());

                    GlobalValues.student.setDOB(dateofbirth.getText().toString());

                    Log.e("Set Gender is ", rb_text);
                    GlobalValues.student.setGender(rb_text);

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

                    GlobalValues.student.setCategoryId(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());

                    if (spinnerStandards != null) {
                        GlobalValues.student.setSubCategoryId(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                    } else {
                        GlobalValues.student.setSubCategoryId("0");
                    }

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

                    Log.e("selected student ", "selected student " + GlobalValues.student.getStateId() + " " + GlobalValues.student.getCityId());

                    //profile update

                    String jsonstudent = gson.toJson(GlobalValues.student);
                    editor.putString("studentdetails", jsonstudent);
                    editor.apply();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (flag_permission == 1) {
                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                        Log.e("Permission Denied "," in onRequestPermissionsResult");
                        File f2= new File(StructureClass.generate());
                        File f1=new File(f2.getAbsolutePath()+"/"+GlobalValues.student.getId()+Constants.PROFILE_PIC+Constants.FILE_NAME_EXT);
                        if((f1.exists()) && (ActivityCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                            fout = new FileOutputStream(f1.getAbsoluteFile());
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fout);
                            profileimageupdate.setImageBitmap(selectedImage);
                        }
                        else if(ActivityCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            profileimageupdate.setImageResource(R.mipmap.profile);
                        }
                        else{
                            profileimageupdate.setImageResource(R.mipmap.profile);
                        }

                        Log.e("File path ",f1.toString());

                        fout.flush();
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
        return;
    }

    @Override
    protected void onResume() {
        profileimageupdate.setImageResource(R.mipmap.profile);

        try {
            Log.e("Permission Denied "," in onPostResume()");
            File f2= new File(StructureClass.generate());
            File f1=new File(f2.getAbsolutePath()+"/"+GlobalValues.student.getId()+Constants.PROFILE_PIC+Constants.FILE_NAME_EXT);

            if((f1.exists()) && (ActivityCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){

                Bitmap myBitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());

                profileimageupdate.setImageBitmap(myBitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }
}



