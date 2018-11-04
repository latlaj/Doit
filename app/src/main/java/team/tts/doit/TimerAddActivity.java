package team.tts.doit;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TimerAddActivity extends AppCompatActivity {
    List<Timer> timerList=new ArrayList<>();
    TextView textView;
    ListView listView;
    ListViewAdapterForTimer lvaft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_add);
        textView=findViewById(R.id.textViewTimer);
        listView=findViewById(R.id.listViewTimer);
        lvaft=new ListViewAdapterForTimer();
        listView.setAdapter(lvaft);
        Cursor c = MyDatabaseHelper.check("timer");
        if (c.getCount() != 0) {
            String[] strings={"id", "title", "content"};
            String order="id";
            Cursor cursor = MyDatabaseHelper.check("timer",strings,order);
            SimpleDateFormat sdFormatter = new SimpleDateFormat(getString(R.string.date_format));
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = sdFormatter.format(cursor.getLong(cursor.getColumnIndex("time")));
                String ID = cursor.getString(cursor.getColumnIndex("id"));
                timerList.add(new Timer(ID, title, content,1));
            }
            cursor.close();
        }
        c.close();
    }
}
