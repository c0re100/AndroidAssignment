package moe.husky.paperscissorstone;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView username, DoB, phoneNumber, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.txtUsername);
        DoB = findViewById(R.id.txtDOB);
        phoneNumber = findViewById(R.id.txtPhoneNumber);
        email = findViewById(R.id.txtEmail);
    }

    public void Submit(View view) {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        pref.edit().putString("username", username.getText().toString()).putString("DoB", DoB.getText().toString()).putString("phone_number", phoneNumber.getText().toString()).putString("email", email.getText().toString()).commit();
    }

    public void Reset(View view) {
        username.setText("");
        DoB.setText("");
        phoneNumber.setText("");
        email.setText("");
        Toast.makeText(this, "You can type again.", Toast.LENGTH_LONG).show();
    }
}
