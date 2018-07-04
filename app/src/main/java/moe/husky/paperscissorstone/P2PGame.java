package moe.husky.paperscissorstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class P2PGame extends AppCompatActivity {
    Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2_pgame);

        btnRetry = findViewById(R.id.btnRetry);
    }
}
