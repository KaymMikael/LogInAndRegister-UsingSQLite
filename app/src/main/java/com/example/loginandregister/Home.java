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

import java.time.LocalTime;

public class Home extends AppCompatActivity {
    private Database db;
    private Day day;
    private TextView txtName, txtDeleteAllData;
    private int userId;
    private Button btnGoUpdate, btnGoUpdate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtName = findViewById(R.id.txtName);
        txtDeleteAllData = findViewById(R.id.txtDeleteAllData);
        btnGoUpdate = findViewById(R.id.btnGoUpdate);
        btnGoUpdate2 = findViewById(R.id.btnGoUpdate2);
        db = new Database(this);
        userId = getIntent().getIntExtra("userId", 0);
        btnGoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, UpdateName.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        btnGoUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, UpdatePassword.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        txtDeleteAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllAccounts();
                Toast.makeText(Home.this, "Successfully deleted all data.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Home.this, LogIn.class);
                startActivity(intent);
                finish();
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
                txtName.setText("Hello " + user.getUsername() + ", Good Morning");
            } else if (day.isAfternoon()) {
                txtName.setText("Hello " + user.getUsername() + ", Good Afternoon");
            } else if (day.isEvening()) {
                txtName.setText("Hello " + user.getUsername() + ", Good Evening");
            }
        } else {
            // Handle the case where the user is not found in the database
            txtName.setText("User not found");
        }
    }

}