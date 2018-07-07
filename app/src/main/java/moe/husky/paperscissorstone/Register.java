package moe.husky.paperscissorstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

        if (!isNetworkAvailable()) {
            startActivity(new Intent(this, NetworkStatus.class));
            return;
        }

        boolean isEditing;
        try {
            isEditing = getIntent().getExtras().getBoolean("editMode", false);
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

        username.setText(profile.getString("username", "unknown"));
        pickDate.updateDate(year, month-1, day);
        phoneNumber.setText(profile.getString("phone_number", "unknown"));
        email.setText(profile.getString("email", "unknown"));
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

        if (!isValidEmail(email.getText().toString())) {
            Toast.makeText(this, "Register Failed, please type a valid email.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isSetUname || !isSetPnum || !isSetMail) {
            Toast.makeText(this, "Register Failed, please complete all required fields.", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        pref.edit().putString("username", username.getText().toString()).putString("DoB", pickDate.getYear() + "-" + (pickDate.getMonth()+1) + "-" + pickDate.getDayOfMonth()).putString("phone_number", phoneNumber.getText().toString()).putString("email", email.getText().toString()).apply();

        Toast.makeText(this, "Register Successful :)", Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, MainPage.class));
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
        pref.edit().putString("username", "Guest").putString("DoB", pickDate.getYear() + "-" + (pickDate.getMonth()+1) + "-" + pickDate.getDayOfMonth()).putString("phone_number", "999").putString("email", "a@b.c").apply();

        Toast.makeText(this, "Play as a Guest :)", Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, MainPage.class));
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
