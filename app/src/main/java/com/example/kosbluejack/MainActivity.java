package com.example.kosbluejack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> nametemp;
    ArrayList<String> passtemp;
    ArrayList<String> idtemp;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        DBHelper dbHelper = new DBHelper(this);
        nametemp = new ArrayList<>();
        passtemp = new ArrayList<>();
        idtemp = new ArrayList<>();
        final long rowCount = dbHelper.getTableUserRow();
        Cursor c = dbHelper.getUserName();
        while(c.moveToNext()){
            String tempname = c.getString(c.getColumnIndex(dbHelper.USER_USER_NAME));
            nametemp.add(tempname);
        }
        Cursor cu = dbHelper.getUserPassword();
        while(cu.moveToNext()){
            String temppass = cu.getString(cu.getColumnIndex(dbHelper.USER_PASSWORD));
            passtemp.add(temppass);
        }
        Cursor ci = dbHelper.getUserId();
        while(ci.moveToNext()){
            String tempid = ci.getString(ci.getColumnIndex(dbHelper.USER_USER_ID));
            idtemp.add(tempid);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText()+"";
                String password = etPassword.getText()+"";
                if(username.equals("")){
                    Toast.makeText(MainActivity.this, "Input username", Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.equals("")){
                    Toast.makeText(MainActivity.this, "Input password", Toast.LENGTH_LONG).show();
                    return;
                }
                if(rowCount == 0){
                    Toast.makeText(MainActivity.this, "Please register yourself first", Toast.LENGTH_LONG).show();
                    return;
                }
                for (int i = 0; i < rowCount; i++) {
                    if(username.equals(nametemp.get(i)) && password.equals(nametemp.get(i))){
                        Intent toKostList = new Intent(MainActivity.this, KostList.class);
                        String activeUser = idtemp.get(i);
                        Log.d(TAG, "activeuser: " + activeUser);
                        CurrLogin.currLogin.add(activeUser);
                        startActivity(toKostList);
                        finish();
                        return;
                    }
                }
                Toast.makeText(MainActivity.this, "Username/password not registered", Toast.LENGTH_LONG).show();
                return;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
