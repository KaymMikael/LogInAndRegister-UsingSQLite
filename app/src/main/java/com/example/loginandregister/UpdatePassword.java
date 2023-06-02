package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Models.Day;
import com.example.loginandregister.Models.UserAccount;
import com.example.loginandregister.Utils.Database;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalTime;

public class UpdatePassword extends AppCompatActivity {
    private int userId;
    private TextView txtGreetings;
    private TextInputEditText oldPass, newPass, confirmPass;
    private Button btnUpdatePass;

    private Database db;
    private Day day;
    private UserAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        db = new Database(this);
        userId = getIntent().getIntExtra("userId", 0);//Default value
        txtGreetings = findViewById(R.id.txtGreetings2);
        oldPass = findViewById(R.id.oldpass);
        newPass = findViewById(R.id.newpass);
        confirmPass = findViewById(R.id.confirmpass);
        btnUpdatePass = findViewById(R.id.btnUpdatePass);
        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int loggedInUserId = db.getUserId(userId);
                    String oldPassword = oldPass.getText().toString();
                    String newPassword = newPass.getText().toString();
                    String confirmPassword = confirmPass.getText().toString();
                    account = db.getUserById(userId);
                    account.setPassword(newPassword);
                    account.setId(userId);
                    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(UpdatePassword.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    } else if(newPassword.length()<=5 && confirmPassword.length()<=5){
                        Toast.makeText(UpdatePassword.this, "Password must be longer than 5 characters", Toast.LENGTH_SHORT).show();
                    }else if (oldPassword.equals(newPassword)) {
                        Toast.makeText(UpdatePassword.this, "New password must be different from the existing password", Toast.LENGTH_SHORT).show();
                    } else {
                        if (newPassword.equals(confirmPassword)) {
                            if (loggedInUserId == userId) {
                                db.updatePassword(account);
                                Toast.makeText(UpdatePassword.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdatePassword.this, LogIn.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(UpdatePassword.this, "Cant update your password.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(UpdatePassword.this, "Password not matching.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        displayUserName();
    }

    @SuppressLint("SetTextI18n")
    private void displayUserName() {
        UserAccount user = db.getUserById(userId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            day = new Day(LocalTime.now().getHour());
        }
        if (user != null) {
            if (day.isMorning()) {
                txtGreetings.setText("Hello " + user.getUsername() + ", Good Morning");
            } else if (day.isAfternoon()) {
                txtGreetings.setText("Hello " + user.getUsername() + ", Good Afternoon");
            } else if (day.isEvening()) {
                txtGreetings.setText("Hello " + user.getUsername() + ", Good Evening");
            }
        } else {
            // Handle the case where the user is not found in the database
            txtGreetings.setText("User not found");
        }
    }
}