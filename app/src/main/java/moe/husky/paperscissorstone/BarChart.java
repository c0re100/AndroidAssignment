package moe.husky.paperscissorstone;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BarChart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar_chart);
        createFragement();
    }

    public void createFragement() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        final ChartFragment fragment = new ChartFragment();
        ft.replace(R.id.chartFragment, fragment);
        ft.commit();
    }

    public void reset(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = SQLiteDatabase.openDatabase(getApplicationContext().getFilesDir().getPath() + "/Record.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
                db.execSQL("DROP TABLE IF EXISTS GamesLog");
                db.execSQL("CREATE TABLE IF NOT EXISTS GamesLog (gameNo INTEGER primary key AUTOINCREMENT, gamedate TEXT, gametime INTEGER, playerName TEXT, opponentName TEXT, opponentAge INTEGER, yourHand INTEGER, opponentHand INTEGER, status TEXT)");

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                final ChartFragment fragment = new ChartFragment();
                ft.replace(R.id.chartFragment, fragment);
                ft.commit();

                Toast.makeText(BarChart.this, "Okay, all record deleted.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
