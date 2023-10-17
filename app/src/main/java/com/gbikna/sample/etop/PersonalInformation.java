package com.gbikna.sample.etop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.util.SignupVr;

public class PersonalInformation extends AppCompatActivity {
    Button button;
    private TextInputLayout firstname, middlename, lastname, username, nin, dob;
    AutoCompleteTextView gender, usertype;

    public static int personalControl = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BussinessInformation.busControl = 1;
                Intent intent = new Intent();
                intent.setClass(PersonalInformation.this, BussinessInformation.class);
                startActivity(intent);
                finish();
            }
        });

        button = (Button) findViewById(R.id.pro_pers);
        firstname = (TextInputLayout)findViewById(R.id.firstname);
        middlename = (TextInputLayout)findViewById(R.id.middlename);
        lastname = (TextInputLayout)findViewById(R.id.lastname);
        username = (TextInputLayout)findViewById(R.id.username);
        nin = (TextInputLayout)findViewById(R.id.nin);
        dob = (TextInputLayout)findViewById(R.id.dob);

        usertype = (AutoCompleteTextView)findViewById(R.id.usertype);
        String[] option3 = {"MERCHANT", "AGENT"};
        ArrayAdapter adapter3 = new ArrayAdapter(this, R.layout.option_item, option3);
        usertype.setAdapter(adapter3);
        usertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usertype.showDropDown();
            }
        });

        gender = (AutoCompleteTextView) findViewById(R.id.gender);
        String[] option = {"MALE", "FEMALE"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.option_item, option);
        gender.setAdapter(adapter);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender.showDropDown();
            }
        });



        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.ThemeOverlay_App_MaterialCalendar);
        materialDateBuilder.setTitleText("SELECT DOB");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Log.i("PERSONALINFORMATION", materialDatePicker.getHeaderText());
                        dob.getEditText().setText(materialDatePicker.getHeaderText());
                        SignupVr.dob = materialDatePicker.getHeaderText();
                    }
                });

        dob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PERSONALINFORMATION", "DATE OF BIRTH ENTERED");
                materialDatePicker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });

        dob.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Log.i("PERSONALINFORMATION", " 2....DATE OF BIRTH ENTERED");
                    materialDatePicker.show(getSupportFragmentManager(), "DATE PICKER");
                } else {
                    // Hide your calender here
                    Log.i("PERSONALINFORMATION", " 2....DATE OF BIRTH ENTERED NOTE");
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proceed();
            }
        });

        if(PersonalInformation.personalControl == 1)
        {
            firstname.getEditText().setText(SignupVr.firstname);
            middlename.getEditText().setText(SignupVr.middlename);
            lastname.getEditText().setText(SignupVr.lastname);
            username.getEditText().setText(SignupVr.username);
            nin.getEditText().setText(SignupVr.nin);
            dob.getEditText().setText(SignupVr.dob);
            gender.setText(SignupVr.gender);
            usertype.setText(SignupVr.usertype);
            PersonalInformation.personalControl = 0;
        }


        if(Profile.controller == 1)
        {
            username.setEnabled(false);
        }
    }



    public void Proceed()
    {
        SignupVr.serialnumber = Utilities.getSerialNumber();
        SignupVr.firstname = firstname.getEditText().getText().toString().trim();
        SignupVr.middlename = middlename.getEditText().getText().toString().trim();
        SignupVr.lastname = lastname.getEditText().getText().toString().trim();
        SignupVr.fullname = SignupVr.lastname + ", " + SignupVr.firstname + " " + SignupVr.middlename;
        SignupVr.username = username.getEditText().getText().toString().trim();
        SignupVr.phonenumber = SignupVr.businessphone;
        SignupVr.usertype = usertype.getText().toString();
        //SignupVr.dob = dob.getEditText().getText().toString().trim();

        //formatter.format(calendar.getTime())
        String action = gender.getText().toString();

        if(action.equals("MALE"))
        {
            SignupVr.gender = "M";
        }else if(action.equals("FEMALE"))
        {
            SignupVr.gender = "F";
        }
        SignupVr.nin = nin.getEditText().getText().toString().trim();
        SignupVr.housenumber = "NA";
        SignupVr.streetname = "NA";
        //SignupVr.dob = "NA";

        if(SignupVr.firstname.isEmpty())
        {
            firstname.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "First Name Cannot be Empty", Toast.LENGTH_LONG).show();
        }/*else if(SignupVr.middlename.isEmpty())
        {
            middlename.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "Middle Name Cannot be Empty", Toast.LENGTH_LONG).show();
        }*/else if(SignupVr.lastname.isEmpty())
        {
            lastname.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "Last Name Cannot be Empty", Toast.LENGTH_LONG).show();
        }
        else if(SignupVr.username.isEmpty())
        {
            username.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "Email Cannot be Empty", Toast.LENGTH_LONG).show();
        }else if(SignupVr.gender.isEmpty())
        {
            gender.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "Gender Must be Selected", Toast.LENGTH_LONG).show();
        }else if(SignupVr.nin.isEmpty())
        {
            nin.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "NIN Cannot be Empty", Toast.LENGTH_LONG).show();
        }else if(SignupVr.dob.isEmpty())
        {
            dob.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "DOB Cannot be Empty", Toast.LENGTH_LONG).show();
        }else if(SignupVr.usertype.isEmpty())
        {
            usertype.setFocusable(true);
            Toast.makeText(PersonalInformation.this, "Select Usertype", Toast.LENGTH_LONG).show();
        }else {
            Intent inte = new Intent();
            inte.setClass(PersonalInformation.this, BankInfo.class);
            startActivity(inte);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        BussinessInformation.busControl = 1;
        intent.setClass(PersonalInformation.this, Login.class);
        startActivity(intent);
        finish();
    }
}