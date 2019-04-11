package com.targeteducare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.targeteducare.Classes.StudentProfile;
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
    /*private static String[] stream = {"Engineering", "Medical"};
    private static String[] std = {"B.E./B.Tech", "M.E./M.Tech", "BSc", "Msc", "BCA", "MCA"};
    private static String[] college = {"College", "School"};*/


    private DatePickerDialog.OnDateSetListener onDateSetListener;
    EditText dateofbirth;
    private RadioGroup radioGroup;
    ImageView profileimageupdate;
    EditText et_name, et_fathersname, et_surname, et_mobno, et_alternateno, et_emailid, et_district;
    String id, state_id;
    TextView tv_username;
    Button update;
    RadioButton rb_female, rb_male, rb;
    JSONArray subrootboard;
    View vstandard;
    TextView tv_standard;

    Bitmap selectedImage;

    String date;

    int radiobutton_gender;

    String rb_text;
    String roll_no;

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
    String citycheckvalue;

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
            Type type = new TypeToken<StudentProfile>() {
            }.getType();
            GlobalValues.studentProfile = gson.fromJson(preferences.getString("studentprofiledetails", ""), type);
            Log.e("selected board ", "" + GlobalValues.studentProfile.getBoard_name());
            Log.e("value subboard editprof", " " + GlobalValues.studentProfile.getSubboard_name());
            //String photo = preferences.getString("image",null);

            vstandard = findViewById(R.id.viewforstandard);

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

            update = findViewById(R.id.button_updateprofile);

            preferences.getString("studentprofiledetails", "");

            et_name.setText(GlobalValues.studentProfile.getName());
            et_fathersname.setText(GlobalValues.studentProfile.getFather_name());
            et_surname.setText(GlobalValues.studentProfile.getSurname());
            tv_username.setText(GlobalValues.studentProfile.getFullname());
            et_mobno.setText(GlobalValues.studentProfile.getMobile());
            dateofbirth.setText(GlobalValues.studentProfile.getDob());


            tv_username.setText(GlobalValues.studentProfile.getFullname());

            //Log.e("getting gender",GlobalValues.studentProfile.getGender());
            if (GlobalValues.studentProfile.getGender().equalsIgnoreCase("Female")) {
                rb_female.setChecked(true);
            } else if (GlobalValues.studentProfile.getGender().equalsIgnoreCase("Male")) {
                rb_male.setChecked(true);
            } else {
                rb_male.setChecked(false);
                rb_female.setChecked(false);
                //rb.setChecked(false);
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


                /*Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                imagepath = Environment.getExternalStorageDirectory()+"/sharedresources/"+HelperFunctions.getDateTimeForFileName()+".png";
                uriImagePath = Uri.fromFile(new File(imagepath));
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,uriImagePath);
                photoPickerIntent.putExtra("outputFormat",Bitmap.CompressFormat.PNG.name());
                photoPickerIntent.putExtra("return-data", true);
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PICTURE_FROM_GALLARY);*/

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
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

                  /*  try{
                        Gson gson = new Gson();
                        GlobalValues.studentProfile.setDob(date);
                        GlobalValues.studentProfile.setName(et_name.getText().toString().trim());
                        GlobalValues.studentProfile.setFather_name(et_fathersname.getText().toString().trim());
                        GlobalValues.studentProfile.setSurname(et_surname.getText().toString().trim());
                        GlobalValues.studentProfile.setFullname(GlobalValues.studentProfile.getName() + " " + GlobalValues.studentProfile.getFather_name() + " " +
                                GlobalValues.studentProfile.getSurname());
                        GlobalValues.studentProfile.setGender(rb_text);
                        GlobalValues.studentProfile.setBoard_name(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                        GlobalValues.studentProfile.setSubboard_name(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard());
                        GlobalValues.studentProfile.setEmail(et_emailid.getText().toString().trim());

                        Log.e("roll no test: ",GlobalValues.studentProfile.getId());

                        //Log.e("spinner state value ",spinnerStates.get(spin_state.getSelectedItemPosition()).getStatename());

                        GlobalValues.studentProfile.setState(spinnerStates.get(spin_state.getSelectedItemPosition()).getStatename());

                        if ((spin_city.getSelectedItemPosition() >= 0) && (spin_city.getSelectedItemPosition() < spinnerCities.size())) {
                            GlobalValues.studentProfile.setCity(spinnerCities.get(spin_city.getSelectedItemPosition()).getCity_name());
                        } else {
                            GlobalValues.studentProfile.setCity("");
                        }

                        tv_username.setText(GlobalValues.studentProfile.getFullname());

                        String jsonstudentprofile = gson.toJson(GlobalValues.studentProfile);
                        editor.putString("studentprofiledetails", jsonstudentprofile);
                        editor.apply();
                    } catch (Exception e){
                        e.printStackTrace();
                    }*/

                    try {
                        OutputStream fout = null;
                        /*File f = File.createTempFile(Constants.PROFILE_PIC, Constants.FILE_NAME_EXT,
                                new File(StructureClass.generate()));*/

                        File f2= new File(StructureClass.generate());
                        File f1=new File(f2.getAbsolutePath()+"/"+GlobalValues.studentProfile.getId()+Constants.PROFILE_PIC+Constants.FILE_NAME_EXT);
                        Log.e("File path ",f1.toString());
                        //BufferedWriter out = new BufferedWriter(new FileWriter(f.getAbsolutePath()));

                        fout = new FileOutputStream(f1.getAbsoluteFile());

                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fout);
                        fout.flush();
                        fout.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONObject objupdate = new JSONObject();
                    JSONObject objsubroot = new JSONObject();
                    JSONObject objroot = new JSONObject();
                    JSONObject objxml = new JSONObject();
                    JSONObject mainobjupdate = new JSONObject();

                    try {
                        objupdate.put("TotalRecord", "");
                        objupdate.put("Id", GlobalValues.studentProfile.getId());
                        objupdate.put("Name", GlobalValues.studentProfile.getFullname());
                        objupdate.put("RollNumber", GlobalValues.studentProfile.getRoll_no());
                        objupdate.put("CenterId", "");
                        objupdate.put("FatherName", GlobalValues.studentProfile.getFather_name());
                        objupdate.put("MotherName", "");
                        objupdate.put("DOB", GlobalValues.studentProfile.getDob());
                        objupdate.put("Mobile", GlobalValues.studentProfile.getMobile());
                        objupdate.put("Email", GlobalValues.studentProfile.getEmail());
                        objupdate.put("QualificationId", "");
                        objupdate.put("CountryId", "");
                        objupdate.put("StateId", "");
                        objupdate.put("CityId", "");
                        objupdate.put("Password", "a");
                        objupdate.put("Address", "");
                        objupdate.put("Qualification", "");
                        objupdate.put("Center", "");
                        objupdate.put("RegistrationDate", "");
                        objupdate.put("Adhar", "");
                        objupdate.put("CategoryId", "");
                        objupdate.put("CasteCategory", "");
                        objupdate.put("Gender", GlobalValues.studentProfile.getGender());
                        objupdate.put("Nationality", "Indian");
                        objupdate.put("AltMobile", "");
                        objupdate.put("AltEmail", "");
                        objupdate.put("IsActive", "");
                        objupdate.put("SubCategoryId", "");
                        objupdate.put("sanstha_id", "");
                        objupdate.put("CategoryName", GlobalValues.studentProfile.getBoard_name());
                        objupdate.put("Totalamt", "");
                        objupdate.put("Packageamount", "");
                        objupdate.put("Type", "");
                        objupdate.put("PracticeId", "");
                        objupdate.put("DepartmentId", "");
                        objupdate.put("PackageDetails", null);
                        objupdate.put("UserId", "");

                        Log.e("subroot check", objupdate.toString());
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

                                SpinnerStandard spinnerStandard = new SpinnerStandard(name_subboard);
                                spinnerStandards.add(spinnerStandard);
                                //Log.e("spinner standards ",spinnerStandards.toString());
                            }
                            arrayAdapterstandard = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                            arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_standard.setAdapter(arrayAdapterstandard);
                            arrayAdapterstandard.notifyDataSetChanged();

                            check_subboardstr = GlobalValues.studentProfile.getSubboard_name();
                            Log.e("subboard in editprof is", check_subboardstr);

                            for (int k = 0; k < spinnerStandards.size(); k++) {
                                if (spinnerStandards.get(k).getStandard().equalsIgnoreCase(check_subboardstr)) {
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

                    profileimageupdate.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

                            id = c.optString("Id");
                            String state_name = c.optString("Name");
                            String country_id = c.optString("CountryId");

                            SpinnerState spinnerState = new SpinnerState(state_name, id);
                            spinnerStates.add(spinnerState);

                        }
                        arrayAdapterstate = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStates);
                        arrayAdapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_state.setAdapter(arrayAdapterstate);
                        arrayAdapterstate.notifyDataSetChanged();

                        String statecheckvalue = GlobalValues.studentProfile.getState();
                        for (int statecheck = 0; statecheck < spinnerStates.size(); statecheck++) {
                            if (spinnerStates.get(statecheck).getStatename().equalsIgnoreCase(statecheckvalue)) {
                                spin_state.setSelection(statecheck);
                                break;
                            }
                        }

                    }

                } else if (accesscode == Connection.GETCITY.ordinal()) {
                    Log.e("res ", "resgetcity " + GlobalValues.TEMP_STR);

                    JSONArray jsonArray1 = new JSONArray(GlobalValues.TEMP_STR);

                    if (jsonArray1 != null) {
                        spinnerCities = new ArrayList<>();

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject c1 = jsonArray1.getJSONObject(i);

                            state_id = c1.optString("Id");
                            String city_name = c1.optString("Name");
                            String id = c1.optString("StateID");

                            SpinnerCity spinnerCity = new SpinnerCity(city_name, state_id);
                            spinnerCities.add(spinnerCity);
                        }
                        arrayAdaptercity = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerCities);
                        arrayAdaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_city.setAdapter(arrayAdaptercity);
                        arrayAdaptercity.notifyDataSetChanged();

                        citycheckvalue = GlobalValues.studentProfile.getCity();
                        for (int citycheck = 0; citycheck < spinnerCities.size(); citycheck++) {
                            if (spinnerCities.get(citycheck).getCity_name().equalsIgnoreCase(citycheckvalue)) {
                                spin_city.setSelection(citycheck);
                                break;
                            }
                        }
                    }
                } else if (accesscode == Connection.GETCATEGORY.ordinal()) {

                    Log.e("res", "res " + GlobalValues.TEMP_STR);

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    subrootboard = jsonObject.getJSONObject("root").optJSONArray("subroot");

                    if (subrootboard != null) {
                        spinnerStreams = new ArrayList<>();
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

                            SpinnerStream spinnerStream = new SpinnerStream(name_board);
                            spinnerStreams.add(spinnerStream);
                        }
                        arrayAdapterstream = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStreams);
                        arrayAdapterstream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_stream.setAdapter(arrayAdapterstream);
                        arrayAdapterstream.notifyDataSetChanged();

                        check_str = GlobalValues.studentProfile.getBoard_name();
                        Log.e("board name is ", check_str);
                        for (int check = 0; check < spinnerStreams.size(); check++) {
                            Log.e("spinner board name is ", spinnerStreams.get(check).getBoard_name());

                            if (spinnerStreams.get(check).getBoard_name().equalsIgnoreCase(check_str)) {
                                spin_stream.setSelection(check);
                                break;
                            }
                        }


                    } else {

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
                            GlobalValues.studentProfile.setRoll_no(roll_no);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Gson gson = new Gson();
                    GlobalValues.studentProfile.setDob(date);
                    GlobalValues.studentProfile.setName(et_name.getText().toString().trim());
                    GlobalValues.studentProfile.setFather_name(et_fathersname.getText().toString().trim());
                    GlobalValues.studentProfile.setSurname(et_surname.getText().toString().trim());
                    GlobalValues.studentProfile.setFullname(GlobalValues.studentProfile.getName() + " " + GlobalValues.studentProfile.getFather_name() + " " +
                            GlobalValues.studentProfile.getSurname());
                    GlobalValues.studentProfile.setGender(rb_text);
                    GlobalValues.studentProfile.setBoard_name(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                    GlobalValues.studentProfile.setSubboard_name(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard());
                    GlobalValues.studentProfile.setEmail(et_emailid.getText().toString().trim());

                    Log.e("roll no test: ", GlobalValues.studentProfile.getId());

                    //Log.e("spinner state value ",spinnerStates.get(spin_state.getSelectedItemPosition()).getStatename());

                    GlobalValues.studentProfile.setState(spinnerStates.get(spin_state.getSelectedItemPosition()).getStatename());

                    try {
                        if ((spin_city.getSelectedItemPosition() >= 0) && (spin_city.getSelectedItemPosition() < spinnerCities.size())) {
                            GlobalValues.studentProfile.setCity(spinnerCities.get(spin_city.getSelectedItemPosition()).getCity_name());
                        } else {
                            GlobalValues.studentProfile.setCity("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tv_username.setText(GlobalValues.studentProfile.getFullname());

                    String jsonstudentprofile = gson.toJson(GlobalValues.studentProfile);
                    editor.putString("studentprofiledetails", jsonstudentprofile);
                    editor.apply();


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
