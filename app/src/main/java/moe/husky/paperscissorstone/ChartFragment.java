package moe.husky.paperscissorstone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

public class ChartFragment extends Fragment {
    SQLiteDatabase db;
    int win, lose, draw;

    class Chart extends View {
        public Chart(Context context) {
            super(context);
        }

        String title = "Game Statistics";
        String items[] = {"Win", "Lose", "Draw"};
        int data[] = {win, lose, draw};

        @Override
        public void onDraw(Canvas c) {
            super.onDraw(c);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

            // Make the entire canvas in white
            paint.setColor(Color.WHITE);
            c.drawPaint(paint);

            int rColor[] = new int[3];
            for (int i = 0; i < 3; i++) {
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                rColor[i] = color;
            }

            // Draw the title
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(64);
            paint.setTypeface(Typeface.SERIF);
            c.drawText(title, 20, 70, paint);

            paint.setTextSize(50);

            int total = win + lose + draw;

            paint.setColor(Color.GRAY);
            c.drawRect(185, 200, 190, 660, paint);
            for (int i = items.length - 1; i >= 0; i--) {
                paint.setColor(rColor[i]);
                float offset = 80;
                float left = 190;
                float top = 180 + offset * i;
                float right = 190 + data[i] * total / 10;
                float bottom = 160 + offset * i;

                if (right > 700) right = 700;

                if (i == 0)
                    c.drawRect(left, 220, right, 320, paint);
                else if (i == 1)
                    c.drawRect(left, 380, right, 480, paint);
                else
                    c.drawRect(left, 540, right, 640, paint);

                c.drawText(items[i], 20, top + bottom - 50, paint);
                c.drawText(Integer.toString(data[i]), right + 20, top + bottom - 50, paint);
            }

            paint.setColor(Color.RED);
            c.drawText("Win rate: " + Double.toString((win * total) / 100.0) + "%", 20, 900, paint);
            paint.setColor(Color.BLUE);
            c.drawText("Total round: " + total, 20, 1000, paint);
        }
    }

    public int getWin() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM 'GamesLog' WHERE status='Win!'", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getLose() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM 'GamesLog' WHERE status='Lose!'", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getDraw() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM 'GamesLog' WHERE status='Draw!'", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = SQLiteDatabase.openDatabase(getActivity().getFilesDir().getPath() + "/Record.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        win = getWin();
        lose = getLose();
        draw = getDraw();

        return new Chart(getActivity());
    }
}
