package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Models.UserAccount;
import com.example.loginandregister.Utils.Database;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {
    private TextInputEditText txtUsername, txtPassword, txtRePassword;
    private Button btnRegister;
    private Database db;
    private TextView txtSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new Database(this);
        txtSignIn = findViewById(R.id.txtSignIn);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = txtUsername.getText().toString();
                    String pass = txtPassword.getText().toString();
                    String repass = txtRePassword.getText().toString();
                    UserAccount userAccount = new UserAccount();
                    if (name.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                        Toast.makeText(Register.this, "Please input all fields.", Toast.LENGTH_SHORT).show();
                    } else if(pass.length()<=4&&repass.length()<=4) {
                        Toast.makeText(Register.this, "Password must be longer than 5 characters", Toast.LENGTH_SHORT).show();
                    }else {
                        if (pass.equals(repass)) {
                            userAccount.setUsername(name);
                            Boolean checkUser = db.checkUsername(userAccount);
                            if (!checkUser) {
                                userAccount.setUsername(name);
                                userAccount.setPassword(pass);
                                Boolean insert = db.insertAccount(userAccount);
                                if (insert) {
                                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Register.this, "User already Exists, please sign in!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "Password not matching", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Register.this, LogIn.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}