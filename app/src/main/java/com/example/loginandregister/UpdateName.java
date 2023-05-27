package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Models.Day;
import com.example.loginandregister.Models.UserAccount;
import com.example.loginandregister.Utils.Database;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalTime;

public class UpdateName extends AppCompatActivity {
    private Day day;
    private TextView txtGreetings;
    private TextInputEditText updateName, passwordUpdate, oldName;
    private Button btnUpdateName;
    private Database db;
    private UserAccount account;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        txtGreetings = findViewById(R.id.txtGreetings);
        userId = getIntent().getIntExtra("userId", 0);
        account = new UserAccount();
        db = new Database(UpdateName.this);
        oldName = findViewById(R.id.oldname);
        updateName = findViewById(R.id.updateName);
        passwordUpdate = findViewById(R.id.passwordUpdate);
        btnUpdateName = findViewById(R.id.btnUpdateName);
        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String existingName = oldName.getText().toString();
                    String newName = updateName.getText().toString();
                    String pass = passwordUpdate.getText().toString();
                    if (existingName.isEmpty() || newName.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(UpdateName.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                    } else {
                        UserAccount userAccount = db.getUserById(userId);
                        userAccount.setUsername(existingName);
                        userAccount.setPassword(pass);
                        userAccount.setId(userId);
                        int loggedInUserId = db.getUserIdByUsername(userAccount.getUsername());
                        Boolean checkUsernamePass = db.checkUsernamePassword(userAccount);
                        if (checkUsernamePass) {
                            Boolean checkUsername = db.checkUsername(userAccount);
                            if (checkUsername) {
                                if (loggedInUserId == userId) {
                                    if (existingName.equals(newName)) {
                                        Toast.makeText(UpdateName.this, "New name must be different from the existing name.", Toast.LENGTH_SHORT).show();
                                    } else if (db.checkUsernameForUser(newName, userId)) { // Check if the new name already exists for the current user
                                        Toast.makeText(UpdateName.this, "Username Already Exists! Change your new name", Toast.LENGTH_SHORT).show();
                                    } else {
                                        userAccount.setUsername(newName); // Update the username field with the new name
                                        db.updateName(userAccount);
                                        Toast.makeText(UpdateName.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UpdateName.this, LogIn.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(UpdateName.this, "You can't delete other user's account", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UpdateName.this, "Username doesn't exist.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UpdateName.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
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