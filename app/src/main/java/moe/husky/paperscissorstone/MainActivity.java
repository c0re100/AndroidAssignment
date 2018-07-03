package moe.husky.paperscissorstone;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView username, phoneNumber, email;
    DatePicker pickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.txtUsername);
        phoneNumber = findViewById(R.id.txtPhoneNumber);
        email = findViewById(R.id.txtEmail);
        pickDate = findViewById(R.id.pickerDOB);
    }

    public void Submit(View view) {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        pref.edit().putString("username", username.getText().toString()).putString("DoB", pickDate.getYear() + "-" + pickDate.getMonth() + "-" + pickDate.getDayOfMonth()).putString("phone_number", phoneNumber.getText().toString()).putString("email", email.getText().toString()).apply();
        Toast.makeText(this, "Register Successful.", Toast.LENGTH_LONG).show();
    }

    public void Reset(View view) {
        username.setText("");
        Calendar now = Calendar.getInstance();
        pickDate.updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        phoneNumber.setText("");
        email.setText("");
        Toast.makeText(this, "You can type again.", Toast.LENGTH_LONG).show();
    }
}
