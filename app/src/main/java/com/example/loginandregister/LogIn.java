package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Models.UserAccount;
import com.example.loginandregister.Utils.Database;
import com.google.android.material.textfield.TextInputEditText;

public class LogIn extends AppCompatActivity {
    private TextInputEditText username, password;
    private Button btnLogin;
    private Database db;
    private UserAccount userAccount;
    private TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new Database(this);
        userAccount = new UserAccount();
        txtRegister = findViewById(R.id.txtRegister);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if(name.isEmpty()||pass.isEmpty()) {
                    Toast.makeText(LogIn.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    userAccount.setUsername(name);
                    userAccount.setPassword(pass);
                    Boolean checkUserPass = db.checkUsernamePassword(userAccount);
                    if(checkUserPass) {
                        int userId = db.getUserIdByUsername(userAccount.getUsername());
                        Toast.makeText(LogIn.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("userId", userId); // Pass the userId to the Home activity
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LogIn.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }
}