package moe.husky.paperscissorstone;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.text.DateFormat.getDateTimeInstance;

public class P2PGame extends AppCompatActivity {
    SQLiteDatabase db;
    private ImageView imgSelf, imgOppo, imgSHand, imgOHand, imgPaper, imgScissors, imgStone;
    private Button btnContinue, btnQuit;
    private TextView selfName, oppoName, Status;
    private boolean isCheating, isEnd;
    private int oppoAge, oppoHand;
    private long startTime;
    private String currentTime;

    Handler tHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            ShowOppoInfo(bundle.getString("name"), bundle.getInt("age"), bundle.getInt("hand"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2pgame);

        imgSelf = findViewById(R.id.imgSelf);
        imgOppo = findViewById(R.id.imgOppo);
        imgSHand = findViewById(R.id.imgSHand);
        imgOHand = findViewById(R.id.imgOHand);
        imgPaper = findViewById(R.id.imgPaper);
        imgScissors = findViewById(R.id.imgScissors);
        imgStone = findViewById(R.id.imgStone);

        btnContinue = findViewById(R.id.btnContinue);
        btnQuit = findViewById(R.id.btnQuit);

        selfName = findViewById(R.id.selfName);
        oppoName = findViewById(R.id.oppoName);
        Status = findViewById(R.id.Status);

        db = SQLiteDatabase.openDatabase(getApplicationContext().getFilesDir().getPath() + "/Record.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        currentTime = df.format(new Date());
        startTime = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getOppoInfo();
            }
        }).start();

        try {
            selfName.setText(getIntent().getExtras().getString("username", ""));
        } catch (Exception ex) {
            selfName.setText("Unknown");
        }

        try {
            isCheating = getIntent().getExtras().getBoolean("cheatMode", false);
        } catch (Exception ex) {
            isCheating = false;
        }

        setOnclick();
    }

    public void setOnclick() {
        imgPaper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isEnd) return;
                imgSHand.setImageResource(R.drawable.paper);
                checkResult(0, oppoHand);
            }
        });

        imgScissors.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isEnd) return;
                imgSHand.setImageResource(R.drawable.scissors);
                checkResult(1, oppoHand);
            }
        });

        imgStone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isEnd) return;
                imgSHand.setImageResource(R.drawable.stone);
                checkResult(2, oppoHand);
            }
        });
    }

    public void getOppoInfo() {
        try {
            URL url = new URL("https://h4vttbs0ai.execute-api.ap-southeast-1.amazonaws.com/ptms");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            InputStream inputStream = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            String result = "";

            while ((line = bufferedReader.readLine()) != null) result += line;
            inputStream.close();

            JSONObject json = new JSONObject(result);    //parse string into a JSON object
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name", json.getString("name"));
            bundle.putInt("age", json.getInt("age"));
            bundle.putInt("hand", json.getInt("hand"));
            msg.setData(bundle);
            tHandler.sendMessage(msg);
        } catch (Exception ex) {
            //Log.v("crash", ex.getMessage());
        }
    }

    public void ShowOppoInfo(String name, int age, int hand) {
        if (isCheating) {
            imgSelf.setImageResource(R.drawable.kenneth);
            imgOppo.setImageResource(R.drawable.husky);
            oppoName.setText("Husky");
            oppoAge = age;
            oppoHand = hand;
        } else {
            oppoName.setText(name);
            oppoAge = age;
            oppoHand = hand;
        }
    }

    public void onBackPressed() {
        MainPage.addQuit();
        finish();
    }

    public void quit(View view) {
        MainPage.addQuit();
        finish();
    }

    public void checkResult(int selfHand, int oppoHand) {
        double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

        switch (oppoHand) {
            case 0:
                imgOHand.setImageResource(R.drawable.paper);
                break;
            case 1:
                imgOHand.setImageResource(R.drawable.scissors);
                break;
            case 2:
                imgOHand.setImageResource(R.drawable.stone);
                break;
        }

        if (selfHand == oppoHand) {
            Status.setText("Draw!");
        }

        if (selfHand < oppoHand || selfHand == 2 && oppoHand == 0) {
            if (isCheating) {
                imgSHand.setImageResource(R.drawable.gun);
                imgOHand.setImageResource(R.drawable.whiteflag);
                Status.setText("Win!");
            } else {
                Status.setText("Lose!");
            }
        }

        if (selfHand == 0 && oppoHand == 2 || selfHand == 1 && oppoHand == 0 || selfHand == 2 && oppoHand == 1) {
            Status.setText("Win!");
        }

        if (isCheating && selfHand < oppoHand || isCheating && selfHand == 2 && oppoHand == 0) {
            db.execSQL("INSERT INTO GamesLog (gameNo, gamedate, gametime, playerName, opponentName, opponentAge, yourHand, opponentHand, status) VALUES (null, '" + currentTime + "', " + elapsedTime + ", '" + selfName.getText().toString() + "', '" + oppoName.getText().toString() + "', " + oppoAge + ", " + 0 + ", " + 2 + ", '" + Status.getText().toString() + "')");
        } else {
            db.execSQL("INSERT INTO GamesLog (gameNo, gamedate, gametime, playerName, opponentName, opponentAge, yourHand, opponentHand, status) VALUES (null, '" + currentTime + "', " + elapsedTime + ", '" + selfName.getText().toString() + "', '" + oppoName.getText().toString() + "', " + oppoAge + ", " + selfHand + ", " + oppoHand + ", '" + Status.getText().toString() + "')");
        }

        btnContinue.setVisibility(View.VISIBLE);
        isEnd = true;
    }

    public void newGame(View view) {
        isEnd = false;
        Status.setText("");
        imgSHand.setImageDrawable(null);
        imgOHand.setImageDrawable(null);
        btnContinue.setVisibility(View.INVISIBLE);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        currentTime = df.format(new Date());
        startTime = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getOppoInfo();
            }
        }).start();
    }
}
