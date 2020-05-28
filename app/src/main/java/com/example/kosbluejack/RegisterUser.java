package com.example.kosbluejack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class RegisterUser extends AppCompatActivity {
    public static final String TAG = "RegisterUser";
    ArrayList<String> nametemp;
    private SmsManager smsManager;
    private int smsPermission = PackageManager.PERMISSION_DENIED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etPhoneNo = findViewById(R.id.etPhoneNo);
        final EditText etConfirmPass = findViewById(R.id.etConfirmPass);
        final EditText DOBContainer = findViewById(R.id.DOBContainer);
        final RadioGroup rgGender = findViewById(R.id.rg_gender);
        final CheckBox cbAgreement = findViewById(R.id.cbAgreement);
        Button btnRegister = findViewById(R.id.btnRegister);
        final DBHelper dbHelper = new DBHelper(this);
        nametemp = new ArrayList<>();
        smsManager = SmsManager.getDefault();

        DOBContainer.setFocusable(false);
        DOBContainer.setKeyListener(null);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DOBContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        DOBContainer.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        checkPermission();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText()+"";
                String password = etPassword.getText()+"";
                String phoneNo = etPhoneNo.getText()+"";
                String confirmPass = etConfirmPass.getText()+"";
                String DOB = DOBContainer.getText()+"";
                String gender = "";
                int genderId = rgGender.getCheckedRadioButtonId();
                long rowCount = dbHelper.getTableUserRow();
                Log.d(TAG, "count: " + rowCount);

                if(username.equals("")){
                    Toast.makeText(RegisterUser.this, "Input username", Toast.LENGTH_LONG).show();
                    return;
                }
                if(username.length() < 3 || username.length() > 25){
                    Toast.makeText(RegisterUser.this, "Username must consist of 3 to 25 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if(username.matches("[0-9]+") || username.matches("[a-zA-Z]+")){
                    Toast.makeText(RegisterUser.this, "Username must consist of at least 1 digit and alphabetic", Toast.LENGTH_LONG).show();
                    return;
                }
                if(rowCount > 0){
                    Cursor c = dbHelper.getUserName();
                    while(c.moveToNext()){
                        String tempname = c.getString(c.getColumnIndex(dbHelper.USER_USER_NAME));
                        nametemp.add(tempname);
                    }
                    Log.d(TAG, "saved name: " + nametemp.get(0));
                    for (int i = 0; i < rowCount; i++) {
                        if(username.equals(nametemp.get(i))){
                            Toast.makeText(RegisterUser.this, "Username must be unique", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    Log.d(TAG, "bleeeh");
                }
//
                if(password.equals("")){
                    Toast.makeText(RegisterUser.this, "Input password", Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.matches("[a-z]+") || password.matches("[A-Z]+") || password.matches("[0-9]+") || password.matches("[a-zA-Z]+") || password.matches("[a-z0-9]+") || password.matches("[A-Z0-9]+")){
                    Toast.makeText(RegisterUser.this, "Password must consist of at least 1 lowercase/1 uppercase/1 digit", Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.length() < 6){
                    Toast.makeText(RegisterUser.this, "Password must consist of at least 6 character", Toast.LENGTH_LONG).show();
                }
                if(phoneNo.equals("")){
                    Toast.makeText(RegisterUser.this, "Input phone number", Toast.LENGTH_LONG).show();
                    return;
                }
                if(phoneNo.length() < 10 || phoneNo.length() > 12){
                    Toast.makeText(RegisterUser.this, "Phone number must consist of 10 to 12 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!phoneNo.matches("[0-9]+")){
                    Toast.makeText(RegisterUser.this, "Phone number must consist of number only", Toast.LENGTH_LONG).show();
                    return;
                }
                if(confirmPass.equals("")){
                    Toast.makeText(RegisterUser.this, "Input password", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!password.equals(confirmPass)){
                    Toast.makeText(RegisterUser.this, "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                if(genderId == -1){
                    Toast.makeText(RegisterUser.this, "Input gender", Toast.LENGTH_LONG).show();
                    return;
                }
                if(genderId != -1){
                    RadioButton genderContainer = findViewById(genderId);
                    gender = genderContainer.getText().toString();
                }
                if(!cbAgreement.isChecked()){
                    Toast.makeText(RegisterUser.this, "Read our Terms and Conditions", Toast.LENGTH_LONG).show();
                    return;
                }
                if(DOB.equals("")){
                    Toast.makeText(RegisterUser.this, "Input date of birth", Toast.LENGTH_LONG).show();
                    return;
                }

                String userId = "";
                userId = String.format("US%03d", rowCount + 1);
                User user = new User(userId, username, password, phoneNo, gender, DOB);
//                Log.d(TAG, "phoneno: " + phoneNo);
                boolean isAdded = dbHelper.insertTableUser(user);
                if(isAdded){
//                    Log.d(TAG, "PHONE: " + phoneNo);
                    if(smsPermission == PackageManager.PERMISSION_GRANTED){
                        String message = "Registered!";
                        smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    }
                    else{
                        checkPermission();
                    }
                    Intent toLogin = new Intent(RegisterUser.this, MainActivity.class);
                    startActivity(toLogin);
                    finish();
                }
                else{
                    Toast.makeText(RegisterUser.this, "Data failed to be inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void checkPermission() {
        smsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if(smsPermission != PackageManager.PERMISSION_GRANTED){
            String[] permissionMessages = new String[]{Manifest.permission.SEND_SMS};
            ActivityCompat.requestPermissions(this, permissionMessages, 1);
        }
    }
}
