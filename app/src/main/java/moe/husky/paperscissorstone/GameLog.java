package moe.husky.paperscissorstone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameLog extends AppCompatActivity {
    SQLiteDatabase db;
    ListView listGameLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_log);

        listGameLog = findViewById(R.id.listGameLog);

        db = SQLiteDatabase.openDatabase(getApplicationContext().getFilesDir().getPath() + "/Record.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        ArrayList<Player> roundList = new ArrayList<>();

        try (Cursor cursor = db.rawQuery("SELECT * FROM 'GamesLog' ORDER BY gameNo DESC", null)) {
            while (cursor.moveToNext()) {
                Player info = new Player("Player: " + cursor.getString(3), "Opponent: " + cursor.getString(4), cursor.getString(1), cursor.getDouble(2), cursor.getString(8), cursor.getInt(6), cursor.getInt(7));
                roundList.add(info);
            }
            cursor.close();
        } catch (Exception ex) {
            Toast.makeText(this, "Database error!", Toast.LENGTH_LONG).show();
            return;
        }

        PlayerListAdapter adapter = new PlayerListAdapter(this, R.layout.adapter_view_layout, roundList);
        listGameLog.setAdapter(adapter);
    }
}
