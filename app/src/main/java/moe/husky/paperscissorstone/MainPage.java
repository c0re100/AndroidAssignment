package moe.husky.paperscissorstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {
    private Button btnStart, btnInfo, btnLog, btnCheat;
    private TextView txtUsername, txtDoB, txtPhoneNumber, txtEmail;
    private boolean cheatMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnInfo = (Button) findViewById(R.id.btnInfo);
        btnLog = (Button) findViewById(R.id.btnLog);
        btnCheat = (Button) findViewById(R.id.btnCheat);

        txtUsername = (TextView) findViewById(R.id.Username);
        txtDoB = (TextView) findViewById(R.id.DoB);
        txtPhoneNumber = (TextView) findViewById(R.id.PhoneNumber);
        txtEmail = (TextView) findViewById(R.id.Email);

        // fetch the preferences (a.k.a Profile)
        fetchInfo();

        Toast.makeText(this, "Welcome to the Paper Scissor Stone game :)", Toast.LENGTH_LONG).show();
    }

    public void fetchInfo() {
        SharedPreferences profile = getSharedPreferences("profile", MODE_PRIVATE);
        txtUsername.setText("Username: " + profile.getString("username", "Unknown"));
        txtDoB.setText("DoB: " + profile.getString("DoB", "Unknown"));
        txtPhoneNumber.setText("Phone: " + profile.getString("phone_number", "Unknown"));
        txtEmail.setText("E-mail: " + profile.getString("email", "Unknown"));
    }

    public void onBackPressed() {
        finish();
    }

    public void EditProfile(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("EditMode", true);
        startActivity(intent);
    }

    public void cheatMode(View view) {
        cheatMode = true;
        Toast.makeText(this, "Yup, Cheat mode ON...anyway, but I hate(love) cheating :)", Toast.LENGTH_LONG).show();
    }
}
