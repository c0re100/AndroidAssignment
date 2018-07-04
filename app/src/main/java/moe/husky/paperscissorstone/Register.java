package moe.husky.paperscissorstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    private EditText username, phoneNumber, email;
    private DatePicker pickDate;
    private SharedPreferences profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isEditing;
        try {
            isEditing = getIntent().getExtras().getBoolean("EditMode", false);
        } catch (Exception ex) {
            isEditing = false;
        }

        profile = getSharedPreferences("profile", MODE_PRIVATE);
        if (!profile.getString("username", "").equals("") && !isEditing) {
            startActivity(new Intent(this, MainPage.class));
        } else {
            setContentView(R.layout.activity_register);

            username = findViewById(R.id.txtUsername);
            phoneNumber = findViewById(R.id.txtPhoneNumber);
            email = findViewById(R.id.txtEmail);
            pickDate = findViewById(R.id.pickerDOB);

            if (!profile.getString("username", "").equals("")) {
                loadProfile();
            }
        }
    }

    public void loadProfile() {
        String date = profile.getString("DoB", "");
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        username.setText(profile.getString("username", "Unknown"));
        pickDate.updateDate(year, month, day);
        phoneNumber.setText(profile.getString("phone_number", "Unknown"));
        email.setText(profile.getString("email", "Unknown"));
    }

    public void Save(View view) {
        boolean isSetUname, isSetPnum, isSetMail;
        isSetUname = isSetPnum = isSetMail = false;
        if (username.getText().toString().equals("")) {
            username.setHint("Please type Username!!!");
            isSetUname = false;
        } else {
            isSetUname = true;
        }

        if (phoneNumber.getText().toString().equals("")) {
            phoneNumber.setHint("Please type phone number!!!");
            isSetPnum = false;
        } else {
            isSetPnum = true;
        }

        if (email.getText().toString().equals("")) {
            email.setHint("Please type E-mail!!!");
            isSetMail = false;
        } else {
            isSetMail = true;
        }

        if (!isSetUname || !isSetPnum || !isSetMail) {
            Toast.makeText(this, "Register Failed, please complete all required fields", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        pref.edit().putString("username", username.getText().toString()).putString("DoB", pickDate.getYear() + "-" + pickDate.getMonth() + "-" + pickDate.getDayOfMonth()).putString("phone_number", phoneNumber.getText().toString()).putString("email", email.getText().toString()).apply();

        Toast.makeText(this, "Register Successful, redirect to game page after 3 seconds :)", Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_main_page);
    }

    public void Reset(View view) {
        username.setText("");
        Calendar now = Calendar.getInstance();
        pickDate.updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        phoneNumber.setText("");
        email.setText("");
        Toast.makeText(this, "The form is resetted.", Toast.LENGTH_LONG).show();
    }

    public void Guest(View view) {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        pref.edit().putString("username", "Guest").putString("DoB", pickDate.getYear() + "-" + pickDate.getMonth() + "-" + pickDate.getDayOfMonth()).putString("phone_number", "999").putString("email", "team@husky.moe").apply();

        Toast.makeText(this, "Play as a Guest, redirect to game page after 3 seconds :)", Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_main_page);
    }
}